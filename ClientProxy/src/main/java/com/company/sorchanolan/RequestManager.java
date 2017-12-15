package com.company.sorchanolan;

import com.company.sorchanolan.Models.File;
import com.company.sorchanolan.Models.FileServer;
import com.company.sorchanolan.Models.Request;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static com.company.sorchanolan.ClientMain.currentFile;
import static com.company.sorchanolan.ClientMain.directoryIpAddress;
import static com.company.sorchanolan.ClientMain.directoryPort;

public class RequestManager {
  private Socket directorySocket = null;
  private Socket fileServerSocket = null;
  private ObjectMapper mapper = new ObjectMapper();
  private DataOutputStream outToDirectory = null;
  private BufferedReader inFromDirectory = null;
  private DataOutputStream outToFileServer = null;
  private BufferedReader inFromFileServer = null;
  private static int userId;

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
    String output = new JSONObject().put("port", port).put("ipAddress", "localhost").put("id", userId).toString();
    outToFileServer.writeBytes("newclient" + output + "\n");
  }

  public Request newFile(String fileName) throws Exception {
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
    Request request = new Request(false, false, fileName, "", -1);
    String requestString = mapper.writeValueAsString(request);
    outToFileServer.writeBytes(requestString + "\n");
    request = mapper.readValue(inFromFileServer.readLine(), Request.class);
    currentFile = new File(request.getFileId(), request.getFileName(), request.getBody());
    return request;
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
    return mapper.readValue(inFromFileServer.readLine(), Request.class);
  }

  public void killClient() throws Exception {
    outToDirectory.writeBytes("kill" + userId + "\n");
    outToFileServer.writeBytes("kill" + userId + "\n");
  }

  public void cacheFile() {

  }
}
