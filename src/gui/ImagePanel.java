package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private String string;

    public ImagePanel(BufferedImage img) {
        this.image = img;
    }
    public ImagePanel(String string) {
        this.string = string;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // Draw image scaled to fit panel
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }else{
            g.drawString(string, getX() + getWidth()/2, getY() + getHeight()/2);
        }
    }
}