package presentation;

import javax.swing.*;
import java.awt.*;

public class TitleLabel extends JLabel {
    private Color topColor = new Color(100, 160, 240);
    private Color bottomColor = new Color(20, 80, 180);
    private int strokeWidth = 4;

    public TitleLabel(String text) {
        super(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        double scaleY = 1.6; // Stretch vertically
        g2.scale(1.0, scaleY);
        
        Font font = getFont();
        String text = getText();
        FontMetrics fm = g2.getFontMetrics(font);
        int x = strokeWidth;
        int y = fm.getAscent() + strokeWidth;
        
        java.awt.font.GlyphVector gv = font.createGlyphVector(g2.getFontRenderContext(), text);
        
        // Main text
        Shape shape = gv.getOutline(x, y);
        
        // Outline
        g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(Color.BLACK);
        g2.draw(shape);
        
        // Fill
        GradientPaint gp = new GradientPaint(0, y - fm.getAscent(), topColor, 0, y + fm.getDescent(), bottomColor);
        g2.setPaint(gp);
        g2.fill(shape);
        
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        double scaleY = 1.6;
        int width = fm.stringWidth(getText()) + strokeWidth * 2 + 10;
        int height = (int) ((fm.getHeight() + strokeWidth * 2 + 10) * scaleY);
        return new Dimension(width, height);
    }
}


class OutlinedButton extends JButton {
    private String line1;
    private String line2;
    private Color fillColor;
    private Color outlineColor = Color.BLACK;
    private int strokeWidth = 2;

    public OutlinedButton(String line1, String line2, Color fillColor) {
        this.line1 = line1;
        this.line2 = line2;
        this.fillColor = fillColor;
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(new Font("Arial Black", Font.BOLD, 26));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        java.awt.font.FontRenderContext frc = g2.getFontRenderContext();
        java.awt.font.TextLayout tl1 = new java.awt.font.TextLayout(line1, getFont(), frc);
        java.awt.font.TextLayout tl2 = new java.awt.font.TextLayout(line2, getFont(), frc);

        Shape shape1 = tl1.getOutline(null);
        Shape shape2 = tl2.getOutline(null);

        float h1 = tl1.getAscent() + tl1.getDescent();
        float h2 = tl2.getAscent() + tl2.getDescent();
        float totalH = h1 + h2 + 5; // 5px spacing

        // center horizontally and vertically
        float x1 = (getWidth() - tl1.getAdvance()) / 2f;
        float y1 = (getHeight() - totalH) / 2f + tl1.getAscent();

        float x2 = (getWidth() - tl2.getAdvance()) / 2f;
        float y2 = y1 + tl1.getDescent() + 5 + tl2.getAscent();

        g2.setStroke(new BasicStroke(strokeWidth * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Draw line 1
        g2.translate(x1, y1);
        g2.setColor(outlineColor);
        g2.draw(shape1);
        if (getModel().isRollover()) {
            g2.setColor(fillColor.darker());
        } else {
            g2.setColor(fillColor);
        }
        g2.fill(shape1);
        g2.translate(-x1, -y1);

        // Draw line 2
        g2.translate(x2, y2);
        g2.setColor(outlineColor);
        g2.draw(shape2);
        if (getModel().isRollover()) {
            g2.setColor(fillColor.darker());
        } else {
            g2.setColor(fillColor);
        }
        g2.fill(shape2);
        g2.translate(-x2, -y2);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        int w = Math.max(fm.stringWidth(line1), fm.stringWidth(line2)) + 20;
        int h = fm.getHeight() * 2 + 15;
        return new Dimension(w, h);
    }
}
