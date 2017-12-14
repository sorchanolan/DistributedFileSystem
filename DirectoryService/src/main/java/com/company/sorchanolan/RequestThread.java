package com.company.sorchanolan;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.sorchanolan.DirectoryService.port;

public class RequestThread extends Thread implements Runnable {
  private volatile boolean running = true;
  private Socket clientSocket = null;
  private DirectoryService server = null;
  private BufferedReader inFromClient = null;
  private DataOutputStream outToClient = null;
  private DirectoryDao dao = null;
  private ObjectMapper mapper = null;
  private LockingService lockingService = null;
  private int userId;

  public RequestThread(DirectoryService server, Socket socket, DirectoryDao dao) {
    this.server = server;
    this.clientSocket = socket;
    this.dao = dao;
    mapper = new ObjectMapper();
    this.lockingService = new LockingService();
    this.userId = server.createID();
  }

  public void run() {
    System.out.println("Server Thread " + port + " running.");
    openComms();

    while (running = true) {
      try {
        String message = inFromClient.readLine();
        if (message != null) {
          processRequest(message);
        }
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  private void openComms() {
    try {
      inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      outToClient = new DataOutputStream(clientSocket.getOutputStream());
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private void processRequest(String message) throws Exception {
    System.out.println(message);
    if (message.startsWith("newfile")) {
      newFile(message.replace("newfile", ""));
      return;
    }

    if (message.startsWith("listfiles")) {
      listFiles();
      return;
    }

    if (message.startsWith("openfile")) {
      openFile(message.replace("openfile", ""));
      return;
    }

    if (message.startsWith("fileserver")) {
      fileServerRequest(message.replace("fileserver", ""));
      return;
    }

    if (message.startsWith("lock")) {
      outToClient.writeBytes(lockingService.lock(message.replace("lock", ""), server.createID(), userId));
      return;
    }

    if (message.startsWith("unlock")) {
      lockingService.unlock(Integer.parseInt(message.replace("unlock", "")), userId);
    }
  }

  private void newFile(String message) throws Exception {
    FileServer fileServer;
    List<Integer> serversWithFileName = dao.getServersHoldingFile(message).stream()
        .map(FileServer::getId)
        .collect(Collectors.toList());
    if (!serversWithFileName.isEmpty()) {
      fileServer = dao.getRandomFileServer(serversWithFileName);
    } else {
      fileServer = dao.getRandomFileServer();
    }
    outToClient.writeBytes(mapper.writeValueAsString(fileServer) + "\n");
  }

  private void listFiles() throws Exception {
    List<String> fileNames = dao.getAllFileNamesFromRunningServers();
    outToClient.writeBytes(mapper.writeValueAsString(fileNames) + "\n");
  }

  private void openFile(String message) throws Exception {
    List<FileServer> servers = dao.getServersHoldingFile(message);
    if (!servers.isEmpty()) {
      outToClient.writeBytes(mapper.writeValueAsString(servers.get(0)) + "\n");
    }
  }

  private void fileServerRequest(String message) throws Exception {
    FileServer fileServer = mapper.readValue(message, FileServer.class);
    List<ServerFileMapping> serverFileMappings = dao.getServerFileMappings();

    List<FileServer> maybeFileServer = dao.getFileServer(fileServer.getIpAddress(), fileServer.getPort());
    if (maybeFileServer.isEmpty()) {
      fileServer.setId(server.createID());
      fileServer.setRunning(true);
      dao.addNewServer(fileServer);
    } else {
      fileServer.setId(maybeFileServer.get(0).getId());
      dao.setFileServerToRunning(maybeFileServer.get(0).getId());
    }

    for (String fileName : fileServer.getFiles()) {
      if (serverFileMappings.stream()
          .noneMatch(mapping -> mapping.getFileName().equals(fileName)
              && mapping.getPort() == fileServer.getPort()
              && mapping.getIpAddress().equals(fileServer.getIpAddress()))) {
        int fileId = server.createID();
        dao.addNewFile(fileId, fileName);
        dao.addNewMapping(fileServer.getId(), fileId);
        serverFileMappings.add(new ServerFileMapping(fileName, fileServer.getPort(), fileServer.getIpAddress()));
      }
    }

    if (!fileServer.getFiles().isEmpty()) {
      System.out.println(mapper.writeValueAsString(dao.getFiles(fileServer.getFiles())));
      outToClient.writeBytes(mapper.writeValueAsString(dao.getFiles(fileServer.getFiles())) + "\n");
    } else {
      outToClient.writeBytes(mapper.writeValueAsString(new ArrayList()) + "\n");
    }
    System.out.println("Server online");
  }
}
