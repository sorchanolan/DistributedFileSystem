package com.company.sorchanolan;

import java.io.File;

public class FileServer {

  public static void main(String[] args) {
    new FileServer();
  }

  public FileServer() {
    File[] listOfFiles = new File("src/main/java/resources").listFiles();

  }
}
