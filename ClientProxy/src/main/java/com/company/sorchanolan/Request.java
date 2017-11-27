package com.company.sorchanolan;

public class Request {
  private String command;
  private String access;
  private String fileName;
  private String body;

  public Request() {
    this.command = "";
    this.access = "";
    this.fileName = "";
    this.body = "";
  }

  public Request(String command, String access, String fileName, String body) {
    this.command = command;
    this.access = access;
    this.fileName = fileName;
    this.body = body;
  }

  public String getCommand() {
    return command;
  }

  public String getAccess() {
    return access;
  }

  public String getFileName() {
    return fileName;
  }

  public String getBody() {
    return body;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public void setAccess(String access) {
    this.access = access;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
