package model;

import java.util.ArrayList;

public class DataOfLastCardPlayed {


    private Player cardUser;
    private Player cardTarget;
    private Card cardPlayed;
    private Card stolenCard;
    private Card[] comboPlayed;
    private ArrayList<Card> drawPileBeforeTurn;

    public DataOfLastCardPlayed(){
        drawPileBeforeTurn = new ArrayList<>();
    }

    public Player getCardUser() {
        return cardUser;
    }

    public void setCardUser(Player cardUser) {
        this.cardUser = cardUser;
    }

    public Player getCardTarget() {
        return cardTarget;
    }

    public void setCardTarget(Player cardTarget) {
        this.cardTarget = cardTarget;
    }

    public Card getCardPlayed() {
        return cardPlayed;
    }

    public void setCardPlayed(Card cardPlayed) {
        this.cardPlayed = cardPlayed;
    }

    public Card getStolenCard() {
        return stolenCard;
    }

    public void setStolenCard(Card stolenCard) {
        this.stolenCard = stolenCard;
    }

    public Card[] getComboPlayed() {
        return comboPlayed;
    }

    public void setComboPlayed(Card comboPlayed1, Card comboPlayed2) {
        if (comboPlayed1 != null && comboPlayed2 != null){
            this.comboPlayed[0] = comboPlayed1;
            this.comboPlayed[1] = comboPlayed2;
        }
        else{
            this.comboPlayed = null;
        }
    }

    public ArrayList<Card> getDrawPileBeforeTurn() {
        return drawPileBeforeTurn;
    }

    public void setDrawPileBeforeTurn(ArrayList<Card> drawPileBeforeTurn) {
        this.drawPileBeforeTurn = drawPileBeforeTurn;
    }

    public void mergeData(Player cardUser, Player cardTarget, Card cardPlayed, Card stolenCard, Card[] comboPlayed, ArrayList<Card> drawPileBeforeTurn){
        this.cardUser = cardUser;
        this.cardTarget = cardTarget;
        this.cardPlayed = cardPlayed;
        this.stolenCard = stolenCard;
        this.comboPlayed = comboPlayed;
        this.drawPileBeforeTurn = drawPileBeforeTurn;
    }
}
