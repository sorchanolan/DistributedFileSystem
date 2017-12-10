package com.company.sorchanolan;

import org.skife.jdbi.v2.DBI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DirectoryService implements Runnable {
  private Thread thread = null;
  private ServerSocket fileServerSocket = null;
  private ServerSocket clientSocket = null;
  private FileServerThread fileServerThread = null;
  private ClientThread clientThread = null;
  private DirectoryDao dao = null;
  private int idCounter = 1;

  public static void main(String[] argv) {
    int clientPort = Integer.parseInt(argv[0]);
    int fileServerPort = Integer.parseInt(argv[1]);
    new DirectoryService(clientPort, fileServerPort);
  }

  public DirectoryService(int clientPort, int fileServerPort) {
    System.out.println("Begin Comms");

    DBI dbi = new DBI("jdbc:mysql://localhost:3306/DirectoryService?autoReconnect=true&useSSL=false",
        "sorcha", "Nolan123");
    dao = dbi.onDemand(DirectoryDao.class);

    try {
      clientSocket = new ServerSocket(clientPort);
      //fileServerSocket = new ServerSocket(fileServerPort);
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
        Socket clientSideSocket = clientSocket.accept();
        clientThread = new ClientThread(this, clientSideSocket, dao);
        clientThread.start();
      } catch (IOException e) {
        System.out.println(e);
      }

//      try {
//        Socket fileSocket = fileServerSocket.accept();
//        fileServerThread = new FileServerThread(this, fileSocket);
//        fileServerThread.start();
//      } catch (IOException e) {
//        System.out.println(e);
//      }
    }
  }

  public synchronized int createID() {
    return idCounter++;
  }
}
