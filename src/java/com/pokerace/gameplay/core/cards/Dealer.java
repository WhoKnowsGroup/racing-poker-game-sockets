/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.core.cards;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import CouchdbOperations.Get_details;

import com.google.gson.JsonObject;
import com.pokerace.gameplay.core.Game;
import com.pokerace.gameplay.core.GameEventListener;
import com.pokerace.gameplay.core.PokerException;
import com.pokerace.gameplay.core.Tournament;
import com.pokerace.gameplay.core.player.Player;
import com.pokerace.gameplay.core.player.PlayerManager;
import com.sockets.players.User;

/**
 *
 * @author lokesh
 */
public class Dealer {
	private final Game game;
	private final List<Player> players;
	private final int total_games = 5;
	private int no_of_games;
	private int deal_number;
	private int game_number;
	private int total_deals;
	private final PlayerManager playerManager;
	private boolean handWon;
	private boolean deal;
	private boolean stop;
	private final boolean exit;
	private boolean bet;
	private boolean ready;
	private String tournament_id;

	public Player getPlayer(String id) {
		for (Player player : this.players) {
			if (player.getID().equals(id))
				return player;
		}
		return this.players.get(0);
	}

	public Game getGame() {
		return game;
	}

	private boolean discard;

	public Dealer() {
		this.game = new Game();
		this.playerManager = new PlayerManager();
		this.players = new ArrayList<>();
		this.handWon = false;
		this.deal = true;
		this.ready = true;
		this.exit = false;
		this.discard = false;
		this.game_number = 1;
	}

	//FIXME:  does anything call this??? [doesn't look like it]
	/*public void start_game() {
		Hand hand;
		HandCollection handCollection = new HandCollection();
		Random random = new Random();
		System.out.println("=====================================");
		System.out.println("STARTING NEW TOURNAMENT");
		System.out.println("=====================================");
		System.out.println("");
		// game = new Game(); 

		game.StartTournament(); 
		Card card = new Card();

		game.addGameEventListener(new GameEventListener() {

			@Override
			public void gameEvent() {
				System.out.println("A game event occured");
			}

			@Override
			public void betPlacedEvent(int betID, int handNumber, double betAmount, Player placedBy) {
				System.out.println("Bet Placed: ID=" + betID + " hand=" + handNumber + " amount=" + betAmount + " player=" + placedBy.getM_Name());
			}

			@Override
			public void betDeletedEvent(int betID) {
				System.out.println("Bet Deleted: ID=" + betID);
			}

			@Override
			public void playerEvent(String playerID) {
				System.out.println("Player Event: playerID=" + playerID);
			}

			@Override
			public void gameReset() {
				System.out.println("Game reset event");
			}

			@Override
			public void aHandHasWon() {
				handWon = true;
				System.out.println("A hand has won event");
				// gameCount++;
			}

			@Override
			public void dealNumberChanged() {
				System.out.println("Deal number changed event");
			}

			@Override
			public void deadLock() {
				System.out.println("Dead lock occured event");
			}

			@Override
			public void allPlayersHaveLockedBetsIn() {
				System.out.println("All players have locked bets in event");
			}

			@Override
			public void playerHasLockedBets(String playerID) {
				System.out.println("Player has locked bet in: playerID=" + playerID);
			}

			@Override
			public void playerUnlocked(String playerID) {
				System.out.println("Player unlocked event: playerID=" + playerID);
			}
		});

		try {

			// Thread.sleep(20000);
			String message1 = "total_games:" + this.total_games;
			send_user(message1);

		}
		catch (Exception e) {
			System.out.println(e);
		}
		while (!this.exit && this.deal && this.game_number <= this.total_games) {
			while (this.ready && this.game_number <= this.total_games) {
				try {
					this.generate_tournament_id();
					Thread.sleep(1000);	//was: 10000
					String message = "start" + ":" + tournament_id;
					send_user(message);
					game.m_Deck = new CardCollection(52);
					game.winnerName = "";
					game.m_AmountWon = 0.0;

					// this.game_number = 0;

					// initialize the hands and hand collection
					for (int i = 0; i < 6; i++) {
						hand = new Hand(i + 1);
						handCollection.add(hand);
					}
					game.m_Hands = handCollection;
					// first deal

					System.out.println("=====================================");
					System.out.println("Starting Game " + game_number);
					

					game.Reset();
					handWon = false;
					this.deal = true;
					Date date = new Date();
					while ((!game.m_ThereIsAWinningHand) && (!this.exit))

					{
						// this.deal_number ++;
						// System.out.println(this.deal+"deal");
						// check_for_timer(date);
						while ((this.deal || check_timer_flag(date)) && this.game_number <= this.total_games) {
							try {
								game.deal();
								displayHandStates(game);
								if (game.m_DealNumber != 1)
									Thread.sleep(500);
								game.Discard();
								message = "discard:true";
								date = new Date();
								this.discard = true;
								send_user(message);
								System.out.println("DEAL NUMBER ------>> " + game.m_DealNumber);
								this.deal_number = game.m_DealNumber;
							}
							catch (PokerException ex) {
								Logger.getLogger(Dealer.class.getName()).log(Level.SEVERE, null, ex);
								System.out.println(ex);
							}
							displayHandStates(game);
							String message34 = "discard:false";
							this.discard = false;
							send_user(message34);
							// this.ready = true;
							System.out.println("WINNIG HAND NUMBER ------>> " + game.m_WinningHandNumber);
							System.out.println("WINNING AMOUNT ------>> " + game.m_AmountWon);
							System.out.println("WINNER NAME ------>> " + game.winnerName);
							this.deal = false;
							if (game.m_WinningHandNumber > 0) {
								message = "winning_hand_number" + ":" + game.m_WinningHandNumber + " ";
								send_user(message);
								message = "";
								message = "winning_hand_type" + ":" + game.m_WinnerTypeName;
								send_user(message);

								for (Player player : this.players) {
									message = "connected_players" + ":";
									System.out.println("in player creddit");
									message += player.getM_Name() + ",";
									message += player.getM_Credit() + " ";
									send_user(message);
								}
								this.ready = true;
								// this.deal = true;
								if (game.m_DealNumber == 1) {
									send_user("redeal");
								}
								else {
									this.game_number++;
								}
							}
							else {

								for (Player player : players) {
									message = "connected_players" + ":";
									System.out.println("in player creddit");
									message += player.getM_Name() + ",";
									message += player.getM_Credit() + "  ";
									send_user(message);
								}
								message = "timer_flag" + ":" + "true";
								send_user(message);
							}
						}
					}

				}
				catch (Exception ex) {
					System.out.println(ex);
					break;
				}
			}
		}
		String message = "end" + ":";
		send_user(message);
		// save_tournament_results(tournament_id,player);
		send_user("close");

	}*/

	public void send_user(String message) {
		for (Player player : this.players) {
			try {
				PrintWriter out = new PrintWriter(player.getSocket().getOutputStream(), true);
				out.println(message);
				if (out.checkError()) {
					out.println(message);
				}

			}
			catch (Exception e) {
				System.out.println("Unsuccessfull");
			}
		}
	}

	/*protected void displayHandStates(Game game) {
		String message = "";
		System.out.println("\nHand Positions");

		for (Hand hand : game.m_Hands) {
			message = "game_number" + ":" + game.m_GameNumber + " ";
			message += "deal_number" + ":" + game.m_DealNumber + " ";
			System.out.print(hand.getHandNumber() + ": ");
			message += "hand_number" + ":" + hand.getHandNumber() + " ";
			message += "hand_status" + ":" + hand.getLocked() + " ";
			send_user(message);
			System.out.println(game.m_GameNumber);
			System.out.println("message" + message);
			if (!this.discard && (game.m_DealNumber != 1)) {
				for (Card card : hand.getCards()) {
					System.out.print(card.getShortName() + " ");
					message = "card" + ":" + card.getShortName() + " ";

					try {
						Thread.sleep(Tournament.CARD_INTERVAL);		//was:  200
						send_user(message);
					}
					catch (Exception e) {
						System.out.println(e);
					}

				}
			}
			else {
				message = "";
				for (Card card : hand.getCards()) {
					System.out.print(card.getShortName() + " ");
					message += "card" + ":" + card.getShortName() + " ";
				}
				// send_user(message);
				try {
					Thread.sleep(Tournament.HAND_INTERVAL);
					send_user(message);
				}
				catch (Exception e) {
					System.out.println(e);
				}
			}
			hand.setTotalRating(game.m_TotalRating);
			System.out.print("\t\tRATING ----->> " + hand.getRating() + "\t\t ODDS ----->> " + hand.getOdds() + "\t\t FORMATTED ODDS ----->> " + hand.getFormattedOdds());
			System.out.println("");
			message = "odds" + ":" + hand.getUnFormatedOdds() + " ";
			send_user(message);
			// message += "winning_hand_number"+":"+game.+" ";
			try {
				message = "hand_type" + ":" + hand.getHandTypeName() + " ";
				send_user(message);
			}
			catch (Exception e) {
				System.out.println(e);
			}
			message = "";
		}
		System.out.println("");
	}*/

	protected boolean check_timer_flag(Date initial) {

		long initial_seconds = initial.getTime();
		long initial_hour = (initial_seconds / 1000 / 60 / 60) % 24;
		long initial_min = (initial_seconds / 1000 / 60) % 60;
		long initial_seconds1 = (initial_seconds / 1000) % 60;
		Date after = new Date();
		long time_after = after.getTime();

		long after_hour = (time_after / 1000 / 60 / 60) % 24;
		long after_min = (time_after / 1000 / 60) % 60;
		long after_seconds1 = (time_after / 1000) % 60;
		long after_diff = 0;

		if (initial_hour == after_hour) {

			if (after_seconds1 >= initial_seconds1)
				after_diff = after_seconds1 - initial_seconds1;
			else {
				after_seconds1 = after_seconds1 + 60;
				after_diff = after_seconds1 - initial_seconds1;
			}

		}
		else {
			if (after_seconds1 >= initial_seconds1)
				after_diff = after_seconds1 - initial_seconds1;
			else {
				after_seconds1 = after_seconds1 + 60;
				after_diff = after_seconds1 - initial_seconds1;
			}
		}

		if (after_diff >= 48) {
			// System.out.println(after_diff);
			return true;
		}

		return false;
	}

	public void addPlayer(String id, User user) {
		Get_details get_details = new Get_details();
		JsonObject user_data = get_details.read_db("pokerace_users", id);
		// System.out.println(id);
		String Nickname = user_data.get("Nickname").toString().replace("\"", "");
		// System.out.println(Nickname);
		String no_of_tournaments = user_data.get("no_of_tournaments").toString().replace("\"", "");
		// System.out.println(no_of_tournaments);
		String player_level = user_data.get("playerlevel").toString().replace("\"", "");

		System.out.print(player_level);
		// System.out.println(player_level);
		Player player = new Player(this.playerManager);
		player.setID(id);
		player.setM_Name(Nickname);
		player.setM_player_level(Double.parseDouble(player_level));
		player.setSocket(user.getSocket());
		player.setNo_of_tournaments(Double.parseDouble(no_of_tournaments));
		player.setM_Credit(1000);
		this.players.add(player);
		String message = "connected_players" + ":";
		System.out.println("in player creddit");
		message += player.getM_Name() + ",";
		message += player.getM_Credit() + " ";
		send_user(message);
	}

	public void generate_tournament_id() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		month = month + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		// String date_toString = date.toString();
		this.tournament_id = "" + month + day + hour + minute + second;
	}
}
