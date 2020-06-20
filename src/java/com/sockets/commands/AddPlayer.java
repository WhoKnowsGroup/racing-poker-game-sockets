/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sockets.commands;

import com.pokerace.gameplay.core.cards.Dealer;
import com.sockets.players.User;

/**
 *
 * @author lokesh
 */
public class AddPlayer extends AbstractCommandFactory {

    @Override
    public void command(Dealer dealer,String id,User user) {
        
       dealer.addPlayer(id,user);
       
    }
    
    
}
