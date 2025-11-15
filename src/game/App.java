package game;

import java.util.ArrayList;
import java.util.Vector;

public class App {
    public static void main(String[] args) {
        BlackJack game = new BlackJack();
        ArrayList<Card> hand1 = game.getHand();
        System.out.println(hand1);

        for (Card card : game.getDeck()) {
            System.out.println(card);
        }
    }
}