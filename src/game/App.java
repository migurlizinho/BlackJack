package game;

import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private static BlackJack game = new BlackJack();

    public static void printInfo(){
        System.out.println("[" + game.getDealerScore() + "]" + " Dealer :" + game.getDealerHandClone());
        System.out.println("[" + game.getPlayerScore() + "]" + " Player :" + game.getPlayerHandClone());
    }

    public static void main(String[] args) {
        game.setWinHandler(() -> {
            printInfo();
            System.out.println("Game Won");
            System.exit(0);
        });
        game.setDrawHandler(() -> {
            printInfo();
            System.out.println("Game Drawn");
            System.exit(0);
        });
        game.setLoseHandler(() -> {
            printInfo();
            System.out.println("Game Lost");
            System.exit(0);
        });
        Scanner sc = new Scanner(System.in);
        printInfo();
        while(true) {
            switch (sc.nextLine()) {
                case "h":
                    game.hit();
                    break;
                case "s":
                    game.stand();
                    break;
            }
            printInfo();
        }
    }
}