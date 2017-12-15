package com.company.sorchanolan;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.company.sorchanolan.FileServer.port;

public class FileServerThread extends Thread implements Runnable {
  private volatile boolean running = true;
  private Socket socket = null;
  private BufferedReader inFromClient = null;
  private DataOutputStream outToClient = null;
  private ObjectMapper mapper = new ObjectMapper();
  private Dao dao = null;

  public FileServerThread(Dao dao, Socket socket) {
    this.dao = dao;
    this.socket = socket;
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  public void run() {
    System.out.println("Server Thread " + port + " running.");
    openComms();

    while (running) {
      try {
        String clientMessage = inFromClient.readLine();
        if (clientMessage != null) {
          processRequest(clientMessage);
        }
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  private void openComms() {
    try {
      inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      outToClient = new DataOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private void processRequest(String clientMessage) throws Exception {
    Request request = mapper.readValue(clientMessage, Request.class);
    if (request.getWriteCommand()) {
      processWriteRequest(request);
    } else {
      processReadRequest(request);
    }
  }

  private void processReadRequest(Request request) throws Exception {
    String body = "";
    String path = "../Files_"  + port + "/" + request.getFileName();
    if (Files.exists(Paths.get(path))) {
      body = String.join("\n", Files.readAllLines(Paths.get(path)));
    } else {
      Path directoryPath = Paths.get("../Files_"  + port + "/");
      if (!Files.exists(directoryPath)) {
        Files.createDirectory(directoryPath);
      }
      Path filePath = Paths.get(path);
      Files.createFile(filePath);
      new UpdateDirectory(dao);
    }

    List<Integer> fileIds = dao.getFile(request.getFileName());
    if (!fileIds.isEmpty()) {
      request.setFileId(fileIds.get(0));
    }

    request.setBody(body);
    System.out.println(mapper.writeValueAsString(request));
    outToClient.writeBytes(mapper.writeValueAsString(request) + "\n");
  }

  private void processWriteRequest(Request request) throws Exception {
    File file = new File("../Files_"  + port + "/" + request.getFileName());
    FileWriter fileWriter = new FileWriter(file, false);
    fileWriter.write(request.getBody());
    fileWriter.close();

    outToClient.writeBytes(mapper.writeValueAsString(request.withAccess(false)) + "\n");
  }
}
