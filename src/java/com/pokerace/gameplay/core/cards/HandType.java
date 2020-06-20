package com.pokerace.gameplay.core.cards;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * Verified: OK
 * @author Administrator
 */
public class HandType {

    public enum HandTypeEnum {

        OnePair,
        TwoPairs,
        ThreeOfAKind,
        PossibleFlush,
        PossibleInsideStraight,
        PossibleInsideStraight3SameSuite,
        PossibleInsideStraightFlush,
        PossibleDblEndedStraight,
        PossibleDblEndedStraight3SameSuite,
        PossibleDblEndedStraightFlush,
        Single;
    }
    private HandTypeEnum handType;

    public HandType(HandTypeEnum handType) {
        this.handType = handType;
    }

    public String toString() {
        switch (this.handType) {
            case OnePair:
                return "1,Pair";

            case Single:
                return "Single";

            case TwoPairs:
                return "2,Pairs";

            case ThreeOfAKind:
                return "3,of,a,Kind";

            case PossibleFlush:
                return "Pos.,Flush";

            case PossibleInsideStraight:
            case PossibleInsideStraight3SameSuite:
                return "Pos.,Straight";

            case PossibleInsideStraightFlush:
                return "Pos.,Straight,Flush";

            case PossibleDblEndedStraight:
            case PossibleDblEndedStraight3SameSuite:
                return "Pos.,Dbl,End,Straight";

            case PossibleDblEndedStraightFlush:
                return "Pos.,Dbl,End,Str,Flush";

            default:
                return "";
        }
    }
    
    public HandTypeEnum getValue(){
        return handType;
    }
}
