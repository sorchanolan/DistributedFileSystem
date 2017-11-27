package com.company.sorchanolan;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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

    try {
      String clientMessage = inFromClient.readLine();
      System.out.println(clientMessage);
      processRequest(clientMessage);
    } catch (IOException e) {
      System.out.println(e);
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

  private void processRequest(String clientMessage) {
    try {
      Request request = mapper.readValue(clientMessage, Request.class);
    } catch (IOException e) {
      System.out.println("Cannot map input to request object" + e);
    }
  }
}
