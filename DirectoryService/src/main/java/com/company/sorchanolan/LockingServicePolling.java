package com.company.sorchanolan;

import com.company.sorchanolan.Models.Client;
import com.company.sorchanolan.Models.LockQueueEntry;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class LockingServicePolling implements Runnable {
  private LockingService lockingService;
  private DirectoryServiceMain server;

  public LockingServicePolling(DirectoryServiceMain server) {
    this.lockingService = new LockingService();
    this.server = server;
  }

  @Override
  public void run() {
    List<LockQueueEntry> topsOfQueues = lockingService.getTopsOfQueues();

    for (LockQueueEntry lockQueueEntry : topsOfQueues) {
      if (!lockingService.checkIfLocked(lockQueueEntry.getFileId())) {
        lockingService.removeFromQueue(lockQueueEntry);
        Optional<Client> client = lockingService.getClient(lockQueueEntry.getUserId());
        if (client.isPresent()) {
          try {
            Socket socket = new Socket(client.get().getIpAddress(), client.get().getPort());
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToClient.writeBytes("unlocked" + lockQueueEntry.getFileId() + "\n");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
}
