package drillpad.domain.entity;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Timer;

import drillpad.domain.event.AnimationPropertiesChangedObservable;
import drillpad.domain.event.AnimationPropertiesChangedObserver;
import drillpad.general.utility.MathUtilities;

/**
 *
 * @author Eric Perron
 */
public class Animation implements AnimationPropertiesChangedObservable, Serializable
{
    private final List<Entity> entities;
    private final List<HashMap<Entity, PositionAndAngle>> frames;
    private final List<Integer> previousFrames;
    private Timer timer;
    private final int framesPerSecond = 30;
    private final int normalSpeedInFrames = 1;
    private final int fasterSpeedInFrames = 4;
    private Entity selectedEntity;
    private int playSpeedInFrames;
    private int skipAmountInSeconds;
    private int currentFrame;
    private boolean isRecording;
    private boolean isFrameByFrame = true;

    private transient List<AnimationPropertiesChangedObserver> animationChangedObservers;

    Animation(List<Entity> entities)
    {
        this.entities = entities;
        frames = new ArrayList<>();
        previousFrames = new ArrayList<>();
        animationChangedObservers = new ArrayList<>();
        timer = new Timer(1000 / framesPerSecond, new ActionListener()
                  {
                      @Override
                      public void actionPerformed(ActionEvent e)
                      {
                          runAnimation();
                      }
                  });
        currentFrame = 0;
        playSpeedInFrames = normalSpeedInFrames;
        skipAmountInSeconds = 3;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        timer = new Timer(1000 / framesPerSecond, new ActionListener()
                  {
                      @Override
                      public void actionPerformed(ActionEvent e)
                      {
                          runAnimation();
                      }
                  });

        // NOTE(Eric): Every observer will need to add itself back after deserialization
        animationChangedObservers = new ArrayList<>();
    }

    /**
     * Starts the animation from the last known position. If the animation is
     * already at the end, nothing happens.
     */
    void start()
    {
        if (currentFrame < frames.size() ||
            (isRecording &&
             !isFrameByFrame &&
             currentFrame <= frames.size()))
        {
            timer.start();
        }
    }

    /**
     * Pauses the animation at the current frame. The next {@link #start())
     * action will continue the animation from where it left off.
     */
    void pause()
    {
        timer.stop();
    }

    /**
     * Stops the animation and reposition it to the start.
     */
    void stop()
    {
        timer.stop();
        currentFrame = 0;
        if (frames.size() > 0)
        {
            updatePositions();
        }
    }

    /**
     *
     */
    void fastForward(boolean isPressed)
    {
        if (isPressed)
        {
            playSpeedInFrames = fasterSpeedInFrames;
        }
        else
        {
            playSpeedInFrames = normalSpeedInFrames;
        }
    }

    /**
     *
     */
    void rewind(boolean isPressed)
    {
        if (isPressed)
        {
            playSpeedInFrames = -fasterSpeedInFrames;
        }
        else
        {
            playSpeedInFrames = normalSpeedInFrames;
        }
    }

    /**
     * Skip forward by a predefined amount of seconds.
     * <p>
     * The amount of second is specified by {@link #skipAmountInSeconds}.
     */
    void skipForward()
    {
        if (isRecording &&
            isFrameByFrame &&
            !timer.isRunning())
        {
            int currentImageIndex = getCurrentFrameIndex();
            if (!previousFrames.isEmpty() &&
                currentFrame == previousFrames.get(currentImageIndex))
            {
                previousFrames.set(currentImageIndex, currentFrame);

                // NOTE(Eric): Delete the rest of the animation for now.
                // Latter, we could do something better here.
                for (int i = previousFrames.size() - 1;
                     i > currentImageIndex;
                     --i)
                {
                    previousFrames.remove(i);
                }

                for (int i = frames.size() - 1;
                     i > currentFrame;
                     --i)
                {
                    frames.remove(i);
                }
            }
            else if (!previousFrames.isEmpty() &&
                     currentFrame > previousFrames.get(currentImageIndex) &&
                     currentFrame < frames.size() - 1)
            {
                // NOTE(Eric): Delete the rest of the animation for now.
                // Latter, we could do something better here.
                for (int i = previousFrames.size() - 1;
                     i > currentImageIndex;
                     --i)
                {
                    previousFrames.remove(i);
                }

                for (int i = frames.size() - 1;
                     i > currentFrame;
                     --i)
                {
                    frames.remove(i);
                }

                previousFrames.add(currentFrame);
            }
            else
            {
                previousFrames.add(currentFrame);
            }

            for (int i = framesPerSecond * skipAmountInSeconds;
                 i > 0;
                 --i)
            {
                HashMap<Entity, PositionAndAngle> map = new HashMap<>();
                for (Entity entity : entities)
                {
                    map.put(entity,
                            new PositionAndAngle(new Point2D.Float(entity.getPosition().x,
                                                                   entity.getPosition().y),
                                                 entity.getAngleInRadians()));
                }
                if (currentFrame == frames.size())
                {
                    frames.add(map);
                }
                else
                {
                    frames.set(currentFrame, map);
                }
                ++currentFrame;
            }
            animationPropertiesChanged();
        }
        else
        {
            currentFrame += framesPerSecond * skipAmountInSeconds;
            if (currentFrame >= frames.size())
            {
                stop();
            }
            else if (!timer.isRunning())
            {
                updatePositions();
            }
        }
    }

    /**
     * Skip back by a predefined amount of seconds.
     * <p>
     * The amount of second is specified by {@link #skipAmountInSeconds}.
     */
    void skipBack()
    {
        currentFrame -= framesPerSecond * skipAmountInSeconds;
        if (currentFrame < 0)
        {
            currentFrame = 0;
        }

        if (!timer.isRunning() &&
            !frames.isEmpty())
        {
            updatePositions();
        }
    }

    // TODO(Eric): Je ne suis plus certain que c'est OK de cette façon pour le
    // principe de la séparation du UI et du domain, mais bon on a pas eu de
    // commentaires des correcteurs à ce sujet.
    void drawPreviousImages(Graphics2D g2d)
    {
        if (isRecording)
        {
            Composite previousComposite = g2d.getComposite();
            for (int index = 0;
                 index < previousFrames.size();
                 ++index)
            {
                float weight = 0f;
                if (previousFrames.size() > 1)
                {
                    weight = (float) index / (float) (previousFrames.size() - 1);
                }
                float alpha = MathUtilities.lerpPrecise(0.2f, 0.8f, weight);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                            alpha));
                HashMap<Entity, PositionAndAngle> map = frames.get(previousFrames.get(index));
                for (Entity entity : map.keySet())
                {
                    Point2D.Float previousPosition = entity.getPosition();
                    float previousAngle = entity.getAngleInRadians();
                    PositionAndAngle positionAndAngle = map.get(entity);
                    entity.setPosition(positionAndAngle.position);
                    entity.setAngleInRadians(positionAndAngle.angleInRadians);
                    entity.draw(g2d);
                    entity.setPosition(previousPosition);
                    entity.setAngleInRadians(previousAngle);
                }
            }
            g2d.setComposite(previousComposite);
        }
    }

    public void drawPositionsAndArrows(Graphics2D g2d)
    {
        int radiusKeyPositions = 10;
        int radiusArrow = 3;
        HashMap<Entity, PositionAndAngle> firstPositions = new HashMap<>();
        HashMap<Entity, PositionAndAngle> lastPositions = new HashMap<>();

        // Get every frames position by Entity (To be able to draw line)
        HashMap<Entity, List<PositionAndAngle>> entityPositions = new HashMap<>();

        for (int index = 0;
             index < frames.size();
             ++index)
        {
            HashMap<Entity, PositionAndAngle> map = frames.get(index);

            for (Entity entity : map.keySet())
            {
                entityPositions.putIfAbsent(entity, new ArrayList<>());

                for (Entity newEntity : entityPositions.keySet())
                {
                    if (newEntity.equals(entity))
                    {
                        entityPositions.get(newEntity).add(map.get(entity));
                    }
                }
            }
        }

        // Trace lines between positions.
        for (HashMap.Entry<Entity, List<PositionAndAngle>> entry : entityPositions.entrySet())
        {
            for (int index = 0;
                 index < entry.getValue().size() - 1;
                 ++index)
            {
                Point2D.Float pos = entry.getValue().get(index).position;
                float angle = entry.getValue().get(index).angleInRadians;
                Point2D.Float nextPos = entry.getValue().get(index + 1).position;
                float nexAngle = entry.getValue().get(index + 1).angleInRadians;
                firstPositions.putIfAbsent(entry.getKey(),
                                           new PositionAndAngle(pos, nexAngle));
                lastPositions.put(entry.getKey(),
                                  new PositionAndAngle(nextPos, angle));

                g2d.setColor(entry.getKey().entityType.getColor());

                if (entry.getKey() instanceof PlayingElement)
                {
                    g2d.setStroke(new BasicStroke(0.1f,
                                                  BasicStroke.CAP_BUTT,
                                                  BasicStroke.JOIN_MITER,
                                                  1.0f, new float[]
                                                  {
                                                      5.0f
                            }, 0f));
                }
                else
                {
                    g2d.setStroke(new BasicStroke(radiusArrow));
                }

                g2d.drawLine((int) pos.getX(),
                             (int) pos.getY(),
                             (int) nextPos.getX(),
                             (int) nextPos.getY());

            }
        }

        // Draw first positions with transparency
        Composite previousComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                    0.3f));
        for (Entity entity : firstPositions.keySet())
        {
            Point2D.Float previousPosition = entity.getPosition();
            float previousAngle = entity.getAngleInRadians();
            PositionAndAngle positionAndAngle = firstPositions.get(entity);
            entity.setPosition(positionAndAngle.position);
            entity.setAngleInRadians(positionAndAngle.angleInRadians);
            entity.draw(g2d);
            entity.setPosition(previousPosition);
            entity.setAngleInRadians(previousAngle);
        }
        g2d.setComposite(previousComposite);

        // Draw last positions
        for (Entity entity : lastPositions.keySet())
        {
            Point2D.Float previousPosition = entity.getPosition();
            float previousAngle = entity.getAngleInRadians();
            PositionAndAngle positionAndAngle = lastPositions.get(entity);
            entity.setPosition(positionAndAngle.position);
            entity.setAngleInRadians(positionAndAngle.angleInRadians);
            entity.draw(g2d);
            entity.setPosition(previousPosition);
            entity.setAngleInRadians(previousAngle);
        }

        // Draw every "inter" positions
        for (int index = 0;
             index < previousFrames.size();
             ++index)
        {
            HashMap<Entity, PositionAndAngle> map = frames.get(previousFrames.get(index));

            for (Entity entity : map.keySet())
            {
                if (!lastPositions.get(entity).equals(map.get(entity)) &&
                    !firstPositions.get(entity).equals(map.get(entity)))
                {
                    g2d.setColor(entity.entityType.getColor());
                    g2d.fillOval((int) map.get(entity).position.getX() - radiusKeyPositions,
                                 (int) map.get(entity).position.getY() - radiusKeyPositions,
                                 radiusKeyPositions * 2,
                                 radiusKeyPositions * 2);
                }
            }
        }

    }

    /**
     * Advance the animation by one frame and moves all entities to their new
     * positions
     */
    private void runAnimation()
    {
        if (isRecording &&
            !isFrameByFrame &&
            selectedEntity != null)
        {
            if (currentFrame == frames.size())
            {
                frames.add(new HashMap<>());
            }
            frames.get(currentFrame).put(selectedEntity,
                                         new PositionAndAngle(
                                                 new Point2D.Float(selectedEntity.getPosition().x,
                                                                   selectedEntity.getPosition().y),
                                                 selectedEntity.getAngleInRadians()));

            if (selectedEntity instanceof Player)
            {
                Player selectedPlayer = (Player) selectedEntity;
                for (Entity entity : entities)
                {
                    if (entity instanceof PlayingElement)
                    {
                        PlayingElement pe = (PlayingElement) entity;
                        if (isPlayingElementOwner(pe, selectedPlayer))
                        {
                            frames.get(currentFrame).put(pe,
                                                         new PositionAndAngle(
                                                                 new Point2D.Float(pe.getPosition().x,
                                                                                   pe.getPosition().y),
                                                                 pe.getAngleInRadians()));
                        }
                    }
                }
            }
        }
        updatePositions();
        currentFrame += playSpeedInFrames;

        // TODO(Eric): Add loopback functionality ?
        if (currentFrame > frames.size() ||
            currentFrame < 0 ||
            (!isRecording && currentFrame == frames.size()))
        {
            stop();
        }
    }

    private void updatePositions()
    {
        if (!frames.isEmpty())
        {
            HashMap<Entity, PositionAndAngle> map = frames.get(currentFrame);
            for (Entity entity : map.keySet())
            {
                entity.setPosition(map.get(entity).position);
                entity.setAngleInRadians(map.get(entity).angleInRadians);
            }
        }

        animationPropertiesChanged();
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

    String getAnimationStat()
    {
        if (isFrameByFrame)
        {
            return "Image courante : " + getCurrentFrameNumber() + " de " +
                   getTotalNumberOfFrames();
        }
        else
        {
            int secondsPerMinute = 60;

            int currentFrames = currentFrame;
            int currentMinutes = currentFrames / (framesPerSecond * secondsPerMinute);
            currentFrames -= currentMinutes * (framesPerSecond * secondsPerMinute);
            int currentSeconds = currentFrames / framesPerSecond;
            currentFrames -= currentSeconds * framesPerSecond;

            int maxFrames = frames.size();
            int maxMinutes = maxFrames / (framesPerSecond * secondsPerMinute);
            maxFrames -= maxMinutes * (framesPerSecond * secondsPerMinute);
            int maxSeconds = maxFrames / framesPerSecond;
            maxFrames -= maxSeconds * framesPerSecond;

            String strCurrentMinutes = (currentMinutes < 10) ?
                                       ("0" + currentMinutes) :
                                       ("" + currentMinutes);
            String strCurrentSeconds = (currentSeconds < 10) ?
                                       ("0" + currentSeconds) :
                                       ("" + currentSeconds);
            String strCurrentFrames = (currentFrames < 10) ?
                                      ("0" + currentFrames) :
                                      ("" + currentFrames);

            String strMaxMinutes = (maxMinutes < 10) ?
                                   ("0" + maxMinutes) :
                                   ("" + maxMinutes);
            String strMaxSeconds = (maxSeconds < 10) ?
                                   ("0" + maxSeconds) :
                                   ("" + maxSeconds);
            String strMaxFrames = (maxFrames < 10) ?
                                  ("0" + maxFrames) :
                                  ("" + maxFrames);

            return (strCurrentMinutes + ":" + strCurrentSeconds + "." + strCurrentFrames + " / " +
                    strMaxMinutes + ":" + strMaxSeconds + "." + strMaxFrames);
        }
    }

    private int getCurrentFrameNumber()
    {
        int result = currentFrame;
        if (isFrameByFrame)
        {
            if (previousFrames.isEmpty())
            {
                result = 0;
            }
            else if (previousFrames.size() == 1)
            {
                result = 1;
            }
            else if (currentFrame >= previousFrames.get(previousFrames.size() - 1))
            {
                result = previousFrames.size();
            }
            else
            {
                for (int frameIndex = 0;
                     frameIndex < previousFrames.size() - 1;
                     ++frameIndex)
                {
                    if (currentFrame < previousFrames.get(frameIndex + 1))
                    {
                        result = frameIndex + 1;
                        break;
                    }
                }
            }
        }

        return result;
    }

    int getCurrentFrameIndex()
    {
        int result = currentFrame;
        if (isFrameByFrame)
        {
            if (previousFrames.size() <= 1)
            {
                result = 0;
            }
            else if (currentFrame > previousFrames.get(previousFrames.size() - 1))
            {
                result = previousFrames.size() - 1;
            }
            else
            {
                for (int frameIndex = 0;
                     frameIndex < previousFrames.size() - 1;
                     ++frameIndex)
                {
                    if (currentFrame < previousFrames.get(frameIndex + 1))
                    {
                        result = frameIndex;
                        break;
                    }
                }
            }
        }

        return result;
    }

    private int getTotalNumberOfFrames()
    {
        int result = frames.size();
        if (isFrameByFrame)
        {
            result = previousFrames.size();
        }

        return result;
    }

    int getMaxFrameIndex()
    {
        int result = 0;
        if (!isFrameByFrame &&
            !frames.isEmpty())
        {
            result = frames.size() - 1;
        }
        else if (isFrameByFrame &&
                 !previousFrames.isEmpty())
        {
            result = previousFrames.size() - 1;
        }

        return result;
    }

    boolean isRecording()
    {
        return isRecording;
    }

    boolean isFrameByFrame()
    {
        return isFrameByFrame;
    }

    boolean isRunning()
    {
        return timer.isRunning();
    }

    void setSkipAmountInSeconds(int value)
    {
        skipAmountInSeconds = value;
    }

    void setRecording(boolean value)
    {
        isRecording = value;
    }

    void setFrameByFrame(boolean value)
    {
        isFrameByFrame = value;
    }

    void setSelectedEntity(Entity value)
    {
        selectedEntity = value;

        if (!isFrameByFrame)
        {
            if (selectedEntity == null)
            {
                stop();
            }
            else
            {
                start();
            }
        }
    }

    void setCurrentFrame(int frame)
    {
        if (!isRecording)
        {
            if (isFrameByFrame &&
                !previousFrames.isEmpty())
            {
                currentFrame = previousFrames.get(frame);
            }
            else
            {
                currentFrame = frame;
            }

            updatePositions();
        }
    }

    @Override
    public void addAnimationPropertiesChangedObserver(AnimationPropertiesChangedObserver object)
            throws NullPointerException
    {
        if (object == null)
        {
            throw new NullPointerException();
        }
        if (!animationChangedObservers.contains(object))
        {
            animationChangedObservers.add(object);
        }
    }

    @Override
    public void deleteAnimationPropertiesChangedObserver(AnimationPropertiesChangedObserver object)
    {
        animationChangedObservers.remove(object);
    }

    @Override
    public void deleteAnimationPropertiesChangedObservers()
    {
        animationChangedObservers.clear();
    }

    @Override
    public void animationPropertiesChanged()
    {
        for (AnimationPropertiesChangedObserver observer : animationChangedObservers)
        {
            observer.animationPropertiesChanged();
        }
    }

    private class PositionAndAngle implements Serializable
    {
        private Point2D.Float position;
        private float angleInRadians;

        public PositionAndAngle(Point2D.Float position, float angleInRadians)
        {
            this.position = position;
            this.angleInRadians = angleInRadians;
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
            return (this.position.equals(((PositionAndAngle) obj).position) &&
                    this.angleInRadians == ((PositionAndAngle) obj).angleInRadians);
        }

        @Override
        public int hashCode()
        {
            return ((int) (position.hashCode() * angleInRadians));
        }

    }

}
