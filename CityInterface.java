import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * interface CityInterface.
 *
 * @author  Anis Menaa William Mesnil
 * @version Version 6
 */

public interface CityInterface
{
    /**
     * entete de Methode step - Avancer
     *
     * @return       void
     */
    public void step();
    /**
     * entete de Methode isValidPosition - Dit si Vehicule sortie de Route
     *
     * @param  car    Le Vehicule
     * @param  tile   La Tile
     * @return        True si Position est Valide
     */
    public boolean isValidPosition(Car car, RoadTile tile);
    /**
     * entete de Methode addTile - Ajouter une Tile
     *
     * @param  tileX      La position X ou ajouter Tile dans la Grille
     * @param  tileY      La position Y ou ajouter Tile dans la Grille
     * @param  connexions Les voies de Circulation vers voisins de Tile dans la Grille
     * @return            True si Tile ajoutee
     */
    public boolean addTile(int newTileX, int newTileY, Circulation[] connexions);
    /**
     * entete de Methode addTile - Ajouter une Tile
     *
     * @param  tile  La Tile a ajouter dans la Grille
     * @return       True si Tile ajoutee
     */
    public boolean addTile(RoadTile newTile);
    /**
     * entete de Methode containsTile - Dire si la Grille contient une Tile a position donnee
     *
     * @param  tileX  La position X ou chercher si Grille contient une Tile
     * @param  tileY  La position Y ou chercher si Grille contient une Tile
     * @return        True si Grille a Tile
     */
    public boolean containsTile(int tileX, int tileY);
    /**
     * entete de Methode containsTile - Dire si Tile dans Grille
     *
     * @param  tile   La tile a rechercher
     * @return        True si Tile dans Grille
     */
    public boolean containsTile(RoadTile tile);
    /**
     * entete de Methode getCityGrid - Recuperer Grille
     * 
     * @return        La Grille
     */
    public RoadTile[][] getCityGrid();
    /**
     * entete de Methode getCityCell - Recuperer une Tile
     * 
     * @param  tileX  La position X de la case ou chercher Tile dans la grille
     * @param  tileY  La position Y de la case ou chercher Tile dans la grille
     * @return        La Tile ou null
     */
    public RoadTile getCityCell(int tileX, int tileY);
    /**
     * entete de Methode removeTile - Retirer une Tile
     *
     * @param  tileX  La Tile a retirer de la Grille
     * @return        True si Tile retiree ou False si aucune Tile
     */
    public boolean removeTile(RoadTile oldTile);
    /**
     * entete de Methode removeTile - Retirer une Tile
     *
     * @param  tileX  La position X ou retirer Tile dans la grille
     * @param  tileY  La position Y ou retirer Tile dans la grille
     * @return        True si Tile retiree ou False si aucune Tile
     */
    public boolean removeTile(int oldTileX, int oldTileY);
    /**
     * entete de Methode generateVehicle - Generer un Vehicule
     *
     * @return        True si Vehicule Generee
     */
    public boolean generateVehicle();
    /**
     * entete de Methode safestGenerateVehicle - Generer un Vehicule avec plus de Chances
     *
     * @return        True si Vehicule Generee
     */
    public boolean safestGenerateVehicle();
    /**
     * entete de Methode nextDirection - Changer Direction Vehicule
     *
     * @param  car    Le Vehicule Arrivee au centre de Tile
     * @return        True si Vehicule continue
     */
    public boolean nextDirection(Car car);
    /**
     * entete de Methode addVehicle - Ajouter un Vehicule
     *
     * @param  car    Le Vehicule a ajouter dans la Liste
     * @return        void
     */
    public void addVehicle(Car car);
    /**
     * entete de Methode containsVehicle - Dire si Vehicule dans Liste
     *
     * @param  car    Le Vehicule a rechercher
     * @return        True si Vehicule dans Liste
     */
    public boolean containsVehicle(Car car);
    /**
     * entete de Methode vehicleIn - Dire si Vehicule dans Tile
     *
     * @param  car    Le Vehicule
     * @param  tile   La Case a rechercher
     * @return        True si Vehicule dans Tile
     */
    public boolean vehicleIn(Car car, RoadTile tile);
    /**
     * entete de Methode computeVehicleGridPosition - Dire avec Position Vehicule dans JPanel sa Position dans Grille
     *
     * @param  car    Le Vehicule a calculer
     * @return        void
     */
    public void computeVehicleGridPosition(Car car);
    /**
     * entete de Methode getVehicles - Recuperer Liste Vehicules
     *
     * @return        La Liste des Vehicules
     */
    public List<Car> getVehicles();
    /**
     * entete de Methode getVehicle - Recuperer Vehicule
     *
     * @param  index  Index du Vehicule recherchee
     * @return        Le Vehicule
     */
    public Car getVehicle(int index);
    /**
     * entete de Methode removeVehicle - Retirer un Vehicule
     *
     * @param  car    Le Vehicule a retirer de la Liste
     * @return        True si Vehicule retiree
     */
    public boolean removeVehicle(Car car);
    /**
     * Methode removeStoppedVehicle - Retirer les Vehicules qui ne Roulent Plus
     *
     * @return        void
     */
    public void removeStoppedVehicle();
    /**
     * entete de Methode closeTo - Calculer les Vehicules qui sont potentiellement Proches
     *
     * @param  car    Le Vehicule a Tester
     * @return        La Liste des Vehicules Proches
     */
    public List<Car> closeTo(Car car);
    /**
     * entete de Methode isColliding - Calculer si deux Vehicules Collisionnent
     *
     * @param  carA   Le Premier Vehicule a Tester
     * @param  carB   Le Second Vehicule a Tester
     * @return        True si les Vehicules Collisionnent
     */
    public boolean isColliding(Car carA,Car carB);
    /**
     * entete de Methode getGridLeftBorder - Recuperer La Bordure Gauche Grille
     *
     * @return        La Bordure Gauche Grille
     */
    public RoadTile[] getGridLeftBorder();
    /**
     * entete de Methode getGridSupBorder - Recuperer La Bordure Superieure Grille
     *
     * @return        La Bordure Superieure Grille
     */
    public RoadTile[] getGridSupBorder();
    /**
     * entete de Methode getGridRightBorder - Recuperer La Bordure Droite Grille
     *
     * @return        La Bordure Droite Grille
     */
    public RoadTile[] getGridRightBorder();
    /**
     * entete de Methode getGridInfBorder - Recuperer La Bordure Inferieure Grille
     *
     * @return        La Bordure Inferieure Grille
     */
    public RoadTile[] getGridInfBorder();
    /**
     * entete de Methode getGridSafeLeftBorder - Recuperer Les Tiles places dans La Bordure Gauche Grille
     *
     * @return        Les Tiles places dans La Bordure Gauche Grille
     */
    public List<RoadTile> getGridSafeLeftBorder();
    /**
     * entete de Methode getGridSafeSupBorder - Recuperer Les Tiles places dans La Bordure Superieure Grille
     *
     * @return        Les Tiles places dans La Bordure Superieure Grille
     */
    public List<RoadTile> getGridSafeSupBorder();
    /**
     * entete de Methode getGridSafeRightBorder - Recuperer Les Tiles places dans La Bordure Droite Grille
     *
     * @return        Les Tiles places dans La Bordure Droite Grille
     */
    public List<RoadTile> getGridSafeRightBorder();
    /**
     * entete de Methode getGridSafeInfBorder - Recuperer Les Tiles places dans La Bordure Inferieure Grille
     *
     * @return        Les Tiles places dans La Bordure Inferieure Grille
     */
    public List<RoadTile> getGridSafeInfBorder();
    /**
     * entete de Methode getGridSafestLeftBorder - Recuperer Les Tiles places dans et Connectes a La Bordure Gauche Grille
     *
     * @return        Les Tiles places dans et Connectes a La Bordure Gauche Grille
     */
    public List<RoadTile> getGridSafestLeftBorder();
    /**
     * entete de Methode getGridSafestSupBorder - Recuperer Les Tiles places dans et Connectes a La Bordure Superieure Grille
     *
     * @return        Les Tiles places dans et Connectes a La Bordure Superieure Grille
     */
    public List<RoadTile> getGridSafestSupBorder();
    /**
     * entete de Methode getGridSafestRightBorder - Recuperer Les Tiles places dans et Connectes a La Bordure Droite Grille
     *
     * @return        Les Tiles places dans et Connectes a La Bordure Droite Grille
     */
    public List<RoadTile> getGridSafestRightBorder();
    /**
     * entete de Methode getGridSafestInfBorder - Recuperer Les Tiles places dans et Connectes a La Bordure Inferieure Grille
     *
     * @return        Les Tiles places dans et Connectes a La Bordure Inferieure Grille
     */
    public List<RoadTile> getGridSafestInfBorder();
    /**
     * entete de Methode getGridWidth - Recuperer Longeur Grille
     *
     * @return        Longeur Grille
     */
    public int getGridWidth();
    /**
     * entete de Methode getGridHeight - Recuperer Hauteur Grille
     *
     * @return        Hauteur Grille
     */
    public int getGridHeight();
    /**
     * entete de Methode getTileWidth - Recuperer Longeur Tile
     *
     * @return        Longeur Tile
     */
    public int getTileWidth();
    /**
     * entete de Methode getTileHeight - Recuperer Hauteur Tile
     *
     * @return        Hauteur Tile
     */
    public int getTileHeight();
    /**
     * entete de Methode getWidth - Recuperer Longeur Ville
     *
     * @return        Longeur Ville
     */
    public int getWidth();
    /**
     * entete de Methode getHeight - Recuperer Hauteur Ville
     *
     * @return        Hauteur Ville
     */
    public int getHeight();
    /**
     * entete de Methode getVehicleMinWidth - Recuperer Longeur Min Vehicule
     *
     * @return        La Longeur Min des Vehicules
     */
    public int getVehicleMinWidth();
    /**
     * entete de Methode getVehicleMinHeight - Recuperer Hauteur Min Vehicule
     *
     * @return        La Hauteur Min des Vehicules
     */
    public int getVehicleMinHeight();
    /**
     * entete de Methode getVehicleMaxWidth - Recuperer Longeur Max Vehicule
     *
     * @return        La Longeur Max des Vehicules
     */
    public int getVehicleMaxWidth();
    /**
     * entete de Methode getVehicleMaxHeight - Recuperer Hauteur Max Vehicule
     *
     * @return        La Hauteur Max des Vehicules
     */
    public int getVehicleMaxHeight();
    /**
     * entete de Methode getVehicleMinSpeed - Recuperer Vitesse Min Vehicule
     *
     * @return        La Vitesse Min des Vehicules
     */
    public int getVehicleMinSpeed();
    /**
     * entete de Methode getVehicleMaxSpeed - Recuperer Vitesse Max Vehicule
     *
     * @return        La Vitesse Max des Vehicules
     */
    public int getVehicleMaxSpeed();
    /**
     * entete de Methode isAuto - Dit si Mode Auto Actif
     *
     * @return        True si autoMode Actif
     */
    public boolean isAuto();
    /**
     * entete de Methode setAuto - Definit si Mode Auto Actif
     *
     * @param  auto   La valeur du Mode Auto
     * @return        void
     */
    public void setAuto(boolean autoMode);
    /**
     * entete de Methode getTileCenter - Recuperer Centre Tile
     *
     * @param  tile   La tile sur laquelle on cherche le centre
     * @return        Un Rectangle representant le Centre de tile
     */
    public Rectangle getTileCenter(RoadTile tile);
    /**
     * entete de Methode getTileLeft - Recuperer Gauche Tile
     *
     * @param  tile   La tile sur laquelle on cherche les voie de Gauche
     * @return        Un Rectangle representant les voie Gauche IN OUT de tile
     */
    public Rectangle getTileLeft(RoadTile tile);
    /**
     * entete de Methode getTileLeftIn - Recuperer Gauche Tile
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Gauche
     * @return        Un Rectangle representant la voie Gauche des vehicules entrant dans tile
     */
    public Rectangle getTileLeftIn(RoadTile tile);
    /**
     * entete de Methode getTileLeftOut - Recuperer Gauche Tile
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Gauche
     * @return        Un Rectangle representant la voie Gauche des vehicules sortant de tile
     */
    public Rectangle getTileLeftOut(RoadTile tile);
    /**
     * entete de Methode getTileUp - Recuperer Haut Tile
     *
     * @param  tile   La tile sur laquelle on cherche les voie de Haut
     * @return        Un Rectangle representant les voie Haut IN OUT de tile
     */
    public Rectangle getTileUp(RoadTile tile);
    /**
     * entete de Methode getTileUpIn - Recuperer Haut Tile
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Haut
     * @return        Un Rectangle representant la voie Haut des vehicules entrant dans tile
     */
    public Rectangle getTileUpIn(RoadTile tile);
    /**
     * entete de Methode getTileUpOut - Recuperer Haut Tile
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Haut
     * @return        Un Rectangle representant la voie Haut des vehicules sortant de tile
     */
    public Rectangle getTileUpOut(RoadTile tile);
    /**
     * entete de Methode getTileRight - Recuperer Droite Tile
     * 
     * tile   La tile sur laquelle on cherche les voie de Droite
     * @return        Un Rectangle representant les voie Droite IN OUT de tile
     */
    public Rectangle getTileRight(RoadTile tile);
    /**
     * entete de Methode getTileRightIn - Recuperer Droite Tile
     * 
     * @param  tile   La tile sur laquelle on cherche la voie de Droite
     * @return        Un Rectangle representant la voie Droite des vehicules entrant dans tile
     */
    public Rectangle getTileRightIn(RoadTile tile);
    /**
     * entete de Methode getTileRightOut - Recuperer Droite Tile
     * 
     * @param  tile   La tile sur laquelle on cherche la voie de Droite
     * @return        Un Rectangle representant la voie Droite des vehicules sortant de tile
     */
    public Rectangle getTileRightOut(RoadTile tile);
    /**
     * entete de Methode getTileDown - Recuperer Bas Tile
     * 
     * @param  tile   La tile sur laquelle on cherche les voie de Bas
     * @return        Un Rectangle representant les voie Bas IN OUT de tile
     */
    public Rectangle getTileDown(RoadTile tile);
    /**
     * entete de Methode getTileDownIn - Recuperer Bas Tile
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Bas
     * @return        Un Rectangle representant la voie Bas des vehicules entrant dans tile
     */
    public Rectangle getTileDownIn(RoadTile tile);
    /**
     * entete de Methode getTileDownOut - Recuperer Bas Tile
     *
     * @param  tile   La tile sur laquelle on cherche la voie de Bas
     * @return        Un Rectangle representant la voie Bas des vehicules sortant de tile
     */
    public Rectangle getTileDownOut(RoadTile tile);
    /**
     * entete de Methode getTileLanesWidth - Recuperer Largeur Voies
     *
     * @return     Le nombre de Sous Pixels occupes par les Voies de Tile
     */
    public int getTileLanesWidth();
    /**
     * entete de Methode getTileLanesHeight - Recuperer Hauteur Voies
     *
     * @return     Le nombre de Sous Pixels occupes par les Voies de Tile
     */
    public int getTileLanesHeight();
    /**
     * entete de Methode getTileCenterWidth - Recuperer Bas Tile
     *
     * @return     Le nombre de Sous Pixels occupes par le Centre de Tile
     */
    public int getTileCenterWidth();
    /**
     * entete de Methode getTileCenterHeight - Recuperer Bas Tile
     *
     * @return     Le nombre de Sous Pixels occupes par le Centre de Tile
     */
    public int getTileCenterHeight();
    /**
     * entete de Methode reset - Reset la Grille et Statistiques
     *
     * @return        True
     */
    public boolean reset();
    /**
     * entete de Methode isActive - Dit si la Simulation est Active
     *
     * @return        Active
     */
    public boolean isActive();
    /**
     * entete de Methode desactive - Desactive Main
     *
     * @return        void
     */
    public void desactive();
}
