import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.List;

/**
 * classe CityControler.
 *
 * @author William Mesnil Anis Menaa
 * @version Version 5
 */
public class CityControler
{
    // variables d'instance - La JFrame et les JComponents a gerer avec Donnees
    private JFrame frame;
    private CityViewInterface view;
    private MenuInterface menu;
    private CityInterface city;
    
    private MenuQuitListener menuQuitListener;
    private MenuResetListener menuResetListener;
    private MenuOptionsAListener menuOptionsAListener;
    private MenuOptionsCListener menuOptionsCListener;
    private MenuRandomListener menuRandomListener;
    private MenuAutoListener menuAutoListener;
    private CityMouseListener cityMouseListener;
    
    /**
     * Constructeur d'objets de classe CityControler
     */
    public CityControler(JFrame frame, CityViewInterface view, MenuInterface menu, CityInterface city)
    {
        // initialisation des variables d'instance
        this.frame = frame;
        this.view = view;
        this.menu = menu;
        this.city = city;
        
        this.cityMouseListener = new CityMouseListener(this);
        this.menuQuitListener = new MenuQuitListener(this);
        this.menuResetListener = new MenuResetListener(this);
        this.menuOptionsAListener = new MenuOptionsAListener(this);
        this.menuOptionsCListener = new MenuOptionsCListener(this);
        this.menuRandomListener = new MenuRandomListener(this);
        this.menuAutoListener = new MenuAutoListener(this);
        
        this.setFrameCloseListener();
        this.setMenuQuitListener();
        this.setMenuResetListener();
        if(this.menu.supportTileOptionsActionListener()){
            this.setMenuOptionsAListener();
        }else if(this.menu.supportTileOptionsChangeListener()){
            this.setMenuOptionsCListener();
        }
        if(this.menu.hasAutoOption()){
            this.setMenuAutoListener();
        }
        if(this.menu.hasRandomOption()){
            this.setMenuRandomListener();
        }
        this.setViewMouseListener();
    }
    
    /**
     * Methode step - Avance la Simulation
     *
     * @return    void
     */
    public void step()
    {
        this.city.step();
        this.view.repaint();
    }
    
    /**
     * Methode addTile - Appel addTile avec les coordonnees du Clic et Options
     *
     * @param  clicX La Position X du Clic dans Grille
     * @param  clicY La Position X du Clic dans Grille
     * @return       void
     */
    public void addTile(int clicX, int clicY)
    {
        if(clicX<0 || clicX>=this.city.getGridWidth()){
            System.out.println("Coordonnee X Invalide");
            return;
        }
        if(clicY<0 || clicY>=this.city.getGridHeight()){
            System.out.println("Coordonnee Y Invalide");
            return;
        }
        Circulation[] connexions = this.menu.getTileOptionsValues();
        if(this.city.addTile(clicX,clicY,connexions)){
            this.view.repaint();
        }
    }
    
    /**
     * Methode removeTile - Appel removeTile avec les coordonnees du Clic
     *
     * @param  clicX La Position X du Clic dans Grille
     * @param  clicY La Position Y du Clic dans Grille
     * @return       void
     */
    public void removeTile(int clicX, int clicY)
    {
        if(clicX<0 || clicX>=this.city.getGridWidth()){
            System.out.println("Coordonnee X Invalide");
            return;
        }
        if(clicY<0 || clicY>=this.city.getGridHeight()){
            System.out.println("Coordonnee Y Invalide");
            return;
        }
        if(this.city.removeTile(clicX,clicY)){
            this.view.repaint();
        }
    }
    
    /**
     * Methode getCity - City
     *
     * @return     La Ville
     */
    public CityInterface getCity()
    {
        return this.city;
    }
    
    /**
     * Methode setFrameCloseListener - Fermeture
     *
     * @return     void
     */
    public void setFrameCloseListener()
    {
        // Code
        this.frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
              System.out.println("Fermeture");
              city.desactive();
              frame.dispose();
            }
        }
        );
    }
    
    /**
     * Methode setViewMouseListener - Ecoute la View
     *
     * @return     void
     */
    public void setViewMouseListener()
    {
        // Code
        this.view.setViewMouseListener(this.cityMouseListener);
    }
    
    /**
     * Methode setMenuQuitListener - Ecoute le Bouton quit
     *
     * @return     void
     */
    public void setMenuQuitListener()
    {
        // Code
        this.menu.setMenuQuitListener(this.menuQuitListener);
    }
    
    /**
     * Methode setMenuResetListener - Ecoute le Bouton reset
     *
     * @return     void
     */
    public void setMenuResetListener()
    {
        // Code
        this.menu.setMenuResetListener(this.menuResetListener);
    }
    
    /**
     * Methode setMenuAutoListener - Ecoute Option Auto
     *
     * @return     void
     */
    public void setMenuAutoListener()
    {
        // Code
        this.menu.setMenuAutoListener(this.menuAutoListener);
    }
    
    /**
     * Methode setMenuOptionsListener - Ecoute les Options de Tile avec ActionListener
     *
     * @return     void
     */
    public void setMenuOptionsAListener()
    {
        // Code
        this.menu.setMenuOptionsListener(this.menuOptionsAListener);
    }
    
    /**
     * Methode setMenuOptionsListener - Ecoute les Options de Tile avec ChangeListener
     *
     * @return     void
     */
    public void setMenuOptionsCListener()
    {
        // Code
        this.menu.setMenuOptionsListener(this.menuOptionsCListener);
    }
    
    /**
     * Methode setMenuRandomOptionListener - Ecoute Option Random
     *
     * @return     void
     */
    public void setMenuRandomListener()
    {
        // Code
        this.menu.setMenuRandomListener(this.menuRandomListener);
    }
    
    /**
     * Methode setEnabledTileButtons - Activer ou Desactiver Boutons
     *
     * @return     void
     */
    public void setEnabledTileButtons()
    {
        boolean randomOptionValue = this.menu.getRandomOptionValue();
        this.menu.setEnabledTileOptions(!randomOptionValue);
    }
    
    /**
     * Methode setAutoConnexion - Activer ou Desactiver Auto
     *
     * @return     void
     */
    public void setAutoConnexion()
    {
        this.city.setAuto(this.menu.getAutoOptionValue());
    }
    
    /**
     * Methode isActive - Dit si Simulation est Active ou Desactive
     *
     * @return     active
     */
    public boolean isActive()
    {
        return this.city.isActive();
    }
    
    /**
     * Methode getTileOptionsComponents - Recuperer les Composants qui representent les Options Circulations
     * 
     * @return     La Liste des Composants
     */
    public List<JComponent> getTileOptionsComponents(){
        return this.menu.getTileOptionsComponents();
    }
    
    /**
     * Methode reset - Reset la Grille
     *
     * @return     void
     */
    public void reset()
    {
        // Code
        Object[] options = {"Confirmer","Retour"};
        int confirm = JOptionPane.showOptionDialog(null,"Confirmer Reset Grille?","Reset",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,options,options[1]);
        if(confirm == JOptionPane.YES_OPTION){
            this.city.reset();
            this.view.repaint();
        }
    }
    
    /**
     * Methode quit - Desactive Main
     *
     * @return     void
     */
    public void quit()
    {
        // Code
        Object[] options = {"Confirmer","Retour"};
        int confirm = JOptionPane.showOptionDialog(null,"Confirmer Fermeture?","Quit",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if(confirm == JOptionPane.YES_OPTION){
            this.city.desactive();
            this.frame.dispose();
            this.frame=null;
            this.view=null;
            this.menu=null;
        }
    }
}
