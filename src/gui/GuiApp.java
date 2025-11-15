package gui;

import game.BlackJack;
import game.Card;
import game.Deck;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiApp extends JFrame {
    public GuiApp(){
        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private final static HashMap<Card, ImageIcon> cards = new HashMap<>();
    private final static BlackJack game = new BlackJack();
    private final static int rows = 3, cols = 8;
    private final static JPanel[][] cells = new JPanel[rows][cols];
    private static JPanel HandCell1;
    private static JPanel HandCell2;
    private static JPanel DealerCell1;
    private static JPanel DealerCell2;
    private static final Player player = new Player(0);
    private static JLabel scoreLabel;

    public static void main(String[] args) {
        GuiApp app = new GuiApp();
        Container content = app.getContentPane();
        app.setLayout(new BorderLayout());

        //-------------------GET CARDS IMAGES BUFFERED-------------------
        ArrayList<ImageIcon> cardsIcons = null;
        try {
            cardsIcons = getCardsImages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage bf = null;
        try {
            bf = ImageIO.read(new File("data\\assets\\Top-Down\\Cards\\Empty-Cell-88x124.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon emptyCellIcon = new ImageIcon(bf);

        //------------------POPULATE CARDS MAP------------------------
        Deck deck = game.getDeck();
        for (int i = 0; i < deck.getLength(); i++) {
            cards.put(deck.get(i), cardsIcons.get(i));
        }

        // ----------------------GAME PANEL-------------------------
        JPanel gamePanel = new JPanel(new GridLayout(rows, cols));
        gamePanel.setBackground(new Color(0x008080));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new JPanel(new BorderLayout());
                cells[r][c].setOpaque(false);
                gamePanel.add(cells[r][c]);
            }
        }
        HandCell1 = cells[rows - 1][0];
        HandCell2 = cells[rows - 1][1];

        DealerCell1 = cells[0][0];
        DealerCell2 = cells[0][1];

        cells[rows - 2][0].add(new JLabel("Score:", SwingConstants.RIGHT));
        scoreLabel = new JLabel(String.valueOf(player.getScore()), SwingConstants.LEFT);
        cells[rows - 2][1].add(scoreLabel);

        content.add(gamePanel, BorderLayout.CENTER);

        // ----------------------BOTTOM MENU-------------------------
        JPanel buttonPanel = getButtonPanel();
        content.add(buttonPanel, BorderLayout.SOUTH);

        newHand();
        app.pack();
        app.setVisible(true);
    }

    private static JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton hitButton = new JButton("Hit");
        hitButton.addActionListener(e -> {newHand();});
        buttonPanel.add(hitButton);

        JButton standButton = new JButton("Stand");
        buttonPanel.add(standButton);

        JButton splitButton = new JButton("Split");
        buttonPanel.add(splitButton);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {restart();});
        buttonPanel.add(restartButton);
        return buttonPanel;
    }

    public static void restart(){
        HandCell1.removeAll();
        HandCell1.revalidate();
        HandCell1.repaint();

        HandCell2.removeAll();
        HandCell2.revalidate();
        HandCell2.repaint();

        DealerCell1.removeAll();
        DealerCell1.revalidate();
        DealerCell1.repaint();

        DealerCell2.removeAll();
        DealerCell2.revalidate();
        DealerCell2.repaint();
    }

    public static void refreshScore(){
        player.setScore(hand.getFirst().getValue() + hand.getLast().getValue());
        scoreLabel.setText(String.valueOf(player.getScore()));
    }

    public static void newHand(){
        ArrayList<Card> hand = game.getHand();
        HandCell1.removeAll();
        HandCell1.add(new JLabel(cards.get(hand.getFirst())));
        HandCell1.revalidate();
        HandCell1.repaint();

        HandCell2.removeAll();
        HandCell2.add(new JLabel(cards.get(hand.getLast())));
        HandCell2.revalidate();
        HandCell2.repaint();

        refreshScore();

        hand = game.getHand();
        DealerCell1.removeAll();
        DealerCell1.add(new JLabel(cards.get(hand.getFirst())));
        DealerCell1.revalidate();
        DealerCell1.repaint();

        DealerCell2.removeAll();
        DealerCell2.add(new JLabel(cards.get(hand.getLast())));
        DealerCell2.revalidate();
        DealerCell2.repaint();
    }

    public static ArrayList<ImageIcon> getCardsImages() throws IOException {
        List<String> suits = List.of("Spades", "Clubs", "Diamonds", "Hearts");
        ArrayList<ImageIcon> cards = new ArrayList<>();

        int cardWidth = 88;   // example values
        int cardHeight = 124;

        int rows = 3;
        int cols = 5;
        for(int i = 0; i < 4; i++){
            BufferedImage sheet = ImageIO.read(new File("data\\assets\\Top-Down\\Cards\\" + suits.get(i) + "-88x124.png"));
            for (int y = 0; y < rows; y++) {        // rows (suits)
                for (int x = 0; x < cols; x++) {   // columns (ranks)
                    if(y == 2 && x == 3)
                        break;
                    BufferedImage card = sheet.getSubimage(
                            x * cardWidth,
                            y * cardHeight,
                            cardWidth,
                            cardHeight
                    );
                    cards.add(new ImageIcon(card));
                }
            }
        }

        return cards;
    }

    public static class Player{
        private int score;
        private final ArrayList<Card> hand = new ArrayList<>(2);
        public Player(int score){
            this.score = score;
        }
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
        public ArrayList<Card> getHand() { return hand; }

        public void setHand(ArrayList<Card> newHand){
            hand.set(0, newHand.getFirst());
            hand.set(1, newHand.getLast());
        }
    }
}
