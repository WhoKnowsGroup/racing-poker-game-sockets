package com.pokerace.gameplay.core.cards;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Verified: OK
 * @author User
 */
public class DealsCollection extends ArrayList<DealHistoryRecord> implements Serializable {
    
    public boolean add(DealHistoryRecord record){
        return super.add(record);
    }
}
