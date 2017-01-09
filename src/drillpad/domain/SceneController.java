package drillpad.domain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import drillpad.domain.entity.Entity;
import drillpad.domain.entity.EntityType;
import drillpad.domain.entity.Player;
import drillpad.domain.entity.PlayingElement;
import drillpad.domain.entity.Role;
import drillpad.domain.entity.Sport;
import drillpad.domain.entity.Strategy;
import drillpad.domain.event.AnimationPropertiesChangedObserver;
import drillpad.domain.event.InstanceChangedObservable;
import drillpad.domain.event.InstanceChangedObserver;

/**
 * Class used as the sole entry point of the domain. All classes who want
 * to access the domain must do so using this class.
 * <p>
 * This class uses the singleton design pattern (private construcor, static
 * instance and public getter).
 *
 * @author Eric Perron
 */
public final class SceneController implements Serializable, InstanceChangedObservable
{
    private static SceneController INSTANCE = new SceneController();
    private static Stack<SceneController> UNDO_STATES = new Stack<>();
    private static Stack<SceneController> REDO_STATES = new Stack<>();

    // TODO(Eric): Use a HashMap instead ?
    private final List<Sport> sports;
    private Sport currentSport;
    private String currentProjectPath;
    private float zoomFactor = 1.0f;
    private boolean isPlayersRolesVisible;
    private boolean isPlayersNamesVisible;
    private Player selectedPlayer;
    private PlayingElement selectedPlayingElement;
    private List<String> projectPaths;

    private transient List<InstanceChangedObserver> instanceChangedObservers;

    private SceneController()
    {
        sports = new ArrayList<>();
        instanceChangedObservers = new ArrayList<>();
        projectPaths = new ArrayList<>();
        currentProjectPath = null;
        currentSport = null;
        selectedPlayer = null;
    }

    // Need to save the projectPaths variable in a file to a known location to
    // be able to retrieve the other files paths.
    private void writeProjectLocations()
    {
        String applicationDir =
                SceneController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        applicationDir = new File(applicationDir).getParent();

        try (FileOutputStream fileOut = new FileOutputStream(applicationDir + "/projectPaths.vls");
             ObjectOutputStream out = new ObjectOutputStream(fileOut))
        {
            out.writeObject(projectPaths);
            out.writeObject(currentProjectPath);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SceneController.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }
    }

    // Gets back the saved projectPaths.
    public void readProjectLocations()
    {
        String applicationDir =
                SceneController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        applicationDir = new File(applicationDir).getParent();

        if (new File(applicationDir + "/projectPaths.vls").exists())
        {
            try (FileInputStream fileIn = new FileInputStream(applicationDir + "/projectPaths.vls");
                 ObjectInputStream in = new ObjectInputStream(fileIn))
            {
                INSTANCE.projectPaths = (List<String>) in.readObject();
                if (currentProjectPath == null)
                {
                    INSTANCE.currentProjectPath = (String) in.readObject();
                }
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(SceneController.class.getName()).log(Level.SEVERE,
                                                                      null, ex);
            }
            catch (IOException | ClassNotFoundException ex)
            {
                Logger.getLogger(SceneController.class.getName()).log(Level.SEVERE,
                                                                      null, ex);
            }
        }

    }

    public void save()
    {
        try (FileOutputStream fileOut = new FileOutputStream(currentProjectPath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut))
        {
            out.writeObject(this);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SceneController.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }

        writeProjectLocations();
    }

    public void saveAs(String pathToFile)
    {
        currentProjectPath = pathToFile;
        addProject(pathToFile);

        try (FileOutputStream fileOut = new FileOutputStream(pathToFile);
             ObjectOutputStream out = new ObjectOutputStream(fileOut))
        {
            out.writeObject(this);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SceneController.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }

        writeProjectLocations();
    }

    public void open(String pathToFile)
    {
        try (FileInputStream fileIn = new FileInputStream(pathToFile);
             ObjectInputStream in = new ObjectInputStream(fileIn))
        {
            List<InstanceChangedObserver> tmpList = instanceChangedObservers;
            INSTANCE = (SceneController) in.readObject();
            INSTANCE.instanceChangedObservers = tmpList;
            INSTANCE.UNDO_STATES = new Stack<>();
            INSTANCE.REDO_STATES = new Stack<>();
            readProjectLocations();
            instanceChanged();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(SceneController.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }
        catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(SceneController.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }
    }

    public void saveState()
    {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos))
        {
            out.writeObject(this);

            ByteArrayInputStream bin =
                    new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois =
                    new ObjectInputStream(bin);

            UNDO_STATES.push((SceneController) ois.readObject());
        }
        catch (Exception ex)
        {
            Logger.getLogger(SceneController.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }
    }

    public int undoLastAction()
    {
        if (!UNDO_STATES.empty())
        {
            List<InstanceChangedObserver> tmpList = instanceChangedObservers;

            REDO_STATES.push(INSTANCE);

            // NOTE(JFB) : Removing state equal to actual INSTANCE
            //             Because we always save "after" each action
            if (UNDO_STATES.size() > 1)
            {
                UNDO_STATES.pop();
            }

            INSTANCE = UNDO_STATES.pop();
            INSTANCE.instanceChangedObservers = tmpList;

            instanceChanged();
        }

        return UNDO_STATES.size();
    }

    public int redoLastAction()
    {
        if (!REDO_STATES.empty())
        {
            List<InstanceChangedObserver> tmpList = instanceChangedObservers;

            INSTANCE = REDO_STATES.pop();

            INSTANCE.instanceChangedObservers = tmpList;

            instanceChanged();
        }

        return REDO_STATES.size();
    }

    // TODO(Eric): Move this to a utility class ?
    public BufferedImage loadImageFromDisk(String pathToImage)
    {
        BufferedImage image = null;
        try
        {
            System.out.print("Trying to load: " + pathToImage + " ...");
            image = ImageIO.read(new File(pathToImage));
            System.out.println(" succeeded!");
        }
        catch (IOException e)
        {
            System.err.println(" failed!");
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.err.println(" failed!");
            e.printStackTrace();
        }

        return image;
    }

    public void createProject()
    {
        sports.clear();
        currentSport = null;
        currentProjectPath = null;
    }

    private void addProject(String path)
    {
        if (!projectPaths.contains(path))
        {
            projectPaths.add(path);
        }
    }

    public boolean addSport(String name,
                            int numberOfTeams, int maxNumberOfPlayers,
                            BufferedImage playingFieldImage,
                            float playingFieldWidth, float playingFieldHeight)
    {
        Sport newSport = new Sport(name, maxNumberOfPlayers, numberOfTeams,
                                   playingFieldImage,
                                   playingFieldWidth, playingFieldHeight);

        boolean sportWasAdded = !sports.contains(newSport);
        if (sportWasAdded)
        {
            sports.add(newSport);
            loadSport(newSport.getName());
        }

        return sportWasAdded;
    }

    public void addStrategy(String name, boolean maxPlayerIsRestricted,
                            BufferedImage playingFieldImage,
                            float width, float height)
    {
        currentSport.addStrategy(name, maxPlayerIsRestricted,
                                 playingFieldImage, width, height);
    }

    public void addEntity(String entityType, Point mousePosition)
    {
        currentSport.addEntity(entityType,
                               normalizePosition(mousePosition, zoomFactor));
    }

    public void addPlayer(String entityType, String role, Point mousePosition)
    {
        currentSport.addPlayer(entityType, role,
                               normalizePosition(mousePosition, zoomFactor));
    }

    public void addPlayingElement(Point mousePosition)
    {
        currentSport.addPlayingElement(normalizePosition(mousePosition, zoomFactor));
    }

    public void addRole(String roleName, String roleAbbreviation)
    {
        Role newRole = new Role(roleName, roleAbbreviation);
        currentSport.addRole(newRole);
    }

    public void addEntityType(String name, Color color, int radius,
                              BufferedImage image, float imageScale, boolean isTeam)
    {
        EntityType newEntityType = new EntityType(name, color, radius,
                                                  image, imageScale, isTeam);
        currentSport.addEntityType(newEntityType);
    }

    public void loadSport(String name)
    {
        for (Sport sport : sports)
        {
            if (sport.getName().equals(name))
            {
                currentSport = sport;
                break;
            }
        }
    }

    public void loadStrategy(String name)
    {
        currentSport.loadStrategy(name);
    }

    public void removeProject(String path)
    {
        projectPaths.remove(path);
    }

    public void removeSport(String name)
    {
        for (Sport sport : sports)
        {
            if (sport.getName().equals(name))
            {
                sports.remove(sport);
                break;
            }
        }
    }

    public void removeStrategy(String name)
    {
        currentSport.removeStrategy(name);
    }

    public void removeRole(String Role)
    {
        currentSport.removeRole(Role);
    }

    public void removeEntityType(String name)
    {
        currentSport.removeEntityType(name);
    }

    public void removeSelectedEntities()
    {
        currentSport.removeSelectedEntities();
    }

    public void editCurrentSport(String newSportName,
                                 int newMaxNumberOfTeams, int newMaxNumberOfPlayers,
                                 BufferedImage newPlayingFieldImage,
                                 float newWidth, float newHeight)
    {
        currentSport.edit(newSportName,
                          newMaxNumberOfTeams, newMaxNumberOfPlayers,
                          newPlayingFieldImage, newWidth, newHeight);
    }

    public void editCurrentStrategy(String newStrategyName,
                                    boolean isMaxNumberOfPlayersRestricted,
                                    BufferedImage newPlayingFieldImage,
                                    float newWidth, float newHeight)
    {
        currentSport.editCurrentStrategy(newStrategyName,
                                         isMaxNumberOfPlayersRestricted,
                                         newPlayingFieldImage,
                                         newWidth, newHeight);
    }

    public void editEntityType(String previousName, String newName,
                               Color newColor, int newRadius,
                               BufferedImage newImage, float newImageScale,
                               boolean isTeam)
    {
        currentSport.editEntityType(previousName, newName,
                                    newColor, newRadius,
                                    newImage, newImageScale, isTeam);
    }

    public void editRole(String previousName,
                         String newName, String newAbbreviation)
    {
        currentSport.editRole(previousName, newName, newAbbreviation);
    }

    public void editPlayer(Player player, String newTeamName, String newRoleName,
                           String newPlayerName,
                           boolean newIsRoleVisible, boolean newIsNameVisible)
    {
        currentSport.editPlayer(player, newTeamName, newRoleName, newPlayerName,
                                newIsRoleVisible, newIsNameVisible);
    }

    public void editPlayingElement(String newName, Color newColor, int newRadius,
                                   BufferedImage newImage, float newImageScale)
    {
        currentSport.editPlayingElement(newName, newColor, newRadius,
                                        newImage, newImageScale);
    }

    /**
     * Toggles the selection status of all entities that are present at the
     * point where the mouse was pressed and return whether or not at least one
     * entity is selected.
     *
     * @param mousePositionInPixel The {@link Point} where the mouse click
     *                             occured.
     * @return Number of selected entities
     */
    public int switchSelectionStatus(Point mousePositionInPixel)
    {
        List<Entity> selectedEntities =
                currentSport.switchSelectionStatus(normalizePosition(mousePositionInPixel,
                                                                     zoomFactor));

        if (selectedEntities.size() == 1 &&
            selectedEntities.get(0) instanceof Player)
        {
            selectedPlayer = (Player) selectedEntities.get(0);
        }
        else if (selectedEntities.size() == 1 &&
                 selectedEntities.get(0) instanceof PlayingElement)
        {
            selectedPlayingElement = (PlayingElement) selectedEntities.get(0);
        }
        else
        {
            selectedPlayer = null;
            selectedPlayingElement = null;
        }

        // TODO(Eric): Should we allow animating more than one entity at a time ?
        if (currentSport.isRecording())
        {
            Entity selectedEntity = null;
            if (selectedPlayer != null)
            {
                selectedEntity = selectedPlayer;
            }
            else if (selectedPlayingElement != null)
            {
                selectedEntity = selectedPlayingElement;
            }
            currentSport.setAnimationSelectedEntity(selectedEntity);
        }

        return selectedEntities.size();
    }

    public void updateSelectedItemsPositions(Point delta)
    {
        currentSport.updateSelectedItemsPosition(normalizePosition(delta, zoomFactor));
    }

    public boolean linkPlayingElementToPlayerLoc(Point mousePositionInPixel)
    {
        boolean result = false;
        Player playerAtLocation =
                currentSport.getPlayerAtLocation(normalizePosition(mousePositionInPixel,
                                                                   zoomFactor));

        if (selectedPlayingElement != null)
        {
            if (playerAtLocation != null)
            {
                selectedPlayingElement.setOwningPlayer(playerAtLocation, true);
                result = true;
            }
        }

        return result;
    }

    public void startAnimation()
    {
        currentSport.startAnimation();
    }

    public void pauseAnimation()
    {
        currentSport.pauseAnimation();
    }

    public void stopAnimation()
    {
        currentSport.stopAnimation();
    }

    public void fastForwardAnimation(boolean isPressed)
    {
        if (currentSport != null)
        {
            currentSport.fastForwardAnimation(isPressed);
        }
    }

    public void rewindAnimation(boolean isPressed)
    {
        if (currentSport != null)
        {
            currentSport.rewindAnimation(isPressed);
        }
    }

    public void skipForwardAnimation()
    {
        currentSport.skipForwardAnimation();
    }

    public void skipBackAnimation()
    {
        currentSport.skipBackAnimation();
    }

    public void drawPreviousAnimationImages(Graphics2D g2d)
    {
        currentSport.drawPreviousAnimationImages(g2d);
    }

    public void drawPositionsAndArrows(Graphics2D g2d)
    {
        currentSport.drawPositionsAndArrows(g2d);
    }

    public boolean showRotationImage(Point mousePosition)
    {
        boolean result = false;
        if (selectedPlayer != null &&
            selectedPlayer.showRotationImage(normalizePosition(mousePosition, zoomFactor)))
        {
            result = true;
        }

        return result;
    }

    public void rotateSelectedPlayer(Point previousMousePosition,
                                     Point currentMousePosition)
    {
        if (selectedPlayer != null)
        {
            selectedPlayer.rotate(normalizePosition(previousMousePosition, zoomFactor),
                                  normalizePosition(currentMousePosition, zoomFactor));
        }
    }

    public static SceneController getInstance()
    {
        return INSTANCE;
    }

    public String getZoomLevelAsString()
    {
        return (Integer.toString((int) (zoomFactor * 100)) + " %");
    }

    public String getMousePositionAsString(Point mousePositionInPixel)
    {
        if (currentSport == null)
        {
            return ("");
        }
        else
        {
            return (currentSport.getMousePositionAsString(
                    normalizePosition(mousePositionInPixel, zoomFactor)));
        }
    }

    public Point2D.Float getNormalizedPosition(Point mousePositionInPixel)
    {
        return normalizePosition(mousePositionInPixel, zoomFactor);
    }

    private Point2D.Float normalizePosition(Point point, float zoomFactor)
    {
        float normalizedXPosition = (float) point.x / zoomFactor;
        float normalizedYPosition = (float) point.y / zoomFactor;

        return new Point2D.Float(normalizedXPosition, normalizedYPosition);
    }

    public float getZoomFactor()
    {
        return zoomFactor;
    }

    /**
     * Returns the selected {@link Player} or {@code null} if none.
     * <p>
     * This method will return the selected {@code Player} only if there is
     * exaclty one selected.
     *
     * @return The selected player if exactly one is selected, {@code null}
     *         otherwise
     */
    public Player getSelectedPlayer()
    {
        return selectedPlayer;
    }

    /**
     * Returns the selected {@link PlayingElement} or {@code null} if none.
     * <p>
     * This method will return the selected {@code PlayingElement} only if there
     * is exaclty one selected.
     *
     * @return The selected playing element if exactly one is selected,
     *         {@code null} otherwise.
     */
    public PlayingElement getSelectedPlayingElement()
    {
        return selectedPlayingElement;
    }

    public boolean isPlayerRolesVisible()
    {
        return isPlayersRolesVisible;
    }

    public boolean isPlayerNamesVisible()
    {
        return isPlayersNamesVisible;
    }

    public List<String> getProjectsPath()
    {
        return projectPaths;
    }

    public String getCurrentProjectPath()
    {
        return currentProjectPath;
    }

    public List<Sport> getSports()
    {
        return sports;
    }

    public int getSportsCount()
    {
        return sports.size();
    }

    public String getCurrentSportName()
    {
        return (currentSport == null ?
                "" :
                currentSport.getName());
    }

    public BufferedImage getCurrentSportPlayingFiledImage()
    {
        return (currentSport == null ?
                null :
                currentSport.getPlayingFieldImage());
    }

    public BufferedImage getSportPlayingFieldImage(String sportName)
    {
        BufferedImage result = null;
        for (Sport sport : sports)
        {
            if (sport.getName().equals(sportName))
            {
                result = sport.getPlayingFieldImage();
                break;
            }
        }

        return result;
    }

    public float getCurrentSportPlayingFieldWidth()
    {
        return currentSport.getPlayingFieldWidth();
    }

    public float getSportPlayingFieldWidth(String sportName)
    {
        float result = 0.0f;
        for (Sport sport : sports)
        {
            if (sport.getName().equals(sportName))
            {
                result = sport.getPlayingFieldWidth();
                break;
            }
        }

        return result;
    }

    public float getCurrentSportPlayingFieldHeight()
    {
        return currentSport.getPlayingFieldHeight();
    }

    public float getSportPlayingFieldHeight(String sportName)
    {
        float result = 0.0f;
        for (Sport sport : sports)
        {
            if (sport.getName().equals(sportName))
            {
                result = sport.getPlayingFieldHeight();
                break;
            }
        }

        return result;
    }

    public int getMaxNumberOfTeams()
    {
        return currentSport.getMaxNumberOfTeams();
    }

    public int getMaxNumberOfTeams(String sportName)
    {
        int result = 0;
        for (Sport sport : sports)
        {
            if (sport.getName().equals(sportName))
            {
                result = sport.getMaxNumberOfTeams();
                break;
            }
        }

        return result;
    }

    public int getMaxNumberOfPlayers()
    {
        return currentSport.getMaxNumberOfPlayers();
    }

    public int getMaxNumberOfPlayers(String sportName)
    {
        int result = 0;
        for (Sport sport : sports)
        {
            if (sport.getName().equals(sportName))
            {
                result = sport.getMaxNumberOfPlayers();
                break;
            }
        }

        return result;
    }

    public int getTeamPlayerCount(String teamName)
    {
        return currentSport.getTeamPlayerCount(teamName);
    }

    public boolean isEntityTypeTeam(String entityTypeName)
    {
        return currentSport.isEntityTypeTeam(entityTypeName);
    }

    public List<String> getRoleNames()
    {
        return currentSport.getRoleNames();
    }

    public List<String> getEntityTypeNames()
    {
        return currentSport.getEntityTypeNames();
    }

    public Color getEntityTypeColor(String name)
    {
        return currentSport.getEntityTypeColor(name);
    }

    public int getEntityTypeRadius(String name)
    {
        return currentSport.getEntityTypeRadius(name);
    }

    public BufferedImage getEntityTypeImage(String name)
    {
        return currentSport.getEntityTypeImage(name);
    }

    public float getEntityTypeImageScale(String name)
    {
        return currentSport.getEntityTypeImageScale(name);
    }

    public String getRoleAbbreviation(String roleName)
    {
        return currentSport.getRoleAbbreviation(roleName);
    }

    public Dimension getImageDimension()
    {
        Dimension result = new Dimension();
        if (currentSport != null)
        {
            result = currentSport.getCurrentStrategyImageDimension();
        }

        return result;
    }

    public List<Strategy> getStrategies(String sportName)
    {
        List<Strategy> strategies = null;
        for (Sport sport : sports)
        {
            if (sport.getName().equals(sportName))
            {
                strategies = sport.getStrategies();
                break;
            }
        }

        return strategies;
    }

    public String getCurrentStrategyName()
    {
        return currentSport.getCurrentStrategyName();
    }

    public float getCurrentStrategyPlayingFieldWidth()
    {
        return currentSport.getCurrentStrategyPlayingFieldWidth();
    }

    public float getCurrentStrategyPlayingFieldHeight()
    {
        return currentSport.getCurrentStrategyPlayingFieldHeight();
    }

    public boolean isCurrentStrategyMaxNumberOfPlayersRestricted()
    {
        return currentSport.isCurrentStrategyMaxNumberOfPlayersRestricted();
    }

    public int getCurrentSportStrategyCount()
    {
        return (currentSport == null ?
                0 :
                currentSport.getStrategyCount());
    }

    public int getSportStrategyCount(String sportName)
    {
        int result = 0;
        for (Sport sport : sports)
        {
            if (sport.getName().equals(sportName))
            {
                result = sport.getStrategyCount();
                break;
            }
        }

        return result;
    }

    public LinkedHashMap<String, BufferedImage> getStrategyPreviewImages()
    {
        return currentSport.getStrategyPreviewImages();
    }

    public BufferedImage getCurrentStrategyPreviewImage()
    {
        return currentSport.getCurrentStrategyPreviewImage();
    }

    public BufferedImage getCurrentStrategyPlayingFieldImage()
    {
        // TODO(Eric): BufferedImage image = new BufferedImage(); ?
        return (currentSport == null ? null :
                currentSport.getCurrentStrategyPlayingFieldImage());
    }

    public List<Entity> getEntities()
    {
        return currentSport.getCurrentStrategyEntities();
    }

    public String getPlayingElementName()
    {
        return currentSport.getPlayingElementName();
    }

    public Color getPlayingElementColor()
    {
        return currentSport.getPlayingElementColor();
    }

    public int getPlayingElementRadius()
    {
        return currentSport.getPlayingElementRadius();
    }

    public BufferedImage getPlayingElementImage()
    {
        return currentSport.getPlayingElementImage();
    }

    public float getPlayingElementImageScale()
    {
        return currentSport.getPlayingElementImageScale();
    }

    public String getAnimationStat()
    {
        return (currentSport == null ?
                "Image : 0 de 0" :
                currentSport.getAnimationStat());
    }

    public int getCurrentFrameIndex()
    {
        return ((currentSport == null) ?
                0 :
                currentSport.getCurrentFrameIndex());
    }

    public int getMaxFrameIndex()
    {
        return ((currentSport == null) ?
                0 :
                currentSport.getMaxFrameIndex());
    }

    public boolean isAnimationFrameByFrame()
    {
        return currentSport.isCurrentStrategyFrameByFrame();
    }

    public boolean isAnimationRunning()
    {
        return currentSport.isAnimationRunning();
    }

    /**
     * Set {@code isPlayerRolesVisible} to the specified value.
     * <p>
     * {@code isPlayerRolesVisible} is the attribute that determines globally if
     * all player's role should be visible or not.
     * <p>
     * Setting this value to {@code false} will hide all player's role.
     * <p>
     * If this value is set to {@code true}, the player's role visibility will
     * be determined individually by each player's {@code isRoleVisible}
     * attribute
     *
     * @param value The new value for this attribute.
     */
    public void setPlayerRolesVisible(boolean value)
    {
        isPlayersRolesVisible = value;
    }

    /**
     * Set {@code isPlayerRolesVisible} to the specified value.
     * <p>
     * {@code isPlayerRolesVisible} is the attribute that determines globally if
     * all player's role should be visible or not.
     * <p>
     * Setting this value to {@code false} will hide all player's role.
     * <p>
     * If this value is set to {@code true}, the player's role visibility will
     * be determined individually by each player's {@code isRoleVisible}
     * attribute
     *
     * @param value The new value for this attribute.
     */
    public void setPlayerNamesVisible(boolean value)
    {
        isPlayersNamesVisible = value;
    }

    public void setZoomFactor(float value)
    {
        zoomFactor = value;
    }

    public void setCurrentStrategyPreviewImage(JPanel panel)
    {
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(),
                                                BufferedImage.TYPE_INT_ARGB);
        panel.paint(image.getGraphics());
        currentSport.setCurrentStrategyPreviewImage(image);
    }

    public void setSkipAmountInSeconds(int value)
    {
        currentSport.setSkipAmountInSeconds(value);
    }

    public void setRecording(boolean value)
    {
        currentSport.setRecording(value);
    }

    public void setFrameByFrameAnimation(boolean value)
    {
        currentSport.setFrameByFrame(value);
    }

    public void setCurrentFrame(int frame)
    {
        currentSport.setCurrentFrame(frame);
    }

    public void addAnimationPropertiesChangedObserver(AnimationPropertiesChangedObserver observer)
    {
        currentSport.addAnimationPropertiesChangedObserver(observer);
    }

    // NOTE(Eric): This method must be used by all observers after instanceChanged
    // to add themselves back to all AnimationPropertiesChangedObserver lists.
    public void addToAllAnimationPropertiesChangedObserver(AnimationPropertiesChangedObserver observer)
    {
        for (Sport sport : sports)
        {
            sport.addToAllAnimationPropertiesChangedObserver(observer);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInstanceChangedObserver(InstanceChangedObserver object) throws NullPointerException
    {
        if (object == null)
        {
            throw new NullPointerException();
        }
        if (!instanceChangedObservers.contains(object))
        {
            instanceChangedObservers.add(object);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteInstanceChangedObserver(InstanceChangedObserver object)
    {
        instanceChangedObservers.remove(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteInstanceChangedObservers()
    {
        instanceChangedObservers.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void instanceChanged()
    {
        for (InstanceChangedObserver observer : instanceChangedObservers)
        {
            observer.instanceChanged();
        }
    }

}
