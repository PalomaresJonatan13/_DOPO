package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ColorSelector extends JPanel {
    private JLabel label;
    private JPanel colorBox;
    private Color selectedColor;
    private String title;

    public ColorSelector(String text, Color initialColor, String chooserTitle) {
        this.title = chooserTitle;
        this.selectedColor = initialColor;
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial Black", Font.BOLD, 12));

        colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBackground(initialColor);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        add(label);
        add(colorBox);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    Color c = JColorChooser.showDialog(ColorSelector.this, title, selectedColor);
                    if (c != null) {
                        setSelectedColor(c);
                    }
                }
            }
        });
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color color) {
        this.selectedColor = color;
        colorBox.setBackground(color);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        label.setEnabled(enabled);
        colorBox.setEnabled(enabled);
        if (enabled) {
            label.setForeground(Color.WHITE);
            colorBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        } else {
            label.setForeground(Color.GRAY);
            colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }

    public void setText(String text) {
        label.setText(text);
    }
}