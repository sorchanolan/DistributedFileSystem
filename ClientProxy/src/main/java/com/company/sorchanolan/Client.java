package com.company.sorchanolan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client implements Runnable {
  private Thread thread = null;
  private ServerSocket welcomeSocket = null;
  private ClientUpdatesThread updatesThread = null;
  public static String ipAddress;
  public static int port;
  public static File currentFile = null;

  public static void main(String[] args) throws Exception {
    ipAddress = args[0];
    port = Integer.valueOf(args[1]);
    new Editor();
    new Client();
    runShutdownHook();
  }

  public Client() throws Exception {
    System.out.println("Begin Comms");
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
        updatesThread = new ClientUpdatesThread(conSocket);
        updatesThread.start();
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }

  private static void runShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        System.out.println("Running Shutdown Hook");
        if (currentFile != null) {
          try {
            RequestManager requestManager = new RequestManager();
            requestManager.saveFile(currentFile.getFileName(), currentFile.getBody(), currentFile.getId());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });
  }
}
