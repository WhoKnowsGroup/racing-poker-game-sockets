package com.pokerace.gameplay.core.cards;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 * Verified: OK
 * @author User
 */
public class DealHistoryRecord implements Serializable{

    private int dealNumber;
    private String hand;
    private String card1;
    private String card2;
    private String card3;
    private String card4;
    private String card5;

    public DealHistoryRecord() {
    }

    public DealHistoryRecord(int dealNumber, String hand, String card1, String card2, String card3,
            String card4, String card5) {
        this.dealNumber = dealNumber;
        this.hand = hand;
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
        this.card4 = card4;
        this.card5 = card5;
    }

    public String getCard1() {
        return card1;
    }

    public String getCard2() {
        return card2;
    }

    public String getCard3() {
        return card3;
    }

    public String getCard4() {
        return card4;
    }

    public String getCard5() {
        return card5;
    }

    public int getDealNumber() {
        return dealNumber;
    }

    public String getHand() {
        return hand;
    }
}
