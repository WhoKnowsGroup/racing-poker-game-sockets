package com.pokerace.gameplay.core;

 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.pokerace.gameplay.core.cards.Card;
import com.pokerace.gameplay.core.cards.CardCollection;
import com.pokerace.gameplay.core.cards.DealHistoryRecord;
import com.pokerace.gameplay.core.cards.DealsCollection;
import com.pokerace.gameplay.core.cards.Hand;
import com.pokerace.gameplay.core.cards.HandCollection;
import com.pokerace.gameplay.core.cards.HandType;
import com.pokerace.gameplay.core.player.Player;
import com.pokerace.gameplay.core.player.PlayerManager;

/// <summary>
/// Returns all the bets for the current game
/// </summary>
/**
 * Represents one instance of a game.
 * @author User
 */
public class Game {

    //private static Game instance;
    public static int m_TotalNumDeals = 0;  //Total number of deals played
    public PlayerManager playerManager;
    public HandCollection m_Hands;
    public CardCollection m_Deck;
    protected DealsCollection m_DealHistory;
    protected List<Bet> m_Bets = new ArrayList<Bet>();  //Holds the bets for the current game.
    protected List<Bet> m_PastBets = new ArrayList<Bet>();  //Holds the bets for all the previous games.
    protected List<Player> m_StalematePlayer = new ArrayList<Player>();  //Holds the stalemate players for the previous games.
    public boolean m_DeadlockHasOccured = false;
    public boolean m_ThereIsAWinningHand = false;
    public int m_WinningHandNumber;
    public int m_DealNumber;
    public double m_TotalRating;
    public double m_AmountWon;  //how much is won in a game run
    public double m_TotalBet;   //the total bets placed in a game run
    public String m_WinnerTypeName;
    public int m_GameNumber = 0;
    public boolean m_GameLocked = false;
    public int m_TournamentGameCounter = 0; //Number of games counter for tournament
    public int m_NumGamesToPlayForTournament = 0;  //Number of games to play for a tournament
    public List<GameEventListener> gameEventListenerList = new ArrayList<GameEventListener>();
    protected List<Player> bet_placed_users = new ArrayList<Player>();
//    private long[] m_StalemateAmount;
//    private String m_PlayerId = "";
//    private long m_PlayerAmount = 0;
//    private int m_TotalBets = 0;
//    private int m_TotalDeals = 0;
//    private long m_HandCode = 0;
//    private String[] m_PlayerIds;
//    private long m_HandCodeOnAHandHasWon = 0;
//    private int m_WinProbability = 0;
//    private int m_HorseProgress = 0;
//    private long m_OutComeTotalAmount = 0;
//    private long m_OutComeDelta = 0;
    public String m_HouseIdOnDeal = "";
//    private String m_PlayerIdOnDeleteBet = "";
    public String m_HouseIdOnBetPlaced = "";
//    private String m_NounceOnBetPlaced = "";
//    private String m_PlayerName = "";
//    private String[] m_ConnectedPlayers;
    public Player playerWon = null;
    public String winnerName = "";
    public double playerWonCredit = 0.0;

    public void StartTournament() {
    	StartTournament(1000);
    }
    
    public void StartTournament(int numGamesToPlay) {
    	this.m_TournamentGameCounter = 0;
        this.m_NumGamesToPlayForTournament = numGamesToPlay;
        this.m_PastBets.clear();
        this.m_Bets.clear();
        this.m_GameNumber = 0;
        this.m_TotalNumDeals = 0;
        setPlayerManager(new PlayerManager());
    }

    public void addGameEventListener(GameEventListener eventListener) {
        this.gameEventListenerList.add(eventListener);
    }

    public void removeGameEventListener(GameEventListener eventListener) {
        this.gameEventListenerList.remove(eventListener);
    }

    private void onGameEvent() {
        for (GameEventListener eventListener : this.gameEventListenerList) {
            eventListener.gameEvent();
        }
    }

    private void onBetPlacedEvent(int betID, int handNumber, double betAmount, Player placedBy) {
        for (GameEventListener eventListener : this.gameEventListenerList) {
            eventListener.betPlacedEvent(betID, handNumber, betAmount, placedBy);
        }
    }

    private void onBetDeletedEvent(int betID) {
        for (GameEventListener eventListener : this.gameEventListenerList) {
            eventListener.betDeletedEvent(betID);
        }
    }

    private void onPlayerEvent(String playerID) {
        for (GameEventListener eventListener : this.gameEventListenerList) {
            eventListener.playerEvent(playerID);
            System.out.println("Credit changed");
            
        }
    }

    private void onGameReset() {
        for (GameEventListener eventListener : this.gameEventListenerList) {
            eventListener.gameReset();
        }
    }

    private void onAHandHasWon() {
        for (GameEventListener eventListener : this.gameEventListenerList) {
            eventListener.aHandHasWon();
        }
    }

    private void onDealNumberChanged() {
        for (GameEventListener eventListener : this.gameEventListenerList) {
            eventListener.dealNumberChanged();
        }
    }

    private void onDeadLock() {
        for (GameEventListener eventListener : this.gameEventListenerList) {
            eventListener.dealNumberChanged();
        }
    }

    private void onAllPlayersHaveLockedBetsIn() {
        for (GameEventListener eventListener : this.gameEventListenerList) {
            eventListener.allPlayersHaveLockedBetsIn();
        }
    }

    private void onPlayerHasLockedBets(String playerID) {
        for (GameEventListener eventListener : this.gameEventListenerList) {
            eventListener.playerHasLockedBets(playerID);
        }
    }

    private void onPlayerUnlocked(String playerID) {
        for (GameEventListener eventListener : this.gameEventListenerList) {
            eventListener.playerUnlocked(playerID);
        }
    }

    /**
     * Indicates whether the tournaments are finished for the game
     * @return true if all tournaments are finished. false otherwise
     */
    public boolean areTournamentsFinished() {
        return this.m_TournamentGameCounter == this.m_NumGamesToPlayForTournament;
    }

    public boolean isLocked() {
        return this.m_GameLocked;
    }

    /**
     * Indicates if the game is locked. If true then no bets can be taken
     * @param locked true if game locked, false otherwise
     */
    public void setLocked(boolean locked) {
        this.m_GameLocked = locked;

        if (!this.m_GameLocked) {
            for (Player player : getPlayerManager().getPlayers()) { //TODO: Implement PlayerManager
                player.setBetsLocked(false);
            }
        }
    }

    /**
     * Allows locking of bets for a player
     * @param playerID The ID of the player whos bets are to be locked
     */
    public void lockBetsForPlayer(String playerID) {
        Player player = getPlayerManager().findByPlayerID(playerID);

        if (player == null) {
            return;
        }

        player.setBetsLocked(true);

        onPlayerHasLockedBets(playerID);

        int count = countLockedPlayers();

        onAllPlayersHaveLockedBetsIn();
    }

    private int countLockedPlayers() {
        int count = 0;

        for (Player player : getPlayerManager().getPlayers()) {
            if (player.isBetsLocked()) {
                count++;
            }

        }
        return count;
    }

    /**
     * Allows unlocking bets for a player
     * @param playerID 
     */
    public void UnlockPlayer(String playerID) {
        Player player = getPlayerManager().findByPlayerID(playerID);

        //TODO: Perform callback actions here
        if (player != null) {
            player.setBetsLocked(false);
        }

        onPlayerUnlocked(playerID);
    }
    /// <summary>
    /// The total number of deals accross all the games played so far
    /// </summary>

    /**
     * Total number of deals across all games played so far
     * @return 
     */
    public int getTotalDeals() {
        return this.m_TotalNumDeals;
    }

    public Game() {
        m_Deck = new CardCollection(52);
        m_Hands = new HandCollection();
        m_DealHistory = new DealsCollection();
        playerManager = new PlayerManager();
        Reset();
    }

//    public Game(PlayerManager playerManager, HandCollection m_Hands, CardCollection m_Deck, DealsCollection m_DealHistory, int m_WinningHandNumber, int m_DealNumber, double m_TotalRating, double m_AmountWon, double m_TotalBet, String m_WinnerTypeName) {
//        this.playerManager = playerManager;
//        this.m_Hands = m_Hands;
//        this.m_Deck = m_Deck;
//        this.m_DealHistory = m_DealHistory;
//        this.m_WinningHandNumber = m_WinningHandNumber;
//        this.m_DealNumber = m_DealNumber;
//        this.m_TotalRating = m_TotalRating;
//        this.m_AmountWon = m_AmountWon;
//        this.m_TotalBet = m_TotalBet;
//        this.m_WinnerTypeName = m_WinnerTypeName;
//    }
    public void DeleteBet(int betID) throws PokerException {
        Bet betFound = null;

        for (Bet bet : this.m_Bets) {
            if (bet.getBetID() == betID) {
                betFound = bet;
                break;
            }
        }

        if (betFound == null) {
            return;
        }

        if (betFound.getDealNumber() != this.m_DealNumber) {
            throw new PokerException("This bet cannot be removed as it is not from the current deal.");
        } else {
            betFound.getUser().setM_Credit(betFound.getUser().getM_Credit() + betFound.getAmount());
            this.m_Bets.remove(betFound);

            onBetDeletedEvent(betID);
        }
    }

    public void Reset() {
        //this.m_Deck.clear();
        if((this.m_GameNumber >= 1 && this.m_ThereIsAWinningHand) || (this.m_DeadlockHasOccured) || (this.m_GameNumber == 0))
        {
        for (int suite = 0; suite <= 3; suite++) {
            for (int cardNumber = 0; cardNumber <= 12; cardNumber++) {
                this.m_Deck.add(new Card(Card.CardNumberEnum.values()[cardNumber], Card.SuitesEnum.values()[suite]));
            }
        }
             if(this.m_DealNumber > 1 || this.m_GameNumber == 0)
             this.m_GameNumber++; 
       //d.profits_added = false ;
        

        this.m_Hands.clear();

        this.m_DealHistory.clear();

        //Before clearing the bets we need to keep a history of all the bets placed.

        this.m_PastBets.addAll(this.m_Bets);
        this.m_Bets.clear();
//        this.m_bonus_players.clear();
        System.out.println(this.m_PastBets);

        /// Initialize the game with six hands
        for (int counter = 1; counter <= 6; counter++) {
            m_Hands.add(new Hand(counter));
        }

        this.m_DeadlockHasOccured = false;
        this.setThereIsAWinningHand(false);
        this.m_WinningHandNumber = 0;
        this.m_DealNumber = 0;
        this.m_TotalBet = 0;
     //Starting a new game

        this.setLocked(false);

        for (Player player : this.getPlayerManager().getPlayers()) {
            player.setBetsLocked(false);
        }

        onGameReset();
        }
        // m_Deck = new CardCollection(52); /* changes */
    }

    public HandCollection getHands() {
        return this.m_Hands;
    }

    /**
     * Deals the cards
     */
    public void deal(boolean fastForward) {
        Random randomObj = new Random();
        int randomIndex = 0;  // The randomly generated index
        int counter = 0;
        Card card;

        this.m_DealNumber++;
        this.m_TotalNumDeals++;

        for (Hand hand : this.m_Hands) {
            for (counter = hand.getCards().size() + 1; counter <= 5; counter++) {
                try {
                    randomIndex = randomObj.nextInt(this.m_Deck.size()) + 1;
                    card = this.m_Deck.getCard(randomIndex);

                     System.out.println("CARD NUMBER ---->> " + card.getCardNumber() + card.getSuite());
                    //TODO: Check if adding by reference works fine
                    //hand.getCards().add(Card.Copy(card));//original C# code
                    hand.getCards().add(card);
                    this.m_Deck.remove(card);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    /// <summary>
    /// Records the current state of the game in the deal history
    /// </summary>
    /**
     * Records the current st5ate of the game in the deal history
     */
    private void recordDealHistory() {
        for (Hand hand : this.m_Hands) {

            //System.out.println("Hand " + hand.getHandNumber() + ". Num Cards " + hand.getCards().size());

            this.m_DealHistory.add(new DealHistoryRecord(this.m_DealNumber, hand.toString(),
                    hand.getCards().getCard(1).toString(), hand.getCards().getCard(2).toString(),
                    hand.getCards().getCard(3).toString(), hand.getCards().getCard(4).toString(),
                    hand.getCards().getCard(5).toString()));
        }
    }

    public void Discard() throws PokerException {
        if (this.m_DeadlockHasOccured) {
            throw new PokerException("A DeadLock has occured. \nMust reset the game");
        }

        if (this.m_Hands.getHand(1).getCards().size() != 5 || this.m_Hands.getHand(2).getCards().size() != 5
                || this.m_Hands.getHand(3).getCards().size() != 5 || this.m_Hands.getHand(4).getCards().size() != 5
                || this.m_Hands.getHand(5).getCards().size() != 5 || this.m_Hands.getHand(6).getCards().size() != 5) {
            throw new PokerException("All hands must have five cards");
        }

        this.recordDealHistory();

        for (Hand hand : this.m_Hands) {
            hand.getCards().sortByCardNumber();
            hand.checkCards();

            if (hand.isQuadruple()) {
                this.setThereIsAWinningHand(true);
                System.out.println("Quadruple"+hand.isQuadruple());
                System.out.println((m_ThereIsAWinningHand));
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getUnwantedCard());
            } //Full House, Straight or Flush is a winning hand
            else if (hand.isFullHouse() || hand.isStraight() || hand.isFlush()) {
                // No need to remove any cards from the hand as all the cards are kept
                this.setThereIsAWinningHand(true);
                System.out.println("Full house"+hand.isFullHouse());
                 System.out.println((m_ThereIsAWinningHand));
            } else if (hand.isPossDEStraightFlush()) {
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getUnwantedCard());
                hand.setHandType(new HandType(HandType.HandTypeEnum.PossibleDblEndedStraightFlush));
            } else if (hand.isPossStraightFlush()) {
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getUnwantedCard());
                hand.setHandType(new HandType(HandType.HandTypeEnum.PossibleInsideStraightFlush));
            } else if (hand.isPossibleFlush()) {
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getUnwantedCard());
                hand.setHandType(new HandType(HandType.HandTypeEnum.PossibleFlush));
            } else if (hand.isPossDEStraight3SameSuite()) {
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getUnwantedCard());
                hand.setHandType(new HandType(HandType.HandTypeEnum.PossibleDblEndedStraight3SameSuite));
            } else if (hand.isPossDEStraight()) {
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getUnwantedCard());
                hand.setHandType(new HandType(HandType.HandTypeEnum.PossibleDblEndedStraight));
            } else if (hand.isPossStraight3SameSuite()) {
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getUnwantedCard());
                hand.setHandType(new HandType(HandType.HandTypeEnum.PossibleInsideStraight3SameSuite));            
            } 
            else if (hand.isPossStraight()) {
                if (hand.isOnePair()) {
                    this.RemoveNonMatchingCards(hand, hand.getCards().getCard(hand.getFirstDoubleIndex()).getCardNumber());
                    hand.setHandType(new HandType(HandType.HandTypeEnum.OnePair));
                } else {
                    CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getUnwantedCard());
                    hand.setHandType(new HandType(HandType.HandTypeEnum.PossibleInsideStraight));
                }
            } else if (hand.isThreeOfAKind()) {
                this.RemoveNonMatchingCards(hand, hand.getCards().getCard(hand.getTripleIndex()).getCardNumber());
                hand.setHandType(new HandType(HandType.HandTypeEnum.ThreeOfAKind));
            } else if (hand.isTwoPairs()) {
                this.HandleTwoPairs(hand);
                hand.setHandType(new HandType(HandType.HandTypeEnum.TwoPairs));
            } else if (hand.isOnePair()) {
                this.RemoveNonMatchingCards(hand, hand.getCards().getCard(hand.getFirstDoubleIndex()).getCardNumber());
                hand.setHandType(new HandType(HandType.HandTypeEnum.OnePair));
            } else //it's a single
            {
                //Keep the highest card which is the last card in the hand since the cards are sorted
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getCards().getCard(1));  // hand now has four cards
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getCards().getCard(1));  // hand now has three cards
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getCards().getCard(1));  // hand now has two cards
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, hand.getCards().getCard(1));  // hand now has one card
                hand.setHandType(new HandType(HandType.HandTypeEnum.Single));
            }

            hand.getCards().sortByCardNumber();

        }//foreach

        if (this.isThereIsAWinningHand()) {
             System.out.println((m_ThereIsAWinningHand));
            this.WorkoutWinningHandNumber();
            this.PayWinningHand();

            onAHandHasWon();
            this.payBonus();
            this.m_TournamentGameCounter++;
        } else {
            this.CalculateOdds();
            this.EnforceNoBetsBusinessRules();
        }

        onDealNumberChanged();
    } //Discard
    public void payBonus()
    {
     
    }

    public void WorkoutWinningHandNumber() {
        this.m_WinnerTypeName = "";
        if (HandCollection.numStraightFlush(this.m_Hands) > 0) {
            //The Winning Hand is the one that is a Straight Flush with highest card number and lowest suite
            int cardNumber = 0;
            int suite = 5;
            Card card;
            int handNumber = 0;

            for (Hand hand : this.m_Hands) {
                if (hand.isStraightFlush()) {
                    card = hand.getCards().getCard(hand.getCards().size());

                    //TODO: Make sure the ordinal comparisions work correctly
                    if ((card.getCardNumber().ordinal() == cardNumber && card.getSuite().ordinal() < suite)
                            || (int) card.getCardNumber().ordinal() > cardNumber) {
                        cardNumber = card.getCardNumber().ordinal();
                        suite = card.getSuite().ordinal();
                        handNumber = hand.getHandNumber();
                    }
                }
            }

            this.m_WinningHandNumber = handNumber;
            this.m_WinnerTypeName = "Straight,Flush";
        } else if (HandCollection.numFourOfAKind(this.m_Hands) > 0) {
            //The Winning Hand is the one that is Four of a Kind with highest card Number
            int cardNumber = -1;
            Card card;
            int handNumber = 0;

            for (Hand hand : this.m_Hands) {
                if (hand.isQuadruple()) {
                    card = hand.getCards().getCard(hand.getCards().size());
                    if (card.getCardNumber().ordinal() > cardNumber) {
                        System.out.println(hand.getCards().getCard(1).getCardNumber());
                        System.out.println(hand.getCards().getCard(2).getCardNumber());
                        System.out.println(hand.getCards().getCard(3).ShortName);
                        System.out.println(hand.getCards().getCard(4).ShortName);
                        //System.out.println(hand.getCards().getCard(5).toString());
                        //System.out.println(hand.getCards().getCard(2).toString());
                       
                        cardNumber = card.getCardNumber().ordinal();
                        handNumber = hand.getHandNumber();
                    }
                }
            }

            this.m_WinningHandNumber = handNumber;
            this.m_WinnerTypeName = "Four,of,a,Kind";
        } else if (HandCollection.numFullHouse(this.m_Hands) > 0) {
            //The Winning Hand is the one that is a Full House with highest card Number
            int cardNumber = -1;
            Card card;
            int handNumber = 0;

            for (Hand hand : this.m_Hands) {
                 System.out.println(hand.isFullHouse());
                if (hand.isFullHouse()) {
                    card = hand.getCards().getCard(hand.getTripleIndex());
                     
                    if (card.getCardNumber().ordinal() > cardNumber) {
                        System.out.println(hand.getCards());
                        System.out.println(hand.getCards().getCard(1).ShortName);
                        System.out.println(hand.getCards().getCard(2).ShortName);
                        System.out.println(hand.getCards().getCard(3).ShortName);
                        System.out.println(hand.getCards().getCard(4).ShortName);
                        System.out.println(hand.getCards().getCard(5).ShortName);
                        cardNumber = card.getCardNumber().ordinal();
                        handNumber = hand.getHandNumber();
                        
                       
                    }
                }
            }

            this.m_WinningHandNumber = handNumber;
            this.m_WinnerTypeName = "Full,House";
        } else if (HandCollection.numFlush(this.m_Hands) > 0) {
            //The Winning Hand is the one that is a Flush with highest card number and lowest suite
            int cardNumber = 0;
            int suite = 0;
            Card card;
            int handNumber = 0;

            for (Hand hand : this.m_Hands) {
                if (hand.isFlush()) {
                    card = hand.getCards().getCard(hand.getCards().size());
                    if ( (card.getSuite().ordinal() > suite) || (((card.getCardNumber().ordinal()) > cardNumber)  && (card.getSuite().ordinal() >= suite)) ) {
                        cardNumber = card.getCardNumber().ordinal();
                        suite = (int) card.getSuite().ordinal();
                        handNumber = hand.getHandNumber();
                    }
                }
            }

            this.m_WinningHandNumber = handNumber;
            this.m_WinnerTypeName = "Flush";
        } else if (HandCollection.numStraight(this.m_Hands) > 0) {
            //The Winning Hand is the one that is a Straight with highest card number and lowest suite
            int cardNumber = 0;
            int suite = 5;
            Card card;
            int handNumber = 0;
            for (Hand hand : this.m_Hands) {
                if (hand.isStraight()) {
                    card = hand.getCards().getCard(hand.getCards().size());
                    if ((card.getCardNumber().ordinal() == cardNumber && card.getSuite().ordinal() < suite)
                            || card.getCardNumber().ordinal() > cardNumber) {
                        cardNumber = card.getCardNumber().ordinal();
                        suite = card.getSuite().ordinal();
                        handNumber = hand.getHandNumber();
                    }
                }
            }

            this.m_WinningHandNumber = handNumber;
            this.m_WinnerTypeName = "Straight";
        } else {
            this.m_WinningHandNumber = 0;
        }
        System.out.println("WorkoutWinningHandNumber FUNCTION IS CALLED");
    }

    public void PayWinningHand() {
        //Increase the credit by the corresponding credits of the bets on the hand
        double amtWon = 0;
        for (Bet bet : this.m_Bets) {
            if (bet.getHandNumber() == this.m_WinningHandNumber) {
                amtWon += bet.getCredit();
                bet.getUser().setM_Credit(bet.getUser().getM_Credit() + Math.round(bet.getCredit()));
                System.out.println("PayWinningHand FUNCTION IS CALLED");
                playerWon = playerWon = bet.getUser();
                winnerName = playerWon.getM_Name();
                playerWonCredit = playerWon.getM_Credit();
                System.out.println("USER CREDIT ------>> " + bet.getUser().getM_Credit() + "BET CREDIT ------>> " + bet.getCredit());
            }
            
        }
        this.m_AmountWon = amtWon;
        //this.Credit = this.Credit + amtWon;
        //System.out.println("WINNIG PLAYER NAME ----->> " + playerWon.getM_Name());

    }

    private void CalculateOdds() {
        this.m_TotalRating = 0.0;

        this.CalcLeader();  //Workout and set the leader

        for (Hand hand : this.m_Hands) {
            hand.setAvailability(this.HandAvailability(hand));
            hand.setCardsLeftInDeck(this.m_Deck.size());
            hand.calculateChances();
            hand.calcBonusAndPenalty();
            this.m_TotalRating = this.m_TotalRating + hand.getRating();

        }

        for (Hand hand : this.m_Hands) {
          //  hand.setTotalRating(m_TotalRating);
           // System.out.println("RATING ----->> " + hand.getRating() + "\t ODDS ----->> " + hand.getOdds() + "\t FORMATTED ODDS ----->> " + hand.getFormattedOdds());
        }

    }

    private void EnforceNoBetsBusinessRules() {
        for (int parentIndex = 1; parentIndex <= this.m_Hands.size(); parentIndex++) {
            if (this.m_Hands.getHand(parentIndex).getAvailability() == 0) {
                boolean found = false;
                if (this.m_Hands.getHand(parentIndex).getHandType().getValue() == HandType.HandTypeEnum.TwoPairs) {
                    //Try to find the first card it needs in the other lock down hands...
                    boolean foundFirstCard = this.FindInLockDownHands(parentIndex,
                            this.m_Hands.getHand(parentIndex).getCards().getCard(this.m_Hands.getHand(parentIndex).getFirstDoubleIndex()).getCardNumber());

                    //Try to find the second card it needs in the other lock down hands...
                    boolean foundSecondCard = this.FindInLockDownHands(parentIndex,
                            this.m_Hands.getHand(parentIndex).getCards().getCard(this.m_Hands.getHand(parentIndex).getSecondDoubleIndex()).getCardNumber());

                    found = foundFirstCard && foundSecondCard;
                } else if (this.m_Hands.getHand(parentIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleDblEndedStraight) {
                    //Try to find the first card it needs in the other lock down hands...
                    boolean foundFirstCard = this.FindInLockDownHands(parentIndex,
                            this.m_Hands.getHand(parentIndex).getPosDEStraightNeededCardNumber1());

                    //Try to find the second card it needs in the other lock down hands...
                    boolean foundSecondCard = this.FindInLockDownHands(parentIndex,
                            this.m_Hands.getHand(parentIndex).getPosDEStraightNeededCardNumber2());

                    found = foundFirstCard && foundSecondCard;
                } else if (this.m_Hands.getHand(parentIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleDblEndedStraightFlush) {
                    //Try to find the first card it needs in the other lock down hands...
                    boolean foundFirstCard = this.FindInLockDownHands(parentIndex,
                            this.m_Hands.getHand(parentIndex).getPosDEStraightNeededCardNumber1(),
                            this.m_Hands.getHand(parentIndex).getNeededSuite());

                    //Try to find the first card it needs in the other lock down hands...
                    boolean foundSecondCard = this.FindInLockDownHands(parentIndex,
                            this.m_Hands.getHand(parentIndex).getPosDEStraightNeededCardNumber2(),
                            this.m_Hands.getHand(parentIndex).getNeededSuite());

                    found = foundFirstCard && foundSecondCard;
                } else if (this.m_Hands.getHand(parentIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleFlush) {
                    int numCardsWithSameSuite = 0;

                    for (int childIndex = 1; childIndex <= this.m_Hands.size(); childIndex++) {
                        if (childIndex != parentIndex) {
                            if (this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.TwoPairs
                                    || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleDblEndedStraightFlush
                                    || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleFlush
                                    || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleDblEndedStraight
                                    || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleDblEndedStraight3SameSuite
                                    || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.ThreeOfAKind) {
                                numCardsWithSameSuite += this.m_Hands.getHand(childIndex).getCards().NumSuites(this.m_Hands.getHand(parentIndex).getNeededSuite());
                            }
                        }
                    }
                    found = ((this.m_Hands.getHand(parentIndex).getCards().size() + numCardsWithSameSuite) == 13);
                }

                if (found) {
                    this.m_Hands.getHand(parentIndex).setLocked(true);  //NO BETS!!!
                }
            }
        }

        //Find out who many hands are taking NO BETS...
        int numHandsWithNoBets = 0;
        for (Hand hand : this.m_Hands) {
            if (hand.getLocked()) {
                numHandsWithNoBets++;
            }
        }

        //If at least one hand has been locked recalculate the total rating
        this.m_TotalRating = 0;
        for (Hand hand : this.m_Hands) {
            this.m_TotalRating = this.m_TotalRating + hand.getRating();
        }

        for (Hand hand : this.m_Hands) {
            hand.setTotalRating(m_TotalRating);
        }


        //If only one hand is left that is taking bets then make it the winner
        if (numHandsWithNoBets == this.m_Hands.size() - 1) {
            for (int index = 1; index <= this.m_Hands.size(); index++) {
                if (!this.m_Hands.getHand(index).getLocked()) {
                    this.setThereIsAWinningHand(true);
                    this.m_WinningHandNumber = index;
                    this.m_WinnerTypeName = "Win,By,Default";
                    this.PayWinningHand();
                    break;
                }
            }
        }

        //If all hands are not taking bets then a deadlock has occured
        if (numHandsWithNoBets == this.m_Hands.size()) {
            this.m_DeadlockHasOccured = true;

            onDeadLock();
        }
    }

    private boolean FindInLockDownHands(int parentIndex, Card.CardNumberEnum cardNumber) {
        boolean found = false;

        for (int childIndex = 1; childIndex <= this.m_Hands.size(); childIndex++) {
            if (childIndex != parentIndex) {
                if ((this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.TwoPairs
                        || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleDblEndedStraightFlush
                        || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleFlush
                        || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleDblEndedStraight
                        || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleDblEndedStraight3SameSuite
                        || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.ThreeOfAKind)
                        && this.m_Hands.getHand(childIndex).getCards().contains(cardNumber)) {
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    private boolean FindInLockDownHands(int parentIndex, Card.CardNumberEnum cardNumber, Card.SuitesEnum suite) {
        boolean found = false;

        for (int childIndex = 1; childIndex <= this.m_Hands.size(); childIndex++) {
            if (childIndex != parentIndex) {
                if ((this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.TwoPairs
                        || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleDblEndedStraightFlush
                        || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleFlush
                        || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleDblEndedStraight
                        || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.PossibleDblEndedStraight3SameSuite
                        || this.m_Hands.getHand(childIndex).getHandType().getValue() == HandType.HandTypeEnum.ThreeOfAKind)
                        && this.m_Hands.getHand(childIndex).getCards().contains(cardNumber, suite)) {
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    private void CalcLeader() {
        if (!this.CalcThreeOfAKindLeader()) {
            if (!this.CalcTwoPairsLeader()) {
                if (!this.CalcOnePairLeader()) {
                    if (!this.CalcSingleLeader()) {
                        //do nothing
                    }
                }
            }
        }
    }

    /// <summary>
    /// Works out if there is a Three of a Kind Leader and if found returns true
    /// </summary>
    /// <returns>true if a Three of a Kind leader is found</returns>
    private boolean CalcThreeOfAKindLeader() {
        Hand leaderHand = null;

        for (Hand hand : this.m_Hands) {
            if (hand.getHandType().getValue() == HandType.HandTypeEnum.ThreeOfAKind) {
                if (leaderHand == null) {
                    leaderHand = hand;
                } else if (hand.getCards().getCard(1).getCardNumber().ordinal() > leaderHand.getCards().getCard(1).getCardNumber().ordinal()) {
                    leaderHand = hand;
                }
            }
        }

        if (leaderHand != null) {
            leaderHand.setIsLeader(true);
            return true;
        } else {
            return false;
        }

    }

    private boolean CalcTwoPairsLeader() {
        Hand leaderHand = null;

        for (Hand hand : this.m_Hands) {
            if (hand.getHandType().getValue() == HandType.HandTypeEnum.TwoPairs) {
                if (leaderHand == null) {
                    leaderHand = hand;
                } else if (hand.getCards().getCard(3).getCardNumber().ordinal() > leaderHand.getCards().getCard(3).getCardNumber().ordinal()) {
                    leaderHand = hand;
                }
            }
        }

        if (leaderHand != null) {
            leaderHand.setIsLeader(true);
            return true;
        } else {
            return false;
        }

    }

    private boolean CalcOnePairLeader() {
        Hand leaderHand = null;

        for (Hand hand : this.m_Hands) {
            if (hand.getHandType().getValue() == HandType.HandTypeEnum.OnePair) {
                if (leaderHand == null) {
                    leaderHand = hand;
                } else if (hand.getCards().getCard(1).getCardNumber().ordinal() > leaderHand.getCards().getCard(1).getCardNumber().ordinal()) {
                    leaderHand = hand;
                }
            }
        }

        if (leaderHand != null) {
            leaderHand.setIsLeader(true);
            return true;
        } else {
            return false;
        }

    }

    private boolean CalcSingleLeader() {
        Hand leaderHand = null;

        for (Hand hand : this.m_Hands) {
            if (hand.getHandType().getValue() == HandType.HandTypeEnum.Single) {
                if (leaderHand == null) {
                    leaderHand = hand;
                } else if (hand.getCards().getCard(1).getCardNumber().ordinal() > leaderHand.getCards().getCard(1).getCardNumber().ordinal()) {
                    leaderHand = hand;
                } else if (hand.getCards().getCard(1).getCardNumber() == leaderHand.getCards().getCard(1).getCardNumber()
                        && hand.getCards().getCard(1).getSuite().ordinal() > leaderHand.getCards().getCard(1).getSuite().ordinal()) {
                    leaderHand = hand;
                }
            }
        }

        if (leaderHand != null) {
            leaderHand.setIsLeader(true);
            return true;
        } else {
            return false;
        }

    }

    private void HandleTwoPairs(Hand hand) {

        for (Card card : hand.getCards()) {
            if (card.getCardNumber() != hand.getCards().getCard(hand.getFirstDoubleIndex()).getCardNumber()
                    && card.getCardNumber() != hand.getCards().getCard(hand.getSecondDoubleIndex()).getCardNumber()) {
                CardCollection.MoveCard(hand.getCards(), this.m_Deck, card);
                break;
            }
        }
    }

    /**
     * Checks if deadlock has occured
     * @return true if a DeadLock occured after a discard action, false otherwise.
     */
    public boolean isDeadLockHasOccured() {
        return this.m_DeadlockHasOccured;
    }

    public int getWinningHandNumber() {
        return this.m_WinningHandNumber;

    }

    /// <summary>
    /// Removes the cards from a hand that don't match the CardNumber passed
    /// in the parameter and adds them back on the deck
    /// </summary>
    /// <param name="hand"></param>
    /// <param name="cardNumber"></param>
    private void RemoveNonMatchingCards(Hand hand, Card.CardNumberEnum cardNumber) {
        CardCollection newCards;

        newCards = CardCollection.Copy(hand.getCards());
        //First need to add them back on the deck

        for (Card card : hand.getCards()) {
            if (card.getCardNumber() != cardNumber) {
                CardCollection.MoveCard(newCards, this.m_Deck, card);
            }
        }

        hand.getCards().clear();

        for (Card card : newCards) {
            hand.getCards().add(card);
        }

    }

    private int HandAvailability(Hand hand) {
        CardCollection cards = new CardCollection(this.m_Deck.size());

        switch (hand.getHandType().getValue()) {
            case OnePair:
            case ThreeOfAKind:
            case Single:

                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards, hand.getCards().getCard(1).getCardNumber());
                break;

            case PossibleInsideStraight:

                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards, hand.getNeededCardNumber());
                break;

            case PossibleInsideStraight3SameSuite:

                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards, hand.getNeededCardNumber());
                CardCollection.CopyCardsWithSameSuite(this.m_Deck, cards, hand.getNeededSuite());
                break;

            case TwoPairs:

                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards, hand.getCards().getCard(1).getCardNumber());
                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards, hand.getCards().getCard(3).getCardNumber());
                break;

            case PossibleDblEndedStraight:

                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards, hand.getPosDEStraightNeededCardNumber1());
                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards, hand.getPosDEStraightNeededCardNumber2());
                break;

            case PossibleDblEndedStraight3SameSuite:

                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards, hand.getPosDEStraightNeededCardNumber1());
                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards, hand.getPosDEStraightNeededCardNumber2());
                CardCollection.CopyCardsWithSameSuite(this.m_Deck, cards, hand.getNeededSuite());
                break;

            case PossibleFlush:

                CardCollection.CopyCardsWithSameSuite(this.m_Deck, cards, hand.getNeededSuite());
                break;

            case PossibleInsideStraightFlush:

                CardCollection.CopyIdenticalCards(this.m_Deck, cards,
                        new Card(hand.getNeededCardNumber(), hand.getNeededSuite()));

                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards, hand.getNeededCardNumber());

                CardCollection.CopyCardsWithSameSuite(this.m_Deck, cards, hand.getNeededSuite());

                break;

            case PossibleDblEndedStraightFlush:
                CardCollection.CopyIdenticalCards(this.m_Deck, cards,
                        new Card(hand.getPosDEStraightNeededCardNumber1(), hand.getNeededSuite()));

                CardCollection.CopyIdenticalCards(this.m_Deck, cards,
                        new Card(hand.getPosDEStraightNeededCardNumber2(), hand.getNeededSuite()));

                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards,
                        hand.getPosDEStraightNeededCardNumber1());

                CardCollection.CopyCardsWithSameCardNumber(this.m_Deck, cards,
                        hand.getPosDEStraightNeededCardNumber2());

                CardCollection.CopyCardsWithSameSuite(this.m_Deck, cards, hand.getNeededSuite());

                break;

        }

        return cards.size();

    }

    public boolean isThereIsAWinningHand() {
        return this.m_ThereIsAWinningHand;
    }

    private void setThereIsAWinningHand(boolean thereIsAWinningHand) {
        this.m_ThereIsAWinningHand = thereIsAWinningHand;
    }

    public List<Bet> getBetsPlaced() {
        return this.m_Bets;
    }

    /// <summary>
    /// Returns all the bets that were placed before the current game
    /// </summary>
    public List<Bet> getPastBets() {
        return this.m_PastBets;
    }

    public void PlaceBet(int handNumber, double amount, Player player) throws PokerException {
        double handCredit;

        if (this.isLocked()) {
            return;  //Can't take bets if the game is locked.
        }
        if (player.isBetsLocked()) {
            return;  //Can't place a bet if all bets locked.
        }
        if (this.m_DealNumber == 0) {
            throw new PokerException(
                    "A bet cannot be placed at this stage. The cards must be dealt at least once before any bets can be placed");
        }

        //Find the hand
        if (this.m_Hands.getHand(handNumber).getLocked()) {
            throw new PokerException(
                    "Player " + player.getM_Name() + " cannot place a bet on hand " + handNumber + " because the hand is locked");
        }

        if (amount <= 0) {
            throw new PokerException(
                    "Invalid bet for player " + player.getM_Name() + ". The amount must be greater than zero.");
        }

        if (player.getM_Credit() - amount < 0) {
            throw new PokerException("Bet is too large for player " + player.getM_Name());
        }

        //Try to find out if this player has already put the same bet
        boolean betFound = false;
       /* for (Bet bet : this.m_Bets) {
            if (bet.sameAs(this.m_DealNumber, player, handNumber, amount)) {
                betFound = true;
                break;
            }
        }*/

        if (betFound) {
            return;
        }

        handCredit = this.m_Hands.getHand(handNumber).getUnFormatedOdds() * amount;
        this.m_Bets.add(new Bet(this.m_GameNumber, this.m_DealNumber, handNumber, amount, handCredit, player));
        this.m_Bets.get(this.m_Bets.size() - 1).setBetID(this.m_Bets.size());
        player.setM_Credit(player.getM_Credit() - amount);
        this.m_TotalBet += amount;
        boolean user_found = false ;
       
        //TODO: Locate this method
        //BetPlaced(this.m_Bets.size(), handNumber, amount, player);
        onBetPlacedEvent(this.m_Bets.size(), handNumber, amount, player);
    }

    public int getDealNumber() {
        return this.m_DealNumber;
    }

    public int getGameNumber() {
        return this.m_GameNumber;
    }

    public CardCollection getDeck() {
        return this.m_Deck;
    }

    public double getTotalRating() {
        return this.m_TotalRating;
    }

    public DealsCollection getDealHistory() {
        return this.m_DealHistory;
    }

    public double getAmountWon() {
        return this.m_AmountWon;
    }

    public double TotalBet() {
        return this.m_TotalBet;
    }

    public String WinnerName() {
        return this.m_WinnerTypeName;
    }

        void setHouseIdOnDeal(String houseId) {
        this.m_HouseIdOnDeal = houseId;
    }
//
//    void setPlayerIdOnDeleteBet(String playerId) {
//        this.m_PlayerIdOnDeleteBet = playerId;
//    }

    void setHouseIdOnBetPlaced(String houseId) {
        this.m_HouseIdOnBetPlaced = houseId;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }
}