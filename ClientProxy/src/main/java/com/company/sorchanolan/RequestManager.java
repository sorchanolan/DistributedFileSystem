package com.company.sorchanolan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestManager {
  private Socket socket = null;
  private ObjectMapper mapper = new ObjectMapper();
  private DataOutputStream outToServer = null;
  private BufferedReader inFromServer = null;

  public RequestManager() throws Exception {
    openComms();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  private void openComms() throws Exception {
    socket = new Socket("localhost", 6543);
    try {
      inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      outToServer = new DataOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public Request readFile(String fileName) throws Exception {
    Request request = new Request(false, false, fileName, "");
    String requestString = mapper.writeValueAsString(request);
    outToServer.writeBytes(requestString + "\n");
    return mapper.readValue(inFromServer.readLine(), Request.class);
  }

  public Request editFile(String fileName) throws Exception {
    Request request = new Request(false, true, fileName, "");
    String requestString = mapper.writeValueAsString(request);
    outToServer.writeBytes(requestString + "\n");
    return mapper.readValue(inFromServer.readLine(), Request.class);
  }

  public Request saveFile(String fileName, String body) throws Exception {
    Request request = new Request(true, false, fileName, body);
    String requestString = mapper.writeValueAsString(request);
    outToServer.writeBytes(requestString + "\n");
    return mapper.readValue(inFromServer.readLine(), Request.class);
  }
}
