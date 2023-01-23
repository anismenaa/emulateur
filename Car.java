import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * classe Car.
 *
 * @author William Mesnil Anis Menaa
 * @version Version 6
 */
public class Car
{
    //La Classe AffineTransform avec une Shape differente est une piste plus complexe
    //Ici on represente la Voiture par un simple Rectangle, dont on gere Origine et Dimensions
    //La Dimension varie selon un MIN et MAX definis dans City
    //Pour rester Proportionnel au Graphique
    
    //Representation des Voitures
    //O__________
    //|         |Hei
    //|_________|ght
    //   Width
    //O : carLeftRear
    
    // variables d'instance - La position, dimension, vitesse et direction de la Voiture
    private Point gridPosition; //Position X et Y dans la Grille
    private Point carLeftRear; //Position X et Y en pixel du Feu Arriere de Voiture dans le JPanel
    private final Dimension carDimensions; //Largeur et Hauteur de Voiture
    private Dimension carOrientedDimensions; //Largeur et Hauteur de Voiture dans JPanel en fonction de Orientation
    private int speed;
    private Direction direction; //La Direction de la Voiture
    private boolean readyToTurn; //Track if Car already changed direction on current tile
    private int actionCounter;
    private int wait;
    private boolean drive;
    //private Point wayPoint;

    /**
     * Constructeur d'objets Secondaire de classe Car
     */
    public Car(int gridX, int gridY, int carX, int carY, int carWidth, int carHeight, int speed, Direction direction)
    {
        // initialisation des variables d'instance
        this(new Point(gridX,gridY), new Point(carX,carY), new Dimension(carWidth,carHeight), speed, direction);
    }
    
    /**
     * Constructeur d'objets Copie de classe Car
     */
    public Car(Car otherCar)
    {
        // initialisation des variables d'instance
        this(otherCar.getGridPosition(), otherCar.getCarLeftRear(), otherCar.getCarDimensions(), otherCar.getSpeed(), otherCar.getDirection());
    }
    
    /**
     * Constructeur d'objets Principal de classe Car
     */
    public Car(Point gridPosition, Point carLeftRear, Dimension carDimensions, int speed, Direction direction)
    {
        // initialisation des variables d'instance
        this.gridPosition = new Point(gridPosition);
        this.carLeftRear = new Point(carLeftRear);
        this.carDimensions = new Dimension(carDimensions);
        this.speed = speed;
        this.direction = direction;
        this.computeOrientedDimensions();
        this.readyToTurn = true;
        this.actionCounter = 0;
        this.wait = 0;
        this.drive = true;
    }

    /**
     * Methode getGridPosition - Recuperer la Position de Voiture dans Grille
     *
     * @return     La Position de Voiture dans Grille
     */
    public Point getGridPosition()
    {
        // Code
        return this.gridPosition;
    }
    
    /**
     * Methode getGridX - Recuperer la Position X de Voiture dans Grille
     *
     * @return     La Position X de Voiture dans Grille
     */
    public int getGridX()
    {
        // Code
        return (int)this.gridPosition.getX();
    }
    
    /**
     * Methode getGridY - Recuperer la Position Y de Voiture dans Grille
     *
     * @return     La Position Y de Voiture dans Grille
     */
    public int getGridY()
    {
        // Code
        return (int)this.gridPosition.getY();
    }
    
    /**
     * Methode getCarLeftRear - Recuperer la Position de Voiture dans JPanel
     *
     * @return     La Position de Voiture dans JPanel
     */
    public Point getCarLeftRear()
    {
        // Code
        return this.carLeftRear;
    }
    
    /**
     * Methode getCarX - Recuperer la Position X de Voiture dans JPanel
     *
     * @return     La Position X de Voiture dans JPanel
     */
    public int getCarX()
    {
        // Code
        return (int)this.carLeftRear.getX();
    }
    
    /**
     * Methode getCarY - Recuperer la Position Y de Voiture dans JPanel
     *
     * @return     La Position Y de Voiture dans JPanel
     */
    public int getCarY()
    {
        // Code
        return (int)this.carLeftRear.getY();
    }
    
    /**
     * Methode getCarDimesnions - Recuperer la Dimension de Voiture
     *
     * @return     La Dimension de Voiture
     */
    public Dimension getCarDimensions()
    {
        // Code
        return this.carDimensions;
    }
    
    /**
     * Methode getCarWidth - Recuperer la Largeur de Voiture
     *
     * @return     La Largeur de Voiture
     */
    public int getCarWidth()
    {
        // Code
        return (int)this.carDimensions.getWidth();
    }
    
    /**
     * Methode getCarHeight - Recuperer la Hauteur de Voiture
     *
     * @return     La Hauteur de Voiture
     */
    public int getCarHeight()
    {
        // Code
        return (int)this.carDimensions.getHeight();
    }
    
    /**
     * Methode getCarOrientedDimensions - Recuperer la Dimension de Voiture selon Orientation
     *
     * @return     La Largeur Oriente de Voiture
     */
    public Dimension getCarOrientedDimensions()
    {
        // Code
        return this.carOrientedDimensions;
    }
    
    /**
     * Methode getCarOrientedWidth - Recuperer la Largeur de Voiture selon Orientation
     *
     * @return     La Dimension Oriente de Voiture
     */
    public int getCarOrientedWidth()
    {
        // Code
        return (int)this.carOrientedDimensions.getWidth();
    }
    
    /**
     * Methode getCarOrientedHeight - Recuperer la Hauteur de Voiture selon Orientation
     *
     * @return     La Hauteur Oriente de Voiture
     */
    public int getCarOrientedHeight()
    {
        // Code
        return (int)this.carOrientedDimensions.getHeight();
    }
    
    /**
     * Methode computeOientedDimensions - Recuperer la Dimension de Voiture selon Orientation dans JPanel
     *
     * @return     void
     */
    public void computeOrientedDimensions()
    {
        // Code
        //Car on Screen have to arrange and swap the Dimensions depending on the Orientation
        //Car also must make them negative if needed as tracking Point is meant to remain LeftRear
        //Any error here can mess Methods for the Graphic and Collision Detection
        int carOrientedWidth=0;
        int carOrientedHeight=0;
        //If Car is Going Left, Dimensions must be negated
        if(this.direction == Direction.LEFT){
            carOrientedWidth = -((int)this.carDimensions.getWidth());
            carOrientedHeight = -((int)this.carDimensions.getHeight());
        }
        //If Car is Going Up, Dimensions must be swapped and OrientedHeight negated
        else if(this.direction == Direction.UP){
            carOrientedWidth = (int)this.carDimensions.getHeight();
            carOrientedHeight = -((int)this.carDimensions.getWidth());
        }
        //If Car is Going Right, Dimensions are already Graphically Correct
        else if(this.direction == Direction.RIGHT){
            carOrientedWidth = (int)this.carDimensions.getWidth();
            carOrientedHeight = (int)this.carDimensions.getHeight();
        }
        //If Car is Going Down, Dimensions must be swapped and OrientedWidth negated
        else if(this.direction == Direction.DOWN){
            carOrientedWidth = -((int)this.carDimensions.getHeight());
            carOrientedHeight = (int)this.carDimensions.getWidth();
        }
        //If Error Here, stop Car
        else{
            this.stop();
            return;
        }
        this.carOrientedDimensions = new Dimension(carOrientedWidth,carOrientedHeight);
    }
    
    /**
     * Methode getSpeed - Recuperer la Vitesse de Voiture dans JPanel
     *
     * @return     La Vitesse de Voiture dans JPanel
     */
    public int getSpeed()
    {
        // Code
        return this.speed;
    }
    
    /**
     * Methode getDirection - Recuperer la Direction de Voiture dans JPanel
     *
     * @return     La Direction de Voiture dans JPanel
     */
    public Direction getDirection()
    {
        // Code
        return this.direction;
    }
    
    /**
     * Methode getDirectionX - Recuperer la Composante X de Direction de Voiture dans JPanel
     *
     * @return     La Composante X de Direction de Voiture dans JPanel
     */
    public int getDirectionX()
    {
        // Code
        return this.direction.getX();
    }
    
    /**
     * Methode getDirectionY - Recuperer la Composante Y de Direction de Voiture dans JPanel
     *
     * @return     La Composante Y de Direction de Voiture dans JPanel
     */
    public int getDirectionY()
    {
        // Code
        return this.direction.getY();
    }
    
    /**
     * Methode getActionCounter - Recuperer le nombre Actions de Voiture dans JPanel
     *
     * @return     Le nombre Action de Voiture dans JPanel
     */
    public int getActionCounter()
    {
        // Code
        return this.actionCounter;
    }
    
    /**
     * Methode isReadyToTurn - Dit si Voiture Prete Tourner
     *
     * @return     True si Prete
     */
    public boolean isReadyToTurn()
    {
        // Code
        return this.readyToTurn;
    }
    
    /**
     * Methode isWaiting - Dit si Voiture en attente
     *
     * @return     True si Voiture en attente
     */
    public boolean isWaiting()
    {
        // Code
        if(this.wait>0){
            return true;
        }
        return false;
    }
    
    /**
     * Methode waitFor - Definir Attente
     *
     * @param  step Le nombre etapes a attendre
     * @return      void
     */
    public void waitFor(int stepNb)
    {
        // Code
        this.wait = stepNb;
    }
    
    /**
     * Methode doWait - Attendre
     *
     * @return     void
     */
    public void doWait()
    {
        // Code
        this.wait--;
        this.actionCounter++;
    }
    
    /**
     * Methode canDrive - Dit si Voiture peut Rouler
     *
     * @return     True si Voiture peut Rouler
     */
    public boolean canDrive()
    {
        // Code
        return this.drive;
    }
    
    /**
     * Methode stop - Arrete la Voiture
     *
     * @return     void
     */
    public void stop()
    {
        // Code
        this.drive = false;
        System.out.println("Arret de Voiture : "+this);
        System.out.println("Nombre Actions : "+this.actionCounter);
    }
    
    /**
     * Methode setGridPosition - Definit Position Voiture dans Grille
     *
     * @return     void
     */
    public void setGridPosition(int gridX, int gridY)
    {
        // Code
        if(!this.gridPosition.equals(new Point(gridX,gridY))){
            this.gridPosition.setLocation(gridX,gridY);
            this.readyToTurn = true;
        }
    }
    
    /**
     * Methode setGridPosition - Definit Position Voiture dans Grille
     *
     * @return     void
     */
    public void setGridPosition(Point gridPosition)
    {
        // Code
        if(!this.gridPosition.equals(gridPosition)){
            this.gridPosition.setLocation(gridPosition);
            this.readyToTurn = true;
        }
    }
    
    /**
     * Methode move - Avance la Voiture
     *
     * @return     void
     */
    public void move()
    {
        // Code
        this.carLeftRear.translate(this.speed*this.direction.getX(),this.speed*this.direction.getY());
        this.actionCounter++;
    }
    
    /**
     * Methode moveReverse - Reculer la Voiture
     *
     * @return     void
     */
    public void moveReverse()
    {
        // Code
        this.carLeftRear.translate(-(this.speed*this.direction.getX()),-(this.speed*this.direction.getY()));
        this.actionCounter++;
    }
    
    /**
     * Methode setCarPosition - Definit Position Voiture dans JPanel
     *
     * @return     void
     */
    public void setCarPosition(int carX, int carY)
    {
        // Code
        this.carLeftRear.setLocation(carX,carY);
        this.computeOrientedDimensions();
    }
    
    /**
     * Methode setCarPosition - Definit Position Voiture dans JPanel
     *
     * @return     void
     */
    public void setCarPosition(Point carLeftRear)
    {
        // Code
        this.carLeftRear.setLocation(carLeftRear);
        this.computeOrientedDimensions();
    }
    
    /**
     * Methode setDirection - Definit Direction Voiture dans JPanel
     *
     * @return     void
     */
    public void setDirection(Direction direction)
    {
        // Code
        this.direction = direction;
        this.readyToTurn = false;
        this.computeOrientedDimensions();
    }
    
    /**
     * Methode getCarHitbox - Avoir Rectangle representant la Voiture
     *
     * @return     Le Rectangle representant la Voiture
     */
    public Rectangle getCarHitbox()
    {
        // Code
        return new Rectangle(this.carLeftRear,this.carOrientedDimensions);
    }
    
    /**
     * Methode isEquals - Dit si Voiture est egale a Objet
     *
     * @return     True si Voiture egale Objet
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
        //Test if o is Car
        if(o.getClass() != this.getClass()){
            return false;
        }
        
        Car otherCar = (Car)o;
        //Test if data are Distinct
        if((this.carLeftRear != otherCar.getCarLeftRear()) || (this.carOrientedDimensions != otherCar.getCarOrientedDimensions())){
            return false;
        }
        
        return true;
    }
}
