import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * classe City.
 *
 * @author William Mesnil Anis Menaa
 * @version Version 6
 */
public class City implements CityInterface
{
    //City contient Modele des Donnees de Simulation
    //Avec Taille Grille et Taille Tile, City determine un ensemble de Parametres
    //Parametres sont voulut Proportionnels pour tout Couple de Valeurs Grille/Tile
    //Calcul base sur le Modele des Valeurs 10X10 Cases et 100X100 Pixels
    //Suppose cas utilisation Test Satisfaisant
    
    //Representation de Grille
    //La Grille[i][j] avec 0<i<GRID_WIDTH et 0<j<GRID_HEIGHT et X=GRID_WIDTH Y=GRID_HEIGHT
    //[0,0][i,0][X,0]
    //[0,j][i,j][X,j]
    //[0,Y][i,Y][X,Y]
    
    // variables d'instance - La Grille de configuration Routes, La Liste des Voitures et Parametres
    //Data
    private RoadTile[][] cityGrid;
    private List<Car> vehicleList;
    private final int GRID_WIDTH;
    private final int GRID_HEIGHT;
    private final int TILE_WIDTH;
    private final int TILE_HEIGHT;
    private final int TILE_LANES_WIDTH;
    private final int TILE_LANES_HEIGHT;
    private final int TILE_CENTER_WIDTH;
    private final int TILE_CENTER_HEIGHT;
    private final int DIVIDE_TILE_IN = 3;
    private final int VEHICLE_MAX_WIDTH; //Largeur Max Vehicule
    private final int VEHICLE_MAX_HEIGHT; //Hauteur Max Vehicule
    private final int VEHICLE_MIN_WIDTH; //Largeur Max Vehicule
    private final int VEHICLE_MIN_HEIGHT; //Hauteur Max Vehicule
    private final int VEHICLE_MIN_SPEED; //Vitesse Min Vehicule
    private final int VEHICLE_MAX_SPEED; //Vitesse Max Vehicule
    private boolean autoMode; //Selon la valeur, force la Tile a se Connecter a ses Voisins
    private Random random; //Generation de Vehicule
    //Stats
    private int lastGeneration; //Nombre de step sans generation de Vehicule
    private int stepCounter; //Nombre de step
    private int generationsCounter; //Nombre de generation de Vehicule
    private int vehicleActionsCounter; //Nombre Action totales des Vehicules
    private boolean active; //Dit si Simulation est Active

    /**
     * Constructeur d'objets de classe City
     */
    public City(int gridWidth, int gridHeight, int tileWidth, int tileHeight)
    {
        // initialisation des variables d'instance
        //Data
        this.GRID_WIDTH = gridWidth;
        this.GRID_HEIGHT = gridHeight;
        this.TILE_WIDTH = tileWidth;
        this.TILE_HEIGHT = tileHeight;
        this.TILE_LANES_WIDTH = this.getTileLanesWidth();
        this.TILE_LANES_HEIGHT = this.getTileLanesHeight();
        this.TILE_CENTER_WIDTH = this.getTileCenterWidth();
        this.TILE_CENTER_HEIGHT = this.getTileCenterWidth();
        //int vehicleWidth = random.nextInt(VEHICLE_MAX_WIDTH-VEHICLE_MIN_WIDTH)+VEHICLE_MIN_WIDTH
        //int vehicleHeight = random.nextInt(VEHICLE_MAX_HEIGHT-VEHICLE_MIN_HEIGHT)+VEHICLE_MIN_HEIGHT
        //int vehicleSpeed = random.nextInt(VEHICLE_MAX_SPEED-VEHICLE_MIN_SPEED)+VEHICLE_MIN_SPEED
        this.VEHICLE_MIN_WIDTH = this.TILE_LANES_WIDTH/4;
        this.VEHICLE_MIN_HEIGHT = this.TILE_LANES_HEIGHT/4;
        this.VEHICLE_MAX_WIDTH = this.TILE_LANES_WIDTH/3;
        this.VEHICLE_MAX_HEIGHT = this.TILE_LANES_HEIGHT/3;
        this.VEHICLE_MIN_SPEED = Math.max(this.VEHICLE_MIN_WIDTH,this.VEHICLE_MIN_HEIGHT);
        this.VEHICLE_MAX_SPEED = Math.min(this.VEHICLE_MAX_WIDTH,this.VEHICLE_MAX_HEIGHT);
        this.cityGrid = new RoadTile[this.GRID_WIDTH][this.GRID_HEIGHT];
        this.vehicleList = new ArrayList<Car>();
        this.autoMode = false;
        this.random = new Random();
        //Stats
        this.lastGeneration = 0;
        this.stepCounter = 0;
        this.generationsCounter = 0;
        this.vehicleActionsCounter = 0;
        this.active = true;
    }
    /**
     * Methode step - Avancer
     *
     * @return    void
     */
    public void step(){
        //If ten step without Generation, try SafestGeneration
        if(this.lastGeneration > 10){
            //If success, reset counter
            if(this.safestGenerateVehicle()){
                this.generationsCounter++;
                this.lastGeneration = 0;
            }
        }
        //Else, 50% chance to do classic Generation
        else{
            double rolledProb = this.random.nextDouble();
            if(rolledProb < 0.50){
                //If success, reset counter
                if(this.generateVehicle()){
                    this.generationsCounter++;
                    this.lastGeneration = 0;
                }
            }
        }
        this.lastGeneration++;
        
        //For each Car, advance
        carLoop:
        for(Car car : vehicleList){
            //If Car is to be dismissed, continue
            if(!car.canDrive()){
                continue carLoop;
            }
            //If Car is waiting, wait this step and continue
            if(car.isWaiting()){
                car.doWait();
                continue carLoop;
            }
            //If currentTile is null, car is out of the Used Grid and flaged as ready to be removed
            if(this.cityGrid[car.getGridX()][car.getGridY()]==null){
                car.stop();
                continue carLoop;
            }
            //If Simulation has too many cars, reduce computing charge using closeTo
            Car next = new Car(car);
            next.move();
            List<Car> closeTo = this.closeTo(next);
            //Test Collision for Close Car
            for(Car otherCar : closeTo){
                //If Collision, slightly reverse to let potential Collider manever and wait for 1 step
                if(this.isColliding(next,otherCar)){
                    car.moveReverse();
                    car.waitFor(1);
                    continue carLoop;
                }
            }
            car.move();
            this.computeVehicleGridPosition(car);
            //If Car has reach Center, turn in new Direction
            RoadTile currentTile = this.cityGrid[car.getGridX()][car.getGridY()];
            //If Car into Valid Tile in Invalid Position
            if(!this.isValidPosition(car,currentTile)){
                car.stop();
                continue carLoop;
            }
            Rectangle center = this.getTileCenter(currentTile);
            if(CollisionTools.hasReachCenterSideFrom(car.getCarHitbox(),center,car.getDirection())){
                //If no Direction Avaliable, car reached end of road
                if(!this.nextDirection(car)){
                    continue carLoop;
                }
                //Set Car at Center Exit in correct Position
                int newCarX;
                int newCarY;
                //Car go Left, set to Center Left
                if(car.getDirection() == Direction.LEFT){
                    Rectangle leftOut = this.getTileLeftOut(currentTile);
                    newCarX = (int)(leftOut.getX() + leftOut.getWidth());
                    newCarY = (int)(leftOut.getY() + leftOut.getHeight());
                    car.setCarPosition(newCarX,newCarY);
                }
                //Car go Up, set to Center Up
                if(car.getDirection() == Direction.UP){
                    Rectangle upOut = this.getTileUpOut(currentTile);
                    newCarX = (int)upOut.getX();
                    newCarY = (int)(upOut.getY() + upOut.getHeight());
                    car.setCarPosition(newCarX,newCarY);
                }
                //Car go Right, set to Center Right
                if(car.getDirection() == Direction.RIGHT){
                    Rectangle rightOut = this.getTileRightOut(currentTile);
                    newCarX = (int)rightOut.getX();
                    newCarY = (int)rightOut.getY();
                    car.setCarPosition(newCarX,newCarY);
                }
                //Car go Down, set to Center Down
                if(car.getDirection() == Direction.DOWN){
                    Rectangle downOut = this.getTileDownOut(currentTile);
                    newCarX = (int)(downOut.getX() + downOut.getWidth());
                    newCarY = (int)downOut.getY();
                    car.setCarPosition(newCarX,newCarY);
                }
            }
        }
        //Remove Vehicule out of Grid
        this.removeStoppedVehicle();
        this.stepCounter++;
        //Print Stats
        //Check if Zero
        int avgGenerations = 0;
        int avgActions = 0;
        if(this.generationsCounter > 0){
            avgGenerations = this.stepCounter/this.generationsCounter;
        }
        if(this.vehicleActionsCounter > 0){
            avgActions = this.generationsCounter/this.vehicleActionsCounter;
        }
        System.out.println("Moyenne Vehicules Generes chaque etape : "+avgGenerations);
        System.out.println("Moyenne Actions des Vehicules : "+avgActions);
    }
    /**
     * Methode isValidPosition - Dit si Vehicule sortie de Route
     *
     * @param  car  Le Vehicule
     * @param  tile La Tile
     * @return      True si Position est Valide
     */
    public boolean isValidPosition(Car car, RoadTile tile){
        //Test the Opposite of CollisionTools.hasReachCenterFrom()
        //If Tile move in a given Direction and has no reach center, then it is approachin from said Direction
        //Thus Giving us the Lane it is supposed to Drive on
        //Test if car approach Center then use the Direction
        System.out.println("IN FUNCTION");
        System.out.println(car.getDirection());
        if(CollisionTools.isApprochingFrom(car.getCarHitbox(),this.getTileCenter(tile),car.getDirection())){
            if((car.getDirection() == Direction.LEFT)
            &&((tile.getCirculationRight() == Circulation.ONE_WAY_OUT)
            ||(tile.getCirculationRight() == Circulation.TWO_WAY))){
                return true;
            }
            else if((car.getDirection() == Direction.UP)
            &&((tile.getCirculationDown() == Circulation.ONE_WAY_OUT)
            ||(tile.getCirculationDown() == Circulation.TWO_WAY))){
                return true;
            }
            else if((car.getDirection() == Direction.RIGHT)
            &&((tile.getCirculationLeft() == Circulation.ONE_WAY_OUT)
            ||(tile.getCirculationLeft() == Circulation.TWO_WAY))){
                return true;
            }
            else if((car.getDirection() == Direction.DOWN)
            &&((tile.getCirculationUp() == Circulation.ONE_WAY_OUT)
            ||(tile.getCirculationUp() == Circulation.TWO_WAY))){
                return true;
            }
        }else{
            if((car.getDirection() == Direction.LEFT)
            &&((tile.getCirculationLeft() == Circulation.ONE_WAY_OUT)
            ||(tile.getCirculationLeft() == Circulation.TWO_WAY))){
                return true;
            }
            else if((car.getDirection() == Direction.UP)
            &&((tile.getCirculationUp() == Circulation.ONE_WAY_OUT)
            ||(tile.getCirculationUp() == Circulation.TWO_WAY))){
                return true;
            }
            else if((car.getDirection() == Direction.RIGHT)
            &&((tile.getCirculationRight() == Circulation.ONE_WAY_OUT)
            ||(tile.getCirculationRight() == Circulation.TWO_WAY))){
                return true;
            }
            else if((car.getDirection() == Direction.DOWN)
            &&((tile.getCirculationDown() == Circulation.ONE_WAY_OUT)
            ||(tile.getCirculationDown() == Circulation.TWO_WAY))){
                return true;
            }
        }
            
        System.out.println("Véhicule à fini Hors Piste!");
        return false;
    }
    /**
     * Methode addTile - Ajouter une Tile
     *
     * @param  tileX      La position X ou ajouter Tile dans la Grille
     * @param  tileY      La position Y ou ajouter Tile dans la Grille
     * @param  connexions Les voies de Circulation vers voisins de Tile dans la Grille
     * @return            True si Tile ajoutee
     */
    public boolean addTile(int newTileX, int newTileY, Circulation[] connexions){
        //Check if Position in Grid
        if((newTileX > 0) || (newTileX < this.GRID_WIDTH)
        ||(newTileY > 0) || (newTileY < this.GRID_WIDTH)){
            //Check if Position is Already Affected
            RoadTile currentTile = this.cityGrid[newTileX][newTileY];
            if(currentTile==null){
                //Get New Tile Neighbour and adjust Connexions between them
                System.out.println("Debut Ajout pour ["+newTileX+","+newTileY+"]");
                System.out.println("Mode Auto : "+this.autoMode);
                RoadTile leftNeighbour = null;
                RoadTile upNeighbour = null;
                RoadTile rightNeighbour = null;
                RoadTile downNeighbour = null;
                Circulation[] matchLeft;
                Circulation[] matchUp;
                Circulation[] matchRight;
                Circulation[] matchDown;
                
                //Left Neighbour
                //Check if Left is Border
                if(newTileX>0){
                    leftNeighbour = cityGrid[newTileX-1][newTileY];
                    //Check if Left is Affected
                    if(leftNeighbour == null){
                        matchLeft = Circulation.matchCirculation(connexions[0],null,autoMode);
                    }else{
                        matchLeft = Circulation.matchCirculation(connexions[0],leftNeighbour.getCirculationRight(),autoMode);
                        leftNeighbour.setCirculationRight(matchLeft[1]);
                    }
                }else{
                    matchLeft = Circulation.matchCirculation(null,null,autoMode);
                }
                if(matchLeft[0] != null){
                    connexions[0] = matchLeft[0];
                }
                
                //Up Neighbour
                //Check if Up is Border
                if(newTileY>0){
                    upNeighbour = cityGrid[newTileX][newTileY-1];
                    //Check if Up is Affected
                    if(upNeighbour == null){
                        matchUp = Circulation.matchCirculation(connexions[1],null,autoMode);
                    }else{
                        matchUp = Circulation.matchCirculation(connexions[1],upNeighbour.getCirculationDown(),autoMode);
                        upNeighbour.setCirculationDown(matchUp[1]);
                    }
                }else{
                    matchUp = Circulation.matchCirculation(null,null,autoMode);
                }
                if(matchUp[0] != null){
                    connexions[1] = matchUp[0];
                }
                
                //Right Neighbour
                //Check if Right is Border
                if(newTileX<this.GRID_WIDTH-1){
                    rightNeighbour = cityGrid[newTileX+1][newTileY];
                    //Check if Right is Affected
                    if(rightNeighbour == null){
                        matchRight = Circulation.matchCirculation(connexions[2],null,autoMode);
                    }else{
                        matchRight = Circulation.matchCirculation(connexions[2],rightNeighbour.getCirculationLeft(),autoMode);
                        rightNeighbour.setCirculationLeft(matchRight[1]);
                    }
                }else{
                    matchRight = Circulation.matchCirculation(null,null,autoMode);
                }
                if(matchRight[0] != null){
                    connexions[2] = matchRight[0];
                }
                
                //Down Neighbour
                //Check if Down is Border
                if(newTileY<this.GRID_HEIGHT-1){
                    downNeighbour = cityGrid[newTileX][newTileY+1];
                    //Check if Down is Affected
                    if(downNeighbour == null){
                        matchDown = Circulation.matchCirculation(connexions[3],null,autoMode);
                    }else{
                        matchDown = Circulation.matchCirculation(connexions[3],downNeighbour.getCirculationUp(),autoMode);
                        downNeighbour.setCirculationUp(matchDown[1]);
                    }
                }else{
                    matchDown = Circulation.matchCirculation(null,null,autoMode);
                }
                if(matchDown[0] != null){
                    connexions[3] = matchDown[0];
                }
                
                //Add Tile to the City
                RoadTile newTile = new RoadTile(newTileX,newTileY,connexions);
                System.out.println("Ajout de "+newTile);
                this.cityGrid[newTile.getGridX()][newTile.getGridY()]=newTile;
                return true;
            }else{
                System.out.println("Position deja occupee par "+currentTile);
                return false;
            }
        }
        System.out.println("New Tile is out of the Grid");
        return false;
    }
    
    /**
     * Methode addTile - Ajouter une Tile
     *
     * @param  tile La Tile a ajouter dans la Grille
     * @return      True si Tile ajoutee
     */
    public boolean addTile(RoadTile newTile){
        //Check if Tile is null
        if(newTile!=null){
            //Check if Position is Already Affected
            RoadTile currentTile = this.cityGrid[newTile.getGridX()][newTile.getGridY()];
            if(currentTile==null){
                //Get New Tile Neighbour and adjust Connexions between them
                System.out.println("Debut Ajout pour ["+newTile.getGridX()+","+newTile.getGridY()+"]");
                System.out.println("Mode Auto : "+this.autoMode);
                RoadTile leftNeighbour = null;
                RoadTile upNeighbour = null;
                RoadTile rightNeighbour = null;
                RoadTile downNeighbour = null;
                Circulation[] matchLeft;
                Circulation[] matchUp;
                Circulation[] matchRight;
                Circulation[] matchDown;
                
                //Left Neighbour
                //Check if Left is Border
                if(newTile.getGridX()>0){
                    leftNeighbour = cityGrid[newTile.getGridX()-1][newTile.getGridY()];
                    //Check if Left is Affected
                    if(leftNeighbour == null){
                        matchLeft = Circulation.matchCirculation(newTile.getCirculationLeft(),null,autoMode);
                    }else{
                        matchLeft = Circulation.matchCirculation(newTile.getCirculationLeft(),leftNeighbour.getCirculationRight(),autoMode);
                        leftNeighbour.setCirculationRight(matchLeft[1]);
                    }
                }else{
                    matchLeft = Circulation.matchCirculation(null,null,autoMode);
                }
                if(matchLeft[0] != null){
                    newTile.setCirculationLeft(matchLeft[0]);
                }
                
                //Up Neighbour
                //Check if Up is Border
                if(newTile.getGridY()>0){
                    upNeighbour = cityGrid[newTile.getGridX()][newTile.getGridY()-1];
                    //Check if Up is Affected
                    if(upNeighbour == null){
                        matchUp = Circulation.matchCirculation(newTile.getCirculationUp(),null,autoMode);
                    }else{
                        matchUp = Circulation.matchCirculation(newTile.getCirculationUp(),upNeighbour.getCirculationDown(),autoMode);
                        upNeighbour.setCirculationDown(matchUp[1]);
                    }
                }else{
                    matchUp = Circulation.matchCirculation(null,null,autoMode);
                }
                if(matchUp[0] != null){
                    newTile.setCirculationUp(matchUp[0]);
                }
                
                //Right Neighbour
                //Check if Right is Border
                if(newTile.getGridX()<this.GRID_WIDTH-1){
                    rightNeighbour = cityGrid[newTile.getGridX()+1][newTile.getGridY()];
                    //Check if Right is Affected
                    if(rightNeighbour == null){
                        matchRight = Circulation.matchCirculation(newTile.getCirculationRight(),null,autoMode);
                    }else{
                        matchRight = Circulation.matchCirculation(newTile.getCirculationRight(),rightNeighbour.getCirculationLeft(),autoMode);
                        rightNeighbour.setCirculationLeft(matchRight[1]);
                    }
                }else{
                    matchRight = Circulation.matchCirculation(null,null,autoMode);
                }
                if(matchRight[0] != null){
                    newTile.setCirculationRight(matchRight[0]);
                }
                
                //Down Neighbour
                //Check if Down is Border
                if(newTile.getGridY()<this.GRID_HEIGHT-1){
                    downNeighbour = cityGrid[newTile.getGridX()][newTile.getGridY()+1];
                    //Check if Down is Affected
                    if(downNeighbour == null){
                        matchDown = Circulation.matchCirculation(newTile.getCirculationDown(),null,autoMode);
                    }else{
                        matchDown = Circulation.matchCirculation(newTile.getCirculationDown(),downNeighbour.getCirculationUp(),autoMode);
                        downNeighbour.setCirculationUp(matchDown[1]);
                    }
                }else{
                    matchDown = Circulation.matchCirculation(null,null,autoMode);
                }
                if(matchDown[0] != null){
                    newTile.setCirculationDown(matchDown[0]);
                }
                
                //Add Tile to the City
                System.out.println("Ajout de "+newTile);
                this.cityGrid[newTile.getGridX()][newTile.getGridY()]=newTile;
                return true;
            }else{
                System.out.println("Position deja occupee par "+currentTile);
                return false;
            }
        }
        System.out.println("New Tile is null");
        return false;
    }
    /**
     * Methode containsTile - Dire si la Grille contient une Tile a position donnee
     *
     * @param  tileX La position X ou chercher si Grille contient une Tile
     * @param  tileY La position Y ou chercher si Grille contient une Tile
     * @return       True si Grille a Tile
     */
    public boolean containsTile(int tileX, int tileY){
        if(this.cityGrid[tileX][tileY]!=null){
            return true;
        }
        return false;
    }
    
    /**
     * Methode containsTile - Dire si Tile dans Grille
     *
     * @param  tile  La tile a rechercher
     * @return       True si Tile dans Grille
     */
    public boolean containsTile(RoadTile tile){
        if(tile.equals(this.cityGrid[tile.getGridX()][tile.getGridY()])){
            return true;
        }
        return false;
    }
    
    /**
     * Methode getCityGrid - Recuperer Grille
     * 
     * @return       La Grille
     */
    public RoadTile[][] getCityGrid(){
        return this.cityGrid;
    }
    
    /**
     * Methode getCityCell - Recuperer une Tile
     * 
     * @param  tileX La position X de la case ou chercher Tile dans la grille
     * @param  tileY La position Y de la case ou chercher Tile dans la grille
     * @return       La Tile ou null
     */
    public RoadTile getCityCell(int tileX, int tileY){
        return this.cityGrid[tileX][tileY];
    }
    
    /**
     * Methode removeTile - Retirer une Tile
     *
     * @param  tileX La position X ou retirer Tile dans la grille
     * @param  tileY La position Y ou retirer Tile dans la grille
     * @return       True si Tile retiree ou False si aucune Tile
     */
    public boolean removeTile(int oldTileX, int oldTileY){
        RoadTile oldTile = this.cityGrid[oldTileX][oldTileY];
        if(oldTile!=null){
            Iterator<Car> it = vehicleList.iterator();
            while(it.hasNext()){
                Car temp = it.next();
                if(oldTile.getGridPosition().equals(temp.getGridPosition())){
                    this.vehicleActionsCounter += temp.getActionCounter();
                    it.remove();
                }
            }
            System.out.println("Retrait de "+oldTile);
            this.cityGrid[oldTileX][oldTileY]=null;
            return true;
        }
        System.out.println("Aucune Tile a retirer position ["+oldTileX+","+oldTileY+"]");
        return false;
    }
    
    /**
     * Methode removeTile - Retirer une Tile
     *
     * @param  tileX La Tile a retirer de la Grille
     * @return       True si Tile retiree ou False si aucune Tile
     */
    public boolean removeTile(RoadTile oldTile){
        if(oldTile!=null){
            RoadTile currentTile = this.cityGrid[oldTile.getGridX()][oldTile.getGridY()];
            if(currentTile==null){
                System.out.println("Aucune Tile a retirer position ["+oldTile.getGridX()+","+oldTile.getGridY()+"]");
                return false;
            }
            if(oldTile.equals(currentTile)){
                Iterator<Car> it = vehicleList.iterator();
                while(it.hasNext()){
                    Car temp = it.next();
                    if(oldTile.getGridPosition().equals(temp.getGridPosition())){
                        this.vehicleActionsCounter += temp.getActionCounter();
                        it.remove();
                    }
                }
                System.out.println("Retrait de "+oldTile);
                this.cityGrid[oldTile.getGridX()][oldTile.getGridY()]=null;
                return true;
            }
        }
        System.out.println("Old Tile is null");
        return false;
    }
    
    /**
     * Methode generateVehicle - Generer un Vehicule
     *
     * @return     True si Vehicule Generee
     */
    public boolean generateVehicle(){
        //int valeur = random.nextInt(MAX-MIN)+MIN
        System.out.println("Tentative de Generation");
        int tryBorder = this.random.nextInt(4);
        int tryX = this.random.nextInt(GRID_WIDTH);
        int tryY = this.random.nextInt(GRID_HEIGHT);
        int vehicleWidth = random.nextInt(VEHICLE_MAX_WIDTH-VEHICLE_MIN_WIDTH)+VEHICLE_MIN_WIDTH;
        int vehicleHeight = random.nextInt(VEHICLE_MAX_HEIGHT-VEHICLE_MIN_HEIGHT)+VEHICLE_MIN_HEIGHT;
        int vehicleSpeed = random.nextInt(VEHICLE_MAX_SPEED-VEHICLE_MIN_SPEED)+VEHICLE_MIN_SPEED;
        System.out.println("TryBorder : "+tryBorder);
        System.out.println("TryX : "+tryX+", TryY : "+tryY);
        switch(tryBorder){
            case 0:
            //La bordure gauche
            if(cityGrid[0][tryY] != null){
                if(cityGrid[0][tryY].getCirculationLeft() != Circulation.NO_WAY){
                    Rectangle left = this.getTileLeftIn(cityGrid[0][tryY]);
                    Car car = new Car(0, tryY, (int)left.getX(), (int)left.getY(), vehicleWidth, vehicleHeight, vehicleSpeed, Direction.RIGHT);
                    vehicleList.add(car);
                    System.out.println("Generation Vehicule Bordure Gauche");
                    System.out.println("Voiture ["+car.getGridX()+","+car.getGridY()+"]");
                    System.out.println("Voiture ["+car.getCarX()+","+car.getCarY()+"]");
                    return true;
                }
            }
            break;
                        
            case 1:
            //La bordure superieure
            if(cityGrid[tryX][0] != null){
                if(cityGrid[tryX][0].getCirculationUp() != Circulation.NO_WAY){
                    Rectangle up = this.getTileUpIn(cityGrid[tryX][0]);
                    Car car = new Car(tryX, 0, (int)(up.getX()+up.getWidth()), (int)up.getY(), vehicleWidth, vehicleHeight, vehicleSpeed, Direction.DOWN);
                    vehicleList.add(car);
                    System.out.println("Generation Vehicule Bordure Haut");
                    System.out.println("Voiture ["+car.getGridX()+","+car.getGridY()+"]");
                    System.out.println("Voiture ["+car.getCarX()+","+car.getCarY()+"]");
                    return true;
                }
            }
            break;
            
            case 2:
            //La bordure droite
            if(cityGrid[GRID_WIDTH-1][tryY] != null){
                if(cityGrid[GRID_WIDTH-1][tryY].getCirculationRight() != Circulation.NO_WAY){
                    Rectangle right = this.getTileRightIn(cityGrid[GRID_WIDTH-1][tryY]);
                    Car car = new Car(GRID_WIDTH-1, tryY, (int)(right.getX()+right.getWidth()), (int)(right.getY()+right.getHeight()), vehicleWidth, vehicleHeight, vehicleSpeed, Direction.LEFT);
                    vehicleList.add(car);
                    System.out.println("Generation Vehicule Bordure Droite");
                    System.out.println("Voiture ["+car.getGridX()+","+car.getGridY()+"]");
                    System.out.println("Voiture ["+car.getCarX()+","+car.getCarY()+"]");
                    return true;
                }
            }
            break;
            
            case 3:
            //La bordure inferieure
            if(cityGrid[tryX][GRID_HEIGHT-1] != null){
                if(cityGrid[tryX][GRID_HEIGHT-1].getCirculationDown() != Circulation.NO_WAY){
                    Rectangle down = this.getTileDownIn(cityGrid[tryX][GRID_HEIGHT-1]);
                    Car car = new Car(tryX, GRID_HEIGHT-1, (int)down.getX(), (int)(down.getY()+down.getHeight()), vehicleWidth, vehicleHeight, vehicleSpeed, Direction.UP);
                    vehicleList.add(car);
                    System.out.println("Generation Vehicule Bordure Bas");
                    System.out.println("Voiture ["+car.getGridX()+","+car.getGridY()+"]");
                    System.out.println("Voiture ["+car.getCarX()+","+car.getCarY()+"]");
                    return true;
                }
            }
            break;
            
            default:
            System.out.println("Aucun Vehicule genere");
            return false;
        }
        System.out.println("Aucun Vehicule genere");
        return false;
    }
    
    /**
     * Methode safestGenerateVehicle - Generer un Vehicule avec plus de Chances
     *
     * @return     True si Vehicule Generee
     */
    public boolean safestGenerateVehicle(){
        //int valeur = random.nextInt(MAX-MIN)+MIN
        System.out.println("Tentative de Generation Sure");
        int tryBorder = this.random.nextInt(4);
        int vehicleWidth = random.nextInt(VEHICLE_MAX_WIDTH-VEHICLE_MIN_WIDTH)+VEHICLE_MIN_WIDTH;
        int vehicleHeight = random.nextInt(VEHICLE_MAX_HEIGHT-VEHICLE_MIN_HEIGHT)+VEHICLE_MIN_HEIGHT;
        int vehicleSpeed = random.nextInt(VEHICLE_MAX_SPEED-VEHICLE_MIN_SPEED)+VEHICLE_MIN_SPEED;
        int tryIndex;
        List<RoadTile> safestBorder;
        RoadTile safestTile;
        System.out.println("TryBorder : "+tryBorder);
        switch(tryBorder){
            case 0:
            //La bordure gauche
            safestBorder = this.getGridSafestLeftBorder();
            if(safestBorder.isEmpty()){
                System.out.println("Pas de Tile Bordure Gauche");
                return false;
            }
            tryIndex = random.nextInt(safestBorder.size());
            safestTile = safestBorder.get(tryIndex);
            Rectangle left = this.getTileLeftIn(safestTile);
            {
                Car car = new Car(0, safestTile.getGridY(), (int)left.getX(), (int)left.getY(), vehicleWidth, vehicleHeight, vehicleSpeed, Direction.RIGHT);
                vehicleList.add(car);
                System.out.println("Generation Vehicule Bordure Gauche");
                System.out.println("Voiture ["+car.getGridX()+","+car.getGridY()+"]");
                System.out.println("Voiture ["+car.getCarX()+","+car.getCarY()+"]");
                return true;
            }
            //break;
                        
            case 1:
            //La bordure superieure
            safestBorder = this.getGridSafestSupBorder();
            if(safestBorder.isEmpty()){
                System.out.println("Pas de Tile Bordure Superieure");
                return false;
            }
            tryIndex = random.nextInt(safestBorder.size());
            safestTile = safestBorder.get(tryIndex);
            Rectangle up = this.getTileUpIn(safestTile);
            {
                Car car = new Car(safestTile.getGridX(), 0, (int)(up.getX()+up.getWidth()), (int)up.getY(), vehicleWidth, vehicleHeight, vehicleSpeed, Direction.DOWN);
                vehicleList.add(car);
                System.out.println("Generation Vehicule Bordure Haut");
                System.out.println("Voiture ["+car.getGridX()+","+car.getGridY()+"]");
                System.out.println("Voiture ["+car.getCarX()+","+car.getCarY()+"]");
                return true;
            }
            //break;
            
            case 2:
            //La bordure droite
            safestBorder = this.getGridSafestRightBorder();
            if(safestBorder.isEmpty()){
                System.out.println("Pas de Tile Bordure Droite");
                return false;
            }
            tryIndex = random.nextInt(safestBorder.size());
            safestTile = safestBorder.get(tryIndex);
            Rectangle right = this.getTileRightIn(safestTile);
            {
                Car car = new Car(GRID_WIDTH-1, safestTile.getGridY(), (int)(right.getX()+right.getWidth()), (int)(right.getY()+right.getHeight()), vehicleWidth, vehicleHeight, vehicleSpeed, Direction.LEFT);
                vehicleList.add(car);
                System.out.println("Generation Vehicule Bordure Droite");
                System.out.println("Voiture ["+car.getGridX()+","+car.getGridY()+"]");
                System.out.println("Voiture ["+car.getCarX()+","+car.getCarY()+"]");
                return true;
            }
            //break;
            
            case 3:
            //La bordure inferieure
            safestBorder = this.getGridSafestInfBorder();
            if(safestBorder.isEmpty()){
                System.out.println("Pas de Tile Bordure Inferieure");
                return false;
            }
            tryIndex = random.nextInt(safestBorder.size());
            safestTile = safestBorder.get(tryIndex);
            Rectangle down = this.getTileDownIn(safestTile);
            {
                Car car = new Car(safestTile.getGridX(), GRID_HEIGHT-1, (int)down.getX(), (int)(down.getY()+down.getHeight()), vehicleWidth, vehicleHeight, vehicleSpeed, Direction.UP);
                vehicleList.add(car);
                System.out.println("Generation Vehicule Bordure Bas");
                System.out.println("Voiture ["+car.getGridX()+","+car.getGridY()+"]");
                System.out.println("Voiture ["+car.getCarX()+","+car.getCarY()+"]");
                return true;
            }
            //break;
            
            default:
            System.out.println("Aucun Vehicule genere");
            return false;
        }
        //System.out.println("Aucun Vehicule genere");
        //return false;
    }
    
    /**
     * Methode nextDirection - Changer Direction Vehicule
     *
     * @param  car Le Vehicule Arrivee au centre de Tile
     * @return     True si Vehicule continue
     */
    public boolean nextDirection(Car car){
        RoadTile tile = cityGrid[car.getGridX()][car.getGridY()];
        List<Direction> nextAvaliable = new ArrayList<Direction>();
        int nextIndex;
        switch(car.getDirection().name()){
            case "LEFT":
            if((tile.getCirculationUp() == Circulation.TWO_WAY)
            ||(tile.getCirculationUp() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.UP);
            }
            if((tile.getCirculationRight() == Circulation.TWO_WAY)
            ||(tile.getCirculationRight() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.RIGHT);
            }
            if((tile.getCirculationDown() == Circulation.TWO_WAY)
            ||(tile.getCirculationDown() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.DOWN);
            }
            break;
            
            case "UP":
            if((tile.getCirculationLeft() == Circulation.TWO_WAY)
            ||(tile.getCirculationLeft() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.LEFT);
            }
            if((tile.getCirculationRight() == Circulation.TWO_WAY)
            ||(tile.getCirculationRight() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.RIGHT);
            }
            if((tile.getCirculationDown() == Circulation.TWO_WAY)
            ||(tile.getCirculationDown() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.DOWN);
            }
            break;
            
            case "RIGHT":
            if((tile.getCirculationLeft() == Circulation.TWO_WAY)
            ||(tile.getCirculationLeft() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.LEFT);
            }
            if((tile.getCirculationUp() == Circulation.TWO_WAY)
            ||(tile.getCirculationUp() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.UP);
            }
            if((tile.getCirculationDown() == Circulation.TWO_WAY)
            ||(tile.getCirculationDown() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.DOWN);
            }
            break;
            
            case "DOWN":
            if((tile.getCirculationLeft() == Circulation.TWO_WAY)
            ||(tile.getCirculationLeft() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.LEFT);
            }
            if((tile.getCirculationUp() == Circulation.TWO_WAY)
            ||(tile.getCirculationUp() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.UP);
            }
            if((tile.getCirculationRight() == Circulation.TWO_WAY)
            ||(tile.getCirculationRight() == Circulation.ONE_WAY_IN)){
                nextAvaliable.add(Direction.RIGHT);
            }
            break;
            
            default:
            System.out.println("Fin de Route");
            car.stop();
            return false;
        }
        if(nextAvaliable.size()<1){
            System.out.println("Fin de Route");
            car.stop();
            return false;
        }
        nextIndex = this.random.nextInt(nextAvaliable.size());
        car.setDirection(nextAvaliable.get(nextIndex));
        return true;
    }
    
    /**
     * Methode addVehicle - Ajouter un Vehicule
     *
     * @param  car La Vehicule a ajouter dans la Liste
     * @return     void
     */
    public void addVehicle(Car car){
        this.vehicleList.add(car);
    }
    
    /**
     * Methode containsVehicle - Dire si Vehicule dans Liste
     *
     * @param  car Le Vehicule a rechercher
     * @return     True si Vehicule dans Liste
     */
    public boolean containsVehicle(Car car){
        for(Car carInList : this.vehicleList){
            if(car.isEquals(carInList)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Methode vehicleIn - Dire si Vehicule dans Tile
     *
     * @param  car  Le Vehicule
     * @param  tile La Case a rechercher
     * @return      True si Vehicule dans Tile
     */
    public boolean vehicleIn(Car car, RoadTile tile){
        if(tile.getGridPosition().equals(car.getGridPosition())){
            return true;
        }
        return false;
    }
    
    /**
     * Methode computeVehicleGridPosition - Dire avec Position Vehicule dans JPanel sa Position dans Grille
     *
     * @param  car Le Vehicule a calculer
     * @return     void
     */
    public void computeVehicleGridPosition(Car car)
    {
        int VEHICLE_TILE_X=-1;
        int VEHICLE_TILE_Y=-1;
        for(int i=0;i<this.GRID_WIDTH;i++){
            if(((this.TILE_WIDTH*i)<=car.getCarX())&&(car.getCarX()<=(this.TILE_WIDTH*(i+1)))){
                VEHICLE_TILE_X=i;
                break;
            }
        }
        for(int j=0;j<this.GRID_HEIGHT;j++){
            if(((this.TILE_HEIGHT*j)<=car.getCarY())&&(car.getCarY()<=(this.TILE_HEIGHT*(j+1)))){
                VEHICLE_TILE_Y=j;
                break;
            }
        }
        if(VEHICLE_TILE_X==-1 || VEHICLE_TILE_Y==-1){
            System.out.println("Voiture pas dans la Grile");
            System.out.println("Voiture ["+car.getCarX()+","+car.getCarY()+"]");
            car.stop();
        }
        car.setGridPosition(VEHICLE_TILE_X,VEHICLE_TILE_Y);
    }
    
    /**
     * Methode getVehicles - Recuperer Liste Vehicules
     *
     * @return     La Liste des Vehicules
     */
    public List<Car> getVehicles()
    {
        return this.vehicleList;
    }
    
    /**
     * Methode getVehicle - Recuperer Vehicule
     *
     * @param  index Index du Vehicule recherchee
     * @return       Le Vehicule
     */
    public Car getVehicle(int index)
    {
        return vehicleList.get(index);
    }
    
    /**
     * Methode removeVehicle - Retirer un Vehicule
     *
     * @param  car  Le Vehicule a retirer de la Liste
     * @return      True si Vehicule retiree
     */
    public boolean removeVehicle(Car car){
        Iterator<Car> it = vehicleList.iterator();
        while(it.hasNext()){
            Car temp = it.next();
            if(car.isEquals(temp)){
                this.vehicleActionsCounter += temp.getActionCounter();
                it.remove();
                return true;
            }
        }
        System.out.println("Le Vehicule est pas dans la Ville");
        return false;
    }
    
    /**
     * Methode removeStoppedVehicle - Retirer les Vehicules qui ne Roulent Plus
     *
     * @return     void
     */
    public void removeStoppedVehicle(){
        Iterator<Car> it = vehicleList.iterator();
        while(it.hasNext()){
            Car temp = it.next();
            if(!temp.canDrive()){
                this.vehicleActionsCounter += temp.getActionCounter();
                it.remove();
            }
        }
    }
    
    /**
     * Methode headedTo - Calculer la Tile Grille suivante dans Direction Vehicule
     *
     * @param  car  Le Vehicule a Tester
     * @return      La Tile Grille suivante dans Direction Vehicule
     */
    public RoadTile headedTo(Car car){
        int headedToGridX = car.getGridX()+car.getDirectionX();
        int headedToGridY = car.getGridY()+car.getDirectionY();
        //If out of the Grid
        if((headedToGridX<0)
        ||(headedToGridX>this.GRID_WIDTH-1)
        ||(headedToGridY<0)
        ||(headedToGridY>this.GRID_HEIGHT-1)){
            return null;
        }
        return this.cityGrid[headedToGridX][headedToGridY];
    }
    
    /**
     * Methode closeTo - Calculer les Vehicules qui sont potentiellement Proches
     *
     * @param  car  Le Vehicule a Tester
     * @return      La Liste des Vehicules Proches
     */
    public List<Car> closeTo(Car car){
        //As Vehicle are supposed to go in one direction and rarely reverse
        //Close Vehicle are constidered to be those on the same tile
        //And those on the tile in the Vehicle Direction
        List<Car> closeTo = new ArrayList<Car>();
        RoadTile headedTo = this.headedTo(car);
        for(Car otherCar : this.vehicleList){
            //Same Car
            if(car.isEquals(otherCar)){
                continue;
            }
            //Same Tile
            if(car.getGridPosition().equals(otherCar.getGridPosition())){
                closeTo.add(otherCar);
            }
            //Tile where Car is headedTo
            else if(headedTo(car) != null){
                if(vehicleIn(otherCar,headedTo)){
                    closeTo.add(otherCar);
                }
            }
        }
        return closeTo;
    }
    
    /**
     * Methode isColliding - Calculer si deux Vehicules Collisionnent
     *
     * @param  carA Le Premier Vehicule a Tester
     * @param  carB Le Second Vehicule a Tester
     * @return      True si les Vehicules Collisionnent
     */
    public boolean isColliding(Car carA,Car carB){
        //As we track left rear and Rectangle can have negative width/height
        //We cannot use Rectangle.intersects(Rectangle r)
        //Method in Class Rectangle implement standard Algorithme supposing
        //Origin as upper left corner and can not work with 0 or negative Dimensions
        //We redefine the Algorithm
        Rectangle hitboxCarA = carA.getCarHitbox();
        Rectangle hitboxCarB = carB.getCarHitbox();
        return CollisionTools.extendedRectToRectCollision(hitboxCarA,hitboxCarB);
    }
    
    /**
     * Methode getGridLeftBorder - Recuperer La Bordure Gauche Grille
     *
     * @return     La Bordure Gauche Grille
     */
    public RoadTile[] getGridLeftBorder()
    {
        RoadTile[] gridLeftBorder = new RoadTile[this.GRID_HEIGHT];
        for(int leftY=0;leftY<this.GRID_HEIGHT;leftY++){
            gridLeftBorder[leftY] = this.cityGrid[0][leftY];
        }
        return gridLeftBorder;
    }
    
    /**
     * Methode getGridSupBorder - Recuperer La Bordure Superieure Grille
     *
     * @return     La Bordure Superieure Grille
     */
    public RoadTile[] getGridSupBorder()
    {
        RoadTile[] gridSupBorder = new RoadTile[this.GRID_WIDTH];
        for(int supX=0;supX<this.GRID_WIDTH;supX++){
            gridSupBorder[supX] = this.cityGrid[supX][0];
        }
        return gridSupBorder;
    }
    
    /**
     * Methode getGridRightBorder - Recuperer La Bordure Droite Grille
     *
     * @return     La Bordure Droite Grille
     */
    public RoadTile[] getGridRightBorder()
    {
        int limitX = this.GRID_WIDTH-1;
        RoadTile[] gridRightBorder = new RoadTile[this.GRID_HEIGHT];
        for(int rightY=0;rightY<this.GRID_HEIGHT;rightY++){
            gridRightBorder[rightY] = this.cityGrid[limitX][rightY];
        }
        return gridRightBorder;
    }
    
    /**
     * Methode getGridInfBorder - Recuperer La Bordure Inferieure Grille
     *
     * @return     La Bordure Inferieure Grille
     */
    public RoadTile[] getGridInfBorder()
    {
        int limitY = this.GRID_HEIGHT-1;
        RoadTile[] gridInfBorder = new RoadTile[this.GRID_WIDTH];
        for(int infX=0;infX<this.GRID_WIDTH;infX++){
            gridInfBorder[infX] = this.cityGrid[infX][limitY];
        }
        return gridInfBorder;
    }
    
    /**
     * Methode getGridSafeLeftBorder - Recuperer Les Tiles places dans La Bordure Gauche Grille
     *
     * @return     Les Tiles places dans La Bordure Gauche Grille
     */
    public List<RoadTile> getGridSafeLeftBorder()
    {
        List<RoadTile> gridSafeLeftBorder = new ArrayList<RoadTile>();
        for(int leftY=0;leftY<this.GRID_HEIGHT;leftY++){
            if(this.cityGrid[0][leftY] != null){
                gridSafeLeftBorder.add(this.cityGrid[0][leftY]);
            }
        }
        return gridSafeLeftBorder;
    }
    
    /**
     * Methode getGridSafeSupBorder - Recuperer Les Tiles places dans La Bordure Superieure Grille
     *
     * @return     Les Tiles places dans La Bordure Superieure Grille
     */
    public List<RoadTile> getGridSafeSupBorder()
    {
        List<RoadTile> gridSafeSupBorder = new ArrayList<RoadTile>();
        for(int supX=0;supX<this.GRID_WIDTH;supX++){
            if(this.cityGrid[supX][0] != null){
                gridSafeSupBorder.add(this.cityGrid[supX][0]);
            }
        }
        return gridSafeSupBorder;
    }
    
    /**
     * Methode getGridSafeRightBorder - Recuperer Les Tiles places dans La Bordure Droite Grille
     *
     * @return     Les Tiles places dans La Bordure Droite Grille
     */
    public List<RoadTile> getGridSafeRightBorder()
    {
        int limitX = this.GRID_WIDTH-1;
        List<RoadTile> gridSafeRightBorder = new ArrayList<RoadTile>();
        for(int rightY=0;rightY<this.GRID_HEIGHT;rightY++){
            if(this.cityGrid[limitX][rightY] != null){
                gridSafeRightBorder.add(this.cityGrid[limitX][rightY]);
            }
        }
        return gridSafeRightBorder;
    }
    
    /**
     * Methode getGridSafeInfBorder - Recuperer Les Tiles places dans La Bordure Inferieure Grille
     *
     * @return     Les Tiles places dans La Bordure Inferieure Grille
     */
    public List<RoadTile> getGridSafeInfBorder()
    {
        int limitY = this.GRID_HEIGHT-1;
        List<RoadTile> gridSafeInfBorder = new ArrayList<RoadTile>();
        for(int infX=0;infX<this.GRID_WIDTH;infX++){
            if(this.cityGrid[infX][limitY] != null){
                gridSafeInfBorder.add(this.cityGrid[infX][limitY]);
            }
        }
        return gridSafeInfBorder;
    }
    
    /**
     * Methode getGridSafestLeftBorder - Recuperer Les Tiles places dans et Connectes a La Bordure Gauche Grille
     *
     * @return     Les Tiles places dans et Connectes a La Bordure Gauche Grille
     */
    public List<RoadTile> getGridSafestLeftBorder()
    {
        List<RoadTile> gridSafestLeftBorder = new ArrayList<RoadTile>();
        for(int leftY=0;leftY<this.GRID_HEIGHT;leftY++){
            if(this.cityGrid[0][leftY] == null){
                continue;
            }
            if(this.cityGrid[0][leftY].getCirculationLeft() != Circulation.NO_WAY){
                gridSafestLeftBorder.add(this.cityGrid[0][leftY]);
            }
        }
        return gridSafestLeftBorder;
    }
    
    /**
     * Methode getGridSafestSupBorder - Recuperer Les Tiles places dans et Connectes a La Bordure Superieure Grille
     *
     * @return     Les Tiles places dans et Connectes a La Bordure Superieure Grille
     */
    public List<RoadTile> getGridSafestSupBorder()
    {
        List<RoadTile> gridSafestSupBorder = new ArrayList<RoadTile>();
        for(int supX=0;supX<this.GRID_WIDTH;supX++){
            if(this.cityGrid[supX][0] != null){
                if(this.cityGrid[supX][0].getCirculationUp() != Circulation.NO_WAY){
                    gridSafestSupBorder.add(this.cityGrid[supX][0]);
                }
            }
        }
        return gridSafestSupBorder;
    }
    
    /**
     * Methode getGridSafestRightBorder - Recuperer Les Tiles places dans et Connectes a La Bordure Droite Grille
     *
     * @return     Les Tiles places dans et Connectes a La Bordure Droite Grille
     */
    public List<RoadTile> getGridSafestRightBorder()
    {
        int limitX = this.GRID_WIDTH-1;
        List<RoadTile> gridSafestRightBorder = new ArrayList<RoadTile>();
        for(int rightY=0;rightY<this.GRID_HEIGHT;rightY++){
            if(this.cityGrid[limitX][rightY] != null){
                if(this.cityGrid[limitX][rightY].getCirculationRight() != Circulation.NO_WAY){
                    gridSafestRightBorder.add(this.cityGrid[limitX][rightY]);
                }
            }
        }
        return gridSafestRightBorder;
    }
    
    /**
     * Methode getGridSafestInfBorder - Recuperer Les Tiles places dans et Connectes a La Bordure Inferieure Grille
     *
     * @return     Les Tiles places dans et Connectes a La Bordure Inferieure Grille
     */
    public List<RoadTile> getGridSafestInfBorder()
    {
        int limitY = this.GRID_HEIGHT-1;
        List<RoadTile> gridSafestInfBorder = new ArrayList<RoadTile>();
        for(int infX=0;infX<this.GRID_WIDTH;infX++){
            if(this.cityGrid[infX][limitY] != null){
                if(this.cityGrid[infX][limitY].getCirculationDown() != Circulation.NO_WAY){
                    gridSafestInfBorder.add(this.cityGrid[infX][limitY]);
                }
            }
        }
        return gridSafestInfBorder;
    }
    
    /**
     * Methode getGridWidth - Recuperer Longeur Grille
     *
     * @return     Longeur Grille
     */
    public int getGridWidth()
    {
        return this.GRID_WIDTH;
    }
    
    /**
     * Methode getGridHeight - Recuperer Hauteur Grille
     *
     * @return     Hauteur Grille
     */
    public int getGridHeight()
    {
        return this.GRID_HEIGHT;
    }
    
    /**
     * Methode getTileWidth - Recuperer Longeur Tile
     *
     * @return     Longeur Tile
     */
    public int getTileWidth()
    {
        return this.TILE_WIDTH;
    }
    
    /**
     * Methode getTileHeight - Recuperer Hauteur Tile
     *
     * @return     Hauteur Tile
     */
    public int getTileHeight()
    {
        return this.TILE_HEIGHT;
    }
    
    /**
     * Methode getWidth - Recuperer Longeur Ville
     *
     * @return     Longeur Ville
     */
    public int getWidth()
    {
        return this.GRID_WIDTH*this.TILE_WIDTH;
    }
    
    /**
     * Methode getHeight - Recuperer Hauteur Ville
     *
     * @return     Hauteur Ville
     */
    public int getHeight()
    {
        return this.GRID_HEIGHT*this.TILE_HEIGHT;
    }
    
    /**
     * Methode getVehicleMinWidth - Recuperer Longeur Min Vehicule
     *
     * @return     La Longeur Min des Vehicules
     */
    public int getVehicleMinWidth()
    {
        // Code
        return this.VEHICLE_MIN_WIDTH;
    }
    
    /**
     * Methode getVehicleMinHeight - Recuperer Hauteur Min Vehicule
     *
     * @return     La Hauteur Min des Vehicules
     */
    public int getVehicleMinHeight()
    {
        // Code
        return this.VEHICLE_MIN_HEIGHT;
    }
    
    /**
     * Methode getVehicleMaxWidth - Recuperer Longeur Max Vehicule
     *
     * @return     La Longeur Max des Vehicules
     */
    public int getVehicleMaxWidth()
    {
        // Code
        return this.VEHICLE_MAX_WIDTH;
    }
    
    /**
     * Methode getVehicleMaxHeight - Recuperer Hauteur Max Vehicule
     *
     * @return     La Hauteur Max des Vehicules
     */
    public int getVehicleMaxHeight()
    {
        // Code
        return this.VEHICLE_MAX_HEIGHT;
    }
    
    /**
     * Methode getVehicleMinSpeed - Recuperer Vitesse Min Vehicule
     *
     * @return     La Vitesse Min des Vehicules
     */
    public int getVehicleMinSpeed()
    {
        // Code
        return this.VEHICLE_MIN_SPEED;
    }
    
    /**
     * Methode getVehicleMaxSpeed - Recuperer Vitesse Max Vehicule
     *
     * @return     La Vitesse Max des Vehicules
     */
    public int getVehicleMaxSpeed()
    {
        // Code
        return this.VEHICLE_MAX_SPEED;
    }
    
    /**
     * Methode isAuto - Dit si Mode Auto Actif
     *
     * @return     True si autoMode Actif
     */
    public boolean isAuto()
    {
        // Code
        return this.autoMode;
    }
    
    /**
     * Methode setAuto - Definit si Mode Auto Actif
     *
     * @param  auto   La Valeur du Mode Auto
     * @return        void
     */
    public void setAuto(boolean autoMode)
    {
        // Code
        this.autoMode = autoMode;
    }
    
    //DECOUPAGE DES TILES :
    //_________________
    //|    |  |  |    |
    //|    |UI|UO|    |
    //|____|__|__|____|
    //| LO |     | RI |
    //|____|  C  |____|
    //| LI |     | RO |
    //|____|_____|____|
    //|    |  |  |    |
    //|    |DO|DI|    |
    //|____|__|__|____|
    //C : Center
    //LI : Left IN
    //LO : Left OUT
    //UI : Up IN
    //UO : Up OUT
    //RI : Right IN
    //RO : Right OUT
    //DI : Down IN
    //DO : Down OUT
    //Separation Lines for IN OUT avaliable with corresponding Rectangle
    //Rectangle(originX,originY,rectangleWidth,rectangleHeight)
    /**
     * Methode getTileCenter - Recuperer Centre
     *
     * @param  tile   La tile sur laquelle on cherche le centre
     * @return        Un Rectangle representant le Centre de tile
     */
    public Rectangle getTileCenter(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Center Upper Left corner using tile position in Grid
        int tileCenterOriginX = (tileLanesWidth)+(tile.getGridX()*TILE_WIDTH);
        int tileCenterOriginY = (tileLanesHeight)+(tile.getGridY()*TILE_HEIGHT);
        //Return representing Rectangle
        return new Rectangle(tileCenterOriginX,tileCenterOriginY,tileCenterWidth,tileCenterHeight);
    }
    
    /**
     * Methode getTileLeft - Recuperer Voie Gauche
     *
     * @param  tile   La tile sur laquelle on cherche les voie de Gauche
     * @return        Un Rectangle representant les voie Gauche IN OUT de tile
     */
    public Rectangle getTileLeft(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Left Lane Upper Left corner using tile position in Grid
        int tileLeftLaneOriginX = 0+(tile.getGridX()*TILE_WIDTH);
        int tileLeftLaneOriginY = (tileLanesHeight)+(tile.getGridY()*TILE_HEIGHT);
        //Return representing Rectangle
        return new Rectangle(tileLeftLaneOriginX,tileLeftLaneOriginY,tileLanesWidth,tileCenterHeight);
    }
    
    /**
     * Methode getTileLeftIn - Recuperer Voie Gauche sens Entrant
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Gauche
     * @return        Un Rectangle representant la voie Gauche des vehicules entrant dans tile
     */
    public Rectangle getTileLeftIn(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Left IN Lane Upper Left corner using tile position in Grid
        int tileLeftInOriginX = 0+(tile.getGridX()*TILE_WIDTH);
        int tileLeftInOriginY = (tileLanesHeight)+(tile.getGridY()*TILE_HEIGHT)+(tileCenterHeight/2);
        //Return representing Rectangle
        return new Rectangle(tileLeftInOriginX,tileLeftInOriginY,tileLanesWidth,tileCenterHeight/2);
    }
    
    /**
     * Methode getTileLeftOut - Recuperer Voie Gauche sens Sortant
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Gauche
     * @return        Un Rectangle representant la voie Gauche des vehicules sortant de tile
     */
    public Rectangle getTileLeftOut(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Left OUT Lane Upper Left corner using tile position in Grid
        int tileLeftOutOriginX = 0+(tile.getGridX()*TILE_WIDTH);
        int tileLeftOutOriginY = (tileLanesHeight)+(tile.getGridY()*TILE_HEIGHT);
        //Return representing Rectangle
        return new Rectangle(tileLeftOutOriginX,tileLeftOutOriginY,tileLanesWidth,tileCenterHeight/2);
    }
    
    /**
     * Methode getTileUp - Recuperer Voie Haut
     *
     * @param  tile   La tile sur laquelle on cherche les voie de Haut
     * @return        Un Rectangle representant les voie Haut IN OUT de tile
     */
    public Rectangle getTileUp(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Up Lane Upper Left corner using tile position in Grid
        int tileUpLaneOriginX = (tileLanesWidth)+(tile.getGridX()*TILE_WIDTH);
        int tileUpLaneOriginY = 0+(tile.getGridY()*TILE_HEIGHT);
        //Return representing Rectangle
        return new Rectangle(tileUpLaneOriginX,tileUpLaneOriginY,tileCenterWidth,tileLanesHeight);
    }
    
    /**
     * Methode getTileUpIn - Recuperer Voie Haut sens Entrant
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Haut
     * @return        Un Rectangle representant la voie Haut des vehicules entrant dans tile
     */
    public Rectangle getTileUpIn(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Up IN Upper Left corner using tile position in Grid
        int tileUpInOriginX = (tileLanesWidth)+(tile.getGridX()*TILE_WIDTH);
        int tileUpInOriginY = 0+(tile.getGridY()*TILE_HEIGHT);
        //Return representing Rectangle
        return new Rectangle(tileUpInOriginX,tileUpInOriginY,tileCenterWidth/2,tileLanesHeight);
    }
    
    /**
     * Methode getTileUpOut - Recuperer Voie Haut sens Sortant
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Haut
     * @return        Un Rectangle representant la voie Haut des vehicules sortant de tile
     */
    public Rectangle getTileUpOut(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Up OUT Upper Left corner using tile position in Grid
        int tileUpOutOriginX = (tileLanesWidth)+(tile.getGridX()*TILE_WIDTH)+(tileCenterWidth/2);
        int tileUpOutOriginY = 0+(tile.getGridY()*TILE_HEIGHT);
        //Return representing Rectangle
        return new Rectangle(tileUpOutOriginX,tileUpOutOriginY,tileCenterWidth/2,tileLanesHeight);
    }
    
    /**
     * Methode getTileRight - Recuperer Voie Droite
     *
     * @param  tile   La tile sur laquelle on cherche les voie de Droite
     * @return        Un Rectangle representant les voie Droite IN OUT de tile
     */
    public Rectangle getTileRight(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Right Lane Upper Left corner using tile position in Grid
        int tileRightLaneOriginX = (tileLanesWidth)+(tile.getGridX()*TILE_WIDTH)+(tileCenterWidth);
        int tileRightLaneOriginY = (tileLanesHeight)+(tile.getGridY()*TILE_HEIGHT);
        //Return representing Rectangle
        return new Rectangle(tileRightLaneOriginX,tileRightLaneOriginY,tileLanesWidth,tileCenterHeight);
    }
    
    /**
     * Methode getTileRightIn - Recuperer Voie Doite sens Entrant
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Droite
     * @return        Un Rectangle representant la voie Droite des vehicules entrant dans tile
     */
    public Rectangle getTileRightIn(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Right OUT Upper Left corner using tile position in Grid
        int tileRightInOriginX = (tileLanesWidth)+(tile.getGridX()*TILE_WIDTH)+(tileCenterWidth);
        int tileRightInOriginY = (tileLanesHeight)+(tile.getGridY()*TILE_HEIGHT);
        //Return representing Rectangle
        return new Rectangle(tileRightInOriginX,tileRightInOriginY,tileLanesWidth,tileCenterHeight/2);
    }
    
    /**
     * Methode getTileRightOut - Recuperer Voie Droite sens Sortant
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Droite
     * @return        Un Rectangle representant la voie Droite des vehicules sortant de tile
     */
    public Rectangle getTileRightOut(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Right IN Upper Left corner using tile position in Grid
        int tileRightOutOriginX = (tileLanesWidth)+(tile.getGridX()*TILE_WIDTH)+(tileCenterWidth);
        int tileRightOutOriginY = (tileLanesHeight)+(tile.getGridY()*TILE_HEIGHT)+(tileCenterHeight/2);
        //Return representing Rectangle
        return new Rectangle(tileRightOutOriginX,tileRightOutOriginY,tileLanesWidth,tileCenterHeight/2);
    }
    
    /**
     * Methode getTileDown - Recuperer Voie Bas
     *
     * @param  tile   La tile sur laquelle on cherche les voie de Bas
     * @return        Un Rectangle representant les voie Bas IN OUT de tile
     */
    public Rectangle getTileDown(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Down Lane Upper Left corner using tile position in Grid
        int tileDownLaneOriginX = (tileLanesWidth)+(tile.getGridX()*TILE_WIDTH);
        int tileDownLaneOriginY = (tileLanesHeight)+(tile.getGridY()*TILE_HEIGHT)+(tileCenterHeight);
        //Return representing Rectangle
        return new Rectangle(tileDownLaneOriginX,tileDownLaneOriginY,tileCenterWidth,tileLanesHeight);
    }
    
    /**
     * Methode getTileDownIn - Recuperer Voie Bas sens Entrant
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Bas
     * @return        Un Rectangle representant la voie Bas des vehicules entrant dans tile
     */
    public Rectangle getTileDownIn(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Down In Upper Left corner using tile position in Grid
        int tileDownInOriginX = (tileLanesWidth)+(tile.getGridX()*TILE_WIDTH)+(tileCenterWidth/2);
        int tileDownInOriginY = (tileLanesHeight)+(tile.getGridY()*TILE_HEIGHT)+(tileCenterHeight);
        //Return representing Rectangle
        return new Rectangle(tileDownInOriginX,tileDownInOriginY,tileCenterWidth/2,tileLanesHeight);
    }
    
    /**
     * Methode getTileDownOut - Recuperer Voie Bas sens Sortant
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Bas
     * @return        Un Rectangle representant la voie Bas des vehicules sortant de tile
     */
    public Rectangle getTileDownOut(RoadTile tile)
    {
        // Code
        //Divide the tile "equally" in three parts
        int tileLanesWidth = TILE_WIDTH/3;
        int tileLanesHeight = TILE_HEIGHT/3;
        //Get the repartition remainder and associate it to tile Center
        int tileCenterWidth = this.getTileCenterWidth();
        int tileCenterHeight = this.getTileCenterHeight();
        //Get the Down Out Upper Left corner using tile position in Grid
        int tileDownOutOriginX = (tileLanesWidth)+(tile.getGridX()*TILE_WIDTH);
        int tileDownOutOriginY = (tileLanesHeight)+(tile.getGridY()*TILE_HEIGHT)+(tileCenterHeight);
        //Return representing Rectangle
        return new Rectangle(tileDownOutOriginX,tileDownOutOriginY,tileCenterWidth/2,tileLanesHeight);
    }
    
    /**
     * Methode getTileLanesWidth - Calcule la Largeur des Voie
     *
     * @return     Le nombre de Sous Pixels occupes par les Voies de Tile
     */
    public int getTileLanesWidth()
    {
        // Code
        int tileLanesWidth = TILE_WIDTH/3;
        return tileLanesWidth;
    }
    
    /**
     * Methode getTileLanesHeight - Calcule la Hauteur des Voie
     *
     * @return     Le nombre de Sous Pixels occupes par les Voies de Tile
     */
    public int getTileLanesHeight()
    {
        // Code
        int tileLanesHeight = TILE_HEIGHT/3;
        return tileLanesHeight;
    }
    
    /**
     * Methode getTileCenterWidth - Affecte le reste de la Division en Largeur du Centre
     *
     * @return     Le nombre de Sous Pixels occupes par le Centre de Tile
     */
    public int getTileCenterWidth()
    {
        // Code
        int tileLanesWidth = TILE_WIDTH/3;
        int tileCenterWidth = TILE_WIDTH-(2*tileLanesWidth);
        return tileCenterWidth;
    }
    
    /**
     * Methode getTileCenterHeight - Affecte le reste de la Division en Hauteur du Centre
     *
     * @return     Le nombre de Sous Pixels occupes par le Centre de Tile
     */
    public int getTileCenterHeight()
    {
        // Code
        int tileLanesHeight = TILE_HEIGHT/3;
        int tileCenterHeight = TILE_HEIGHT-(2*tileLanesHeight);
        return tileCenterHeight;
    }
    
    /**
     * Methode reset - Reset la Grille et Statistiques
     *
     * @return     True
     */
    public boolean reset(){
        Iterator<Car> it = this.vehicleList.iterator();
        while(it.hasNext()){
            it.next();
            it.remove();
        }
        for(int i=0;i<this.GRID_WIDTH;i++){
            for(int j=0;j<this.GRID_HEIGHT;j++){
                this.cityGrid[i][j]=null;
            }
        }
        this.lastGeneration = 0;
        this.stepCounter = 0;
        this.generationsCounter = 0;
        this.vehicleActionsCounter = 0;
        return true;
    }
    
    /**
     * Methode isActive - Dit si la Simulation est Active
     *
     * @return     Active
     */
    public boolean isActive(){
        return this.active;
    }
    
    /**
     * Methode desactive - Desactive Main
     *
     * @return     void
     */
    public void desactive(){
        this.reset();
        this.active=false;
    }
}
