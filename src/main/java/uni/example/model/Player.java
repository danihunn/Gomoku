package uni.example.model;

import java.util.Objects;

public class Player {

    private final String name;
    private final char symbol;

    public Player(final String name, final char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return name + " (" + symbol + ")";
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj instanceof Player) {
            final Player other = (Player) obj;
            result = Objects.equals(name, other.name) && symbol == other.symbol;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, symbol);
    }
}
