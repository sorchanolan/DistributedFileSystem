package com.company.sorchanolan;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientUpdatesThread extends Thread implements Runnable {
  private volatile boolean running = true;
  private Socket socket = null;
  private BufferedReader inFromServer = null;
  private DataOutputStream outToServer = null;
  private ObjectMapper mapper = new ObjectMapper();

  public ClientUpdatesThread(Socket socket) {
    this.socket = socket;
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  @Override
  public void run() {
    openComms();

    while (running) {
      try {
        String message = inFromServer.readLine();
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
      inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      outToServer = new DataOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private void processRequest(String message) {

  }
}
