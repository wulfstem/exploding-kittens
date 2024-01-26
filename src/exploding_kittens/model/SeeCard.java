package exploding_kittens.model;

public class SeeCard extends Card{


    private static final int VISION_OF_DRAW_PILE = 3;

    public SeeCard(cardType type, String cardName, Deck deck) {
        super(type, cardName, deck);
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
            player.getTui().showMessage(String.valueOf(result));
        }
    }
}
