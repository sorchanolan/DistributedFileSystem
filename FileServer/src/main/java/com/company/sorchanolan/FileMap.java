package com.company.sorchanolan;

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
