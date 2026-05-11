package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class MapButton extends JButton {
    private boolean hovered = false;
    private Color outlineColor = Color.BLACK;

    public MapButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(new Font("Arial Black", Font.BOLD, 16));
        setForeground(Color.BLACK);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        int baseW = fm.stringWidth(getText()) + 60; // 30px horiz padding * 2
        int baseH = fm.getHeight() + 40; // 20px vert padding * 2
        
        int prefW = baseW + 10;
        int prefH = baseH + 10;
        return new Dimension(prefW, prefH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        FontMetrics fm = g2.getFontMetrics(getFont());
        int textW = fm.stringWidth(getText());
        int textH = fm.getHeight();

        int boxW = textW + 60;
        int boxH = textH + 40;

        g2.translate(cx, cy);
        
        RoundRectangle2D rect = new RoundRectangle2D.Double(-boxW / 2.0, -boxH / 2.0, boxW, boxH, 20, 20);

        g2.setColor(Color.WHITE);
        if (hovered) {
            g2.setColor(new Color(240, 240, 240));
        }
        g2.fill(rect);

        g2.setStroke(new BasicStroke(4f));
        g2.setColor(outlineColor);
        g2.draw(rect);

        g2.setColor(getForeground());
        // Center text exactly
        int textX = -textW / 2;
        // Ascent represents distance from baseline to top. 
        // We want the text's center to be at y=0, so the baseline should be at y = textH / 2 - descent.
        int textY = (fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}
