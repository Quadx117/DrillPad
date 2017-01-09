package drillpad.domain.entity;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 *
 * @author Eric Perron
 */
public class PlayingElement extends Entity implements Serializable
{
    private Player owningPlayer;

    /**
     * Creates a {@code PlayingElement} of the specified {@link EntityType} and
     * at the specified position.
     * <p>
     * This {@code PlayingElement} will not be collidable. This means that it
     * will be able to pass through other colidable entity's. This enables the
     * {@code PlayingElement} to be placed within a {@link Player}'s image.
     * <p>
     * The position is the center point of the {@code PlayingElement}.
     *
     * @param entityType The {@code EntityType} of this {@code PlayingElement}.
     * @param position   The position of this {@code PlayingElement}.
     */
    PlayingElement(EntityType entityType, Point2D.Float position)
    {
        super(entityType, position);
        isCollidable = false;
        owningPlayer = null;
    }

    Player getOwningPlayer()
    {
        return owningPlayer;
    }

    public void setOwningPlayer(Player player, boolean value)
    {
        // NOTE(JFB) : Done this way, to be sure its set to null if the owningPlayer that has it is unselected.
        if (player == owningPlayer && !value)
        {
            owningPlayer = null;
        }
        else if (value)
        {
            owningPlayer = player;
        }
    }

    void setFree()
    {
        owningPlayer = null;
    }

}
