import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Classe CityMouseListener.
 *
 * @author William Mesnil Anis Meena
 * @version Version 6
 */
public class CityMouseListener extends MouseAdapter
{
    // variables d'instance
    private CityControler controler;
    private CityInterface city;

    /**
     * Constructeur d'objets de classe CityMouseListener
     */
    public CityMouseListener(CityControler controler)
    {
        // initialisation des variables d'instance
        this.controler=controler;
        this.city=this.controler.getCity();
    }
    
    /**
     * Méthode mouseClicked
     *
     * @param  e   le MouseEvent
     * @return     Affiche Tile
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        // Code
        //e.getButton() renvoie 1 pour clic gauche et 3 pour click droit
        int CLIC_ZONE_X=-1;
        int CLIC_ZONE_Y=-1;
        for(int i=0;i<this.city.getGridWidth();i++){
            if(((this.city.getTileWidth()*i)<=e.getX())&&(e.getX()<=(this.city.getTileWidth()*(i+1)))){
                CLIC_ZONE_X=i;
                break;
            }
        }
        for(int j=0;j<this.city.getGridHeight();j++){
            if(((this.city.getTileHeight()*j)<=e.getY())&&(e.getY()<=(this.city.getTileHeight()*(j+1)))){
                CLIC_ZONE_Y=j;
                break;
            }
        }
        System.out.println("Clic ["+CLIC_ZONE_X+","+CLIC_ZONE_Y+"]");
        if(e.getButton()==1){
            this.controler.addTile(CLIC_ZONE_X,CLIC_ZONE_Y);
        }else if(e.getButton()==3){
            this.controler.removeTile(CLIC_ZONE_X,CLIC_ZONE_Y);
        }else{
            System.out.println("Aucune action associee a ce bouton");
        }
    }
}
