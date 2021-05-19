package org.unimelb.whiteboard.client.Menus;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;

class FileImageView extends FileView {
    private final Icon icon;
    private final FileNameExtensionFilter filter;

    public FileImageView(FileNameExtensionFilter filter, Icon anIcon) {
        this.filter = filter;
        this.icon = anIcon;
    }
}
