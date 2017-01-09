package drillpad;

import javax.swing.UIManager;

import drillpad.gui.MainWindow;

/**
 * This is the main entry point of the application
 *
 * @author Eric Perron
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // Use the system's look and feel
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        drillpad.gui.MainWindow mainWindow = new drillpad.gui.MainWindow();
        mainWindow.setVisible(true);
    }

}
