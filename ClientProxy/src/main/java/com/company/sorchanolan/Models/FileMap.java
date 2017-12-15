package com.company.sorchanolan.Models;

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

  public void setBody(String body) {
    this.body = body;
  }
}
