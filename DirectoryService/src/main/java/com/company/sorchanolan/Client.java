package com.company.sorchanolan;

public class Client {
  private int id;
  private int port;
  private String ipAddress;
  private boolean running;

  public Client() {
    this.id = -1;
    this.port = -1;
    this.ipAddress = "";
    this.running = false;
  }

  public Client(int id, int port, String ipAddress, boolean running) {
    this.id = id;
    this.port = port;
    this.ipAddress = ipAddress;
    this.running = running;
  }

  public int getId() {
    return id;
  }

  public int getPort() {
    return port;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public boolean isRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }
}
