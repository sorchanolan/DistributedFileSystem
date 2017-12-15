package com.company.sorchanolan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client implements Runnable {
  private Thread thread = null;
  private ServerSocket welcomeSocket = null;
  private ClientUpdatesThread updatesThread = null;
  public static String directoryIpAddress;
  public static int directoryPort;
  public static int port;
  public static File currentFile = null;
  private Editor editor = null;

  public static void main(String[] args) throws Exception {
    directoryIpAddress = args[0];
    directoryPort = Integer.valueOf(args[1]);
    port = Integer.valueOf(args[2]);
    new Client();
    runShutdownHook();
  }

  public Client() throws Exception {
    editor = new Editor();
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
        updatesThread = new ClientUpdatesThread(conSocket, editor);
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
            requestManager.killClient();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });
  }
}
