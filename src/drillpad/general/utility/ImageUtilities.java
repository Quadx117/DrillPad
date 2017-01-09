package drillpad.general.utility;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

/**
 *
 * @author Eric Perron
 */
public class ImageUtilities
{
    // TODO(Eric): Create a better Image comparator
    /**
     * Returns whether or not two {@link Image}s are the same.
     *
     * @param image1 The first {@code Image} to compare.
     * @param image2 The second {@code Image} to compare.
     * @return {@code true} if both images are the same, {@code false}
     *         otherwise.
     */
    public static boolean imagesAreEqual(Image image1, Image image2)
    {
        boolean result = false;
        if (image1 == image2)
        {
            result = true;
        }

        return result;
    }

    public static ImageIcon getScaledImage(Image srcImg,
                                           int destWidth, int destHeight,
                                           boolean keepOriginalAspectRatio)
    {
        int srcWidth = srcImg.getWidth(null);
        int srcHeight = srcImg.getHeight(null);
        float imageAspectRatio = (float) srcWidth / (float) srcHeight;
        float destinationAspectRatio = (float) destWidth / (float) destHeight;
        int newWidth = destWidth;
        int newHeight = destHeight;
        if (keepOriginalAspectRatio)
        {
            if (imageAspectRatio > destinationAspectRatio)
            {
                newWidth = destWidth;
                newHeight = (int) (newWidth / imageAspectRatio);
            }
            else
            {
                newHeight = destHeight;
                newWidth = (int) (newHeight * imageAspectRatio);
            }
        }

        BufferedImage resizedImg = new BufferedImage(newWidth, newHeight,
                                                     BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, newWidth, newHeight, null);
        g2.dispose();

        return (new ImageIcon(resizedImg));
    }

}
