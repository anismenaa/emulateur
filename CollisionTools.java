import java.awt.Rectangle;

/**
 * classe CollisionTools.
 *
 * @author William Mesnil Anis Menaa
 * @version Version 6
 */
public class CollisionTools
{
    //Created after Questions and Research on Collision for Unfixed Origin Shapes
    //Informative Thread : StackOverflow - How do negative sized rectangles intersect
    /**
     * Methode classicRectToRectCollision - Calculer si deux Rectangles Collisionnent selon Algo classique
     *
     * @param  boxA Le Premier Rectangle representant objet a Tester
     * @param  boxB Le Second Rectangle representant objet a Tester
     * @return      True si les Rectangles Collisionnent
     */
    public static boolean classicRectToRectCollision(Rectangle hitboxA, Rectangle hitboxB)
    {
        //Suppose Origin is top left corner and Dimensions are Positive
        if((hitboxA.getX() < hitboxB.getX()+hitboxB.getWidth())
        &&(hitboxA.getX()+hitboxA.getWidth() > hitboxB.getX())
        &&(hitboxA.getY() < hitboxB.getY()+hitboxB.getHeight())
        &&(hitboxA.getY()+hitboxA.getHeight() > hitboxB.getY())){
            return true;
        }
        return false;
    }
    /**
     * Methode extendedRectToRectCollision - Calculer si deux Rectangles Collisionnent selon Algo modifie
     *
     * @param  boxA Le Premier Rectangle representant objet a Tester
     * @param  boxB Le Second Rectangle representant objet a Tester
     * @return      True si les Rectangles Collisionnent
     */
    public static boolean extendedRectToRectCollision(Rectangle hitboxA, Rectangle hitboxB)
    {
        //Suppose Origin can be any corner and Dimensions can be negative
        //Set infos for hitboxA
        double aLeftSide = Math.min(hitboxA.getX(),hitboxA.getX()+hitboxA.getWidth());
        double aSupSide = Math.min(hitboxA.getY(),hitboxA.getY()+hitboxA.getHeight());
        double aRightSide = Math.max(hitboxA.getX(),hitboxA.getX()+hitboxA.getWidth());
        double aInfSide = Math.max(hitboxA.getY(),hitboxA.getY()+hitboxA.getHeight());
        //Set infos for hitboxB
        double bLeftSide = Math.min(hitboxB.getX(),hitboxB.getX()+hitboxB.getWidth());
        double bSupSide = Math.min(hitboxB.getY(),hitboxB.getY()+hitboxB.getHeight());
        double bRightSide = Math.max(hitboxB.getX(),hitboxB.getX()+hitboxB.getWidth());
        double bInfSide = Math.max(hitboxB.getY(),hitboxB.getY()+hitboxB.getHeight());
        //Do classic Algo with now safe Values
        if((aLeftSide < bRightSide)
        &&(aRightSide > bLeftSide)
        &&(aSupSide < bInfSide)
        &&(aInfSide > bSupSide)){
            return true;
        }
        return false;
    }
    /**
     * Methode hasReachCenterSideFrom - Calculer si un objet represente par Rectangle a atteint cote de autre Rectangle depuis son cote correspondant a sa Direction
     *
     * @param  box  Le Rectangle representant Objet a Tester
     * @param  ctr  Le Rectangle representant Objectif
     * @param  dir  La Direction par laquelle Objet tente de rejoindre la Bordure de Objectif
     * @return      True si les Rectangles Collisionnent
     */
    public static boolean hasReachCenterSideFrom(Rectangle hitbox, Rectangle center, Direction from){
        //Suppose Origin can be any corner and Dimensions can be negative
        //If Object move to the Left, test Object Left Side < Goal Right Side
        if(from == Direction.LEFT){
            //Set Used Info
            double objectLeftSide = Math.min(hitbox.getX(),hitbox.getX()+hitbox.getWidth());
            double goalRightSide = Math.max(center.getX(),center.getX()+center.getWidth());
            //Compute
            if(objectLeftSide < goalRightSide){
                return true;
            }
            return false;
        }
        //If Object move to the Top, test Object Upper Side < Goal Lower Side
        else if(from == Direction.UP){
            //Set Used Info
            double objectSupSide = Math.min(hitbox.getY(),hitbox.getY()+hitbox.getHeight());
            double goalInfSide = Math.max(center.getY(),center.getY()+center.getHeight());
            //Compute
            if(objectSupSide < goalInfSide){
                return true;
            }
            return false;
        }
        //If Object move to the Right, test Object Right Side > Goal Left Side
        else if(from == Direction.RIGHT){
            //Set Used Info
            double objectRightSide = Math.max(hitbox.getX(),hitbox.getX()+hitbox.getWidth());
            double goalLeftSide = Math.min(center.getX(),center.getX()+center.getWidth());
            //Compute
            if(objectRightSide > goalLeftSide){
                return true;
            }
            return false;
        }
        //If Object move to the Bottom, test Object Lower Side > Goal Upper Side
        else if(from == Direction.DOWN){
            //Set Used Info
            double objectInfSide = Math.max(hitbox.getY(),hitbox.getY()+hitbox.getHeight());
            double goalSupSide = Math.min(center.getY(),center.getY()+center.getHeight());
            //Compute
            if(objectInfSide > goalSupSide){
                return true;
            }
            return false;
        }
        //Unsupported Direction
        System.out.println("Unsupported Multi Component Direction");
        return false;
    }
    /**
     * Methode isApprochingFrom - Calculer si un objet represente par Rectangle se rapproche ou eloigne Rectangle avec sa Direction Actuelle
     *
     * @param  box  Le Rectangle representant Objet a Tester
     * @param  ctr  Le Rectangle representant Objectif
     * @param  dir  La Direction par laquelle Objet se Deplace
     * @return      True si les Rectangles Collisionnent
     */
    public static boolean isApprochingFrom(Rectangle hitbox, Rectangle center, Direction from){
        //Suppose Origin can be any corner and Dimensions can be negative
        //If Object move to the Left, test if Object is approchig from the Right with Object Left Side > Goal Right Side
        if(from == Direction.LEFT){
            //Set Used Info
            double objectLeftSide = Math.min(hitbox.getX(),hitbox.getX()+hitbox.getWidth());
            double goalRightSide = Math.max(center.getX(),center.getX()+center.getWidth());
            //Compute
            if(objectLeftSide > goalRightSide){
                return true;
            }
            return false;
        }
        //If Object move to the Top, test if Object is approchig from the Bottom with Object Upper Side > Goal Lower Side
        else if(from == Direction.UP){
            //Set Used Info
            double objectSupSide = Math.min(hitbox.getY(),hitbox.getY()+hitbox.getHeight());
            double goalInfSide = Math.max(center.getY(),center.getY()+center.getHeight());
            //Compute
            if(objectSupSide > goalInfSide){
                return true;
            }
            return false;
        }
        //If Object move to the Right, test if Object is approchig from the Left with Object Right Side < Goal Left Side
        else if(from == Direction.RIGHT){
            //Set Used Info
            double objectRightSide = Math.max(hitbox.getX(),hitbox.getX()+hitbox.getWidth());
            double goalLeftSide = Math.min(center.getX(),center.getX()+center.getWidth());
            //Compute
            if(objectRightSide < goalLeftSide){
                return true;
            }
            return false;
        }
        //If Object move to the Bottom, test if Object is approachin from the Top with Object Lower Side < Goal Upper Side
        else if(from == Direction.DOWN){
            //Set Used Info
            double objectInfSide = Math.max(hitbox.getY(),hitbox.getY()+hitbox.getHeight());
            double goalSupSide = Math.min(center.getY(),center.getY()+center.getHeight());
            //Compute
            if(objectInfSide < goalSupSide){
                return true;
            }
            return false;
        }
        //Unsupported Direction
        System.out.println("Unsupported Multi Component Direction");
        return false;
    }
}
