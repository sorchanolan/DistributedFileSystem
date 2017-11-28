package com.company.sorchanolan;

import javax.swing.*;
import java.awt.*;

public class Editor {

  public Editor() {

  }

  private JPanel createPanel() {
    JPanel panel = new JPanel(new GridLayout());
    //panel.setBorder(BorderFactory.createTitledBorder("Description"));
    JTextArea editor = new JTextArea(30, 80);
    panel.add(new JScrollPane(editor));
    editor.setLineWrap(true);
    editor.setWrapStyleWord(true);
    editor.setText(editor.toString());
    return panel;
  }

  public void display(Request request) {
    JFrame f = new JFrame("Text Editor");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setContentPane(createPanel());
    f.pack();
    f.setLocationRelativeTo(null);
    f.setVisible(true);
  }
}
