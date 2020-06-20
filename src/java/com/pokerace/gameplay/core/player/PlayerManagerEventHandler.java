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
public interface PlayerManagerEventHandler extends EventListener{
    public void playerInsert();
    public void playerCreditChanged();
    public void playerUpdate();
    public void playerBitletsChanged();
    public void playerBonusesChanged();
}
