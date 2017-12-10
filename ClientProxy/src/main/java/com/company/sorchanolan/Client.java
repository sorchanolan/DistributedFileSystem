package com.company.sorchanolan;

public class Client {
  public static String ipAddress;
  public static int port;

  public static void main(String[] args) throws Exception {
    ipAddress = args[0];
    port = Integer.valueOf(args[1]);
    new Editor();
  }
}
