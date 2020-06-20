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
public abstract class AbstractCommandFactory 
{   
    public abstract void command(Dealer dealer,String message,User user);
  //  public abstract void start();
}
