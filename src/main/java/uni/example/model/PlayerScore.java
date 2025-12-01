package uni.example.model;


public class PlayerScore {

    private final String name;
    private final int wins;

    public PlayerScore(final String name, final int wins) {
        this.name = name;
        this.wins = wins;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }
}
