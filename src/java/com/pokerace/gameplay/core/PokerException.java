package com.pokerace.gameplay.core;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author User
 */
public class PokerException extends Exception{
    public PokerException(){
        super("Unknown exception inside application");
    }
    
    public PokerException(String exception){
        super(exception);
    }
}
