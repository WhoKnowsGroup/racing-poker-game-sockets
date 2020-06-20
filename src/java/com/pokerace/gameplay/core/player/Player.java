package com.pokerace.gameplay.core.player;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pokerace.gameplay.core.player.PlayerEvent;
import com.pokerace.gameplay.core.player.PlayerException;
import com.pokerace.gameplay.core.player.PlayerManager;

/**
 *
 * @author Administrator
 */
public class Player implements Comparable<Player> {
    private boolean m_BetsLocked = false;
    private String m_Name = "";
    private double m_Credit;
    private double no_of_tournaments;
    private double no_of_bitlets;
    private double no_of_bonuses;
    private double[] no_of_shots = new double[9];
    private double player_level;
    boolean betsLocked;
    private String playerLost;
    private String ID;
    private List<PlayerEvent> playerEventList = new ArrayList<PlayerEvent>();
    private PlayerManager playerManager;
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }
     public void setNo_of_shots(double[] no_of_shots) 
     {
        this.no_of_shots = no_of_shots;
    }

    public double[] getNo_of_shots() {
        return no_of_shots;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    
    public Player(PlayerManager playerManager){
		this.playerManager = playerManager;
        for(int i=0 ; i<9 ; i++)
        {
            no_of_shots[i] =0;
        }
    }

    public void addPlayerEvent(PlayerEvent playerEvent) {
        playerEventList.add(playerEvent);
    }

    public void removePlayerEvent(PlayerEvent playerEvent) {
        playerEventList.remove(playerEvent);
    }

    private void onCreditChanged() {
        for (PlayerEvent playerEvent : playerEventList) {
            playerEvent.creditChanged();
        }
    }
     private void onBitletChanged() {
        for (PlayerEvent playerEvent : playerEventList) {
            playerEvent.bitletsChanged();
        }
    }
      private void onBonusesChanged() {
        for (PlayerEvent playerEvent : playerEventList) {
            playerEvent.bonusesChanged();
        }
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        if (ID.equals("")) {
            try {
                throw new PlayerException("Player id must be greater than zero");
            } catch (PlayerException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.ID = ID;

    }

    public String getM_Name() {
        return m_Name;
    }
    public void setM_shots(int index)
    {
        this.no_of_shots[index] += 1;
    }
    public void setM_Name(String m_Name) {
        if (!m_Name.isEmpty()) {
            this.m_Name = m_Name;
        } else {
            try {
                throw new PlayerException("Name cannot be blank");
            } catch (PlayerException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     public double getM_shots(int index)
    {
        //this.no_of_shots[index] += 1;
        return this.no_of_shots[index];
    }
    public double getM_Credit() {
        return m_Credit;
    }
    public double getM_bitlets()
    {
        return this.no_of_bitlets;
    }
    public double getM_bonuses()
    {
        return this.no_of_bonuses;
    }

    public void setM_Credit(double m_Credit) {
        if (m_Credit < 0) {
            try {
                throw new PlayerException("Credit must be greater than zero");
            } catch (PlayerException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.m_Credit = m_Credit;

        playerManager.RaisePlayerCreditChangedEvent();
    }

    public void setM_bitlets(double m_bitlets)
    {
        this.no_of_bitlets = m_bitlets ;
        playerManager.RaisePlayerBitletsChangedEvent();
    }
    
     public void setM_player_level(double player_level)
    {
        this.player_level = player_level;
        playerManager.RaisePlayerBitletsChangedEvent();
    }
       public double getM_player_level()
    {
       // this.player_level = player_level;
        //playerManager.RaisePlayerBitletsChangedEvent();
        return this.player_level;
    }
    
    public void setM_bonuses(double m_bonuses)
    {
        this.no_of_bonuses = m_bonuses ;
        playerManager.RaisePlayerBonusesChangedEvent();
    }
    
    /// <summary>
    /// Use this method to get the default credit for a new player
    /// </summary>
    /// <returns></returns>
    public static double GetDefaultCredit() {
        return 10000.0;
    }
    /// <summary>
    /// If set to true indicates that this player has locked all bets for the current deal.
    /// </summary>

    public boolean isBetsLocked() {
        return betsLocked;
    }

    public void setBetsLocked(boolean betsLocked) {
        this.betsLocked = betsLocked;
    }
    
    /*
     * Code written by Omkar 
     */
    
    public void setPlayerLost(String playerLost) {
        this.playerLost = playerLost;
    }
    
    public double getNo_of_tournaments() {
        return no_of_tournaments;
    }

    public void setNo_of_tournaments(double no_of_tournaments) {
        this.no_of_tournaments = no_of_tournaments;
    }

   

    @Override
    public int compareTo(Player p2) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Double credit =  ((Player)p2).getM_Credit();
        return (int) (credit - this.getM_Credit()) ;
    }
}
