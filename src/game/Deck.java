package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;

public class Deck implements Iterable<Card>{
    ArrayList<Card> cards;

    public Deck(short size){
        cards = new ArrayList<>(size);
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    public Card get(Predicate<Card> predicate){
        for (Card card : cards) {
            if(predicate.test(card))
                return card;
        }
        return null;
    }

    public Card get(int index){
        return cards.get(index);
    }

    public Deck append(Card card){
        cards.add(card);
        return this;
    }

    public int getLength(){
        return cards.size();
    }
}