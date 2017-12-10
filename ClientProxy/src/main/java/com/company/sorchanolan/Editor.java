package com.company.sorchanolan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class Editor implements ActionListener {
  private JFrame frame = null;
  private JTextArea editor = null;
  private JPanel panel = null;
  private JMenuItem newFile = new JMenuItem("New");
  private JMenu open = new JMenu("Open");
  private JMenuItem fileItem = null;
  private JMenuItem edit = new JMenuItem("Edit");
  private JMenuItem save = new JMenuItem("Finish Editing");
  private String fileName = "";
  private RequestManager requestManager = null;
  private Request request = new Request();

  public Editor() throws Exception {
    requestManager = new RequestManager();
    display(request);
  }

  private JPanel createPanel(Request request) {
    panel = new JPanel(new GridLayout());
    editor = new JTextArea(30, 80);
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
    menuBar.add(open);
    menuBar.add(newFile);
    if (!request.getFileName().isEmpty()) {
      menuBar.add(edit);
      menuBar.add(save);
    }
    addOpenMenu();
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

  private void addOpenMenu() {
    try {
      String[] files = requestManager.listFiles();
      open.removeAll();
      for (String file : files) {
        JMenuItem menuItem = new JMenuItem(file);
        menuItem.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent ae) {
            fileName = ae.getActionCommand();
            Request request = new Request();
            try {
              request = requestManager.openFile(correctFileName(fileName));
            } catch (Exception e) {
              e.printStackTrace();
            }
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            display(request);
          }
        });
        open.add(menuItem);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == newFile) {
      fileName = JOptionPane.showInputDialog(frame, "Please enter file name:");
      if (fileName != null) {
        Request request = new Request();
        try {
          request = requestManager.newFile(correctFileName(fileName));
        } catch (Exception e) {
          e.printStackTrace();
        }
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        display(request);
      }
    }

    if (ae.getSource() == edit) {
      Request request = new Request();
      try {
        request = requestManager.editFile(correctFileName(fileName));
      } catch (Exception e) {
        e.printStackTrace();
      }
      frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      display(request);
    }

    if (ae.getSource() == save) {
      Request request = new Request();
      try {
        request = requestManager.saveFile(correctFileName(fileName), editor.getText());
      } catch (Exception e) {
        e.printStackTrace();
      }
      frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      display(request);
    }
  }

  private String correctFileName(String name) {
    return name.contains(".txt") ? name : name + ".txt";
  }
}
