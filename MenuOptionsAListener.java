import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import java.util.List;

/**
 * classe MenuOptionsAListener.
 *
 * @author Anis Meena
 * @version Version 6
 */
public class MenuOptionsAListener implements ActionListener
{
    // variables d'instance - Le Controler
    //On ecoute les changements de parametres pour aider Menu a dessiner preview
    private CityControler controler;

    /**
     * Constructeur d'objets de classe MenuOptionsAListener
     */
    public MenuOptionsAListener(CityControler controler)
    {
        // initialisation des variables d'instance
        this.controler = controler;
    }

    /**
     * Methode actionPerformed - Ecouter modif et redessiner preview
     *
     * @param  e   ActionEvent
     * @return     void
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // Code
        List<JComponent> options = this.controler.getTileOptionsComponents();
        for(JComponent option : options){
            System.out.println(option.getClass().getName());
        }
    }
}
