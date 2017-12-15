package com.company.sorchanolan;

import com.company.sorchanolan.Models.FileMap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientMain implements Runnable {
  private Thread thread = null;
  private ServerSocket welcomeSocket = null;
  private ClientUpdatesThread updatesThread = null;
  public static String directoryIpAddress;
  public static int directoryPort;
  public static int port;
  public static FileMap currentFile = null;
  private Editor editor = null;
  private static RequestManager requestManager = null;

  public static void main(String[] args) throws Exception {
    directoryIpAddress = args[0];
    directoryPort = Integer.valueOf(args[1]);
    port = Integer.valueOf(args[2]);
    new ClientMain();
    runShutdownHook();
  }

  public ClientMain() throws Exception {
    requestManager = new RequestManager();
    editor = new Editor(requestManager);
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
        try {
          requestManager.killClient();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}
