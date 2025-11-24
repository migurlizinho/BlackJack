package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackJack {
    public enum GAME_STATE{
        ONGOING, WIN, LOSE, DRAW
    }
    private GAME_STATE state = GAME_STATE.ONGOING;
    public final static short BLACKJACK = 21;
    private final static short DEFAULT_DECK_SIZE = 52;
    private final Deck deck;
    private final static Random RANDOM = new Random(System.nanoTime());
    private final Player player;
    private final Player dealer;
    private Runnable WinHandler = null;
    private Runnable LoseHandler = null;
    private Runnable DrawHandler = null;
    private ArrayList<Card> usedCards = new ArrayList<>();

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

    private void resolveGameState(){
        switch (state){
            case WIN -> {if(WinHandler != null) WinHandler.run();}
            case LOSE -> {if(LoseHandler != null) LoseHandler.run();}
            case DRAW -> {if(DrawHandler != null) DrawHandler.run();}
        }
    }

    private void phit() {
        player.addCardToHand(getRandCard());
        int s = player.getScore();
        state = (s == BLACKJACK)? GAME_STATE.WIN : (s > BLACKJACK)? GAME_STATE.LOSE : GAME_STATE.ONGOING;
    }
    public void hit(){
        phit();
        resolveGameState();
    }
    public void hit(Runnable innerAction){
        phit();
        innerAction.run();
        resolveGameState();
    }

    private void pstand(){
        if(dealer.getScore() > player.getScore()){
            return;
        }
        if(dealer.getScore() == player.getScore()){
            int diff = BLACKJACK - dealer.getScore();
            if(diff < 10){
                state = GAME_STATE.DRAW;
                return;
            }
        }

        while(dealer.getScore() < BLACKJACK && dealer.getScore() <= player.getScore()){
            dealer.addCardToHand(getRandCard());
        }
        state = (dealer.getScore() > BLACKJACK)? GAME_STATE.WIN : (dealer.getScore() == player.getScore())? GAME_STATE.DRAW : GAME_STATE.LOSE;
    }
    public void stand(){
        pstand();
        resolveGameState();
    }
    public void stand(Runnable innerAction){
        pstand();
        innerAction.run();
        resolveGameState();
    }

    public void restart(){
        player.setHand(getRandHand());
        dealer.setHand(getRandHand());
        state = GAME_STATE.ONGOING;
        for (int i = 0; i < usedCards.size(); i++) {
            usedCards.remove(i);
        }
    }

    public void setWinHandler(Runnable action){
        WinHandler = action;
    }
    public void setLoseHandler(Runnable action){
        LoseHandler = action;
    }
    public void setDrawHandler(Runnable action){
        DrawHandler = action;
    }

    public boolean gameStateIs(GAME_STATE state){
        return this.state == state;
    }

    public GAME_STATE getGameState(){
        return state;
    }

    public Object getPlayerHandClone(){
        return player.cloneHand();
    }

    public int getPlayerScore(){
        return player.getScore();
    }

    public void addRandCardToPlayer(){player.addCardToHand(getRandCard());}

    public Object getDealerHandClone(){
        return dealer.cloneHand();
    }

    public int getDealerScore(){
        return dealer.getScore();
    }

    public ArrayList<Card> getRandHand(){
        ArrayList<Card> hand = new ArrayList<>(2);
        hand.add(getRandCard());
        hand.add(getRandCard());
        return hand;
    }

    public Card getRandCard(){
        Card card = deck.get(RANDOM.nextInt(deck.getLength()));
        while(usedCards.contains(card)){
            card = deck.get(RANDOM.nextInt(deck.getLength()));
        }
        usedCards.add(card);
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
    }
    public static class Number extends Card{
        public Number(int value, Suit suit) throws CardException {
            super(String.valueOf(value) + " of " + Card.getSuitName(suit), value, suit);
            if(value < 2 || value > 10)
                throw new CardException(this);
        }
    }
}