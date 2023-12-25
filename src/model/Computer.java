package model;

import java.util.ArrayList;

public class Computer extends Player{


    public static final String COMPUTER_NAME = "RoboTukas";

    public Computer(String playerName, Game game) {
        super(playerName, game);
    }

    @Override
    public void makeMove() {
        // Analyze the hand
        Hand hand = this.getPlayerHand();

        // Check for 'Defuse' card and use it if an Exploding Kitten is drawn
        if (this.getGame().getLastCardPlayed().getCardType() == Card.CARD_TYPE.BOMB && handContains(hand.getCardsInHand(), Card.CARD_TYPE.DEFUSE)) {
            playCard(Card.CARD_TYPE.DEFUSE);
            return;
        }

        // Use 'See the Future' if available and deck size is small
        if (this.getGame().getDeck().getSize() < 20 && handContains(hand.getCardsInHand(), Card.CARD_TYPE.SEE3)) {
            playCard(Card.CARD_TYPE.SEE3);
            return;
        }

        // Play 'Skip' or 'Attack' if available and the situation seems risky
        if (isSituationRisky() && (handContains(hand.getCardsInHand(), Card.CARD_TYPE.SKIP) || handContains(hand.getCardsInHand(), Card.CARD_TYPE.ATTACK2))) {
            playCard(handContains(hand.getCardsInHand(), Card.CARD_TYPE.SKIP) ? Card.CARD_TYPE.SKIP : Card.CARD_TYPE.ATTACK2);
            return;
        }

        // Draw a card if no other actions are taken
        this.draw();
    }

    private boolean handContains(ArrayList<Card> hand, Card.CARD_TYPE type) {
        // Check if the hand contains a card of the specified type
        return hand.stream().anyMatch(card -> card.getCardType() == type);
    }
        private void playCard(Card.CARD_TYPE cardType) {
            // Find the first card of the specified type in the hand
            Card cardToPlay = this.getPlayerHand().getCardsInHand().stream()
                    .filter(card -> card.getCardType() == cardType)
                    .findFirst()
                    .orElse(null);

            if (cardToPlay != null) {
                // Remove the card from the hand
                this.getPlayerHand().getCardsInHand().remove(cardToPlay);

                // Implement the action of the card
                cardToPlay.action(this); // Assuming action method is implemented in Card class

                // Additional game state updates can be added here if necessary
            }
        }
    private boolean isSituationRisky() {
        // Example condition: Consider the situation risky if the deck size is below a certain threshold
        final int RISK_THRESHOLD = 10; // This threshold can be adjusted based on game rules
        return this.getGame().getDeck().getSize() < RISK_THRESHOLD;
    }
}
