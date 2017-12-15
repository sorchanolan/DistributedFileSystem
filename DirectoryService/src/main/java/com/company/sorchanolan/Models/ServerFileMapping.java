package com.company.sorchanolan.Models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hubspot.rosetta.annotations.RosettaNaming;

@RosettaNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ServerFileMapping {
  private String fileName;
  private int port;
  private String ipAddress;

  public ServerFileMapping() {
    this.fileName = "";
    this.port = -1;
    this.ipAddress = "";
  }

  public ServerFileMapping(String fileName, int port, String ipAddress) {
    this.fileName = fileName;
    this.port = port;
    this.ipAddress = ipAddress;
  }

  public String getFileName() {
    return fileName;
  }

  public int getPort() {
    return port;
  }

  public String getIpAddress() {
    return ipAddress;
  }
}
