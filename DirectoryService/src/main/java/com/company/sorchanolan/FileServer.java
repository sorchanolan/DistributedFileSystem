package com.company.sorchanolan;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hubspot.rosetta.annotations.RosettaNaming;

import java.util.ArrayList;
import java.util.List;

@RosettaNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileServer {
  private int id;
  private String ipAddress;
  private int port;
  private List<String> files;
  private boolean running;

  public FileServer() {
    this.id = -1;
    this.ipAddress = "";
    this.port = -1;
    this.files = new ArrayList<>();
    this.running = false;
  }

  public FileServer(int id, String ipAddress, int port, List<String> files, boolean running) {
    this.id = id;
    this.ipAddress = ipAddress;
    this.port = port;
    this.files = files;
    this.running = running;
  }

  public int getId() {
    return id;
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

  public void setId(int id) {
    this.id = id;
  }

  public boolean isRunning() {
    return running;
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

  public void setRunning(boolean running) {
    this.running = running;
  }
}
