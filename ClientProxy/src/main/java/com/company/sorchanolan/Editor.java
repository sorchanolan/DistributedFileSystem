package com.company.sorchanolan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;

public class Editor implements ActionListener {
  private JFrame frame = null;
  private JMenuItem newFile = new JMenuItem("New");
  private JMenuItem open = new JMenuItem("Open");
  private JMenuItem edit = new JMenuItem("Edit");
  private JMenuItem save = new JMenuItem("Save");
  private String fileName = "";
  private RequestManager requestManager = new RequestManager();
  private DataOutputStream outToServer = null;
  private BufferedReader inFromServer = null;

  public Editor(DataOutputStream outToServer, BufferedReader inFromServer) {
    this.outToServer = outToServer;
    this.inFromServer = inFromServer;
  }

  private JPanel createPanel(Request request) {
    JPanel panel = new JPanel(new GridLayout());
    JTextArea editor = new JTextArea(30, 80);
    panel.add(new JScrollPane(editor));
    editor.setLineWrap(true);
    editor.setWrapStyleWord(true);
    editor.setText(request.getBody());
    editor.setEditable(request.getWriteAccess());
    return panel;
  }

  public void display(Request request) {

    frame = new JFrame(request.getFileName());
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(newFile);
    menuBar.add(open);
    if (!request.getFileName().isEmpty()) {
      menuBar.add(edit);
      menuBar.add(save);
    }
    newFile.addActionListener(this);
    open.addActionListener(this);
    edit.addActionListener(this);
    save.addActionListener(this);
    frame.setJMenuBar(menuBar);
    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(createPanel(request));
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == newFile) {
      fileName = JOptionPane.showInputDialog(frame, "Please enter file name:");
      frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      display(new Request(false, true, fileName + ".txt", ""));
    }

    if (ae.getSource() == open) {
      fileName = JOptionPane.showInputDialog(frame, "Please enter file name to open:");
      Request request = new Request();
      try {
        request = requestManager.readFile(fileName + ".txt", outToServer, inFromServer);
      } catch (Exception e) {
        e.printStackTrace();
      }
      frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      display(request);
    }
  }
}
