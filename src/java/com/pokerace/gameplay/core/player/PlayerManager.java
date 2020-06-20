package com.pokerace.gameplay.core.player;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.List;

import com.pokerace.gameplay.core.BusRuleException;
import com.pokerace.gameplay.core.BusRuleViolation;
import com.pokerace.gameplay.core.BusRulesViolations;
import com.pokerace.gameplay.core.ExceptionTypeEnums;

/**
 *
 * @author Administrator
 */
public class PlayerManager {
    //static instance removed to support multiple sessions
    //private static PlayerManager m_PlayerManager = new PlayerManager();

    private List<Player> m_Players = new ArrayList<Player>();
    
    private List<PlayerManagerEventHandler> playerManagerEventHandlersList = new ArrayList<PlayerManagerEventHandler>();
    
    public void addPlayerManagerEventHandler(PlayerManagerEventHandler playerManagerEventHandler) {
        playerManagerEventHandlersList.add(playerManagerEventHandler);
    }

    public void removePlayerManagerEventHandler(PlayerManagerEventHandler playerManagerEventHandler) {
        playerManagerEventHandlersList.remove(playerManagerEventHandler);
    }
    
    private void onPlayerInsert(){
        for (PlayerManagerEventHandler playerManagerEventHandler : playerManagerEventHandlersList) {
            playerManagerEventHandler.playerInsert();
        }
    }

    private void onPlayerCreditChanged() {
        for (PlayerManagerEventHandler playerManagerEventHandler : playerManagerEventHandlersList) {
            playerManagerEventHandler.playerCreditChanged();
        }
    }
     private void onPlayerBitletsChanged() {
        for (PlayerManagerEventHandler playerManagerEventHandler : playerManagerEventHandlersList) {
            playerManagerEventHandler.playerBitletsChanged();
        }
    }
     private void onPlayerBonusesChanged() {
        for (PlayerManagerEventHandler playerManagerEventHandler : playerManagerEventHandlersList) {
            playerManagerEventHandler.playerBonusesChanged();
        }
    }

    private void onPlayerUpdate() {
        for (PlayerManagerEventHandler playerManagerEventHandler : playerManagerEventHandlersList) {
            playerManagerEventHandler.playerUpdate();
        }
    }

    public void RaisePlayerCreditChangedEvent() {
        this.onPlayerCreditChanged();
    }
    public void RaisePlayerBitletsChangedEvent() {
        this.onPlayerBitletsChanged();
    }
    public void RaisePlayerBonusesChangedEvent() {
        this.onPlayerBonusesChanged();
    }

    public void ResetAllPlayersCredits(double newCredit) {
        for (Player player : this.m_Players) {
            player.setM_Credit(newCredit);
        }
    }

    private void ValidatePlayer(Player player) throws BusRuleException {
        BusRulesViolations violations = new BusRulesViolations();

        if (player.getM_Name() == null || player.getM_Name().trim().length() == 0) {
            violations.add(
                    new BusRuleViolation(
                    ExceptionTypeEnums.eInvalidProperty,
                    "Name", "Name is a mandatory field"));
        }

        if (player.getM_Credit() < 0) {
            violations.add(
                    new BusRuleViolation(
                    ExceptionTypeEnums.eInvalidProperty,
                    "Credit", "Credit must be greater than zero"));
        }

        if (!violations.getBusRuleViolationList().isEmpty()) {
            throw new BusRuleException(violations);
        }
    }

    public void Insert(Player player) throws BusRuleException {
        this.ValidatePlayer(player);

        BusRulesViolations violations = new BusRulesViolations();

        boolean found = false;
        for(Player p : this.m_Players){
            if(p.getM_Name().equals(player.getM_Name())){
                found = true;
                break;
            }
        }
        
        //TODO Change this
        
        if (found) {
            violations.add(
                    new BusRuleViolation(
                    ExceptionTypeEnums.eBusRuleViolation,
                    "InvalidPlayerName",
                    "A player with the same already exists."));
            throw new BusRuleException(violations);
        }

        //player.setID(String.valueOf(this.m_Players.size()) + 1);
        this.m_Players.add(player);

        onPlayerInsert();
    }

    public List<Player> getPlayers(){
        return this.m_Players;
    }
    
    public Player findByPlayerID(String playerID){
        for(Player player : this.m_Players){
            if(player.getID() == playerID){
                return player;
            }
        }
        
        return null;
    }
    public void changePlayerID(String currentPlayerID, String newPlayerId) throws PlayerException {
        if (this.findByPlayerID(newPlayerId) != null) {
            throw new PlayerException(
                    String.format("Invalid player id {0}. This Player id is already in use", newPlayerId));
        }
        Player player = findByPlayerID(currentPlayerID);
        player.setID(newPlayerId);
        onPlayerUpdate();
    }
}
