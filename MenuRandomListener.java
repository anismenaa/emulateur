import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * classe MenuRandomListener.
 *
 * @author William Mesnil
 * @version Version 6
 */
public class MenuRandomListener implements ActionListener
{
    // variables d'instance - Le Controler
    //On ecoute bouton Random si le Menu en a un
    private CityControler controler;

    /**
     * Constructeur d'objets de classe MenuRandomListener
     */
    public MenuRandomListener(CityControler controler)
    {
        // initialisation des variables d'instance
        this.controler=controler;
    }

    /**
     * Methode actionPerformed - Ecoute activation mode Random
     *
     * @param  e   ActionEvent
     * @return     void
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // Code
        controler.setEnabledTileButtons();
    }
}
