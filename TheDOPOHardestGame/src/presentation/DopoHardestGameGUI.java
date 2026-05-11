package presentation;

import domain.DopoHardestGame;
import domain.DopoHardestGame.GameMode;
import domain.exceptions.DOPOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class DopoHardestGameGUI extends JFrame {

    // Card names
    private static final String CARD_MAIN = "MAIN";
    private static final String CARD_MAPS = "MAPS";
    private static final String CARD_GAME = "GAME";

    // Maps directory (relative to working directory)
    private static final String MAPS_DIR = "src/domain/maps";

    private CardLayout cardLayout;
    private JPanel cardContainer;

    private MainPanel mainPanel;
    private MapsPanel mapsPanel;
    private GamePanel gamePanel;

    /**
     * Creates and initializes the GUI.
     */
    public DopoHardestGameGUI() {
        super("The DOPO's Hardest Game");
        this.prepareElements();
        this.prepareActions();
    }

    // -----------------------------------------------------------------------
    // Element preparation
    // -----------------------------------------------------------------------

    /**
     * Creates and arranges all top-level visual components.
     */
    private void prepareElements() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                deleteCustomMaps();
            }
        });

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);

        mainPanel = new MainPanel();
        mapsPanel = new MapsPanel();
        gamePanel = new GamePanel();

        cardContainer.add(mainPanel, CARD_MAIN);
        cardContainer.add(mapsPanel, CARD_MAPS);
        cardContainer.add(gamePanel, CARD_GAME);

        add(cardContainer);
        cardLayout.show(cardContainer, CARD_MAIN);
    }

    private void deleteCustomMaps() {
        File mapsDir = new File("src/domain/maps");
        File[] customMaps = mapsDir.listFiles((dir, name) -> name.startsWith("custom_map_") && name.endsWith(".txt"));
        if (customMaps != null) {
            for (File file : customMaps) {
                file.delete();
            }
        }
    }

    // -----------------------------------------------------------------------
    // Action preparation
    // -----------------------------------------------------------------------

    /**
     * Registers all window-level and inter-panel actions.
     */
    private void prepareActions() {
        prepareActionsMain();
        prepareActionsMaps();
    }

    /**
     * Wires actions for the main panel.
     */
    private void prepareActionsMain() {
        mainPanel.getPlayButton().addActionListener(e -> {
            cardLayout.show(cardContainer, CARD_MAPS);
        });
    }

    /**
     * Wires actions for the maps panel.
     */
    private void prepareActionsMaps() {
        mapsPanel.getBackButton().addActionListener(e -> {
            cardLayout.show(cardContainer, CARD_MAIN);
        });

        mapsPanel.setMapSelectedListener(e -> {
            loadAndStartGame(e.getActionCommand());
        });
    }

    /**
     * Loads the given map file and switches to the game panel.
     *
     * @param mapFileName filename inside the maps directory.
     */
    private void loadAndStartGame(String mapFileName) {
        File mapFile = new File(MAPS_DIR + "/" + mapFileName);
        try {
            DopoHardestGame game = new DopoHardestGame(mapFile, mapsPanel.getSelectedMode());
            gamePanel.startGame(game, mapsPanel.getPlayerColors());
            cardLayout.show(cardContainer, CARD_GAME);
            gamePanel.requestFocusInWindow();
        } catch (DOPOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not load map: " + ex.getMessage(),
                    "Map Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -----------------------------------------------------------------------
    // Entry point
    // -----------------------------------------------------------------------

    /**
     * Launches the application.
     *
     * @param args command-line arguments (unused).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DopoHardestGameGUI gui = new DopoHardestGameGUI();
            gui.setVisible(true);
        });
    }
}

// ---------------------------------------------------------------------------
// Panel: Main Menu
// ---------------------------------------------------------------------------

/**
 * The main title screen with a play button.
 */
class MainPanel extends JPanel {

    private JButton playButton;

    public MainPanel() {
        this.prepareElements();
    }

    private void prepareElements() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 235, 255)); // Light blue/lavender background

        // Top black bar
        JPanel topBar = new JPanel();
        topBar.setBackground(Color.BLACK);
        topBar.setPreferredSize(new Dimension(100, 40));
        add(topBar, BorderLayout.NORTH);

        // Center area
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Title section
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel preTitle = new JLabel("THE DOPO'S...");
        preTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, -25, 0); // pull it down closer
        titlePanel.add(preTitle, gbc);

        TitleLabel mainTitle = new TitleLabel("HARDEST GAME");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 65));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        titlePanel.add(mainTitle, gbc);

        JLabel postTitle = new JLabel("VERSION 1.0");
        postTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(-20, 0, 0, 15); // pull it up closer
        titlePanel.add(postTitle, gbc);

        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titlePanel);

        // centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Buttons section
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonsPanel.setOpaque(false);

        playButton = new OutlinedButton("PLAY", "GAME", new Color(240, 40, 40));
        buttonsPanel.add(playButton);
        /*
         * buttonsPanel.add(new OutlinedButton("LEADER", "BOARD", new Color(60, 100,
         * 240)));
         * buttonsPanel.add(new OutlinedButton("MORE", "GAMES", new Color(40, 200,
         * 80)));
         * buttonsPanel.add(new OutlinedButton("SNUBBY", "LAND", new Color(240, 220,
         * 20)));
         */

        centerPanel.add(buttonsPanel);

        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);

        // Bottom black bar
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(Color.BLACK);
        bottomBar.setPreferredSize(new Dimension(100, 40));
        bottomBar.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JLabel bottomRight = new JLabel("DOPO");
        bottomRight.setForeground(Color.WHITE);
        bottomRight.setFont(new Font("SansSerif", Font.BOLD, 16));
        bottomBar.add(bottomRight, BorderLayout.EAST);

        add(bottomBar, BorderLayout.SOUTH);
    }

    public JButton getPlayButton() {
        return playButton;
    }

}

// ---------------------------------------------------------------------------
// Panel: Map Selection
// ---------------------------------------------------------------------------

/**
 * Panel showing the available maps to choose from.
 */
class MapsPanel extends JPanel {

    private JButton backButton;
    private JComboBox<GameMode> modeSelector;
    private JButton p1ColorBtn;
    private JButton p2ColorBtn;
    private JPanel gridPanel;
    private JPanel wrapperPanel;
    private JScrollPane scrollPane;
    private ActionListener mapSelectedListener;
    private JButton uploadButton;

    public MapsPanel() {
        this.prepareElements();
    }

    public void setMapSelectedListener(ActionListener listener) {
        this.mapSelectedListener = listener;
        refreshMaps();
    }

    private void prepareElements() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 235, 255));

        // Header
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.BLACK);
        topBar.setPreferredSize(new Dimension(100, 60));

        backButton = new JButton("BACK");
        backButton.setFont(new Font("Arial Black", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        leftWrapper.setOpaque(false);
        leftWrapper.add(backButton);
        topBar.add(leftWrapper, BorderLayout.WEST);

        JLabel title = new JLabel("SELECT A MAP");
        title.setFont(new Font("Arial Black", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        topBar.add(title, BorderLayout.CENTER);

        // Right side wrapper
        JPanel rightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        rightWrapper.setOpaque(false);
        rightWrapper.setPreferredSize(new Dimension(200, 60));

        uploadButton = new JButton("UPLOAD MAP");
        uploadButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setContentAreaFilled(false);
        uploadButton.setFocusPainted(false);
        uploadButton.setBorderPainted(false);
        uploadButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        uploadButton.addActionListener(e -> uploadMap());
        rightWrapper.add(uploadButton);

        topBar.add(rightWrapper, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Center Area (Grid of maps)
        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setOpaque(false);

        wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));
        wrapperPanel.add(gridPanel);

        scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        bottomBar.setBackground(Color.BLACK);

        JLabel modeLabel = new JLabel("Game Mode:");
        modeLabel.setForeground(Color.WHITE);
        modeLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
        bottomBar.add(modeLabel);

        modeSelector = new JComboBox<>(GameMode.values());
        modeSelector.setSelectedItem(GameMode.PLAYER);
        bottomBar.add(modeSelector);

        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        colorPanel.setOpaque(false);

        p1ColorBtn = new JButton("P1 Border");
        p1ColorBtn.setBackground(Color.BLACK);
        p1ColorBtn.setForeground(Color.WHITE);
        p1ColorBtn.setFocusPainted(false);
        p1ColorBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Select P1 Border Color", p1ColorBtn.getBackground());
            if (c != null)
                p1ColorBtn.setBackground(c);
        });

        p2ColorBtn = new JButton("P2/M Border");
        p2ColorBtn.setBackground(Color.RED);
        p2ColorBtn.setForeground(Color.WHITE);
        p2ColorBtn.setFocusPainted(false);
        p2ColorBtn.setEnabled(false);
        p2ColorBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Select P2/M Border Color", p2ColorBtn.getBackground());
            if (c != null)
                p2ColorBtn.setBackground(c);
        });

        modeSelector.addActionListener(e -> {
            GameMode mode = (GameMode) modeSelector.getSelectedItem();
            if (mode == GameMode.PLAYER) {
                p2ColorBtn.setEnabled(false);
                p2ColorBtn.setText("P2/M Border");
            } else if (mode == GameMode.PvsP) {
                p2ColorBtn.setEnabled(true);
                p2ColorBtn.setText("P2 Border");
                p2ColorBtn.setBackground(Color.RED);
            } else {
                p2ColorBtn.setEnabled(true);
                p2ColorBtn.setText("M Border");
                p2ColorBtn.setBackground(Color.GRAY);
            }
        });

        colorPanel.add(p1ColorBtn);
        colorPanel.add(p2ColorBtn);
        bottomBar.add(colorPanel);

        add(bottomBar, BorderLayout.SOUTH);
    }

    public Color[] getPlayerColors() {
        return new Color[] { p1ColorBtn.getBackground(), p2ColorBtn.getBackground() };
    }

    public GameMode getSelectedMode() {
        return (GameMode) modeSelector.getSelectedItem();
    }

    public JButton getBackButton() {
        return backButton;
    }

    private void refreshMaps() {
        gridPanel.removeAll();
        File mapsDir = new File("src/domain/maps");
        String[] mapFiles = mapsDir.list((dir, name) -> name.endsWith(".txt"));
        if (mapFiles == null || mapFiles.length == 0) {
            mapFiles = new String[] { "map1.txt", "map2.txt", "map3.txt" }; // Fallback
        }

        java.util.Arrays.sort(mapFiles); // Sort them alphabetically

        for (int i = 0; i < mapFiles.length; i++) {
            String fileName = mapFiles[i];
            String name = fileName.replace(".txt", "").toUpperCase();
            if (name.startsWith("CUSTOM_MAP_")) {
                name = name.replace("CUSTOM_MAP_", "CUSTOM MAP ");
            }
            JButton btn = new MapButton(name);
            btn.setActionCommand(fileName);
            if (mapSelectedListener != null) {
                btn.addActionListener(mapSelectedListener);
            }

            // Right click menu
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem saveItem = new JMenuItem("Save as .txt");
            saveItem.addActionListener(e -> saveMap(fileName));
            popupMenu.add(saveItem);

            if (fileName.startsWith("custom_map_")) {
                JMenuItem deleteItem = new JMenuItem("Delete");
                deleteItem.addActionListener(e -> deleteMap(fileName));
                popupMenu.add(deleteItem);
            }

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        popupMenu.show(btn, e.getX(), e.getY());
                    }
                }
            });

            gridPanel.add(btn);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void uploadMap() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                int nextId = getNextCustomMapId();
                File destFile = new File("src/domain/maps/custom_map_" + nextId + ".txt");
                java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                refreshMaps();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to upload map: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveMap(String mapFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(mapFileName));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File destFile = fileChooser.getSelectedFile();
            File srcFile = new File("src/domain/maps/" + mapFileName);
            try {
                java.nio.file.Files.copy(srcFile.toPath(), destFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(this, "Map saved successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to save map: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteMap(String mapFileName) {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this custom map?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            File file = new File("src/domain/maps/" + mapFileName);
            if (file.delete()) {
                refreshMaps();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the map.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int getNextCustomMapId() {
        File mapsDir = new File("src/domain/maps");
        String[] customMaps = mapsDir.list((dir, name) -> name.startsWith("custom_map_") && name.endsWith(".txt"));
        int maxId = 0;
        if (customMaps != null) {
            for (String cm : customMaps) {
                try {
                    String numPart = cm.replace("custom_map_", "").replace(".txt", "");
                    int id = Integer.parseInt(numPart);
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return maxId + 1;
    }
}

// ---------------------------------------------------------------------------
// Panel: Game
// ---------------------------------------------------------------------------

/**
 * Panel that renders the live game state and handles keyboard input.
 */
class GamePanel extends JPanel {

    private DopoHardestGame game;
    private Timer gameLoop;
    private JLabel statusLabel;
    private JLabel p1StatsLabel;
    private JLabel p2StatsLabel;
    private BoardRenderer renderer;

    public GamePanel() {
        this.prepareElements();
    }

    private void prepareElements() {
        setLayout(new BorderLayout());
        setBackground(new Color(10, 10, 20));

        renderer = new BoardRenderer();
        add(renderer, BorderLayout.CENTER);

        statusLabel = new JLabel("Use arrow keys to move. P = pause.", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(180, 180, 200));
        statusLabel.setBackground(new Color(20, 20, 35));
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        add(statusLabel, BorderLayout.SOUTH);

        // Stats Panel at North
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));

        p1StatsLabel = new JLabel("");
        p1StatsLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
        p1StatsLabel.setForeground(new Color(255, 100, 100)); // reddish for P1

        p2StatsLabel = new JLabel("");
        p2StatsLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
        p2StatsLabel.setForeground(new Color(100, 150, 255)); // bluish for P2

        statsPanel.add(p1StatsLabel, BorderLayout.WEST);
        statsPanel.add(p2StatsLabel, BorderLayout.EAST);
        add(statsPanel, BorderLayout.NORTH);

        setFocusable(true);
        prepareActions();
    }

    private boolean up, down, left, right;

    private void prepareActions() {
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (game == null)
                    return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> up = true;
                    case KeyEvent.VK_DOWN -> down = true;
                    case KeyEvent.VK_LEFT -> left = true;
                    case KeyEvent.VK_RIGHT -> right = true;
                    case KeyEvent.VK_P -> game.togglePause();
                }
                updatePlayerMovement();
            }

            public void keyReleased(KeyEvent e) {
                if (game == null)
                    return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> up = false;
                    case KeyEvent.VK_DOWN -> down = false;
                    case KeyEvent.VK_LEFT -> left = false;
                    case KeyEvent.VK_RIGHT -> right = false;
                }
                updatePlayerMovement();
            }
        });
    }

    private void updatePlayerMovement() {
        if (game == null)
            return;
        int dx = 0;
        int dy = 0;
        if (up)
            dy -= 1;
        if (down)
            dy += 1;
        if (left)
            dx -= 1;
        if (right)
            dx += 1;
        game.setPlayerDirection(0, dx, dy);
    }

    /**
     * Starts a new game on this panel, replacing any previous one.
     *
     * @param game         the loaded game instance.
     * @param playerColors the selected border colors for players.
     */
    public void startGame(DopoHardestGame game, Color[] playerColors) {
        this.game = game;
        renderer.setGame(game, playerColors);

        if (gameLoop != null)
            gameLoop.stop();

        gameLoop = new Timer(20, e -> tick()); // ~50 fps
        gameLoop.start();
    }

    /**
     * Advances the game by one tick and updates the UI.
     */
    private void tick() {
        if (game == null)
            return;

        game.update();
        renderer.repaint();
        updateStatsLabels();

        if (game.isVictory()) {
            gameLoop.stop();
            statusLabel.setText("You win!");
        } else if (game.isGameOver()) {
            gameLoop.stop();
            statusLabel.setText("Game over! Time's up.");
        } else if (game.isPaused()) {
            statusLabel.setText("Paused — press P to resume.");
        } else {
            statusLabel.setText(String.format(
                    "Time: %.2f s  |  Arrow keys to move  |  P = pause",
                    game.getTimeRemaining()));
        }
    }

    private void updateStatsLabels() {
        if (game.getPlayers() == null || game.getPlayers().isEmpty())
            return;

        java.util.Iterator<domain.players.Player> it = game.getPlayers().iterator();
        domain.players.Player p1 = it.hasNext() ? it.next() : null;
        domain.players.Player p2 = it.hasNext() ? it.next() : null;

        if (p1 != null) {
            p1StatsLabel.setText(String.format("P1 - Deaths: %d | Coins: %d", p1.getDeaths(), p1.getCoinCount()));
        } else {
            p1StatsLabel.setText("");
        }

        if (p2 != null) {
            String p2Prefix = game.getGameMode() == GameMode.PvsP ? "P2" : "M";
            p2StatsLabel
                    .setText(String.format("%s - Deaths: %d | Coins: %d", p2Prefix, p2.getDeaths(), p2.getCoinCount()));
        } else {
            p2StatsLabel.setText("");
        }
    }
}

// ---------------------------------------------------------------------------
// Renderer: Board
// ---------------------------------------------------------------------------

/**
 * Draws all game objects on a scaled grid.
 */
class BoardRenderer extends JPanel {

    private DopoHardestGame game;
    private Color[] playerColors;

    // Tile size (fixed)
    private static final int TILE = 30;

    // Colors
    private static final Color COLOR_BG = new Color(0xb4, 0xb5, 0xfe);
    private static final Color COLOR_TILE_GRAY = new Color(0xe6, 0xe6, 0xff);
    private static final Color COLOR_TILE_WHITE = new Color(0xf7, 0xf7, 0xff);
    private static final Color COLOR_SPECIAL_CELL = new Color(0xb5, 0xfe, 0xb4); // start, final, safe
    private static final Color COLOR_ENEMY = new Color(0x00, 0x00, 0xff);
    private static final Color COLOR_PLAYER = new Color(0xff, 0x00, 0x00);
    private static final Color COLOR_COIN = new Color(0xff, 0xff, 0x00);

    public BoardRenderer() {
        setBackground(COLOR_BG);
    }

    public void setGame(DopoHardestGame game, Color[] playerColors) {
        this.game = game;
        this.playerColors = playerColors;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (game == null)
            return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cols = game.getWidth();
        int rows = game.getHeight();

        // Center the board using the fixed tile size
        int offsetX = (getWidth() - cols * TILE) / 2;
        int offsetY = (getHeight() - rows * TILE) / 2;

        // Draw cells
        for (domain.Cell cell : game.getCells()) {
            int px = (int) (cell.getX() * TILE);
            int py = (int) (cell.getY() * TILE);
            // Checkerboard uses rounded grid position for parity
            int col = (int) Math.round(cell.getX());
            int row = (int) Math.round(cell.getY());
            domain.Cell.CellType type = cell.getType();
            if (type == domain.Cell.CellType.NORMAL) {
                g2.setColor(((col + row) % 2 == 0) ? COLOR_TILE_GRAY : COLOR_TILE_WHITE);
            } else {
                g2.setColor(COLOR_SPECIAL_CELL);
            }
            g2.fillRect(offsetX + px, offsetY + py, (int) (cell.getWidth() * TILE), (int) (cell.getHeight() * TILE));
        }

        // Draw enemies — Black border
        if (!game.getEnemies().isEmpty()) {
            for (domain.enemies.Enemy enemy : game.getEnemies()) {
                if (!enemy.isActive())
                    continue;
                int px = (int) (enemy.getX() * TILE);
                int py = (int) (enemy.getY() * TILE);
                int enemySize = (int) (enemy.getWidth() * TILE);

                g2.setColor(COLOR_ENEMY);
                g2.fillOval(offsetX + px, offsetY + py, enemySize, enemySize);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3f));
                g2.drawOval(offsetX + px, offsetY + py, enemySize, enemySize);
            }
        }

        // Draw coins — Black border, colored background
        if (!game.getCoins().isEmpty()) {
            for (domain.Coin coin : game.getCoins()) {
                if (!coin.isActive())
                    continue;
                int px = (int) (coin.getX() * TILE);
                int py = (int) (coin.getY() * TILE);
                int coinSize = (int) (coin.getWidth() * TILE);

                if (coin.getType() == domain.Coin.CoinType.RED) {
                    g2.setColor(Color.RED);
                } else if (coin.getType() == domain.Coin.CoinType.BLUE) {
                    g2.setColor(Color.BLUE);
                } else if (coin.getType() == domain.Coin.CoinType.GREEN) {
                    g2.setColor(Color.GREEN);
                } else {
                    g2.setColor(COLOR_COIN);
                }

                g2.fillOval(offsetX + px, offsetY + py, coinSize, coinSize);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3f));
                g2.drawOval(offsetX + px, offsetY + py, coinSize, coinSize);
            }
        }

        // Draw special objects
        if (!game.getSpecialObjects().isEmpty()) {
            for (domain.SpecialObject obj : game.getSpecialObjects()) {
                if (!obj.isActive())
                    continue;
                int px = (int) (obj.getX() * TILE);
                int py = (int) (obj.getY() * TILE);
                int objW = (int) (obj.getWidth() * TILE);
                int objH = (int) (obj.getHeight() * TILE);

                if (obj.getType() == domain.SpecialObject.SpecialObjectType.BOMB) {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(offsetX + px, offsetY + py, objW, objH);
                    g2.setStroke(new BasicStroke(3f));
                    g2.drawRect(offsetX + px, offsetY + py, objW, objH);
                } else if (obj.getType() == domain.SpecialObject.SpecialObjectType.LIFE) {
                    g2.setColor(new Color(173, 216, 230)); // Light blue
                    g2.fillRect(offsetX + px, offsetY + py, objW, objH);
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(3f));
                    g2.drawRect(offsetX + px, offsetY + py, objW, objH);
                }
            }
        }

        // Draw players
        if (!game.getPlayers().isEmpty()) {
            int playerIndex = 0;
            for (domain.players.Player player : game.getPlayers()) {
                int px = (int) (player.getX() * TILE);
                int py = (int) (player.getY() * TILE);
                int playerSize = (int) (player.getWidth() * TILE);

                domain.players.PlayerState state = player.getCurrentState();
                if (state instanceof domain.players.RedState) {
                    g2.setColor(Color.RED);
                } else if (state instanceof domain.players.BlueState) {
                    g2.setColor(Color.BLUE);
                } else if (state instanceof domain.players.GreenState) {
                    g2.setColor(Color.GREEN);
                } else {
                    g2.setColor(COLOR_PLAYER);
                }

                g2.fillRect(offsetX + px, offsetY + py, playerSize, playerSize);

                if (playerColors != null && playerIndex < playerColors.length) {
                    g2.setColor(playerColors[playerIndex]);
                } else {
                    g2.setColor(Color.BLACK);
                }
                g2.setStroke(new BasicStroke(3f));
                g2.drawRect(offsetX + px, offsetY + py, playerSize, playerSize);

                playerIndex++;
            }
        }
    }
}
