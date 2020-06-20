package com.pokerace.gameplay.core.cards;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * Verified: OK
 * @author Administrator
 */
public class Card {

    public String ShortName;
    private CardNumberEnum cardNumber;
    private SuitesEnum suite;

    public Card() {
//        int pickCardNumber = new Random().nextInt(CardNumberEnum.values().length);
//        int pickSuite =  new Random().nextInt(SuitesEnum.values().length);
    }

    public Card(CardNumberEnum cardNumber, SuitesEnum suite) {
        this.cardNumber = cardNumber;
        this.suite = suite;
    }

    public CardNumberEnum getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(CardNumberEnum cardNumber) {
        this.cardNumber = cardNumber;
    }

    public SuitesEnum getSuite() {
        return suite;
    }

    public void setSuite(SuitesEnum suite) {
        this.suite = suite;
    }

    public static Card Copy(Card cardToCopy) {
        Card newCard = new Card(cardToCopy.cardNumber, cardToCopy.suite);

        return newCard;
    }

    public String ToString() {
        return this.cardNumber.toString() + " of " + this.suite.toString();
    }

    public String getShortName() {
        String result = "";

        switch (this.cardNumber) {
            case Two:
                result = "2";
                break;
            case Three:
                result = "3";
                break;
            case Four:
                result = "4";
                break;
            case Five:
                result = "5";
                break;
            case Six:
                result = "6";
                break;
            case Seven:
                result = "7";
                break;
            case Eight:
                result = "8";
                break;
            case Nine:
                result = "9";
                break;
            case Ten:
                result = "10";
                break;
            case Jack:
                result = "j";
                break;
            case Queen:
                result = "q";
                break;
            case King:
                result = "k";
                break;
            case Ace:
                result = "a";
                break;
            default:
                break;
        }
        switch (this.suite) {
            case Hearts:
                result = result + "h";
                break;
            case Diamonds:
                result = result + "d";
                break;
            case Clubs:
                result = result + "c";
                break;
            case Spades:
                result = result + "s";
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.cardNumber != null ? this.cardNumber.hashCode() : 0);
        hash = 41 * hash + (this.suite != null ? this.suite.hashCode() : 0);
        return hash;
    }

    /**
     * Cards are equal if their number and suite are equal
     * @param o
     * @return true if equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        Card card = (Card) o;
        return (this.cardNumber == card.cardNumber && this.suite == card.suite);
    }

    public enum CardNumberEnum {

        Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace
    }

    public enum SuitesEnum {

        //Hearts, Diamonds, Clubs, Spades
        Spades,Clubs,Diamonds,Hearts
    }
}