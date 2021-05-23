package org.unimelb.whiteboard.client.menu;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.shape.MyShape;
import org.unimelb.whiteboard.client.whiteboard.WhiteBoardWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class FileSaveListener implements ActionListener {
    private final WhiteBoardWindow window;
    private final Client client;
    private String action;

    public FileSaveListener(WhiteBoardWindow window, Client client, String action) {
        super();
        this.window = window;
        this.client = client;
        this.action = action;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            savePic();
        } catch (Exception er) {
            JOptionPane.showMessageDialog(window.getFrame(), "Failed to save image");
            er.printStackTrace();
        }
    }

    private void savePic() throws IOException {
        System.out.println("Save " + action);

        Dimension imageSize = window.getPaintBoardPanel().getSize();
        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = image.createGraphics();
        window.getPaintBoardPanel().paint(graphics);
        graphics.dispose();

        if (action.equals("saveAs")) {
            saveAsPic(image);
        } else {
            // Save jpg
            String currentPath = client.getCurrentSavePath();
            if (currentPath == null) {
                saveAsPic(image);
            } else {
                // Not handle duplicate
                Vector<MyShape> history = window.getPaintManager().getPaintHistory();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(currentPath));
                oos.writeObject(history);
                oos.close();
            }
        }
    }

    private void saveAsPic(BufferedImage image) throws IOException {
        JFileChooser jf = new JFileChooser(".");
        jf.setAcceptAllFileFilterUsed(false);
        jf.addChoosableFileFilter(new FileNameExtensionFilter("WhiteBoard files", "wb"));
        jf.addChoosableFileFilter(new FileNameExtensionFilter("JPG files", "jpg"));
        jf.addChoosableFileFilter(new FileNameExtensionFilter("PNG files", "png"));

        int value = jf.showSaveDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            File imageFile = jf.getSelectedFile(); // get the image file
            FileNameExtensionFilter fileFilter = (FileNameExtensionFilter) jf.getFileFilter();
            String format = fileFilter.getExtensions()[0];
            String savePath = imageFile.getPath() + "." + format;
            if (format.equals("wb")) {
                Vector<MyShape> history = window.getPaintManager().getPaintHistory();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savePath));
                oos.writeObject(history);
                oos.close();
                client.setCurrentSavePath(savePath);
                System.out.println(savePath);
            } else {
                File file = new File(savePath);
                if (!file.exists()) {
                    file.createNewFile();
                    ImageIO.write(image, format, file);
                } else {
                    // Duplicated image name
                    int confirm = JOptionPane.showConfirmDialog(window.getFrame(),
                            "This name has existed in this path. Replace the file?", "Warning",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        ImageIO.write(image, format, file);
                    }
                }
            }
        }
    }

}
