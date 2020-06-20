package com.pokerace.gameplay.core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.EventListener;

import com.pokerace.gameplay.core.player.Player;

/**
 *
 * @author User
 */
public interface GameEventListener extends EventListener{
    public void gameEvent();
    public void betPlacedEvent(int betID, int handNumber, double betAmount, Player placedBy);
    public void betDeletedEvent(int betID);
    public void playerEvent(String playerID);
    public void gameReset();
    public void aHandHasWon();
    public void dealNumberChanged();
    public void deadLock();
    public void allPlayersHaveLockedBetsIn();
    public void playerHasLockedBets(String playerID);
    public void playerUnlocked(String playerID);
}
