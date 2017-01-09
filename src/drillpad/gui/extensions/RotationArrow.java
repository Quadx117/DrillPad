package drillpad.gui.extensions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

import javax.swing.JComponent;

/**
 *
 * @author Eric Perron
 */
public class RotationArrow extends JComponent
{
    private final TriangleShape triangle1;
    private final TriangleShape triangle2;
    private final QuadCurve2D arc;
    private final Color color = Color.BLACK;
    private float angleInRadians = 0f;

    private final float triangleHalfWidth = 3f;
    private final float triangleHeight = 6f;

    public RotationArrow()
    {
        this.triangle1 = new TriangleShape(new Point2D.Float(20f, 5f),
                                           new Point2D.Float(20f + triangleHalfWidth, 5f + triangleHeight),
                                           new Point2D.Float(20f - triangleHalfWidth, 5f + triangleHeight));

        this.triangle2 = new TriangleShape(new Point2D.Float(20f, 40f),
                                           new Point2D.Float(20f + triangleHalfWidth, 40f - triangleHeight),
                                           new Point2D.Float(20f - triangleHalfWidth, 40f - triangleHeight));

        double x1 = triangle1.getBounds().getCenterX();
        double y1 = triangle1.getBounds().getCenterY();
        double x2 = triangle2.getBounds().getCenterX();
        double y2 = triangle2.getBounds().getCenterY();
        double ctrlx = x1 + 5;
        double ctrly = y1 + ((y2 - y1) / 2f);
        this.arc = new QuadCurve2D.Double(x1, y1, ctrlx, ctrly, x2, y2);

        AffineTransform transform = new AffineTransform();
        transform.rotate(-0.3f,
                         triangle1.getBounds2D().getCenterX(),
                         triangle1.getBounds2D().getCenterY());
        triangle1.transform(transform);

        transform = new AffineTransform();
        transform.rotate(0.3f,
                         triangle2.getBounds2D().getCenterX(),
                         triangle2.getBounds2D().getCenterY());
        triangle2.transform(transform);

        setBounds(10, 10, 50, 50);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g2d.rotate(angleInRadians,
                   arc.getCtrlX(),
                   arc.getCtrlY());
        g2d.fill(triangle1);
        g2d.fill(triangle2);
        g2d.draw(arc);
    }

    public void setAngleInRadians(float value)
    {
        angleInRadians = value;
    }

    @SuppressWarnings("serial")
    private class TriangleShape extends Path2D.Double
    {
        public TriangleShape(Point2D point1, Point2D point2, Point2D point3)
        {
            moveTo(point1.getX(), point1.getY());
            lineTo(point2.getX(), point2.getY());
            lineTo(point3.getX(), point3.getY());
            closePath();
        }

    }

}
