package com.company.sorchanolan;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class RequestThread extends Thread implements Runnable {
  private volatile boolean running = true;
  private Socket clientSocket = null;
  private DirectoryService server = null;
  private int port = -1;
  private BufferedReader inFromClient = null;
  private DataOutputStream outToClient = null;
  private DirectoryDao dao = null;
  private ObjectMapper mapper = null;

  public RequestThread(DirectoryService server, Socket socket, DirectoryDao dao) {
    this.server = server;
    this.clientSocket = socket;
    this.dao = dao;
    port = socket.getPort();
    mapper = new ObjectMapper();
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
      FileServer fileServer = dao.getRandomFileServer();
      outToClient.writeBytes(mapper.writeValueAsString(fileServer) + "\n");
      return;
    }

    if (message.startsWith("listfiles")) {
      List<String> fileNames = dao.getAllFileNames();
      outToClient.writeBytes(mapper.writeValueAsString(fileNames) + "\n");
      return;
    }

    if (message.startsWith("openfile")) {
      List<FileServer> servers = dao.getServersHoldingFile(message.replace("openfile", ""));
      if (!servers.isEmpty()) {
        outToClient.writeBytes(mapper.writeValueAsString(servers.get(0)) + "\n");
      }
      return;
    }

    if (message.startsWith("fileserver")) {
      message = message.replace("fileserver", "");
      FileServer fileServer = mapper.readValue(message, FileServer.class);
      List<String> fileNames = dao.getAllFileNames();

      List<FileServer> maybeFileServer = dao.getFileServer(fileServer.getIpAddress(), fileServer.getPort());
      if (maybeFileServer.isEmpty()) {
        fileServer.setId(server.createID());
        dao.addNewServer(fileServer);
      } else {
        fileServer.setId(maybeFileServer.get(0).getId());
      }

      for (String fileName : fileServer.getFiles()) {
        if (!fileNames.contains(fileName)) {
          int fileId = server.createID();
          dao.addNewFile(fileId, fileName);
          dao.addNewMapping(fileServer.getId(), fileId);
          fileNames.add(fileName);
        }
      }
    }
  }
}
