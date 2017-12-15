package com.company.sorchanolan;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hubspot.rosetta.annotations.RosettaNaming;

@RosettaNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileLock {
  private int id;
  private int fileId;
  private boolean status;
  private long validUntil;
  private int userId;

  public FileLock() {
    this.id = -1;
    this.fileId = -1;
    this.status = false;
    this.validUntil = -1;
    this.userId = -1;
  }

  public FileLock(int id, int fileId, boolean status, long validUntil, int userId) {
    this.id = id;
    this.fileId = fileId;
    this.status = status;
    this.validUntil = validUntil;
    this.userId = userId;
  }

  public int getId() {
    return id;
  }

  public int getFileId() {
    return fileId;
  }

  public boolean isStatus() {
    return status;
  }

  public long getValidUntil() {
    return validUntil;
  }

  public int getUserId() {
    return userId;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public void setValidUntil(long validUntil) {
    this.validUntil = validUntil;
  }
}
