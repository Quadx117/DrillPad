package drillpad.domain.entity;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import drillpad.general.utility.SerializationUtilities;

/**
 *
 * @author Eric Perron
 */
public final class EntityType implements Serializable
{
    private String name;
    private Color color;
    // NOTE(Eric): Used when drawing a circle instead of an image
    private int radius;
    private transient BufferedImage image;
    private float imageScale;
    private boolean isTeam;

    // TODO(Eric): Get rid of this constructor after we change how we deal with
    // the PlayingElement creation and null value
    public EntityType(String name, Color color, int radius, boolean isTeam)
    {
        this(name, color, radius, null, 1.0f, isTeam);
    }

    public EntityType(String name, Color color, int radius,
                      BufferedImage image, float imageScale, boolean isTeam)
    {
        this.name = name;
        this.color = color;
        this.radius = radius;
        this.image = image;
        this.imageScale = imageScale;
        this.isTeam = isTeam;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj.getClass() != this.getClass())
        {
            return false;
        }
        return (this.name.equals(((EntityType) obj).name));
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
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

    boolean hasImage()
    {
        return (image != null);
    }

    boolean isTeam()
    {
        return isTeam;
    }

    String getName()
    {
        return name;
    }

    Color getColor()
    {
        return color;
    }

    int getRadius()
    {
        return radius;
    }

    BufferedImage getImage()
    {
        return image;
    }

    float getImageScale()
    {
        return imageScale;
    }

    void edit(String newName, Color newColor, int newRadius,
              BufferedImage newImage, float newImageScale,
              boolean newIsTeam)
    {
        name = newName;
        color = newColor;
        radius = newRadius;
        image = newImage;
        imageScale = newImageScale;
        isTeam = newIsTeam;
    }

}
