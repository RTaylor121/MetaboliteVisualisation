import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class ImagePanel extends JLayeredPane{

    private static BufferedImage image;

    public ImagePanel() {
    	super();
    }
    
    public static void setImage(BufferedImage img, int width, int height){
    	
    	System.out.println("HEIGHT: " + height + ". WIDTH: " + width);
    	Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    	BufferedImage bimg = new BufferedImage(scaledImage.getWidth(null),
				scaledImage.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
//				BufferedImage.TYPE_BYTE_GRAY);
    	bimg.createGraphics().drawImage(scaledImage, 0, 0, null);
    	image = bimg;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

}