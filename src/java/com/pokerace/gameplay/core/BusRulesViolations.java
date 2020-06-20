package com.pokerace.gameplay.core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class BusRulesViolations {

    private List<BusRuleViolation> busRuleViolationList = new ArrayList();

    public BusRuleViolation get(int index) {
        try {
            return busRuleViolationList.get(index);
        } catch (Exception ex) {
            return null;
        }
    }
    
//    public int count(){
//        return busRuleViolationList.size();
//    }

    public void add(BusRuleViolation busRuleViolation) {
        busRuleViolationList.add(busRuleViolation);
    }

    public List<BusRuleViolation> getBusRuleViolationList() {
        return busRuleViolationList;
    }
    
}
