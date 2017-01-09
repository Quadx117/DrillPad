package drillpad.gui.extensions;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.io.Serializable;

import javax.swing.JPanel;

import drillpad.domain.SceneController;
import drillpad.domain.event.InstanceChangedObserver;
import drillpad.gui.drawing.SceneDrawer;

/**
 *
 * @author Eric Perron
 */
public class DrawingPanel extends JPanel implements Serializable,
                                                    InstanceChangedObserver
{
    private SceneController controller = SceneController.getInstance();
    private SceneDrawer mainDrawer;

    // This constructor is only used by the NetNeans GUI builder
    public DrawingPanel()
    {
        mainDrawer = new SceneDrawer();
        controller.addInstanceChangedObserver(this);
        setVisible(true);
    }

    public void updateMousePosition(Point2D.Float mousePosition)
    {
        mainDrawer.updateMousePosition(mousePosition);
    }

    public void showPositionsAndArrows(boolean value)
    {
        mainDrawer.showPositionsAndArrows(value);
    }

    public void showLinkLine(boolean value)
    {
        mainDrawer.showLinkLine(value);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (mainDrawer != null)
        {
            super.paintComponent(g);
            mainDrawer.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        int width = super.getPreferredSize().width;
        int height = super.getPreferredSize().height;

        if (controller != null)
        {
            width = (int) (controller.getZoomFactor() * width);
            height = (int) (controller.getZoomFactor() * height);
        }
        return new Dimension(width, height);
    }

    @Override
    public void instanceChanged()
    {
        controller = SceneController.getInstance();
    }

}
