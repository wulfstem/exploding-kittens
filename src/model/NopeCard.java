package model;

public class NopeCard extends Card{


    public NopeCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // deny the last card played except it's a bomb or a defuse
        if (player.getGame().getData1().getCardPlayed().getCardType().equals(CARD_TYPE.NOPE)){
            // WE DO SOMETHING DIFFERENT HERE CONVERTING LAST NOPE TO YES
        }
        else{
            Card lastCardPlayed = player.getGame().getData1().getCardPlayed();
            switch (lastCardPlayed.getCardType()) {
                case ATTACK2:
                    player.getGame().setCurrent(player.getGame().getData1().getCardUser().getPositionIndex());
                    if (player.getGame().getTurns() == 2){
                        player.getGame().setTurns(1);
                    }
                    else{
                        player.getGame().setTurns(player.getGame().getTurns() - 2);
                    }
                    break;
                case FAVOR:
                case REGULAR:
                    player.getGame().getData1().getCardTarget().getPlayerHand().getCardsInHand().add(player.getGame().getData1().getStolenCard());
                    player.getGame().getData1().getCardUser().getPlayerHand().getCardsInHand().remove(player.getGame().getData1().getStolenCard());
                    break;
                case SHUFFLE:
                    player.getGame().getDeck().setDrawPile(player.getGame().getData1().getDrawPileBeforeTurn());
                    break;
                case SKIP:
                    player.getGame().getData1().getCardUser().setSkipTurn(false);
                    player.getGame().setCurrent(player.getGame().getData1().getCardUser().getPositionIndex());
                    break;
                case SEE3:
                    // Not sure about this one
                    break;
                default:
                    // throw some exception
            }
        }
    }
}
