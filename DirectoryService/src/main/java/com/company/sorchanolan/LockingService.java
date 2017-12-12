package com.company.sorchanolan;

import org.skife.jdbi.v2.DBI;

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

  public boolean checkIfLocked(String fileName) {
    List<Integer> fileId = dao.getFileId(fileName);
    if (!fileId.isEmpty()) {
      return dao.lockExists(fileId.get(0), Instant.now().toEpochMilli());
    }
    return true;
  }

  public void setLock(int id, String fileName, int userId) {
    List<Integer> fileId = dao.getFileId(fileName);
    if (!fileId.isEmpty()) {
      FileLock fileLock = new FileLock(id, fileId.get(0), true, Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli(), userId);
      dao.addNewFileLock(fileLock);
    }
  }

  public void unlock(String fileName, int userId) {
    List<Integer> fileId = dao.getFileId(fileName);
    if (!fileId.isEmpty()) {
      dao.updateLockStatus(false, fileId.get(0), userId);
    }
  }
}