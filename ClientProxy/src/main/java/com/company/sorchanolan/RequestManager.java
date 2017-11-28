package com.company.sorchanolan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;

public class RequestManager {
  ObjectMapper mapper = new ObjectMapper();

  public RequestManager() {
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

  }

  public Optional<String> editFile(String fileName) {
    Request request = new Request(false, true, fileName, "");
    try {
      return Optional.ofNullable(mapper.writeValueAsString(request));
    } catch (JsonProcessingException e) {
      System.out.println("Cannot map request to JSON object" + e);
    }
    return Optional.empty();
  }

  public Request readFile(String fileName, DataOutputStream outToServer, BufferedReader inFromServer) throws Exception {
    Request request = new Request(false, false, fileName, "");
    String requestString = mapper.writeValueAsString(request);
    outToServer.writeBytes(requestString + "\n");
    return mapper.readValue(inFromServer.readLine(), Request.class);
  }

  public Optional<String> saveFile(String fileName, String body) {
    Request request = new Request(true, true, fileName, body);
    try {
      return Optional.ofNullable(mapper.writeValueAsString(request));
    } catch (JsonProcessingException e) {
      System.out.println("Cannot map request to JSON object" + e);
    }
    return Optional.empty();
  }
}
