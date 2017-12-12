package com.company.sorchanolan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class FileServer implements Runnable {
  public static String directoryIpAddress;
  public static int directoryPort;
  public static int port;
  private Thread thread = null;
  private ServerSocket welcomeSocket = null;
  private FileServerThread client = null;
  public List<FileMap> files = Collections.synchronizedList(new ArrayList<>());

  public static void main(String[] argv) throws Exception {
    port = Integer.parseInt(argv[0]);
    directoryIpAddress = argv[1];
    directoryPort = Integer.parseInt(argv[2]);
    new FileServer();
  }

  public FileServer() throws Exception {
    System.out.println("Begin Comms");
    new UpdateDirectory(this);
    welcomeSocket = new ServerSocket(port);

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
