package com.company.sorchanolan;

import java.util.Optional;

public class FileServer {
  FileDao fileDao = new FileDao();

  public static void main(String[] args) {
    new FileServer();
  }


  public FileServer() {
    Optional<String> file = fileDao.getFile("dfdsfs.txt");
    try {
      fileDao.updateFile("1.txt", "hi update file with this pls");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
