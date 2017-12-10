package com.company.sorchanolan;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static com.company.sorchanolan.Client.ipAddress;
import static com.company.sorchanolan.Client.port;

public class RequestManager {
  private Socket directorySocket = null;
  private Socket fileServerSocket = null;
  private ObjectMapper mapper = new ObjectMapper();
  private DataOutputStream outToDirectory = null;
  private BufferedReader inFromDirectory = null;
  private DataOutputStream outToFileServer = null;
  private BufferedReader inFromFileServer = null;

  public RequestManager() throws Exception {
    openDirectoryComms(ipAddress, port);
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  private void openDirectoryComms(String ipAddress, int port) throws Exception {
    directorySocket = new Socket(ipAddress, port);
    inFromDirectory = new BufferedReader(new InputStreamReader(directorySocket.getInputStream()));
    outToDirectory = new DataOutputStream(directorySocket.getOutputStream());
  }

  private void openFileServerComms(String ipAddress, int port) throws Exception {
    fileServerSocket = new Socket(ipAddress, port);
    inFromFileServer = new BufferedReader(new InputStreamReader(fileServerSocket.getInputStream()));
    outToFileServer = new DataOutputStream(fileServerSocket.getOutputStream());
  }

  public Request newFile(String fileName) throws Exception {
    outToDirectory.writeBytes("newfile\n");
    FileServer server = mapper.readValue(inFromDirectory.readLine(), FileServer.class);
    openFileServerComms(server.getIpAddress(), server.getPort());
    return readFile(fileName);
  }

  public Request readFile(String fileName) throws Exception {
    Request request = new Request(false, false, fileName, "");
    String requestString = mapper.writeValueAsString(request);
    outToFileServer.writeBytes(requestString + "\n");
    return mapper.readValue(inFromFileServer.readLine(), Request.class);
  }

  public Request editFile(String fileName) throws Exception {
    Request request = new Request(false, true, fileName, "");
    String requestString = mapper.writeValueAsString(request);
    outToFileServer.writeBytes(requestString + "\n");
    return mapper.readValue(inFromFileServer.readLine(), Request.class);
  }

  public Request saveFile(String fileName, String body) throws Exception {
    Request request = new Request(true, false, fileName, body);
    String requestString = mapper.writeValueAsString(request);
    outToFileServer.writeBytes(requestString + "\n");
    return mapper.readValue(inFromFileServer.readLine(), Request.class);
  }
}
