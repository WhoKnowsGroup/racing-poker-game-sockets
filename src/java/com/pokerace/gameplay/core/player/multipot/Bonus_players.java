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
import java.util.ArrayList;
import java.util.List;

import com.pokerace.gameplay.core.player.Player;

public class Bonus_players {
    
    public Player player;
    public String bonus_type ;
    public double credit_amount ;
    public double total_bets ; 
    public double bitlets ;
    public double bonuses =0;
    public double player_level;
    public double[] shots = new double[] {0,0,0,0,0,0,0,0,0};
    List<Boolean> previous_bets = new ArrayList<Boolean>();
    
    public Bonus_players(Player player_id,String bonus_type , double total_bets,double credit_amount, double bitlets, double bonuses , double player_level)
    {
        this.player = player_id;
        this.bonus_type = bonus_type;
        this.credit_amount = credit_amount;
        this.total_bets = total_bets;
        this.bitlets = bitlets;
        this.bonuses += 1.0;
        this.player_level = player_level;
        if(this.bonus_type.equalsIgnoreCase("2shot"))
        {
            shots[0] +=1;
        }
        if(this.bonus_type.equalsIgnoreCase("3shot"))
        {
            shots[1] +=1;
        }
        if(this.bonus_type.equalsIgnoreCase("4shot"))
        {
            shots[2] +=1;
        }
        if(this.bonus_type.equalsIgnoreCase("5shot"))
        {
            shots[3] +=1;
        }
        if(this.bonus_type.equalsIgnoreCase("6shot"))
        {
            shots[4] +=1;
        }
        if(this.bonus_type.equalsIgnoreCase("7shot"))
        {
            shots[5] +=1;
        }
        if(this.bonus_type.equalsIgnoreCase("8shot"))
        {
            shots[6] +=1;
        }
        if(this.bonus_type.equalsIgnoreCase("9shot"))
        {
            shots[7] +=1;
        }
        if(this.bonus_type.equalsIgnoreCase("10shot"))
        {
            shots[8] +=1;
        }
        
    } 
    
}
