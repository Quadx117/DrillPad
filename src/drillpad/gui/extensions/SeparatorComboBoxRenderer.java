package drillpad.gui.extensions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashSet;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * This class renders a {@link JComboBox} with a {@link JSeparator} after every
 * element at the indexes specified at construction time.
 *
 * @author Eric Perron
 */
public class SeparatorComboBoxRenderer extends BasicComboBoxRenderer
{
    private final JPanel separatorPanel = new JPanel(new BorderLayout());
    private final JSeparator separator = new JSeparator();

    private final HashSet<Integer> seperatorIndexes;

    /**
     * Constructs a {@code SeparatorComboBoxRenderer} for an item in a list.
     * <p>
     * This renderer will add a {@link JSeparator} after the item if the item's
     * index is specified in the {@code seperatorIndexes} array.
     *
     * @param seperatorIndexes The list of indexes after which the renderer must
     *                         draw a {@link JSeparator}
     */
    public SeparatorComboBoxRenderer(int... seperatorIndexes)
    {
        this.seperatorIndexes = new HashSet<>();
        for (int i = 0; i < seperatorIndexes.length; ++i)
        {
            this.seperatorIndexes.add(seperatorIndexes[i]);
        }
    }

    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus)
    {
        Component comp = super.getListCellRendererComponent(list, value, index,
                                                            isSelected, cellHasFocus);
        if (index != -1 && seperatorIndexes.contains(index))
        {
            separatorPanel.removeAll();
            separatorPanel.add(comp, BorderLayout.CENTER);
            separatorPanel.add(separator, BorderLayout.SOUTH);
            return separatorPanel;
        }
        else
        {
            return comp;
        }
    }

}
