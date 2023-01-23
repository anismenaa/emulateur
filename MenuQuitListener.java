import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * classe MenuQuitListener.
 *
 * @author Anis Menaa
 * @version Version 6
 */
public class MenuQuitListener implements ActionListener
{
    // variables d'instance - Le Controler
    private CityControler controler;

    /**
     * Constructeur d'objets de classe MenuCommandsListener
     */
    public MenuQuitListener(CityControler controler)
    {
        // initialisation des variables d'instance
        this.controler=controler;
    }

    /**
     * Methode actionPerformed - Appel la Methode quit
     *
     * @param  e   ActionEvent
     * @return     void
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // Code
        controler.quit();
    }
}
