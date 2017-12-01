package com.company.sorchanolan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DirectoryService implements Runnable {
  private Thread thread = null;
  private ServerSocket welcomeSocket = null;
  private DirectoryServiceThread client = null;

  public static void main(String[] argv) {
    int port = Integer.parseInt(argv[0]);
    new DirectoryService(port);
  }

  public DirectoryService(int port) {
    System.out.println("Begin Comms");
    try {
      welcomeSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.out.println(e);
    }

    if (thread == null)
    {
      thread = new Thread(this);
      thread.start();
    }
  }

  @Override
  public void run() {
    while (thread != null) {
      try {
        Socket conSocket = welcomeSocket.accept();
        client = new DirectoryServiceThread(this, conSocket);
        client.start();
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }
}
