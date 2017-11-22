package com.company.sorchanolan;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {

  public static void main(String[] args) throws Exception {
    new Client();
  }

  public Client() throws Exception {
    Scanner input =new Scanner(System.in);
    List<String> commandTypes = Arrays.asList("open", "close", "read", "write");

    while (true) {
      String command = input.next();
      String fileName = "";

      if (commandTypes.contains(command)) {
        fileName = input.next();

        switch (command) {
          case ("open") : openFile(fileName);
          case ("close") : closeFile(fileName);
          case ("read") : readFile(fileName);
          case ("write") : writeFile(fileName);
        }

      } else {
        input.nextLine();
      }

      System.out.println(command + " " + fileName);
    }
  }

  private void openFile(String fileName) {

  }

  private void closeFile(String fileName) {

  }

  private void readFile(String fileName) {

  }

  private void writeFile(String fileName) {

  }
}
