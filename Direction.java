
/**
 * Enumeration Direction - La Direction du Vehicule
 *
 * @author Anis Menaa
 * @version Version 6
 */
public enum Direction
{
    LEFT(-1,0), UP(0,-1), RIGHT(1,0), DOWN(0,1);
    
    private int x;
    private int y;
    
    Direction(int x,int y)
    {
        this.x=x;
        this.y=y;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
}
