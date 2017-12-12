package com.company.sorchanolan;

public class File {
  private int id;
  private String fileName;

  public File() {
    this.id = -1;
    this.fileName = "";
  }

  public File(int id, String fileName) {
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
