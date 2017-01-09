package drillpad.gui;

import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import drillpad.domain.SceneController;
import drillpad.gui.extensions.SortedComboBox;

/**
 *
 * @author Eric Perron
 */
public class RolePropertyWindow extends javax.swing.JDialog
{
    private final SceneController controller = SceneController.getInstance();
    private final SortedComboBox mainWindowRoleComboBox;
    private final int maxNumberOfAbbreviationChar = 3;
    private final boolean isNewRole;

    /**
     * Creates a new {@link JDialog} used to add or edit a {@link Role}.
     *
     * @param isNewRole              Whether or not this {@code JDialog} is used
     *                               to add a new {@code Role}.
     * @param mainWindowRoleComboBox The {@link SortedComboBox} used to display
     *                               the available {@code Role}'s in the
     *                               {@link MainWindow}.
     */
    RolePropertyWindow(boolean isNewRole, SortedComboBox mainWindowRoleComboBox)
    {
        this.isNewRole = isNewRole;
        this.mainWindowRoleComboBox = mainWindowRoleComboBox;

        initComponents();
        init();
    }

    private void init()
    {
        roleComboBox.setVisible(!isNewRole);
        deleteButton.setVisible(!isNewRole);

        if (!isNewRole)
        {
            for (String role : controller.getRoleNames())
            {
                roleComboBox.addItem(role);
            }

            roleComboBox.setSelectedIndex(0);
            loadRole(roleComboBox.getSelectedItem().toString());
        }

        setPreferredSize(null);
        setSize(getPreferredSize());
    }

    private void loadRole(String roleName)
    {
        roleNameTextField.setText(roleName);
        roleAbbreviationTextField.setText(controller.getRoleAbbreviation(roleName));
    }

    // TODO(Eric): This is used in multiple classes, create utility method.
    private boolean roleExist(JComboBox comboBox, String roleName)
    {
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel) comboBox.getModel();

        return (model.getIndexOf(roleName) != -1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        roleComboBox = new javax.swing.JComboBox<>();
        roleNameLabel = new javax.swing.JLabel();
        roleNameTextField = new javax.swing.JTextField();
        roleAbbreviationLabel = new javax.swing.JLabel();
        roleAbbreviationTextField = new javax.swing.JTextField();
        deleteButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(isNewRole ? "Ajout d'un rôle" : "Édition des rôles");
        setAlwaysOnTop(true);
        setLocation(new java.awt.Point(0, 0));
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        roleComboBox.setMaximumSize(new java.awt.Dimension(140, 24));
        roleComboBox.setMinimumSize(new java.awt.Dimension(140, 24));
        roleComboBox.setPreferredSize(new java.awt.Dimension(140, 24));
        roleComboBox.setRenderer(new BasicComboBoxRenderer());
        roleComboBox.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                roleComboBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 5);
        getContentPane().add(roleComboBox, gridBagConstraints);

        roleNameLabel.setText("Rôle du joueur");
        roleNameLabel.setMaximumSize(new java.awt.Dimension(70, 20));
        roleNameLabel.setMinimumSize(new java.awt.Dimension(70, 20));
        roleNameLabel.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        getContentPane().add(roleNameLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 10);
        getContentPane().add(roleNameTextField, gridBagConstraints);

        roleAbbreviationLabel.setText("Abréviation du rôle");
        roleAbbreviationLabel.setMaximumSize(new java.awt.Dimension(91, 20));
        roleAbbreviationLabel.setMinimumSize(new java.awt.Dimension(91, 20));
        roleAbbreviationLabel.setPreferredSize(new java.awt.Dimension(91, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        getContentPane().add(roleAbbreviationLabel, gridBagConstraints);

        roleAbbreviationTextField.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                roleAbbreviationTextFieldKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 10);
        getContentPane().add(roleAbbreviationTextField, gridBagConstraints);

        deleteButton.setText("Supprimer");
        deleteButton.setMaximumSize(new java.awt.Dimension(90, 24));
        deleteButton.setMinimumSize(new java.awt.Dimension(90, 24));
        deleteButton.setOpaque(false);
        deleteButton.setPreferredSize(new java.awt.Dimension(90, 24));
        deleteButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                deleteButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 10);
        getContentPane().add(deleteButton, gridBagConstraints);

        okButton.setText("OK");
        okButton.setMaximumSize(new java.awt.Dimension(90, 24));
        okButton.setMinimumSize(new java.awt.Dimension(90, 24));
        okButton.setPreferredSize(new java.awt.Dimension(90, 24));
        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 5);
        getContentPane().add(okButton, gridBagConstraints);

        cancelButton.setText("Annuler");
        cancelButton.setMaximumSize(new java.awt.Dimension(90, 24));
        cancelButton.setMinimumSize(new java.awt.Dimension(90, 24));
        cancelButton.setPreferredSize(new java.awt.Dimension(90, 24));
        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 10);
        getContentPane().add(cancelButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okButtonActionPerformed
    {//GEN-HEADEREND:event_okButtonActionPerformed
        String newName = roleNameTextField.getText();
        String newAbbreviation = roleAbbreviationTextField.getText();
        if (newName == null || newName.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                                          "Le champs \"Rôle du joueur\" est obligatoire.",
                                          getTitle(),
                                          JOptionPane.ERROR_MESSAGE);
        }
        else if (newAbbreviation == null || newAbbreviation.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                                          "Le champs \"Abréviation du rôle\" est obligatoire.",
                                          getTitle(),
                                          JOptionPane.ERROR_MESSAGE);
        }
        else if (isNewRole)
        {
            if (roleExist(mainWindowRoleComboBox, newName))
            {
                JOptionPane.showMessageDialog(this,
                                              "Ce rôle existe déjà." +
                                              "Veuillez choisir un nom différent",
                                              getTitle(),
                                              JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                controller.addRole(newName, newAbbreviation);

                mainWindowRoleComboBox.addItem(newName);
                mainWindowRoleComboBox.setSelectedItem(newName);
                dispose();
            }
        }
        else
        {
            String currentName = roleComboBox.getSelectedItem().toString();
            String currentAbbreviation = controller.getRoleAbbreviation(currentName);
            if (currentName.equals(newName) &&
                currentAbbreviation.equals(newAbbreviation))
            {
                dispose();
            }
            else
            {
                int option = JOptionPane.showConfirmDialog(this,
                                                           "Voullez-vous vraiment modifier les informations ?",
                                                           "Modification du rôle",
                                                           JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.OK_OPTION)
                {
                    controller.editRole(currentName, newName, newAbbreviation);

                    mainWindowRoleComboBox.removeItem(currentName);
                    mainWindowRoleComboBox.addItem(newName);
                    dispose();
                }
            }
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void roleAbbreviationTextFieldKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_roleAbbreviationTextFieldKeyTyped
    {//GEN-HEADEREND:event_roleAbbreviationTextFieldKeyTyped
        char c = evt.getKeyChar();
        if ((c != KeyEvent.VK_BACK_SPACE || c != KeyEvent.VK_DELETE) &&
            roleAbbreviationTextField.getText().length() == maxNumberOfAbbreviationChar)
        {
            evt.consume();
        }
    }//GEN-LAST:event_roleAbbreviationTextFieldKeyTyped

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteButtonActionPerformed
    {//GEN-HEADEREND:event_deleteButtonActionPerformed
        int option = JOptionPane.showConfirmDialog(this,
                                                   "Voullez-vous vraiment supprimer ce rôle ?",
                                                   "Supression du rôle",
                                                   JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            Object itemToRemove = roleComboBox.getSelectedItem();
            controller.removeRole(itemToRemove.toString());
            mainWindowRoleComboBox.removeItem(itemToRemove.toString());
            roleComboBox.removeItem(itemToRemove);

            if (controller.getRoleNames().isEmpty())
            {
                dispose();
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void roleComboBoxItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_roleComboBoxItemStateChanged
    {//GEN-HEADEREND:event_roleComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {
            loadRole(roleComboBox.getSelectedItem().toString());
        }
    }//GEN-LAST:event_roleComboBoxItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel roleAbbreviationLabel;
    private javax.swing.JTextField roleAbbreviationTextField;
    private javax.swing.JComboBox<String> roleComboBox;
    private javax.swing.JLabel roleNameLabel;
    private javax.swing.JTextField roleNameTextField;
    // End of variables declaration//GEN-END:variables
}