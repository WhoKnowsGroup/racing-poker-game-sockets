/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sockets.commands;

import static java.lang.Thread.MIN_PRIORITY;

import java.util.HashMap;

import sun.rmi.runtime.Log;

import com.pokerace.gameplay.core.cards.Dealer;
import com.sockets.players.User;

/**
 *
 * @author lokesh
 */
public class GameController {
    
    private HashMap<String,AbstractCommandFactory> commands ;

    public GameController(HashMap<String,AbstractCommandFactory> commands) 
    {
         this.commands = commands;          
    }
    
    public void executeAction(String input,Dealer dealer,String request, User user)
    {
        if(!commands.containsKey(input))
        {
          Log.getLog("InvalidCommand",input,MIN_PRIORITY);
        }
        else
        commands.get(input).command(dealer,request,user);
    }
    
    
}
