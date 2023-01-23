import java.util.Arrays;

/**
 * classe EnumTools.
 *
 * @author William Mesnil
 * @version Version 6
 */
public class EnumTools
{
    //Method Found On : StackOverflow
    //Thread Name : Getting all names in a enum as a String[]
    //Method Author : Bohemian
    /**
     * Methode getNames - Recuperer Valeurs Enum forme String
     *
     * @author      Bohemian on StackOverflow
     * @param  e    La Classe de type Enum
     * @return      Les Valeurs de e sous forme de String
     */
    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}
