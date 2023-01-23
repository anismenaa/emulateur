import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;

import javax.swing.JComponent;

import java.util.List;

/**
 * classe MenuOptionsCListener.
 *
 * @author William Mesnil
 * @version Version 6
 */
public class MenuOptionsCListener implements ChangeListener
{
    // variables d'instance - Le Controler
    //On ecoute les changements de parametres pour aider Menu a dessiner preview
    private CityControler controler;
    
    /**
     * Constructeur d'objets de classe MenuOptionsCListener
     */
    public MenuOptionsCListener(CityControler controler)
    {
        // initialisation des variables d'instance
        this.controler = controler;
    }
    
    /**
     * Méthode stateChanged
     *
     * @param  e   Evenement
     * @return     Vide
     */
    @Override
    public void stateChanged(ChangeEvent e)
    {
        // Code
        JSlider slider = (JSlider)e.getSource();
        //When User set Slider on Stable Value
        if(!slider.getValueIsAdjusting()){
            //Left Option
            if(slider.getName() == "L"){
                System.out.print("Valeur Gauche : ");
            }
            //Up Option
            else if(slider.getName() == "U"){
                System.out.print("Valeur Haut : ");
            }
            //Right Option
            else if(slider.getName() == "R"){
                System.out.print("Valeur Droite : ");
            }
            //Down Option
            else if(slider.getName() == "D"){
                System.out.print("Valeur Bas : ");
            }
            System.out.println(slider.getValue());
        }
    }
    
    /**
     * Méthode stateChanged
     *
     * @param  e   Evenement
     * @return     Vide
     *
     *@Override
     *public void stateChanged(ChangeEvent e)
     *{
     *    // Code
     *    JSlider slider = (JSlider)e.getSource();
     *    if(!slider.getValueIsAdjusting()){
     *        List<JComponent> options = this.controler.getTileOptionsComponents();
     *        for(JComponent option : options){
     *            if(slider.equals(option)){
     *                System.out.println("Valeur Changee");
     *            }
     *        }
     *    }
     *}
     */
}
