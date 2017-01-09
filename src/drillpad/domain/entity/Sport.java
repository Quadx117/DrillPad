package drillpad.domain.entity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import drillpad.domain.event.AnimationPropertiesChangedObserver;

/**
 *
 * @author Eric Perron
 */
public class Sport implements Serializable
{
    private String name;
    private final HashMap<String, Role> roles;
    private final HashMap<String, EntityType> entityTypes;
    // TODO(Eric): Use a HashMap instead ?
    private final List<Strategy> strategies;
    private Strategy currentStrategy;
    private int maxNumberOfTeams;
    private int maxNumberOfPlayers;
    private final PlayingField playingField;

    public Sport(String name,
                 int maxNumberOfPlayers, int maxNumberOfTeams,
                 BufferedImage playingFieldImage,
                 float playingFieldWidth, float playingFieldHeight)
    {
        this.name = name;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.maxNumberOfTeams = maxNumberOfTeams;
        roles = new HashMap<>();
        entityTypes = new HashMap<>();
        strategies = new ArrayList<>();
        playingField = new PlayingField(playingFieldImage,
                                        playingFieldWidth, playingFieldHeight);

        // TODO(Eric): Handle null here (disable all buttons and menuItems and
        // enable them when creating a strategy.  Need to disable everything again
        // when deleting the last strategy
        currentStrategy = new Strategy("default", true,
                                       playingFieldImage,
                                       playingFieldWidth, playingFieldHeight);
    }

    /**
     * Adds a {@code Strategy} if it is not already part of the underlying
     * {@code Collection}.
     * <p>
     * If {@code maxPlayerIsRestricted} is set to {@code true}, adding a
     * {@link Player} to the current {@code Strategy} when the maximum is
     * already met will have no effect.
     *
     * @param name                           The {@link String} used to identify
     *                                       the
     *                                       strategy.
     * @param isMaxNumberOfPlayersRestricted Whether or not the maximum number
     *                                       of players
     *                                       per team is checked before adding a new
     *                                       player.
     * @param playingFieldImage              The {@link BufferedImage} used as
     *                                       the
     *                                       strategy's playing field image.
     * @param width                          The width in meters of the
     *                                       strategy's
     *                                       playing field.
     * @param height                         The height in meters of the
     *                                       strategy's
     *                                       playing field.
     *
     * @return {@code true} if the element was added to the underlying
     *         {@code Collection}, {@code false} otherwise.
     */
    public boolean addStrategy(String name,
                               boolean isMaxNumberOfPlayersRestricted,
                               BufferedImage playingFieldImage,
                               float width, float height)
    {
        Strategy strategy = new Strategy(name, isMaxNumberOfPlayersRestricted,
                                         playingFieldImage, width, height);
        boolean result = false;
        if (!strategies.contains(strategy))
        {
            result = strategies.add(strategy);
            currentStrategy = strategy;
        }

        return result;
    }

    /**
     * Adds a {@link Role} in the underlying {@code Collection}.
     * <p>
     * If the {@code Role}, already exists, it is replaced by the new one.
     *
     * @param role The {@code Role} to add to the underlying {@code Collection}.
     */
    public void addRole(Role role)
    {
        roles.put(role.getName(), role);
    }

    /**
     * Adds an {@link EntityType} in the underlying {@code Collection}.
     * <p>
     * If the {@code EntityType}, already exists, it is replaced by the new one.
     *
     * @param entityType The {@code EntityType} to add to the underlying
     *                   {@code Collection}.
     */
    public void addEntityType(EntityType entityType)
    {
        entityTypes.put(entityType.getName(), entityType);
    }

    public void addEntity(String entityTypeName, Point2D.Float mousePosition)
    {
        currentStrategy.addEntity(entityTypes.get(entityTypeName),
                                  mousePosition);
    }

    public void addPlayer(String entityTypeName, String roleName,
                          Point2D.Float mousePosition)
    {
        currentStrategy.addPlayer(entityTypes.get(entityTypeName),
                                  roles.get(roleName),
                                  mousePosition);
    }

    public void addPlayingElement(Point2D.Float mousePosition)
    {
        currentStrategy.addPlayingElement(mousePosition);
    }

    public void addAnimationPropertiesChangedObserver(AnimationPropertiesChangedObserver observer)
    {
        currentStrategy.addAnimationPropertiesChangedObserver(observer);
    }

    public void addToAllAnimationPropertiesChangedObserver(AnimationPropertiesChangedObserver observer)
    {
        for (Strategy strategy : strategies)
        {
            strategy.addAnimationPropertiesChangedObserver(observer);
        }
    }

    public void removeStrategy(String name)
    {
        for (Strategy strategy : strategies)
        {
            if (strategy.getName().equals(name))
            {
                strategies.remove(strategy);
                break;
            }
        }
    }

    public void removeRole(String name)
    {
        roles.remove(name);
    }

    public void removeEntityType(String name)
    {
        entityTypes.remove(name);
    }

    public void removeSelectedEntities()
    {
        currentStrategy.removeSelectedEntities();
    }

    public void edit(String newSportName,
                     int newMaxNumberOfTeams, int newMaxNumberOfPlayers,
                     BufferedImage newPlayingFieldImage,
                     float newWidth, float newHeight)
    {
        name = newSportName;
        maxNumberOfTeams = newMaxNumberOfTeams;
        maxNumberOfPlayers = newMaxNumberOfPlayers;
        playingField.edit(newPlayingFieldImage, newWidth, newHeight);
    }

    public void editCurrentStrategy(String newStrategyName,
                                    boolean isMaxNumberOfPlayersRestricted,
                                    BufferedImage newPlayingFieldImage,
                                    float newWidth, float newHeight)
    {
        currentStrategy.edit(newStrategyName, isMaxNumberOfPlayersRestricted,
                             newPlayingFieldImage, newWidth, newHeight);
    }

    public void editEntityType(String previousName, String newName,
                               Color newColor, int newRadius,
                               BufferedImage newImage, float newImageScale,
                               boolean newIsTeam)
    {
        // TODO(Eric): else we have a problem, what should we do ?
        if (entityTypes.containsKey(previousName))
        {
            // NOTE(Eric): Since insertion and removal is fast with a HashMap
            // and it will never grow big enough, it is safe to always remove
            // the EntityType and put it back, even if the key hasn't changed.
            EntityType entityType = entityTypes.remove(previousName);
            entityType.edit(newName, newColor, newRadius,
                            newImage, newImageScale, newIsTeam);
            entityTypes.put(newName, entityType);
        }
    }

    public void editRole(String previousName,
                         String newName, String newAbbreviation)
    {
        // TODO(Eric): else we have a problem, what should we do ?
        if (roles.containsKey(previousName))
        {
            // NOTE(Eric): Since insertion and removal is fast with a HashMap
            // and it will never grow big enough, it is safe to always remove
            // the Role and put it back, even if the key hasn't changed.
            Role role = roles.remove(previousName);
            role.edit(newName, newAbbreviation);
            roles.put(newName, role);
        }
    }

    public void editPlayer(Player player, String newTeamName, String newRoleName,
                           String newPlayerName,
                           boolean newIsRoleVisible, boolean newIsNameVisible)
    {
        currentStrategy.editPlayer(player, entityTypes.get(newTeamName),
                                   roles.get(newRoleName), newPlayerName,
                                   newIsRoleVisible, newIsNameVisible);
    }

    public void editPlayingElement(String newName, Color newColor, int newRadius,
                                   BufferedImage newImage, float newImageScale)
    {
        currentStrategy.editPlayingElement(newName, newColor, newRadius,
                                           newImage, newImageScale);
    }

    public void loadStrategy(String name)
    {
        for (Strategy strategy : strategies)
        {
            if (strategy.getName().equals(name))
            {
                currentStrategy = strategy;
                break;
            }
        }
    }

    public List<Entity> switchSelectionStatus(Point2D.Float mousePositionInPixel)
    {
        return currentStrategy.switchSelectionStatus(mousePositionInPixel);
    }

    public void updateSelectedItemsPosition(Point2D.Float delta)
    {
        currentStrategy.updateSelectedItemsPosition(delta);
    }

    public void startAnimation()
    {
        currentStrategy.startAnimation();
    }

    public void pauseAnimation()
    {
        currentStrategy.pauseAnimation();
    }

    public void stopAnimation()
    {
        currentStrategy.stopAnimation();
    }

    public void fastForwardAnimation(boolean isPressed)
    {
        if (currentStrategy != null)
        {
            currentStrategy.fastForwardAnimation(isPressed);
        }
    }

    public void rewindAnimation(boolean isPressed)
    {
        if (currentStrategy != null)
        {
            currentStrategy.rewindAnimation(isPressed);
        }
    }

    public void skipForwardAnimation()
    {
        currentStrategy.skipForwardAnimation();
    }

    public void skipBackAnimation()
    {
        currentStrategy.skipBackAnimation();
    }

    public void drawPreviousAnimationImages(Graphics2D g2d)
    {
        currentStrategy.drawPreviousAnimationImages(g2d);
    }

    public void drawPositionsAndArrows(Graphics2D g2d)
    {
        currentStrategy.drawPositionsAndArrows(g2d);
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
        return (this.name.equals(((Sport) obj).name));
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    public String getMousePositionAsString(Point2D.Float mousePositionInPixel)
    {
        if (currentStrategy == null)
        {
            return (playingField.getMousePositionAsString(mousePositionInPixel));
        }
        else
        {
            return (currentStrategy.getMousePositionAsString(mousePositionInPixel));
        }
    }

    public String getName()
    {
        return name;
    }

    public int getMaxNumberOfPlayers()
    {
        return maxNumberOfPlayers;
    }

    public int getMaxNumberOfTeams()
    {
        return maxNumberOfTeams;
    }

    public BufferedImage getPlayingFieldImage()
    {
        return playingField.getImage();
    }

    public float getPlayingFieldWidth()
    {
        return playingField.getWidth();
    }

    public float getPlayingFieldHeight()
    {
        return playingField.getHeight();
    }

    public List<String> getRoleNames()
    {
        List roleNames = new ArrayList();
        for (Role role : roles.values())
        {
            roleNames.add(role.getName());
        }

        Collections.sort(roleNames, Collator.getInstance());

        return roleNames;
    }

    public String getRoleAbbreviation(String roleName)
    {
        return roles.get(roleName).getAbbreviation();
    }

    public List<String> getEntityTypeNames()
    {
        List entityTypeNames = new ArrayList();
        for (EntityType entityType : entityTypes.values())
        {
            entityTypeNames.add(entityType.getName());
        }

        Collections.sort(entityTypeNames, Collator.getInstance());

        return entityTypeNames;
    }

    public Color getEntityTypeColor(String name)
    {
        Color entityTypeColor = null;
        if (entityTypes.containsKey(name))
        {
            entityTypeColor = entityTypes.get(name).getColor();
        }

        return entityTypeColor;
    }

    public int getEntityTypeRadius(String name)
    {
        int entityTypeDiameter = 0;
        if (entityTypes.containsKey(name))
        {
            entityTypeDiameter = entityTypes.get(name).getRadius();
        }

        return entityTypeDiameter;
    }

    public BufferedImage getEntityTypeImage(String name)
    {
        BufferedImage entityTypeImage = null;
        if (entityTypes.containsKey(name))
        {
            entityTypeImage = entityTypes.get(name).getImage();
        }

        return entityTypeImage;
    }

    public float getEntityTypeImageScale(String name)
    {
        float entityTypeImageScale = 1f;
        if (entityTypes.containsKey(name))
        {
            entityTypeImageScale = entityTypes.get(name).getImageScale();
        }

        return entityTypeImageScale;
    }

    public List<Strategy> getStrategies()
    {
        return strategies;
    }

    public int getStrategyCount()
    {
        return strategies.size();
    }

    public LinkedHashMap<String, BufferedImage> getStrategyPreviewImages()
    {
        LinkedHashMap<String, BufferedImage> strategiesImages = new LinkedHashMap<>();

        for (Strategy strategy : strategies)
        {
            strategiesImages.put(strategy.getName(), strategy.getStrategyPreviewImage());
        }

        return strategiesImages;
    }

    public boolean isEntityTypeTeam(String entityTypeName)
    {
        return entityTypes.get(entityTypeName).isTeam();
    }

    public String getCurrentStrategyName()
    {
        return currentStrategy.getName();
    }

    public List<Entity> getCurrentStrategyEntities()
    {
        if (currentStrategy == null)
        {
            return (new ArrayList<>());
        }
        else
        {
            return currentStrategy.getEntities();
        }
    }

    public boolean isCurrentStrategyMaxNumberOfPlayersRestricted()
    {
        return currentStrategy.isMaxNumberOfPlayersRestricted();
    }

    public float getCurrentStrategyPlayingFieldWidth()
    {
        return currentStrategy.getPlayingFieldWidth();
    }

    public float getCurrentStrategyPlayingFieldHeight()
    {
        return currentStrategy.getPlayingFieldHeight();
    }

    public Dimension getCurrentStrategyImageDimension()
    {
        Dimension result = new Dimension();
        // NOTE(Eric): This is never null since we create a default strategy
        // with the sport playingField when there is none.
        if (currentStrategy != null)
        {
            result.width = currentStrategy.getPlayingFieldImage().getWidth();
            result.height = currentStrategy.getPlayingFieldImage().getHeight();
        }

        return result;
    }

    public int getTeamPlayerCount(String teamName)
    {
        return currentStrategy.getTeamPlayerCount(teamName);
    }

    public BufferedImage getCurrentStrategyPlayingFieldImage()
    {
        BufferedImage result;
        // NOTE(Eric): This is never null since we create a default strategy
        // with the sport playingField when there is none.
        if (currentStrategy == null)
        {
            result = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        else
        {
            result = currentStrategy.getPlayingFieldImage();
        }

        return result;
    }

    public BufferedImage getCurrentStrategyPreviewImage()
    {
        return currentStrategy.getStrategyPreviewImage();
    }

    public String getPlayingElementName()
    {
        return currentStrategy.getPlayingElementName();
    }

    public Color getPlayingElementColor()
    {
        return currentStrategy.getPlayingElementColor();
    }

    public int getPlayingElementRadius()
    {
        return currentStrategy.getPlayingElementRadius();
    }

    public BufferedImage getPlayingElementImage()
    {
        return currentStrategy.getPlayingElementImage();
    }

    public float getPlayingElementImageScale()
    {
        return currentStrategy.getPlayingElementImageScale();
    }

    public Player getPlayerAtLocation(Point2D.Float mousePositionInPixel)
    {
        return currentStrategy.getPlayerAtLocation(mousePositionInPixel);
    }

    public int getCurrentFrameIndex()
    {
        return currentStrategy.getCurrentFrameIndex();
    }

    public int getMaxFrameIndex()
    {
        return currentStrategy.getMaxFrameIndex();
    }

    public String getAnimationStat()
    {
        if (currentStrategy == null)
        {
            return "Image : 0 de 0";
        }
        else
        {
            return currentStrategy.getAnimationStat();
        }
    }

    public boolean isRecording()
    {
        return currentStrategy.isRecording();
    }

    public boolean isCurrentStrategyFrameByFrame()
    {
        return currentStrategy.isFrameByFrame();
    }

    public boolean isAnimationRunning()
    {
        return currentStrategy.isRunning();
    }

    public void setCurrentStrategyPreviewImage(BufferedImage value)
    {
        currentStrategy.setStrategyPreviewImage(value);
    }

    public void setSkipAmountInSeconds(int value)
    {
        currentStrategy.setSkipAmountInSeconds(value);
    }

    public void setRecording(boolean value)
    {
        currentStrategy.setRecording(value);
    }

    public void setFrameByFrame(boolean value)
    {
        currentStrategy.setFrameByFrame(value);
    }

    public void setCurrentFrame(int frame)
    {
        currentStrategy.setCurrentFrame(frame);
    }

    public void setAnimationSelectedEntity(Entity value)
    {
        currentStrategy.setAnimationSelectedEntity(value);
    }

}
