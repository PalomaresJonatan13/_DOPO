package presentation;

import domain.DopoHardestGame;
import domain.Cell;
import domain.Cell.CellType;
import domain.enemies.Enemy;
import domain.players.*;
import domain.Coin;
import domain.Coin.CoinType;
import domain.SpecialObject;
import domain.SpecialObject.SpecialObjectType;

import javax.swing.*;
import java.awt.*;

// ---------------------------------------------------------------------------
// Renderer: Board
// ---------------------------------------------------------------------------

public class BoardRenderer extends JPanel {

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
        if (game == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cols = game.getWidth();
        int rows = game.getHeight();

        // Center the board using the fixed tile size
        int offsetX = (getWidth() - cols * TILE) / 2;
        int offsetY = (getHeight() - rows * TILE) / 2;

        // Draw cells
        for (Cell cell : game.getCells()) {
            int px = (int) (cell.getX() * TILE);
            int py = (int) (cell.getY() * TILE);
            // Checkerboard uses rounded grid position for parity
            int col = (int) Math.round(cell.getX());
            int row = (int) Math.round(cell.getY());
            CellType type = cell.getType();
            if (type == CellType.NORMAL) {
                g2.setColor(((col + row) % 2 == 0) ? COLOR_TILE_GRAY : COLOR_TILE_WHITE);
            } else {
                g2.setColor(COLOR_SPECIAL_CELL);
            }
            g2.fillRect(offsetX + px, offsetY + py, (int) (cell.getWidth() * TILE), (int) (cell.getHeight() * TILE));
        }

        // Draw enemies
        if (!game.getEnemies().isEmpty()) {
            for (Enemy enemy : game.getEnemies()) {
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

        // Draw coins
        if (!game.getCoins().isEmpty()) {
            for (Coin coin : game.getCoins()) {
                if (!coin.isActive())
                    continue;
                int px = (int) (coin.getX() * TILE);
                int py = (int) (coin.getY() * TILE);
                int coinSize = (int) (coin.getWidth() * TILE);

                if (coin.getType() == CoinType.RED) {
                    g2.setColor(Color.RED);
                } else if (coin.getType() == CoinType.BLUE) {
                    g2.setColor(Color.BLUE);
                } else if (coin.getType() == CoinType.GREEN) {
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
            for (SpecialObject obj : game.getSpecialObjects()) {
                if (!obj.isActive())
                    continue;
                int px = (int) (obj.getX() * TILE);
                int py = (int) (obj.getY() * TILE);
                int objW = (int) (obj.getWidth() * TILE);
                int objH = (int) (obj.getHeight() * TILE);

                if (obj.getType() == SpecialObjectType.BOMB) {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(offsetX + px, offsetY + py, objW, objH);
                    g2.setStroke(new BasicStroke(3f));
                    g2.drawRect(offsetX + px, offsetY + py, objW, objH);
                } else if (obj.getType() == SpecialObjectType.LIFE) {
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
            for (Player player : game.getPlayers()) {
                int px = (int) (player.getX() * TILE);
                int py = (int) (player.getY() * TILE);
                int playerSize = (int) (player.getWidth() * TILE);

                PlayerState state = player.getCurrentState();
                if (state instanceof RedState) {
                    g2.setColor(Color.RED);
                } else if (state instanceof BlueState) {
                    g2.setColor(Color.BLUE);
                } else if (state instanceof GreenState) {
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