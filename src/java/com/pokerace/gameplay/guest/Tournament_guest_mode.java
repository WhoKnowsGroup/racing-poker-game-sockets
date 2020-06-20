/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.guest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.gson.JsonObject;
import com.pokerace.gameplay.core.PokerException;
import com.pokerace.gameplay.core.Tournament;
import com.pokerace.gameplay.core.TournamentMode;
import com.pokerace.gameplay.core.cards.Hand;
import com.pokerace.gameplay.core.player.Player;
import com.sockets.players.User;

/**
 *
 * @author lokesh
 */
public class Tournament_guest_mode extends Tournament {
	private User guest_user;
	private int smart_bot_index = -1;
	private int stupid_bot_index = -1;
	private Map<String, Set<Integer>> betHistory = new HashMap<>();

	public Tournament_guest_mode(int total_games, String tournament_id, String tournament_name, int maxPlayers, int minPlayers, Double credits) {
		super(TournamentMode.GUEST, total_games, tournament_id, tournament_name, maxPlayers, minPlayers, credits);
	}

	@Override
	protected void tournament_loading_time() {
		int check = 1;
		try {
			while (check <= 8) {
				Thread.sleep(500);		//was:  1000
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
			// this.serverState.Game_commands(this.tournament_name, this,"start");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	protected void bettime_bots(Date date) throws InterruptedException, PokerException {
		System.out.println("Bettime");
		
		Random rand = new Random();
		int bots_length = this.players.size();
		
		//get hands and sort by odds
		List<Hand> hands = new ArrayList<>(this.game.getHands());
		hands.sort(new Comparator<Hand>() {
			@Override
			public int compare(Hand left, Hand right) {
				if (left.getUnFormatedOdds() == right.getUnFormatedOdds()) {
					return 0;
				}
				
				return left.getUnFormatedOdds() < right.getUnFormatedOdds() ? -1 : 1;
			}
		});
		
		while (!check_timer_flag(date)) {
			try {
				Thread.sleep(2000);
				
				int randnum = rand.nextInt(bots_length);
				int gamesRemaining = total_games - this.game_number + 1;
				
				String betKey = this.game_number + ":" + this.deal_number;
				if (! betHistory.containsKey(betKey)) {
					betHistory.put(betKey, new HashSet<Integer>());
				}
				if (betHistory.get(betKey).contains(randnum)) {
					//this bot has already bet on this round; don't keep betting!
					continue;
				}
				
				Player player = this.players.get(randnum);		//XXX:  it's generally safe to assume that the guest player is first in the list (for guest-mode games)
				if (player.getID().equalsIgnoreCase(this.guest_user.getId())) {
					//didn't pick a bot; do nothing
				} 
				else {
					//allocate 'smart' and 'stupid' bots
					if (smart_bot_index == -1) {
						smart_bot_index = randnum;
					}
					else if (stupid_bot_index == -1) {
						stupid_bot_index = randnum;
					}
					
					//find our opponent
					Player opponent = null;
					for (Player opp : this.players) {
						if (opp.getID().equalsIgnoreCase(this.guest_user.getId())) {
							opponent = opp;
							break;
						}
					}
					
					//determin max credits pool available for betting
					int creditsPool = (int)Math.max(opponent.getM_Credit(), this.tournament_credits);
					creditsPool = (int)Math.min(creditsPool, player.getM_Credit());
					
					int amount = 0;
					int minBet = 0;
					int handnum = rand.nextInt(5) + 1;			//'non-smart' bots just pick a hand at random
					if (randnum % 2 == smart_bot_index % 2) {
						//smarter bots pick the one of the three hands that are most likely to win
						handnum = hands.get(rand.nextInt(3)).getHandNumber();
						minBet = (int)Math.floor(creditsPool / Math.max(gamesRemaining, 2)) / 8;
						
						
						if (randnum == smart_bot_index) {
							//the smartest bot only bets when the odds are significantly in favor of one particular hand
							Hand bestHand = hands.get(0);
							Hand secondBest = hands.get(1);
							if (bestHand.getUnFormatedOdds() > 3.5 || secondBest.getUnFormatedOdds() - bestHand.getUnFormatedOdds() < 1.0) {
								continue;
							}
							
							handnum = bestHand.getHandNumber();
							minBet = (int)Math.floor(creditsPool / Math.max(gamesRemaining, 4));
							
						}
						else {
							//smarter bots bet every other round only (starting from second round)
							if (this.deal_number % 2 != 0) {
								continue;
							}
						}
					}
					else if (randnum == stupid_bot_index) {
						//the stupid bot always places a large wager on the hand with the worst odds
						Hand worstHand = hands.get(hands.size() - 1);
						if (worstHand.getUnFormatedOdds() < 6) {
							continue;
						}
						minBet = (int)Math.floor(creditsPool / 2);
						handnum = worstHand.getHandNumber();
					}
					else {
						//'non-smart' bots bet every other round only (starting from first round)
						if (this.deal_number % 2 != 1) {
							continue;
						}
					}
					
					
					if (tournament_credits == 100) {
						//special case, for games that start with 100 credits, bet up to half the current credit amount
						amount = rand.nextInt((int) ((player.getM_Credit()) / 2));
					}
					else {
						//for all other games, bet up to 10% of the current credit amount
						//amount = rand.nextInt((int) ((player.getM_Credit()) / 10));
					
						//for all other games, bet up to '1 / (total_games / 2)' of the current credit amount
						int divisor = (int)Math.max(gamesRemaining, 2);//(int) Math.ceil(total_games / 2.0);
						if (minBet >= creditsPool / divisor) {
							minBet = (int)Math.floor(creditsPool / divisor);
						}
						
						amount = rand.nextInt((int) (creditsPool / divisor)) - minBet;
						amount =  minBet + ( amount > 0 ? amount : 0 );
					}
					
					
					//always bet in increments of 10
					if (amount % 10 != 0) {
						int rem = amount % 10;
						amount = amount - rem;
					}
					
					//place the bet
					this.game.PlaceBet(handnum, amount, player);
					
					//send the user's updated number of credits
					String message = "connected_players" + ":";
					System.out.println("in player creddit");
					message += player.getM_Name() + ",";
					message += player.getM_Credit() + " ";
					send_user(message);
					
					//see if we should also fire a chat message
					int div_rand = rand.nextInt(10) + 1;
					int div_rand1 = rand.nextInt(10) + 1;
					if (div_rand <= 2 && div_rand1 <= 2) {
						chat();
					}
					
					betHistory.get(betKey).add(randnum);

				}
			} catch (Exception e) {
				System.out.println(e);
			}

		}
	}

	@Override
	public void addPlayers(User user, JsonObject user_details) {
		this.guest_user = new User(user);
		String id = user.getId();
		// System.out.println(id);
		String Nickname = user.getId();
		// System.out.println(Nickname);
		String no_of_tournaments = "0";
		// System.out.println(no_of_tournaments);
		String player_level = "1";

		super.addPlayers(id, Nickname, no_of_tournaments, player_level, user.getSocket());
	}
}
