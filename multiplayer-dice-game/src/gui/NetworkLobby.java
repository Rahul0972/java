package gui;
import java.util.ArrayList;

import network.GameServer;
import network.GameClient;
import network.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class NetworkLobby extends JPanel {
    private JMenuBar menuBar;
    private ImageIcon backgroundIcon;
    public NetworkLobby() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        loadBackgroundImage();
        setupMenu();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Multiplayer Dice Game", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        JButton hostBtn = new JButton("Host Game");
        styleButton(hostBtn, new Color(0, 153, 255), Color.WHITE);
        gbc.gridy = 1; gbc.gridwidth = 2;
        add(hostBtn, gbc);

        JButton startBtn = new JButton("Start Game");
        styleButton(startBtn, new Color(76, 175, 80), Color.WHITE);
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
        styleButton(joinBtn, new Color(244, 67, 54), Color.WHITE);
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        add(joinBtn, gbc);

        // Host button action
        hostBtn.addActionListener(e -> {
            new Thread(() -> {
                try {
                    GameServer server = new GameServer();
                    String ip = network.NetworkUtils.getLocalIpAddress();
                    SwingUtilities.invokeLater(() ->
                        showCustomDialog("Share this code with your friend: " + server.getCode() + "\nYour IP: " + ip, "Host Info")
                    );
                    server.waitForClient();
                    SwingUtilities.invokeLater(() -> {
                        showCustomDialog("Player joined! Click 'Start Game' to begin.", "Info");
                        startBtn.setEnabled(true);
                        startBtn.putClientProperty("server", server);
                    });
                } catch (IOException ex) {
                    SwingUtilities.invokeLater(() ->
                        showCustomDialog("Error hosting game: " + ex.getMessage(), "Error")
                    );
                }
            }).start();
        });

        startBtn.addActionListener(e -> {
            GameServer server = (GameServer) startBtn.getClientProperty("server");
            if (server != null) {
                showCustomDialog("Game started! (Integrate with GameWindow)", "Info");
                // new GameWindow(...)
                // Remove this panel from parent
                Container parent = getParent();
                if (parent instanceof JFrame) {
                    JFrame frame = (JFrame) parent;
                    frame.setContentPane(new GameWindow(new ArrayList<>()));
                    frame.setJMenuBar(new GameWindow(new ArrayList<>()).getJMenuBar());
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        // Join button action
        joinBtn.addActionListener(e -> {
            String codeText = codeField.getText().trim();
            String ip = ipField.getText().trim();
            if (codeText.length() != 5 || !codeText.matches("\\d{5}")) {
                showCustomDialog("Please enter a valid 5-digit code.", "Error");
                return;
            }
            try {
                int code = Integer.parseInt(codeText);
                GameClient client = new GameClient(ip, code);
                showCustomDialog("Connected! Waiting for host to start the game.", "Info");
                // new GameWindow(...)
                Container parent = getParent();
                if (parent instanceof JFrame) {
                    JFrame frame = (JFrame) parent;
                    frame.setContentPane(new GameWindow(new ArrayList<>()));
                    frame.setJMenuBar(new GameWindow(new ArrayList<>()).getJMenuBar());
                    frame.revalidate();
                    frame.repaint();
                }
            } catch (IOException ex) {
                showCustomDialog("Could not connect: " + ex.getMessage(), "Error");
            }
        });
    }

    // Load background image
    private void loadBackgroundImage() {
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundIcon != null) {
            g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Custom dialog with themed background
    private void showCustomDialog(String message, String title) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), title, true);
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundIcon != null) {
                    g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(new BorderLayout());
        JLabel msgLabel = new JLabel("<html><div style='text-align:center; color:white; font-size:18px;'>" + message.replace("\n", "<br>") + "</div></html>", SwingConstants.CENTER);
        msgLabel.setFont(new Font("Montserrat", Font.BOLD, 18));
        panel.add(msgLabel, BorderLayout.CENTER);
        JButton okBtn = new JButton("OK");
        styleButton(okBtn, new Color(0, 153, 255), Color.WHITE);
        okBtn.addActionListener(e -> dialog.dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(okBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        dialog.setContentPane(panel);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(false);
        dialog.setVisible(true);
    }

    private void setupMenu() {
        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(80, 120, 200));
        JMenu gameMenu = new JMenu("Menu");
        gameMenu.setFont(new Font("Montserrat", Font.BOLD, 18));
        gameMenu.setForeground(Color.WHITE);
        JMenuItem switchToLocal = new JMenuItem("Switch to Local Game");
        styleMenuItem(switchToLocal);
        switchToLocal.addActionListener(e -> {
            gui.Main.main(new String[]{});
        });
        gameMenu.add(switchToLocal);
        menuBar.add(gameMenu);
    }

    private void styleMenuItem(JMenuItem item) {
        item.setFont(new Font("Montserrat", Font.BOLD, 18));
        item.setBackground(new Color(0, 153, 255));
        item.setForeground(Color.WHITE);
        item.setOpaque(true);
    }

    public JMenuBar getJMenuBar() {
        return menuBar;
    }

    // Modern button styling (same as GameWindow)
    private void styleButton(JButton button, Color bg, Color fg) {
        Font btnFont;
        try {
            btnFont = new Font("Montserrat", Font.BOLD, 20);
        } catch (Exception e) {
            btnFont = new Font("Segoe UI", Font.BOLD, 20);
        }
        button.setFont(btnFont);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 48));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createLineBorder(bg.brighter(), 3, true));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
            }
        });
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = b.getWidth(), h = b.getHeight();
                Color grad1 = bg;
                Color grad2 = bg.brighter();
                GradientPaint gp = new GradientPaint(0, 0, grad1, 0, h, grad2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h, 28, 28);
                g2.setColor(bg.darker());
                g2.drawRoundRect(0, 0, w-1, h-1, 28, 28);
                g2.dispose();
                super.paint(g, c);
            }
        });
    }
}
