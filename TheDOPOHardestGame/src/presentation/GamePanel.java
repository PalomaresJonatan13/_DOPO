package presentation;

import domain.*;
import domain.players.Player;
import domain.DopoHardestGame.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

// ---------------------------------------------------------------------------
// Panel: Game
// ---------------------------------------------------------------------------

class GamePanel extends JPanel {

    private DopoHardestGame game;
    private Timer gameLoop;
    private JLabel statusLabel;
    private PlayerStatsBox p1StatsBox;
    private PlayerStatsBox p2StatsBox;
    private BoardRenderer renderer;
    private JButton backButton;
    private ActionListener backToMapsListener;

    public GamePanel() {
        this.prepareElements();
    }

    public void setBackToMapsListener(ActionListener listener) {
        this.backToMapsListener = listener;
    }

    private void prepareElements() {
        setLayout(new BorderLayout());
        setBackground(new Color(10, 10, 20));

        renderer = new BoardRenderer();
        add(renderer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(20, 20, 35));
        bottomPanel.setOpaque(true);

        statusLabel = new JLabel("Use arrow keys to move. Press P to pause.", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(180, 180, 200));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));

        backButton = new JButton("BACK TO MAPS");
        backButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(50, 50, 70));
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            if (gameLoop != null) {
                gameLoop.stop();
            }
            if (backToMapsListener != null) {
                backToMapsListener.actionPerformed(e);
            }
        });

        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(backButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Stats Panel at North
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));

        p1StatsBox = new PlayerStatsBox();
        p2StatsBox = new PlayerStatsBox();

        statsPanel.add(p1StatsBox, BorderLayout.WEST);
        statsPanel.add(p2StatsBox, BorderLayout.EAST);
        add(statsPanel, BorderLayout.NORTH);

        setFocusable(true);
        prepareActions();
    }

    private boolean up, down, left, right;
    private boolean p2Up, p2Down, p2Left, p2Right;

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
                    case KeyEvent.VK_W -> p2Up = true;
                    case KeyEvent.VK_S -> p2Down = true;
                    case KeyEvent.VK_A -> p2Left = true;
                    case KeyEvent.VK_D -> p2Right = true;
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
                    case KeyEvent.VK_W -> p2Up = false;
                    case KeyEvent.VK_S -> p2Down = false;
                    case KeyEvent.VK_A -> p2Left = false;
                    case KeyEvent.VK_D -> p2Right = false;
                }
                updatePlayerMovement();
            }
        });
    }

    private void updatePlayerMovement() {
        if (game == null) return;

        // Player 1
        int dx1 = 0, dy1 = 0;
        if (up)    dy1 -= 1;
        if (down)  dy1 += 1;
        if (left)  dx1 -= 1;
        if (right) dx1 += 1;
        game.setPlayerDirection(0, dx1, dy1);

        // Player 2 (PvsP only)
        if (game.getGameMode() == GameMode.PvsP) {
            int dx2 = 0, dy2 = 0;
            if (p2Up)    dy2 -= 1;
            if (p2Down)  dy2 += 1;
            if (p2Left)  dx2 -= 1;
            if (p2Right) dx2 += 1;
            game.setPlayerDirection(1, dx2, dy2);
        }
    }

    private Color p1Color, p2Color;

    public void startGame(DopoHardestGame game, Color[] playerColors) {
        this.game = game;
        this.p1Color = playerColors[0];
        this.p2Color = playerColors[1];
        renderer.setGame(game, playerColors);

        // Reset key states
        up = down = left = right = false;
        p2Up = p2Down = p2Left = p2Right = false;

        String controlsHelp = "Use arrow keys to move.";
        if (game.getGameMode() == GameMode.PvsP) {
            controlsHelp = "P1: Arrows | P2: WASD to move.";
        }
        statusLabel.setText(controlsHelp + " Press P to pause.");

        if (gameLoop != null) gameLoop.stop();

        gameLoop = new Timer(20, e -> tick()); // ~50 fps
        gameLoop.start();
    }

    private void tick() {
        if (game == null) return;

        game.update();
        renderer.repaint();
        updateStatsLabels();

        if (game.isVictory()) {
            List<Player> winners = game.getWinners();
            gameLoop.stop();
            if (winners != null && !winners.isEmpty()) {
                if (winners.size() == 1) {
                    statusLabel.setText(String.format("%s wins!", winners.get(0).getName()));
                } else {
                    statusLabel.setText("It's a tie!");
                }
            } else {
                statusLabel.setText("What did just happen?");
            }
        } else if (game.isGameOver()) {
            gameLoop.stop();
            statusLabel.setText("GAME OVER");
        } else if (game.isPaused()) {
            statusLabel.setText("Paused. Press P to resume.");
        } else {
            String controlsHelp = "Arrow keys to move";
            if (game.getGameMode() == GameMode.PvsP) {
                controlsHelp = "P1: Arrows | P2: WASD";
            }
            statusLabel.setText(String.format(
                    "Time: %.2f s              /  %s  /  Press P to pause",
                    game.getTimeRemaining(), controlsHelp));
        }
    }

    private void updateStatsLabels() {
        if (game.getPlayers() == null || game.getPlayers().isEmpty()) return;

        List<domain.players.Player> players = game.getPlayers();
        domain.players.Player p1 = players.size() > 0 ? players.get(0) : null;
        domain.players.Player p2 = players.size() > 1 ? players.get(1) : null;

        if (p1 != null) {
            String text = String.format("P1 - Deaths: %d | Coins: %d", p1.getDeaths(), p1.getCoinCount());
            p1StatsBox.updateStats(text, p1Color);
        } else {
            p1StatsBox.updateStats("", null);
        }

        if (p2 != null) {
            String p2Prefix = game.getGameMode() == GameMode.PvsP ? "P2" : "M";
            String text = String.format("%s - Deaths: %d | Coins: %d", p2Prefix, p2.getDeaths(), p2.getCoinCount());
            p2StatsBox.updateStats(text, p2Color);
        } else {
            p2StatsBox.updateStats("", null);
        }
    }

    private class PlayerStatsBox extends JPanel {
        private JPanel colorIndicator;
        private JLabel textLabel;

        public PlayerStatsBox() {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));

            colorIndicator = new JPanel();
            colorIndicator.setPreferredSize(new Dimension(14, 14));
            colorIndicator.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            colorIndicator.setVisible(false);

            textLabel = new JLabel("");
            textLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
            textLabel.setForeground(Color.WHITE);

            add(colorIndicator);
            add(textLabel);
        }

        public void updateStats(String text, Color borderColor) {
            if (text == null || text.isEmpty()) {
                colorIndicator.setVisible(false);
                textLabel.setText("");
            } else {
                colorIndicator.setBackground(borderColor);
                colorIndicator.setVisible(true);
                textLabel.setText(text);
            }
        }
    }
}