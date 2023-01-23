import java.awt.Point;

/**
 * classe RoadTile.
 *
 * @author William Mesnil Anis Menaa
 * @version Version 6
 */
public class RoadTile
{
    //Une Route est definie par sa Position et a des Connexions
    //La taille de chaque Cellule de la Grille etant identique
    //TILE_WIDTH et TILE_HEIGHT sont directement stockes dans City
    
    //Representation des Routes
    //O________________
    //|    |  |  |    |
    //|    |CU|  |    |
    //|____|__|__|____|
    //|    |     | CR |
    //|____|  C  |____|
    //| CL |     |    |
    //|____|_____|____|
    //|    |  |  |    |
    //|    |  |CD|    |
    //|____|__|__|____|
    //O : gridPosition
    //CL : Circulation Left
    //CU : Circulation Up
    //CR : Circulation Right
    //CD : Circulation Down
    
    // variables d'instance - Position et Voies/Connexions de Route
    private final Point gridPosition;
    private Circulation left;
    private Circulation up;
    private Circulation right;
    private Circulation down;
    
    /**
     * Constructeur d'objets Secondaire de classe RoadTile
     */
    public RoadTile(int gridX, int gridY, Circulation left, Circulation up, Circulation right, Circulation down)
    {
        // initialisation des variables d'instance
        this(new Point(gridX,gridY), left, up, right, down);
    }
    
    /**
     * Constructeur d'objets Secondaire de classe RoadTile
     */
    public RoadTile(int gridX, int gridY, Circulation[] connexions)
    {
        // initialisation des variables d'instance
        this(new Point(gridX,gridY), connexions[0], connexions[1], connexions[2], connexions[3]);
    }
    
    /**
     * Constructeur d'objets Secondaire de classe RoadTile
     */
    public RoadTile(int gridX, int gridY, Circulation[] connexionsHorizontal, Circulation[] connexionsVertical)
    {
        // initialisation des variables d'instance
        this(new Point(gridX,gridY), connexionsHorizontal[0], connexionsVertical[0], connexionsHorizontal[1], connexionsVertical[1]);
    }
    
    /**
     * Constructeur d'objets Secondaire de classe RoadTile
     */
    public RoadTile(Point gridPosition, Circulation[] connexions)
    {
        // initialisation des variables d'instance
        this(gridPosition, connexions[0], connexions[1], connexions[2], connexions[3]);
    }
    
    /**
     * Constructeur d'objets Secondaire de classe RoadTile
     */
    public RoadTile(Point gridPosition, Circulation[] connexionsHorizontal, Circulation[] connexionsVertical)
    {
        // initialisation des variables d'instance
        this(gridPosition, connexionsHorizontal[0], connexionsVertical[0], connexionsHorizontal[1], connexionsVertical[1]);
    }
    
    /**
     * Constructeur d'objets Principal de classe RoadTile
     */
    public RoadTile(Point gridPosition, Circulation left, Circulation up, Circulation right, Circulation down)
    {
        // initialisation des variables d'instance
        this.gridPosition = new Point(gridPosition);
        this.left = left;
        this.up = up;
        this.right = right;
        this.down = down;
    }

    /**
     * Methode getGridPosition - Recuperer la Position de Tile dans Grille
     *
     * @return     La Position de Tile dans Grille
     */
    public Point getGridPosition()
    {
        // Code
        return this.gridPosition;
    }
    
    /**
     * Methode getGridX - Recuperer la Position X de Tile dans Grille
     *
     * @return     La Position X de Tile dans Grille
     */
    public int getGridX()
    {
        // Code
        return (int)this.gridPosition.getX();
    }
    
    /**
     * Methode getGridY - Recuperer la Position Y de Tile dans Grille
     *
     * @return     La Position Y de Tile dans Grille
     */
    public int getGridY()
    {
        // Code
        return (int)this.gridPosition.getY();
    }
    
    /**
     * Methode getCirculationLeft - Recuperer Circulation vers Gauche Route
     *
     * @return     La Circulation Gauche
     */
    public Circulation getCirculationLeft()
    {
        // Code
        return this.left;
    }
    
    /**
     * Methode getCirculationUp - Recuperer Circulation vers Haut Route
     *
     * @return     La Circulation Haut
     */
    public Circulation getCirculationUp()
    {
        // Code
        return this.up;
    }
    
    /**
     * Methode getCirculationRight - Recuperer Circulation vers Droite Route
     *
     * @return     La Circulation Droite
     */
    public Circulation getCirculationRight()
    {
        // Code
        return this.right;
    }
    
    /**
     * Methode getCirculationDown - Recuperer Circulation vers Bas Route
     *
     * @return     La Circulation Bas
     */
    public Circulation getCirculationDown()
    {
        // Code
        return this.down;
    }
    
    /**
     * Methode getConnexionsHorizontal - Recuperer Circulation vers Gauche et Droite Route
     *
     * @return     La Circulation Gauche et Droite
     */
    public Circulation[] getConnexionsHorizontal()
    {
        // Code
        Circulation[] connexionsHorizontal = new Circulation[2];
        connexionsHorizontal[0] = this.left;
        connexionsHorizontal[1] = this.right;
        return connexionsHorizontal;
    }
    
    /**
     * Methode getConnexionsVertical - Recuperer Circulation vers Haut et Bas Route
     *
     * @return     La Circulation Haut et Bas
     */
    public Circulation[] getConnexionsVertical()
    {
        // Code
        Circulation[] connexionsVertical = new Circulation[2];
        connexionsVertical[0] = this.up;
        connexionsVertical[1] = this.down;
        return connexionsVertical;
    }
    
    /**
     * Methode getConnexions - Recuperer Circulation vers Voisins Route
     *
     * @return     La Circulation Vers Voisins
     */
    public Circulation[] getConnexions()
    {
        // Code
        Circulation[] connexions = new Circulation[4];
        connexions[0] = this.left;
        connexions[1] = this.up;
        connexions[2] = this.right;
        connexions[3] = this.down;
        return connexions;
    }
    
    /**
     * Methode getConnexionsCount - Recuperer nombre de voies de Circulations vers Voisins Route
     *
     * @return     Le nombre de voies de Circulation Vers Voisins
     */
    public int getConnexionsCount()
    {
        // Code
        //Get Total Number of Connexions
        int count = 0;
        if((this.left!=Circulation.NO_WAY) && (this.left!=null)){
            count++;
        }
        if((this.up!=Circulation.NO_WAY) && (this.up!=null)){
            count++;
        }
        if((this.right!=Circulation.NO_WAY) && (this.right!=null)){
            count++;
        }
        if((this.down!=Circulation.NO_WAY) && !(this.down!=null)){
            count++;
        }
        return count;
    }
    
    /**
     * Methode setCirculationLeft - Redefinir Circulation vers Gauche Route
     *
     * @param  left La Circulation vers la Gauche
     * @return      void
     */
    public void setCirculationLeft(Circulation left)
    {
        // Code
        this.left = left;
    }
    
    /**
     * Methode setCirculationUp - Redefinir Circulation vers Haut Route
     *
     * @param  up  La Circulation vers le Haut
     * @return     void
     */
    public void setCirculationUp(Circulation up)
    {
        // Code
        this.up = up;
    }
    
    /**
     * Methode setCirculationRight - Redefinir Circulation vers Droite Route
     *
     * @param  right La Circulation vers la Droite
     * @return       void
     */
    public void setCirculationRight(Circulation right)
    {
        // Code
        this.right = right;
    }
    
    /**
     * Methode setCirculationDown - Redefinir Circulation vers Bas Route
     *
     * @param  down La Circulation vers le Bas
     * @return      void
     */
    public void setCirculationDown(Circulation down)
    {
        // Code
        this.down = down;
    }
    
    /**
     * Methode isEquals - Dit si Tile est egale a Objet
     *
     * @return     True si Tile egale Objet
     */
    public boolean isEquals(Object o)
    {
        // Code
        //Avoid direct Override of Object.equals(Object o) Method
        //Test equality on data basis to try and preserve the HashCode property
        //Test if o is null
        if(o == null){
            return false;
        }
        //Test if o is RoadTile
        if(o.getClass() != this.getClass()){
            return false;
        }
        
        RoadTile otherRoadTile = (RoadTile)o;
        //Test if Position in City are Distinct
        if(this.gridPosition != otherRoadTile.getGridPosition()){
            return false;
        }
        
        return true;
    }
}
