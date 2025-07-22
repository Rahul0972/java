package gui;
import javax.swing.*;
import game.GameLogic;
import game.Player;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main {
    private static final int MAX_PLAYERS = 5;
    private static final int MIN_PLAYERS = 2;

    public static void main(String[] args) {
        UIManager.put("Panel.background", new Color(80, 120, 200));
        UIManager.put("Button.background", new Color(120, 180, 255));
        UIManager.put("Button.foreground", Color.WHITE);

        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Multiplayer Dice Game");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(700, 500);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setLayout(new BorderLayout());

            JPanel startPanel = new JPanel();
            startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
            startPanel.setBackground(new Color(80, 120, 200));
            JLabel title = new JLabel("Multiplayer Dice Game", SwingConstants.CENTER);
            title.setFont(new Font("Montserrat", Font.BOLD, 32));
            title.setForeground(Color.WHITE);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            startPanel.add(Box.createVerticalStrut(40));
            startPanel.add(title);
            startPanel.add(Box.createVerticalStrut(30));

            JButton localBtn = new JButton("Local Game");
            localBtn.setFont(new Font("Montserrat", Font.BOLD, 22));
            localBtn.setBackground(new Color(0, 153, 255));
            localBtn.setForeground(Color.WHITE);
            localBtn.setFocusPainted(false);
            localBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            startPanel.add(localBtn);
            startPanel.add(Box.createVerticalStrut(20));

            JButton onlineBtn = new JButton("Online Game");
            onlineBtn.setFont(new Font("Montserrat", Font.BOLD, 22));
            onlineBtn.setBackground(new Color(76, 175, 80));
            onlineBtn.setForeground(Color.WHITE);
            onlineBtn.setFocusPainted(false);
            onlineBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            startPanel.add(onlineBtn);

            mainFrame.add(startPanel, BorderLayout.CENTER);
            mainFrame.setVisible(true);

            localBtn.addActionListener(e -> {
                startPanel.setVisible(false);
                JPanel setupPanel = new JPanel();
                setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.Y_AXIS));
                setupPanel.setBackground(new Color(80, 120, 200));
                JLabel numLabel = new JLabel("Enter number of players (2-5):", SwingConstants.CENTER);
                numLabel.setFont(new Font("Montserrat", Font.BOLD, 20));
                numLabel.setForeground(Color.WHITE);
                numLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                setupPanel.add(Box.createVerticalStrut(30));
                setupPanel.add(numLabel);
                JTextField numField = new JTextField();
                numField.setMaximumSize(new Dimension(120, 32));
                numField.setFont(new Font("Montserrat", Font.PLAIN, 18));
                setupPanel.add(numField);
                setupPanel.add(Box.createVerticalStrut(20));
                JButton nextBtn = new JButton("Next");
                nextBtn.setFont(new Font("Montserrat", Font.BOLD, 18));
                nextBtn.setBackground(new Color(0, 153, 255));
                nextBtn.setForeground(Color.WHITE);
                nextBtn.setFocusPainted(false);
                nextBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                setupPanel.add(nextBtn);
                mainFrame.add(setupPanel, BorderLayout.CENTER);
                mainFrame.revalidate();
                mainFrame.repaint();

                nextBtn.addActionListener(ev -> {
                    final int numPlayers;
                    try {
                        numPlayers = Integer.parseInt(numField.getText().trim());
                    } catch (NumberFormatException ex) {
                        numLabel.setText("Please enter a valid number (2-5):");
                        numLabel.setForeground(Color.YELLOW);
                        return;
                    }
                    if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
                        numLabel.setText("Please enter a valid number (2-5):");
                        numLabel.setForeground(Color.YELLOW);
                        return;
                    }
                    setupPanel.setVisible(false);
                    JPanel namePanel = new JPanel();
                    namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
                    namePanel.setBackground(new Color(80, 120, 200));
                    List<JTextField> nameFields = new ArrayList<>();
                    for (int i = 1; i <= numPlayers; i++) {
                        JLabel label = new JLabel("Enter name for Player " + i + ":", SwingConstants.CENTER);
                        label.setFont(new Font("Montserrat", Font.BOLD, 18));
                        label.setForeground(Color.WHITE);
                        label.setAlignmentX(Component.CENTER_ALIGNMENT);
                        namePanel.add(label);
                        JTextField field = new JTextField();
                        field.setMaximumSize(new Dimension(180, 32));
                        field.setFont(new Font("Montserrat", Font.PLAIN, 16));
                        namePanel.add(field);
                        nameFields.add(field);
                        namePanel.add(Box.createVerticalStrut(10));
                    }
                    JButton startGameBtn = new JButton("Start Game");
                    startGameBtn.setFont(new Font("Montserrat", Font.BOLD, 18));
                    startGameBtn.setBackground(new Color(0, 153, 255));
                    startGameBtn.setForeground(Color.WHITE);
                    startGameBtn.setFocusPainted(false);
                    startGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                    namePanel.add(startGameBtn);
                    mainFrame.add(namePanel, BorderLayout.CENTER);
                    mainFrame.revalidate();
                    mainFrame.repaint();

                    startGameBtn.addActionListener(ev2 -> {
                        List<Player> players = new ArrayList<>();
                        for (int i = 0; i < numPlayers; i++) {
                            String name = nameFields.get(i).getText().trim();
                            if (name.isEmpty()) name = "Player " + (i + 1);
                            players.add(new Player(name));
                        }
                        namePanel.setVisible(false);
                        GameWindow window = new GameWindow(players);
                        mainFrame.setContentPane(window);
                        mainFrame.setJMenuBar(window.getJMenuBar());
                        mainFrame.revalidate();
                        mainFrame.repaint();
                    });
                });
            });

            onlineBtn.addActionListener(e -> {
                startPanel.setVisible(false);
                NetworkLobby lobby = new NetworkLobby();
                mainFrame.setContentPane(lobby);
                mainFrame.setJMenuBar(lobby.getJMenuBar());
                mainFrame.revalidate();
                mainFrame.repaint();
            });
        });
    }

}
