package org.unimelb.whiteboard.client.menu;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;

class FileImageView extends FileView {
    private Icon icon;
    private FileNameExtensionFilter filter;

    public FileImageView(FileNameExtensionFilter filter, Icon anIcon) {
        this.filter = filter;
        this.icon = anIcon;
    }
}
