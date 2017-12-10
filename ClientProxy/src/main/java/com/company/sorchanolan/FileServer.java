package com.company.sorchanolan;

import java.util.List;

public class FileServer {
  private String ipAddress;
  private int port;

  public FileServer() {
    this.ipAddress = "";
    this.port = -1;
  }

  public FileServer(String ipAddress, int port) {
    this.ipAddress = ipAddress;
    this.port = port;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public int getPort() {
    return port;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public void setPort(int port) {
    this.port = port;
  }
}