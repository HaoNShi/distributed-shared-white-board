package org.unimelb.whiteboard.client.Menus;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;

class FileImageView extends FileView {
    private final Icon icon;
    private FileNameExtensionFilter filter;

    public FileImageView(FileNameExtensionFilter filter, Icon anIcon) {
        filter = filter;
        icon = anIcon;
    }
}
