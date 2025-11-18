package game;

import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private static BlackJack game = new BlackJack();

    public static void printInfo(){
        System.out.println("[" + game.getDealerScore() + "]" + " Dealer :" + game.getDealerHand());
        System.out.println("[" + game.getPlayerScore() + "]" + " Player :" + game.getPlayerHand());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        printInfo();
        while(true) {
            switch (sc.nextLine()) {
                case "h":
                    if (game.hit()) {
                        printInfo();
                        System.out.println("Game Won");
                        return;
                    }
                    if(game.getPlayerScore() > 21){
                        printInfo();
                        System.out.println("Game Lost");
                        return;
                    }
                    break;
                case "s":
                    if (game.stand()) {
                        printInfo();
                        System.out.println("Game Won");
                        return;
                    }else{
                        printInfo();
                        System.out.println("Game Lost");
                        return;
                    }
            }
            printInfo();
        }
    }
}