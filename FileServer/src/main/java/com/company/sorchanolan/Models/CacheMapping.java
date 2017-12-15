package com.company.sorchanolan.Models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hubspot.rosetta.annotations.RosettaNaming;

@RosettaNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CacheMapping {
  private int userId;
  private int fileId;

  public CacheMapping() {
    this.userId = -1;
    this.fileId = -1;
  }

  public CacheMapping(int userId, int fileId) {
    this.userId = userId;
    this.fileId = fileId;
  }

  public int getUserId() {
    return userId;
  }

  public int getFileId() {
    return fileId;
  }
}
