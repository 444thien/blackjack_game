package black_jack;

import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Blackjack
{
    // Overlooking variables
    ArrayList<Cards> deck;
    Random random = new Random();

    // Player Variables
    ArrayList<Cards> playerHand;
    int playerSum;
    int playerAceCount;

    // Dealer Variables
    Cards hiddenCard;
    ArrayList<Cards> dealerHand;
    int dealerSum;
    int dealerAceCount;

    //Graphic Variables
    int boardWidth = 1280;
    int boardHeight = 720;
    int cardWidth = 110;    // ratio should be 1 : 1.4
    int cardHeight = 154;

    // Creating Blackjack Table
    JFrame frame = new JFrame("BlackJack");
    JPanel gamePanel = new JPanel()
    {
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            try
            {
                //Displaying hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if(!stayButton.isEnabled())
                {
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImgPath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

                //Displaying other dealer cards
                for(int i = 0; i < dealerHand.size(); i++)
                {
                    Cards card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImgPath())).getImage();
                    g.drawImage(cardImg, cardWidth + 25 + (cardWidth + 5)*i, 20, cardWidth, cardHeight, null);
                }

                //Displaying players hand
                for(int i = 0; i < playerHand.size(); i++)
                {
                    Cards card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImgPath())).getImage();
                    g.drawImage(cardImg, 20 + (cardWidth + 5)*i, 320, cardWidth, cardHeight, null);
                }

                // Displaying player hand's current value
                g.setFont(new Font("Arial",Font.PLAIN, 18));
                g.setColor(Color.white);
                g.drawString("Your current hand is: " + playerSum, 25, 500);

                // Once player hits stay
                if(!stayButton.isEnabled())
                {
                    // Reduce all aces if needed
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("STAY: ");
                    System.out.println(dealerSum);
                    System.out.println(playerSum);

                    // Deciding and displaying game results
                    String message;
                    if(playerSum > 21)
                    {
                        message = "You Lose.";
                    } else if (dealerSum > 21)
                    {
                        message = "You Win!";
                    } else if(playerSum == dealerSum)
                    {
                        message = "You've tied...";
                    } else if(playerSum > dealerSum)
                    {
                        message = "You Win!";
                    } else
                    {
                        message = "You Lose.";
                    }

                    // Displaying game result
                    g.setFont(new Font("Arial",Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };

    // Creating buttons
    JPanel button = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    JButton resetButton = new JButton("Play Again?");

    // Constructor (how the game is also ran)
    Blackjack()
    {
        startGame();

        // Setting the GUI
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        // Buttons
        hitButton.setFocusable(false);
        stayButton.setFocusable(false);
        resetButton.setFocusable(false);
        button.add(hitButton);
        button.add(stayButton);
        button.add(resetButton);
        resetButton.setEnabled(false);
        frame.add(button, BorderLayout.SOUTH);

        // Hit button logic
        hitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Updating player hand by adding last card from the deck to the player's hand
                Cards card = deck.removeLast();
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);

                // Checking to see if you've exceeded 21, if you have you can't hit again
                // You have to stand with what you have and lose
                if(reducePlayerAce() > 21)
                {
                    hitButton.setEnabled(false);
                }

                // Updating display
                gamePanel.repaint();
            }
        });

        // Stay button logic
        stayButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Once you hit stay (game ends), you can replay again
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                resetButton.setEnabled(true);

                // Rule for dealer stand at greater than 17
                while(dealerSum < 17)
                {
                    Cards card = deck.removeLast();
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                    dealerHand.add(card);
                }

                // Updating display
                gamePanel.repaint();
            }
        });

        // Play-again button logic
        resetButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Resetting buttons back to normal state
                hitButton.setEnabled(true);
                stayButton.setEnabled(true);
                resetButton.setEnabled(false);

                // Restarting game and updating display
                startGame();
                gamePanel.repaint();
            }
        });
    }

    public void startGame()
    {
        // Start with a deck and shuffle it
        buildDeck();
        shuffleDeck();

        // Dealer variables
        dealerHand = new ArrayList<Cards>();
        dealerSum = 0;
        dealerAceCount = 0;

        // Getting Hidden card for dealer, then getting the second card for
        // the beginning of the game
        hiddenCard = deck.removeLast();
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0; // Add 1 if it is an ace or else add 0

        Cards card_2 = deck.removeLast();
        dealerSum += card_2.getValue();
        dealerAceCount += card_2.isAce() ? 1 : 0;
        dealerHand.add(card_2);

        System.out.println("DEALER:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);

        // Doing the same but for the player hand
        playerHand = new ArrayList<Cards>();
        playerSum = 0;
        playerAceCount = 0;

        for(int i = 0; i < 2; i++)
        {
            Cards card = deck.removeLast();
            playerSum += card.getValue();
            playerAceCount += card_2.isAce() ? 1 : 0;
            playerHand.add(card);
        }

        System.out.println("PLAYER:");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);
    }

    // Building the entire deck of each card of each suit
    public void buildDeck()
    {
        deck = new ArrayList<Cards>();
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] suits = {"S", "C", "D", "H"};

        for (String suit : suits) {
            for (String value : values) {
                Cards addCard = new Cards(value, suit);
                deck.add(addCard);
            }
        }

        System.out.println("BUILD DECK");
        System.out.println(deck);
    }

    // Shuffling the deck by swapping
    public void shuffleDeck()
    {
        for(int i = 0; i < deck.size(); i++)
        {
            int randNum = random.nextInt(deck.size());
            Cards currCard = deck.get(i);
            Cards randomCard = deck.get(randNum);
            deck.set(i, randomCard);
            deck.set(randNum, currCard);
        }

        System.out.println("Shuffle Test");
        System.out.println(deck);
    }

    // Check to see if it's over 21, and we have an ace, this mean you
    // can have the ace be a 1 instead
    public int reducePlayerAce()
    {
        while(playerSum > 21 && playerAceCount > 0)
        {
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }

    // Same function as above but for dealer
    public int reduceDealerAce()
    {
        while(dealerSum > 21 && dealerAceCount > 0)
        {
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }
}
