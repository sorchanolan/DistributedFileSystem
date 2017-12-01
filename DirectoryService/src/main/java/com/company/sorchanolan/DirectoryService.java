package com.company.sorchanolan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DirectoryService implements Runnable {
  private Thread thread = null;
  private ServerSocket fileServerSocket = null;
  private ServerSocket clientSocket = null;
  private FileServerThread fileServerThread = null;
  private ClientThread clientThread = null;

  public static void main(String[] argv) {
    int fileServerPort = Integer.parseInt(argv[0]);
    int clientPort = Integer.parseInt(argv[1]);
    new DirectoryService(fileServerPort, clientPort);
  }

  public DirectoryService(int fileServerPort, int clientPort) {
    System.out.println("Begin Comms");
    try {
      fileServerSocket = new ServerSocket(fileServerPort);
      clientSocket = new ServerSocket(clientPort);
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
        Socket fileSocket = fileServerSocket.accept();
        fileServerThread = new FileServerThread(this, fileSocket);
        fileServerThread.start();
      } catch (IOException e) {
        System.out.println(e);
      }

      try {
        Socket clientSideSocket = clientSocket.accept();
        clientThread = new ClientThread(this, clientSideSocket);
        clientThread.start();
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }
}
