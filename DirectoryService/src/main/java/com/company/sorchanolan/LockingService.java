package com.company.sorchanolan;

import org.skife.jdbi.v2.DBI;

import java.io.BufferedReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class LockingService {
  DirectoryDao dao;

  public LockingService() {
    DBI dbi = new DBI("jdbc:mysql://localhost:3306/DirectoryService?autoReconnect=true&useSSL=false",
        "sorcha", "Nolan123");
    dao = dbi.onDemand(DirectoryDao.class);
  }

  public String lock(String message, int id, int userId) {
    int fileId = Integer.parseInt(message);
    if (!checkIfLocked(fileId)) {
      setLock(id, fileId, userId);
      return "true\n";
    }
    putInQueue(new LockQueueEntry(fileId, userId, Instant.now().toEpochMilli()));
    return "false\n";
  }

  public boolean checkIfLocked(int fileId) {
    return dao.lockExists(fileId, Instant.now().toEpochMilli());
  }

  public void setLock(int id, int fileId, int userId) {
    FileLock fileLock = new FileLock(id, fileId, true, Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli(), userId);
    dao.addNewFileLock(fileLock);
  }

  public void unlock(int fileId, int userId) {
    dao.updateLockStatus(false, fileId, userId);
  }

  public void putInQueue(LockQueueEntry lockQueueEntry) {
    dao.addToLockQueue(lockQueueEntry);
  }

  public void removeFromQueue(LockQueueEntry lockQueueEntry) {
    dao.removeFromLockQueue(lockQueueEntry);
  }

  public List<LockQueueEntry> getTopsOfQueues() {
    return dao.getTopsOfQueues();
  }
}
