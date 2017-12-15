package com.company.sorchanolan;

import com.company.sorchanolan.Models.FileMap;
import com.company.sorchanolan.Models.FileServer;
import com.company.sorchanolan.Models.Request;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.company.sorchanolan.ClientMain.*;

public class RequestManager {
  private Socket directorySocket = null;
  private Socket fileServerSocket = null;
  private ObjectMapper mapper = new ObjectMapper();
  private DataOutputStream outToDirectory = null;
  private BufferedReader inFromDirectory = null;
  private DataOutputStream outToFileServer = null;
  private BufferedReader inFromFileServer = null;
  private static int userId;
  private static boolean fileServerConnected = false;

  public RequestManager() throws Exception {
    openDirectoryComms(directoryIpAddress, directoryPort);
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  private void openDirectoryComms(String ipAddress, int port) throws Exception {
    directorySocket = new Socket(ipAddress, port);
    inFromDirectory = new BufferedReader(new InputStreamReader(directorySocket.getInputStream()));
    outToDirectory = new DataOutputStream(directorySocket.getOutputStream());
    String output = new JSONObject().put("port", ClientMain.port).put("ipAddress", "localhost").toString();
    outToDirectory.writeBytes("newclient" + output + "\n");
    userId = mapper.readValue(inFromDirectory.readLine(), int.class);
  }

  private void openFileServerComms(String ipAddress, int port) throws Exception {
    fileServerSocket = new Socket(ipAddress, port);
    inFromFileServer = new BufferedReader(new InputStreamReader(fileServerSocket.getInputStream()));
    outToFileServer = new DataOutputStream(fileServerSocket.getOutputStream());
    if (!fileServerConnected) {
      String output = new JSONObject().put("port", ClientMain.port).put("ipAddress", "localhost").put("id", userId).toString();
      outToFileServer.writeBytes("newclient" + output + "\n");
      fileServerConnected = true;
    }
  }

  public Request newFile(String fileName) throws Exception {
    killClient();
    outToDirectory.writeBytes("newfile" + fileName + "\n");
    FileServer server = mapper.readValue(inFromDirectory.readLine(), FileServer.class);
    openFileServerComms(server.getIpAddress(), server.getPort());
    return readFile(fileName);
  }

  public String[] listFiles() throws Exception {
    outToDirectory.writeBytes("listfiles\n");
    String[] files = mapper.readValue(inFromDirectory.readLine(), String[].class);
    return files;
  }

  public Request openFile(String fileName) throws Exception {
    outToDirectory.writeBytes("openfile" + fileName + "\n");
    FileServer server = mapper.readValue(inFromDirectory.readLine(), FileServer.class);
    openFileServerComms(server.getIpAddress(), server.getPort());
    return readFile(fileName);
  }

  public Request readFile(String fileName) throws Exception {
    Optional<Request> cacheRequest = checkCache(fileName);
    if (!cacheRequest.isPresent()) {
      Request request = new Request(false, false, fileName, "", -1);
      String requestString = mapper.writeValueAsString(request);
      outToFileServer.writeBytes(requestString + "\n");
      request = mapper.readValue(inFromFileServer.readLine(), Request.class);
      currentFile = new FileMap(request.getFileId(), request.getFileName(), request.getBody());
      cacheFile();
      return request;
    }
    return cacheRequest.get();
  }

  public Request editFile(String fileName, int fileId) throws Exception {
    outToDirectory.writeBytes("lock" + fileId + "\n");
    if (inFromDirectory.readLine().equals("true")) {
      Request request = new Request(false, true, fileName, "", fileId);
      String requestString = mapper.writeValueAsString(request);
      outToFileServer.writeBytes(requestString + "\n");
      return mapper.readValue(inFromFileServer.readLine(), Request.class);
    }
    return new Request(false, false, fileName, "", fileId);
  }

  public Request saveFile(String fileName, String body, int fileId) throws Exception {
    outToDirectory.writeBytes("unlock" + fileId + "\n");
    Request request = new Request(true, false, fileName, body, fileId);
    String requestString = mapper.writeValueAsString(request);
    outToFileServer.writeBytes(requestString + "\n");
    currentFile.setBody(body);
    updateCache(currentFile);
    return mapper.readValue(inFromFileServer.readLine(), Request.class);
  }

  public void killClient() throws Exception {
    String folderName = "Cache_"  + userId;
    File folder = new File(folderName);
    File[] listOfFiles = folder.listFiles();
    if (listOfFiles != null) {
      for (File file : listOfFiles) {
        file.delete();
      }
    }
    Files.deleteIfExists(Paths.get(folderName));
    outToFileServer.writeBytes("kill" + userId + "\n");
    outToDirectory.writeBytes("kill" + userId + "\n");
  }

  public void cacheFile() throws Exception {
    String path = "Cache_"  + userId + "/" + currentFile.getFileName();
    if (!Files.exists(Paths.get(path))) {
      Path directoryPath = Paths.get("Cache_"  + userId + "/");
      if (!Files.exists(directoryPath)) {
        Files.createDirectory(directoryPath);
      }
      Path filePath = Paths.get(path);
      Files.createFile(filePath);
      FileWriter fileWriter = new FileWriter(new File(path), false);
      fileWriter.write( mapper.writeValueAsString(currentFile));
      fileWriter.close();
    }
  }

  public Optional<Request> checkCache(String fileName) throws Exception {
    String path = "Cache_" + userId + "/" + fileName;
    if (Files.exists(Paths.get(path))) {
      FileMap file = mapper.readValue(String.join("\n", Files.readAllLines(Paths.get(path))), FileMap.class);
      Request request = new Request(false, false, file.getFileName(), file.getBody(), file.getId());
      return Optional.of(request);
    }
    return Optional.empty();
  }

  public void updateCache(FileMap file) throws Exception {
    String path = "Cache_" + userId + "/" + file.getFileName();
    if (Files.exists(Paths.get(path))) {
      FileMap fileFromCache = mapper.readValue(String.join("\n", Files.readAllLines(Paths.get(path))), FileMap.class);
      fileFromCache.setBody(file.getBody());
      FileWriter fileWriter = new FileWriter(new File(path), false);
      fileWriter.write(mapper.writeValueAsString(fileFromCache));
      fileWriter.close();
    }
  }
}
