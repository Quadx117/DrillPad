package drillpad.domain.entity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import drillpad.general.math.Vector2;

/**
 *
 * @author Eric Perron
 */
public class Entity implements Serializable
{
    protected EntityType entityType;
    private Point2D.Float position;
    private float angleInRadians;
    private boolean isSelected;
    protected boolean isCollidable;

    /**
     * Creates an {@code Entity} of the specified {@link EntityType} and at the
     * specified position.
     * <p>
     * This {@code Entity} will be collidable by default. This means that it
     * will not be able to pass through other colidable entity's.
     * <p>
     * The position is the center point of the {@code Entity}.
     *
     * @param entityType The {@code EntityType} of this {@code Entity}.
     * @param position   The position of this {@code Entity}.
     */
    Entity(EntityType entityType, Point2D.Float position)
    {
        this.entityType = entityType;
        this.position = position;
        this.angleInRadians = 0f;
        this.isSelected = false;
        this.isCollidable = true;
    }

    boolean contains(Point2D.Float point)
    {
        return (xIsInsideItemWidth(point.x) && yIsInsideItemHeight(point.y));
    }

    // TODO(Eric): If we implement simple and advanced mode rendering, we will
    // need to adjust both of these methods.
    private boolean xIsInsideItemWidth(float x)
    {
        int halfWidth = getWidth() / 2;

        return (x < (position.x + halfWidth)) && (x > (position.x - halfWidth));
    }

    private boolean yIsInsideItemHeight(float y)
    {
        int halfHeight = getHeight() / 2;

        return (y < (position.y + halfHeight)) && (y > (position.y - halfHeight));
    }

    void translate(Point2D.Float delta)
    {
        position.x = (position.x + delta.x);
        position.y = (position.y + delta.y);
    }

    /**
     * Toggles the selection status of the entity.
     */
    void switchSelectionStatus()
    {
        isSelected = !isSelected;
    }

    boolean isCollidingWith(Entity other)
    {
        boolean result = false;
        Rectangle2D rect1 = new Rectangle2D.Float(position.x - (float) getWidth() / 2f,
                                                  position.y - (float) getHeight() / 2f,
                                                  getWidth(),
                                                  getHeight());
        Rectangle2D rect2 = new Rectangle2D.Float(other.getPosition().x - (float) other.getWidth() / 2f,
                                                  other.getPosition().y - (float) other.getHeight() / 2f,
                                                  other.getWidth(),
                                                  other.getHeight());
        if (rect1.intersects(rect2))
        {
            result = true;
        }

        return result;
    }

    // TODO(Eric): Je ne suis plus certain que c'est OK de cette façon pour le
    // principe de la séparation du UI et du domain, mais bon on a pas eu de
    // commentaires des correcteurs à ce sujet.
    public void draw(Graphics2D g2d)
    {
        if (entityType.hasImage())
        {
            AffineTransform previousTransform = g2d.getTransform();
            g2d.rotate(angleInRadians, position.getX(), position.getY());
            g2d.drawImage(entityType.getImage(),
                          (int) position.getX() - getWidth() / 2,
                          (int) position.getY() - getHeight() / 2,
                          getWidth(),
                          getHeight(),
                          null);
            g2d.setTransform(previousTransform);
        }
        else
        {
            g2d.setColor(entityType.getColor());
            g2d.fillOval((int) position.getX() - entityType.getRadius(),
                         (int) position.getY() - entityType.getRadius(),
                         entityType.getRadius() * 2,
                         entityType.getRadius() * 2);
        }
    }

    public void rotate(Point2D.Float previousMousePosition,
                       Point2D.Float currentMousePosition)
    {
        Vector2 a = new Vector2(previousMousePosition.x - position.x,
                                previousMousePosition.y - position.y);
        Vector2 b = new Vector2(currentMousePosition.x - position.x,
                                currentMousePosition.y - position.y);
        a.normalize();
        b.normalize();
        float y = a.perp().dotProduct(b);
        float x = a.dotProduct(b);
        angleInRadians += (float) Math.atan2(y, x);
    }

    public boolean hasImage()
    {
        return entityType.hasImage();
    }

    public Point2D.Float getPosition()
    {
        return position;
    }

    public int getWidth()
    {
        if (entityType.hasImage())
        {
            return (int) (entityType.getImage().getWidth() * entityType.getImageScale());
        }
        else
        {
            return (entityType.getRadius() * 2);
        }
    }

    public int getHeight()
    {
        if (entityType.hasImage())
        {
            return (int) (entityType.getImage().getHeight() * entityType.getImageScale());
        }
        else
        {
            return (entityType.getRadius() * 2);
        }
    }

    public float getAngleInRadians()
    {
        return angleInRadians;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    void setPosition(Point2D.Float value)
    {
        position = value;
    }

    void setAngleInRadians(float value)
    {
        angleInRadians = value;
    }
}
