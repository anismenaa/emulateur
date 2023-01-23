import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * classe MenuResetListener.
 *
 * @author Anis Meena
 * @version Version 5
 */
public class MenuResetListener implements ActionListener
{
    // variables d'instance - Le Controler
    private CityControler controler;

    /**
     * Constructeur d'objets de classe MenuResetListener
     */
    public MenuResetListener(CityControler controler)
    {
        // initialisation des variables d'instance
        this.controler=controler;
    }
    /**
     * Methode actionPerformed - Appel la Methode reset
     *
     * @param  e   ActionEvent
     * @return     void
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // Code
        controler.reset();
    }
}
