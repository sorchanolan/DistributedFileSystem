package com.company.sorchanolan.Models;

import java.util.ArrayList;
import java.util.List;

public class FileServerData {
  private String ipAddress;
  private int port;
  private List<String> files;

  public FileServerData() {
    this.ipAddress = "";
    this.port = -1;
    this.files = new ArrayList<>();
  }

  public FileServerData(String ipAddress, int port, List<String> files) {
    this.ipAddress = ipAddress;
    this.port = port;
    this.files = files;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public int getPort() {
    return port;
  }

  public List<String> getFiles() {
    return files;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setFiles(List<String> files) {
    this.files = files;
  }
}
