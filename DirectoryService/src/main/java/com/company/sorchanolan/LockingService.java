package com.company.sorchanolan;

import org.skife.jdbi.v2.DBI;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class LockingService {
  DirectoryDao dao;

  public LockingService() {
    DBI dbi = new DBI("jdbc:mysql://localhost:3306/DirectoryService?autoReconnect=true&useSSL=false",
        "sorcha", "Nolan123");
    dao = dbi.onDemand(DirectoryDao.class);
  }

  public boolean checkIfLocked(String fileName) {
    Optional<Integer> fileId = dao.getFileId(fileName);
    return !fileId.isPresent() || dao.lockExists(fileId.get(), Instant.now().toEpochMilli());
  }

  public void setLock(int id, String fileName, int userId) {
    Optional<Integer> fileId = dao.getFileId(fileName);
    if (fileId.isPresent()) {
      FileLock fileLock = new FileLock(id, fileId.get(), true, Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli(), userId);
      dao.addNewFileLock(fileLock);
    }
  }

  public void unlock(String fileName, int userId) {
    Optional<Integer> fileId = dao.getFileId(fileName);
    if (fileId.isPresent()) {
      dao.updateLockStatus(false, fileId.get(), userId);
    }
  }
}
