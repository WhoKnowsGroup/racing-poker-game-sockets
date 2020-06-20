package com.pokerace.gameplay.core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

import com.pokerace.gameplay.core.player.Player;

/**
 *  Verified: OK
 * @author Administrator
 */
public class Bet implements Serializable {

    private int GameNumber;
    private int DealNumber;
    private int BetID;
    private int HandNumber;
    private double Amount;
    private double Credit;
    private Player User;

    public Bet(int gameNumber, int dealNumber, int handNumber, double amount, double credit, Player user) {
        this.GameNumber = gameNumber;
        this.DealNumber = dealNumber;
        this.HandNumber = handNumber;
        this.Amount = amount;
        this.Credit = credit;
        this.User = user;
    }

    public int getBetID() {
        return BetID;
    }

    public void setBetID(int BetID) {
        this.BetID = BetID;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public void setCredit(double Credit) {
        this.Credit = Credit;
    }

    public void setDealNumber(int DealNumber) {
        this.DealNumber = DealNumber;
    }

    public void setGameNumber(int GameNumber) {
        this.GameNumber = GameNumber;
    }

    public void setHandNumber(int HandNumber) {
        this.HandNumber = HandNumber;
    }

    public void setUser(Player User) {
        this.User = User;
    }

    public double getAmount() {
        return Amount;
    }

    public double getCredit() {
        return Credit;
    }

    public int getDealNumber() {
        return DealNumber;
    }

    public int getGameNumber() {
        return GameNumber;
    }

    public int getHandNumber() {
        return HandNumber;
    }

    public Player getUser() {
        return User;
    }
    
    public boolean sameAs(int dealNumber, Player player, int handNumber, double amount){
        return this.DealNumber == dealNumber && this.User.getID() == player.getID() && this.HandNumber == handNumber && this.Amount == amount;
    }
}
