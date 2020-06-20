package com.pokerace.gameplay.core.player;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.EventListener;

/**
 *
 * @author Administrator
 */
public interface PlayerEvent extends EventListener{
    public void creditChanged();
    //public void tournamentChanged();
    public void bitletsChanged();
    public void bonusesChanged();
}
