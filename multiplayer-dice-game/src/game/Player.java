package game;

public class Player implements Comparable<Player> {
    private String name;
    private int score;
    private int roll;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.roll = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void updateScore(int score) {
        this.score += score;
    }

    public void resetScore() {
        this.score = 0;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(this.roll, other.roll);
    }
}