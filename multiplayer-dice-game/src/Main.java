import javax.swing.*;
import game.GameLogic;
import game.Player;
import gui.GameWindow;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int MAX_PLAYERS = 5;
    private static final int MIN_PLAYERS = 2;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Prompt for number of players
            int numPlayers = 0;
            while (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
                String input = JOptionPane.showInputDialog(null, "Enter number of players (2-5):");
                if (input == null) System.exit(0);
                try {
                    numPlayers = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    numPlayers = 0;
                }
            }

            // Prompt for player names
            List<Player> players = new ArrayList<>();
            for (int i = 1; i <= numPlayers; i++) {
                String name = JOptionPane.showInputDialog(null, "Enter name for Player " + i + ":");
                if (name == null || name.trim().isEmpty()) name = "Player " + i;
                players.add(new Player(name));
            }

            // Start the game window
            new GameWindow(players);
        });
    }
}