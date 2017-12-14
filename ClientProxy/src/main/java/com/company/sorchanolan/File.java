package com.company.sorchanolan;

public class File {
  private int id;
  private String fileName;
  private String body;

  public File() {
    this.id = -1;
    this.fileName = "";
    this.body = "";
  }

  public File(int id, String fileName, String body) {
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
