package gui;

import network.GameServer;
import network.GameClient;
import network.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class NetworkLobby extends JFrame {
    public NetworkLobby() {
        setTitle("Multiplayer Dice Game - Lobby");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Multiplayer Dice Game", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        JButton hostBtn = new JButton("Host Game");
        hostBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridy = 1; gbc.gridwidth = 2;
        add(hostBtn, gbc);

        JButton startBtn = new JButton("Start Game");
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        startBtn.setEnabled(false);
        gbc.gridy = 6; gbc.gridwidth = 2;
        add(startBtn, gbc);

        JLabel orLabel = new JLabel("or", SwingConstants.CENTER);
        orLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 2; gbc.gridwidth = 2;
        add(orLabel, gbc);

        JLabel codeLabel = new JLabel("Enter 5-digit code:");
        codeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridx = 0;
        add(codeLabel, gbc);

        JTextField codeField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 3;
        add(codeField, gbc);

        JLabel ipLabel = new JLabel("Host IP:");
        ipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 4; gbc.gridx = 0;
        add(ipLabel, gbc);

        JTextField ipField = new JTextField("127.0.0.1");
        gbc.gridx = 1; gbc.gridy = 4;
        add(ipField, gbc);

        JButton joinBtn = new JButton("Join Game");
        joinBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        add(joinBtn, gbc);

        // Host button action
        hostBtn.addActionListener(e -> {
            new Thread(() -> {
                try {
                    GameServer server = new GameServer();
                    String ip = NetworkUtils.getLocalIpAddress();
                    SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Share this code with your friend: " + server.getCode() + "\nYour IP: " + ip, "Host Info", JOptionPane.INFORMATION_MESSAGE)
                    );
                    server.waitForClient();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Player joined! Click 'Start Game' to begin.");
                        startBtn.setEnabled(true);
                        startBtn.putClientProperty("server", server);
                    });
                } catch (IOException ex) {
                    SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Error hosting game: " + ex.getMessage())
                    );
                }
            }).start();
        });

        startBtn.addActionListener(e -> {
            GameServer server = (GameServer) startBtn.getClientProperty("server");
            if (server != null) {
                // TODO: Launch game window for host and start the game
                JOptionPane.showMessageDialog(this, "Game started! (Integrate with GameWindow)");
                // new GameWindow(...)
                this.dispose();
            }
        });

        // Join button action
        joinBtn.addActionListener(e -> {
            String codeText = codeField.getText().trim();
            String ip = ipField.getText().trim();
            if (codeText.length() != 5 || !codeText.matches("\\d{5}")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid 5-digit code.");
                return;
            }
            try {
                int code = Integer.parseInt(codeText);
                GameClient client = new GameClient(ip, code);
                JOptionPane.showMessageDialog(this, "Connected! Waiting for host to start the game.");
                // TODO: Launch game window for client
                // new GameWindow(...)
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Could not connect: " + ex.getMessage());
            }
        });
    }
}
