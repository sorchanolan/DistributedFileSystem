package com.company.sorchanolan;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hubspot.rosetta.annotations.RosettaNaming;

@RosettaNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LockQueueEntry {
  private int fileId;
  private int userId;
  private long timestamp;

  public LockQueueEntry() {
    this.fileId = -1;
    this.userId = -1;
    this.timestamp = -1;
  }

  public LockQueueEntry(int fileId, int userId, long timestamp) {
    this.fileId = fileId;
    this.userId = userId;
    this.timestamp = timestamp;
  }

  public int getFileId() {
    return fileId;
  }

  public int getUserId() {
    return userId;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
