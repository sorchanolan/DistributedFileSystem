package com.company.sorchanolan;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.*;
import java.net.URL;
import java.util.Optional;

public class FileDao {

  public FileDao() {

  }

  public Optional<String> getFile(String fileName) {
    try {
      URL fileUrl = Resources.getResource(fileName);
      return Optional.ofNullable(Resources.toString(fileUrl, Charsets.UTF_8));
    } catch (Exception e) {
      System.out.println("This file does not exist: " + e);
      return Optional.empty();
    }
  }

  public void updateFile(String fileName, String updatedFileContents) throws Exception {
    File originalFile = new File(fileName);
    BufferedReader br = new BufferedReader(new FileReader(originalFile));
    String s = originalFile.toString();
  }
}
