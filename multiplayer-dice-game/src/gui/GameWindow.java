package gui;

import game.Player;
import game.Dice;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

public class GameWindow extends JFrame {
    private List<Player> players;
    private int currentPlayerIndex;
    private JLabel diceLabel;
    private JTextArea resultArea;
    private JButton rollButton;
    private JButton restartButton;
    private JButton exitButton;
    private JLabel playerTurnLabel;
    private JLabel diceImageLabel;
    private JPanel buttonPanel;
    private ImageIcon[] diceIcons;
    private ImageIcon backgroundIcon;

    public GameWindow(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
        loadDiceImages();
        loadBackgroundImage();
        initializeUI();
    }

    // Load dice images (dice1.png to dice6.png) from resources folder
    private void loadDiceImages() {
        diceIcons = new ImageIcon[6];
        for (int i = 0; i < 6; i++) {
            java.net.URL location = getClass().getResource("dice" + (i + 1) + ".png");
            if (location != null) {
                ImageIcon icon = new ImageIcon(location);
                Image scaledImg = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                diceIcons[i] = new ImageIcon(scaledImg);
            } else {
                // Fallback: show a colored square with the number
                Image img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
                Graphics g = img.getGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, 50, 50);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.drawString("" + (i + 1), 15, 35);
                g.dispose();
                diceIcons[i] = new ImageIcon(img);
            }
        }
    }

    // Load background image
    private void loadBackgroundImage() {
        // Use the attached background image (background.png or background.jpg)
        java.net.URL bgUrl = getClass().getResource("/background.jpg");
        if (bgUrl == null) {
            bgUrl = getClass().getResource("/background.png");
        }
        if (bgUrl != null) {
            backgroundIcon = new ImageIcon(bgUrl);
        } else {
            backgroundIcon = null;
        }
    }

    private void initializeUI() {
        setTitle("Dice Game");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));

        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundIcon != null) {
                    g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPanel);

        // Top panel for player turn
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        playerTurnLabel = new JLabel("Turn: " + players.get(currentPlayerIndex).getName(), SwingConstants.CENTER);
        playerTurnLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        playerTurnLabel.setForeground(Color.WHITE);
        topPanel.add(playerTurnLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for dice image and label
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        diceImageLabel = new JLabel(diceIcons[0]) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw border for dice
                g.setColor(new Color(255,255,255,180));
                g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
            }
        };
        diceImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        diceImageLabel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        centerPanel.add(diceImageLabel, gbc);

        gbc.gridy = 1;
        diceLabel = new JLabel("Roll the dice!", SwingConstants.CENTER);
        diceLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        diceLabel.setForeground(Color.WHITE);
        centerPanel.add(diceLabel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for results and control buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        resultArea = new JTextArea(4, 20) {
            @Override
            public void append(String str) {
                super.append(str);
                // Limit to last 8 lines
                String[] lines = getText().split("\n");
                if (lines.length > 8) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = lines.length - 8; i < lines.length; i++) sb.append(lines[i]).append("\n");
                    setText(sb.toString());
                }
            }
        };
        resultArea.setEditable(false);
        Font resultFont;
        try {
            resultFont = new Font("Montserrat", Font.BOLD, 20);
        } catch (Exception e) {
            resultFont = new Font("Segoe UI", Font.BOLD, 20);
        }
        resultArea.setFont(resultFont);
        resultArea.setOpaque(false);
        resultArea.setForeground(new Color(255, 255, 210));
        resultArea.setBorder(null);
        bottomPanel.add(resultArea, BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 8));
        buttonPanel.setOpaque(false);
        rollButton = new JButton("Roll Dice");
        styleButton(rollButton, new Color(0, 153, 255), Color.WHITE);
        rollButton.setToolTipText("Roll the dice for your turn");
        rollButton.addActionListener(new RollDiceAction());
        buttonPanel.add(rollButton);

        restartButton = new JButton("Restart");
        styleButton(restartButton, new Color(76, 175, 80), Color.WHITE);
        restartButton.setToolTipText("Restart the game");
        restartButton.setEnabled(false);
        restartButton.addActionListener(e -> restartGame());
        buttonPanel.add(restartButton);

        exitButton = new JButton("Exit");
        styleButton(exitButton, new Color(244, 67, 54), Color.WHITE);
        exitButton.setToolTipText("Exit the game");
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        Font btnFont;
        try {
            btnFont = new Font("Montserrat", Font.BOLD, 18);
        } catch (Exception e) {
            btnFont = new Font("Segoe UI", Font.BOLD, 18);
        }
        button.setFont(btnFont);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 2, true),
            BorderFactory.createEmptyBorder(8, 24, 8, 24)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
    }

    private class RollDiceAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            rollDice();
        }
    }

    private void rollDice() {
        Dice dice = new Dice();
        int rollResult = dice.roll();
        diceImageLabel.setIcon(diceIcons[rollResult - 1]);
        diceLabel.setText(players.get(currentPlayerIndex).getName() + " rolled: " + rollResult);
        players.get(currentPlayerIndex).updateScore(rollResult);
        resultArea.append(players.get(currentPlayerIndex).getName() + ": " + rollResult + "\n");
        resultArea.setCaretPosition(resultArea.getDocument().getLength());

        currentPlayerIndex++;
        if (currentPlayerIndex < players.size()) {
            playerTurnLabel.setText("Turn: " + players.get(currentPlayerIndex).getName());
        } else {
            determineWinner();
        }
    }

    private void restartGame() {
        // Reset all player scores and rolls
        for (Player player : players) {
            player.resetScore();
            player.setRoll(0);
        }
        currentPlayerIndex = 0;
        diceImageLabel.setIcon(diceIcons[0]);
        diceLabel.setText("Roll the dice!");
        resultArea.setText("");
        playerTurnLabel.setText("Turn: " + players.get(currentPlayerIndex).getName());
        rollButton.setEnabled(true);
        restartButton.setEnabled(false);
    }

    private void determineWinner() {
        int highestScore = -1;
        List<String> winners = new ArrayList<>();
        for (Player player : players) {
            int score = player.getScore();
            if (score > highestScore) {
                highestScore = score;
                winners.clear();
                winners.add(player.getName());
            } else if (score == highestScore) {
                winners.add(player.getName());
            }
        }
        String winnerMessage;
        if (winners.size() == 1) {
            winnerMessage = "🏆 Winner: " + winners.get(0) + " with a score of " + highestScore;
        } else {
            winnerMessage = "🤝 Draw between: " + String.join(", ", winners) + " (score: " + highestScore + ")";
        }
        resultArea.append(winnerMessage + "\n");
        diceLabel.setText(winnerMessage);
        playerTurnLabel.setText("Game Over!");
        rollButton.setEnabled(false);
        restartButton.setEnabled(true);
    }
}