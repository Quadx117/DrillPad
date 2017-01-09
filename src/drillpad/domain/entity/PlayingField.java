package drillpad.domain.entity;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;

import drillpad.general.utility.SerializationUtilities;

/**
 *
 * @author Eric Perron
 */
public class PlayingField implements Serializable
{
    private float width;
    private float height;
    private transient BufferedImage image;

    private final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    /**
     * Creates a new {@code PlayingField} instance.
     *
     * @param image  The {@link BufferedImage} representing the playing field
     * @param width  The real world width of the playing field in meters
     * @param height The real world width of the playing field in meters
     */
    public PlayingField(BufferedImage image, float width, float height)
    {
        this.image = image;
        this.width = width;
        this.height = height;
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        SerializationUtilities.serializeBufferedImage(out, image);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        image = SerializationUtilities.deserializeBufferedImage(in);
    }

    String getMousePositionAsString(Point2D.Float mousePosition)
    {
        // Conversion to meters
        Point2D.Float positionInMeters =
                new Point2D.Float((mousePosition.x / (float) image.getWidth()) * width,
                                  (mousePosition.y / (float) image.getHeight()) * height);

        // NOTE: We only support meters as units for the playingField, so it is
        // safe to put it directly in the returned string.
        return (decimalFormat.format(positionInMeters.x) + "; " +
                decimalFormat.format(positionInMeters.y) + " m");
    }

    BufferedImage getImage()
    {
        return image;
    }

    float getWidth()
    {
        return width;
    }

    float getHeight()
    {
        return height;
    }

    void edit(BufferedImage newPlayingFieldImage, float newWidth, float newHeight)
    {
        image = newPlayingFieldImage;
        width = newWidth;
        height = newHeight;
    }

}
