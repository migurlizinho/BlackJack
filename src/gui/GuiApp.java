package gui;

import game.BlackJack;
import game.Card;
import game.Deck;
import game.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    private static JPanel[] PlayerHandPanels;
    private static JPanel[] DealerHandPanels;
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
        PlayerHandPanels = cells[rows - 1];
        DealerHandPanels = cells[0];

        cells[rows - 2][0].add(new JLabel("Score:", SwingConstants.RIGHT));
        scoreLabel = new JLabel(String.valueOf(game.getPlayerScore()), SwingConstants.LEFT);
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
        hitButton.addActionListener(e -> {
            refreshPlayerHand();
        });
        buttonPanel.add(hitButton);

        JButton standButton = new JButton("Stand");
        buttonPanel.add(standButton);

        JButton splitButton = new JButton("Split");
        buttonPanel.add(splitButton);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {newHand();});
        buttonPanel.add(restartButton);
        return buttonPanel;
    }

    public static void restart(){
        for (JPanel cell : PlayerHandPanels) {
            cell.removeAll();
            cell.revalidate();
            cell.repaint();
        }
        for (JPanel cell : DealerHandPanels) {
            cell.removeAll();
            cell.revalidate();
            cell.repaint();
        }
    }

    public static void refreshScore(){
        scoreLabel.setText(String.valueOf(game.getPlayerScore()));
    }

    public static void refreshPlayerHand(){
        ArrayList<Card> hand = game.getPlayerHand();
        for (int i = 0; i < PlayerHandPanels.length && i < hand.size(); i++) {
            JPanel cell = PlayerHandPanels[i];
            cell.removeAll();
            cell.add(new JLabel(cards.get(hand.get(i))));
            cell.revalidate();
            cell.repaint();
        }
        refreshScore();
    }

    public static void newHand(){
        restart();
        ArrayList<Card> hand = game.getRandHand();
        JPanel HandCell1 = PlayerHandPanels[0];
        JPanel HandCell2 = PlayerHandPanels[1];
        HandCell1.removeAll();
        HandCell1.add(new JLabel(cards.get(hand.getFirst())));
        HandCell1.revalidate();
        HandCell1.repaint();

        HandCell2.removeAll();
        HandCell2.add(new JLabel(cards.get(hand.getLast())));
        HandCell2.revalidate();
        HandCell2.repaint();

        refreshScore();

        JPanel DealerCell1 = DealerHandPanels[0];
        JPanel DealerCell2 = DealerHandPanels[1];
        hand = game.getRandHand();
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
}
