package com.pokerace.gameplay.core.player.multipot;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lokesh
 */
public class Player_level implements Comparable<Player_level>
{
   Double player_level ;
   String player_name ;
   
   Player_level(Double player_level , String player_name)
   {
       this.player_level = player_level;
       this.player_name = player_name ;
       
   }

    @Override
    public int compareTo(Player_level t) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         int level =  t.player_level.intValue() ;
        return (int) (level - this.player_level.intValue()) ;
    }
}
