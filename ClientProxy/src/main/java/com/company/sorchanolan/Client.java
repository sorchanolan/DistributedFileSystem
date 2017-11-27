package com.company.sorchanolan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Client {
  Socket clientSocket = new Socket("localhost", 6543);
  BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
  BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) throws Exception {
    new Client();
  }

  public Client() throws Exception {
    Scanner input =new Scanner(System.in);
    List<String> commandTypes = Arrays.asList("edit", "read", "save");
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    while (true) {
      String command = input.next();
      String fileName = "";

      if (commandTypes.contains(command)) {
        fileName = input.next();
        Optional<String> response = Optional.empty();

        switch (command) {
          case ("edit") : response = editFile(fileName);
          break;
          case ("read") : response = readFile(fileName);
          break;
          case ("save") : response = saveFile(fileName, input.next());
          break;
        }

        if (response.isPresent()) {
          try {
            outToServer.writeBytes(response.get() + "\n");
          } catch (IOException e) {
            System.out.println("Cannot write to server: " + e);
          }
        }

        String serverResponse = inFromServer.readLine();
        Request serverRequest = mapper.readValue(serverResponse, Request.class);
        System.out.println(serverResponse);

      } else {
        input.nextLine();
      }

      System.out.println(command + " " + fileName);
    }
  }

  private Optional<String> editFile(String fileName) {
    Request request = new Request("read", "rw", fileName, "");
    try {
      return Optional.ofNullable(mapper.writeValueAsString(request));
    } catch (JsonProcessingException e) {
      System.out.println("Cannot map request to JSON object" + e);
    }
    return Optional.empty();
  }

  private Optional<String> readFile(String fileName) {
    Request request = new Request("read", "ro", fileName, "");
    try {
      return Optional.ofNullable(mapper.writeValueAsString(request));
    } catch (JsonProcessingException e) {
      System.out.println("Cannot map request to JSON object" + e);
    }
    return Optional.empty();
  }

  private Optional<String> saveFile(String fileName, String body) {
    Request request = new Request("write", "rw", fileName, body);
    try {
      return Optional.ofNullable(mapper.writeValueAsString(request));
    } catch (JsonProcessingException e) {
      System.out.println("Cannot map request to JSON object" + e);
    }
    return Optional.empty();
  }
}
