package com.company.sorchanolan;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread implements Runnable {
  private volatile boolean running = true;
  private Socket clientSocket = null;
  private DirectoryService server = null;
  private int port = -1;
  private BufferedReader inFromClient = null;
  private DataOutputStream outToClient = null;
  private DirectoryDao dao = null;
  private ObjectMapper mapper = null;

  public ClientThread(DirectoryService server, Socket socket, DirectoryDao dao) {
    this.server = server;
    this.clientSocket = socket;
    this.dao = dao;
    port = socket.getPort();
    mapper = new ObjectMapper();
  }

  public void run() {
    System.out.println("Server Thread " + port + " running.");
    openClientComms();

    while (running = true) {
      try {
        String clientMessage = inFromClient.readLine();
        processRequest(clientMessage);
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  private void openClientComms() {
    try {
      inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      outToClient = new DataOutputStream(clientSocket.getOutputStream());
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private void processRequest(String message) {
    System.out.println(message);
    if (message.startsWith("newfile")) {
      FileServer server = dao.getRandomFileServer();
      try {
        outToClient.writeBytes(mapper.writeValueAsString(server) + "\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
