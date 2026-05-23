package presentation;

import domain.players.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerColorSelector extends JPanel {
    private JLabel label;
    private JPanel colorBox;
    private int colorIndex;
    private final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN};
    private final Player.PlayerType[] types = {Player.PlayerType.RED, Player.PlayerType.BLUE, Player.PlayerType.GREEN};

    public PlayerColorSelector(String text, int initialIndex) {
        this.colorIndex = initialIndex;
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial Black", Font.BOLD, 12));

        colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBackground(colors[colorIndex]);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        add(label);
        add(colorBox);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    cycleColor();
                }
            }
        });
    }

    private void cycleColor() {
        colorIndex = (colorIndex + 1) % colors.length;
        colorBox.setBackground(colors[colorIndex]);
    }

    public Player.PlayerType getSelectedType() {
        return types[colorIndex];
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        label.setEnabled(enabled);
        colorBox.setEnabled(enabled);
        Color c = enabled ? Color.WHITE : Color.GRAY;
        label.setForeground(c);
        colorBox.setBorder(BorderFactory.createLineBorder(c, 1));
    }
}