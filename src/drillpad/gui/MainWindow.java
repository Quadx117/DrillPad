package drillpad.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import drillpad.domain.SceneController;
import drillpad.domain.event.AnimationPropertiesChangedObserver;
import drillpad.domain.event.InstanceChangedObserver;
import drillpad.gui.extensions.RotationArrow;
import drillpad.gui.extensions.SeparatorComboBoxRenderer;
import drillpad.gui.extensions.SortedComboBox;

/**
 *
 * @author Eric Perron
 */
public class MainWindow extends javax.swing.JFrame implements AnimationPropertiesChangedObserver,
                                                              InstanceChangedObserver
{
    private enum ApplicationMode
    {
        ADD_ENTITY,
        ADD_PLAYING_ELEMENT,
        LINK_PLAYING_ELEMENT,
        ROTATION,
        SELECT
    }

    private final Cursor INVISIBLE_CURSOR;

    private SceneController controller = SceneController.getInstance();

    private Color backgroundColor;
    private ApplicationMode actualMode;

    private final List<Image> applicationIcons;

    // NOTE(Eric): These attributes are used to manage the movement
    private Point previousMousePosition = new Point();
    private final Point delta = new Point();

    private boolean animationWasPreviouslyRunning;

    // Used to display an arrow when it is possible to rotate a Player
    RotationArrow rotationArrow;
    
    // Used to block the actionlistener when we refresh comboboxes
    boolean actionListenerEnabled = true;

    /* The total number of values in this array should always be odd and evenly
       distributed on each side of the value 1.0f so that the cursor position
       for the jSlider is centered when the zoom zoomFactor is set to 1.0f (100 %).
       This ensures that the index in the middle, given by scaleValues.length / 2
       will always be the original zoom index.
     */
    private final float[] zoomFactorValues =
    {
        0.33f, 0.5f, 0.67f, 1.0f, 1.5f, 2.0f, 3.0f
    };

    // NOTE(Eric): If we change the border width (which is currently 1 on each
    // side by default) we will need to change the internal padding (ipadx and
    // ipady) in the gridBagConstraints of the drawingBorderPanel.
    private final Border blackLineBorder = BorderFactory.createLineBorder(Color.BLACK);
    private final Border emptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);

    private final String frameByFrameString = "Image par image";
    private final String realTimeAnimationString = "Temps réel";

    /**
     * Creates new form MainWindow
     */
    public MainWindow()
    {
        INVISIBLE_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
                Toolkit.getDefaultToolkit().getImage(""),
                new Point(0, 0),
                "Invisible cursor");

        backgroundColor = Color.WHITE;
        applicationIcons = loadApplicationIconImages();
        actualMode = ApplicationMode.SELECT;
        controller.addInstanceChangedObserver(this);
        initComponents();
        rotationArrow = new RotationArrow();
        drawingPanel.add(rotationArrow);
        rotationArrow.setVisible(false);

        // TODO(Eric): Observer pattern for this
        // NOTE: Needed for the marching ants selection effect
        new Timer(50, new ActionListener()
          {
              @Override
              public void actionPerformed(ActionEvent e)
              {
                  drawingPanel.repaint();
              }
          }).start();

        strategyComboBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent ie)
            {
                if (ie.getStateChange() == ItemEvent.SELECTED)
                {
                    if (controller.getCurrentSportStrategyCount() > 0)
                    {
                        controller.loadStrategy(strategyComboBox.getSelectedStrategyName());
                        String animationMode = (controller.isAnimationFrameByFrame()) ?
                                               frameByFrameString :
                                               realTimeAnimationString;
                        animationModeComboBox.setSelectedItem(animationMode);
                        resizeDrawingPanel();
                    }
                    else
                    {
                        refreshMenusAndButtonsState();
                    }
                }
                animationPropertiesChanged();
            }
        });

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

        displayModeMenuButtonGroup = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        toolBarPanel = new javax.swing.JPanel();
        strategyComboBox = new drillpad.gui.extensions.StrategyComboBox();
        topToolBarPanel = new javax.swing.JPanel();
        strategyToolBar = new javax.swing.JToolBar();
        addStrategyButton = new javax.swing.JButton();
        editStrategyButton = new javax.swing.JButton();
        deleteStrategyButton = new javax.swing.JButton();
        editPlayerButton = new javax.swing.JButton();
        editPlayingElementButton = new javax.swing.JButton();
        linkPlayingElementButton = new javax.swing.JButton();
        middleToolBarPanel = new javax.swing.JPanel();
        editToolBar = new javax.swing.JToolBar();
        undoButton = new javax.swing.JButton();
        redoButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        playbackToolBarSeparator = new javax.swing.JSeparator();
        playbackToolBar = new javax.swing.JToolBar();
        recordOffButton = new javax.swing.JButton();
        recordOnButton = new javax.swing.JButton();
        skipBackButton = new javax.swing.JButton();
        rewindButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        fastForwardButton = new javax.swing.JButton();
        skipForwardButton = new javax.swing.JButton();
        playbackToolBarFiller1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        skipAmountInSecondsSpinner = new javax.swing.JSpinner();
        playbackToolBarFiller2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        animationModeComboBox = new javax.swing.JComboBox<>();
        playbackToolBarFiller3 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        middleToolBarFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0));
        bottomToolBarPanel = new javax.swing.JPanel();
        entityToolBar = new javax.swing.JToolBar();
        addPlayingElementButton = new javax.swing.JButton();
        addEntityButton = new javax.swing.JButton();
        entityToolBarFiller1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        entityTypeComboBox = new SortedComboBox(1);
        entityToolBarFiller2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        roleComboBox = new SortedComboBox(1);
        mainScrollPane = new javax.swing.JScrollPane();
        mainScrollPanePanel = new javax.swing.JPanel();
        drawingBorderPanel = new javax.swing.JPanel();
        drawingPanel = new drillpad.gui.extensions.DrawingPanel();
        statusPanel = new javax.swing.JPanel();
        timeSlider = new javax.swing.JSlider();
        currentFrameLabel = new javax.swing.JLabel();
        mousePositionIconLabel = new javax.swing.JLabel();
        mousePositionLabel = new javax.swing.JLabel();
        zoomLabel = new javax.swing.JLabel();
        originalZoomButton = new javax.swing.JButton();
        zoomOutButton = new javax.swing.JButton();
        zoomSlider = new javax.swing.JSlider();
        zoomInButton = new javax.swing.JButton();
        topMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newProjectMenuItem = new javax.swing.JMenuItem();
        newSportMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exportMenuItem = new javax.swing.JMenuItem();
        fileMenuSeparator1 = new javax.swing.JPopupMenu.Separator();
        quitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        editMenuSeparator1 = new javax.swing.JPopupMenu.Separator();
        editSportMenuItem = new javax.swing.JMenuItem();
        editEntityTypeMenuItem = new javax.swing.JMenuItem();
        editRoleMenuItem = new javax.swing.JMenuItem();
        editPlayingElementMenuItem = new javax.swing.JMenuItem();
        backgroundColorMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        zoomInMenuItem = new javax.swing.JMenuItem();
        zoomOutMenuItem = new javax.swing.JMenuItem();
        originalZoomMenuItem = new javax.swing.JMenuItem();
        viewMenuSeparator1 = new javax.swing.JPopupMenu.Separator();
        toolBarsSubMenu = new javax.swing.JMenu();
        entityToolBarCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        strategyToolBarCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        editToolBarCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        playbackToolBarCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        showStatusBarCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        viewMenuSeparator2 = new javax.swing.JPopupMenu.Separator();
        displayModeSubMenu = new javax.swing.JMenu();
        simpleDisplayModeRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        showPlayerRolesCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        showPlayerNamesCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        showDrawingPanelBorderCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        playbackMenu = new javax.swing.JMenu();
        playPauseMenuItem = new javax.swing.JMenuItem();
        stopMenuItem = new javax.swing.JMenuItem();
        rewindMenuItem = new javax.swing.JMenuItem();
        fastForwardMenuItem = new javax.swing.JMenuItem();
        skipBackMenuItem = new javax.swing.JMenuItem();
        skipForwardMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VisuaLigue");
        setIconImages(applicationIcons);
        setMinimumSize(new java.awt.Dimension(600, 300));
        setName("MainWindow"); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter()
        {
            public void componentShown(java.awt.event.ComponentEvent evt)
            {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        mainPanel.setMinimumSize(new java.awt.Dimension(400, 300));
        mainPanel.setOpaque(false);
        mainPanel.setPreferredSize(new java.awt.Dimension(1260, 706));
        mainPanel.setLayout(new java.awt.BorderLayout());

        toolBarPanel.setBackground(new java.awt.Color(255, 255, 255));
        toolBarPanel.setFocusable(false);
        toolBarPanel.setPreferredSize(new java.awt.Dimension(712, 114));
        toolBarPanel.setLayout(new java.awt.GridBagLayout());

        strategyComboBox.setMinimumSize(new java.awt.Dimension(220, 92));
        strategyComboBox.setOpaque(false);
        strategyComboBox.setPreferredSize(new java.awt.Dimension(220, 92));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        toolBarPanel.add(strategyComboBox, gridBagConstraints);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        strategyComboBox.setPreferredSize(null);

        strategyComboBox.setMinimumSize(strategyComboBox.getPreferredSize());
        strategyComboBox.setMaximumSize(strategyComboBox.getPreferredSize());

        topToolBarPanel.setBackground(new java.awt.Color(255, 255, 255));
        topToolBarPanel.setMinimumSize(new java.awt.Dimension(48, 38));
        topToolBarPanel.setPreferredSize(new java.awt.Dimension(573, 30));
        topToolBarPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        strategyToolBar.setFloatable(false);
        strategyToolBar.setRollover(true);
        strategyToolBar.setFocusable(false);
        strategyToolBar.setMaximumSize(new java.awt.Dimension(10000, 28));
        strategyToolBar.setMinimumSize(new java.awt.Dimension(28, 28));
        strategyToolBar.setName(""); // NOI18N
        strategyToolBar.setOpaque(false);
        strategyToolBar.setPreferredSize(new java.awt.Dimension(800, 28));

        addStrategyButton.setText("Ajouter Stratégie...");
        addStrategyButton.setFocusable(false);
        addStrategyButton.setMaximumSize(new java.awt.Dimension(127, 28));
        addStrategyButton.setMinimumSize(new java.awt.Dimension(127, 28));
        addStrategyButton.setOpaque(false);
        addStrategyButton.setPreferredSize(new java.awt.Dimension(127, 28));
        addStrategyButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addStrategyButtonActionPerformed(evt);
            }
        });
        strategyToolBar.add(addStrategyButton);

        editStrategyButton.setText("Editer Stratégie...");
        editStrategyButton.setFocusable(false);
        editStrategyButton.setMaximumSize(new java.awt.Dimension(119, 28));
        editStrategyButton.setMinimumSize(new java.awt.Dimension(119, 28));
        editStrategyButton.setOpaque(false);
        editStrategyButton.setPreferredSize(new java.awt.Dimension(119, 28));
        editStrategyButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editStrategyButtonActionPerformed(evt);
            }
        });
        strategyToolBar.add(editStrategyButton);

        deleteStrategyButton.setText("Supprimer Stratégie");
        deleteStrategyButton.setFocusable(false);
        deleteStrategyButton.setMaximumSize(new java.awt.Dimension(127, 28));
        deleteStrategyButton.setMinimumSize(new java.awt.Dimension(127, 28));
        deleteStrategyButton.setOpaque(false);
        deleteStrategyButton.setPreferredSize(new java.awt.Dimension(127, 28));
        deleteStrategyButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                deleteStrategyButtonActionPerformed(evt);
            }
        });
        strategyToolBar.add(deleteStrategyButton);

        editPlayerButton.setText("Éditer joueur...");
        editPlayerButton.setEnabled(false);
        editPlayerButton.setFocusable(false);
        editPlayerButton.setMaximumSize(new java.awt.Dimension(107, 28));
        editPlayerButton.setMinimumSize(new java.awt.Dimension(107, 28));
        editPlayerButton.setOpaque(false);
        editPlayerButton.setPreferredSize(new java.awt.Dimension(107, 28));
        editPlayerButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editPlayerButtonActionPerformed(evt);
            }
        });
        strategyToolBar.add(editPlayerButton);

        editPlayingElementButton.setText("Éditer l'élément de jeu...");
        editPlayingElementButton.setFocusable(false);
        editPlayingElementButton.setMaximumSize(new java.awt.Dimension(151, 28));
        editPlayingElementButton.setMinimumSize(new java.awt.Dimension(151, 28));
        editPlayingElementButton.setOpaque(false);
        editPlayingElementButton.setPreferredSize(new java.awt.Dimension(151, 28));
        editPlayingElementButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editPlayingElementButtonActionPerformed(evt);
            }
        });
        strategyToolBar.add(editPlayingElementButton);

        linkPlayingElementButton.setText("Assigner l'élément de jeu...");
        linkPlayingElementButton.setEnabled(false);
        linkPlayingElementButton.setFocusable(false);
        linkPlayingElementButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        linkPlayingElementButton.setMaximumSize(new java.awt.Dimension(151, 28));
        linkPlayingElementButton.setMinimumSize(new java.awt.Dimension(151, 28));
        linkPlayingElementButton.setOpaque(false);
        linkPlayingElementButton.setPreferredSize(new java.awt.Dimension(151, 28));
        linkPlayingElementButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        linkPlayingElementButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                linkPlayingElementButtonActionPerformed(evt);
            }
        });
        strategyToolBar.add(linkPlayingElementButton);

        topToolBarPanel.add(strategyToolBar);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        strategyToolBar.setPreferredSize(null);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        toolBarPanel.add(topToolBarPanel, gridBagConstraints);
        topToolBarPanel.getAccessibleContext().setAccessibleName("");
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        topToolBarPanel.setPreferredSize(null);

        middleToolBarPanel.setBackground(new java.awt.Color(255, 255, 255));
        middleToolBarPanel.setMinimumSize(new java.awt.Dimension(48, 38));
        middleToolBarPanel.setLayout(new javax.swing.BoxLayout(middleToolBarPanel, javax.swing.BoxLayout.LINE_AXIS));

        editToolBar.setFloatable(false);
        editToolBar.setRollover(true);
        editToolBar.setFocusable(false);
        editToolBar.setMaximumSize(new java.awt.Dimension(100, 28));
        editToolBar.setMinimumSize(new java.awt.Dimension(28, 28));
        editToolBar.setName(""); // NOI18N
        editToolBar.setOpaque(false);
        editToolBar.setPreferredSize(new java.awt.Dimension(100, 28));

        undoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/editToolBar/edit-undo-5_24x24.png"))); // NOI18N
        undoButton.setToolTipText("Annuler (Ctrl+Z)");
        undoButton.setEnabled(false);
        undoButton.setFocusable(false);
        undoButton.setMaximumSize(new java.awt.Dimension(28, 28));
        undoButton.setMinimumSize(new java.awt.Dimension(28, 28));
        undoButton.setOpaque(false);
        undoButton.setPreferredSize(new java.awt.Dimension(28, 28));
        undoButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                undoButtonActionPerformed(evt);
            }
        });
        editToolBar.add(undoButton);

        redoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/editToolBar/edit-redo-5_24x24.png"))); // NOI18N
        redoButton.setToolTipText("Rétablir (Ctrl+Y)");
        redoButton.setEnabled(false);
        redoButton.setFocusable(false);
        redoButton.setMaximumSize(new java.awt.Dimension(28, 28));
        redoButton.setMinimumSize(new java.awt.Dimension(28, 28));
        redoButton.setOpaque(false);
        redoButton.setPreferredSize(new java.awt.Dimension(28, 28));
        redoButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                redoButtonActionPerformed(evt);
            }
        });
        editToolBar.add(redoButton);

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/editToolBar/edit-delete-6_24x24.png"))); // NOI18N
        deleteButton.setToolTipText("Effacer la sélection (Supprimer)");
        deleteButton.setEnabled(false);
        deleteButton.setFocusable(false);
        deleteButton.setMaximumSize(new java.awt.Dimension(28, 28));
        deleteButton.setMinimumSize(new java.awt.Dimension(28, 28));
        deleteButton.setOpaque(false);
        deleteButton.setPreferredSize(new java.awt.Dimension(28, 28));
        deleteButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                deleteButtonActionPerformed(evt);
            }
        });
        editToolBar.add(deleteButton);

        middleToolBarPanel.add(editToolBar);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        editToolBar.setPreferredSize(null);

        editToolBar.setMinimumSize(editToolBar.getPreferredSize());
        editToolBar.setMaximumSize(editToolBar.getPreferredSize());

        playbackToolBarSeparator.setOrientation(javax.swing.SwingConstants.VERTICAL);
        playbackToolBarSeparator.setMaximumSize(new java.awt.Dimension(1, 32767));
        playbackToolBarSeparator.setMinimumSize(new java.awt.Dimension(1, 0));
        playbackToolBarSeparator.setPreferredSize(new java.awt.Dimension(1, 0));
        middleToolBarPanel.add(playbackToolBarSeparator);

        playbackToolBar.setFloatable(false);
        playbackToolBar.setRollover(true);
        playbackToolBar.setFocusable(false);
        playbackToolBar.setMaximumSize(new java.awt.Dimension(10000, 28));
        playbackToolBar.setMinimumSize(new java.awt.Dimension(28, 28));
        playbackToolBar.setName(""); // NOI18N
        playbackToolBar.setOpaque(false);
        playbackToolBar.setPreferredSize(new java.awt.Dimension(469, 28));
        playbackToolBar.setRequestFocusEnabled(false);

        recordOffButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/playbackToolBar/media-record-off-4.png"))); // NOI18N
        recordOffButton.setToolTipText("Enregistrer");
        recordOffButton.setFocusable(false);
        recordOffButton.setMaximumSize(new java.awt.Dimension(28, 28));
        recordOffButton.setMinimumSize(new java.awt.Dimension(28, 28));
        recordOffButton.setOpaque(false);
        recordOffButton.setPreferredSize(new java.awt.Dimension(28, 28));
        recordOffButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                recordOffButtonActionPerformed(evt);
            }
        });
        playbackToolBar.add(recordOffButton);

        recordOnButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/playbackToolBar/media-record-4.png"))); // NOI18N
        recordOnButton.setToolTipText("Enregistrer");
        recordOnButton.setFocusable(false);
        recordOnButton.setMaximumSize(new java.awt.Dimension(28, 28));
        recordOnButton.setMinimumSize(new java.awt.Dimension(28, 28));
        recordOnButton.setOpaque(false);
        recordOnButton.setPreferredSize(new java.awt.Dimension(28, 28));
        recordOnButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                recordOnButtonActionPerformed(evt);
            }
        });
        playbackToolBar.add(recordOnButton);
        recordOnButton.setVisible(false);

        skipBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/playbackToolBar/media-skip-backward-4.png"))); // NOI18N
        skipBackButton.setToolTipText("Revenir en arrière (Ctrl+Maj+R)");
        skipBackButton.setFocusable(false);
        skipBackButton.setMaximumSize(new java.awt.Dimension(28, 28));
        skipBackButton.setMinimumSize(new java.awt.Dimension(28, 28));
        skipBackButton.setOpaque(false);
        skipBackButton.setPreferredSize(new java.awt.Dimension(28, 28));
        skipBackButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                skipBackButtonActionPerformed(evt);
            }
        });
        playbackToolBar.add(skipBackButton);

        rewindButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/playbackToolBar/media-seek-backward-4.png"))); // NOI18N
        rewindButton.setToolTipText("Rembobinage (Ctrl+R)");
        rewindButton.setFocusable(false);
        rewindButton.setMaximumSize(new java.awt.Dimension(28, 28));
        rewindButton.setMinimumSize(new java.awt.Dimension(28, 28));
        rewindButton.setOpaque(false);
        rewindButton.setPreferredSize(new java.awt.Dimension(28, 28));
        rewindButton.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                rewindButtonStateChanged(evt);
            }
        });
        playbackToolBar.add(rewindButton);

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/playbackToolBar/media-playback-stop-4.png"))); // NOI18N
        stopButton.setToolTipText("Arrêter (Ctrl+T)");
        stopButton.setFocusable(false);
        stopButton.setMaximumSize(new java.awt.Dimension(28, 28));
        stopButton.setMinimumSize(new java.awt.Dimension(28, 28));
        stopButton.setOpaque(false);
        stopButton.setPreferredSize(new java.awt.Dimension(28, 28));
        stopButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                stopButtonActionPerformed(evt);
            }
        });
        playbackToolBar.add(stopButton);

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/playbackToolBar/media-playback-start-4.png"))); // NOI18N
        playButton.setToolTipText("Lecture (Ctrl+P)");
        playButton.setFocusable(false);
        playButton.setMaximumSize(new java.awt.Dimension(28, 28));
        playButton.setMinimumSize(new java.awt.Dimension(28, 28));
        playButton.setOpaque(false);
        playButton.setPreferredSize(new java.awt.Dimension(28, 28));
        playButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                playButtonActionPerformed(evt);
            }
        });
        playbackToolBar.add(playButton);

        pauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/playbackToolBar/media-playback-pause-4.png"))); // NOI18N
        pauseButton.setToolTipText("Pause (Ctrl+P)");
        pauseButton.setFocusable(false);
        pauseButton.setMaximumSize(new java.awt.Dimension(28, 28));
        pauseButton.setMinimumSize(new java.awt.Dimension(28, 28));
        pauseButton.setOpaque(false);
        pauseButton.setPreferredSize(new java.awt.Dimension(28, 28));
        pauseButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pauseButtonActionPerformed(evt);
            }
        });
        playbackToolBar.add(pauseButton);
        pauseButton.setVisible(false);

        fastForwardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/playbackToolBar/media-seek-forward-4.png"))); // NOI18N
        fastForwardButton.setToolTipText("Avance rapide (Ctrl+F)");
        fastForwardButton.setFocusable(false);
        fastForwardButton.setMaximumSize(new java.awt.Dimension(28, 28));
        fastForwardButton.setMinimumSize(new java.awt.Dimension(28, 28));
        fastForwardButton.setOpaque(false);
        fastForwardButton.setPreferredSize(new java.awt.Dimension(28, 28));
        fastForwardButton.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                fastForwardButtonStateChanged(evt);
            }
        });
        playbackToolBar.add(fastForwardButton);

        skipForwardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/playbackToolBar/media-skip-forward-4.png"))); // NOI18N
        skipForwardButton.setToolTipText("Sauter en avant (Ctrl+Maj+F)");
        skipForwardButton.setFocusable(false);
        skipForwardButton.setMaximumSize(new java.awt.Dimension(28, 28));
        skipForwardButton.setMinimumSize(new java.awt.Dimension(28, 28));
        skipForwardButton.setOpaque(false);
        skipForwardButton.setPreferredSize(new java.awt.Dimension(28, 28));
        skipForwardButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                skipForwardButtonActionPerformed(evt);
            }
        });
        playbackToolBar.add(skipForwardButton);

        playbackToolBarFiller1.setFocusable(false);
        playbackToolBar.add(playbackToolBarFiller1);

        skipAmountInSecondsSpinner.setModel(new javax.swing.SpinnerNumberModel(3, 1, null, 1));
        skipAmountInSecondsSpinner.setToolTipText("Nombre de secondes pour le saut avant / arrière");
        skipAmountInSecondsSpinner.setMaximumSize(new java.awt.Dimension(38, 28));
        skipAmountInSecondsSpinner.setMinimumSize(new java.awt.Dimension(38, 28));
        skipAmountInSecondsSpinner.setPreferredSize(new java.awt.Dimension(38, 28));
        skipAmountInSecondsSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                skipAmountInSecondsSpinnerStateChanged(evt);
            }
        });
        playbackToolBar.add(skipAmountInSecondsSpinner);

        playbackToolBarFiller2.setFocusable(false);
        playbackToolBar.add(playbackToolBarFiller2);

        animationModeComboBox.setMaximumRowCount(2);
        animationModeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { frameByFrameString, realTimeAnimationString }));
        animationModeComboBox.setMaximumSize(new java.awt.Dimension(110, 28));
        animationModeComboBox.setMinimumSize(new java.awt.Dimension(110, 28));
        animationModeComboBox.setOpaque(false);
        animationModeComboBox.setPreferredSize(new java.awt.Dimension(110, 28));
        animationModeComboBox.setRenderer(new BasicComboBoxRenderer());
        animationModeComboBox.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                animationModeComboBoxItemStateChanged(evt);
            }
        });
        playbackToolBar.add(animationModeComboBox);

        playbackToolBarFiller3.setFocusable(false);
        playbackToolBar.add(playbackToolBarFiller3);

        middleToolBarPanel.add(playbackToolBar);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        playbackToolBar.setPreferredSize(null);

        playbackToolBar.setMinimumSize(playbackToolBar.getPreferredSize());
        playbackToolBar.setMaximumSize(playbackToolBar.getPreferredSize());

        middleToolBarPanel.add(middleToolBarFiller);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        toolBarPanel.add(middleToolBarPanel, gridBagConstraints);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        middleToolBarPanel.setPreferredSize(null);

        middleToolBarPanel.setMinimumSize(middleToolBarPanel.getPreferredSize());
        middleToolBarPanel.setMaximumSize(middleToolBarPanel.getPreferredSize());

        bottomToolBarPanel.setBackground(new java.awt.Color(255, 255, 255));
        bottomToolBarPanel.setMinimumSize(new java.awt.Dimension(48, 38));
        bottomToolBarPanel.setPreferredSize(new java.awt.Dimension(573, 30));
        bottomToolBarPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 2));

        entityToolBar.setFloatable(false);
        entityToolBar.setRollover(true);
        entityToolBar.setFocusable(false);
        entityToolBar.setMaximumSize(new java.awt.Dimension(10000, 28));
        entityToolBar.setMinimumSize(new java.awt.Dimension(28, 28));
        entityToolBar.setName(""); // NOI18N
        entityToolBar.setOpaque(false);
        entityToolBar.setPreferredSize(new java.awt.Dimension(600, 28));

        addPlayingElementButton.setText("Ajouter élément de jeu");
        addPlayingElementButton.setFocusable(false);
        addPlayingElementButton.setMaximumSize(new java.awt.Dimension(130, 28));
        addPlayingElementButton.setMinimumSize(new java.awt.Dimension(130, 28));
        addPlayingElementButton.setOpaque(false);
        addPlayingElementButton.setPreferredSize(new java.awt.Dimension(130, 28));
        addPlayingElementButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addPlayingElementButtonActionPerformed(evt);
            }
        });
        entityToolBar.add(addPlayingElementButton);

        addEntityButton.setMnemonic(java.awt.event.KeyEvent.VK_J);
        addEntityButton.setText("Ajouter entitée");
        addEntityButton.setFocusable(false);
        addEntityButton.setMaximumSize(new java.awt.Dimension(79, 28));
        addEntityButton.setMinimumSize(new java.awt.Dimension(79, 28));
        addEntityButton.setOpaque(false);
        addEntityButton.setPreferredSize(new java.awt.Dimension(79, 28));
        addEntityButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addEntityButtonActionPerformed(evt);
            }
        });
        entityToolBar.add(addEntityButton);

        entityToolBarFiller1.setFocusable(false);
        entityToolBar.add(entityToolBarFiller1);

        entityTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ajouter type d'entité..." }));
        entityTypeComboBox.setToolTipText("Le type d'entité à ajouter");
        entityTypeComboBox.setMaximumSize(new java.awt.Dimension(140, 28));
        entityTypeComboBox.setMinimumSize(new java.awt.Dimension(140, 28));
        entityTypeComboBox.setOpaque(false);
        entityTypeComboBox.setPreferredSize(new java.awt.Dimension(140, 28));
        entityTypeComboBox.setRenderer(new SeparatorComboBoxRenderer(0));
        entityTypeComboBox.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                entityTypeComboBoxItemStateChanged(evt);
            }
        });
        entityTypeComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                entityTypeComboBoxActionPerformed(evt);
            }
        });
        entityToolBar.add(entityTypeComboBox);

        entityToolBarFiller2.setFocusable(false);
        entityToolBar.add(entityToolBarFiller2);

        roleComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ajouter rôle..." }));
        roleComboBox.setToolTipText("Le rôle du joueur à ajouter (Si le type est une équipe)");
        roleComboBox.setMaximumSize(new java.awt.Dimension(140, 28));
        roleComboBox.setMinimumSize(new java.awt.Dimension(140, 28));
        roleComboBox.setOpaque(false);
        roleComboBox.setPreferredSize(new java.awt.Dimension(140, 28));
        roleComboBox.setRenderer(new SeparatorComboBoxRenderer(0));
        roleComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                roleComboBoxActionPerformed(evt);
            }
        });
        entityToolBar.add(roleComboBox);

        bottomToolBarPanel.add(entityToolBar);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        entityToolBar.setPreferredSize(null);

        entityToolBar.setMinimumSize(entityToolBar.getPreferredSize());
        entityToolBar.setMaximumSize(entityToolBar.getPreferredSize());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        toolBarPanel.add(bottomToolBarPanel, gridBagConstraints);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        bottomToolBarPanel.setPreferredSize(null);

        mainPanel.add(toolBarPanel, java.awt.BorderLayout.NORTH);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        toolBarPanel.setPreferredSize(null);

        mainScrollPane.setBorder(null);
        mainScrollPane.setOpaque(false);
        mainScrollPane.setPreferredSize(new java.awt.Dimension(1262, 683));
        // NOTE: Makes the scroll pane transparent so we can see the frame background color
        mainScrollPane.getViewport().setOpaque(false);
        mainScrollPane.addMouseWheelListener(new java.awt.event.MouseWheelListener()
        {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt)
            {
                mainScrollPaneMouseWheelMoved(evt);
            }
        });

        mainScrollPanePanel.setFocusable(false);
        mainScrollPanePanel.setOpaque(false);
        mainScrollPanePanel.setLayout(new java.awt.GridBagLayout());

        drawingBorderPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        drawingBorderPanel.setFocusable(false);
        drawingBorderPanel.setOpaque(false);
        drawingBorderPanel.setPreferredSize(new java.awt.Dimension(1261, 682));
        drawingBorderPanel.setLayout(new java.awt.GridBagLayout());

        drawingPanel.setOpaque(false);
        drawingPanel.setPreferredSize(new java.awt.Dimension(1259, 680));
        drawingPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseDragged(java.awt.event.MouseEvent evt)
            {
                drawingPanelMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt)
            {
                drawingPanelMouseMoved(evt);
            }
        });
        drawingPanel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                drawingPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                drawingPanelMouseReleased(evt);
            }
        });
        drawingPanel.setLayout(null);
        drawingBorderPanel.add(drawingPanel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        mainScrollPanePanel.add(drawingBorderPanel, gridBagConstraints);

        mainScrollPane.setViewportView(mainScrollPanePanel);

        mainPanel.add(mainScrollPane, java.awt.BorderLayout.CENTER);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        mainScrollPane.setPreferredSize(null);

        statusPanel.setFocusable(false);
        statusPanel.setMinimumSize(new java.awt.Dimension(343, 28));
        statusPanel.setOpaque(false);
        statusPanel.setPreferredSize(new java.awt.Dimension(1270, 28));
        statusPanel.setLayout(new java.awt.GridBagLayout());

        timeSlider.setMaximum(0);
        timeSlider.setFocusable(false);
        timeSlider.setOpaque(false);
        timeSlider.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                timeSliderStateChanged(evt);
            }
        });
        timeSlider.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                timeSliderMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                timeSliderMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        statusPanel.add(timeSlider, gridBagConstraints);

        currentFrameLabel.setText(controller.getAnimationStat());
        currentFrameLabel.setMaximumSize(new java.awt.Dimension(66, 28));
        currentFrameLabel.setMinimumSize(new java.awt.Dimension(66, 28));
        currentFrameLabel.setPreferredSize(new java.awt.Dimension(66, 28));
        currentFrameLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                currentFrameLabelMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 12);
        statusPanel.add(currentFrameLabel, gridBagConstraints);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        currentFrameLabel.setPreferredSize(null);

        currentFrameLabel.setMinimumSize(currentFrameLabel.getPreferredSize());
        currentFrameLabel.setMaximumSize(currentFrameLabel.getPreferredSize());

        mousePositionIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/statusBar/mousePosition-2_24x24.png"))); // NOI18N
        mousePositionIconLabel.setFocusable(false);
        mousePositionIconLabel.setMaximumSize(new java.awt.Dimension(28, 28));
        mousePositionIconLabel.setMinimumSize(new java.awt.Dimension(28, 28));
        mousePositionIconLabel.setPreferredSize(new java.awt.Dimension(28, 28));
        mousePositionIconLabel.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 4);
        statusPanel.add(mousePositionIconLabel, gridBagConstraints);

        mousePositionLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        mousePositionLabel.setText("1000, 1000 px");
        mousePositionLabel.setFocusable(false);
        mousePositionLabel.setMaximumSize(new java.awt.Dimension(200, 28));
        mousePositionLabel.setMinimumSize(new java.awt.Dimension(40, 28));
        mousePositionLabel.setPreferredSize(new java.awt.Dimension(80, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        statusPanel.add(mousePositionLabel, gridBagConstraints);

        zoomLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        zoomLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        zoomLabel.setText("100 %");
        zoomLabel.setFocusable(false);
        zoomLabel.setMaximumSize(new java.awt.Dimension(35, 28));
        zoomLabel.setMinimumSize(new java.awt.Dimension(35, 28));
        zoomLabel.setPreferredSize(new java.awt.Dimension(35, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        statusPanel.add(zoomLabel, gridBagConstraints);

        originalZoomButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/statusBar/zoom-orginal-size_24x24.png"))); // NOI18N
        originalZoomButton.setToolTipText("Taille réelle (Ctrl+Pavé numérique-0)");
        originalZoomButton.setContentAreaFilled(false);
        originalZoomButton.setFocusable(false);
        originalZoomButton.setMaximumSize(new java.awt.Dimension(28, 28));
        originalZoomButton.setMinimumSize(new java.awt.Dimension(28, 28));
        originalZoomButton.setPreferredSize(new java.awt.Dimension(28, 28));
        originalZoomButton.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                originalZoomButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                originalZoomButtonMouseExited(evt);
            }
        });
        originalZoomButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                originalZoomButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        statusPanel.add(originalZoomButton, gridBagConstraints);

        zoomOutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/statusBar/zoom-out_24x24.png"))); // NOI18N
        zoomOutButton.setToolTipText("Zoom arrière (Ctrl+Pavé numérique -)");
        zoomOutButton.setContentAreaFilled(false);
        zoomOutButton.setFocusable(false);
        zoomOutButton.setMaximumSize(new java.awt.Dimension(28, 28));
        zoomOutButton.setMinimumSize(new java.awt.Dimension(28, 28));
        zoomOutButton.setPreferredSize(new java.awt.Dimension(28, 28));
        zoomOutButton.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                zoomOutButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                zoomOutButtonMouseExited(evt);
            }
        });
        zoomOutButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                zoomOutButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        statusPanel.add(zoomOutButton, gridBagConstraints);

        zoomSlider.setMaximum(zoomFactorValues.length - 1);
        zoomSlider.setValue(zoomFactorValues.length / 2);
        zoomSlider.setFocusable(false);
        zoomSlider.setMaximumSize(new java.awt.Dimension(100, 28));
        zoomSlider.setMinimumSize(new java.awt.Dimension(100, 28));
        zoomSlider.setOpaque(false);
        zoomSlider.setPreferredSize(new java.awt.Dimension(100, 28));
        zoomSlider.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                zoomSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        statusPanel.add(zoomSlider, gridBagConstraints);

        zoomInButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/buttonIcons/statusBar/zoom-in_24x24.png"))); // NOI18N
        zoomInButton.setToolTipText("Zoom avant (Ctrl+Pavé numérique +)");
        zoomInButton.setContentAreaFilled(false);
        zoomInButton.setFocusable(false);
        zoomInButton.setMaximumSize(new java.awt.Dimension(28, 28));
        zoomInButton.setMinimumSize(new java.awt.Dimension(28, 28));
        zoomInButton.setPreferredSize(new java.awt.Dimension(28, 28));
        zoomInButton.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                zoomInButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                zoomInButtonMouseExited(evt);
            }
        });
        zoomInButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                zoomInButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 20);
        statusPanel.add(zoomInButton, gridBagConstraints);

        mainPanel.add(statusPanel, java.awt.BorderLayout.SOUTH);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        statusPanel.setPreferredSize(null);

        getContentPane().setBackground(backgroundColor);
        getContentPane().add(mainPanel);
        // NOTE: Force the component to recalculate it's preferred size based on it's child components
        mainPanel.setPreferredSize(null);

        fileMenu.setMnemonic(java.awt.event.KeyEvent.VK_F);
        fileMenu.setText("Fichier");

        newProjectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        newProjectMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/fileMenu/project-new_16x16.png"))); // NOI18N
        newProjectMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_P);
        newProjectMenuItem.setText("Nouveau projet...");
        newProjectMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                newProjectMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newProjectMenuItem);

        newSportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newSportMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/fileMenu/document-new-6_16x16.png"))); // NOI18N
        newSportMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_N);
        newSportMenuItem.setText("Nouveau sport...");
        newSportMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                newSportMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newSportMenuItem);

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/fileMenu/folder-open-2_16x16.png"))); // NOI18N
        openMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_O);
        openMenuItem.setText("Ouvrir...");
        openMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/fileMenu/document-save-5_16x16.png"))); // NOI18N
        saveMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_E);
        saveMenuItem.setText("Enregistrer");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/fileMenu/document-save-as-5_16x16.png"))); // NOI18N
        saveAsMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_S);
        saveAsMenuItem.setText("Enregistrer sous...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        exportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_X);
        exportMenuItem.setText("Exporter en tant qu'image...");
        exportMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exportMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exportMenuItem);
        fileMenu.add(fileMenuSeparator1);

        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/fileMenu/application-exit-2_16x16.png"))); // NOI18N
        quitMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_Q);
        quitMenuItem.setText("Quitter");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                quitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(quitMenuItem);

        topMenuBar.add(fileMenu);

        editMenu.setMnemonic(java.awt.event.KeyEvent.VK_D);
        editMenu.setText("Édition");

        undoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/editMenu/edit-undo-5_16x16.png"))); // NOI18N
        undoMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_A);
        undoMenuItem.setText("Annuler");
        undoMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                undoMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(undoMenuItem);

        redoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        redoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/editMenu/edit-redo-5_16x16.png"))); // NOI18N
        redoMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_R);
        redoMenuItem.setText("Rétablir");
        redoMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                redoMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(redoMenuItem);

        deleteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/editMenu/edit-delete-6_16x16.png"))); // NOI18N
        deleteMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_E);
        deleteMenuItem.setText("Effacer la sélection");
        deleteMenuItem.setEnabled(false);
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                deleteMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(deleteMenuItem);
        editMenu.add(editMenuSeparator1);

        editSportMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_S);
        editSportMenuItem.setText("Éditer le sport ...");
        editSportMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editSportMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editSportMenuItem);

        editEntityTypeMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_Q);
        editEntityTypeMenuItem.setText("Éditer les types d'entités...");
        editEntityTypeMenuItem.setEnabled(false);
        editEntityTypeMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editEntityTypeMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editEntityTypeMenuItem);

        editRoleMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_J);
        editRoleMenuItem.setText("Éditer les rôles des joueurs...");
        editRoleMenuItem.setEnabled(false);
        editRoleMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editRoleMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editRoleMenuItem);

        editPlayingElementMenuItem.setText("Éditer l'élément de jeu...");
        editPlayingElementMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editPlayingElementMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editPlayingElementMenuItem);

        backgroundColorMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_C);
        backgroundColorMenuItem.setText("Modifier couleur de fond...");
        backgroundColorMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                backgroundColorMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(backgroundColorMenuItem);

        topMenuBar.add(editMenu);

        viewMenu.setMnemonic(java.awt.event.KeyEvent.VK_A);
        viewMenu.setText("Affichage");

        zoomInMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, java.awt.event.InputEvent.CTRL_MASK));
        zoomInMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/viewMenu/zoom-in_16x16.png"))); // NOI18N
        zoomInMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_V);
        zoomInMenuItem.setText("Zoom avant");
        zoomInMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                zoomInMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(zoomInMenuItem);

        zoomOutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.CTRL_MASK));
        zoomOutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/viewMenu/zoom-out_16x16.png"))); // NOI18N
        zoomOutMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_R);
        zoomOutMenuItem.setText("Zoom arrière");
        zoomOutMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                zoomOutMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(zoomOutMenuItem);

        originalZoomMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_NUMPAD0, java.awt.event.InputEvent.CTRL_MASK));
        originalZoomMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/viewMenu/zoom-original_16x16.png"))); // NOI18N
        originalZoomMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_T);
        originalZoomMenuItem.setText("Taille réelle");
        originalZoomMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                originalZoomMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(originalZoomMenuItem);
        viewMenu.add(viewMenuSeparator1);

        toolBarsSubMenu.setMnemonic(java.awt.event.KeyEvent.VK_O);
        toolBarsSubMenu.setText("Barres d'outils");

        entityToolBarCheckBoxMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_P);
        entityToolBarCheckBoxMenuItem.setSelected(true);
        entityToolBarCheckBoxMenuItem.setText("Entitée");
        entityToolBarCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                entityToolBarCheckBoxMenuItemActionPerformed(evt);
            }
        });
        toolBarsSubMenu.add(entityToolBarCheckBoxMenuItem);

        strategyToolBarCheckBoxMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_S);
        strategyToolBarCheckBoxMenuItem.setSelected(true);
        strategyToolBarCheckBoxMenuItem.setText("Stratégie");
        strategyToolBarCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                strategyToolBarCheckBoxMenuItemActionPerformed(evt);
            }
        });
        toolBarsSubMenu.add(strategyToolBarCheckBoxMenuItem);

        editToolBarCheckBoxMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_N);
        editToolBarCheckBoxMenuItem.setSelected(true);
        editToolBarCheckBoxMenuItem.setText("Édition");
        editToolBarCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editToolBarCheckBoxMenuItemActionPerformed(evt);
            }
        });
        toolBarsSubMenu.add(editToolBarCheckBoxMenuItem);

        playbackToolBarCheckBoxMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_L);
        playbackToolBarCheckBoxMenuItem.setSelected(true);
        playbackToolBarCheckBoxMenuItem.setText("Lecture");
        playbackToolBarCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                playbackToolBarCheckBoxMenuItemActionPerformed(evt);
            }
        });
        toolBarsSubMenu.add(playbackToolBarCheckBoxMenuItem);

        viewMenu.add(toolBarsSubMenu);

        showStatusBarCheckBoxMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_E);
        showStatusBarCheckBoxMenuItem.setSelected(true);
        showStatusBarCheckBoxMenuItem.setText("Barre d'état");
        showStatusBarCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showStatusBarCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(showStatusBarCheckBoxMenuItem);
        viewMenu.add(viewMenuSeparator2);

        displayModeSubMenu.setMnemonic(java.awt.event.KeyEvent.VK_M);
        displayModeSubMenu.setText("Mode d'affichage");

        displayModeMenuButtonGroup.add(simpleDisplayModeRadioButtonMenuItem);
        simpleDisplayModeRadioButtonMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_S);
        simpleDisplayModeRadioButtonMenuItem.setText("Simple");
        displayModeSubMenu.add(simpleDisplayModeRadioButtonMenuItem);

        displayModeMenuButtonGroup.add(jRadioButtonMenuItem1);
        jRadioButtonMenuItem1.setMnemonic(java.awt.event.KeyEvent.VK_A);
        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("Avancé");
        displayModeSubMenu.add(jRadioButtonMenuItem1);

        viewMenu.add(displayModeSubMenu);

        showPlayerRolesCheckBoxMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_L);
        showPlayerRolesCheckBoxMenuItem.setSelected(true);
        showPlayerRolesCheckBoxMenuItem.setText("Afficher les roles des joueurs");
        showPlayerRolesCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showPlayerRolesCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(showPlayerRolesCheckBoxMenuItem);
        controller.setPlayerRolesVisible(showPlayerRolesCheckBoxMenuItem.isSelected());

        showPlayerNamesCheckBoxMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_N);
        showPlayerNamesCheckBoxMenuItem.setSelected(true);
        showPlayerNamesCheckBoxMenuItem.setText("Afficher les noms des joueurs");
        showPlayerNamesCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showPlayerNamesCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(showPlayerNamesCheckBoxMenuItem);
        controller.setPlayerNamesVisible(showPlayerNamesCheckBoxMenuItem.isSelected());

        showDrawingPanelBorderCheckBoxMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_B);
        showDrawingPanelBorderCheckBoxMenuItem.setText("Afficher la bordure de la zone de dessin");
        showDrawingPanelBorderCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showDrawingPanelBorderCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(showDrawingPanelBorderCheckBoxMenuItem);

        topMenuBar.add(viewMenu);

        playbackMenu.setMnemonic(java.awt.event.KeyEvent.VK_L);
        playbackMenu.setText("Lecture");

        playPauseMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        playPauseMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/playbackMenu/media-playback-start-4_16x16.png"))); // NOI18N
        playPauseMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_E);
        playPauseMenuItem.setText("Lecture / Pause");
        playPauseMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                playPauseMenuItemActionPerformed(evt);
            }
        });
        playbackMenu.add(playPauseMenuItem);

        stopMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        stopMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/playbackMenu/media-playback-stop-4_16x16.png"))); // NOI18N
        stopMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_T);
        stopMenuItem.setText("Arrêter");
        stopMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                stopMenuItemActionPerformed(evt);
            }
        });
        playbackMenu.add(stopMenuItem);

        rewindMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        rewindMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/playbackMenu/media-seek-backward-4_16x16.png"))); // NOI18N
        rewindMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_R);
        rewindMenuItem.setText("Rembobinage");
        rewindMenuItem.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                rewindMenuItemMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                rewindMenuItemMouseReleased(evt);
            }
        });
        playbackMenu.add(rewindMenuItem);
        rewindMenuItem.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
        put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,
            java.awt.event.InputEvent.CTRL_MASK,
            false),
        "rewindPressed");
    rewindMenuItem.getActionMap().put("rewindPressed",
        new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.rewindAnimation(true);
            }
        });
        rewindMenuItem.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
        put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,
            java.awt.event.InputEvent.CTRL_MASK,
            true),
        "fastForwardrewindReleased");
    rewindMenuItem.getActionMap().put("rewindReleased",
        new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.rewindAnimation(false);
            }
        });

        fastForwardMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        fastForwardMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/playbackMenu/media-seek-forward-4_16x16.png"))); // NOI18N
        fastForwardMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_A);
        fastForwardMenuItem.setText("Avance rapide");
        fastForwardMenuItem.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                fastForwardMenuItemMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                fastForwardMenuItemMouseReleased(evt);
            }
        });
        playbackMenu.add(fastForwardMenuItem);
        fastForwardMenuItem.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
        put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F,
            java.awt.event.InputEvent.CTRL_MASK,
            false),
        "fastForwardPressed");
    fastForwardMenuItem.getActionMap().put("fastForwardPressed",
        new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.fastForwardAnimation(true);
            }
        });
        fastForwardMenuItem.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
        put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F,
            java.awt.event.InputEvent.CTRL_MASK,
            true),
        "fastForwardReleased");
    fastForwardMenuItem.getActionMap().put("fastForwardReleased",
        new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.fastForwardAnimation(false);
            }
        });

        skipBackMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        skipBackMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/playbackMenu/media-skip-backward-4_16x16.png"))); // NOI18N
        skipBackMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_I);
        skipBackMenuItem.setText("Revenir en arrière");
        skipBackMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                skipBackMenuItemActionPerformed(evt);
            }
        });
        playbackMenu.add(skipBackMenuItem);

        skipForwardMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        skipForwardMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/menuIcons/playbackMenu/media-skip-forward-4_16x16.png"))); // NOI18N
        skipForwardMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_V);
        skipForwardMenuItem.setText("Sauter en avant");
        skipForwardMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                skipForwardMenuItemActionPerformed(evt);
            }
        });
        playbackMenu.add(skipForwardMenuItem);

        topMenuBar.add(playbackMenu);

        setJMenuBar(topMenuBar);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_quitMenuItemActionPerformed
    {//GEN-HEADEREND:event_quitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_quitMenuItemActionPerformed

    private void zoomSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_zoomSliderStateChanged
    {//GEN-HEADEREND:event_zoomSliderStateChanged
        controller.setZoomFactor(zoomFactorValues[zoomSlider.getValue()]);
        zoomLabel.setText(controller.getZoomLevelAsString());
        drawingBorderPanel.setPreferredSize(drawingPanel.getPreferredSize());
        mainScrollPanePanel.revalidate();
        drawingPanel.repaint();
    }//GEN-LAST:event_zoomSliderStateChanged

    private void saveState()
    {
        controller.saveState();
        undoButton.setEnabled(true);
        undoMenuItem.setEnabled(true);
    }

    private void undoMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_undoMenuItemActionPerformed
    {//GEN-HEADEREND:event_undoMenuItemActionPerformed
        //NOTE(JFB) : Quickfix to keep the current Strategy in case we undo
        String currentStrategy = controller.getCurrentStrategyName();

        if (controller.undoLastAction() > 0)
        {
            undoButton.setEnabled(true);
            undoMenuItem.setEnabled(true);
        }
        else
        {
            undoButton.setEnabled(false);
            undoMenuItem.setEnabled(false);
        }

        redoButton.setEnabled(true);
        redoMenuItem.setEnabled(true);

        setTitle(controller.getCurrentSportName() +
                 " - VisuaLigue [" +
                 (controller.getCurrentProjectPath() == null ? "Nouveau projet" :
                  controller.getCurrentProjectPath()) +
                 "]");

        refreshMenusAndButtonsState();
        rebuildComboboxesItems();
        strategyComboBox.setSelectedItem(currentStrategy);

        resizeDrawingPanel();

        controller.saveState();
    }//GEN-LAST:event_undoMenuItemActionPerformed

    private void drawingPanelMouseMoved(java.awt.event.MouseEvent evt)//GEN-FIRST:event_drawingPanelMouseMoved
    {//GEN-HEADEREND:event_drawingPanelMouseMoved
        Point mousePosition = evt.getPoint();
        updateMousePositionLabel(mousePosition);
        //Update only when needed in the drawing panel
        if (actualMode == ApplicationMode.LINK_PLAYING_ELEMENT)
        {
            drawingPanel.updateMousePosition(
                    controller.getNormalizedPosition(mousePosition));
        }

        if (controller.showRotationImage(mousePosition))
        {
            showRotationArrow(mousePosition);
        }
        else
        {
            hideRotationArrow();
        }
    }//GEN-LAST:event_drawingPanelMouseMoved

    private void zoomInMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_zoomInMenuItemActionPerformed
    {//GEN-HEADEREND:event_zoomInMenuItemActionPerformed
        int newValue = (zoomSlider.getValue() + 1 > zoomSlider.getMaximum()) ?
                       zoomSlider.getMaximum() : zoomSlider.getValue() + 1;
        zoomSlider.setValue(newValue);
    }//GEN-LAST:event_zoomInMenuItemActionPerformed

    private void zoomOutMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_zoomOutMenuItemActionPerformed
    {//GEN-HEADEREND:event_zoomOutMenuItemActionPerformed
        int newValue = (zoomSlider.getValue() - 1 < zoomSlider.getMinimum()) ?
                       zoomSlider.getMinimum() : zoomSlider.getValue() - 1;
        zoomSlider.setValue(newValue);
    }//GEN-LAST:event_zoomOutMenuItemActionPerformed

    private void originalZoomMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_originalZoomMenuItemActionPerformed
    {//GEN-HEADEREND:event_originalZoomMenuItemActionPerformed
        zoomSlider.setValue(getOriginalZoomIndex());
    }//GEN-LAST:event_originalZoomMenuItemActionPerformed

    private void drawingPanelMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_drawingPanelMousePressed
    {//GEN-HEADEREND:event_drawingPanelMousePressed
        Point mousePosition = evt.getPoint();
        previousMousePosition = mousePosition;
        // TODO(Eric): Handle this in a better way ?
        if (actualMode == ApplicationMode.ADD_ENTITY)
        {
            actualMode = ApplicationMode.SELECT;

            String entityTypeName = null;
            if (entityTypeComboBox.getSelectedIndex() > 0)
            {
                entityTypeName = entityTypeComboBox.getSelectedItem().toString();
            }

            String roleName = null;
            if (roleComboBox.getSelectedIndex() > 0)
            {
                roleName = roleComboBox.getSelectedItem().toString();
            }

            if (entityTypeName != null)
            {
                if (controller.isEntityTypeTeam(entityTypeName))
                {
                    if (roleName == null)
                    {
                        JOptionPane.showMessageDialog(this,
                                                      "Le role est obligatoire lors de l'ajout d'un joueur à une équipe.",
                                                      getTitle(),
                                                      JOptionPane.ERROR_MESSAGE);
                    }
                    else if (controller.isCurrentStrategyMaxNumberOfPlayersRestricted() &&
                             controller.getTeamPlayerCount(entityTypeName) >= controller.getMaxNumberOfPlayers())
                    {
                        JOptionPane.showMessageDialog(this,
                                                      "Vous avez atteint le maximum de joueur pour cette équipe",
                                                      getTitle(),
                                                      JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        controller.addPlayer(entityTypeName, roleName, mousePosition);
                        // TODO(Eric): Handle this mor gracefully :)
                        controller.switchSelectionStatus(mousePosition);
                        showPlayerPropertyWindow();
                        controller.switchSelectionStatus(mousePosition);
                        saveState();
                    }
                }
                else
                {
                    controller.addEntity(entityTypeName, mousePosition);
                    saveState();
                }
            }
            drawingPanel.repaint();
        }
        else if (actualMode == ApplicationMode.ADD_PLAYING_ELEMENT)
        {
            actualMode = ApplicationMode.SELECT;
            controller.addPlayingElement(mousePosition);
            saveState();
        }
        else if (SwingUtilities.isLeftMouseButton(evt) &&
                 controller.showRotationImage(mousePosition))
        {
            actualMode = ApplicationMode.ROTATION;
        }
        else if (actualMode == ApplicationMode.SELECT &&
                 SwingUtilities.isLeftMouseButton(evt))
        {
            int selectedEntitiesCount = controller.switchSelectionStatus(mousePosition);
            deleteButton.setEnabled(selectedEntitiesCount > 0);
            deleteMenuItem.setEnabled(selectedEntitiesCount > 0);
            editPlayerButton.setEnabled(controller.getSelectedPlayer() != null);
            addEntityButton.setEnabled(!(selectedEntitiesCount > 0));
            linkPlayingElementButton.setEnabled(controller.getSelectedPlayingElement() != null);
            drawingPanel.repaint();
        }
        else if (actualMode == ApplicationMode.LINK_PLAYING_ELEMENT)
        {
            actualMode = ApplicationMode.SELECT;

            controller.linkPlayingElementToPlayerLoc(mousePosition);

            drawingPanel.showLinkLine(false);
            drawingPanel.repaint();
            saveState();
        }

        drawingPanel.showPositionsAndArrows(true);
        controller.setCurrentStrategyPreviewImage(drawingPanel);
        drawingPanel.showPositionsAndArrows(false);
        strategyComboBox.updatePreviewImage(strategyComboBox.getSelectedStrategyName(),
                                            controller.getCurrentStrategyPreviewImage());

    }//GEN-LAST:event_drawingPanelMousePressed

    private void drawingPanelMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_drawingPanelMouseDragged
    {//GEN-HEADEREND:event_drawingPanelMouseDragged
        Point mousePosition = evt.getPoint();
        updateMousePositionLabel(mousePosition);

        if (SwingUtilities.isRightMouseButton(evt))
        {
            delta.setLocation((mousePosition.getX() - previousMousePosition.x),
                              (mousePosition.getY() - previousMousePosition.y));
            controller.updateSelectedItemsPositions(delta);
            previousMousePosition = evt.getPoint();
            drawingPanel.repaint();
        }
        else if (SwingUtilities.isLeftMouseButton(evt) &&
                 actualMode == ApplicationMode.ROTATION)
        {
            showRotationArrow(mousePosition);
            controller.rotateSelectedPlayer(previousMousePosition, mousePosition);
        }
    }//GEN-LAST:event_drawingPanelMouseDragged

    private void zoomInButtonMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_zoomInButtonMouseEntered
    {//GEN-HEADEREND:event_zoomInButtonMouseEntered
        zoomInButton.setContentAreaFilled(true);
    }//GEN-LAST:event_zoomInButtonMouseEntered

    private void zoomInButtonMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_zoomInButtonMouseExited
    {//GEN-HEADEREND:event_zoomInButtonMouseExited
        zoomInButton.setContentAreaFilled(false);
    }//GEN-LAST:event_zoomInButtonMouseExited

    private void zoomOutButtonMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_zoomOutButtonMouseEntered
    {//GEN-HEADEREND:event_zoomOutButtonMouseEntered
        zoomOutButton.setContentAreaFilled(true);
    }//GEN-LAST:event_zoomOutButtonMouseEntered

    private void zoomOutButtonMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_zoomOutButtonMouseExited
    {//GEN-HEADEREND:event_zoomOutButtonMouseExited
        zoomOutButton.setContentAreaFilled(false);
    }//GEN-LAST:event_zoomOutButtonMouseExited

    private void originalZoomButtonMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_originalZoomButtonMouseEntered
    {//GEN-HEADEREND:event_originalZoomButtonMouseEntered
        originalZoomButton.setContentAreaFilled(true);
    }//GEN-LAST:event_originalZoomButtonMouseEntered

    private void originalZoomButtonMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_originalZoomButtonMouseExited
    {//GEN-HEADEREND:event_originalZoomButtonMouseExited
        originalZoomButton.setContentAreaFilled(false);
    }//GEN-LAST:event_originalZoomButtonMouseExited

    private void zoomInButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_zoomInButtonActionPerformed
    {//GEN-HEADEREND:event_zoomInButtonActionPerformed
        zoomInMenuItem.doClick();
    }//GEN-LAST:event_zoomInButtonActionPerformed

    private void zoomOutButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_zoomOutButtonActionPerformed
    {//GEN-HEADEREND:event_zoomOutButtonActionPerformed
        zoomOutMenuItem.doClick();
    }//GEN-LAST:event_zoomOutButtonActionPerformed

    private void originalZoomButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_originalZoomButtonActionPerformed
    {//GEN-HEADEREND:event_originalZoomButtonActionPerformed
        originalZoomMenuItem.doClick();
    }//GEN-LAST:event_originalZoomButtonActionPerformed

    private void playPauseMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_playPauseMenuItemActionPerformed
    {//GEN-HEADEREND:event_playPauseMenuItemActionPerformed
        recordOffButton.setEnabled(false);
        recordOnButton.setEnabled(false);

        if (playButton.isVisible())
        {
            controller.startAnimation();
        }
        else
        {
            controller.pauseAnimation();
        }

        playButton.setVisible(!playButton.isVisible());
        pauseButton.setVisible(!pauseButton.isVisible());
    }//GEN-LAST:event_playPauseMenuItemActionPerformed

    private void stopMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_stopMenuItemActionPerformed
    {//GEN-HEADEREND:event_stopMenuItemActionPerformed
        controller.stopAnimation();
        recordOffButton.setEnabled(true);
        recordOnButton.setEnabled(true);
        playButton.setVisible(true);
        pauseButton.setVisible(false);
        timeSlider.setValue(0);
    }//GEN-LAST:event_stopMenuItemActionPerformed

    private void skipBackMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_skipBackMenuItemActionPerformed
    {//GEN-HEADEREND:event_skipBackMenuItemActionPerformed
        controller.skipBackAnimation();
    }//GEN-LAST:event_skipBackMenuItemActionPerformed

    private void skipForwardMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_skipForwardMenuItemActionPerformed
    {//GEN-HEADEREND:event_skipForwardMenuItemActionPerformed
        controller.skipForwardAnimation();
    }//GEN-LAST:event_skipForwardMenuItemActionPerformed

    private void backgroundColorMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backgroundColorMenuItemActionPerformed
    {//GEN-HEADEREND:event_backgroundColorMenuItemActionPerformed
        backgroundColor = JColorChooser.showDialog(null,
                                                   "Veuillez choisir la couleur d'arrière plan",
                                                   backgroundColor);
        if (backgroundColor != null)
        {
            getContentPane().setBackground(backgroundColor);
        }
    }//GEN-LAST:event_backgroundColorMenuItemActionPerformed

    private void redoMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_redoMenuItemActionPerformed
    {//GEN-HEADEREND:event_redoMenuItemActionPerformed
        //NOTE(JFB) : Quickfix to keep the current Strategy in case we redo
        String currentStrategy = controller.getCurrentStrategyName();
        
        if (controller.redoLastAction() > 0)
        {
            redoButton.setEnabled(true);
            redoMenuItem.setEnabled(true);
        }
        else
        {
            redoButton.setEnabled(false);
            redoMenuItem.setEnabled(false);
        }

        undoButton.setEnabled(true);
        undoMenuItem.setEnabled(true);

        setTitle(controller.getCurrentSportName() +
                 " - VisuaLigue [" +
                 (controller.getCurrentProjectPath() == null ? "Nouveau projet" :
                  controller.getCurrentProjectPath()) +
                 "]");

        refreshMenusAndButtonsState();
        rebuildComboboxesItems();
        strategyComboBox.setSelectedItem(currentStrategy);

        resizeDrawingPanel();

        controller.saveState();
    }//GEN-LAST:event_redoMenuItemActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_playButtonActionPerformed
    {//GEN-HEADEREND:event_playButtonActionPerformed
        playPauseMenuItem.doClick();
    }//GEN-LAST:event_playButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_stopButtonActionPerformed
    {//GEN-HEADEREND:event_stopButtonActionPerformed
        stopMenuItem.doClick();
    }//GEN-LAST:event_stopButtonActionPerformed

    private void skipBackButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_skipBackButtonActionPerformed
    {//GEN-HEADEREND:event_skipBackButtonActionPerformed
        skipBackMenuItem.doClick();
    }//GEN-LAST:event_skipBackButtonActionPerformed

    private void skipForwardButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_skipForwardButtonActionPerformed
    {//GEN-HEADEREND:event_skipForwardButtonActionPerformed
        skipForwardMenuItem.doClick();
    }//GEN-LAST:event_skipForwardButtonActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_undoButtonActionPerformed
    {//GEN-HEADEREND:event_undoButtonActionPerformed
        undoMenuItem.doClick();
    }//GEN-LAST:event_undoButtonActionPerformed

    private void redoButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_redoButtonActionPerformed
    {//GEN-HEADEREND:event_redoButtonActionPerformed
        redoMenuItem.doClick();
    }//GEN-LAST:event_redoButtonActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pauseButtonActionPerformed
    {//GEN-HEADEREND:event_pauseButtonActionPerformed
        playPauseMenuItem.doClick();
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void deleteMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteMenuItemActionPerformed
    {//GEN-HEADEREND:event_deleteMenuItemActionPerformed
        controller.removeSelectedEntities();
        deleteButton.setEnabled(false);
        deleteMenuItem.setEnabled(false);
        addEntityButton.setEnabled(true);
        saveState();
    }//GEN-LAST:event_deleteMenuItemActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteButtonActionPerformed
    {//GEN-HEADEREND:event_deleteButtonActionPerformed
        deleteMenuItem.doClick();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void editStrategyButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editStrategyButtonActionPerformed
    {//GEN-HEADEREND:event_editStrategyButtonActionPerformed
        showStrategyPropertyWindow(false);
    }//GEN-LAST:event_editStrategyButtonActionPerformed

    private void addStrategyButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addStrategyButtonActionPerformed
    {//GEN-HEADEREND:event_addStrategyButtonActionPerformed
        showStrategyPropertyWindow(true);
    }//GEN-LAST:event_addStrategyButtonActionPerformed

    private void skipAmountInSecondsSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_skipAmountInSecondsSpinnerStateChanged
    {//GEN-HEADEREND:event_skipAmountInSecondsSpinnerStateChanged
        try
        {
            skipAmountInSecondsSpinner.commitEdit();
        }
        catch (ParseException ex)
        {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        controller.setSkipAmountInSeconds((int) (skipAmountInSecondsSpinner.getValue()));
    }//GEN-LAST:event_skipAmountInSecondsSpinnerStateChanged

    private void recordOffButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_recordOffButtonActionPerformed
    {//GEN-HEADEREND:event_recordOffButtonActionPerformed
        recordOnButton.setVisible(true);
        recordOffButton.setVisible(false);
        playButton.setEnabled(false);
        playPauseMenuItem.setEnabled(false);
        stopButton.setEnabled(false);
        stopMenuItem.setEnabled(false);
        rewindButton.setEnabled(false);
        rewindMenuItem.setEnabled(false);
        fastForwardButton.setEnabled(false);
        fastForwardMenuItem.setEnabled(false);
        exportMenuItem.setEnabled(false);
        controller.setRecording(true);
    }//GEN-LAST:event_recordOffButtonActionPerformed

    private void recordOnButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_recordOnButtonActionPerformed
    {//GEN-HEADEREND:event_recordOnButtonActionPerformed
        recordOffButton.setVisible(true);
        recordOnButton.setVisible(false);
        playButton.setEnabled(true);
        playPauseMenuItem.setEnabled(true);
        stopButton.setEnabled(true);
        stopMenuItem.setEnabled(true);
        rewindButton.setEnabled(true);
        rewindMenuItem.setEnabled(true);
        fastForwardButton.setEnabled(true);
        fastForwardMenuItem.setEnabled(true);
        exportMenuItem.setEnabled(true);
        controller.setRecording(false);

        // NOTE(Eric): Brings the animation back to the start
        controller.stopAnimation();
    }//GEN-LAST:event_recordOnButtonActionPerformed

    private void showStatusBarCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showStatusBarCheckBoxMenuItemActionPerformed
    {//GEN-HEADEREND:event_showStatusBarCheckBoxMenuItemActionPerformed
        statusPanel.setVisible(showStatusBarCheckBoxMenuItem.isSelected());
    }//GEN-LAST:event_showStatusBarCheckBoxMenuItemActionPerformed

    private void entityToolBarCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_entityToolBarCheckBoxMenuItemActionPerformed
    {//GEN-HEADEREND:event_entityToolBarCheckBoxMenuItemActionPerformed
        entityToolBar.setVisible(entityToolBarCheckBoxMenuItem.isSelected());
    }//GEN-LAST:event_entityToolBarCheckBoxMenuItemActionPerformed

    private void strategyToolBarCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_strategyToolBarCheckBoxMenuItemActionPerformed
    {//GEN-HEADEREND:event_strategyToolBarCheckBoxMenuItemActionPerformed
        strategyToolBar.setVisible(strategyToolBarCheckBoxMenuItem.isSelected());
    }//GEN-LAST:event_strategyToolBarCheckBoxMenuItemActionPerformed

    private void editToolBarCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editToolBarCheckBoxMenuItemActionPerformed
    {//GEN-HEADEREND:event_editToolBarCheckBoxMenuItemActionPerformed
        editToolBar.setVisible(editToolBarCheckBoxMenuItem.isSelected());
    }//GEN-LAST:event_editToolBarCheckBoxMenuItemActionPerformed

    private void playbackToolBarCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_playbackToolBarCheckBoxMenuItemActionPerformed
    {//GEN-HEADEREND:event_playbackToolBarCheckBoxMenuItemActionPerformed
        playbackToolBar.setVisible(playbackToolBarCheckBoxMenuItem.isSelected());
        playbackToolBarSeparator.setVisible(playbackToolBar.isVisible());
    }//GEN-LAST:event_playbackToolBarCheckBoxMenuItemActionPerformed

    private void mainScrollPaneMouseWheelMoved(java.awt.event.MouseWheelEvent evt)//GEN-FIRST:event_mainScrollPaneMouseWheelMoved
    {//GEN-HEADEREND:event_mainScrollPaneMouseWheelMoved
        if (evt.isControlDown())
        {
            if (evt.getWheelRotation() < 0)
            {
                zoomInMenuItem.doClick();
            }
            else if (evt.getWheelRotation() > 0)
            {
                zoomOutMenuItem.doClick();
            }
        }
    }//GEN-LAST:event_mainScrollPaneMouseWheelMoved

    private void fastForwardButtonStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_fastForwardButtonStateChanged
    {//GEN-HEADEREND:event_fastForwardButtonStateChanged
        AbstractButton abstractButton = (AbstractButton) evt.getSource();
        ButtonModel buttonModel = abstractButton.getModel();
        controller.fastForwardAnimation(buttonModel.isPressed());
    }//GEN-LAST:event_fastForwardButtonStateChanged

    private void rewindButtonStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_rewindButtonStateChanged
    {//GEN-HEADEREND:event_rewindButtonStateChanged
        AbstractButton abstractButton = (AbstractButton) evt.getSource();
        ButtonModel buttonModel = abstractButton.getModel();
        controller.rewindAnimation(buttonModel.isPressed());
    }//GEN-LAST:event_rewindButtonStateChanged

    private void fastForwardMenuItemMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fastForwardMenuItemMousePressed
    {//GEN-HEADEREND:event_fastForwardMenuItemMousePressed
        if (SwingUtilities.isLeftMouseButton(evt))
        {
            controller.fastForwardAnimation(true);
            evt.consume();
        }
    }//GEN-LAST:event_fastForwardMenuItemMousePressed

    private void fastForwardMenuItemMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fastForwardMenuItemMouseReleased
    {//GEN-HEADEREND:event_fastForwardMenuItemMouseReleased
        if (SwingUtilities.isLeftMouseButton(evt))
        {
            controller.fastForwardAnimation(false);
            evt.consume();
        }
    }//GEN-LAST:event_fastForwardMenuItemMouseReleased

    private void rewindMenuItemMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_rewindMenuItemMousePressed
    {//GEN-HEADEREND:event_rewindMenuItemMousePressed
        if (SwingUtilities.isLeftMouseButton(evt))
        {
            controller.rewindAnimation(true);
        }
    }//GEN-LAST:event_rewindMenuItemMousePressed

    private void rewindMenuItemMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_rewindMenuItemMouseReleased
    {//GEN-HEADEREND:event_rewindMenuItemMouseReleased
        if (SwingUtilities.isLeftMouseButton(evt))
        {
            controller.rewindAnimation(false);
        }
    }//GEN-LAST:event_rewindMenuItemMouseReleased

    private void animationModeComboBoxItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_animationModeComboBoxItemStateChanged
    {//GEN-HEADEREND:event_animationModeComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {
            Object item = evt.getItem();
            if (item instanceof String)
            {
                String animationMode = (String) item;
                if (animationMode.equals(frameByFrameString))
                {
                    controller.setFrameByFrameAnimation(true);
                }
                else
                {
                    controller.setFrameByFrameAnimation(false);
                }
            }
        }

        animationPropertiesChanged();
    }//GEN-LAST:event_animationModeComboBoxItemStateChanged

    private void newSportMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newSportMenuItemActionPerformed
    {//GEN-HEADEREND:event_newSportMenuItemActionPerformed
        showSportPropertyWindow(true);
    }//GEN-LAST:event_newSportMenuItemActionPerformed

    private void editSportMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editSportMenuItemActionPerformed
    {//GEN-HEADEREND:event_editSportMenuItemActionPerformed
        showSportPropertyWindow(false);
    }//GEN-LAST:event_editSportMenuItemActionPerformed

    private void deleteStrategyButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteStrategyButtonActionPerformed
    {//GEN-HEADEREND:event_deleteStrategyButtonActionPerformed
        controller.removeStrategy(controller.getCurrentStrategyName());
        strategyComboBox.removeItem(controller.getCurrentStrategyName());

        refreshMenusAndButtonsState();
        saveState();
    }//GEN-LAST:event_deleteStrategyButtonActionPerformed

    private void editRoleMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editRoleMenuItemActionPerformed
    {//GEN-HEADEREND:event_editRoleMenuItemActionPerformed
        showRolePropertyWindow(false);
    }//GEN-LAST:event_editRoleMenuItemActionPerformed

    private void editEntityTypeMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editEntityTypeMenuItemActionPerformed
    {//GEN-HEADEREND:event_editEntityTypeMenuItemActionPerformed
        showEntityTypePropertyWindow(false);
    }//GEN-LAST:event_editEntityTypeMenuItemActionPerformed

    private void addEntityButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addEntityButtonActionPerformed
    {//GEN-HEADEREND:event_addEntityButtonActionPerformed
        actualMode = ApplicationMode.ADD_ENTITY;
    }//GEN-LAST:event_addEntityButtonActionPerformed

    private void showPlayerRolesCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showPlayerRolesCheckBoxMenuItemActionPerformed
    {//GEN-HEADEREND:event_showPlayerRolesCheckBoxMenuItemActionPerformed
        controller.setPlayerRolesVisible(showPlayerRolesCheckBoxMenuItem.isSelected());
    }//GEN-LAST:event_showPlayerRolesCheckBoxMenuItemActionPerformed

    private void editPlayerButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editPlayerButtonActionPerformed
    {//GEN-HEADEREND:event_editPlayerButtonActionPerformed
        showPlayerPropertyWindow();
    }//GEN-LAST:event_editPlayerButtonActionPerformed

    private void showDrawingPanelBorderCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showDrawingPanelBorderCheckBoxMenuItemActionPerformed
    {//GEN-HEADEREND:event_showDrawingPanelBorderCheckBoxMenuItemActionPerformed
        if (showDrawingPanelBorderCheckBoxMenuItem.isSelected())
        {
            drawingBorderPanel.setBorder(blackLineBorder);
        }
        else
        {
            drawingBorderPanel.setBorder(emptyBorder);
        }
    }//GEN-LAST:event_showDrawingPanelBorderCheckBoxMenuItemActionPerformed

    private void entityTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_entityTypeComboBoxActionPerformed
    {//GEN-HEADEREND:event_entityTypeComboBoxActionPerformed
        if (entityTypeComboBox.getSelectedIndex() == 0 &&
            evt.getModifiers() == ActionEvent.MOUSE_EVENT_MASK &&
            actionListenerEnabled)
        {
            showEntityTypePropertyWindow(true);
        }
    }//GEN-LAST:event_entityTypeComboBoxActionPerformed

    private void entityTypeComboBoxItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_entityTypeComboBoxItemStateChanged
    {//GEN-HEADEREND:event_entityTypeComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED &&
            entityTypeComboBox.getSelectedIndex() != 0)
        {
            Object item = evt.getItem();
            if (item instanceof String)
            {
                String entityType = (String) item;
                if (controller.isEntityTypeTeam(entityType))
                {
                    roleComboBox.setEnabled(true);
                }
                else
                {
                    roleComboBox.setEnabled(false);
                }
            }
        }
    }//GEN-LAST:event_entityTypeComboBoxItemStateChanged

    private void roleComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_roleComboBoxActionPerformed
    {//GEN-HEADEREND:event_roleComboBoxActionPerformed
        if (roleComboBox.getSelectedIndex() == 0 &&
            evt.getModifiers() == ActionEvent.MOUSE_EVENT_MASK &&
            actionListenerEnabled)
        {
            showRolePropertyWindow(true);
        }
    }//GEN-LAST:event_roleComboBoxActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_openMenuItemActionPerformed
    {//GEN-HEADEREND:event_openMenuItemActionPerformed
        showHomeScreenWindow();
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveMenuItemActionPerformed
    {//GEN-HEADEREND:event_saveMenuItemActionPerformed
        if (controller.getCurrentProjectPath() == null)
        {
            showSaveAsDialog();
        }
        else
        {
            controller.save();
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void editPlayingElementMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editPlayingElementMenuItemActionPerformed
    {//GEN-HEADEREND:event_editPlayingElementMenuItemActionPerformed
        showPlayingElementWindow();
    }//GEN-LAST:event_editPlayingElementMenuItemActionPerformed

    private void editPlayingElementButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editPlayingElementButtonActionPerformed
    {//GEN-HEADEREND:event_editPlayingElementButtonActionPerformed
        editPlayingElementMenuItem.doClick();
    }//GEN-LAST:event_editPlayingElementButtonActionPerformed

    private void showPlayerNamesCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showPlayerNamesCheckBoxMenuItemActionPerformed
    {//GEN-HEADEREND:event_showPlayerNamesCheckBoxMenuItemActionPerformed
        controller.setPlayerNamesVisible(showPlayerNamesCheckBoxMenuItem.isSelected());
    }//GEN-LAST:event_showPlayerNamesCheckBoxMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveAsMenuItemActionPerformed
    {//GEN-HEADEREND:event_saveAsMenuItemActionPerformed
        showSaveAsDialog();
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void newProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newProjectMenuItemActionPerformed
    {//GEN-HEADEREND:event_newProjectMenuItemActionPerformed
        controller.createProject();
        showSaveAsDialog();
        showSportPropertyWindow(true);
        setTitle(controller.getCurrentSportName() +
                 " - VisuaLigue [" +
                 (controller.getCurrentProjectPath() == null ? "Nouveau projet" :
                  controller.getCurrentProjectPath()) +
                 "]");
    }//GEN-LAST:event_newProjectMenuItemActionPerformed

    private void addPlayingElementButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addPlayingElementButtonActionPerformed
    {//GEN-HEADEREND:event_addPlayingElementButtonActionPerformed
        actualMode = ApplicationMode.ADD_PLAYING_ELEMENT;
    }//GEN-LAST:event_addPlayingElementButtonActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_formComponentShown
    {//GEN-HEADEREND:event_formComponentShown
        showHomeScreenWindow();
    }//GEN-LAST:event_formComponentShown

    private void drawingPanelMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_drawingPanelMouseReleased
    {//GEN-HEADEREND:event_drawingPanelMouseReleased
        hideRotationArrow();
        drawingPanel.showPositionsAndArrows(true);
        controller.setCurrentStrategyPreviewImage(drawingPanel);
        drawingPanel.showPositionsAndArrows(false);
        strategyComboBox.updatePreviewImage(strategyComboBox.getSelectedStrategyName(),
                                            controller.getCurrentStrategyPreviewImage());

        if ((actualMode == ApplicationMode.SELECT &&
             SwingUtilities.isRightMouseButton(evt)) ||
            actualMode == ApplicationMode.ROTATION)
        {
            //Save controller state after moving an entity
            saveState();
        }

        actualMode = ApplicationMode.SELECT;
    }//GEN-LAST:event_drawingPanelMouseReleased

    private void linkPlayingElementButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_linkPlayingElementButtonActionPerformed
    {//GEN-HEADEREND:event_linkPlayingElementButtonActionPerformed
        actualMode = ApplicationMode.LINK_PLAYING_ELEMENT;

        drawingPanel.showLinkLine(true);
        drawingPanel.repaint();
    }//GEN-LAST:event_linkPlayingElementButtonActionPerformed

    private void currentFrameLabelMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_currentFrameLabelMouseClicked
    {//GEN-HEADEREND:event_currentFrameLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_currentFrameLabelMouseClicked

    private void timeSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_timeSliderStateChanged
    {//GEN-HEADEREND:event_timeSliderStateChanged
        if (timeSlider.getValueIsAdjusting())
        {
            controller.setCurrentFrame(timeSlider.getValue());
        }
    }//GEN-LAST:event_timeSliderStateChanged

    private void exportMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exportMenuItemActionPerformed
    {//GEN-HEADEREND:event_exportMenuItemActionPerformed
        drawingPanel.showPositionsAndArrows(true);
        controller.setCurrentStrategyPreviewImage(drawingPanel);
        showExportAsImageDialog();
        drawingPanel.showPositionsAndArrows(false);
    }//GEN-LAST:event_exportMenuItemActionPerformed

    private void timeSliderMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_timeSliderMousePressed
    {//GEN-HEADEREND:event_timeSliderMousePressed
        if (controller.isAnimationRunning())
        {
            animationWasPreviouslyRunning = true;
            controller.pauseAnimation();
        }
        else
        {
            animationWasPreviouslyRunning = false;
        }
    }//GEN-LAST:event_timeSliderMousePressed

    private void timeSliderMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_timeSliderMouseReleased
    {//GEN-HEADEREND:event_timeSliderMouseReleased
        if (!controller.isAnimationRunning() &&
            animationWasPreviouslyRunning)
        {
            controller.startAnimation();
        }
    }//GEN-LAST:event_timeSliderMouseReleased

    private void showExportAsImageDialog()
    {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("Format PNG (*.png)", "png");
        fileChooser.setFileFilter(filter);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Format JPG (*.jpg)", "jpg"));

        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            String path = fileChooser.getSelectedFile().toString();
            if (!(path.endsWith(".png") || path.endsWith(".jpg")))
            {
                // TODO(JFB) : Probably handle this a better way...
                String ext = fileChooser.getFileFilter().toString();
                if (ext.contains("png"))
                {
                    path += ".png";
                }
                else
                {
                    path += ".jpg";
                }
            }

            try
            {
                BufferedImage image = controller.getCurrentStrategyPreviewImage();

                if (path.endsWith(".png"))
                {
                    ImageIO.write(image, "png", new File(path));
                }
                else if (path.endsWith(".jpg"))
                {
                    // NOTE(JFB) : need to remove alpha channel for jpg file
                    int w = image.getWidth();
                    int h = image.getHeight();
                    BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = newImage.createGraphics();
                    g2d.setColor(backgroundColor);
                    g2d.fillRect(0, 0, w, h);
                    g2d.drawImage(image, 0, 0, null);
                    g2d.dispose();
                    ImageIO.write(newImage, "jpg", new File(path));
                }
            }
            catch (IOException e)
            {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, e);
            }

        }

    }

    private void showSaveAsDialog()
    {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("VisuaLigue Save (*.vls)", "vls");
        fileChooser.setFileFilter(filter);

        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            String path = fileChooser.getSelectedFile().toString();
            if (!path.endsWith(".vls"))
            {
                path += ".vls";
            }
            controller.saveAs(path);
        }

        setTitle(controller.getCurrentSportName() +
                 " - VisuaLigue [" +
                 (controller.getCurrentProjectPath() == null ? "Nouveau projet" :
                  controller.getCurrentProjectPath()) +
                 "]");
    }

    private List<Image> loadApplicationIconImages()
    {
        List<Image> appIcons = new ArrayList();
        appIcons.add(new ImageIcon(getClass().getResource("/res/logos/appLogo_16x16.png")).getImage());
        appIcons.add(new ImageIcon(getClass().getResource("/res/logos/appLogo_20x20.png")).getImage());
        appIcons.add(new ImageIcon(getClass().getResource("/res/logos/appLogo_24x24.png")).getImage());
        appIcons.add(new ImageIcon(getClass().getResource("/res/logos/appLogo_32x32.png")).getImage());
        appIcons.add(new ImageIcon(getClass().getResource("/res/logos/appLogo_64x64.png")).getImage());

        return appIcons;
    }

    /**
     * This method is used to resize the drawing panel to the size of the loaded
     * image
     */
    private void resizeDrawingPanel()
    {
        Dimension dimension = controller.getImageDimension();
        drawingPanel.setPreferredSize(dimension);
        drawingBorderPanel.setPreferredSize(drawingPanel.getPreferredSize());
        mainScrollPanePanel.revalidate();
        drawingPanel.repaint();
    }

    private void updateMousePositionLabel(Point mousePositionInPixel)
    {
        String mousePosition = controller.getMousePositionAsString(mousePositionInPixel);
        mousePositionLabel.setText(mousePosition);
        mousePositionLabel.setPreferredSize(null);
    }

    /* In order for this mehtod to return the right value, the array must be
     * balanced (see comment on array declaration).
     */
    private int getOriginalZoomIndex()
    {
        assert (zoomFactorValues[zoomFactorValues.length / 2] == 1.0f) : "The array is unbalanced";
        return (zoomFactorValues.length / 2);
    }

    private void resizeCurrentFrameLabel()
    {
        // Resize the label
        currentFrameLabel.setPreferredSize(null);
        currentFrameLabel.setMinimumSize(currentFrameLabel.getPreferredSize());
        currentFrameLabel.setMaximumSize(currentFrameLabel.getPreferredSize());

        // Resize the toolbar
        playbackToolBar.setPreferredSize(null);
        playbackToolBar.setMinimumSize(playbackToolBar.getPreferredSize());
        playbackToolBar.setMaximumSize(playbackToolBar.getPreferredSize());
    }

    private void showHomeScreenWindow()
    {
        int padding = 25;

        HomeScreenWindow homeScreenWindow = new HomeScreenWindow();

        Dimension dimension = new Dimension(mainScrollPane.getWidth() - padding,
                                            mainScrollPane.getHeight() - padding);

        homeScreenWindow.setPreferredSize(dimension);
        homeScreenWindow.setSize(dimension);
        homeScreenWindow.setIconImages(applicationIcons);
        homeScreenWindow.setLocationRelativeTo(mainScrollPane);
        homeScreenWindow.setVisible(true);

        if (controller.getCurrentProjectPath() == null)
        {
            showSaveAsDialog();
            showSportPropertyWindow(true);
        }
        else
        {
            setTitle(controller.getCurrentSportName() +
                     " - VisuaLigue [" +
                     (controller.getCurrentProjectPath() == null ?
                      "Nouveau projet" :
                      controller.getCurrentProjectPath()) +
                     "]");

            refreshMenusAndButtonsState();
            rebuildComboboxesItems();

            // TODO(JFB) : Quickfix to remove later.
            String strategyName = homeScreenWindow.getSelectedStrategyName();
            if (strategyName != null)
            {
                controller.loadStrategy(strategyName);
                strategyComboBox.setSelectedItem(strategyName);
            }

            resizeDrawingPanel();
        }

        // Initial state at loading for undo/redo
        saveState();
    }

    private void showSportPropertyWindow(boolean isNewSport)
    {
        SportPropertyWindow sportPropertyWindow = new SportPropertyWindow(isNewSport);
        sportPropertyWindow.setIconImages(applicationIcons);
        sportPropertyWindow.setLocationRelativeTo(this);
        sportPropertyWindow.setVisible(true);

        if (controller.getSportsCount() > 0)
        {
            setTitle(controller.getCurrentSportName() +
                     " - VisuaLigue [" +
                     (controller.getCurrentProjectPath() == null ?
                      "Nouveau projet" :
                      controller.getCurrentProjectPath()) +
                     "]");
            resizeDrawingPanel();

            if (isNewSport)
            {
                rebuildComboboxesItems();
            }
        }

        // TODO(JFB) : Handle this more gracefully (Null Sports)
        controller.save();

        // NOTE(JFB) : For undo/redo
        saveState();
    }

    private void showStrategyPropertyWindow(boolean isNewStrategy)
    {
        int currentNumberOfStrategies = controller.getCurrentSportStrategyCount();

        StrategyPropertyWindow strategyPropertyWindow =
                new StrategyPropertyWindow(strategyComboBox,
                                           isNewStrategy);
        strategyPropertyWindow.setIconImages(applicationIcons);
        strategyPropertyWindow.setLocationRelativeTo(this);
        strategyPropertyWindow.setVisible(true);

        if (isNewStrategy &&
            currentNumberOfStrategies < controller.getCurrentSportStrategyCount())
        {
            controller.addAnimationPropertiesChangedObserver(this);
            controller.setSkipAmountInSeconds((int) (skipAmountInSecondsSpinner.getValue()));
            controller.setFrameByFrameAnimation(
                    animationModeComboBox.getSelectedItem().toString().equals(frameByFrameString));
        }

        refreshMenusAndButtonsState();
        resizeDrawingPanel();
        saveState();
    }

    private void showRolePropertyWindow(boolean isNewRole)
    {
        RolePropertyWindow rolePropertyWindow =
                new RolePropertyWindow(isNewRole, roleComboBox);
        rolePropertyWindow.setIconImages(applicationIcons);
        rolePropertyWindow.setLocationRelativeTo(this);
        rolePropertyWindow.setVisible(true);

        editRoleMenuItem.setEnabled(!controller.getRoleNames().isEmpty());

        saveState();
    }

    private void showEntityTypePropertyWindow(boolean isNewEntityType)
    {
        EntityTypePropertyWindow entityTypePropertyWindow =
                new EntityTypePropertyWindow(isNewEntityType, entityTypeComboBox);
        entityTypePropertyWindow.setIconImages(applicationIcons);
        entityTypePropertyWindow.setLocationRelativeTo(this);
        entityTypePropertyWindow.setVisible(true);

        editEntityTypeMenuItem.setEnabled(!controller.getEntityTypeNames().isEmpty());

        saveState();
    }

    private void showPlayerPropertyWindow()
    {
        PlayerPropertyWindow playerPropertyWindow =
                new PlayerPropertyWindow(controller.getSelectedPlayer());
        playerPropertyWindow.setIconImages(applicationIcons);
        playerPropertyWindow.setLocationRelativeTo(this);
        playerPropertyWindow.setVisible(true);

        saveState();
    }

    private void showPlayingElementWindow()
    {
        PlayingElementWindow playingElementWindow = new PlayingElementWindow();
        playingElementWindow.setIconImages(applicationIcons);
        playingElementWindow.setLocationRelativeTo(this);
        playingElementWindow.setVisible(true);
        resizeDrawingPanel();

        saveState();
    }

    @Override
    public void instanceChanged()
    {
        controller = SceneController.getInstance();

        // NOTE(Eric): We need to add ourself back to all other observer lists here
        controller.addToAllAnimationPropertiesChangedObserver(this);
    }

    private void refreshMenusAndButtonsState()
    {
        boolean enable = controller.getCurrentSportStrategyCount() > 0;

        // Menus
        exportMenuItem.setEnabled(enable);
        playPauseMenuItem.setEnabled(enable);
        stopMenuItem.setEnabled(enable);
        rewindMenuItem.setEnabled(enable);
        fastForwardMenuItem.setEnabled(enable);
        skipBackMenuItem.setEnabled(enable);
        skipForwardMenuItem.setEnabled(enable);
        playbackMenu.setEnabled(enable);

        // Buttons
        editStrategyButton.setEnabled(enable);
        deleteStrategyButton.setEnabled(enable);
        addEntityButton.setEnabled(enable);
        addPlayingElementButton.setEnabled(enable);
        linkPlayingElementButton.setEnabled(controller.getSelectedPlayingElement() != null);
        recordOnButton.setEnabled(enable);
        recordOffButton.setEnabled(enable);
        skipBackButton.setEnabled(enable);
        rewindButton.setEnabled(enable);
        stopButton.setEnabled(enable);
        playButton.setEnabled(enable);
        pauseButton.setEnabled(enable);
        fastForwardButton.setEnabled(enable);
        skipForwardButton.setEnabled(enable);

        // Others
        strategyComboBox.setEnabled(enable);
        animationModeComboBox.setEnabled(enable);
    }

    @Override
    public void animationPropertiesChanged()
    {
        timeSlider.setMaximum(controller.getMaxFrameIndex());
        timeSlider.setValue(controller.getCurrentFrameIndex());
        // TODO(Eric): Find a better name for this
        currentFrameLabel.setText(controller.getAnimationStat());
    }

    private void rebuildComboboxesItems()
    {
        strategyComboBox.removeAllItems();
        strategyComboBox.addItems(controller.getStrategyPreviewImages());
        strategyComboBox.setEnabled((controller.getCurrentSportStrategyCount() > 0));

        actionListenerEnabled = false;
        entityTypeComboBox.removeAllSortedElements();
        for (String entityType : controller.getEntityTypeNames())
        {
            entityTypeComboBox.addItem(entityType);
        }
        editEntityTypeMenuItem.setEnabled(!controller.getEntityTypeNames().isEmpty());

        roleComboBox.removeAllSortedElements();
        for (String role : controller.getRoleNames())
        {
            roleComboBox.addItem(role);
        }
        editRoleMenuItem.setEnabled(!controller.getRoleNames().isEmpty());
        actionListenerEnabled = true;
    }

    private void showRotationArrow(Point mousePosition)
    {
        Point2D.Float normalizedPosition =
                controller.getNormalizedPosition(mousePosition);
        drawingPanel.setCursor(INVISIBLE_CURSOR);
        int x = mousePosition.x - (rotationArrow.getBounds().width / 2);
        int y = mousePosition.y - (rotationArrow.getBounds().height / 2);
        rotationArrow.setLocation(x, y);

        float xOrigin = controller.getSelectedPlayer().getPosition().x;
        float yOrigin = controller.getSelectedPlayer().getPosition().y;
        float xPosition = normalizedPosition.x - xOrigin;
        float yPosition = normalizedPosition.y - yOrigin;
        float angle = (float) Math.atan2(yPosition, xPosition);
        rotationArrow.setAngleInRadians(angle);
        rotationArrow.setVisible(true);
    }

    private void hideRotationArrow()
    {
        drawingPanel.setCursor(Cursor.getDefaultCursor());
        rotationArrow.setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addEntityButton;
    private javax.swing.JButton addPlayingElementButton;
    private javax.swing.JButton addStrategyButton;
    private javax.swing.JComboBox<String> animationModeComboBox;
    private javax.swing.JMenuItem backgroundColorMenuItem;
    private javax.swing.JPanel bottomToolBarPanel;
    private javax.swing.JLabel currentFrameLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JButton deleteStrategyButton;
    private javax.swing.ButtonGroup displayModeMenuButtonGroup;
    private javax.swing.JMenu displayModeSubMenu;
    private javax.swing.JPanel drawingBorderPanel;
    private drillpad.gui.extensions.DrawingPanel drawingPanel;
    private javax.swing.JMenuItem editEntityTypeMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JPopupMenu.Separator editMenuSeparator1;
    private javax.swing.JButton editPlayerButton;
    private javax.swing.JButton editPlayingElementButton;
    private javax.swing.JMenuItem editPlayingElementMenuItem;
    private javax.swing.JMenuItem editRoleMenuItem;
    private javax.swing.JMenuItem editSportMenuItem;
    private javax.swing.JButton editStrategyButton;
    private javax.swing.JToolBar editToolBar;
    private javax.swing.JCheckBoxMenuItem editToolBarCheckBoxMenuItem;
    private javax.swing.JToolBar entityToolBar;
    private javax.swing.JCheckBoxMenuItem entityToolBarCheckBoxMenuItem;
    private javax.swing.Box.Filler entityToolBarFiller1;
    private javax.swing.Box.Filler entityToolBarFiller2;
    private drillpad.gui.extensions.SortedComboBox entityTypeComboBox;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JButton fastForwardButton;
    private javax.swing.JMenuItem fastForwardMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPopupMenu.Separator fileMenuSeparator1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JButton linkPlayingElementButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JPanel mainScrollPanePanel;
    private javax.swing.Box.Filler middleToolBarFiller;
    private javax.swing.JPanel middleToolBarPanel;
    private javax.swing.JLabel mousePositionIconLabel;
    private javax.swing.JLabel mousePositionLabel;
    private javax.swing.JMenuItem newProjectMenuItem;
    private javax.swing.JMenuItem newSportMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JButton originalZoomButton;
    private javax.swing.JMenuItem originalZoomMenuItem;
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton playButton;
    private javax.swing.JMenuItem playPauseMenuItem;
    private javax.swing.JMenu playbackMenu;
    private javax.swing.JToolBar playbackToolBar;
    private javax.swing.JCheckBoxMenuItem playbackToolBarCheckBoxMenuItem;
    private javax.swing.Box.Filler playbackToolBarFiller1;
    private javax.swing.Box.Filler playbackToolBarFiller2;
    private javax.swing.Box.Filler playbackToolBarFiller3;
    private javax.swing.JSeparator playbackToolBarSeparator;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JButton recordOffButton;
    private javax.swing.JButton recordOnButton;
    private javax.swing.JButton redoButton;
    private javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JButton rewindButton;
    private javax.swing.JMenuItem rewindMenuItem;
    private drillpad.gui.extensions.SortedComboBox roleComboBox;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JCheckBoxMenuItem showDrawingPanelBorderCheckBoxMenuItem;
    private javax.swing.JCheckBoxMenuItem showPlayerNamesCheckBoxMenuItem;
    private javax.swing.JCheckBoxMenuItem showPlayerRolesCheckBoxMenuItem;
    private javax.swing.JCheckBoxMenuItem showStatusBarCheckBoxMenuItem;
    private javax.swing.JRadioButtonMenuItem simpleDisplayModeRadioButtonMenuItem;
    private javax.swing.JSpinner skipAmountInSecondsSpinner;
    private javax.swing.JButton skipBackButton;
    private javax.swing.JMenuItem skipBackMenuItem;
    private javax.swing.JButton skipForwardButton;
    private javax.swing.JMenuItem skipForwardMenuItem;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JButton stopButton;
    private javax.swing.JMenuItem stopMenuItem;
    private drillpad.gui.extensions.StrategyComboBox strategyComboBox;
    private javax.swing.JToolBar strategyToolBar;
    private javax.swing.JCheckBoxMenuItem strategyToolBarCheckBoxMenuItem;
    private javax.swing.JSlider timeSlider;
    private javax.swing.JPanel toolBarPanel;
    private javax.swing.JMenu toolBarsSubMenu;
    private javax.swing.JMenuBar topMenuBar;
    private javax.swing.JPanel topToolBarPanel;
    private javax.swing.JButton undoButton;
    private javax.swing.JMenuItem undoMenuItem;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JPopupMenu.Separator viewMenuSeparator1;
    private javax.swing.JPopupMenu.Separator viewMenuSeparator2;
    private javax.swing.JButton zoomInButton;
    private javax.swing.JMenuItem zoomInMenuItem;
    private javax.swing.JLabel zoomLabel;
    private javax.swing.JButton zoomOutButton;
    private javax.swing.JMenuItem zoomOutMenuItem;
    private javax.swing.JSlider zoomSlider;
    // End of variables declaration//GEN-END:variables

}
// TODO(Eric): Est-ce qu'on veut un bouton "Ajuster à la page" (best fit) ?
