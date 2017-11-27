package com.company.sorchanolan;

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

  public FileServerThread(FileServer server, Socket socket) {
    this.server = server;
    this.socket = socket;
    port = socket.getPort();
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
    String inputs[] = clientMessage.split(" ", 2);
    String command = inputs[0];
    String fileName = inputs[1];
  }
}
