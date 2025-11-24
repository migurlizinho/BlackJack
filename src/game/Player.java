package game;

import java.util.ArrayList;
import java.util.Iterator;

public class Player implements Iterable<Card>{
    private int score;
    private boolean isRefreshed = true;
    private ArrayList<Card> hand = new ArrayList<>(2);
    public Player(int score){
        this.score = score;
    }

    public int getScore() {
        if(!isRefreshed)
            refreshScore();
        return score;
    }

    public void refreshScore(){
        score = 0;
        for (Card card : hand) {
            score += card.getValue();
        }
        isRefreshed = true;
    }
    public void setScore(int score) { this.score = score; }

    public Object cloneHand() {
        return hand.clone();
    }

    public void setHand(ArrayList<Card> newHand){
        hand = newHand;
        isRefreshed = false;
    }

    public Player addCardToHand(Card card){
        int v = card.getValue();
        if(card instanceof BlackJack.Ace && score + 11 < BlackJack.BLACKJACK)
            v = 11;
        hand.add(card);
        score += v;
        isRefreshed = false;
        return this;
    }

    @Override
    public Iterator<Card> iterator() {
        return hand.iterator();
    }
}
