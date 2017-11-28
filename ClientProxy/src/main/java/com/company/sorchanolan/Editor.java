package com.company.sorchanolan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Editor implements ActionListener {

  public Editor() {

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
    JFrame f = new JFrame(request.getFileName());
    JMenuBar menuBar = new JMenuBar();
    JMenuItem newFile = new JMenuItem("New");
    JMenuItem open = new JMenuItem("Open");
    JMenuItem edit = new JMenuItem("Edit");
    JMenuItem save = new JMenuItem("Save");
    menuBar.add(newFile);
    menuBar.add(open);
    menuBar.add(edit);
    menuBar.add(save);
    newFile.addActionListener(this);
    open.addActionListener(this);
    edit.addActionListener(this);
    save.addActionListener(this);
    f.setJMenuBar(menuBar);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setContentPane(createPanel(request));
    f.pack();
    f.setLocationRelativeTo(null);
    f.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

  }
}
