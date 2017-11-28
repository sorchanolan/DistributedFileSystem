package com.company.sorchanolan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
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
  Socket socket = null;
  DataOutputStream outToServer = null;
  BufferedReader inFromServer = null;
  ObjectMapper mapper = new ObjectMapper();
  Editor editor = new Editor();

  public static void main(String[] args) throws Exception {
    new Client();
  }

  private void openComms() {
    try {
      inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      outToServer = new DataOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public Client() throws Exception {
    socket = new Socket("localhost", 6543);
    openComms();
    Scanner input =new Scanner(System.in);
    List<String> commandTypes = Arrays.asList("edit", "read", "save");
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    while (true) {
      System.out.println("Enter command:");
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
        EventQueue.invokeLater(() -> editor.display(serverRequest));

      } else {
        input.nextLine();
      }

      System.out.println(command + " " + fileName);
    }
  }

  private Optional<String> editFile(String fileName) {
    Request request = new Request(false, true, fileName, "");
    try {
      return Optional.ofNullable(mapper.writeValueAsString(request));
    } catch (JsonProcessingException e) {
      System.out.println("Cannot map request to JSON object" + e);
    }
    return Optional.empty();
  }

  private Optional<String> readFile(String fileName) {
    Request request = new Request(false, false, fileName, "");
    try {
      return Optional.ofNullable(mapper.writeValueAsString(request));
    } catch (JsonProcessingException e) {
      System.out.println("Cannot map request to JSON object" + e);
    }
    return Optional.empty();
  }

  private Optional<String> saveFile(String fileName, String body) {
    Request request = new Request(true, true, fileName, body);
    try {
      return Optional.ofNullable(mapper.writeValueAsString(request));
    } catch (JsonProcessingException e) {
      System.out.println("Cannot map request to JSON object" + e);
    }
    return Optional.empty();
  }
}
