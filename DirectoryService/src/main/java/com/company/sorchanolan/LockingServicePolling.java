package com.company.sorchanolan;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

public class LockingServicePolling implements Runnable {
  private LockingService lockingService;
  private DirectoryService server;

  public LockingServicePolling(DirectoryService server) {
    this.lockingService = new LockingService();
    this.server = server;
  }

  @Override
  public void run() {
    List<LockQueueEntry> topsOfQueues = lockingService.getTopsOfQueues();

    for (LockQueueEntry lockQueueEntry : topsOfQueues) {
      if (!lockingService.checkIfLocked(lockQueueEntry.getFileId())) {
        lockingService.lock(String.valueOf(lockQueueEntry.getFileId()), server.createID(), lockQueueEntry.getUserId());
        lockingService.removeFromQueue(lockQueueEntry);

        Socket socket = server.userIdToSocketMapping.get(lockQueueEntry.getUserId());
        try {
          DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
          e.printStackTrace();
        }


      }
    }
  }
}
