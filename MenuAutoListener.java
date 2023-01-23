import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * classe MenuAutoListener.
 *
 * @author William Mesnil
 * @version Version 6
 */
public class MenuAutoListener implements ActionListener
{
    // variables d'instance - Le Controler
    //On ecoute bouton Connect
    private CityControler controler;

    /**
     * Constructeur d'objets de classe MenuAutoListener
     */
    public MenuAutoListener(CityControler controler)
    {
        // initialisation des variables d'instance
        this.controler = controler;
    }

    /**
     * Methode actionPerformed - Ecouter et Aller MAJ Ville
     *
     * @param  e   ActionEvent
     * @return     void
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // Code
        controler.setAutoConnexion();
    }
}
