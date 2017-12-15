package com.company.sorchanolan;

import org.skife.jdbi.v2.DBI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServerMain implements Runnable {
  public static String directoryIpAddress;
  public static int directoryPort;
  public static int port;
  private Thread thread = null;
  private ServerSocket welcomeSocket = null;
  private FileServerThread client = null;
  private Dao dao = null;

  public static void main(String[] argv) throws Exception {
    port = Integer.parseInt(argv[0]);
    directoryIpAddress = argv[1];
    directoryPort = Integer.parseInt(argv[2]);
    new FileServerMain();
  }

  public FileServerMain() throws Exception {
    DBI dbi = new DBI("jdbc:mysql://localhost:3306/FileServerMain?autoReconnect=true&useSSL=false",
        "sorcha", "Nolan123");
    dao = dbi.onDemand(Dao.class);

    System.out.println("Begin Comms");
    new UpdateDirectory(dao);
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
        client = new FileServerThread(dao, conSocket);
        client.start();
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }
}
