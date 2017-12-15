package com.company.sorchanolan;

public class Request {
  private boolean writeCommand;
  private boolean writeAccess;
  private String fileName;
  private String body;
  private int fileId;

  public Request() {
    this.writeCommand = false;
    this.writeAccess = false;
    this.fileName = "";
    this.body = "";
    this.fileId = -1;
  }

  public Request(boolean command, boolean access, String fileName, String body, int fileId) {
    this.writeCommand = command;
    this.writeAccess = access;
    this.fileName = fileName;
    this.body = body;
    this.fileId = fileId;
  }

  public boolean getWriteCommand() {
    return writeCommand;
  }

  public boolean getWriteAccess() {
    return writeAccess;
  }

  public String getFileName() {
    return fileName;
  }

  public String getBody() {
    return body;
  }

  public int getFileId() {
    return fileId;
  }

  public void setWriteCommand(boolean writeCommand) {
    this.writeCommand = writeCommand;
  }

  public void setWriteAccess(boolean writeAccess) {
    this.writeAccess = writeAccess;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public void setFileId(int fileId) {
    this.fileId = fileId;
  }
}
