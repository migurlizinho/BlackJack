package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackJack {
    public final static short BLACKJACK = 21;
    private final static short DEFAULT_DECK_SIZE = 52;
    private final Deck deck;
    private final static Random RANDOM = new Random(System.nanoTime());
    private final Player player;
    private final Player dealer;

    public final static short ROYALTY_VALUE = 10;

    public BlackJack(Deck deck){
        this.deck = deck;
        fillDeck();
        player = new Player(0);
        player.setHand(getRandHand());
        dealer = new Player(0);
        dealer.setHand(getRandHand());
    }

    public BlackJack(short deckSize){
        this(new Deck(deckSize));
    }

    public BlackJack(){
        this(new Deck(DEFAULT_DECK_SIZE));
    }

    public Deck getDeck() {
        return deck;
    }

    public boolean hit() {
        player.addCardToHand(getRandCard());
        return player.getScore() == 21;
    }

    public boolean stand(){
        if(dealer.getScore() > player.getScore())
            return false;
        while(dealer.getScore() < BLACKJACK && dealer.getScore() < player.getScore()){
            dealer.addCardToHand(getRandCard());
        }
        return dealer.getScore() > BLACKJACK || dealer.getScore() < player.getScore();
    }

    public ArrayList<Card> getPlayerHand(){
        return player.getHand();
    }

    public int getPlayerScore(){
        return player.getScore();
    }

    public void addRandCardToPlayer(){
        player.addCardToHand(getRandCard());
    }

    public ArrayList<Card> getDealerHand(){
        return dealer.getHand();
    }

    public int getDealerScore(){
        return dealer.getScore();
    }

    public ArrayList<Card> getRandHand(){
        ArrayList<Card> hand = new ArrayList<>(2);
        hand.add(deck.get(RANDOM.nextInt(deck.getLength())));
        hand.add(deck.get(RANDOM.nextInt(deck.getLength())));
        return hand;
    }

    public Card getRandCard(){
        Card card = deck.get(RANDOM.nextInt(deck.getLength()));
        deck.remove(card);
        return card;
    }

    private void fillDeck(){
        for (Card.Suit suit : Card.Suit.values()) {
            deck.append(new Ace(suit));
            for (Integer value : numbers) {
                try {
                    deck.append(new Number(value, suit));
                } catch (CardException e) {
                    throw new RuntimeException(e);
                }
            }
            deck.append(new Jack(suit)).append(new Queen(suit)).append(new King(suit));
        }
    }

    private static final List<Integer> numbers = List.of(2, 3, 4, 5, 6, 7, 8, 9, 10);

    public static class King extends Card{
        public King(Suit suit) {
            super( "King of " + Card.getSuitName(suit), ROYALTY_VALUE, suit);
        }
    }
    public static class Queen extends Card{
        public Queen(Suit suit) {
            super("Queen of " + Card.getSuitName(suit), ROYALTY_VALUE, suit);
        }
    }
    public static class Jack extends Card{
        public Jack(Suit suit) {
            super("Jack of " + Card.getSuitName(suit), ROYALTY_VALUE, suit);
        }
    }
    public static class Ace extends Card{
        public Ace(Suit suit) {
            super("Ace of " + Card.getSuitName(suit), 1, suit);
        }
        @Override
        public String toString() {
            return getName() + " : 1/11 points";
        }
    }
    public static class Number extends Card{
        public Number(int value, Suit suit) throws CardException {
            super(String.valueOf(value) + " of " + Card.getSuitName(suit), value, suit);
            if(value < 2 || value > 10)
                throw new CardException(this);
        }
    }
}