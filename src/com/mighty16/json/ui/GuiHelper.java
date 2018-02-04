package com.mighty16.json.ui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class GuiHelper {

    public static JPopupMenu getJsonContextMenuPopup(final JEditorPane textPanel, TextResources textResources) {
        ActionListener actionListener = e -> {
            String command = e.getActionCommand();
            if (command.equals(textResources.getPasteCommand())) {
                textPanel.paste();
            } else if (command.equals(textResources.getCopyCommand())) {
                textPanel.copy();
            } else if (command.equals(textResources.getCutCommand())) {
                textPanel.cut();
            }
        };

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem cutMenuItem = new JMenuItem(textResources.getCutCommand());
        popupMenu.add(cutMenuItem);
        JMenuItem copyMenuItem = new JMenuItem(textResources.getCopyCommand());
        copyMenuItem.addActionListener(actionListener);
        popupMenu.add(copyMenuItem);
        JMenuItem pasteMenuItem = new JMenuItem(textResources.getPasteCommand());
        pasteMenuItem.addActionListener(actionListener);
        popupMenu.add(pasteMenuItem);
        return popupMenu;
    }

}
