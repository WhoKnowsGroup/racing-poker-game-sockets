/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sockets.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.pokerace.gameplay.core.PokerException;
import com.pokerace.gameplay.core.cards.Dealer;
import com.sockets.players.User;

/**
 *
 * @author lokesh
 */
public class PlaceBet extends AbstractCommandFactory {

    @Override
    public void command(Dealer dealer, String request,User user) {
        try
        {
            dealer.getGame().PlaceBet(1, 100,dealer.getPlayer(user.getId()));
        } catch (PokerException ex) {
            Logger.getLogger(PlaceBet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
