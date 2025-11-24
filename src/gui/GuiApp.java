package gui;

import game.BlackJack;
import game.Card;
import game.Deck;

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
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private final static HashMap<Card, ImagePanel> cards = new HashMap<>();
    private final static BlackJack game = new BlackJack();
    private final static int rows = 3, cols = 8;
    private static JPanel[] PlayerHandPanels;
    private static JPanel[] DealerHandPanels;
    private static JLabel playerScoreLabel;
    private static JLabel dealerScoreLabel;
    private static Container content;
    private static boolean isRunning = true;

    private static void messageDialog(String s, GuiApp app){
        JOptionPane.showMessageDialog(app, s, "Game end", JOptionPane.INFORMATION_MESSAGE);
        isRunning = false;
    }

    public static void main(String[] args) {
        GuiApp app = new GuiApp();
        content = app.getContentPane();
        app.setLayout(new BorderLayout());
        game.setDrawHandler(() -> { messageDialog("Draw", app);});
        game.setLoseHandler(() -> { messageDialog("Lose", app);});
        game.setWinHandler(()  -> { messageDialog("Win", app);});

        //-------------------GET CARDS IMAGES BUFFERED-------------------
        ArrayList<ImagePanel> cardsIcons = null;
        try {
            cardsIcons = getCardsImages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //------------------POPULATE CARDS MAP------------------------
        Deck deck = game.getDeck();
        for (int i = 0; i < deck.getLength(); i++) {
            cards.put(deck.get(i), cardsIcons.get(i));
        }

        // ----------------------GAME PANEL-------------------------
        JPanel gamePanel = new JPanel(new GridLayout(rows, cols));
        gamePanel.setBackground(new Color(0x008080));

        JPanel[][] cells = new JPanel[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new JPanel(new BorderLayout());
                cells[r][c].setOpaque(false);
                gamePanel.add(cells[r][c]);
            }
        }
        PlayerHandPanels = cells[rows - 1];
        DealerHandPanels = cells[0];

        JPanel scoreTextPanel = new JPanel(new BorderLayout());
        JPanel scoreValuesPanel = new JPanel(new BorderLayout());
        scoreValuesPanel.setOpaque(false);
        scoreTextPanel.setOpaque(false);
        cells[1][0].add(scoreTextPanel);
        cells[1][1].add(scoreValuesPanel);
        Font font = new Font("JetBrains Mono", Font.BOLD, 20);

        JLabel l1 = new JLabel("Your Score: ", SwingConstants.RIGHT);
        l1.setFont(font);
        scoreTextPanel.add(l1, BorderLayout.SOUTH);
        playerScoreLabel = new JLabel(String.valueOf(game.getPlayerScore()), SwingConstants.LEFT);
        playerScoreLabel.setFont(font);
        scoreValuesPanel.add(playerScoreLabel, BorderLayout.SOUTH);

        JLabel l2 = new JLabel("Dealer Score: ", SwingConstants.RIGHT);
        l2.setFont(font);
        scoreTextPanel.add(l2, BorderLayout.NORTH);
        dealerScoreLabel = new JLabel(String.valueOf(game.getDealerScore()), SwingConstants.LEFT);
        dealerScoreLabel.setFont(font);
        scoreValuesPanel.add(dealerScoreLabel, BorderLayout.NORTH);

        content.add(gamePanel, BorderLayout.CENTER);

        // ----------------------BOTTOM MENU-------------------------
        JPanel buttonPanel = getButtonPanel();
        content.add(buttonPanel, BorderLayout.SOUTH);

        refreshHands();
        app.setVisible(true);
    }

    private static JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setPreferredSize(new Dimension(1280, 50));

        JButton hitButton = new JButton("Hit");
        hitButton.addActionListener((actionEvent) -> {
            if(isRunning)
                game.hit(GuiApp::refreshHands);
        });
        hitButton.setPreferredSize(new Dimension(400, 40));
        buttonPanel.add(hitButton);

        JButton standButton = new JButton("Stand");
        standButton.addActionListener((actionEvent) -> {
            if(isRunning)
                game.stand(GuiApp::refreshHands);
        });
        standButton.setPreferredSize(new Dimension(400, 40));
        buttonPanel.add(standButton);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {restart();});
        restartButton.setPreferredSize(new Dimension(400, 40));
        buttonPanel.add(restartButton);
        return buttonPanel;
    }

    public static void restart(){
        game.restart();
        refreshHands();
        refreshScore();
//        System.out.println(game.getDealerHandClone());
//        System.out.println(game.getPlayerHandClone());
        isRunning = true;
    }

    public static void refreshScore(){
        playerScoreLabel.setText(String.valueOf(game.getPlayerScore()));
        dealerScoreLabel.setText(String.valueOf(game.getDealerScore()));
    }

    public static void refreshHands(){
        ArrayList<Card> hand = (ArrayList<Card>) game.getPlayerHandClone();
        for (int i = 0; i < PlayerHandPanels.length; i++) {
            JPanel cell = PlayerHandPanels[i];
            cell.removeAll();
            if(hand.size() > i)
                cell.add(cards.get(hand.get(i)));
            cell.revalidate();
            cell.repaint();
        }
        refreshScore();
        hand = (ArrayList<Card>) game.getDealerHandClone();
        for (int i = 0; i < DealerHandPanels.length; i++) {
            JPanel cell = DealerHandPanels[i];
            cell.removeAll();
            if(hand.size() > i)
                cell.add(cards.get(hand.get(i)));
            cell.revalidate();
            cell.repaint();
        }
    }

    public static ArrayList<ImagePanel> getCardsImages() throws IOException {
        List<String> suits = List.of("Spades", "Clubs", "Diamonds", "Hearts");
        ArrayList<ImagePanel> cards = new ArrayList<>();
        int cardWidth = 88;   // example values
        int cardHeight = 124;
        int rows = 3;
        int cols = 5;
        for(int i = 0; i < 4; i++){
            BufferedImage sheet = ImageIO.read(new File("data/assets/Top-Down/Cards/" + suits.get(i) + "-88x124.png"));
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
                    cards.add(new ImagePanel(card));
                }
            }
        }
        return cards;
    }
}
