package presentation;
import domain.*;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Swing user interface for playing Easy Sokoban.
 */
public class SokobanGUI extends JFrame {
    private JMenuItem menuItemNuevo;
    private JMenuItem menuItemAbrir;
    private JMenuItem menuItemSalvar;
    private JMenuItem menuItemSalir;
    private JMenuItem menuItemReiniciar;
    private HashMap<Integer, JMenuItem> colorItems = new HashMap<>();
    private JLabel warningText;
    private Board board;
    private EasySokoban sokoban;
    public static Dimension DEFAULT_DIMENSIONS;

    /**
     * Creates and initializes the Sokoban GUI.
     */
    public SokobanGUI() {
        super("EasySokoban");
        this.sokoban = new EasySokoban();
        this.prepareElements();
        this.prepareActions();
    }

    static {
        final Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        DEFAULT_DIMENSIONS = new Dimension(
            (int) screenDimensions.getWidth()/2,
            (int) screenDimensions.getHeight()/2
        );
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Creates and arranges all visual components.
     */
    private void prepareElements() {
        setSize(DEFAULT_DIMENSIONS);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        this.add(this.prepareElementsMenu(), BorderLayout.NORTH);
        this.add(this.prepareElementsBoard(), BorderLayout.CENTER);
        this.add(this.prepareElementsWarning(), BorderLayout.SOUTH);
    }

    /**
     * Builds the main menu bar and all menu items.
     *
     * @return configured menu bar.
     */
    private JMenuBar prepareElementsMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu("Menu");
        JMenu colorsMenu = new JMenu("Colores");

        menuItemNuevo = new JMenuItem("Nuevo");
        menuItemAbrir = new JMenuItem("Abrir");
        menuItemSalvar = new JMenuItem("Salvar");
        menuItemSalir = new JMenuItem("Salir");
        menuItemReiniciar = new JMenuItem("Reiniciar");

        mainMenu.add(menuItemNuevo);
        mainMenu.add(menuItemAbrir);
        mainMenu.add(menuItemSalvar);
        mainMenu.add(menuItemReiniciar);
        mainMenu.add(menuItemSalir);

        EasySokoban.getTypes().forEach((key, value) -> {
            colorItems.put(value, new JMenuItem(key));
            colorsMenu.add(colorItems.get(value));
        });

        menuBar.add(mainMenu);
        menuBar.add(colorsMenu);

        return menuBar;
    }

    /**
     * Builds and returns the board panel.
     *
     * @return board panel used to render the game state.
     */
    private JPanel prepareElementsBoard() {
        this.board = new Board(this.sokoban);
        board.setFocusable(true);
        board.requestFocusInWindow();

        return this.board;
    }

    /**
     * Builds the status label shown at the bottom of the window.
     *
     * @return warning/status label.
     */
    private JLabel prepareElementsWarning() {
        warningText = new JLabel("Play with the arrow keys.");
        return warningText;
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Registers global window actions and delegates menu action wiring.
     */
    private void prepareActions() {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev){
                exit("Do you really want to exit the game?");
            }
        });

        this.prepareActionsMenu();
    }

    /**
     * Registers all menu and keyboard actions.
     */
    private void prepareActionsMenu() {
        JFrame sokobanGUI = this;
        menuItemSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                exit("You are about to exit the game. If you haven't saved the game, save it before you exit. Do you really want to exit?");
            }
        });

        menuItemAbrir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                int fileSelectionResult = fileChooser.showOpenDialog(sokobanGUI);
                if (fileSelectionResult == JFileChooser.APPROVE_OPTION) {
                    File fileSelected = fileChooser.getSelectedFile();
                    JOptionPane.showMessageDialog(sokobanGUI, "The option to open a saved game from a file is still in construction. If your game is in "+fileSelected.getName()+" , don't worry, we will make this option avaiable soon!");
                }
            }
        });

        menuItemSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                int fileSelectionResult = fileChooser.showSaveDialog(sokobanGUI);
                if (fileSelectionResult == JFileChooser.APPROVE_OPTION) {
                    File fileSelected = fileChooser.getSelectedFile();
                    JOptionPane.showMessageDialog(sokobanGUI, "The option to save a game in a file is still in construction. We are working to let you save your game in "+fileSelected.getName()+".");
                }
            }
        });

        menuItemReiniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sokoban.reset();
                board.setDefaultColors();
                board.repaint();
                warningText.setText("The board was reset.");
            }
        });

        colorItems.forEach((type, item) -> {
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    Map<Integer, Color> colors = board.getColors();
                    Color color = JColorChooser.showDialog(
                        null, "Select a color", colors.get(type)
                    );
                    if (color != null) {
                        colors.put(type, color);
                        board.repaint();
                    }
                }
            });
        });

        board.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                try {
                    switch (event.getKeyCode()) {
                        case KeyEvent.VK_UP    -> sokoban.moveUp();
                        case KeyEvent.VK_RIGHT -> sokoban.moveRight();
                        case KeyEvent.VK_DOWN  -> sokoban.moveDown();
                        case KeyEvent.VK_LEFT  -> sokoban.moveLeft();
                    }
                    board.repaint();
                    warningText.setText("Play with the arrow keys.");
                } catch (SokobanException e) {
                    warningText.setText(e.getMessage());
                }
            }
        });
    }

    /**
     * Shows an exit confirmation dialog and closes the application if accepted.
     *
     * @param message text displayed in the confirmation dialog.
     */
    private void exit(String message) {
        int close = JOptionPane.showConfirmDialog(this, message, "Exit confirmation", JOptionPane.YES_NO_OPTION);
        if (close == JOptionPane.YES_OPTION) {
            setVisible(false);
            System.exit(0);
        }
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    /**
     * Launches the graphical application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        SokobanGUI gui = new SokobanGUI();
        gui.setVisible(true);
    }
}



/**
 * Swing panel responsible for drawing the Sokoban board.
 */
class Board extends JPanel {
    public static int tileSize = 30;
    private EasySokoban sokoban;
    private Map<Integer, Color> colors = new HashMap<>();

    /**
     * Creates a board panel using the default Sokoban board.
     */
    public Board() {
        super();
        this.sokoban = EasySokoban.createDefaultSokoban();
        setDefaultColors();
    }

    /**
     * Creates a board panel for a specific Sokoban instance.
     *
     * @param sokoban game instance to render.
     */
    public Board(EasySokoban sokoban) {
        super();
        this.sokoban = sokoban;
        setDefaultColors();
    }

    /**
     * Returns the number of board rows.
     *
     * @return board height.
     */
    public int getRows() {
        return this.sokoban.getHeight();
    }

    /**
     * Returns the number of board columns.
     *
     * @return board width.
     */
    public int getColumns() {
        return this.sokoban.getWidth();
    }

    /**
     * Restores the default color palette for each cell type.
     */
    public void setDefaultColors() {
        colors.put(EasySokoban.EMPTY, new Color(235, 235, 211));
        colors.put(EasySokoban.BLOCK, new Color(193, 152, 117));
        colors.put(EasySokoban.BOX, new Color(188, 95, 4));
        colors.put(EasySokoban.FIXED_BOX, new Color(104, 83, 77));
        colors.put(EasySokoban.TARGET, new Color(219, 84, 97));
        colors.put(EasySokoban.PLAYER, new Color(0, 0, 0));
    }

    /**
     * Returns the current mapping from cell type to color.
     *
     * @return color map in use by this panel.
     */
    public Map<Integer, Color> getColors() {
        return colors;
    }

    /**
     * Replaces the active color mapping.
     *
     * @param newColors new type-to-color map.
     */
    public void setColors(Map<Integer, Color> newColors) {
        colors = newColors; // no checks
    }

    /**
     * Paints the full board centered in the panel.
     *
     * @param g graphics context.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int startX = (getWidth() - getColumns()*tileSize)/2;
        int startY = (getHeight() - getRows()*tileSize)/2;
        int[][] board = this.sokoban.getBoard();
        for (int r=0; r<board.length; r++) {
            for (int c=0; c<board[r].length; c++) {
                switch (board[r][c]) {
                    case EasySokoban.EMPTY -> {
                        g.setColor(colors.get(EasySokoban.EMPTY));
                        g.fillRect(startX + c*tileSize, startY + r*tileSize, tileSize, tileSize);
                    } case EasySokoban.BLOCK -> {
                        g.setColor(colors.get(EasySokoban.BLOCK));
                        g.fillRect(startX + c*tileSize, startY + r*tileSize, tileSize, tileSize);
                    } case EasySokoban.BOX -> {
                        g.setColor(colors.get(EasySokoban.BOX));
                        g.fillRect(startX + c*tileSize, startY + r*tileSize, tileSize, tileSize);
                    } case EasySokoban.FIXED_BOX -> {
                        g.setColor(colors.get(EasySokoban.FIXED_BOX));
                        g.fillRect(startX + c*tileSize, startY + r*tileSize, tileSize, tileSize);
                    } case EasySokoban.TARGET -> {
                        g.setColor(colors.get(EasySokoban.EMPTY));
                        g.fillRect(startX + c*tileSize, startY + r*tileSize, tileSize, tileSize);
                        g.setColor(colors.get(EasySokoban.TARGET));
                        g.fillOval(startX + (int) ((c+1f/3)*tileSize), startY + (int) ((r+1f/3)*tileSize), tileSize/3, tileSize/3);
                    } case EasySokoban.PLAYER -> {
                        g.setColor(colors.get(EasySokoban.EMPTY));
                        g.fillRect(startX + c*tileSize, startY + r*tileSize, tileSize, tileSize);
                        g.setColor(colors.get(EasySokoban.PLAYER));
                        g.fillOval(startX + (int) ((c+1f/4)*tileSize), startY + r*tileSize, tileSize/2, tileSize/2);
                        g.fillRect(startX + (int) ((c+1f/4)*tileSize), startY + (int) ((r+1f/2)*tileSize), tileSize/2, tileSize/2);
                    }
                }
            }
        }
    }
}