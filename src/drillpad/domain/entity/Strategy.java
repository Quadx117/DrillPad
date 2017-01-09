package drillpad.domain.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import drillpad.domain.event.AnimationPropertiesChangedObserver;
import drillpad.general.utility.SerializationUtilities;

/**
 *
 * @author Eric Perron
 */
public class Strategy implements Serializable
{
    private String name;
    private final List<Entity> entities;
    private final PlayingField playingField;
    private final PlayingElement playingElement;
    private final Animation animation;
    private transient BufferedImage strategyPreviewImage;
    private boolean isMaxNumberOfPlayersRestricted;

    Strategy(String name, boolean isMaxNumberOfPlayersRestricted,
             BufferedImage playingFieldImage,
             float width, float height)
    {
        this.name = name;
        this.isMaxNumberOfPlayersRestricted = isMaxNumberOfPlayersRestricted;
        this.playingField = new PlayingField(playingFieldImage, width, height);
        // TODO(Eric): Delete this and manage null playingElement
        this.playingElement = new PlayingElement(
                new EntityType("default", Color.BLACK, 5, false), null);
        this.entities = new ArrayList<>();
        animation = new Animation(entities);

        // TODO(JFB) : Create function to generate preview image : newStrategy.generatePreviewImage() 
        this.strategyPreviewImage = playingFieldImage;
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        SerializationUtilities.serializeBufferedImage(out, strategyPreviewImage);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        strategyPreviewImage = SerializationUtilities.deserializeBufferedImage(in);
    }

    boolean addEntity(EntityType entityType, Point2D.Float mousePosition)
    {
        Entity entity = new Entity(entityType, mousePosition);
        return entities.add(entity);
    }

    boolean addPlayer(EntityType entityType, Role role, Point2D.Float mousePosition)
    {
        Player player = new Player(entityType, role, mousePosition);
        return entities.add(player);
    }

    boolean addPlayingElement(Point2D.Float mousePosition)
    {
        // TODO(Eric): Handle this in a better way, but still allow multiple instances
        PlayingElement element = new PlayingElement(playingElement.entityType,
                                                    mousePosition);
        return entities.add(element);
    }

    void addAnimationPropertiesChangedObserver(AnimationPropertiesChangedObserver observer)
    {
        animation.addAnimationPropertiesChangedObserver(observer);
    }

    void removeSelectedEntities()
    {
        for (int i = entities.size() - 1; i >= 0; --i)
        {
            if (entities.get(i).isSelected())
            {
                if (entities.get(i) instanceof Player &&
                    getOwnedPlayingElements((Player) entities.get(i)).size() > 0)
                {
                    for (PlayingElement pe : getOwnedPlayingElements((Player) entities.get(i)))
                    {
                        pe.setFree();
                    }
                }

                entities.remove(i);
            }
        }
    }

    public void edit(String newStrategyName,
                     boolean newIsMaxNumberOfPlayersRestricted,
                     BufferedImage newPlayingFieldImage,
                     float newWidth, float newHeight)
    {
        name = newStrategyName;
        isMaxNumberOfPlayersRestricted = newIsMaxNumberOfPlayersRestricted;
        playingField.edit(newPlayingFieldImage, newWidth, newHeight);
    }

    void editPlayer(Player player, EntityType newTeam, Role newRole,
                    String newPlayerName,
                    boolean newIsRoleVisible, boolean newIsNameVisible)
    {
        player.edit(newTeam, newRole, newPlayerName,
                    newIsRoleVisible, newIsNameVisible);
    }

    void editPlayingElement(String newName, Color newColor, int newRadius,
                            BufferedImage newImage, float newImageScale)
    {
        playingElement.entityType.edit(newName, newColor, newRadius,
                                       newImage, newImageScale, false);
    }

    void startAnimation()
    {
        animation.start();
    }

    void pauseAnimation()
    {
        animation.pause();
    }

    void stopAnimation()
    {
        animation.stop();
    }

    void fastForwardAnimation(boolean isPressed)
    {
        animation.fastForward(isPressed);
    }

    void rewindAnimation(boolean isPressed)
    {
        animation.rewind(isPressed);
    }

    void skipForwardAnimation()
    {
        animation.skipForward();
    }

    void skipBackAnimation()
    {
        animation.skipBack();
    }

    void drawPreviousAnimationImages(Graphics2D g2d)
    {
        animation.drawPreviousImages(g2d);
    }

    public void drawPositionsAndArrows(Graphics2D g2d)
    {
        animation.drawPositionsAndArrows(g2d);
    }

    void updateSelectedItemsPosition(Point2D.Float delta)
    {
        List<PlayingElement> playingElements = new ArrayList<>();

        for (Entity entity : entities)
        {
            if (entity instanceof PlayingElement)
            {
                playingElements.add(((PlayingElement) entity));
            }
        }

        for (Entity entity : entities)
        {
            if (entity.isSelected())
            {
                Point2D.Float previousPosition =
                        new Point2D.Float(entity.getPosition().x,
                                          entity.getPosition().y);
                entity.translate(delta);
                if (!(entity instanceof PlayingElement) &&
                    entityCollided(entity))
                {
                    entity.setPosition(previousPosition);
                }
                else
                {
                    // TODO(JFB) : Probably cleaner if Player has List<PlayingElement> instead of putting owningPlayer in PlayingElement.
                    // TODO(Eric) : Player should probably own only one PlayingElement.
                    // And yes it would be better to put it in player so we would not
                    // have to iterate over all PlayingElements.  We could simply add
                    // this in the translation code of the player as an override of
                    // the base method.  Maybe if we have time.
                    for (PlayingElement pe : playingElements)
                    {
                        if (entity instanceof Player &&
                            isPlayingElementOwner(pe, (Player) entity) &&
                            !pe.isSelected())
                        {
                            pe.translate(delta);
                        }
                    }
                }
            }
        }
    }

    private boolean entityCollided(Entity entity)
    {
        boolean result = false;
        for (Entity other : entities)
        {
            // NOTE(Eric): All entities other than PlayingElement are collidable
            if (!entity.equals(other) &&
                !(other instanceof PlayingElement) &&
                entity.isCollidingWith(other))
            {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * Toggles the selection status of all entities that are present at the
     * point where the mouse was pressed and return the {@link List} of selected
     * entities.
     *
     * @param mousePositionInPixel
     * @param zoomFactor
     * @return
     */
    List<Entity> switchSelectionStatus(Point2D.Float mousePosition)
    {
        List<Entity> selectedEntities = new ArrayList<>();
        for (Entity entity : entities)
        {
            if (entity.contains(mousePosition))
            {
                entity.switchSelectionStatus();
            }
            if (entity.isSelected())
            {
                selectedEntities.add(entity);
            }
        }

        return selectedEntities;
    }

    Player getPlayerAtLocation(Point2D.Float mousePosition)
    {
        Player playerAtLocation = null;

        for (Entity entity : entities)
        {
            if (entity.contains(mousePosition) &&
                entity instanceof Player)
            {
                playerAtLocation = (Player) entity;
            }
        }

        return playerAtLocation;
    }

    String getMousePositionAsString(Point2D.Float mousePositionInPixel)
    {
        return playingField.getMousePositionAsString(mousePositionInPixel);
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
        return (this.name.equals(((Strategy) obj).name));
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    public String getName()
    {
        return name;
    }

    public BufferedImage getStrategyPreviewImage()
    {
        return strategyPreviewImage;
    }

    BufferedImage getPlayingFieldImage()
    {
        return playingField.getImage();
    }

    float getPlayingFieldWidth()
    {
        return playingField.getWidth();
    }

    float getPlayingFieldHeight()
    {
        return playingField.getHeight();
    }

    String getPlayingElementName()
    {
        return playingElement.entityType.getName();
    }

    Color getPlayingElementColor()
    {
        return playingElement.entityType.getColor();
    }

    int getPlayingElementRadius()
    {
        return playingElement.entityType.getRadius();
    }

    BufferedImage getPlayingElementImage()
    {
        return playingElement.entityType.getImage();
    }

    float getPlayingElementImageScale()
    {
        return playingElement.entityType.getImageScale();
    }

    List<Entity> getEntities()
    {
        return entities;
    }

    boolean isMaxNumberOfPlayersRestricted()
    {
        return isMaxNumberOfPlayersRestricted;
    }

    boolean isRecording()
    {
        return animation.isRecording();
    }

    boolean isFrameByFrame()
    {
        return animation.isFrameByFrame();
    }

    boolean isRunning()
    {
        return animation.isRunning();
    }

    int getCurrentFrameIndex()
    {
        return animation.getCurrentFrameIndex();
    }

    int getMaxFrameIndex()
    {
        return animation.getMaxFrameIndex();
    }

    String getAnimationStat()
    {
        return animation.getAnimationStat();
    }

    List<PlayingElement> getOwnedPlayingElements(Player player)
    {
        List<PlayingElement> ownedPlayingElements = new ArrayList<>();
        for (Entity entity : entities)
        {
            if (entity instanceof PlayingElement)
            {
                if (player == ((PlayingElement) entity).getOwningPlayer())
                {
                    ownedPlayingElements.add((PlayingElement) entity);
                }
            }
        }

        return ownedPlayingElements;
    }

    private boolean isPlayingElementOwner(PlayingElement playingElement, Player player)
    {
        boolean isOwner = false;
        if (player == playingElement.getOwningPlayer())
        {
            isOwner = true;
        }

        return isOwner;
    }

    int getTeamPlayerCount(String teamName)
    {
        int teamPlayerCount = 0;
        for (Entity entity : entities)
        {
            if (entity instanceof Player &&
                ((Player) entity).getTeamName().equals(teamName))
            {
                ++teamPlayerCount;
            }
        }

        return teamPlayerCount;
    }

    void setStrategyPreviewImage(BufferedImage value)
    {
        strategyPreviewImage = value;
    }

    void setSkipAmountInSeconds(int value)
    {
        animation.setSkipAmountInSeconds(value);
    }

    void setRecording(boolean value)
    {
        animation.setRecording(value);
    }

    void setFrameByFrame(boolean value)
    {
        animation.setFrameByFrame(value);
    }

    void setCurrentFrame(int frame)
    {
        animation.setCurrentFrame(frame);
    }

    void setAnimationSelectedEntity(Entity value)
    {
        animation.setSelectedEntity(value);
    }

}
