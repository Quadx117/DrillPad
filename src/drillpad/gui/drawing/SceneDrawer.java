package drillpad.gui.drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.Timer;

import drillpad.domain.SceneController;
import drillpad.domain.entity.Entity;
import drillpad.domain.entity.Player;
import drillpad.domain.event.InstanceChangedObserver;

/**
 *
 * @author Eric Perron
 */
public class SceneDrawer implements InstanceChangedObserver
{
    private SceneController controller = SceneController.getInstance();
    private BasicStroke dashedStroke;
    private float dashLength = 5.0f;
    private float dashPhase = 0f;
    private final float dash[] =
    {
        dashLength
    };
    private boolean isLinkLineEnabled = false;
    private boolean isArrowEnabled = false;
    private Point2D.Float currentMousePosition = new Point2D.Float();

    public SceneDrawer()
    {
        controller.addInstanceChangedObserver(this);
        dashedStroke = new BasicStroke(1.0f,
                                       BasicStroke.CAP_BUTT,
                                       BasicStroke.JOIN_MITER,
                                       1.0f, dash, dashPhase);

        // Animates the dashed selection (marching ants selection effect)
        new Timer(50, new ActionListener()
          {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  dashPhase += (2 * dashLength) - 1;
              }
          }).start();
    }

    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        AffineTransform previousTransform = g2d.getTransform();
        g2d.scale(controller.getZoomFactor(), controller.getZoomFactor());

        if (controller.getSportsCount() > 0)
        {
            drawBackground(g2d);

            if (isArrowEnabled)
            {
                controller.drawPositionsAndArrows(g2d);
            }
            else
            {
                controller.drawPreviousAnimationImages(g2d);
            }

            drawEntities(g2d);

            // Used to draw a line between Playing Element and Mouse Position
            if (isLinkLineEnabled)
            {
                drawLinkLine(g2d);
            }
        }
        else
        {
            g2d.setBackground(Color.GRAY);
        }

        g2d.setTransform(previousTransform);
    }

    public void showPositionsAndArrows(boolean value)
    {
        isArrowEnabled = value;
    }

    public void showLinkLine(boolean value)
    {
        isLinkLineEnabled = value;
    }

    private void drawLinkLine(Graphics2D g2d)
    {
        g2d.setStroke(dashedStroke);
        g2d.setColor(Color.BLACK);
        g2d.drawLine((int) controller.getSelectedPlayingElement().getPosition().getX(),
                     (int) controller.getSelectedPlayingElement().getPosition().getY(),
                     (int) currentMousePosition.getX(),
                     (int) currentMousePosition.getY());
    }

    private void drawBackground(Graphics2D g2d)
    {
        g2d.drawImage(controller.getCurrentStrategyPlayingFieldImage(), 0, 0, null);
    }

    private void drawEntities(Graphics2D g2d)
    {
        List<Entity> entities = controller.getEntities();
        for (Entity entity : entities)
        {
            // Highlight the entity if it is selected
            if (entity.isSelected())
            {
                Point2D.Float entityPosition = entity.getPosition();
                int xOffset = (entity.getWidth() / 2) + 1;
                int yOffset = (entity.getHeight() / 2) + 1;

                Shape shape;
                if (entity.hasImage())
                {
                    shape = new Rectangle((int) entityPosition.getX() - xOffset,
                                          (int) entityPosition.getY() - yOffset,
                                          xOffset * 2, yOffset * 2);
                }
                else
                {
                    shape = new Ellipse2D.Float((int) entityPosition.x - xOffset,
                                                (int) entityPosition.y - yOffset,
                                                xOffset * 2, yOffset * 2);
                }

                Stroke previousStroke = g2d.getStroke();
                dashedStroke = new BasicStroke(1.0f,
                                               BasicStroke.CAP_BUTT,
                                               BasicStroke.JOIN_MITER,
                                               1.0f, dash, dashPhase);

                AffineTransform previousTransform = g2d.getTransform();
                // TODO(Eric): Handle this differently ?
                g2d.rotate(entity.getAngleInRadians(),
                           entityPosition.x,
                           entityPosition.y);
                g2d.setColor(Color.WHITE);
                g2d.draw(shape);
                g2d.setStroke(dashedStroke);
                g2d.setColor(Color.BLACK);
                g2d.draw(shape);
                g2d.setStroke(previousStroke);
                g2d.setTransform(previousTransform);
            }

            // Draw the entity
            // TODO(Eric): Handle this in a better way
            if (entity instanceof Player && !isArrowEnabled)
            {
                ((Player) entity).draw(g2d);
            }
            else if (!(entity instanceof Player))
            {
                entity.draw(g2d);
            }
        }
    }

    public void updateMousePosition(Point2D.Float mousePosition)
    {
        currentMousePosition = mousePosition;
    }

    @Override
    public void instanceChanged()
    {
        controller = SceneController.getInstance();
    }

}
