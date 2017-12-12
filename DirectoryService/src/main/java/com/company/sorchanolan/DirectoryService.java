package com.company.sorchanolan;

import org.skife.jdbi.v2.DBI;

import java.io.IOException;
import java.net.ServerSocket;

public class DirectoryService implements Runnable {
  private Thread thread = null;
  private ServerSocket socket = null;
  private RequestThread requestThread = null;
  private DirectoryDao dao = null;
  private int idCounter = 1;

  public static void main(String[] argv) {
    int port = Integer.parseInt(argv[0]);
    new DirectoryService(port);
  }

  public DirectoryService(int port) {
    System.out.println("Begin Comms");

    DBI dbi = new DBI("jdbc:mysql://localhost:3306/DirectoryService?autoReconnect=true&useSSL=false",
        "sorcha", "Nolan123");
    dao = dbi.onDemand(DirectoryDao.class);
    idCounter = dao.getCurrentIdCounter() + 1;
    dao.setAllFileServersToNotRunning();

    try {
      socket = new ServerSocket(port);
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
        requestThread = new RequestThread(this, socket.accept(), dao);
        requestThread.start();
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }

  public synchronized int createID() {
    return idCounter++;
  }
}
