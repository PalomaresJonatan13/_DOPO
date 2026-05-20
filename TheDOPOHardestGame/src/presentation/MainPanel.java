package presentation;

import javax.swing.*;
import java.awt.*;

// ---------------------------------------------------------------------------
// Panel: Main Menu
// ---------------------------------------------------------------------------

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

        // Buttons section
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonsPanel.setOpaque(false);

        playButton = new OutlinedButton("PLAY", "GAME", new Color(240, 40, 40));
        buttonsPanel.add(playButton);
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
