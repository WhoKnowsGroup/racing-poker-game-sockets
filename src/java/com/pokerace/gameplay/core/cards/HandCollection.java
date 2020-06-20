package com.pokerace.gameplay.core.cards;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Verified: OK
 * @author User
 */
public class HandCollection extends ArrayList<Hand> {

    @Override
    public boolean add(Hand hand) {
        return super.add(hand);
    }

    /**
     * Overrides the get method of the parent ArrayList class. This is used 
     * for looping through all hands only. THIS MEHTOD MUST NOT BE USED TO FETCH
     * A HAND BY A GIVEN HAND NUMBBER.
     * @param index The index of the element in the ArrayList.
     * @return The Hand present in list at the index passed
     */
    @Override
    public Hand get(int index) {
        //return super.get(index - 1); //Original C# code. Subtract unity to convert to real index
        return super.get(index);
    }
    
    public Hand getHand(int index){
        return super.get(index - 1);
    }
    
    public Hand find(int handNumber) {
        Hand hand;

        Iterator<Hand> iterator = super.iterator();
        while (iterator.hasNext()) {
            hand = iterator.next();
            if (hand.getHandNumber() == handNumber) {
                return hand;
            }
        }
        return null;
    }

    /**
     * Returns number of hands that are straight flush
     * @param hands Collection to count in
     * @return number hands that are straight flush
     */
    public static int numStraightFlush(HandCollection hands) {
        int count = 0;
        Hand hand;

        Iterator<Hand> iterator = hands.iterator();
        while (iterator.hasNext()) {
            hand = iterator.next();
            if (hand.isStraightFlush()) {
                count++;
            }
        }

        return count;
    }

    public static int numFourOfAKind(HandCollection hands) {
        int count = 0;
        Hand hand;

        Iterator<Hand> iterator = hands.iterator();
        while (iterator.hasNext()) {
            hand = iterator.next();
            if (hand.isQuadruple()) {
                count++;
            }
        }

        return count;
    }

    public static int numFullHouse(HandCollection hands) {
        int count = 0;
        Hand hand;

        Iterator<Hand> iterator = hands.iterator();
        while (iterator.hasNext()) {
            hand = iterator.next();
            if (hand.isFullHouse()) {
                count++;
            }
        }

        return count;
    }

    public static int numFlush(HandCollection hands) {
        int count = 0;
        Hand hand;

        Iterator<Hand> iterator = hands.iterator();
        while (iterator.hasNext()) {
            hand = iterator.next();
            if (hand.isFlush()) {
                count++;
            }
        }

        return count;
    }

    public static int numStraight(HandCollection hands) {
        int count = 0;
        Hand hand;

        Iterator<Hand> iterator = hands.iterator();
        while (iterator.hasNext()) {
            hand = iterator.next();
            if (hand.isStraight()) {
                count++;
            }
        }
        return count;
    }
}
