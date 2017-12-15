package com.company.sorchanolan.Models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hubspot.rosetta.annotations.RosettaNaming;

@RosettaNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileMap {
  private int id;
  private String fileName;
  private String body;

  public FileMap() {
    this.id = -1;
    this.fileName = "";
    this.body = "";
  }

  public FileMap(int id, String fileName, String body) {
    this.id = id;
    this.fileName = fileName;
    this.body = body;
  }

  public String getFileName() {
    return fileName;
  }

  public int getId() {
    return id;
  }

  public String getBody() {
    return body;
  }
}
