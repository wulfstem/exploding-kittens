package model;

public class See3Card extends Card{


    private static final int VISION_OF_DRAW_PILE = 3;

    public See3Card(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // see the top 3 cards of the drawPile
        if (!(player instanceof Computer)){
            StringBuilder result = new StringBuilder();
            if (player.getGame().getDeck().getDrawPile().size() >= 3){
                for (int i = 0; i < VISION_OF_DRAW_PILE; i++){
                    result.append(player.getGame().getDeck().getDrawPile().get(i).getCardName()).append(" (").append(player.getGame().getDeck().getDrawPile().get(i).getCardType()).append(") |");
                }
            }
            else{
                for (int i = 0; i < player.getGame().getDeck().getDrawPile().size(); i++){
                    result.append(player.getGame().getDeck().getDrawPile().get(i).getCardName()).append(" (").append(player.getGame().getDeck().getDrawPile().get(i).getCardType()).append(") |");
                }
            }
            System.out.println(result);
        }
    }
}
