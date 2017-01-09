package drillpad.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import drillpad.domain.SceneController;
import drillpad.general.utility.ImageUtilities;

/**
 *
 * @author Eric Perron
 */
public class PlayingElementWindow extends javax.swing.JDialog
{
    private final SceneController controller = SceneController.getInstance();
    private BufferedImage newImage;
    private final NumberFormat percentFormat;
    private final NumberFormat integerFormat;
    private final String nullImageText = "<html>\n    [Pas d'image<br>sélectionnée]\n</html>";

    /**
     * Creates a new {@link JDialog} used to add or edit a
     * {@link PlayingElement}.
     */
    PlayingElementWindow()
    {
        percentFormat = NumberFormat.getNumberInstance();
        percentFormat.setMaximumIntegerDigits(4);
        percentFormat.setMaximumFractionDigits(1);

        integerFormat = NumberFormat.getIntegerInstance();
        integerFormat.setMaximumIntegerDigits(4);

        initComponents();
        init();
    }

    private void init()
    {
        nameTextField.setText(controller.getPlayingElementName());
        colorPreviewLabel.setBackground(controller.getPlayingElementColor());
        diameterFormattedTextField.setValue(controller.getPlayingElementRadius() * 2);
        newImage = controller.getPlayingElementImage();
        imageScaleFormattedTextField.setValue(controller.getPlayingElementImageScale() * 100);

        if (newImage != null)
        {
            imagePreviewLabel.setIcon(
                    ImageUtilities.getScaledImage(newImage,
                                                  imagePreviewLabel.getWidth(),
                                                  imagePreviewLabel.getHeight(),
                                                  true));
        }

        setPreferredSize(null);
        setSize(getPreferredSize());
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        colorLabel = new javax.swing.JLabel();
        diameterLabel = new javax.swing.JLabel();
        colorPreviewLabel = new javax.swing.JLabel();
        imageLabel = new javax.swing.JLabel();
        imagePreviewLabel = new javax.swing.JLabel();
        browseImageButton = new javax.swing.JButton();
        imageScaleLabel = new javax.swing.JLabel();
        imageScaleFormattedTextField = new javax.swing.JFormattedTextField(percentFormat);
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        diameterFormattedTextField = new javax.swing.JFormattedTextField(integerFormat);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Édition de l'élément de jeu");
        setMinimumSize(new java.awt.Dimension(307, 273));
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        nameLabel.setText("Nom");
        nameLabel.setMaximumSize(new java.awt.Dimension(21, 20));
        nameLabel.setMinimumSize(new java.awt.Dimension(21, 20));
        nameLabel.setPreferredSize(new java.awt.Dimension(21, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        getContentPane().add(nameLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(nameTextField, gridBagConstraints);

        colorLabel.setText("Couleur");
        colorLabel.setMaximumSize(new java.awt.Dimension(37, 20));
        colorLabel.setMinimumSize(new java.awt.Dimension(37, 20));
        colorLabel.setPreferredSize(new java.awt.Dimension(37, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        getContentPane().add(colorLabel, gridBagConstraints);

        diameterLabel.setText("Diamètre du cercle (pixels)");
        diameterLabel.setMaximumSize(new java.awt.Dimension(130, 20));
        diameterLabel.setMinimumSize(new java.awt.Dimension(130, 20));
        diameterLabel.setPreferredSize(new java.awt.Dimension(130, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 5);
        getContentPane().add(diameterLabel, gridBagConstraints);

        colorPreviewLabel.setBackground(new java.awt.Color(0, 0, 0));
        colorPreviewLabel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255))));
        colorPreviewLabel.setMaximumSize(new java.awt.Dimension(20, 20));
        colorPreviewLabel.setMinimumSize(new java.awt.Dimension(20, 20));
        colorPreviewLabel.setOpaque(true);
        colorPreviewLabel.setPreferredSize(new java.awt.Dimension(20, 20));
        colorPreviewLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                colorPreviewLabelMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(colorPreviewLabel, gridBagConstraints);

        imageLabel.setText("Image");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        getContentPane().add(imageLabel, gridBagConstraints);

        imagePreviewLabel.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        imagePreviewLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagePreviewLabel.setText(nullImageText);
        imagePreviewLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        imagePreviewLabel.setMaximumSize(new java.awt.Dimension(100, 100));
        imagePreviewLabel.setMinimumSize(new java.awt.Dimension(100, 100));
        imagePreviewLabel.setPreferredSize(new java.awt.Dimension(100, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(imagePreviewLabel, gridBagConstraints);

        browseImageButton.setText("Parcourir...");
        browseImageButton.setMaximumSize(new java.awt.Dimension(90, 23));
        browseImageButton.setMinimumSize(new java.awt.Dimension(90, 23));
        browseImageButton.setPreferredSize(new java.awt.Dimension(90, 23));
        browseImageButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                browseImageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 10);
        getContentPane().add(browseImageButton, gridBagConstraints);

        imageScaleLabel.setText("Échelle (%)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        getContentPane().add(imageScaleLabel, gridBagConstraints);

        imageScaleFormattedTextField.setColumns(5);
        imageScaleFormattedTextField.setValue(new Float(100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(imageScaleFormattedTextField, gridBagConstraints);

        okButton.setText("OK");
        okButton.setMaximumSize(new java.awt.Dimension(90, 23));
        okButton.setMinimumSize(new java.awt.Dimension(90, 23));
        okButton.setPreferredSize(new java.awt.Dimension(90, 23));
        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 5);
        getContentPane().add(okButton, gridBagConstraints);

        cancelButton.setText("Annuler");
        cancelButton.setMaximumSize(new java.awt.Dimension(90, 23));
        cancelButton.setMinimumSize(new java.awt.Dimension(90, 23));
        cancelButton.setPreferredSize(new java.awt.Dimension(90, 23));
        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 10);
        getContentPane().add(cancelButton, gridBagConstraints);

        diameterFormattedTextField.setColumns(5);
        diameterFormattedTextField.setMinimumSize(new java.awt.Dimension(40, 20));
        diameterFormattedTextField.setPreferredSize(new java.awt.Dimension(40, 20));
        diameterFormattedTextField.setValue(new Integer(40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(diameterFormattedTextField, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okButtonActionPerformed
    {//GEN-HEADEREND:event_okButtonActionPerformed
        String newName = nameTextField.getText();
        Color newColor = colorPreviewLabel.getBackground();
        int newDiameter = ((Number) diameterFormattedTextField.getValue()).intValue();
        float newImageScale = ((Number) imageScaleFormattedTextField.getValue()).floatValue() / 100f;
        if (newName == null || newName.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                                          "Le champs \"" + nameLabel.getText() + "\" est obligatoire.\n" +
                                          "Veuillez saisir un nom avant de continuer.",
                                          getTitle(),
                                          JOptionPane.ERROR_MESSAGE);
        }
        else if (newDiameter <= 0)
        {
            JOptionPane.showMessageDialog(this,
                                          "Le diamètre doit être supérieur à 0",
                                          getTitle(),
                                          JOptionPane.ERROR_MESSAGE);
        }
        else if (newImageScale <= 0)
        {
            JOptionPane.showMessageDialog(this,
                                          "L'échelle (%) de l'image doit être supérieur à 0",
                                          getTitle(),
                                          JOptionPane.ERROR_MESSAGE);
        }
        else if (propertiesChanged())
        {
            int option = JOptionPane.showConfirmDialog(this,
                                                       "Voullez-vous vraiment modifier les informations ?",
                                                       getTitle(),
                                                       JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.OK_OPTION)
            {
                controller.editPlayingElement(newName, newColor, newDiameter / 2,
                                              newImage, newImageScale);
                dispose();
            }
        }
        else
        {
            dispose();
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void browseImageButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_browseImageButtonActionPerformed
    {//GEN-HEADEREND:event_browseImageButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("Images (*.jpg, *.png)", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);

        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            String path = fileChooser.getSelectedFile().toString();
            newImage = controller.loadImageFromDisk(path);

            ImageIcon previewIcon;
            // NOTE: Only resize the image if one of the image dimension (width
            // or height) is greater than the imagePreviewLabel dimension.
            if (newImage.getWidth() > imagePreviewLabel.getWidth() ||
                newImage.getHeight() > imagePreviewLabel.getHeight())
            {
                previewIcon = ImageUtilities.getScaledImage(newImage,
                                                            imagePreviewLabel.getWidth(),
                                                            imagePreviewLabel.getHeight(),
                                                            true);
            }
            else
            {
                previewIcon = new ImageIcon(newImage);
            }

            imagePreviewLabel.setIcon(previewIcon);
            imagePreviewLabel.setText("");
            imagePreviewLabel.repaint();
        }
    }//GEN-LAST:event_browseImageButtonActionPerformed

    private void colorPreviewLabelMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_colorPreviewLabelMousePressed
    {//GEN-HEADEREND:event_colorPreviewLabelMousePressed
        Color color = JColorChooser.showDialog(null,
                                               "Veuillez choisir la couleur de l'entité",
                                               colorPreviewLabel.getBackground());
        if (color != null)
        {
            colorPreviewLabel.setBackground(color);
        }
    }//GEN-LAST:event_colorPreviewLabelMousePressed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        close();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        close();
    }//GEN-LAST:event_formWindowClosing

    private boolean propertiesChanged()
    {
        String currentName = controller.getPlayingElementName();
        Color currentColor = controller.getPlayingElementColor();
        int currentDiameter = controller.getPlayingElementRadius() * 2;
        BufferedImage currentImage = controller.getPlayingElementImage();
        float currentImageScale = controller.getPlayingElementImageScale();

        String newName = nameTextField.getText();
        Color newColor = colorPreviewLabel.getBackground();
        int newDiameter = ((Number) diameterFormattedTextField.getValue()).intValue();
        float newImageScale = ((Number) imageScaleFormattedTextField.getValue()).floatValue() / 100f;

        boolean nameChanged = !currentName.equals(newName);
        boolean colorChanged = !currentColor.equals(newColor);
        boolean diameterChanged = currentDiameter != newDiameter;
        boolean imageChanged = !ImageUtilities.imagesAreEqual(currentImage,
                                                              newImage);
        boolean imageScaleChanged = currentImageScale != newImageScale;

        return (nameChanged || colorChanged || diameterChanged ||
                imageChanged || imageScaleChanged);
    }

    private void close()
    {
        if (propertiesChanged())
        {
            int option = JOptionPane.showConfirmDialog(this,
                                                       "Il y a des modifications non enregistrées\n" +
                                                       "Voullez-vous vraiment annuler ses modifications ?",
                                                       getTitle(),
                                                       JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.OK_OPTION)
            {
                dispose();
            }
        }
        else
        {
            dispose();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseImageButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel colorLabel;
    private javax.swing.JLabel colorPreviewLabel;
    private javax.swing.JFormattedTextField diameterFormattedTextField;
    private javax.swing.JLabel diameterLabel;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel imagePreviewLabel;
    private javax.swing.JFormattedTextField imageScaleFormattedTextField;
    private javax.swing.JLabel imageScaleLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
}