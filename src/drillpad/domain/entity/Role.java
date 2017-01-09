package drillpad.domain.entity;

import java.io.Serializable;

/**
 *
 * @author Eric Perron
 */
public final class Role implements Serializable
{
    // TODO(Eric): Enum for text alignment ?
    private String name;
    private String abbreviation;

    public Role(String name, String abbreviation)
    {
        this.name = name;
        this.abbreviation = abbreviation;
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
        return (this.name.equals(((Role) obj).name));
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    String getName()
    {
        return name;
    }

    String getAbbreviation()
    {
        return abbreviation;
    }

    void edit(String newName, String newAbbreviation)
    {
        name = newName;
        abbreviation = newAbbreviation;
    }

}
