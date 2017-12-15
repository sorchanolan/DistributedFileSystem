package com.company.sorchanolan;

import org.skife.jdbi.v2.DBI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DirectoryServiceMain implements Runnable {
  private Thread thread = null;
  private ServerSocket socket = null;
  private RequestThread requestThread = null;
  private DirectoryDao dao = null;
  private int idCounter = 1;
  private int LOCK_POLLING_INTERVAL = 1;
  public static int port;
  public Map<Integer, Socket> userIdToSocketMapping = Collections.synchronizedMap(new HashMap<Integer, Socket>());

  public static void main(String[] argv) {
    port = Integer.parseInt(argv[0]);
    new DirectoryServiceMain();
  }

  public DirectoryServiceMain() {
    System.out.println("Begin Comms");

    DBI dbi = new DBI("jdbc:mysql://localhost:3306/DirectoryService?autoReconnect=true&useSSL=false",
        "sorcha", "Nolan123");
    dao = dbi.onDemand(DirectoryDao.class);
    idCounter = dao.getCurrentIdCounter() + 1;
    dao.setAllFileServersToNotRunning();

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(new LockingServicePolling(this), 0, LOCK_POLLING_INTERVAL, TimeUnit.MINUTES);

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
