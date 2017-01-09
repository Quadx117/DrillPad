package drillpad.domain.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import drillpad.domain.SceneController;

/**
 *
 * @author Eric Perron
 */
public class Player extends Entity implements Serializable
{
    private Role role;
    private String name;
    private boolean isRoleVisible;
    private boolean isNameVisible;

    Player(EntityType entityType, Role role, Point2D.Float position)
    {
        super(entityType, position);
        this.role = role;
        this.isRoleVisible = true;
    }

    // TODO(Eric): Je ne suis plus certain que c'est OK de cette façon pour le
    // principe de la séparation du UI et du domain, mais bon on a pas eu de
    // commentaires des correcteurs à ce sujet.
    @Override
    public void draw(Graphics2D g2d)
    {
        // Draw the entity
        super.draw(g2d);

        // Draw the player's role
        if (SceneController.getInstance().isPlayerRolesVisible() &&
            isRoleVisible)
        {
            // TODO(Eric): global font ? font size and style determined by the user ?
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 24f));
            // TODO(Eric): FontUtils to center the text ?
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int halfStringWidth = fontMetrics.stringWidth(role.getAbbreviation()) / 2;
            int halfStringHeight = (fontMetrics.getHeight() / 2) - fontMetrics.getDescent();
            int x = (int) getPosition().x - halfStringWidth;
            int y = (int) getPosition().y + halfStringHeight;

            // TODO(Eric): Create global text color modifiable by user in Edit menu (like backgroundColor)
            g2d.setColor(Color.BLACK);
            g2d.drawString(role.getAbbreviation(), x + 1, y + 1);

            g2d.setColor(Color.WHITE);
            g2d.drawString(role.getAbbreviation(), x, y);

        }

        // Draw the player's name
        if (SceneController.getInstance().isPlayerNamesVisible() &&
            isNameVisible)
        {
            // TODO(Eric): global font ? font size and style determined by the user ?
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 24f));
            // TODO(Eric): FontUtils to center the text ?
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int halfStringWidth = fontMetrics.stringWidth(name) / 2;
            int stringHeight = fontMetrics.getHeight() - fontMetrics.getDescent();
            int x = (int) getPosition().x - halfStringWidth;
            int y = (int) getPosition().y + (getHeight() / 2) + stringHeight;

            // TODO(Eric): Create global text color modifiable by user in Edit menu (like backgroundColor)
            g2d.setColor(Color.BLACK);
            g2d.drawString(name, x + 1, y + 1);

            g2d.setColor(Color.WHITE);
            g2d.drawString(name, x, y);
        }
    }

    public boolean showRotationImage(Point2D.Float mousePosition)
    {
        float innerRectPadding = 6;
        float x = getPosition().x - ((getWidth() + innerRectPadding) / 2);
        float y = getPosition().y - ((getHeight() + innerRectPadding) / 2);
        Rectangle2D.Float innerRect = new Rectangle2D.Float(x,
                                                            y,
                                                            getWidth() + innerRectPadding,
                                                            getHeight() + innerRectPadding);
        float outerPadding = 60;
        x = innerRect.x - (outerPadding / 2);
        y = innerRect.y - (outerPadding / 2);
        Rectangle2D.Float outerRect = new Rectangle2D.Float(x,
                                                            y,
                                                            innerRect.width + outerPadding,
                                                            innerRect.height + outerPadding);

        return (outerRect.contains(mousePosition) &&
                !innerRect.contains(mousePosition));
    }

    // TODO(Eric):  Change visibility of these methods if we change how we deal with this
    public boolean isRoleVisible()
    {
        return isRoleVisible;
    }

    public boolean isNameVisible()
    {
        return isNameVisible;
    }

    public String getRoleName()
    {
        return role.getName();
    }

    public String getTeamName()
    {
        return entityType.getName();
    }

    public String getName()
    {
        return name;
    }

    void edit(EntityType newTeam, Role newRole, String newPlayerName,
              boolean newIsRoleVisible, boolean newIsNameVisible)
    {
        entityType = newTeam;
        role = newRole;
        name = newPlayerName;
        isRoleVisible = newIsRoleVisible;
        isNameVisible = newIsNameVisible;
    }

}
