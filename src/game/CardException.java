package game;

public class CardException extends Exception {
    public CardException(String message) {
        super(message);
    }
    public CardException(Card card){
      super("Invalid card value : " + card);
    }
}
