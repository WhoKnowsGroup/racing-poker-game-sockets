package com.pokerace.gameplay.core.cards;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.text.DecimalFormat;


/**
 * Verified: OK
 * @author User
 */
public class Hand {

    private CardCollection cards = new CardCollection(5);//also set to 5 in 'private CardCollection cloneCardCollection(CardCollection cardCollection)'
    private HandType handType;
    private static final String NO_BETS = "No Bets";
    private int handNumber;
    private int numberOfDoubles;
    private int firstDoubleIndex;
    private int secondDoubleIndex;
    private int tripleIndex;
    private Card unwantedCard;
    private Card.CardNumberEnum posDEStraightNeededCardNumber1;
    private Card.CardNumberEnum posDEStraightNeededCardNumber2;
    private boolean quadruple;
    private boolean straight;
    private boolean possStraight;
    private boolean possStraight3SameSuite;
    private boolean possStraightFlush;
    private boolean possDEStraight;
    private boolean possDEStraight3SameSuite;
    private boolean possDEStraightFlush;
    private Card.SuitesEnum neededSuite;
    private Card.CardNumberEnum neededCardNumber;
    private boolean possibleFlush;
    private boolean isLeader;
    private int availability;
    private int cardsLeftInDeck;
    private int score;
    private int bonus;
    private int penalty;
    private int leaderBonus;
    private double totalRating;
    private boolean m_Locked = false;

    public Hand(int handNumber) {
        this.handNumber = handNumber;
    }

    public String toString() {
        return "Hand " + this.handNumber;
    }

    public void setAvailability(int availability) {
    	//number of cards in the deck that match what we need to win
        this.availability = availability;
    }

    public int getAvailability() {
        return availability;
    }

    public int getCardsLeftInDeck() {
        return cardsLeftInDeck;
    }

    public void setCardsLeftInDeck(int cardsLeftInDeck) {
        this.cardsLeftInDeck = cardsLeftInDeck;
    }

    public CardCollection getCards() {
        return cards;
    }

    public HandType getHandType() {
        return handType;
    }

    public void setHandType(HandType handType) {
        this.handType = handType;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

    public void checkCards() {
        this.numberOfDoubles = 0;
        this.firstDoubleIndex = 0;
        this.secondDoubleIndex = 0;
        this.tripleIndex = 0;
        this.quadruple = false;

        this.straight = (this.cards.getCard(2).getCardNumber().ordinal() == this.cards.getCard(1).getCardNumber().ordinal() + 1
                && this.cards.getCard(3).getCardNumber().ordinal() == this.cards.getCard(2).getCardNumber().ordinal() + 1
                && this.cards.getCard(4).getCardNumber().ordinal() == this.cards.getCard(3).getCardNumber().ordinal() + 1
                && this.cards.getCard(5).getCardNumber().ordinal() == this.cards.getCard(4).getCardNumber().ordinal() + 1);

        //Check for quadruple
        if (this.cards.getCard(1).getCardNumber() == this.cards.getCard(2).getCardNumber()
                && this.cards.getCard(2).getCardNumber() == this.cards.getCard(3).getCardNumber()
                && this.cards.getCard(3).getCardNumber() == this.cards.getCard(4).getCardNumber()) {
            this.unwantedCard = this.cards.getCard(5);
            this.quadruple = true;
        } else if (this.cards.getCard(2).getCardNumber() == this.cards.getCard(3).getCardNumber()
                && this.cards.getCard(3).getCardNumber() == this.cards.getCard(4).getCardNumber()
                && this.cards.getCard(4).getCardNumber() == this.cards.getCard(5).getCardNumber()) {
            this.unwantedCard = this.cards.getCard(1);
            this.quadruple = true;
        } 
        
       //Check for triples
        else if (this.cards.getCard(1).getCardNumber() == this.cards.getCard(2).getCardNumber()
                && this.cards.getCard(2).getCardNumber() == this.cards.getCard(3).getCardNumber()) {
            //there is a triple starting at 1
            this.tripleIndex = 1;
            if (this.cards.getCard(4).getCardNumber() == this.cards.getCard(5).getCardNumber()) {
                //there is a double starting at 4
                this.numberOfDoubles = 1;
                this.firstDoubleIndex = 4;
            }
        } //Check for triples
        else if (this.cards.getCard(2).getCardNumber() == this.cards.getCard(3).getCardNumber()
                && this.cards.getCard(3).getCardNumber() == this.cards.getCard(4).getCardNumber()) {
            //there is a triple starting at 2
            this.tripleIndex = 2;
        } else if (this.cards.getCard(3).getCardNumber() == this.cards.getCard(4).getCardNumber()
                && this.cards.getCard(4).getCardNumber() == this.cards.getCard(5).getCardNumber()) {
            //there is a triple starting at 3
            this.tripleIndex = 3;
            if (this.cards.getCard(1).getCardNumber() == this.cards.getCard(2).getCardNumber()) {
                this.numberOfDoubles = 1;
                this.firstDoubleIndex = 1;
            }
        } //Check for doubles
        else if (this.cards.getCard(1).getCardNumber() == this.cards.getCard(2).getCardNumber()) {
            //there is a double starting at 1
            this.firstDoubleIndex = 1;
            if (this.cards.getCard(3).getCardNumber() == this.cards.getCard(4).getCardNumber()) {
                //there is another double starting at 3
                this.secondDoubleIndex = 3;
                this.numberOfDoubles = 2;
            } else if (this.cards.getCard(4).getCardNumber() == this.cards.getCard(5).getCardNumber()) {
                //there is another double starting at 4
                this.secondDoubleIndex = 4;
                this.numberOfDoubles = 2;
            } else if (this.cards.getCard(3).getCardNumber() == this.cards.getCard(4).getCardNumber()
                    && this.cards.getCard(4).getCardNumber() == this.cards.getCard(5).getCardNumber()) {
                //It is a full house
                this.numberOfDoubles = 1;
                this.tripleIndex = 3;
            } else {
                this.numberOfDoubles = 1;
            }
        } else if (this.cards.getCard(2).getCardNumber() == this.cards.getCard(3).getCardNumber()) {
            //there is a double starting at 2
            this.firstDoubleIndex = 2;
            if (this.cards.getCard(4).getCardNumber() == this.cards.getCard(5).getCardNumber()) {
                //there is a double starting at 4
                this.secondDoubleIndex = 4;
                this.numberOfDoubles = 2;
            } else {
                this.numberOfDoubles = 1;
            }
        } else if (this.cards.getCard(3).getCardNumber() == this.cards.getCard(4).getCardNumber()) {
            //there is a double starting at 3
            this.firstDoubleIndex = 3;
            this.numberOfDoubles = 1;
        } else if (this.cards.getCard(4).getCardNumber() == this.cards.getCard(5).getCardNumber()) {
            //there is a double starting at 4
            this.firstDoubleIndex = 4;
            this.numberOfDoubles = 1;
        }

        this.possDEStraightFlush = false;
        this.possStraightFlush = false;
        this.possibleFlush = false;
        this.possDEStraight3SameSuite = false;
        this.possDEStraight = false;

        this.CheckForPossDEStraightFlush();
        if (!this.possDEStraightFlush) {
            this.CheckForPossStraightFlush();
            if (!this.possStraightFlush) {
                this.CheckForPossibleFlush();
                if (!this.possibleFlush) {
                    this.CheckForPossDEStraight();
                    if (!this.possDEStraight3SameSuite && !this.possDEStraight) {
                        this.CheckForPossStraight();
                    }
                }
            }
        }
    }

    private void CheckForPossDEStraightFlush() {
        this.possDEStraightFlush = false;

        CardCollection cardsCopy;
        Card spareCard;

        for (Card card : this.cards) {
            //Replaced copy with reference assignment
            //cardsCopy = CardCollection.Copy(this.cards);
            cardsCopy = cloneCardCollection(this.cards);

            spareCard = Card.Copy(card);
            cardsCopy.remove(spareCard);

            if (cardsCopy.getCard(4).getCardNumber().ordinal() == cardsCopy.getCard(3).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(3).getCardNumber().ordinal() == cardsCopy.getCard(2).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(2).getCardNumber().ordinal() == cardsCopy.getCard(1).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(4).getCardNumber() != Card.CardNumberEnum.Ace
                    && cardsCopy.getCard(1).getCardNumber() != Card.CardNumberEnum.Two
                    && cardsCopy.allCardsSameSuite()) {
                this.possDEStraightFlush = true;
                this.unwantedCard = spareCard;

                //TODO: Check if the below two fields are appropriated populated with the correct enum mapping from computed index
                this.posDEStraightNeededCardNumber1 = Card.CardNumberEnum.values()[cardsCopy.getCard(1).getCardNumber().ordinal() - 1];
                this.posDEStraightNeededCardNumber2 = Card.CardNumberEnum.values()[cardsCopy.getCard(4).getCardNumber().ordinal() + 1];

                this.neededSuite = cardsCopy.getCard(1).getSuite();
                break;
            }//if
        }//foreach

    }

    private void CheckForPossDEStraight() {
        this.possDEStraight = false;
        this.possDEStraight3SameSuite = false;

        CardCollection cardsCopy;
        Card spareCard;

        for (Card card : this.cards) {
            //Replaced copy with reference assignment
            //cardsCopy = CardCollection.Copy(this.cards);
            cardsCopy = cloneCardCollection(this.cards);

            spareCard = Card.Copy(card);
            cardsCopy.remove(spareCard);

            // Check for Possible Double Ended Straight
            if (cardsCopy.getCard(4).getCardNumber().ordinal() == cardsCopy.getCard(3).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(3).getCardNumber().ordinal() == cardsCopy.getCard(2).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(2).getCardNumber().ordinal() == cardsCopy.getCard(1).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(4).getCardNumber() != Card.CardNumberEnum.Ace
                    && cardsCopy.getCard(1).getCardNumber() != Card.CardNumberEnum.Two) {
                this.unwantedCard = spareCard;

                //TODO: Check if the below two fields are appropriated populated with the correct enum mapping from computed index
                this.posDEStraightNeededCardNumber1 = Card.CardNumberEnum.values()[cardsCopy.getCard(1).getCardNumber().ordinal() - 1];
                this.posDEStraightNeededCardNumber2 = Card.CardNumberEnum.values()[cardsCopy.getCard(4).getCardNumber().ordinal() + 1];

                if (cardsCopy.NumHearts() == 3 || cardsCopy.NumDiamonds() == 3
                        || cardsCopy.NumClubs() == 3 || cardsCopy.NumSpades() == 3) {

                    this.possDEStraight3SameSuite = true;
                    if (cardsCopy.getCard(2).getSuite() == cardsCopy.getCard(3).getSuite()
                            && cardsCopy.getCard(3).getSuite() == cardsCopy.getCard(4).getSuite()) {
                        this.neededSuite = cardsCopy.getCard(2).getSuite();
                    } else {
                        this.neededSuite = cardsCopy.getCard(1).getSuite();
                    }
                } else {
                    this.possDEStraight = true;
                }

                break;
            }//if
        }//foreach

    }//CheckForDEStraight

    private void CheckForPossStraightFlush() {
        this.possStraightFlush = false;

        CardCollection cardsCopy;
        Card spareCard;

        for (Card card : this.cards) {
            //Replaced copy with reference assignment
            //cardsCopy = CardCollection.Copy(this.cards);
            cardsCopy = cloneCardCollection(this.cards);

            spareCard = Card.Copy(card);
            cardsCopy.remove(spareCard);

            //Check for Possible Straight
            if (cardsCopy.getCard(1).getCardNumber() == Card.CardNumberEnum.Two
                    && cardsCopy.getCard(2).getCardNumber() == Card.CardNumberEnum.Three
                    && cardsCopy.getCard(3).getCardNumber() == Card.CardNumberEnum.Four
                    && cardsCopy.getCard(4).getCardNumber() == Card.CardNumberEnum.Five && cardsCopy.allCardsSameSuite()) {
                this.possStraightFlush = true;
                this.neededCardNumber = Card.CardNumberEnum.Six;
                this.neededSuite = cardsCopy.getCard(1).getSuite();
                this.unwantedCard = spareCard;
                break;
            } else if (cardsCopy.getCard(1).getCardNumber() == Card.CardNumberEnum.Jack
                    && cardsCopy.getCard(2).getCardNumber() == Card.CardNumberEnum.Queen
                    && cardsCopy.getCard(3).getCardNumber() == Card.CardNumberEnum.King
                    && cardsCopy.getCard(4).getCardNumber() == Card.CardNumberEnum.Ace && cardsCopy.allCardsSameSuite()) {
                this.possStraightFlush = true;
                this.neededCardNumber = Card.CardNumberEnum.Ten;
                this.neededSuite = cardsCopy.getCard(1).getSuite();
                this.unwantedCard = spareCard;
                break;
            } else if (cardsCopy.getCard(4).getCardNumber().ordinal() == cardsCopy.getCard(3).getCardNumber().ordinal() + 2
                    && cardsCopy.getCard(3).getCardNumber().ordinal() == cardsCopy.getCard(2).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(2).getCardNumber().ordinal() == cardsCopy.getCard(1).getCardNumber().ordinal() + 1 && cardsCopy.allCardsSameSuite()) {
                this.possStraightFlush = true;
                //TODO: Check if enum index is fetched properly
                this.neededCardNumber = Card.CardNumberEnum.values()[cardsCopy.getCard(3).getCardNumber().ordinal() + 1];
                this.neededSuite = cardsCopy.getCard(1).getSuite();
                this.unwantedCard = spareCard;
                break;
            } else if (cardsCopy.getCard(4).getCardNumber().ordinal() == cardsCopy.getCard(3).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(3).getCardNumber().ordinal() == cardsCopy.getCard(2).getCardNumber().ordinal() + 2
                    && cardsCopy.getCard(2).getCardNumber().ordinal() == cardsCopy.getCard(1).getCardNumber().ordinal() + 1 && cardsCopy.allCardsSameSuite()) {
                this.possStraightFlush = true;
                //TODO: Check if enum is fetched properly from index
                this.neededCardNumber = Card.CardNumberEnum.values()[cardsCopy.getCard(2).getCardNumber().ordinal() + 1];
                this.neededSuite = cardsCopy.getCard(1).getSuite();
                this.unwantedCard = spareCard;
                break;
            } else if (cardsCopy.getCard(4).getCardNumber().ordinal() == cardsCopy.getCard(3).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(3).getCardNumber().ordinal() == cardsCopy.getCard(2).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(2).getCardNumber().ordinal() == cardsCopy.getCard(1).getCardNumber().ordinal() + 2 && cardsCopy.allCardsSameSuite()) {
                this.possStraightFlush = true;
                //TODO: Check if enum is fetched correctly from index
                this.neededCardNumber = Card.CardNumberEnum.values()[cardsCopy.getCard(1).getCardNumber().ordinal() + 1];
                this.neededSuite = cardsCopy.getCard(1).getSuite();
                this.unwantedCard = spareCard;
                break;
            }

        }//foreach
    }

    private void CheckForPossStraight() {
        this.possStraight = false;
        this.possStraight3SameSuite = false;

        CardCollection cardsCopy;
        Card spareCard;

        for (Card card : this.cards) {
            //Replaced copy with reference assignment
            //cardsCopy = CardCollection.Copy(this.cards);
            cardsCopy = cloneCardCollection(this.cards);

            spareCard = Card.Copy(card);
            cardsCopy.remove(spareCard);

            //Check for Possible Straight
            if (cardsCopy.getCard(1).getCardNumber() == Card.CardNumberEnum.Two
                    && cardsCopy.getCard(2).getCardNumber() == Card.CardNumberEnum.Three
                    && cardsCopy.getCard(3).getCardNumber() == Card.CardNumberEnum.Four
                    && cardsCopy.getCard(4).getCardNumber() == Card.CardNumberEnum.Five) {
                this.possStraight = true;
                this.neededCardNumber = Card.CardNumberEnum.Six;
            } else if (cardsCopy.getCard(1).getCardNumber() == Card.CardNumberEnum.Jack
                    && cardsCopy.getCard(2).getCardNumber() == Card.CardNumberEnum.Queen
                    && cardsCopy.getCard(3).getCardNumber() == Card.CardNumberEnum.King
                    && cardsCopy.getCard(4).getCardNumber() == Card.CardNumberEnum.Ace) {
                this.possStraight = true;
                this.neededCardNumber = Card.CardNumberEnum.Ten;
            } else if (cardsCopy.getCard(4).getCardNumber().ordinal() == cardsCopy.getCard(3).getCardNumber().ordinal() + 2
                    && cardsCopy.getCard(3).getCardNumber().ordinal() == cardsCopy.getCard(2).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(2).getCardNumber().ordinal() == cardsCopy.getCard(1).getCardNumber().ordinal() + 1) {
                this.possStraight = true;
                //TODO: Check if enum is fetched correctly from index
                this.neededCardNumber = Card.CardNumberEnum.values()[cardsCopy.getCard(3).getCardNumber().ordinal() + 1];
            } else if (cardsCopy.getCard(4).getCardNumber().ordinal() == cardsCopy.getCard(3).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(3).getCardNumber().ordinal() == cardsCopy.getCard(2).getCardNumber().ordinal() + 2
                    && cardsCopy.getCard(2).getCardNumber().ordinal() == cardsCopy.getCard(1).getCardNumber().ordinal() + 1) {
                this.possStraight = true;
                //TODO: Check if enum is fetched correctly from index
                this.neededCardNumber = Card.CardNumberEnum.values()[cardsCopy.getCard(2).getCardNumber().ordinal() + 1];
            } else if (cardsCopy.getCard(4).getCardNumber().ordinal() == cardsCopy.getCard(3).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(3).getCardNumber().ordinal() == cardsCopy.getCard(2).getCardNumber().ordinal() + 1
                    && cardsCopy.getCard(2).getCardNumber().ordinal() == cardsCopy.getCard(1).getCardNumber().ordinal() + 2) {
                this.possStraight = true;
                this.neededCardNumber = Card.CardNumberEnum.values()[cardsCopy.getCard(1).getCardNumber().ordinal() + 1];
            }

           if (this.possStraight) {
                this.unwantedCard = spareCard;
                /*if (cardsCopy.NumHearts() == 3 || cardsCopy.NumDiamonds() == 3
                        || cardsCopy.NumClubs() == 3 || cardsCopy.NumSpades() == 3) {
                    this.possStraight = false;
                    this.possStraight3SameSuite = true;
                    if (cardsCopy.getCard(2).getSuite() == cardsCopy.getCard(3).getSuite()
                            && cardsCopy.getCard(3).getSuite() == cardsCopy.getCard(4).getSuite()) {
                        this.neededSuite = cardsCopy.getCard(2).getSuite();
                    } else {
                        this.neededSuite = cardsCopy.getCard(1).getSuite();
                    }
                }//i*/

                break;
            }//if

        }//foreach

    }

    private void CheckForPossibleFlush() {
        Card.SuitesEnum suite;

        this.possibleFlush = (this.cards.NumHearts() == 4
                || this.cards.NumClubs() == 4
                || this.cards.NumDiamonds() == 4
                || this.cards.NumSpades() == 4);

        // Work out the unwanted card
        if (this.possibleFlush) {
            if (this.cards.NumHearts() == 4) {
                suite = Card.SuitesEnum.Hearts;
            } else if (this.cards.NumClubs() == 4) {
                suite = Card.SuitesEnum.Clubs;
            } else if (this.cards.NumDiamonds() == 4) {
                suite = Card.SuitesEnum.Diamonds;
            } else {
                suite = Card.SuitesEnum.Spades;
            }

            this.neededSuite = suite;

            for (Card card : this.cards) {
                if (card.getSuite() != suite) //found it
                {
                    this.unwantedCard = Card.Copy(card);
                    break;
                }
            }
        }

    }

    /**
     * Its a Full House if there is a double and a triple
     * @return true if full house
     */
    public boolean isFullHouse() {
        return (this.numberOfDoubles == 1 && this.tripleIndex != 0);
    }

    public boolean isStraight() {
        return this.straight;
    }

    /**
     * Its a Flush if all cards are of the same suite
     * @return true if Flush else false
     */
    public boolean isFlush() {
        return (this.cards.NumHearts() == 5
                || this.cards.NumDiamonds() == 5
                || this.cards.NumClubs() == 5
                || this.cards.NumSpades() == 5);
    }

    public Card getUnwantedCard() {
        return this.unwantedCard;
    }

    public int getNumberOfDoubles() {
        return this.numberOfDoubles;
    }

    public int getFirstDoubleIndex() {
        return firstDoubleIndex;
    }

    public int getSecondDoubleIndex() {
        return secondDoubleIndex;
    }

    public int getTripleIndex() {
        return tripleIndex;
    }

    public Card.CardNumberEnum getPosDEStraightNeededCardNumber1() {
        return posDEStraightNeededCardNumber1;
    }

    public Card.CardNumberEnum getPosDEStraightNeededCardNumber2() {
        return posDEStraightNeededCardNumber2;
    }

    public boolean isPossStraight() {
        return possStraight;
    }

    public boolean isPossStraight3SameSuite() {
        return possStraight3SameSuite;
    }

    public boolean isPossStraightFlush() {
        return possStraightFlush;
    }

    public boolean isPossDEStraight() {
        return possDEStraight;
    }

    public boolean isPossDEStraight3SameSuite() {
        return possDEStraight3SameSuite;
    }

    public boolean isPossDEStraightFlush() {
        return possDEStraightFlush;
    }

    public Card.CardNumberEnum getNeededCardNumber() {
        return neededCardNumber;
    }

    public Card.SuitesEnum getNeededSuite() {
        return neededSuite;
    }

    public boolean isPossibleFlush() {
        return possibleFlush;
    }

    public boolean isOnePair() {
        return this.numberOfDoubles == 1;
    }

    public boolean isThreeOfAKind() {
        return this.tripleIndex != 0;
    }

    public boolean isTwoPairs() {
        return this.numberOfDoubles == 2;
    }

    public boolean isIsLeader() {
        return isLeader;
    }

    public void setIsLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    public void calculateChances() {
        this.leaderBonus = 0;
        switch (this.handType.getValue()) {
            case Single:
                this.score = 5;
                break;
            case OnePair:
                this.score = 30;
                break;
            case PossibleInsideStraight:
                this.score = 53;
                break;
            case PossibleInsideStraight3SameSuite:
                this.score = 65;
                break;
            case TwoPairs:
                this.score = 50;
                break;
            case PossibleDblEndedStraight:
                this.score = 42;
                break;
            case PossibleDblEndedStraight3SameSuite:
                this.score = 68;
                break;
            case PossibleInsideStraightFlush:
                this.score = 50;
                break;
            case PossibleFlush:
                this.score = 46;
                break;
            case ThreeOfAKind:
                this.score = 115;
                break;
            case PossibleDblEndedStraightFlush:
                this.score = 50;
                break;
            default:
                this.score = 0;
                break;
        }

    }

    public void calcBonusAndPenalty() {
        switch (this.handType.getValue()) {
            case PossibleDblEndedStraight:
                this.calcDEStraightBonusAndPenalty();
                break;
            case PossibleDblEndedStraight3SameSuite:
                this.calcDEStraight3SameSuiteBonusAndPenalty();
                break;
            case PossibleFlush:
                this.CalcFlushBonusAndPenalty();
                break;
            case PossibleInsideStraightFlush:
                this.CalcInsideStraightFlushBonusAndPenalty();
                break;
            case PossibleInsideStraight:
                this.CalcInsideStraightBonusAndPenalty();
                break;
            case PossibleInsideStraight3SameSuite:
                this.CalcInsideStraight3SameSuiteBonusAndPenalty();
                break;
            case TwoPairs:
                this.CalcTwoPairsBonusAndPenalty();
                break;
            case OnePair:
                this.CalcOnePairBonusAndPenalty();
                break;
            case PossibleDblEndedStraightFlush:
                this.CalcDEStraightFlushBonusAndPenalty();
                break;
            case ThreeOfAKind:
                this.CalcThreeOfAKindBonusAndPenalty();
                break;
            case Single:
                this.CalcSingleBonusAndPenalty();
                break;
        }

    }

    private void calcDEStraightBonusAndPenalty() {
        switch (this.availability) {
            case 8:
                this.bonus = 127;
                this.penalty = 0;
                break;
            case 7:
                this.bonus = 105;
                this.penalty = 0;
                break;
            case 6:
                this.bonus = 83;
                this.penalty = 0;
                break;
            case 5:
                this.bonus = 61;
                this.penalty = 0;
                break;
            case 4:
                this.bonus = 38;
                this.penalty = 0;
                break;
            case 3:
                this.bonus = 13;
                this.penalty = 0;
                break;
            case 2:
                this.bonus = 0;
                this.penalty = 0;
                break;
            case 1:
                this.bonus = 0;
                this.penalty = 25;
                break;
            case 0:
                this.bonus = 0;
                this.penalty = 35;
                break;
            default:
                this.bonus = 0;
                this.penalty = 0;
                break;
        }

    }

    private void calcDEStraight3SameSuiteBonusAndPenalty() {
        switch (this.availability) {
            case 8:
                this.bonus = 127;
                this.penalty = 0;
                break;
            case 7:
                this.bonus = 105;
                this.penalty = 0;
                break;
            case 6:
                this.bonus = 83;
                this.penalty = 0;
                break;
            case 5:
                this.bonus = 61;
                this.penalty = 0;
                break;
            case 4:
                this.bonus = 38;
                this.penalty = 0;
                break;
            case 3:
                this.bonus = 13;
                this.penalty = 0;
                break;
            case 2:
                this.bonus = 0;
                this.penalty = 0;
                break;
            case 1:
                this.bonus = 0;
                this.penalty = 25;
                break;
            case 0:
                this.bonus = 0;
                this.penalty = 35;
                break;
            default:
                this.bonus = 0;
                this.penalty = 0;
                break;
        }

    }

    private void CalcFlushBonusAndPenalty() {
        switch (this.availability) {
            case 17:
                this.bonus = 325;
                this.penalty = 0;
                break;
            case 16:
                this.bonus = 303;
                this.penalty = 0;
                break;
            case 15:
                this.bonus = 281;
                this.penalty = 0;
                break;
            case 14:
                this.bonus = 259;
                this.penalty = 0;
                break;
            case 13:
                this.bonus = 237;
                this.penalty = 0;
                break;
            case 12:
                this.bonus = 215;
                this.penalty = 0;
                break;
            case 11:
                this.bonus = 193;
                this.penalty = 0;
                break;
            case 10:
                this.bonus = 171;
                this.penalty = 0;
                break;
            case 9:
                this.bonus = 149;
                this.penalty = 0;
                break;
            case 8:
                this.bonus = 127;
                this.penalty = 0;
                break;
            case 7:
                this.bonus = 105;
                this.penalty = 0;
                break;
            case 6:
                this.bonus = 83;
                this.penalty = 0;
                break;
            case 5:
                this.bonus = 61;
                this.penalty = 0;
                break;
            case 4:
                this.bonus = 38;
                this.penalty = 0;
                break;
            case 3:
                this.bonus = 13;
                this.penalty = 0;
                break;
            case 2:
                this.bonus = 0;
                this.penalty = 0;
                break;
            case 1:
                this.bonus = 0;
                this.penalty = 25;
                break;
            case 0:
                this.bonus = 0;
                this.penalty = 35;
                break;
            default:
                this.bonus = 0;
                this.penalty = 0;
                break;
        }

    }

    private void CalcInsideStraightFlushBonusAndPenalty() {
        this.CalcFlushBonusAndPenalty();
    }

    private void CalcInsideStraightBonusAndPenalty() {
        switch (this.availability) {
            case 4:
                this.bonus = 15;
                this.penalty = 0;
                break;
            case 3:
                this.bonus = 8;
                this.penalty = 0;
                break;
            case 2:
                this.bonus = 0;
                this.penalty = 0;
                break;
            case 1:
                this.bonus = 0;
                this.penalty = 15;
                break;
            case 0:
                this.bonus = 0;
                this.penalty = 25;
                break;
            default:
                this.bonus = 0;
                this.penalty = 0;
                break;
        }

    }

    private void CalcInsideStraight3SameSuiteBonusAndPenalty() {
        switch (this.availability) {
            case 4:
                this.bonus = 15;
                this.penalty = 0;
                break;
            case 3:
                this.bonus = 8;
                this.penalty = 0;
                break;
            case 2:
                this.bonus = 0;
                this.penalty = 0;
                break;
            case 1:
                this.bonus = 0;
                this.penalty = 15;
                break;
            case 0:
                this.bonus = 0;
                this.penalty = 25;
                break;
            default:
                this.bonus = 0;
                this.penalty = 0;
                break;
        }

    }

    private void CalcTwoPairsBonusAndPenalty() {
        switch (this.availability) {
            case 4:
                this.bonus = 50;
                this.penalty = 0;
                break;
            case 3:
                this.bonus = 29;
                this.penalty = 0;
                break;
            case 2:
                this.bonus = 6;
                this.penalty = 0;
                break;
            case 1:
                this.bonus = 0;
                this.penalty = 4;
                break;
            case 0:
                this.bonus = 0;
                this.penalty = 20;
                break;
            default:
                this.bonus = 0;
                this.penalty = 0;
                break;
        }

    }

    private void CalcOnePairBonusAndPenalty() {
        switch (this.availability) {
            case 2:
                this.bonus = 0;
                this.penalty = 0;
                break;
            case 1:
                this.bonus = 4;
                this.penalty = 0;
                break;
            case 0:
                this.bonus = 17;
                this.penalty = 0;
                break;
            default:
                this.bonus = 0;
                this.penalty = 0;
                break;
        }

    }

    private void CalcDEStraightFlushBonusAndPenalty() {
        switch (this.availability) {
            case 17:
                this.bonus = 325;
                this.penalty = 0;
                break;
            case 16:
                this.bonus = 303;
                this.penalty = 0;
                break;
            case 15:
                this.bonus = 281;
                this.penalty = 0;
                break;
            case 14:
                this.bonus = 259;
                this.penalty = 0;
                break;
            case 13:
                this.bonus = 237;
                this.penalty = 0;
                break;
            case 12:
                this.bonus = 215;
                this.penalty = 0;
                break;
            case 11:
                this.bonus = 193;
                this.penalty = 0;
                break;
            case 10:
                this.bonus = 171;
                this.penalty = 0;
                break;
            case 9:
                this.bonus = 149;
                this.penalty = 0;
                break;
            case 8:
                this.bonus = 127;
                this.penalty = 0;
                break;
            case 7:
                this.bonus = 105;
                this.penalty = 0;
                break;
            case 6:
                this.bonus = 83;
                this.penalty = 0;
                break;
            case 5:
                this.bonus = 61;
                this.penalty = 0;
                break;
            case 4:
                this.bonus = 38;
                this.penalty = 0;
                break;
            case 3:
                this.bonus = 13;
                this.penalty = 0;
                break;
            case 2:
                this.bonus = 0;
                this.penalty = 0;
                break;
            case 1:
                this.bonus = 0;
                this.penalty = 25;
                break;
            case 0:
                this.bonus = 0;
                this.penalty = 35;
                break;
            default:
                this.bonus = 0;
                this.penalty = 0;
                break;
        }

    }

    private void CalcThreeOfAKindBonusAndPenalty() {
        switch (this.availability) {
            case 1:
                this.bonus = 0;
                this.penalty = 0;
                break;
            case 0:
                this.bonus = 0;
                this.penalty = 25;
                break;
            default:
                this.bonus = 0;
                this.penalty = 0;
                break;
        }

    }

    private void CalcSingleBonusAndPenalty() {
        switch (this.availability) {
            case 3:
                this.bonus = 0;
                this.penalty = 0;
                break;
            case 2:
                this.bonus = 10;
                this.penalty = 0;
                break;
            case 1:
                this.bonus = 21;
                this.penalty = 0;
                break;
            case 0:
                this.bonus = 33;
                this.penalty = 0;
                break;
            default:
                this.bonus = 0;
                this.penalty = 0;
                break;
        }

    }

    public int getHandNumber() {
        return handNumber;
    }

    public String getHandTypeName() {
        return handType.toString();
    }

    public int getShots() {
        return (5 - this.cards.size());
    }

    public int getHandAvailability() {
        return availability;
    }

    public int getFormula() {
        return (this.getShots() * this.availability * 4);
    }

    public int getLeaderBonus() {
        return this.leaderBonus;
    }

    public int getBonus() {
        return this.bonus;
    }

    public int getPenalty() {
        return this.penalty;
    }

    public int getScore() {
        return this.score;
    }

    public int getChances() {
    	//leaderBonus is always 0
    	//bonus is a score assigned based upon the number of cards in the deck that build towards a winning hand (higher numbers -> larger bonus)
    	//penalty is a score assigned based upon the number of cards in the deck that build towards a winning hand (lower numbers -> larger penalty)
    	//score is a fixed value assigned based upon the type of hand we are (pair, three of a kind, possible flush, etc.)
    	//formula is computed based on the number of available spots in the hand and the number of cards in the deck that build towards a winning hand
    	
    	//XXX:  if the hand is locked, the 'penalty' score will be adjusted to cause this method to return 0
        return (this.getFormula() + this.leaderBonus + this.bonus - this.penalty + this.score);
    }

    public boolean getLocked() {
        return this.m_Locked;
    }

    public void setLocked(boolean m_Locked) {
        this.m_Locked = m_Locked;

        //This is just to make the Chances equal to zero
        this.penalty = this.getFormula() + this.leaderBonus + this.bonus + this.score;
    }

    public double getRating() {
        if (this.cardsLeftInDeck > 0) {
            return ((double) this.getChances() / (double) this.cardsLeftInDeck);
        } else {
            return 0;
        }
    }

    public double getOdds() {
        if (this.getRating() > 0) {
            return (100 * this.totalRating) / (125 * this.getRating());
        } else {
            return 0;
        }
    }

    public String getFormattedOdds() {
        if (this.getLocked()) {
            return NO_BETS;
        } else if (this.getOdds() < 16) {

            //TODO: Confirm format

            //return String.format("$%2d.", Math.round(this.getOdds()));
            //return (Math.Round(this.getOdds(), 1)).toString("$##.00");
            DecimalFormat decimal = new DecimalFormat("#.#");
            return String.format(decimal.format(this.getOdds()));


        } else {
            return "16";
        }
    }

    public double getUnFormatedOdds() {
        if (this.getOdds() < 16) {
            double roundValue = this.getOdds();
            
             roundValue *= 100 ;
             roundValue = Math.round(roundValue);
             roundValue /=100 ;
             roundValue *= 10 ;
             roundValue = Math.round(roundValue);
             roundValue /= 10 ;
             
            return roundValue;
            //return Math.Round(this.getOdds(), 1);
        } else {
            return 16.00;
        }
    }

    /**
     * Returns true if two hands have the same two pairs
     * @param hand1
     * @param hand2
     * @return true if both hands have same two pairs, false otherwise
     */
    public static boolean isSameTwoPairs(Hand hand1, Hand hand2) {
        return hand1.isTwoPairs() && hand2.isTwoPairs()
                && hand1.cards.getCard(1).getCardNumber() == hand2.cards.getCard(1).getCardNumber()
                && hand1.cards.getCard(3).getCardNumber() == hand2.cards.getCard(3).getCardNumber();
    }

    public boolean isQuadruple() {
        return this.quadruple;
    }

    /**
     * Calculates show much credit a hand would be given if a bet was placed on it
     * @param betAmount
     * @param hand
     * @return The credit amount
     */
    public static double GetCredit(double betAmount, Hand hand) {
        //TODO: Confirm appropriate return value
        return betAmount * Math.round(hand.getUnFormatedOdds());
        //return betAmount * Math.Round(hand.getUnFormatedOdds(), 2);
    }

    public boolean isStraightFlush() {
        return this.isFlush() && this.isStraight();
    }

    private CardCollection cloneCardCollection(CardCollection cardCollection) {
        CardCollection newCardCollection = new CardCollection(5);
        for (Card card : cardCollection) {
            newCardCollection.add(card);
        }

        return newCardCollection;
    }
}