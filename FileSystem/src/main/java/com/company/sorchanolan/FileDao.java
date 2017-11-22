package com.company.sorchanolan;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileDao {
  private List<File> listOfFiles = Arrays.asList(new File("src/main/java/resources").listFiles());

  public Optional<File> getFile(String fileName) {
    return listOfFiles.stream().filter(file -> file.getName().equals(fileName)).findAny();
  }
}
