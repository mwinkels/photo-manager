package nl.mwinkels.photomanager;


import nl.mwinkels.photomanager.data.DetectedFace;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author mwinkels
 * @since Jul 03, 2016
 */
public class PhotoFacesWindow extends JFrame {

    public PhotoFacesWindow(BufferedImage image, Set<DetectedFace> detectedFaces) {
        JLayeredPane layeredPane = new JLayeredPane();
        ImageIcon icon = new ImageIcon();
        JLabel label = new JLabel(icon);
        layeredPane.add(label, 1);
        FacesPane facesPane = new FacesPane();
        layeredPane.add(facesPane, 0);
        setContentPane(layeredPane);
        int scale = determineScale(image.getWidth(), image.getHeight());
        Dimension imageSize = new Dimension(image.getWidth() / scale, image.getHeight() / scale);
        setSize(imageSize);
        Image scaledInstance = image.getScaledInstance(imageSize.width, imageSize.height, Image.SCALE_DEFAULT);
        layeredPane.setPreferredSize(imageSize);
        icon.setImage(scaledInstance);
        label.setSize(imageSize);
        facesPane.setSize(imageSize);
        facesPane.setFaces(detectedFaces, scale);
        pack();
    }

    private int determineScale(int width, int height) {
        int max = Math.max(width, height);
        return max < 1000 ? 1 : max / 1000;
    }

    private class FacesPane extends JPanel {

        private List<Rectangle> rectangles;

        public FacesPane() {
            setOpaque(false);
        }

        public void setFaces(Set<DetectedFace> faces, int scale) {
            rectangles = faces.stream().map(f -> f.toRectangle(scale)).collect(Collectors.toList());
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            rectangles.forEach(g2::draw);
        }
    }
}
