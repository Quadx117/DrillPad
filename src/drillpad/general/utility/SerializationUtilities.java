package drillpad.general.utility;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 *
 * @author Eric Perron
 */
public class SerializationUtilities
{
    public static void serializeBufferedImage(ObjectOutputStream out,
                                              BufferedImage image) throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        if (image == null)
        {
            out.writeInt(0);
        }
        else
        {
            ImageIO.write(image, "png", buffer);

            // Prepend image with byte count
            out.writeInt(buffer.size());

            // Write image
            buffer.writeTo(out);
        }
    }

    public static BufferedImage deserializeBufferedImage(ObjectInputStream in)
    {
        BufferedImage image = null;

        try
        {
            // Read byte count
            int size = in.readInt();
            if (size > 0)
            {
                byte[] buffer = new byte[size];

                // Make sure you read all bytes of the image
                in.readFully(buffer);

                image = ImageIO.read(new ByteArrayInputStream(buffer));
            }

        }
        catch (IOException ex)
        {
            Logger.getLogger(SerializationUtilities.class.getName()).log(Level.SEVERE,
                                                                         null, ex);
        }

        return image;
    }

}
