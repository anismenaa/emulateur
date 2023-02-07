import java.awt.Color;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
import java.awt.GridLayout;

import java.awt.event.ActionListener;

import javax.swing.event.ChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * classe Menu.
 *
 * @author William Mesnil
 * @version Version 6
 */
public class Menu extends JPanel implements MenuInterface
{
    // variables d'instance
    private JButton saveButton;
    private JButton loadButton;
    private JButton quitButton;
    private JButton resetButton;
    
    private JSlider leftSlider;
    private JSlider upSlider;
    private JSlider rightSlider;
    private JSlider downSlider;
    
    private JRadioButton randomButton;
    private JRadioButton autoButton;
    
    //private JPanel preview;
    
    private Random random;
    private String[] choix; //Recuperer Valeurs de Enum Circulation

    /**
     * Constructeur d'objets de classe Menu
     */
    public Menu(int menuWidth,int menuHeight)
    {
        // initialisation des variables d'instance
        //gbc.gridx = x;
        //gbc.gridy = y;
        //gbc.gridwidth = w;
        //gbc.gridheight = h;
        super();
        //Add 10 because i cant make any Grid Layout work like i want
        this.setSize(menuWidth,menuHeight);
        this.setBounds(0,0,this.getWidth(),this.getHeight());
        this.setLayout(new GridLayout(10,1,0,0));
        this.setBackground(new Color(0,100,100));
        //set JSliders
        this.setJSliders();
        //Quit Button
        this.quitButton = new JButton("Quit");
        //Reset Button
        this.resetButton = new JButton("Reset");
        //Random Button
        this.randomButton = new JRadioButton("Rand");
        this.randomButton.setBackground(this.getBackground());
        this.randomButton.setHorizontalAlignment(JButton.CENTER);
        //Auto Button
        this.autoButton = new JRadioButton("Auto");
        this.autoButton.setBackground(this.getBackground());
        this.autoButton.setHorizontalAlignment(JButton.CENTER);
        
        this.random = new Random();
        
        //Regroup Quit and Reset
        JPanel qrPanel = new JPanel();
        qrPanel.setBackground(this.getBackground());
        qrPanel.add(this.quitButton);
        qrPanel.add(this.resetButton);
        
        //Regroup Random and Auto
        JPanel plusPanel = new JPanel();
        plusPanel.setBackground(this.getBackground());
        plusPanel.add(this.randomButton);
        plusPanel.add(this.autoButton);
        
        //POURQUOI?
        //GridBagConstraints gbc = new GridBagConstraints();
        
        //Left Label
        JPanel leftPanel = new JPanel(new GridLayout(2,1,0,0));
        leftPanel.setBackground(this.getBackground());
        JLabel leftLabel = new JLabel("Left Way");
        leftLabel.setHorizontalAlignment(JLabel.CENTER);
        leftLabel.setVerticalAlignment(JLabel.BOTTOM);
        leftPanel.add(leftLabel);
        leftPanel.add(this.leftSlider);
        
        //Up Label
        JPanel upPanel = new JPanel(new GridLayout(2,1,0,0));
        upPanel.setBackground(this.getBackground());
        JLabel upLabel = new JLabel("Up Way");
        upLabel.setHorizontalAlignment(JLabel.CENTER);
        upLabel.setVerticalAlignment(JLabel.BOTTOM);
        upPanel.add(upLabel);
        upPanel.add(this.upSlider);
        
        //Right Label
        JPanel rightPanel = new JPanel(new GridLayout(2,1,0,0));
        rightPanel.setBackground(this.getBackground());
        JLabel rightLabel = new JLabel("Right Way");
        rightLabel.setHorizontalAlignment(JLabel.CENTER);
        rightLabel.setVerticalAlignment(JLabel.BOTTOM);
        rightPanel.add(rightLabel);
        rightPanel.add(this.rightSlider);
        
        //Down Label
        JPanel downPanel = new JPanel(new GridLayout(2,1,0,0));
        downPanel.setBackground(this.getBackground());
        JLabel downLabel = new JLabel("Down Way");
        downLabel.setHorizontalAlignment(JLabel.CENTER);
        downLabel.setVerticalAlignment(JLabel.BOTTOM);
        downPanel.add(downLabel);
        downPanel.add(this.downSlider);
        
        this.add(qrPanel);
        this.add(leftPanel);
        this.add(upPanel);
        this.add(rightPanel);
        this.add(downPanel);
        this.add(plusPanel);
        this.setOpaque(true);
    }
    /**
     * Methode setJSliders - Definir les JSliders
     * 
     * @see        TutorialsPoint
     * @author     EnumTools.getNames From Bohemian on StackOverflow
     * @return     void
     */
    private void setJSliders(){
        //Get Names
        choix = EnumTools.getNames(Circulation.class);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        for(int i=0;i<choix.length;i++){
            labels.put(i, new JLabel(choix[i]));
        }
        int borne = choix.length;
        //Left JSlider
        this.leftSlider=new JSlider(0,(borne-1),1);
        this.leftSlider.setName("L");
        this.leftSlider.setMajorTickSpacing(1);
        this.leftSlider.setMinorTickSpacing(0);
        this.leftSlider.setLabelTable(labels);
        this.leftSlider.setPaintTicks(true);
        this.leftSlider.setPaintLabels(true);
        this.leftSlider.setInverted(false);
        this.leftSlider.setBackground(this.getBackground());
        //Up JSlider
        this.upSlider=new JSlider(0,(borne-1),1);
        this.upSlider.setName("U");
        this.upSlider.setMajorTickSpacing(1);
        this.upSlider.setMinorTickSpacing(0);
        this.upSlider.setLabelTable(labels);
        this.upSlider.setPaintTicks(true);
        this.upSlider.setPaintLabels(true);
        this.upSlider.setInverted(false);
        this.upSlider.setBackground(this.getBackground());
        //Right JSlider
        this.rightSlider=new JSlider(0,(borne-1),1);
        this.rightSlider.setName("R");
        this.rightSlider.setMajorTickSpacing(1);
        this.rightSlider.setMinorTickSpacing(0);
        this.rightSlider.setLabelTable(labels);
        this.rightSlider.setPaintTicks(true);
        this.rightSlider.setPaintLabels(true);
        this.rightSlider.setInverted(false);
        this.rightSlider.setBackground(this.getBackground());
        //Down JSlider
        this.downSlider=new JSlider(0,(borne-1),1);
        this.downSlider.setName("D");
        this.downSlider.setMajorTickSpacing(1);
        this.downSlider.setMinorTickSpacing(0);
        this.downSlider.setLabelTable(labels);
        this.downSlider.setPaintTicks(true);
        this.downSlider.setPaintLabels(true);
        this.downSlider.setInverted(false);
        this.downSlider.setBackground(this.getBackground());
    }
    /**
     * Methode getTileOptionsValues - Recuperer la valeur des Options Circulations
     *
     * @return     Les Options
     */
    public Circulation[] getTileOptionsValues(){
        Circulation[] options = new Circulation[4];
        System.out.println("GETTING MENU VALUE");
        if(!randomButton.isSelected()){
            System.out.println("RANDOM MODE : OFF");
            options[0] = Circulation.valueOf(Circulation.class,choix[this.leftSlider.getValue()]);
            options[1] = Circulation.valueOf(Circulation.class,choix[this.upSlider.getValue()]);
            options[2] = Circulation.valueOf(Circulation.class,choix[this.rightSlider.getValue()]);
            options[3] = Circulation.valueOf(Circulation.class,choix[this.downSlider.getValue()]);
        }else{
            System.out.println("RANDOM MODE : ON");
            int borne = choix.length;
            int randomLeft = this.random.nextInt(borne);
            int randomUp = this.random.nextInt(borne);
            int randomRight = this.random.nextInt(borne);
            int randomDown = this.random.nextInt(borne);
            options[0] = Circulation.valueOf(Circulation.class,choix[randomLeft]);
            options[1] = Circulation.valueOf(Circulation.class,choix[randomUp]);
            options[2] = Circulation.valueOf(Circulation.class,choix[randomRight]);
            options[3] = Circulation.valueOf(Circulation.class,choix[randomDown]);
        }
        System.out.println("LEFT : "+options[0]);
        System.out.println("UP : "+options[1]);
        System.out.println("RIGHT : "+options[2]);
        System.out.println("DOWN : "+options[3]);
        return options;
    }
    /**
     * Methode hasRandomOption - Dit si Bouton Random
     *
     * @return     True si Bouton Random
     */
    public boolean hasRandomOption(){
        return true;
    }
    /**
     * Methode getRandomOptionValue - Dit si Bouton Random selectionne (operation optionelle)
     *
     * @return     True si selectionne ou renvoie Exception si pas de Random dans Menu
     * @throws     UnsupportedOperationException
     */
    public boolean getRandomOptionValue(){
        return randomButton.isSelected();
    }
    /**
     * Methode hasAutoOption - Dit si Bouton Auto
     *
     * @return     True si Bouton Auto
     */
    public boolean hasAutoOption(){
        return true;
    }
    /**
     * Methode getAutoOptionValue - Dit si Bouton Auto selectionne (operation optionelle)
     *
     * @return     True si selectionne ou renvoie Exception si pas de Auto dans Menu
     * @throws     UnsupportedOperationException
     */
    public boolean getAutoOptionValue(){
        return autoButton.isSelected();
    }
    /**
     * Methode setEnabledTileOptions - Activer ou Desactiver Modification Options
     *
     * @param  bool La Valeur
     * @return      void
     */
    public void setEnabledTileOptions(boolean bool){
        leftSlider.setEnabled(bool);
        upSlider.setEnabled(bool);
        rightSlider.setEnabled(bool);
        downSlider.setEnabled(bool);
    }
    /**
     * Methode supportTileOptionsActionListener - Dit si Composants qui representent les Options Circulations supportent ActionListener
     *
     * @return     True si addActionListener disponible et False si autre type de Listener
     */
    public boolean supportTileOptionsActionListener(){
        return false;
    }
    /**
     * Methode supportTileOptionsChangeListener - Dit si Composants qui representent les Options Circulations supportent ChangeListener
     *
     * @return     True si addChangeListener disponible et False si autre type de Listener
     */
    public boolean supportTileOptionsChangeListener(){
        return true;
    }
    /**
     * Methode getTileOptionsComponents - Recuperer les Composants qui representent les Options Circulations
     *
     * @return     Les Composants Options
     */
    public List<JComponent> getTileOptionsComponents(){
        List<JComponent> components = new ArrayList<JComponent>();
        components.add(leftSlider);
        components.add(upSlider);
        components.add(rightSlider);
        components.add(downSlider);
        return components;
    }
    /**
     * Methode getTileOptionsComponentsClass - Recuperer la Classe des Composants qui representent les Options Circulations
     *
     * @return     La Classe des Composants Options
     */
    public Class getTileOptionsComponentsClass(){
        return JSlider.class;
    }
    /**
     * Methode setMenuQuitListener - Ajouter Listener au Bouton qui represente Commande Quitter
     *
     * @param  al  Le ActionListener
     * @return     void
     */
    public void setMenuQuitListener(ActionListener menuQuitListener){
        this.quitButton.addActionListener(menuQuitListener);
    }
    /**
     * Methode removeMenuQuitListener - Retirer Listener au Bouton qui represente Commande Quitter
     *
     * @param  a   Le ActionListener
     * @return     void
     */
    public void removeMenuQuitListener(ActionListener menuQuitListener){
        this.quitButton.removeActionListener(menuQuitListener);
    }
    /**
     * Methode setMenuResetListener - Ajouter Listener au Bouton qui represente Commande Reset
     *
     * @param  al  Le ActionListener
     * @return     void
     */
    public void setMenuResetListener(ActionListener menuResetListener){
        this.resetButton.addActionListener(menuResetListener);
    }
    /**
     * Methode removeMenuResetListener - Retirer Listener au Bouton qui represente Commande Reset
     *
     * @param  al  Le ActionListener
     * @return     void
     */
    public void removeMenuResetListener(ActionListener menuResetListener){
        this.resetButton.removeActionListener(menuResetListener);
    }
    /**
     * Methode setMenuOptionsListener - Ajouter Listener aux Composants qui representent les Options Circulations (operation optionelle)
     * Implementer cette Methode conduira probablement a ne pas utiliser la version pour ChangeListener
     *
     * @param  al  Le ActionListener
     * @return     void
     * @throws     UnsupportedOperationException
     */
    public void setMenuOptionsListener(ActionListener menuOptionsListener){
        throw new UnsupportedOperationException("This Menu uses JSlider as Options Components");
    }
    /**
     * Methode removeMenuOptionsListener - Retirer Listener aux Composants qui representent les Options Circulations (operation optionelle)
     * Implementer cette Methode conduira probablement a ne pas utiliser la version pour ChangeListener
     *
     * @param  al  Le ActionListener
     * @return     void
     * @throws     UnsupportedOperationException
     */
    public void removeMenuOptionsListener(ActionListener menuOptionsListener){
        throw new UnsupportedOperationException("This Menu uses JSlider as Options Components");
    }
    /**
     * Methode setMenuOptionsListener - Ajouter Listener aux Composants qui representent les Options Circulations (operation optionelle)
     * Implementer cette Methode conduira probablement a ne pas utiliser la version pour ActionListener
     * 
     * @param  cl  Le ChangeListener
     * @return     void
     * @throws     UnsupportedOperationException
     */
    public void setMenuOptionsListener(ChangeListener menuOptionsListener){
        this.leftSlider.addChangeListener(menuOptionsListener);
        this.upSlider.addChangeListener(menuOptionsListener);
        this.rightSlider.addChangeListener(menuOptionsListener);
        this.downSlider.addChangeListener(menuOptionsListener);
    }
    /**
     * Methode removeMenuOptionsListener - Retirer Listener aux Composants qui representent les Options Circulations (operation optionelle)
     * Implementer cette Methode conduira probablement a ne pas utiliser la version pour ActionListener
     *
     * @param  cl  Le ChangeListener
     * @return     void
     * @throws     UnsupportedOperationException
     */
    public void removeMenuOptionsListener(ChangeListener menuOptionsListener){
        this.leftSlider.removeChangeListener(menuOptionsListener);
        this.upSlider.removeChangeListener(menuOptionsListener);
        this.rightSlider.removeChangeListener(menuOptionsListener);
        this.downSlider.removeChangeListener(menuOptionsListener);
    }
    /**
     * Methode setMenuAutoListener - Ajouter Listener Bouton qui represente Auto (operation optionelle)
     *
     * @param  al  Le ActionListener
     * @return     void
     * @throws     UnsupportedOperationException
     */
    public void setMenuAutoListener(ActionListener menuAutoListener){
        this.autoButton.addActionListener(menuAutoListener);
    }
    /**
     * Methode removeMenuAutoListener - Retirer Listener Bouton qui represente Auto (operation optionelle)
     *
     * @param  al  Le ActionListener
     * @return     void
     * @throws     UnsupportedOperationException
     */
    public void removeMenuAutoListener(ActionListener menuAutoListener){
        this.autoButton.removeActionListener(menuAutoListener);
    }
    /**
     * Methode setMenuRandomListener - Ajouter Listener au Bouton qui represente Option Random (operation optionelle)
     *
     * @param  al  Le ActionListener
     * @return     void
     * @throws     UnsupportedOperationException
     */
    public void setMenuRandomListener(ActionListener menuRandomListener){
        this.randomButton.addActionListener(menuRandomListener);
    }
    /**
     * Methode removeMenuRandomListener - Retirer Listener au Bouton qui represente Option Random (operation optionelle)
     *
     * @param  al  Le ActionListener
     * @return     void
     * @throws     UnsupportedOperationException
     */
    public void removeMenuRandomListener(ActionListener menuRandomListener){
        this.randomButton.removeActionListener(menuRandomListener);
    }
}
