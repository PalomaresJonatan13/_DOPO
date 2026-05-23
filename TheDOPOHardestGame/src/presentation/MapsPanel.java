package presentation;

import domain.DopoHardestGame.GameMode;
import domain.players.Player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;


// ---------------------------------------------------------------------------
// Panel: Map Selection
// ---------------------------------------------------------------------------

class MapsPanel extends JPanel {

    private JButton backButton;
    private JComboBox<GameMode> modeSelector;
    private ColorSelector p1ColorSelector;
    private ColorSelector p2ColorSelector;
    private PlayerColorSelector p1PlayerColorSelector;
    private PlayerColorSelector p2PlayerColorSelector;
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

        p1ColorSelector = new ColorSelector("P1 Border", Color.BLACK, "Select P1 Border Color");
        p2ColorSelector = new ColorSelector("P2/M Border", Color.RED, "Select P2/M Border Color");
        p2ColorSelector.setEnabled(false);

        p1PlayerColorSelector = new PlayerColorSelector("P1 Color", 0); // Red
        p2PlayerColorSelector = new PlayerColorSelector("P2 Color", 1); // Blue
        p2PlayerColorSelector.setEnabled(false);

        modeSelector.addActionListener(e -> {
            GameMode mode = (GameMode) modeSelector.getSelectedItem();
            if (mode == GameMode.PLAYER) {
                p2ColorSelector.setEnabled(false);
                p2ColorSelector.setText("P2/M Border");
                p2PlayerColorSelector.setEnabled(false);
            } else if (mode == GameMode.PvsP) {
                p2ColorSelector.setEnabled(true);
                p2ColorSelector.setText("P2 Border");
                p2ColorSelector.setSelectedColor(Color.RED);
                p2PlayerColorSelector.setEnabled(true);
            } else {
                p2ColorSelector.setEnabled(true);
                p2ColorSelector.setText("M Border");
                p2ColorSelector.setSelectedColor(Color.GRAY);
                p2PlayerColorSelector.setEnabled(true);
            }
        });

        colorPanel.add(p1ColorSelector);
        colorPanel.add(p1PlayerColorSelector);
        colorPanel.add(p2ColorSelector);
        colorPanel.add(p2PlayerColorSelector);
        bottomBar.add(colorPanel);

        add(bottomBar, BorderLayout.SOUTH);
    }

    public Color[] getPlayerColors() {
        return new Color[] { p1ColorSelector.getSelectedColor(), p2ColorSelector.getSelectedColor() };
    }

    public PlayerType getP1PlayerColor() {
        return p1PlayerColorSelector.getSelectedType();
    }

    public PlayerType getP2PlayerColor() {
        return p2PlayerColorSelector.getSelectedType();
    }

    public GameMode getSelectedMode() {
        return (GameMode) modeSelector.getSelectedItem();
    }

    public JButton getBackButton() {
        return backButton;
    }

    private void refreshMaps() {
        gridPanel.removeAll();
        File mapsDir = DopoHardestGameGUI.getMapsDir();
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
                File destFile = new File(DopoHardestGameGUI.getMapsDir(), "custom_map_" + nextId + ".txt");
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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
            File srcFile = new File(DopoHardestGameGUI.getMapsDir(), mapFileName);
            try {
                Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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
            File file = new File(DopoHardestGameGUI.getMapsDir(), mapFileName);
            if (file.delete()) {
                refreshMaps();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the map.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int getNextCustomMapId() {
        File mapsDir = DopoHardestGameGUI.getMapsDir();
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