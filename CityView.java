import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.Graphics2D;

import java.util.List;

import javax.swing.JPanel;

/**
 * classe CityView.
 *
 * @author William Mesnil Anis Menaa
 * @version Version 6
 */
public class CityView extends JPanel implements CityViewInterface
{
    // variables d'instance - Le Modele qui contient les donnees
    private CityInterface city;

    /**
     * Constructeur d'objets de classe CityView
     */
    public CityView(CityInterface city, int alignX, int alignY)
    {
        // initialisation des variables d'instance
        super();
        this.city = city;
        this.setBackground(Color.GRAY);
        this.setSize(this.city.getWidth(),this.city.getHeight());
        this.setBounds(alignX,alignY,this.getWidth(),this.getHeight());
        this.setOpaque(false);
    }
    
    /**
     * Methode paintComponent
     *
     * @param  g   Le Graphique
     * @return     void
     */
    @Override
    public void paintComponent(Graphics g)
    {
        // Code
        //Only Forward Graphics g to Call DrawCity
        this.drawCity(g);
    }
    
    /**
     * Methode drawCity - Dessiner les Cases, Routes et Voitures
     *
     * @see        CityViewInterface#draw()
     * @param  g   Le Graphique
     * @return     void
     */
    public void drawCity(Graphics g)
    {
        // Code
        //Draw Cells Limit, Placed Tiles and Cars
        g.setColor(Color.BLACK);
        this.setBackground(Color.GRAY);
        this.drawCells(g);
        this.drawTiles(g);
        this.drawVehicles(g);
        g.setColor(Color.BLACK);
    }
    
    /**
     * Méthode drawCells - Dessiner les Cases
     *
     * @see        CityViewInterface#drawCells()
     * @param  g   Le Graphique
     * @return     void
     */
    public void drawCells(Graphics g)
    {
        // Code
        g.setColor(Color.BLACK);
        int GRID_WIDTH = this.city.getGridWidth();
        int GRID_HEIGHT = this.city.getGridHeight();
        int TILE_WIDTH = this.city.getTileWidth();
        int TILE_HEIGHT = this.city.getTileHeight();
        int CITY_WIDTH = this.city.getWidth();
        int CITY_HEIGHT = this.city.getHeight();
        //g.drawLine(startX,startY,endX,endY);
        for(int i=0;i<GRID_WIDTH;i++){
            g.drawLine(TILE_WIDTH * i, 0, TILE_WIDTH * i, CITY_HEIGHT);
        }
        for(int j=0;j<GRID_HEIGHT;j++){
            g.drawLine(0, TILE_HEIGHT * j, CITY_WIDTH, TILE_HEIGHT * j);
        }
    }
    
    /**
     * Méthode drawTile - Dessiner une Route
     *
     * @param  g    Le Graphique
     * @param  tile La Tile
     * @return      void
     */
    public void drawTile(Graphics g, RoadTile tile)
    {
        // Code
        int TILE_WIDTH = this.city.getTileWidth();
        int TILE_HEIGHT = this.city.getTileHeight();
        //Paint Center
        Rectangle center = this.city.getTileCenter(tile);
        g.fillRect((int)center.getX(),(int)center.getY(),(int)center.getWidth(),(int)center.getHeight());
        //Paint Lanes
        //Paint Left Lane
        if(tile == null){
            return;
        }
        //Left Lane
        if(tile.getCirculationLeft() != Circulation.NO_WAY){
            //Left Lane is Two Way
            if(tile.getCirculationLeft() == Circulation.TWO_WAY){
                //Get entier Lane
                Rectangle leftLane = this.city.getTileLeft(tile);
                g.setColor(Color.BLACK);
                //Both Lane
                g.fillRect((int)leftLane.getX(),(int)leftLane.getY(),(int)leftLane.getWidth(),(int)leftLane.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = leftLane.getX()
                //StartY = leftLane.getY()+(leftLane.getHeight()/2)
                //EndX = leftLane.getX()+leftLane.getWidth()
                //EndY = leftLane.getY()+(leftLane.getHeight()/2)
                g.drawLine((int)leftLane.getX(),(int)(leftLane.getY()+(leftLane.getHeight()/2)),(int)(leftLane.getX()+leftLane.getWidth()),(int)(leftLane.getY()+(leftLane.getHeight()/2)));
                //Mark at Center
                //StartX = leftLane.getX()+leftLane.getWidth()
                //StartY = leftLane.getY()+(leftLane.getHeight()/2)
                //EndX = leftLane.getX()+leftLane.getWidth()
                //EndY = leftLane.getY()+leftLane.getHeight()
                g.drawLine((int)(leftLane.getX()+leftLane.getWidth()),(int)(leftLane.getY()+(leftLane.getHeight()/2)),(int)(leftLane.getX()+leftLane.getWidth()),(int)(leftLane.getY()+leftLane.getHeight()));
            }
            //Left Lane is One Way Entry
            else if(tile.getCirculationLeft() == Circulation.ONE_WAY_IN){
                //Get Left Out
                Rectangle leftOut = this.city.getTileLeftOut(tile);
                g.setColor(Color.BLACK);
                //Lane Out
                g.fillRect((int)leftOut.getX(),(int)leftOut.getY(),(int)leftOut.getWidth(),(int)leftOut.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = leftOut.getX()
                //StartY = leftOut.getY()+leftOut.getHeight()
                //EndX = leftOut.getX()+leftOut.getWidth()
                //EndY = leftOut.getY()+leftOut.getHeight()
                g.drawLine((int)leftOut.getX(),(int)(leftOut.getY()+leftOut.getHeight()),(int)(leftOut.getX()+leftOut.getWidth()),(int)(leftOut.getY()+leftOut.getHeight()));
                //Arrow Sign
                //SignTopLeftX = leftOut.getX()+(leftOut.getWidth()/2)
                //SignTopLeftY = leftOut.getY()+leftOut.getHeight()
                //SignWidth = leftOut.getHeight()
                //SignHeight = leftOut.getHeight()
                int signTopLeftX = (int)(leftOut.getX()+(leftOut.getWidth()/2));
                int signTopLeftY = (int)(leftOut.getY()+leftOut.getHeight());
                g.setColor(Color.BLUE);
                g.fillOval(signTopLeftX,signTopLeftY,(int)leftOut.getHeight(),(int)leftOut.getHeight());
                //Symbol -
                //StartX = signTopLeftX
                //StartY = signTopLeftY+(leftOut.getHeight()/2)
                //EndX = signTopLeftX+leftOut.getHeight()
                //EndY = signTopLeftY+(leftOut.getHeight()/2)
                g.setColor(Color.WHITE);
                g.drawLine(signTopLeftX,signTopLeftY+(int)(leftOut.getHeight()/2),signTopLeftX+(int)leftOut.getHeight(),signTopLeftY+(int)(leftOut.getHeight()/2));
                //Symbol /
                //StartX = signTopLeftX
                //StartY = signTopLeftY+(leftOut.getHeight()/2)
                //EndX = signTopLeftX+(leftOut.getHeight()/2)
                //EndY = signTopLeftY
                g.drawLine(signTopLeftX,signTopLeftY+(int)(leftOut.getHeight()/2),signTopLeftX+(int)(leftOut.getHeight()/2),signTopLeftY);
                //Symbol \
                //StartX = signTopLeftX
                //StartY = signTopLeftY+(leftOut.getHeight()/2)
                //EndX = signTopLeftX+(leftOut.getHeight()/2)
                //EndY = signTopLeftY+leftOut.getHeight()
                g.drawLine(signTopLeftX,signTopLeftY+(int)(leftOut.getHeight()/2),signTopLeftX+(int)(leftOut.getHeight()/2),signTopLeftY+(int)leftOut.getHeight());
            }
            //Left Lane is One Way Exit
            else if(tile.getCirculationLeft() == Circulation.ONE_WAY_OUT){
                //Get Left In
                Rectangle leftIn = this.city.getTileLeftIn(tile);
                g.setColor(Color.BLACK);
                //Lane In
                g.fillRect((int)leftIn.getX(),(int)leftIn.getY(),(int)leftIn.getWidth(),(int)leftIn.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = leftIn.getX()
                //StartY = leftIn.getY()
                //EndX = leftIn.getX()+leftIn.getWidth()
                //EndY = leftIn.getY()
                g.drawLine((int)leftIn.getX(),(int)leftIn.getY(),(int)(leftIn.getX()+leftIn.getWidth()),(int)leftIn.getY());
                //Mark at Center
                //StartX = leftIn.getX()+leftIn.getWidth()
                //StartY = leftIn.getY()
                //EndX = leftIn.getX()+leftIn.getWidth()
                //EndY = leftIn.getY()+leftIn.getHeight()
                g.drawLine((int)(leftIn.getX()+leftIn.getWidth()),(int)leftIn.getY(),(int)(leftIn.getX()+leftIn.getWidth()),(int)(leftIn.getY()+leftIn.getHeight()));
                //Interdiction Sign
                //SignTopLeftX = leftIn.getX()+(leftIn.getWidth()/2)
                //SignTopLeftY = leftIn.getY()-leftIn.getHeight()
                //SignWidth = leftIn.getHeight()
                //SignHeight = leftIn.getHeight()
                int signTopLeftX = (int)(leftIn.getX()+(leftIn.getWidth()/2));
                int signTopLeftY = (int)(leftIn.getY()-leftIn.getHeight());
                g.setColor(Color.RED);
                g.fillOval(signTopLeftX,signTopLeftY,(int)leftIn.getHeight(),(int)leftIn.getHeight());
                //Symbol -
                //StartX = signTopLeftX
                //StartY = signTopLeftY+(leftIn.getHeight()/2)
                //EndX = signTopLeftX+leftIn.getHeight()
                //EndY = signTopLeftY+(leftIn.getHeight()/2)
                g.setColor(Color.WHITE);
                g.drawLine(signTopLeftX,signTopLeftY+(int)(leftIn.getHeight()/2),signTopLeftX+(int)leftIn.getHeight(),signTopLeftY+(int)(leftIn.getHeight()/2));
            }
        }
        //Up Lane
        if(tile.getCirculationUp() != Circulation.NO_WAY){
            //Up Lane is Two Way
            if(tile.getCirculationUp() == Circulation.TWO_WAY){
                //Get entier Lane
                Rectangle upLane = this.city.getTileUp(tile);
                g.setColor(Color.BLACK);
                //Both Lane
                g.fillRect((int)upLane.getX(),(int)upLane.getY(),(int)upLane.getWidth(),(int)upLane.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = upLane.getX()+(upLane.getWidth()/2)
                //StartY = upLane.getY()
                //EndX = upLane.getX()+(upLane.getWidth()/2)
                //EndY = upLane.getY()+upLane.getHeight()
                g.drawLine((int)(upLane.getX()+(upLane.getWidth()/2)),(int)upLane.getY(),(int)(upLane.getX()+(upLane.getWidth()/2)),(int)(upLane.getY()+upLane.getHeight()));
                //Mark at Center
                //StartX = upLane.getX()
                //StartY = upLane.getY()+upLane.getHeight()
                //EndX = upLane.getX()+(upLane.getWidth()/2)
                //EndY = upLane.getY()+upLane.getHeight()
                g.drawLine((int)upLane.getX(),(int)(upLane.getY()+upLane.getHeight()),(int)(upLane.getX()+(upLane.getWidth()/2)),(int)(upLane.getY()+upLane.getHeight()));
            }
            //Up Lane is One Way Entry
            else if(tile.getCirculationUp() == Circulation.ONE_WAY_IN){
                //Get Up Out
                Rectangle upOut = this.city.getTileUpOut(tile);
                g.setColor(Color.BLACK);
                //Lane Out
                g.fillRect((int)upOut.getX(),(int)upOut.getY(),(int)upOut.getWidth(),(int)upOut.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = upOut.getX()
                //StartY = upOut.getY()
                //EndX = upOut.getX()
                //EndY = upOut.getY()+upOut.getHeight()
                g.drawLine((int)upOut.getX(),(int)upOut.getY(),(int)upOut.getX(),(int)(upOut.getY()+upOut.getHeight()));
                //Arrow Sign
                //SignTopLeftX = upOut.getX()-upOut.getWidth()
                //SignTopLeftY = upOut.getY()+(upOut.getHeight()/2)
                //SignWidth = upOut.getWidth()
                //SignHeight = upOut.getWidth()
                int signTopLeftX = (int)(upOut.getX()-upOut.getWidth());
                int signTopLeftY = (int)(upOut.getY()+(upOut.getHeight()/2));
                g.setColor(Color.BLUE);
                g.fillOval(signTopLeftX,signTopLeftY,(int)upOut.getWidth(),(int)upOut.getWidth());
                //Symbol |
                //StartX = signTopLeftX+(upOut.getWidth()/2)
                //StartY = signTopLeftY
                //EndX = signTopLeftX+(upOut.getWidth()/2)
                //EndY = signTopLeftY+upOut.getWidth()
                g.setColor(Color.WHITE);
                g.drawLine(signTopLeftX+(int)(upOut.getWidth()/2),signTopLeftY,signTopLeftX+(int)(upOut.getWidth()/2),signTopLeftY+(int)upOut.getWidth());
                //Symbol /
                //StartX = signTopLeftX
                //StartY = signTopLeftY+(upOut.getWidth()/2)
                //EndX = signTopLeftX+(upOut.getWidth()/2)
                //EndY = signTopLeftY
                g.drawLine(signTopLeftX,signTopLeftY+(int)(upOut.getWidth()/2),signTopLeftX+(int)(upOut.getWidth()/2),signTopLeftY);
                //Symbol \
                //StartX = signTopLeftX+(upOut.getWidth()/2)
                //StartY = signTopLeftY
                //EndX = signTopLeftX+upOut.getWidth()
                //EndY = signTopLeftY+(upOut.getWidth()/2)
                g.drawLine(signTopLeftX+(int)(upOut.getWidth()/2),signTopLeftY,signTopLeftX+(int)upOut.getWidth(),signTopLeftY+(int)(upOut.getWidth()/2));
            }
            //Up Lane is One Way Exit
            else if(tile.getCirculationUp() == Circulation.ONE_WAY_OUT){
                //Get Up In
                Rectangle upIn = this.city.getTileUpIn(tile);
                g.setColor(Color.BLACK);
                //Lane In
                g.fillRect((int)upIn.getX(),(int)upIn.getY(),(int)upIn.getWidth(),(int)upIn.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = upIn.getX()+upIn.getWidth()
                //StartY = upIn.getY()
                //EndX = upIn.getX()+upIn.getWidth()
                //EndY = upIn.getY()+upIn.getHeight()
                g.drawLine((int)(upIn.getX()+upIn.getWidth()),(int)upIn.getY(),(int)(upIn.getX()+upIn.getWidth()),(int)(upIn.getY()+upIn.getHeight()));
                //Mark at Center
                //StartX = upIn.getX()
                //StartY = upIn.getY()+upIn.getHeight()
                //EndX = upIn.getX()+upIn.getWidth()
                //EndY = upIn.getY()+upIn.getHeight()
                g.drawLine((int)upIn.getX(),(int)(upIn.getY()+upIn.getHeight()),(int)(upIn.getX()+upIn.getWidth()),(int)(upIn.getY()+upIn.getHeight()));
                //Interdiction Sign
                //SignTopLeftX = upIn.getX()+upIn.getWidth()
                //SignTopLeftY = upIn.getY()+(upIn.getHeight()/2)
                //SignWidth = upIn.getWidth()
                //SignHeight = upIn.getWidth()
                int signTopLeftX = (int)(upIn.getX()+upIn.getWidth());
                int signTopLeftY = (int)(upIn.getY()+(upIn.getHeight()/2));
                g.setColor(Color.RED);
                g.fillOval(signTopLeftX,signTopLeftY,(int)upIn.getWidth(),(int)upIn.getWidth());
                //Symbol -
                //StartX = signTopLeftX
                //StartY = signTopLeftY+(upIn.getWidth()/2)
                //EndX = signTopLeftX+upIn.getWidth
                //EndY = signTopLeftY+(upIn.getWidth()/2)
                g.setColor(Color.WHITE);
                g.drawLine(signTopLeftX,signTopLeftY+(int)(upIn.getWidth()/2),signTopLeftX+(int)upIn.getWidth(),signTopLeftY+(int)(upIn.getWidth()/2));
            }
        }
        //Right Lane
        if(tile.getCirculationRight() != Circulation.NO_WAY){
            //Right Lane is Two Way
            if(tile.getCirculationRight() == Circulation.TWO_WAY){
                //Get entier Lane
                Rectangle rightLane = this.city.getTileRight(tile);
                g.setColor(Color.BLACK);
                //Both Lane
                g.fillRect((int)rightLane.getX(),(int)rightLane.getY(),(int)rightLane.getWidth(),(int)rightLane.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = rightLane.getX()
                //StartY = rightLane.getY()+(rightLane.getHeight()/2)
                //EndX = rightLane.getX()+rightLane.getWidth()
                //EndY = rightLane.getY()+(rightLane.getHeight()/2)
                g.drawLine((int)rightLane.getX(),(int)(rightLane.getY()+(rightLane.getHeight()/2)),(int)(rightLane.getX()+rightLane.getWidth()),(int)(rightLane.getY()+(rightLane.getHeight()/2)));
                //Mark at Center
                //StartX = rightLane.getX()
                //StartY = rightLane.getY()
                //EndX = rightLane.getX()
                //EndY = rightLane.getY()+(rightLane.getHeight()/2)
                g.drawLine((int)rightLane.getX(),(int)rightLane.getY(),(int)rightLane.getX(),(int)(rightLane.getY()+(rightLane.getHeight()/2)));
            }
            //Right Lane is One Way Entry
            else if(tile.getCirculationRight() == Circulation.ONE_WAY_IN){
                //Get Right Out
                Rectangle rightOut = this.city.getTileRightOut(tile);
                g.setColor(Color.BLACK);
                //Lane Out
                g.fillRect((int)rightOut.getX(),(int)rightOut.getY(),(int)rightOut.getWidth(),(int)rightOut.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = rightOut.getX()
                //StartY = rightOut.getY()
                //EndX = rightOut.getX()+rightOut.getWidth()
                //EndY = rightOut.getY()
                g.drawLine((int)rightOut.getX(),(int)rightOut.getY(),(int)(rightOut.getX()+rightOut.getWidth()),(int)rightOut.getY());
                //Arrow Sign
                //SignTopLeftX = rightOut.getX()
                //SignTopLeftY = rightOut.getY()-rightOut.getHeight()
                //SignWidth = rightOut.getHeight()
                //SignHeight = rightOut.getHeight()
                int signTopLeftX = (int)rightOut.getX();
                int signTopLeftY = (int)(rightOut.getY()-rightOut.getHeight());
                g.setColor(Color.BLUE);
                g.fillOval(signTopLeftX,signTopLeftY,(int)rightOut.getHeight(),(int)rightOut.getHeight());
                //Symbol -
                //StartX = signTopLeftX
                //StartY = signTopLeftY+(rightOut.getHeight()/2)
                //EndX = signTopLeftX+rightOut.getHeight()
                //EndY = signTopLeftY+(rightOut.getHeight()/2)
                g.setColor(Color.WHITE);
                g.drawLine(signTopLeftX,signTopLeftY+(int)(rightOut.getHeight()/2),signTopLeftX+(int)rightOut.getHeight(),signTopLeftY+(int)(rightOut.getHeight()/2));
                //Symbol /
                //StartX = signTopLeftX+(rightOut.getHeight()/2)
                //StartY = signTopLeftY+rightOut.getHeight()
                //EndX = signTopLeftX+rightOut.getHeight
                //EndY = signTopLeftY+(rightOut.getHeight()/2)
                g.drawLine(signTopLeftX+(int)(rightOut.getHeight()/2),signTopLeftY+(int)rightOut.getHeight(),signTopLeftX+(int)rightOut.getHeight(),signTopLeftY+(int)(rightOut.getHeight()/2));
                //Symbol \
                //StartX = signTopLeftX+(rightOut.getHeight()/2)
                //StartY = signTopLeftY
                //EndX = signTopLeftX+rightOut.getHeight
                //EndY = signTopLeftY+(rightOut.getHeight()/2)
                g.drawLine(signTopLeftX+(int)(rightOut.getHeight()/2),signTopLeftY,signTopLeftX+(int)rightOut.getHeight(),signTopLeftY+(int)(rightOut.getHeight()/2));
            }
            //Right Lane is One Way Exit
            else if(tile.getCirculationRight() == Circulation.ONE_WAY_OUT){
                //Get Right In
                Rectangle rightIn = this.city.getTileRightIn(tile);
                g.setColor(Color.BLACK);
                //Lane In
                g.fillRect((int)rightIn.getX(),(int)rightIn.getY(),(int)rightIn.getWidth(),(int)rightIn.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = rightIn.getX()
                //StartY = rightIn.getY()+rightIn.getHeight()
                //EndX = rightIn.getX()+rightIn.getWidth()
                //EndY = rightIn.getY()+rightIn.getHeight()
                g.drawLine((int)rightIn.getX(),(int)(rightIn.getY()+rightIn.getHeight()),(int)(rightIn.getX()+rightIn.getWidth()),(int)(rightIn.getY()+rightIn.getHeight()));
                //Mark at Center
                //StartX = rightIn.getX()
                //StartY = rightIn.getY()
                //EndX = rightIn.getX()
                //EndY = rightIn.getY()+rightIn.getHeight()
                g.drawLine((int)rightIn.getX(),(int)rightIn.getY(),(int)rightIn.getX(),(int)(rightIn.getY()+rightIn.getHeight()));
                //Interdiction Sign
                //SignTopLeftX = rightIn.getX()
                //SignTopLeftY = rightIn.getY()+rightIn.getHeight()
                //SignWidth = rightIn.getHeight()
                //SignHeight = rightIn.getHeight()
                int signTopLeftX = (int)rightIn.getX();
                int signTopLeftY = (int)(rightIn.getY()+rightIn.getHeight());
                g.setColor(Color.RED);
                g.fillOval(signTopLeftX,signTopLeftY,(int)rightIn.getHeight(),(int)rightIn.getHeight());
                //Symbol -
                //StartX = signTopLeftX
                //StartY = signTopLeftY+(rightIn.getHeight()/2)
                //EndX = signTopLeftX+rightIn.getHeight()
                //EndY = signTopLeftY+(rightIn.getHeight()/2)
                g.setColor(Color.WHITE);
                g.drawLine(signTopLeftX,signTopLeftY+(int)(rightIn.getHeight()/2),signTopLeftX+(int)rightIn.getHeight(),signTopLeftY+(int)(rightIn.getHeight()/2));
            }
        }
        //Down Lane
        if(tile.getCirculationDown() != Circulation.NO_WAY){
            //Down Lane is Two Way
            if(tile.getCirculationDown() == Circulation.TWO_WAY){
                //Get entier Lane
                Rectangle downLane = this.city.getTileDown(tile);
                g.setColor(Color.BLACK);
                //Both Lane
                g.fillRect((int)downLane.getX(),(int)downLane.getY(),(int)downLane.getWidth(),(int)downLane.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = downLane.getX()+(downLane.getWidth()/2)
                //StartY = downLane.getY()
                //EndX = downLane.getX()+(downLane.getWidth()/2)
                //EndY = downLane.getY()+downLane.getHeight()
                g.drawLine((int)(downLane.getX()+(downLane.getWidth()/2)),(int)downLane.getY(),(int)(downLane.getX()+(downLane.getWidth()/2)),(int)(downLane.getY()+downLane.getHeight()));
                //Mark at Center
                //StartX = downLane.getX()+(downLane.getWidth()/2)
                //StartY = downLane.getY()
                //EndX = downLane.getX()+downLane.getWidth()
                //EndY = downLane.getY()
                g.drawLine((int)(downLane.getX()+(downLane.getWidth()/2)),(int)downLane.getY(),(int)(downLane.getX()+downLane.getWidth()),(int)downLane.getY());
            }
            //Down Lane is One Way Entry
            else if(tile.getCirculationDown() == Circulation.ONE_WAY_IN){
                //Get Down Out
                Rectangle downOut = this.city.getTileDownOut(tile);
                g.setColor(Color.BLACK);
                //Lane Out
                g.fillRect((int)downOut.getX(),(int)downOut.getY(),(int)downOut.getWidth(),(int)downOut.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = downOut.getX()+downOut.getWidth()
                //StartY = downOut.getY()
                //EndX = downOut.getX()+downOut.getWidth()
                //EndY = downOut.getY()+downOut.getHeight()
                g.drawLine((int)(downOut.getX()+downOut.getWidth()),(int)downOut.getY(),(int)(downOut.getX()+downOut.getWidth()),(int)(downOut.getY()+downOut.getHeight()));
                //Arrow Sign
                //SignTopLeftX = downOut.getX()+downOut.getWidth()
                //SignTopLeftY = downOut.getY()
                //SignWidth = downOut.getWidth()
                //SignHeight = downOut.getWidth()
                int signTopLeftX = (int)(downOut.getX()+downOut.getWidth());
                int signTopLeftY = (int)downOut.getY();
                g.setColor(Color.BLUE);
                g.fillOval(signTopLeftX,signTopLeftY,(int)downOut.getWidth(),(int)downOut.getWidth());
                //Symbol |
                //StartX = signTopLeftX+(downOut.getWidth()/2)
                //StartY = signTopLeftY
                //EndX = signTopLeftX+(downOut.getWidth()/2)
                //EndY = signTopLeftY+downOut.getWidth()
                g.setColor(Color.WHITE);
                g.drawLine(signTopLeftX+(int)(downOut.getWidth()/2),signTopLeftY,signTopLeftX+(int)(downOut.getWidth()/2),signTopLeftY+(int)downOut.getWidth());
                //Symbol /
                //StartX = signTopLeftX+(downOut.getWidth()/2)
                //StartY = signTopLeftY+downOut.getWidth()
                //EndX = signTopLeftX+downOut.getWidth()
                //EndY = signTopLeftY+(downOut.getWidth()/2)
                g.drawLine(signTopLeftX+(int)(downOut.getWidth()/2),signTopLeftY+(int)downOut.getWidth(),signTopLeftX+(int)downOut.getWidth(),signTopLeftY+(int)(downOut.getWidth()/2));
                //Symbol \
                //StartX = signTopLeftX
                //StartY = signTopLeftY+(downOut.getWidth()/2)
                //EndX = signTopLeftX+(downOut.getWidth()/2)
                //EndY = signTopLeftY+downOut.getWidth()
                g.drawLine(signTopLeftX,signTopLeftY+(int)(downOut.getWidth()/2),signTopLeftX+(int)(downOut.getWidth()/2),signTopLeftY+(int)downOut.getWidth());
            }
            //Down Lane is One Way Exit
            else if(tile.getCirculationDown() == Circulation.ONE_WAY_OUT){
                //Get Down In
                Rectangle downIn = this.city.getTileDownIn(tile);
                g.setColor(Color.BLACK);
                //Lane In
                g.fillRect((int)downIn.getX(),(int)downIn.getY(),(int)downIn.getWidth(),(int)downIn.getHeight());
                g.setColor(Color.WHITE);
                //Separation Line
                //StartX = downIn.getX()
                //StartY = downIn.getY()
                //EndX = downIn.getX()
                //EndY = downIn.getY()+downIn.getHeight()
                g.drawLine((int)downIn.getX(),(int)downIn.getY(),(int)downIn.getX(),(int)(downIn.getY()+downIn.getHeight()));
                //Mark at Center
                //StartX = downIn.getX()
                //StartY = downIn.getY()
                //EndX = downIn.getX()+downIn.getWidth()
                //EndY = downIn.getY()
                g.drawLine((int)downIn.getX(),(int)downIn.getY(),(int)(downIn.getX()+downIn.getWidth()),(int)downIn.getY());
                //Interdiction Sign
                //SignTopLeftX = downIn.getX()-downIn.getWidth()
                //SignTopLeftY = downIn.getY()
                //SignWidth = downIn.getWidth()
                //SignHeight = downIn.getWidth()
                int signTopLeftX = (int)(downIn.getX()-downIn.getWidth());
                int signTopLeftY = (int)downIn.getY();
                g.setColor(Color.RED);
                g.fillOval(signTopLeftX,signTopLeftY,(int)downIn.getWidth(),(int)downIn.getWidth());
                //Symbol -
                //StartX = signTopLeftX
                //StartY = signTopLeftY+(downIn.getWidth()/2)
                //EndX = signTopLeftX+downIn.getWidth
                //EndY = signTopLeftY+(downIn.getWidth()/2)
                g.setColor(Color.WHITE);
                g.drawLine(signTopLeftX,signTopLeftY+(int)(downIn.getWidth()/2),signTopLeftX+(int)downIn.getWidth(),signTopLeftY+(int)(downIn.getWidth()/2));
            }
        }
    }
    
    /**
     * Méthode drawTiles - Dessiner les Routes
     *
     * @see        CityViewInterface#drawTiles()
     * @param  g   Le Graphique
     * @return     void
     */
    public void drawTiles(Graphics g)
    {
        // Code
        RoadTile[][] cityGrid = this.city.getCityGrid();
        int GRID_WIDTH = this.city.getGridWidth();
        int GRID_HEIGHT = this.city.getGridHeight();
        for(int i=0;i<GRID_WIDTH;i++){
            for(int j=0;j<GRID_HEIGHT;j++){
                if(cityGrid[i][j] != null){
                    g.setColor(Color.BLACK);
                    this.drawTile(g, cityGrid[i][j]);
                }
            }
        }
    }
    
    /**
     * Méthode drawVehicle - Dessiner un Vehicule
     *
     * @param  g   Le Graphique
     * @param  car Le Vehicule
     * @return     void
     */
    public void drawVehicle(Graphics g, Car car)
    {
        // Code
        g.setColor(Color.RED);
        g.fillRect(car.getCarX(),car.getCarY(),car.getCarOrientedWidth(),car.getCarOrientedHeight());
    }
    
    /**
     * Méthode drawVehicles - Dessiner les Vehicules
     *
     * @see        CityViewInterface#drawVehicles()
     * @param  g   Le Graphique
     * @return     void
     */
    public void drawVehicles(Graphics g)
    {
        // Code
        List<Car> vehicleList = this.city.getVehicles();
        g.setColor(Color.RED);
        for(Car car : vehicleList){
            this.drawVehicle(g, car);
        }
        g.setColor(Color.BLACK);
    }
    
    /**
     * Methode setViewMouseListener - Ajouter Listener Clic au JPanel
     *
     * @param  al  Le ActionListener
     * @return     void
     */
    public void setViewMouseListener(MouseListener viewMouseListener){
        this.addMouseListener(viewMouseListener);
    }
    
    /**
     * Methode removeViewMouseListener - Retirer Listener Clic du JPanel
     *
     * @param  al  Le ActionListener
     * @return     void
     */
    public void removeViewMouseListener(MouseListener viewMouseListener){
        this.removeMouseListener(viewMouseListener);
    }
}
