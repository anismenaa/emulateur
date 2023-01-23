import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import javax.swing.event.ChangeListener;

import java.util.List;

/**
 * interface MenuInterface.
 *
 * @author  Anis Menaa William Mesnil
 * @version Version 5
 */

public interface MenuInterface
{
    /**
     * entete de Methode getTileOptionsValues - Recuperer la valeur des Options Circulations
     *
     * @return        Les Options
     */
    public Circulation[] getTileOptionsValues();
    /**
     * entete de Methode hasRandomOption - Dit si Bouton Random
     *
     * @return        True si Bouton Random
     */
    public boolean hasRandomOption();
    /**
     * entete de Methode getRandomOptionValue - Dit si Bouton Random selectionne (operation optionelle)
     *
     * @return        True si selectionne ou renvoie Exception si pas de Random dans Menu
     * @throws        UnsupportedOperationException
     */
    public boolean getRandomOptionValue();
    /**
     * entete de Methode hasAutoOption - Dit si Bouton Auto
     *
     * @return        True si Bouton Auto
     */
    public boolean hasAutoOption();
    /**
     * entete de Methode getAutoOptionValue - Dit si Bouton Auto selectionne (operation optionelle)
     *
     * @return        True si selectionne ou renvoie Exception si pas de Auto dans Menu
     * @throws        UnsupportedOperationException
     */
    public boolean getAutoOptionValue();
    /**
     * entete de Methode setEnabledTileOptions - Activer ou Desactiver Modification Options
     *
     * @param  bool   La Valeur
     * @return        void
     */
    public void setEnabledTileOptions(boolean bool);
    /**
     * entete de Methode supportTileOptionsActionListener - Dit si Composants qui representent les Options Circulations supportent ActionListener
     *
     * @return        True si addActionListener disponible et False si autre type de Listener
     */
    public boolean supportTileOptionsActionListener();
    /**
     * entete de Methode supportTileOptionsChangeListener - Dit si Composants qui representent les Options Circulations supportent ChangeListener
     *
     * @return        True si addChangeListener disponible et False si autre type de Listener
     */
    public boolean supportTileOptionsChangeListener();
    /**
     * entete de Methode getTileOptionsComponents - Recuperer les Composants qui representent les Options Circulations
     *
     * @return        Les Composants Options
     */
    public List<JComponent> getTileOptionsComponents();
    /**
     * entete de Methode getTileOptionsComponentsClass - Recuperer la Classe des Composants qui representent les Options Circulations
     *
     * @return        La Classe des Composants Options
     */
    public Class getTileOptionsComponentsClass();
    /**
     * entete de Methode setMenuQuitListener - Ajouter Listener au Bouton qui represente Commande Quitter
     *
     * @param  al     Le ActionListener
     * @return        void
     */
    public void setMenuQuitListener(ActionListener menuQuitListener);
    /**
     * entete de Methode removeMenuQuitListener - Retirer Listener au Bouton qui represente Commande Quitter
     *
     * @param  al     Le ActionListener
     * @return        void
     */
    public void removeMenuQuitListener(ActionListener menuQuitListener);
    /**
     * entete de Methode setMenuResetListener - Ajouter Listener au Bouton qui represente Commande Reset
     *
     * @param  al     Le ActionListener
     * @return        void
     */
    public void setMenuResetListener(ActionListener menuResetListener);
    /**
     * entete de Methode removeMenuResetListener - Retirer Listener au Bouton qui represente Commande Reset
     *
     * @param  al     Le ActionListener
     * @return        void
     */
    public void removeMenuResetListener(ActionListener menuResetListener);
    /**
     * entete de Methode setMenuOptionsListener - Ajouter Listener aux Composants qui representent les Options Circulations (operation optionelle)
     * Implementer cette Methode conduira probablement a ne pas utiliser la version pour ChangeListener
     *
     * @param  al     Le ActionListener
     * @return        void
     * @throws        UnsupportedOperationException
     */
    public void setMenuOptionsListener(ActionListener menuOptionsListener);
    /**
     * entete de Methode removeMenuOptionsListener - Retirer Listener aux Composants qui representent les Options Circulations (operation optionelle)
     * Implementer cette Methode conduira probablement a ne pas utiliser la version pour ChangeListener
     *
     * @param  al     Le ActionListener
     * @return        void
     * @throws        UnsupportedOperationException
     */
    public void removeMenuOptionsListener(ActionListener menuOptionsListener);
    /**
     * entete de Methode setMenuOptionsListener - Ajouter Listener aux Composants qui representent les Options Circulations (operation optionelle)
     * Implementer cette Methode conduira probablement a ne pas utiliser la version pour ActionListener
     * 
     * @param  cl     Le ChangeListener
     * @return        void
     * @throws        UnsupportedOperationException
     */
    public void setMenuOptionsListener(ChangeListener menuOptionsListener);
    /**
     * entete de Methode removeMenuOptionsListener - Retirer Listener aux Composants qui representent les Options Circulations (operation optionelle)
     * Implementer cette Methode conduira probablement a ne pas utiliser la version pour ActionListener
     *
     * @param  cl     Le ChangeListener
     * @return        void
     * @throws        UnsupportedOperationException
     */
    public void removeMenuOptionsListener(ChangeListener menuOptionsListener);
    /**
     * entete de Methode setMenuAutoListener - Ajouter Listener Bouton qui represente Auto (operation optionelle)
     *
     * @param  al     Le ActionListener
     * @return        void
     * @throws        UnsupportedOperationException
     */
    public void setMenuAutoListener(ActionListener menuAutoListener);
    /**
     * entete de Methode removeMenuAutoListener - Retirer Listener Bouton qui represente Auto (operation optionelle)
     *
     * @param  al     Le ActionListener
     * @return        void
     * @throws        UnsupportedOperationException
     */
    public void removeMenuAutoListener(ActionListener menuAutoListener);
    /**
     * entete de Methode setMenuRandomListener - Ajouter Listener au Bouton qui represente Option Random (operation optionelle)
     *
     * @param  al     Le ActionListener
     * @return        void
     * @throws        UnsupportedOperationException
     */
    public void setMenuRandomListener(ActionListener menuRandomListener);
    /**
     * entete de Methode removeMenuRandomListener - Retirer Listener au Bouton qui represente Option Random (operation optionelle)
     *
     * @param  al     Le ActionListener
     * @return        void
     * @throws        UnsupportedOperationException
     */
    public void removeMenuRandomListener(ActionListener menuRandomListener);
}
