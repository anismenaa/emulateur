import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * classe App.
 *
 * @author William Mesnil Anis Menaa
 * @version Version 6
 */
public class App
{
    // variables d'instance
    //Avancement
    //Bouton Quitter : OK
    //Bouton Auto : OK
    //Fonction Auto : Presque Correcte
    //Bouton Generer Voiture : PAS IMPLEMENTE
    //Bouton Save : A FAIRE
    //Bouton Load : A FAIRE
    //Appercu : NE FONCTIONNE PAS
    //Bugs Corriges
    //Erreur Booleen
    //Erreur dans gestion de Next Position (vehicule collision avec prochaine position et appel de fonction errone)
    //Erreur dans gestion de Next Direction (vehicule peut demi tour, ce qui est parfois seuls solution pour impasse mais pas reste routes
    //Forcer demi tour si impasse avec ajout de booleen allowsTurnback dans une Tile
    public final static int TILE_WIDTH = 100;//Taille_X Route
    public final static int TILE_HEIGHT = 100;//Taille_Y Route
    public final static int GRID_WIDTH = 10;//Nombre de colonne dand Ville
    public final static int GRID_HEIGHT = 10;//Nombre de ligne dans Ville
    public final static int MENU_WIDTH = 400;//Taille_X Menu
    public final static int MENU_HEIGHT = Math.max(1000,GRID_HEIGHT*TILE_HEIGHT);;//Taille_Y Menu
    public final static int FRAME_WIDTH = MENU_WIDTH+(GRID_WIDTH*TILE_WIDTH);//Taille_X Frame est Ville avec ajout espace lateral Menu
    public final static int FRAME_HEIGHT = Math.max(MENU_HEIGHT,GRID_HEIGHT*TILE_HEIGHT);//Taille_Y Frame
    public final static int SIMULATION_SPEED = 2;//Vitesse Simulation en secondes
    //System.out.println();
    /**
     * Main
     */
    public static void main(String[] args)
    {
        // initialisation des variables d'instance
        CityInterface city = new City(GRID_WIDTH,GRID_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
        CityViewInterface view = new CityView(city,MENU_WIDTH,0);
        MenuInterface menu = new Menu(MENU_WIDTH,MENU_HEIGHT);
        JFrame frame = new JFrame("Emulateur Traffic");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        //frame.setResizable(false);
        frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.add((Menu)menu);
        frame.add((CityView)view);
        frame.setVisible(true);
        CityControler controler = new CityControler(frame,view,menu,city);
        while(controler.isActive()){
            controler.step();
            try
            {
                Thread.sleep(1000*SIMULATION_SPEED);
            }
            catch (java.lang.InterruptedException ie)
            {
                ie.printStackTrace();
            }
        }
    }
}
