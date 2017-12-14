package com.company.sorchanolan;

public class Client {
  public static String ipAddress;
  public static int port;
  public static File currentFile;

  public static void main(String[] args) throws Exception {
    ipAddress = args[0];
    port = Integer.valueOf(args[1]);
    new Editor();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        System.out.println("Running Shutdown Hook");
        try {
          RequestManager requestManager = new RequestManager();
          requestManager.saveFile(currentFile.getFileName(), currentFile.getBody(), currentFile.getId());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}
