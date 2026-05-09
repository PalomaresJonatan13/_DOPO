package presentation;
import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana principal del simulador de bosque: menú de archivos, botón tic-tac y panel de dibujo.
 */
public class ForestGUI extends JFrame {  
    /** Píxeles por lado de celda en la cuadrícula. */
    public static final int SIDE=20;

    /** Tamaño del bosque (coincide con {@link Forest#getSize()} del modelo inicial). */
    public final int SIZE;
    private JButton ticTacButton;
    private PhotoForest photo;
    private Forest theForest;

    private JMenuItem newFile;
    private JMenuItem open;
    private JMenuItem saveAs;
    private JMenuItem importFile;
    private JMenuItem exportFile;
    private JMenuItem exit;
   
    /**
     * Construye la GUI con un {@link Forest} nuevo y configura componentes y acciones.
     */
    private ForestGUI() {
        theForest=new Forest();
        SIZE=theForest.getSize();
        prepareElements();
        prepareActions();
    }
    
    /**
     * Crea controles, menú y dimensiones de la ventana.
     */
    private void prepareElements() {
        setTitle("Schelling Forest");
        photo=new PhotoForest(this);
        ticTacButton=new JButton("Tic-tac");
        setLayout(new BorderLayout());
        add(photo,BorderLayout.CENTER);
        add(ticTacButton,BorderLayout.SOUTH);
        add(this.prepareElementsMenu(), BorderLayout.NORTH);

        setSize(new Dimension(SIDE*SIZE+15,SIDE*SIZE+72+20)); 
        setResizable(false);
        photo.repaint();
    }

    /**
     * @return barra de menú con entradas de archivo
     */
    private JMenuBar prepareElementsMenu() {
        JMenu menu = new JMenu("Menú");
        JMenuBar menuBar = new JMenuBar();

        this.newFile    =  new JMenuItem("Nuevo");
        this.open       =  new JMenuItem("Abrir");
        this.saveAs     = new JMenuItem("Guardar como");
        this.importFile = new JMenuItem("Importar");
        this.exportFile = new JMenuItem("Exportar como");
        this.exit       = new JMenuItem("Salir");

        menu.add(newFile);
        menu.add(open);
        menu.add(saveAs);
        menu.add(importFile);
        menu.add(exportFile);
        menu.add(exit);

        menuBar.add(menu);
        return menuBar;
    }

    /**
     * Registra listeners del botón y del menú.
     */
    private void prepareActions(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);       
        ticTacButton.addActionListener(
            new ActionListener(){
                /** {@inheritDoc} */
                public void actionPerformed(ActionEvent e) {
                    ticTacButtonAction();
                }
            }
        );
        this.prepareActionsMenu();
    }

    /**
     * Asocia cada ítem del menú a su manejador.
     */
    private void prepareActionsMenu() {
        this.newFile.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent event) {
                optionNew();
            }
        });

        this.open.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent event) {
                optionOpen();
            }
        });

        this.saveAs.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent event) {
                optionSaveAs();
            }
        });

        this.importFile.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent event) {
                optionImport();
            }
        });

        this.exportFile.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent event) {
                optionExportAs();
            }
        });

        this.exit.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent event) {
                optionExit();
            }
        });
    }

    /**
     * Reinicia el modelo con un {@link Forest} nuevo y redibuja.
     */
    private void optionNew() {
        theForest = new Forest();
        photo.repaint();
    }

    /**
     * Abre un .dat y reemplaza el bosque actual; muestra error en diálogo si falla.
     */
    private void optionOpen() {
        File file = chooseFile();
        if (file != null) {
            try {
                theForest = theForest.openFile(file);
                photo.repaint();
            } catch (ForestException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    /**
     * Guarda el bosque en el archivo elegido (.dat).
     */
    private void optionSaveAs() {
        File file = chooseFile();
        if (file != null) {
            try {
                theForest.saveAs(file);
            } catch (ForestException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    /**
     * Importa estado desde un .txt y redibuja.
     */
    private void optionImport() {
        File file = chooseFile();
        if (file != null) {
            try {
                theForest.importFile(file);
                photo.repaint();
            } catch (ForestException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    /**
     * Exporta el estado actual a un .txt.
     */
    private void optionExportAs() {
        File file = chooseFile();
        if (file != null) {
            try {
                theForest.exportAs(file);
            } catch (ForestException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    /**
     * Cierra la aplicación.
     */
    private void optionExit() {
        System.exit(0);
    }

    /**
     * Muestra un diálogo de selección de archivo.
     * @return archivo elegido o {@code null} si se cancela
     */
    private File chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int fileResult = fileChooser.showOpenDialog(null);
        if (fileResult == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Ejecuta un tic-tac global y refresca el panel.
     */
    private void ticTacButtonAction() {
        theForest.ticTac();
        photo.repaint();
    }

    /**
     * @return bosque mostrado actualmente
     */
    public Forest gettheForest(){
        return theForest;
    }
    
    /**
     * Punto de entrada: crea la ventana y la hace visible.
     * @param args argumentos de línea de órdenes (no usados)
     */
    public static void main(String[] args) {
        ForestGUI cg=new ForestGUI();
        cg.setVisible(true);
    }  
}

/**
 * Panel que dibuja la cuadrícula del bosque y los {@link Thing} con forma y color.
 */
class PhotoForest extends JPanel{
    private ForestGUI gui;

    /**
     * @param gui ventana propietaria (para tamaño y modelo)
     */
    public PhotoForest(ForestGUI gui) {
        this.gui=gui;
        setBackground(Color.white);
        setPreferredSize(new Dimension(ForestGUI.SIDE*gui.SIZE+10, ForestGUI.SIDE*gui.SIZE+10));         
    }

    /**
     * Dibuja líneas de cuadrícula y rellena cada celda según el {@link Thing} presente.
     * @param g contexto gráfico
     */
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
