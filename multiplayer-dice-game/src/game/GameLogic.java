package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameLogic {
    private List<Player> players;
    private int currentPlayerIndex;
    
    public GameLogic(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void rollDiceForCurrentPlayer(Dice dice) {
        Player currentPlayer = getCurrentPlayer();
        int rollResult = dice.roll();
        currentPlayer.updateScore(rollResult);
    }

    public Player determineWinner() {
        return Collections.max(players);
    }

    public boolean isGameOver() {
        // Implement game over logic if needed
        return false;
    }
}