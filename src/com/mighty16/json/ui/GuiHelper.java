package com.mighty16.json.ui;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiHelper {

    public static JPopupMenu getJsonContextMenuPopup(final JEditorPane textPanel) {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if (command.equals("Paste")) {
                    textPanel.paste();
                } else if (command.equals("Copy")) {
                    textPanel.copy();
                } else if (command.equals("Cut")) {
                    textPanel.cut();
                }
            }
        };

        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem cutMenuItem = new JMenuItem("Cut");
        popupMenu.add(cutMenuItem);

        // Copy
        JMenuItem copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.addActionListener(actionListener);
        popupMenu.add(copyMenuItem);
        // Paste
        JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.addActionListener(actionListener);
        popupMenu.add(pasteMenuItem);

        return popupMenu;
    }

}
