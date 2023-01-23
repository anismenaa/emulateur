import java.awt.Graphics;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/**
 * interface CityViewInterface.
 *
 * @author  Anis Menaa William Mesnil
 * @version Version 6
 */

public interface CityViewInterface
{
    /**
     * entete de Methode repaint - Dessiner les Cases, Routes et Voitures
     *
     * @return      void
     */
    public void repaint();
    /**
     * entete de Methode drawCity - Dessiner les Cases, Routes et Voitures
     *
     * @param  g    Le Graphique
     * @return      void
     */
    public void drawCity(Graphics g);
    /**
     * entete de Methode drawCells - Dessiner les Cases
     *
     * @param  g    Le Graphique
     * @return      void
     */
    public void drawCells(Graphics g);
    /**
     * entete de Methode drawTiles - Dessiner les Routes
     *
     * @param  g    Le Graphique
     * @return      void
     */
    public void drawTiles(Graphics g);
    /**
     * entete de Methode drawVehicles - Dessiner les Vehicules
     *
     * @param  g    Le Graphique
     * @return      void
     */
    public void drawVehicles(Graphics g);
    /**
     * entete de Methode setViewMouseListener - Ajouter Listener Clic au JPanel
     *
     * @param  al   Le ActionListener
     * @return      void
     */
    public void setViewMouseListener(MouseListener viewMouseListener);
    /**
     * entete de Methode removeViewMouseListener - Retirer Listener Clic du JPanel
     *
     * @param  al  Le ActionListener
     * @return     void
     */
    public void removeViewMouseListener(MouseListener viewMouseListener);
}
