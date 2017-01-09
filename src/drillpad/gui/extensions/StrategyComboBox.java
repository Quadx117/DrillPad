package drillpad.gui.extensions;

import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import drillpad.domain.SceneController;
import drillpad.domain.event.InstanceChangedObserver;
import drillpad.general.utility.ImageUtilities;

/**
 * Custom comboBox used to display a miniature image and the name of a strategy.
 *
 * @author Eric Perron
 *
 * @see
 * https://docs.oracle.com/javase/tutorial/uiswing/examples/components/CustomComboBoxDemoProject/src/components/CustomComboBoxDemo.java
 *
 */
public final class StrategyComboBox extends JPanel implements Serializable,
                                                              InstanceChangedObserver
{
    private SceneController controller = SceneController.getInstance();

    private final LinkedHashMap<String, ImageIcon> strategiesImages;
    private final JComboBox strategyComboBox;

    private final int imageWidth = 125;
    private final int imageHeight = 70;

    private final String emptyStrategyName = "[Veuillez créer une stratégie]";

    public StrategyComboBox()
    {
        super();

        this.strategiesImages = new LinkedHashMap<>();
        controller.addInstanceChangedObserver(this);

        strategyComboBox = new JComboBox();
        ComboBoxRenderer renderer = new ComboBoxRenderer();
        renderer.setPreferredSize(null);
        strategyComboBox.setPreferredSize(new Dimension(160, 90));
        strategyComboBox.setRenderer(renderer);
        strategyComboBox.setMaximumRowCount(3);
        add(strategyComboBox);
        setVisible(true);
    }

    /**
     * Adds an item to the combo box.
     * The image can be of any size and will be automatically resized to the
     * proper size.
     *
     * @param name  The name of the item to add to the combo box.
     * @param image The {@code ImageIcon} of the item to add to the combo box.
     */
    public void addItem(String name, BufferedImage image)
    {
        strategyComboBox.addItem(name);

        if (image != null)
        {
            strategiesImages.put(name,
                                 ImageUtilities.getScaledImage(image,
                                                               imageWidth,
                                                               imageHeight,
                                                               true));
        }

        if (strategyComboBox.getItemCount() > 1)
        {
            removeItem(emptyStrategyName);
        }
    }

    public void addItems(LinkedHashMap<String, BufferedImage> map)
    {
        for (Map.Entry<String, BufferedImage> strategyPreview : map.entrySet())
        {
            addItem(strategyPreview.getKey(), strategyPreview.getValue());
        }
    }

    public void removeItem(String name)
    {
        if (itemExist(name))
        {
            strategyComboBox.removeItem(name);
            strategiesImages.remove(name);
        }

        if (strategyComboBox.getItemCount() == 0)
        {
            addItem(emptyStrategyName, null);
        }
    }

    public void editItem(String previousName, String newName, BufferedImage image)
    {
        removeItem(previousName);
        addItem(newName, image);
    }

    public void updatePreviewImage(String name, BufferedImage image)
    {
        strategiesImages.put(name,
                             ImageUtilities.getScaledImage(image,
                                                           imageWidth,
                                                           imageHeight,
                                                           true));
        repaint();
    }

    public void removeAllItems()
    {
        strategyComboBox.removeAllItems();
        strategiesImages.clear();
        addItem(emptyStrategyName, null);
    }

    public void setSelectedItem(String name)
    {
        strategyComboBox.setSelectedItem(name);
    }

    public void addItemListener(ItemListener itemListener)
    {
        strategyComboBox.addItemListener(itemListener);
    }

    public String getSelectedStrategyName()
    {
        return strategyComboBox.getSelectedItem().toString();
    }

    @Override
    public void instanceChanged()
    {
        controller = SceneController.getInstance();
    }

    @Override
    public void setEnabled(boolean bln)
    {
        super.setEnabled(bln);
        strategyComboBox.setEnabled(bln);
    }

    public boolean itemExist(String name)
    {
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel) strategyComboBox.getModel();

        return (model.getIndexOf(name) != -1);
    }

    private class ComboBoxRenderer extends BasicComboBoxRenderer
    {
        public ComboBoxRenderer()
        {
            super();
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the imageName and text corresponding
         * to the selected value and returns the label, set up
         * to display the text and imageName.
         */
        @Override
        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)
        {
            super.getListCellRendererComponent(list, value, index,
                                               isSelected, cellHasFocus);

            // NOTE(JFB): Possible to be null when we delete the last strategy                                  
            if (value != null)
            {
                String imageName = value.toString();

                setIcon(strategiesImages.get(imageName));
                setText(imageName);
                setVerticalTextPosition(BOTTOM);
                setHorizontalTextPosition(CENTER);
            }

            return this;
        }

    }

}
