import javax.swing.*;
import game.GameLogic;
import game.Player;
import gui.GameWindow;
import java.util.ArrayList;
import java.util.List;
import gui.NetworkLobby;
public class Main {
    private static final int MAX_PLAYERS = 5;
    private static final int MIN_PLAYERS = 2;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"Local Game", "Online Game"};
            int choice = JOptionPane.showOptionDialog(null,
                    "How do you want to play?",
                    "Multiplayer Dice Game",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (choice == 0) {
                // Local game logic (manual)
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
                List<Player> players = new ArrayList<>();
                for (int i = 1; i <= numPlayers; i++) {
                    String name = JOptionPane.showInputDialog(null, "Enter name for Player " + i + ":");
                    if (name == null || name.trim().isEmpty()) name = "Player " + i;
                    players.add(new Player(name));
                }
                new GameWindow(players);
            } else if (choice == 1) {
                // Online game logic (code)
                new NetworkLobby().setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
