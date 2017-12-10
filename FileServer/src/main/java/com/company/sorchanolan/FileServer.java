package com.company.sorchanolan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer implements Runnable {
  public static String directoryIpAddress;
  public static int directoryPort;
  public static int port;
  private Thread thread = null;
  private ServerSocket welcomeSocket = null;
  private FileServerThread client = null;
  private DirectoryCommunication directoryCommunication = null;

  public static void main(String[] argv) throws Exception {
    port = Integer.parseInt(argv[0]);
    directoryIpAddress = argv[1];
    directoryPort = Integer.parseInt(argv[2]);
    new FileServer();
  }

  public FileServer() throws Exception {
    System.out.println("Begin Comms");
    directoryCommunication = new DirectoryCommunication();

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
        client = new FileServerThread(this, conSocket);
        client.start();
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }
}
