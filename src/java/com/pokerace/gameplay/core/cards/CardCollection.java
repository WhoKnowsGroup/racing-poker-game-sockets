package com.pokerace.gameplay.core.cards;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Verified: OK
 * @author Administrator
 */
public class CardCollection implements Collection<Card> {

    private int maxItems = 0;
    private List<Card> list = new ArrayList<Card>();

    /**
     * Verified: OK
     * @param maxItems 
     */
    public CardCollection(int maxItems) {
        this.maxItems = maxItems;
    }

    /**
     * 
     * @return 
     */
    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Verified: OK
     * Original function name in .NET: Exists(Card card)
     * @param o
     * @return 
     */
    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<Card> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * Verified: OK
     * Function in .NET code did not have return but threw and exception instead
     * @param card
     * @return true if added to list else false.
     */
    @Override
    public boolean add(Card card) {
        if (list.size() < this.maxItems) {
            return list.add(card);
        }

        return false;
    }
    /**
     * Verified: OK
     * Removes a card from the list. Card has a custom equals method implemented
     * for the remove comparison
     * @param o
     * @return true if remove was successful, false otherwise
     */
    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addAll(Collection<? extends Card> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        list.clear();
    }

    /**
     * This method corresponds to the get(index) method of a collection. It must
     * be used only for purpose of iterative for loops.
     * @param index The index to be fetched
     * @return The Card corresponding to the passed index in the Collection.
     */
    public Card get(int index){
        return list.get(index);
    }
    
    /**
     * The complete program operates on an index starting with 1 instead of 0,
     * hence the get operation is performed by subtracting 1 from the index passed
     * in as the parameter
     * @param index The card index beginning from 1. This is offset by 1 to get 
     * real index in list
     * @return {@link Card} if found else null
     */
    public Card getCard(int index) {
        return list.get(index - 1);//offset by 1 to get real index
      //   return list.get(index - 2);
      
    }

    /**
     * Verified: OK
     * Sorts the cards on the card number
     */
    public void sortByCardNumber() {
        Collections.sort(list, new CardNumberComparator());
    }

    /**
     * Verified: OK
     * Removes all cards except the exemptCard passed as paramter
     * @param exemptCard The card to be retained in the list
     */
    private void removeNonMatchCards(Card exemptCard) {
        list.clear();
        list.add(exemptCard);
    }

    //TODO: Remove this function if it is never used after finishing porting
    /**
     * Verified: OK
     * Fetches the smallest card in the card list. Search is on card number.
     * @return The smallest card in the card list
     */
    public Card getSmallestCard() {
        return Collections.min(list, new CardNumberComparator());
    }

    //TODO: Remove this function if program works without cloning.
    /**
     * This function should not be supported.
     * @param cardCollection
     * @return 
     */
    private static CardCollection copy(CardCollection cardCollection) {
        throw new UnsupportedOperationException("Will not be supported");
    }

    /**
     * Verified: OK
     * Counts number of cards of a specific suite
     * @param suite The suites who's occurrence is to be computed
     * @return the number of cards with the same suite
     */
    public int NumSuites(Card.SuitesEnum suite) {
        int count = 0;
        for (Card card : list) {
            if (card.getSuite() == suite) {
                count++;
            }
        }
        return count;
    }

    /**
     * Verified: OK
     * @return 
     */
    public int NumHearts() {
        return NumSuites(Card.SuitesEnum.Hearts);
    }

    /**
     * Verified: OK
     * @return 
     */
    public int NumDiamonds() {
        return NumSuites(Card.SuitesEnum.Diamonds);
    }

    /**
     * Verified: OK
     * @return 
     */
    public int NumClubs() {
        return NumSuites(Card.SuitesEnum.Clubs);
    }

    /**
     * Verified: OK
     * @return 
     */
    public int NumSpades() {
        return NumSuites(Card.SuitesEnum.Spades);
    }

    /**
     * Verified: OK
     * Checks if all cards in the collection are of the same suite
     * @return true if all cards are of same suite, false otherwise. Will return
     * false for an empty list.
     */
    public boolean allCardsSameSuite() {
        Card.SuitesEnum suite = null;
        
        for (Card card : list) {
            if (suite == null) {
                suite = card.getSuite();
            }else if(card.getSuite() != suite){
                return false;
            }
        }
        
        return !list.isEmpty();
    }
    
    /**
     * Verified: OK
     * Counts the number of cards in the list of a given card number
     * @param cardNumber The card number who's occurrence to count
     * @return the number of cards with the given card number in the list
     */
    public int numCardsWithSameCardNumber(Card.CardNumberEnum cardNumber){
        int count = 0;
        
        for(Card card : list){
            if(card.getCardNumber() == cardNumber){
                count ++;
            }
        }
        
        return count;
    }
    
    /**
     * Verified: OK
     * Copies cards with the same card number from the source to the destination collection
     * @param source
     * @param destination
     * @param cardNumber 
     */
    public static void CopyCardsWithSameCardNumber(CardCollection source, CardCollection destination, Card.CardNumberEnum cardNumber){
        for(Card card : source){
            if(card.getCardNumber() == cardNumber){
                if(!destination.contains(card)){
                    //TODO: Card added here without cloning. Make sure all program logic works fine during testing
                    destination.add(card);
                }
            }
        }
    }
    
    /**
     * Verified: OK
     * Copies cards with the same suite from source to destination collection
     * @param source
     * @param destination
     * @param suite 
     */
    public static void CopyCardsWithSameSuite(CardCollection source, CardCollection destination, Card.SuitesEnum suite){
        for(Card card : source){
            if(card.getSuite() == suite){
                if(!destination.contains(card)){
                    //TODO: Card added here without cloning. Make sure all program logic works fine during testing
                    destination.add(card);
                }
            }
        }
    }
    //TODO: Each collection should ideally have only one unique card. It does not make
    //sense to seach through the whole list when coping only one card from source to destination.
    //This needs to be tested and optimized.
    /**
     * Verified: OK
     * Copies the same card from source to destination
     * @param source
     * @param destination
     * @param cardToCheck 
     */
    public static void CopyIdenticalCards(CardCollection source, CardCollection destination, Card cardToCheck){
        for(Card card : source){
            if(card.equals(cardToCheck)){
                if(!destination.contains(card)){
                    //TODO: Card added here without cloning. Make sure all program logic works fine during testing
                    destination.add(card);
                }
            }
        }
    }
    /**
     * Verified: OK
     * @param cardNumber
     * @param suite
     * @return 
     */
    public boolean contains(Card.CardNumberEnum cardNumber, Card.SuitesEnum suite){
        return contains(new Card(cardNumber, suite));
    }
    /**
     * Verified: OK
     * @param cardNumber
     * @return 
     */
    public boolean contains(Card.CardNumberEnum cardNumber){
        return numCardsWithSameCardNumber(cardNumber) > 0;
    }
    
    /**
     * Counts the cards with the same suite
     * @param suite The suite to count
     * @return number of cards with the same suite
     */
    public int numCardsWithSameSuite(Card.SuitesEnum suite){
        int count = 0;
        
        for(Card card : list){
            if(card.getSuite() == suite){
                count ++;
            }
        }
        return count;
    }
    
    /**
     * Counts number of cards with the same suite and number in the list
     * @param suite
     * @param cardNumber
     * @return number of cards with same suite and number
     */
    public int numIdenticalCards(Card.SuitesEnum suite, Card.CardNumberEnum cardNumber){
        int count = 0;
        
        for(Card card : list){
            if(card.getSuite() == suite && card.getCardNumber() == cardNumber){
                count ++;
            }
        }
        return count;
    }
    
    /**
     * Verified: OK
     * Adds a new copy of the specified card to the destination collection and removes
     * the same from the source collection if found in source collection. Card will be
     * added to destination collection irrespective of presence of card in source collection
     * @param source
     * @param destination
     * @param card 
     */
    public static void MoveCard(CardCollection source, CardCollection destination, Card card){
        Card newCard = new Card(card.getCardNumber(),card.getSuite());
        destination.add(newCard);
        source.remove(card);
    }
    
    public static CardCollection Copy(CardCollection collection){
        CardCollection newCollection = new CardCollection(collection.getMaxItems());
        
        for(Card card : collection.list){
            Card newCard = new Card();
            newCard.setCardNumber(card.getCardNumber());
            newCard.setSuite(card.getSuite());
            newCollection.add(newCard);
        }
        return newCollection;
    }
    /**
     * Verified: OK
     * Getter for maxItems data member
     * @return maxItems
     */
    public int getMaxItems(){
        return maxItems;
    }
}
