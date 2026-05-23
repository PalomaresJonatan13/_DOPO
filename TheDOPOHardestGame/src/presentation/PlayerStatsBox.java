package presentation;

import javax.swing.*;
import java.awt.*;

public class PlayerStatsBox extends JPanel {
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