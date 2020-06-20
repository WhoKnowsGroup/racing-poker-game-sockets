package com.pokerace.gameplay.core.cards;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Comparator;

/**
 * Verified: OK
 * Compares two cards on their respective card numbers
 * @author Administrator
 */
public class CardNumberComparator implements Comparator<Card> {

    /**
     * Compares two card objects
     * @param o1 Card 1
     * @param o2 Card 2
     * @return +1 if card 1 number greater than card 2 number, returns -1 if card 1 number
     * is less than card 2 number and returns 0 if both card numbers are equal
     */
    @Override
    public int compare(Card o1, Card o2) {
        return o1.getCardNumber().compareTo(o2.getCardNumber());
    }
}
