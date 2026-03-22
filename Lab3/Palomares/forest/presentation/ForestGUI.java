package presentation;
import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ForestGUI extends JFrame{  
    public static final int SIDE=20;

    public final int SIZE;
    private JButton ticTacButton;
    private JPanel  controlPanel;
    private PhotoForest photo;
    private Forest theForest;
   
    
    private ForestGUI() {
        theForest=new Forest();
        SIZE=theForest.getSize();
        prepareElements();
        prepareActions();
    }
    
    private void prepareElements() {
        setTitle("Schelling Forest");
        photo=new PhotoForest(this);
        ticTacButton=new JButton("Tic-tac");
        setLayout(new BorderLayout());
        add(photo,BorderLayout.NORTH);
        add(ticTacButton,BorderLayout.SOUTH);
        setSize(new Dimension(SIDE*SIZE+15,SIDE*SIZE+72)); 
        setResizable(false);
        photo.repaint();
    }

    private void prepareActions(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);       
        ticTacButton.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    ticTacButtonAction();
                }
            });

    }

    private void ticTacButtonAction() {
        theForest.ticTac();
        photo.repaint();
    }

    public Forest gettheForest(){
        return theForest;
    }
    
    public static void main(String[] args) {
        ForestGUI cg=new ForestGUI();
        cg.setVisible(true);
    }  
}

class PhotoForest extends JPanel{
    private ForestGUI gui;

    public PhotoForest(ForestGUI gui) {
        this.gui=gui;
        setBackground(Color.white);
        setPreferredSize(new Dimension(ForestGUI.SIDE*gui.SIZE+10, ForestGUI.SIDE*gui.SIZE+10));         
    }


    public void paintComponent(Graphics g){
        Forest theForest=gui.gettheForest();
        super.paintComponent(g);
         
        for (int c=0;c<=theForest.getSize();c++){
            g.drawLine(c*ForestGUI.SIDE,0,c*ForestGUI.SIDE,theForest.getSize()*ForestGUI.SIDE);
        }
        for (int f=0;f<=theForest.getSize();f++){
            g.drawLine(0,f*ForestGUI.SIDE,theForest.getSize()*ForestGUI.SIDE,f*ForestGUI.SIDE);
        }
        List<Integer> shadowAffected = new ArrayList<>();
        for (int f=0;f<theForest.getSize();f++){
            for(int c=0;c<theForest.getSize();c++){
                if (theForest.getThing(f,c)!=null){
                    g.setColor(theForest.getThing(f,c).getColor());
                    int shape = theForest.getThing(f, c).shape();
                    if (shape!=Thing.ROUND){                  
                        g.fillRoundRect(ForestGUI.SIDE*c+1,ForestGUI.SIDE*f+1,ForestGUI.SIDE-2,ForestGUI.SIDE-2,2,2);  
                        if (shape == Thing.SHADOW) {
                            shadowAffected.add(f);
                            g.setColor(new Color(0, 0, 0, 120));
                            g.fillRect(1,ForestGUI.SIDE*f+1,ForestGUI.SIDE*c-2,ForestGUI.SIDE-2);
                        } 
                    } else {
                        g.fillOval(ForestGUI.SIDE*c+1,ForestGUI.SIDE*f+1,ForestGUI.SIDE-2,ForestGUI.SIDE-2);
                    }
                    if (theForest.getThing(f,c).isLivingThing()){
                        g.setColor(Color.red);
                        if (((LivingThing)theForest.getThing(f,c)).getEnergy()>=50){
                            g.drawString("+",ForestGUI.SIDE*c+6,ForestGUI.SIDE*f+15);
                        } else {
                            g.drawString("~",ForestGUI.SIDE*c+6,ForestGUI.SIDE*f+17);
                        }
                    }
                }
                if (shadowAffected.contains(f)) {
                    g.setColor(new Color(0, 0, 0, 120));
                    g.fillRect(ForestGUI.SIDE*c+1,ForestGUI.SIDE*f+1,ForestGUI.SIDE-1,ForestGUI.SIDE-2);
                }
            }
        }
    }
}