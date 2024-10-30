package black_jack;

public class Cards {
    String value;
    String suit;

    Cards(String value, String suit)
    {
        this.value = value;
        this.suit = suit;
    }

    // Converting card into something readable
    public String toString()
    {
        return value + "-" + suit;
    }

    public int getValue()
    {
        // Returning for case if it is a face card
        if ("AJQK".contains(value))
        {
            if(value == "A")
            {
                return 11;
            }
            return 10;
        }
        // Value return for case it is a number card
        return Integer.parseInt(value);
    }

    // Checking to see if card is an ace
    public boolean isAce()
    {
        return value == "A";
    }

    // For GUI
    public String getImgPath()
    {
        return "./cards/" + toString() + ".png";
    }
}
