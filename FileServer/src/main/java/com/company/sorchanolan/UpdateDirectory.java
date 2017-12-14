package com.company.sorchanolan;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.sorchanolan.FileServer.directoryIpAddress;
import static com.company.sorchanolan.FileServer.directoryPort;
import static com.company.sorchanolan.FileServer.port;

public class UpdateDirectory {
  private Socket directorySocket = null;
  private ObjectMapper mapper = new ObjectMapper();
  private DataOutputStream outToDirectory = null;
  private BufferedReader inFromDirectory = null;
  private FileServer server = null;

  public UpdateDirectory(FileServer server) throws Exception {
    this.server = server;
    openDirectoryComms();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    sendDataToDirectoryService();
  }

  private void openDirectoryComms() throws Exception {
    directorySocket = new Socket(directoryIpAddress, directoryPort);
    outToDirectory = new DataOutputStream(directorySocket.getOutputStream());
    inFromDirectory = new BufferedReader(new InputStreamReader(directorySocket.getInputStream()));
  }

  private void sendDataToDirectoryService() throws Exception {
    File folder = new File("../Files_" + port);
    List<String> files = new ArrayList<>();
    if (folder.listFiles() != null) {
      files = Lists.newArrayList(folder.listFiles()).stream()
          .map(File::getName)
          .filter(name -> !name.startsWith("."))
          .collect(Collectors.toList());
    }

    FileServerData fileServerData = new FileServerData("localhost", port, files);
    String dataAsString = mapper.writeValueAsString(fileServerData);
    outToDirectory.writeBytes("fileserver" + dataAsString + "\n");
    TypeReference<List<FileMap>> typeRef = new TypeReference<List<FileMap>>() {};
    List<FileMap> fileMaps = mapper.readValue(inFromDirectory.readLine(), typeRef);
    fileMaps.stream()
        .filter(fm -> !server.files.contains(fm))
        .forEach(fm -> server.files.add(fm));
  }
}
