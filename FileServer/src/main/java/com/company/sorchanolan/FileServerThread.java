package com.company.sorchanolan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileServerThread extends Thread implements Runnable {
  private volatile boolean running = true;
  private Socket socket = null;
  private FileServer server = null;
  private int port = -1;
  private BufferedReader inFromClient = null;
  private DataOutputStream outToClient = null;
  private ObjectMapper mapper = new ObjectMapper();

  public FileServerThread(FileServer server, Socket socket) {
    this.server = server;
    this.socket = socket;
    port = socket.getPort();
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
    Request request = new Request();
    try {
      request = mapper.readValue(clientMessage, Request.class);
    } catch (IOException e) {
      System.out.println("Cannot map input to request object" + e);
    }

    if (request.getWriteCommand()) {
      processWriteRequest(request);
    } else {
      processReadRequest(request);
    }
  }

  private void processReadRequest(Request request) throws Exception {
    String body = "";
    if (Files.exists(Paths.get("Files/" + request.getFileName()))) {
      try {
        body = String.join("\n", Files.readAllLines(Paths.get("Files/" + request.getFileName())));
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      Path path = Paths.get("Files/" + request.getFileName());
      Files.createFile(path);
      DirectoryCommunication directoryCommunication = new DirectoryCommunication();
    }

    request.setBody(body);
    try {
      try {
        System.out.println(mapper.writeValueAsString(request));
        outToClient.writeBytes(mapper.writeValueAsString(request) + "\n");
      } catch (JsonProcessingException jpe) {
        System.out.println("Cannot write json request as string: " + jpe);
      }
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private void processWriteRequest(Request request) throws Exception {
    File file = new File("Files/" + request.getFileName());
    FileWriter fileWriter = new FileWriter(file, false);
    fileWriter.write(request.getBody());
    fileWriter.close();

    outToClient.writeBytes(mapper.writeValueAsString(request.withAccess(false)) + "\n");
  }
}
