/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.core.player.multipot;

/**
 *
 * @author lokesh
 */
public class Player_shot implements Comparable<Player_shot>
{
    Double shot ;
    String Player_name  ;
    
    Player_shot(Double shot, String name)
    {
        this.shot = shot ;
        this.Player_name = name ;
    }
     @Override
    public int compareTo(Player_shot p1) 
    {
         Double credit =  ((Player_shot)p1).shot;
        return (int) (credit - this.shot) ;
    }

}
