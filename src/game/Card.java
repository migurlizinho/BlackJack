package game;

public abstract class Card {
    private final String name;
    private final int value;
    protected enum Suit {
        SPADES, CLUBS, DIAMONDS, HEARTS
    }
    private final Suit suit;

    public Card(String name, int value, Suit suit){
        this.name = name;
        this.value = value;
        this.suit = suit;
    }

    public int getValue(){ return value; }

    public final String getName() { return name; }

    public static String getSuitName(Suit suit){
        return (suit == Suit.SPADES)?"Spades":(suit == Suit.CLUBS)?"Clubs":(suit == Suit.HEARTS)?"Hearts":"Diamonds";
    }

    public String getSuitName(){
        return Card.getSuitName(this.suit);
    }

    public static int add(Card c1, Card c2){
        return c1.getValue() + c2.getValue();
    }

    @Override
    public String toString() {
        return name + " : " + value + " points";
    }
}
