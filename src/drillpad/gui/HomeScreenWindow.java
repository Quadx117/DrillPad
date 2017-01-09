package drillpad.gui;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import drillpad.domain.SceneController;
import drillpad.domain.entity.Sport;
import drillpad.domain.entity.Strategy;
import drillpad.general.utility.ImageUtilities;

/**
 *
 * @author Eric Perron
 */
public class HomeScreenWindow extends javax.swing.JDialog
{
    private SceneController controller = SceneController.getInstance();
    private final DefaultListModel sportsListModel = new DefaultListModel();
    private final DefaultListModel strategiesListModel = new DefaultListModel();
    private final DefaultListModel projectsListModel = new DefaultListModel();
    private final HashMap<String, String> projectLocations = new HashMap<>();
    private final HashMap<String, BufferedImage> strategyImages = new HashMap<>();
    private final int imageWidth = 120;
    private final int imageHeight = 75;
    private String selectedStrategy;

    /**
     * Creates new form HomeScreenWindow
     */
    public HomeScreenWindow()
    {
        initComponents();

        controller.readProjectLocations();

        initProjectsList();
        initSportsList();
        initStrategiesList();
    }

    private void initProjectsList()
    {
        projectsList.setModel(projectsListModel);

        List<String> pathsToRemove = new ArrayList<>();
        int currentProjectIndex = 0;

        for (String path : controller.getProjectsPath())
        {
            if (new File(path).exists())
            {
                String[] splittedPath = path.split("(/)|(\\\\)");
                projectLocations.put(splittedPath[splittedPath.length - 1], path);
                projectsListModel.addElement(splittedPath[splittedPath.length - 1]);

                if (path.equals(controller.getCurrentProjectPath()))
                {
                    currentProjectIndex = projectsListModel.indexOf(splittedPath[splittedPath.length - 1]);
                }
            }
            else
            {
                pathsToRemove.add(path);
            }
        }

        // NOTE(JFB) : Needed for conccurent modification while iterating through controller.getProjectsPath()
        for (String pathToRemove : pathsToRemove)
        {
            controller.removeProject(pathToRemove);
        }

        projectsList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent lse)
            {
                if(projectsList.getSelectedIndex() >= 0)
                {
                    setTitle(projectLocations.get(projectsList.getSelectedValue()));
                    controller.open(projectLocations.get(projectsList.getSelectedValue()));
                    controller = SceneController.getInstance();
                    showSportsOfSelectedProject();
                    sportsList.setSelectedIndex(
                            sportsListModel.indexOf(
                                    controller.getCurrentSportName()));
                }

                newSportButton.setEnabled(projectsList.getSelectedIndex() >= 0);
                loadSportButton.setEnabled(projectsList.getSelectedIndex() >= 0 &&
                                           sportsList.getSelectedIndex() >= 0);
                
            }
        });

        projectsList.setSelectedIndex(currentProjectIndex);
    }

    private void initSportsList()
    {
        int currentSportIndex = 0;

        sportsList.setModel(sportsListModel);
        sportsList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent lse)
            {
                if(sportsList.getSelectedIndex() >= 0)
                {
                    showParametersOfSelectedSport();
                    showStrategiesOfSelectedSports();
                    strategiesList.setSelectedIndex(
                            strategiesListModel.indexOf(
                                    controller.getCurrentStrategyName()));

                }

                newSportButton.setEnabled(projectsList.getSelectedIndex() >= 0);
                loadSportButton.setEnabled(projectsList.getSelectedIndex() >= 0 &&
                                           sportsList.getSelectedIndex() >= 0);
            }
        });

        if (controller.getSportsCount() > 0)
        {
            currentSportIndex = sportsListModel.indexOf(controller.getCurrentSportName());
        }

        sportsList.setSelectedIndex(currentSportIndex);
    }

    private void initStrategiesList()
    {
        int currentStrategyIndex = 0;

        strategiesList.setModel(strategiesListModel);
        strategiesList.setVisibleRowCount(-1);
        strategiesList.setCellRenderer(new StrategyListRenderer());

        strategiesList.setSelectedIndex(0);

        if (controller.getCurrentSportStrategyCount() > 0)
        {
            currentStrategyIndex = strategiesListModel.indexOf(controller.getCurrentStrategyName());
        }
        
        strategiesList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent lse)
            {
                if(strategiesList.getSelectedIndex() >= 0)
                {
                    previewImageLabel.setIcon(ImageUtilities.getScaledImage(strategyImages.get(strategiesList.getSelectedValue()),
                                                  previewImageLabel.getWidth(),
                                                  previewImageLabel.getHeight(),
                                                  true));
                }
            }
        });

        strategiesList.setSelectedIndex(currentStrategyIndex);
    }

    private void showSportsOfSelectedProject()
    {
        sportsListModel.removeAllElements();

        for (Sport sport : controller.getSports())
        {
            sportsListModel.addElement(sport.getName());
        }
    }

    private void showParametersOfSelectedSport()
    {
        String sportName = sportsList.getSelectedValue();
        BufferedImage image = controller.getSportPlayingFieldImage(sportName);

        maxTeamDataLabel.setText(String.valueOf(controller.getMaxNumberOfTeams(sportName)));
        maxPlayerDataLabel.setText(String.valueOf(controller.getMaxNumberOfPlayers(sportName)));
        strategyNumberDataLabel.setText(String.valueOf(controller.getSportStrategyCount(sportName)));

        if (image != null)
        {
            previewImageLabel.setIcon(
                    ImageUtilities.getScaledImage(image,
                                                  previewImageLabel.getWidth(),
                                                  previewImageLabel.getHeight(),
                                                  true));
        }
        previewImageLabel.setText("");

        playingFieldSizeDataLabel.setText(controller.getSportPlayingFieldWidth(sportName) + "m X " +
                                          controller.getSportPlayingFieldHeight(sportName) + "m");
    }

    private void showStrategiesOfSelectedSports()
    {
        strategiesListModel.clear();

        if (sportsList.getSelectedValue() != null)
        {
            for (Strategy strategy : controller.getStrategies(sportsList.getSelectedValue()))
            {
                strategiesListModel.addElement(strategy.getName());

                BufferedImage image = strategy.getStrategyPreviewImage();
                strategyImages.put(strategy.getName(),image);
                                   /*ImageUtilities.getScaledImage(image,
                                                                 imageWidth,
                                                                 imageHeight,
                                                                 true));*/
            }
        }
    }

    // TODO(Eric): IF the same as in StrategyComboBox, create a class in extensions
    private class StrategyListRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)
        {
            super.getListCellRendererComponent(list, value, index,
                                               isSelected, cellHasFocus);

            String imageName = value.toString();

            if (strategyImages.get(imageName) != null)
            {
                setIcon(ImageUtilities.getScaledImage(strategyImages.get(imageName),
                                      imageWidth,
                                      imageHeight,
                                      true));
            }
             
            setText(imageName);
            setVerticalTextPosition(BOTTOM);
            setHorizontalTextPosition(CENTER);

            setBorder(new EmptyBorder(10, 10, 10, 10));

            return this;
        }

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

        jScrollPane1 = new javax.swing.JScrollPane();
        sportsList = new javax.swing.JList<>();
        maxPlayerLabel = new javax.swing.JLabel();
        maxTeamLabel = new javax.swing.JLabel();
        strategyNumberLabel = new javax.swing.JLabel();
        sportsLabel = new javax.swing.JLabel();
        sportParametersLabel = new javax.swing.JLabel();
        playingFieldLabel = new javax.swing.JLabel();
        previewImageLabel = new javax.swing.JLabel();
        playingFieldSizeLabel = new javax.swing.JLabel();
        strategiesLabel = new javax.swing.JLabel();
        loadSportButton = new javax.swing.JButton();
        newSportButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        strategiesList = new javax.swing.JList<>();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        maxPlayerDataLabel = new javax.swing.JLabel();
        maxTeamDataLabel = new javax.swing.JLabel();
        strategyNumberDataLabel = new javax.swing.JLabel();
        playingFieldSizeDataLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        projectsList = new javax.swing.JList<>();
        projectsLabel = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        newProjectButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
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

        sportsList.setModel(new javax.swing.AbstractListModel<String>()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        sportsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        sportsList.setMaximumSize(new java.awt.Dimension(120, 85));
        sportsList.setMinimumSize(new java.awt.Dimension(120, 85));
        sportsList.setPreferredSize(new java.awt.Dimension(120, 85));
        jScrollPane1.setViewportView(sportsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        maxPlayerLabel.setText("Nombre de joueurs par équipe :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 5);
        getContentPane().add(maxPlayerLabel, gridBagConstraints);

        maxTeamLabel.setText("Nombre d'équipes :");
        maxTeamLabel.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(maxTeamLabel, gridBagConstraints);

        strategyNumberLabel.setText("Nombre de stratégies :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(strategyNumberLabel, gridBagConstraints);

        sportsLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        sportsLabel.setText("Sports");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 2);
        getContentPane().add(sportsLabel, gridBagConstraints);

        sportParametersLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        sportParametersLabel.setText("Paramètres du sport");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 2);
        getContentPane().add(sportParametersLabel, gridBagConstraints);

        playingFieldLabel.setText("Terrain :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(playingFieldLabel, gridBagConstraints);

        previewImageLabel.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        previewImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        previewImageLabel.setText("[Image Terrain]");
        previewImageLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        previewImageLabel.setMaximumSize(new java.awt.Dimension(200, 100));
        previewImageLabel.setMinimumSize(new java.awt.Dimension(200, 100));
        previewImageLabel.setPreferredSize(new java.awt.Dimension(200, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 10);
        getContentPane().add(previewImageLabel, gridBagConstraints);

        playingFieldSizeLabel.setText("Dimensions :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(playingFieldSizeLabel, gridBagConstraints);

        strategiesLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        strategiesLabel.setText("Stratégies");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        getContentPane().add(strategiesLabel, gridBagConstraints);

        loadSportButton.setText("Charger le sport");
        loadSportButton.setEnabled(false);
        loadSportButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loadSportButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 10);
        getContentPane().add(loadSportButton, gridBagConstraints);

        newSportButton.setText("Nouveau sport...");
        newSportButton.setEnabled(false);
        newSportButton.setMaximumSize(new java.awt.Dimension(120, 25));
        newSportButton.setMinimumSize(new java.awt.Dimension(120, 25));
        newSportButton.setPreferredSize(new java.awt.Dimension(120, 25));
        newSportButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                newSportButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 10, 2);
        getContentPane().add(newSportButton, gridBagConstraints);

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setEnabled(false);

        strategiesList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        strategiesList.setModel(new javax.swing.AbstractListModel<String>()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        strategiesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        strategiesList.setAutoscrolls(false);
        strategiesList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        jScrollPane2.setViewportView(strategiesList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        getContentPane().add(jScrollPane2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        getContentPane().add(filler1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        getContentPane().add(maxPlayerDataLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        getContentPane().add(maxTeamDataLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        getContentPane().add(strategyNumberDataLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        getContentPane().add(playingFieldSizeDataLabel, gridBagConstraints);

        projectsList.setModel(new javax.swing.AbstractListModel<String>()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        projectsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        projectsList.setMaximumSize(new java.awt.Dimension(120, 85));
        projectsList.setMinimumSize(new java.awt.Dimension(120, 85));
        projectsList.setPreferredSize(new java.awt.Dimension(120, 85));
        jScrollPane3.setViewportView(projectsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        getContentPane().add(jScrollPane3, gridBagConstraints);

        projectsLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        projectsLabel.setText("Projets");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 2, 2);
        getContentPane().add(projectsLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        getContentPane().add(filler2, gridBagConstraints);

        newProjectButton.setText("Nouveau projet...");
        newProjectButton.setMaximumSize(new java.awt.Dimension(120, 25));
        newProjectButton.setMinimumSize(new java.awt.Dimension(120, 25));
        newProjectButton.setPreferredSize(new java.awt.Dimension(120, 25));
        newProjectButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                newProjectButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        getContentPane().add(newProjectButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadSportButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_loadSportButtonActionPerformed
    {//GEN-HEADEREND:event_loadSportButtonActionPerformed
        loadSport();
    }//GEN-LAST:event_loadSportButtonActionPerformed

    private void loadSport()
    {
        controller.loadSport(sportsList.getSelectedValue());

        if (strategiesList.getSelectedValue() == null)
        {
            strategiesList.setSelectedIndex(0);
        }

        // controller.loadStrategy(strategiesList.getSelectedValue());
        // TODO(JFB) : Quickfix, problem with StrategyComboBox itemListener
        selectedStrategy = strategiesList.getSelectedValue();

        dispose();
    }
    
    // TODO(JFB) : Quickfix, problem with StrategyComboBox itemListener
    public String getSelectedStrategyName()
    {
        return selectedStrategy;
    }

    private void newSportButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newSportButtonActionPerformed
    {//GEN-HEADEREND:event_newSportButtonActionPerformed
        //NOTE(JFB) : Should we pass the MainWindow to be able to use showSportPropertyWindow from here?
        SportPropertyWindow sportPropertyWindow = new SportPropertyWindow(true);
        sportPropertyWindow.setIconImage(new ImageIcon(getClass().getResource("/res/logos/appLogo_16x16.png")).getImage());
        sportPropertyWindow.setLocationRelativeTo(this);
        sportPropertyWindow.setVisible(true);
        
        if(!sportsListModel.contains(controller.getCurrentSportName()))
        {
            sportsListModel.addElement(controller.getCurrentSportName());
        }

    }//GEN-LAST:event_newSportButtonActionPerformed

    private void newProjectButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newProjectButtonActionPerformed
    {//GEN-HEADEREND:event_newProjectButtonActionPerformed
        controller.createProject();
        dispose();
    }//GEN-LAST:event_newProjectButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        close();
    }//GEN-LAST:event_formWindowClosing

    private void close()
    {
        if (projectsListModel.size() == 0)
        {
            JOptionPane.showMessageDialog(this,
                                          "Veuillez d'abord créer un projet.",
                                          getTitle(),
                                          JOptionPane.ERROR_MESSAGE);
        }
        else if (sportsList.getSelectedIndex() >= 0)
        {
            loadSport();
        }
        else
        {
            dispose();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton loadSportButton;
    private javax.swing.JLabel maxPlayerDataLabel;
    private javax.swing.JLabel maxPlayerLabel;
    private javax.swing.JLabel maxTeamDataLabel;
    private javax.swing.JLabel maxTeamLabel;
    private javax.swing.JButton newProjectButton;
    private javax.swing.JButton newSportButton;
    private javax.swing.JLabel playingFieldLabel;
    private javax.swing.JLabel playingFieldSizeDataLabel;
    private javax.swing.JLabel playingFieldSizeLabel;
    private javax.swing.JLabel previewImageLabel;
    private javax.swing.JLabel projectsLabel;
    private javax.swing.JList<String> projectsList;
    private javax.swing.JLabel sportParametersLabel;
    private javax.swing.JLabel sportsLabel;
    private javax.swing.JList<String> sportsList;
    private javax.swing.JLabel strategiesLabel;
    private javax.swing.JList<String> strategiesList;
    private javax.swing.JLabel strategyNumberDataLabel;
    private javax.swing.JLabel strategyNumberLabel;
    // End of variables declaration//GEN-END:variables
}
