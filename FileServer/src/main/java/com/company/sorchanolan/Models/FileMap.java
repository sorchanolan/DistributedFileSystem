package com.company.sorchanolan.Models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hubspot.rosetta.annotations.RosettaNaming;

@RosettaNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileMap {
  private int id;
  private String fileName;

  public FileMap() {
    this.id = -1;
    this.fileName = "";
  }

  public FileMap(int id, String fileName) {
    this.id = id;
    this.fileName = fileName;
  }

  public String getFileName() {
    return fileName;
  }

  public int getId() {
    return id;
  }
}
