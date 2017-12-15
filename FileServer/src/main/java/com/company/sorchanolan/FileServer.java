package com.company.sorchanolan;

import org.skife.jdbi.v2.DBI;

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
  private Dao dao = null;
  public List<FileMap> files = Collections.synchronizedList(new ArrayList<>());

  public static void main(String[] argv) throws Exception {
    port = Integer.parseInt(argv[0]);
    directoryIpAddress = argv[1];
    directoryPort = Integer.parseInt(argv[2]);
    new FileServer();
  }

  public FileServer() throws Exception {
    DBI dbi = new DBI("jdbc:mysql://localhost:3306/DirectoryService?autoReconnect=true&useSSL=false",
        "sorcha", "Nolan123");
    dao = dbi.onDemand(Dao.class);

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
