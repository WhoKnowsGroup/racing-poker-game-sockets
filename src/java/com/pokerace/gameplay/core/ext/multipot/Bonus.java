package com.pokerace.gameplay.core.ext.multipot;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lokesh
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.pokerace.gameplay.core.player.Player;

public class Bonus implements Serializable{
    
 public Player user ;
 public double total_bets ;
// public boolean profits_history;
 public double credits;
 List<Boolean> previous_profits ;
 public double bonus_amount ;
 public double bitlets ;
 public double bonuses;
 public boolean paid_bonus = false ;
 public double player_level = 0.0;
 
 Bonus(Player user,double total_bets)
 {
     this.user = user ;
     this.total_bets = total_bets ;
     this.credits = 0.0 ;
     this.previous_profits = new ArrayList<Boolean>();
     this.bonus_amount =0.0 ;
     
 }
 
    
}
