package presentation;

import domain.*;
import domain.exceptions.DOPOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class DopoHardestGameGUI extends JFrame {

    private static final String CARD_MAIN = "MAIN";
    private static final String CARD_MAPS = "MAPS";
    private static final String CARD_GAME = "GAME";

    public static final String MAPS_DIR = "src/domain/maps";

    private CardLayout cardLayout;
    private JPanel cardContainer;

    private MainPanel mainPanel;
    private MapsPanel mapsPanel;
    private GamePanel gamePanel;

    public DopoHardestGameGUI() {
        super("The DOPO's Hardest Game");
        this.prepareElements();
        this.prepareActions();
    }

    // -----------------------------------------------------------------------
    // Element preparation
    // -----------------------------------------------------------------------

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

    public static File getMapsDir() {
        String[] candidates = {
            "src/domain/maps",
            "../src/domain/maps",
            "../../src/domain/maps"
        };
        for (String candidate : candidates) {
            File dir = new File(candidate);
            if (dir.exists() && dir.isDirectory()) {
                return dir;
            }
        }
        return new File("src/domain/maps");
    }

    private void deleteCustomMaps() {
        File mapsDir = getMapsDir();
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

    private void prepareActions() {
        prepareActionsMain();
        prepareActionsMaps();
        prepareActionsGame();
    }

    private void prepareActionsMain() {
        mainPanel.getPlayButton().addActionListener(e -> {
            cardLayout.show(cardContainer, CARD_MAPS);
        });
    }

    private void prepareActionsMaps() {
        mapsPanel.getBackButton().addActionListener(e -> {
            cardLayout.show(cardContainer, CARD_MAIN);
        });
        
        mapsPanel.setMapSelectedListener(e -> {
            loadAndStartGame(e.getActionCommand());
        });
    }

    private void prepareActionsGame() {
        gamePanel.setBackToMapsListener(e -> {
            cardLayout.show(cardContainer, CARD_MAPS);
        });
    }

    private void loadAndStartGame(String mapFileName) {
        File mapFile = new File(getMapsDir(), mapFileName);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DopoHardestGameGUI gui = new DopoHardestGameGUI();
            gui.setVisible(true);
        });
    }
}
