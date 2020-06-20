/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.potpoker;

import java.util.Collections;
import java.util.Date;
import java.util.Random;

import sun.rmi.runtime.Log;

import com.pokerace.gameplay.core.TournamentMode;
import com.pokerace.gameplay.core.cards.Card;
import com.pokerace.gameplay.core.cards.CardCollection;
import com.pokerace.gameplay.core.cards.Hand;
import com.pokerace.gameplay.core.cards.HandCollection;
import com.pokerace.gameplay.core.ext.multipot.MultipotGame;
import com.pokerace.gameplay.core.ext.multipot.MultipotTournament;
import com.pokerace.gameplay.core.player.Player;
import com.pokerace.gameplay.core.player.multipot.Bonus_players;
import com.pokerace.gameplay.core.player.multipot.Sort_player_level_and_top_shots;
import com.sockets.start.Server_State;

/**
 *
 * @author lokesh
 */
public class Tournament_potpoker extends MultipotTournament {
	public Tournament_potpoker(int total_games, String tournament_id, String tournament_name, int maxPlayers, int minPlayers, Double credits, Server_State serverState, int bitlets_required) {
		super(TournamentMode.POT, total_games, tournament_id, tournament_name, maxPlayers, minPlayers, credits, serverState, bitlets_required);
		
		//FIXME:  need to confirm that this is consistent with whatever the original code did
		this.credits_required = credits;
	}

	public void start_tournament() throws InterruptedException {
		Hand hand;
		HandCollection handCollection = new HandCollection();
		Random random = new Random();
		game.StartTournament(this.total_games); /* changes */
		Card card = new Card();

		// tournament_loading_time();
		String message = "";
		tournament_loading_time();
		if (playerCount <= minPlayers)
			tournament_loading_time();
		String message1 = "total_games:" + this.total_games;
		send_user(message1);

		while (this.game_number <= this.total_games) {
			try {
				if (game.m_GameNumber != 1 || this.game.m_ThereIsAWinningHand)
					Thread.sleep(10000);
				message = "start" + ":" + tournament_id;
				send_user(message);
				game.m_Deck = new CardCollection(52);
				game.winnerName = "";
				game.m_AmountWon = 0.0;

				for (int i = 0; i < 6; i++) {
					hand = new Hand(i + 1);
					handCollection.add(hand);
				}
				game.m_Hands = handCollection;

				System.out.println("=====================================");
				System.out.println("Starting Game " + this.game_number);

				this.game.Reset();
				Date date = new Date();
				this.timerFlag = true;

				while (!this.game.m_ThereIsAWinningHand) {
					while (this.timerFlag || check_timer_flag(date)) {
						message = "deal:start";
						send_user(message);
						Thread.sleep(1000);
						this.timerFlag = false;
						this.gameStatus = "dealing";
						this.game.deal(this.isFastForward());
						displayHandStates(this.game);
						//if (this.deal_number != 1)
							Thread.sleep(2500);
						this.game.Discard();
						message = "discard:true";
						this.discard = true;
						date = new Date();
						send_user(message);
						this.gameStatus = "bettime";
						System.out.println("DEAL NUMBER ------>> " + game.m_DealNumber);
						this.deal_number = this.game.m_DealNumber;
						displayHandStates(game);
						this.discard = false;
						String message34 = "discard:false";
						send_user(message34);
						System.out.println("WINNIG HAND NUMBER ------>> " + game.m_WinningHandNumber);
						System.out.println("WINNING AMOUNT ------>> " + game.m_AmountWon);
						System.out.println("WINNER NAME ------>> " + game.winnerName);
						date = new Date();
						if (game.m_WinningHandNumber > 0) {
							message = "winning_hand_number" + ":" + game.m_WinningHandNumber + " ";
							send_user(message);
							message = "";
							message = "winning_hand_type" + ":" + game.m_WinnerTypeName;
							send_user(message);
							for (Player player : players) {
								message = "connected_players" + ":";
								System.out.println("in player creddit");
								message += player.getM_Name() + ",";
								message += player.getM_Credit() + " ";
								send_user(message);
							}
							for (Bonus_players b : ((MultipotGame)game).m_bonus_players) {
								message = "Bonus_player" + ":";
								message += b.player.getM_Name() + ",";
								message += "Bonus_type" + "-";
								message += b.bonus_type + "*";
								message += "Bonus_amount" + "||";
								message += b.credit_amount + "()";
								message += "Bitlets" + "@";
								message += b.bitlets + "!";
								message += "Player_level" + "&";
								message += b.player_level;
								send_user(message);
								for (Player player : players) {
									if (b.player.getID().equalsIgnoreCase(player.getID())) {
										player.setM_bitlets(player.getM_bitlets() + b.bitlets);
										player.setM_bonuses(player.getM_bonuses() + 1.0);
										player.setM_player_level(player.getM_player_level() + b.player_level);
										if (b.bonus_type.equalsIgnoreCase("2shot")) {
											player.setM_shots(0);
										}
										if (b.bonus_type.equalsIgnoreCase("3shot")) {
											player.setM_shots(1);
										}
										if (b.bonus_type.equalsIgnoreCase("4shot")) {
											player.setM_shots(2);
										}
										if (b.bonus_type.equalsIgnoreCase("5shot")) {
											player.setM_shots(3);
										}
										if (b.bonus_type.equalsIgnoreCase("6shot")) {
											player.setM_shots(4);
										}
										if (b.bonus_type.equalsIgnoreCase("7shot")) {
											player.setM_shots(5);
										}
										if (b.bonus_type.equalsIgnoreCase("8shot")) {
											player.setM_shots(6);
										}
										if (b.bonus_type.equalsIgnoreCase("9shot")) {
											player.setM_shots(7);
										}
										if (b.bonus_type.equalsIgnoreCase("10shot")) {
											player.setM_shots(8);
										}
									}
								}
							}
							if (game.m_DealNumber == 1) {
								send_user("redeal");
							} else {
								this.game_number++;
							}
						} else {
							for (Player player : players) {
								message = "connected_players" + ":";
								System.out.println("in player creddit");
								message += player.getM_Name() + ",";
								message += player.getM_Credit() + " ";
								send_user(message);
							}
							message = "timer_flag" + ":" + "true";
							send_user(message);
							bettime_bots(date);
						}
					}
				}
			} catch (Exception e) {
				Log.getLog("InvalidPokerException", "Tournament", Thread.MIN_PRIORITY);
			}

		}
		Thread.sleep(4000);
		for (Player ply : players) {
			System.out.println(ply.getM_Credit());
		}
		Collections.sort(players);

		int i = 0;
		for (Player ply : players) {

			System.out.println(ply.getID());
			if (i == 0) {
				Double add_credits = this.credits_required * 5;
				ply.setM_Credit(add_credits);
				ply.setM_bitlets(this.bitlets_required * 5);
				message = "firstwinner:" + ply.getM_Name();
				send_user(message);
			} else if (i == 1) {
				Double add_credits = this.credits_required * 3;
				ply.setM_Credit(add_credits);
				ply.setM_bitlets((this.bitlets_required * 3));
				message = "secondwinner:" + ply.getM_Name();
				send_user(message);
			} else if (i == 2) {
				Double add_credits = this.credits_required * 1.5;
				ply.setM_Credit(add_credits);
				ply.setM_bitlets((this.bitlets_required * 1.5));
				message = "thirdwinner:" + ply.getM_Name();
				send_user(message);
			} else {
				ply.setM_Credit(0);
				ply.setM_bitlets(ply.getM_bitlets() - this.bitlets_required);
				message = "winners:" + "done";
				send_user(message);
			}
			System.out.println(ply.getM_Credit() + "," + ply.getM_bitlets());
			i++;
		}

		SaveTournament_results results = new SaveTournament_results(players, tournament_id, tournament_credits);
		results.save_results();
		results.save_recent_winners();
		for (Player ply : players) {
			message = "result" + ":";
			System.out.println("in player creddit");
			message += ply.getM_Name() + ",";
			message += ply.getM_Credit() + ",";
			message += ply.getM_bitlets() + ",";
			message += ply.getM_player_level();
			send_user(message);
		}

		for (Player player : players) {
			message = "connected_players" + ":";
			System.out.println("in player creddit");
			message += player.getM_Name() + ",";
			message += player.getM_Credit() + ",";
			message += player.getM_bitlets() + ",";
			message += player.getM_player_level();
			send_user(message);
		}

		message = "end" + ":";
		send_user(message);
		Sort_player_level_and_top_shots sort = new Sort_player_level_and_top_shots();
		sort.load_players();
		// Top50_players top50 = new Top50_players(this.players);
		// top50.Sort_top50();
		// Thread.sleep(8000);
		// save_tournament_results(tournament_id,player);

		send_user("close");
	}

	@Override
	protected void tournament_loading_time() {
		int check = 1;
		try {
			while (check <= 11) {
				Thread.sleep(1000);
				if (check == 1)
					this.add_bot();
				if (check == 3)
					this.add_bot();
				if (check == 5)
					this.add_bot();
				if (check >= 6)
					this.add_bot();
				check++;
			}
			this.serverState.Game_command(this.tournament_name, this, "start");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
