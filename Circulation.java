
/**
 * Enumeration Circulation - Sens de Circulation sur les differents voies/branches de Route
 *
 * @author Anis Menaa William Mesnil
 * @version Version 6
 */
public enum Circulation
{
    NO_WAY, ONE_WAY_IN, ONE_WAY_OUT, TWO_WAY;
    
    /**
     * Methode matchCirculation - Renvoi le couple de Circulation qui permet connexion optimale rapport a celles passees en parametre
     *
     * @param  typeT La Connexion de Tile
     * @param  typeN La Connexion du Voisin
     * @param  auto  True si connexions sont forcees
     * @return       Le couple Connexion qui correspond le mieux
     */
    public static Circulation[] matchCirculation(Circulation typeA, Circulation typeB, boolean autoMode){
        Circulation[] match = new Circulation[2];
        //Neighbour is border or null
        if(typeB == null){
            //If New Tile passed as null, special code meaning Neighbour is Border
            if(typeA == null){
                //Checking AutoMode
                if(autoMode){
                    //AutoMode activated, forcing connexion
                    match[0] = Circulation.TWO_WAY;
                    match[1] = null;
                }
                //Keeping typeA as null so using method know connexion should not change
                else{
                match[0] = null;
                match[1] = null;
            }
            }
        }
        //AutoMode change Neighbour Value in order to adapt for the new Tile
        //AutoMode primary goal is pleasing new Tile configuration
        else if(autoMode){
            //New Tile is a Two Way
            if(typeA == Circulation.TWO_WAY){
                //AutoMode activated, changes Neighbour to match New Tile full Connexion
                match[0] = typeA;
                match[1] = typeA;
            }
            //New Tile is a One Way Entry
            else if(typeA == Circulation.ONE_WAY_IN){
                //AutoMode activated, changes Neighbour to be New Tile One Way Exit
                match[0] = typeA;
                match[1] = Circulation.ONE_WAY_OUT;
            }
            //New Tile is a One Way Exit
            else if(typeA == Circulation.ONE_WAY_OUT){
                //AutoMode activated, changes Neighbour to be New Tile One Way Entry
                match[0] = typeA;
                match[1] = Circulation.ONE_WAY_IN;
            }
            //New Tile is a No Way
            else{
                //Neighbour is a One Way Entry
                if(typeB == Circulation.ONE_WAY_IN){
                    //AutoMode activated, connect New Tile as One Way Exit
                    match[0] = Circulation.ONE_WAY_OUT;
                    match[1] = typeB;
                }
                //Neighbour is a One Way Exit
                else if(typeB == Circulation.ONE_WAY_OUT){
                    //AutoMode activated, connect New Tile as One Way Entry
                    match[0] = Circulation.ONE_WAY_IN;
                    match[1] = typeB;
                }
                //Neighbour is a Two Way or No Way
                else{
                    //AutoMode activated, connect the Tiles with largest possibe type
                    match[0] = Circulation.TWO_WAY;
                    match[1] = Circulation.TWO_WAY;
                }
            }
        }
        //AutoMode is off
        //Procede to no changes
        else{
            match[0] = typeA;
            match[1] = typeB;
        }
        return match;
    }
}
