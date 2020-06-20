package com.pokerace.gameplay.core;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import sun.rmi.runtime.Log;
import CouchdbOperations.Search_for_field;

import com.google.gson.JsonObject;
import com.pokerace.gameplay.core.cards.Card;
import com.pokerace.gameplay.core.cards.CardCollection;
import com.pokerace.gameplay.core.cards.Hand;
import com.pokerace.gameplay.core.cards.HandCollection;
import com.pokerace.gameplay.core.player.Player;
import com.pokerace.gameplay.core.player.PlayerManager;
import com.sockets.players.User;

//FIXME:  technical debt - fix the database queries that failed audit (and then ensure that the neutral ones should be okay)
public abstract class Tournament {
	public static final long HAND_INTERVAL = 100;
	public static final long CARD_INTERVAL = 16;		//was:  350
	
	protected static final int MIN_DEALS_PER_GAME = 3;  //XXX:  note that the count goes 1, 1, 2, 3!!!
	
	protected final int total_games;
	protected final String tournament_id;
	protected final String tournament_name;
	protected final int maxPlayers;
	protected final int minPlayers;
	protected final PlayerManager playerManager;
	protected final Double tournament_credits;

	protected int game_number;
	protected int deal_number;
	protected boolean timerFlag;
	protected String gameStatus;
	protected List<Player> players;
	protected boolean deal;
	protected boolean exit;
	protected boolean discard;
	protected int playerCount = 0;
	protected Game game;
	protected TournamentMode mode;
	protected boolean fastForward = false;

	@SuppressWarnings("unused")
	private Tournament() {
		this(TournamentMode.GUEST, 0, null, null, 0, 0, 0.0);
	}

	public Tournament(TournamentMode mode, int total_games, String tournament_id, String tournament_name, int maxPlayers, int minPlayers, Double credits) {
		this.total_games = total_games;
		this.game = new Game();
		this.tournament_id = tournament_id;
		this.tournament_name = tournament_name;
		this.maxPlayers = maxPlayers;
		this.minPlayers = minPlayers;
		this.game_number = 1;
		this.deal_number = 1;
		this.timerFlag = true;
		this.gameStatus = "loading";
		this.playerManager = new PlayerManager();
		this.tournament_credits = credits;
		this.players = new ArrayList<>();
		this.deal = true;
		this.exit = false;
		this.discard = false;
		this.mode = mode;
	}

	protected abstract void tournament_loading_time();
	public abstract void addPlayers(User user, JsonObject user_details);

	protected void bettime_bots(Date date) throws InterruptedException, PokerException {
	}
	
	public String add_bot() {
		//FIXME:  this is basically assuming that there are (at least) 200 bots and just picking a random one
		Random rand = new Random();
		//String randnum = Integer.toString(rand.nextInt(200));
		Search_for_field search_details = new Search_for_field();
		JsonObject bot_details = search_details.get_details("kentuckypoker_bots", rand.nextInt(200));
		String id = bot_details.get("_id").toString().replace("\"", "");
		// System.out.println(id);
		String Nickname = bot_details.get("Nickname").toString().replace("\"", "");
		// System.out.println(Nickname);
		String no_of_tournaments = bot_details.get("no_of_tournaments").toString().replace("\"", "");
		// System.out.println(no_of_tournaments);
		String player_level = bot_details.get("playerlevel").toString().replace("\"", "");
		// System.out.println(player_level);
		Player player = new Player(playerManager);
		player.setID(id);
		player.setM_Name(Nickname);
		player.setM_player_level(Double.parseDouble(player_level));
		// player.setSocket(user.getSocket());
		player.setNo_of_tournaments(Double.parseDouble(no_of_tournaments));
		player.setM_Credit(this.tournament_credits);
		if (this.playerCount <= maxPlayers)
			this.players.add(player);
		playerCount++;
		String message = "connected_players" + ":";
		message += player.getM_Name() + ",";
		message += player.getM_Credit() + " ";
		send_user(message);
		
		return Nickname;
	}
	
	protected void chat() throws InterruptedException {
		Random rand = new Random();
		int bots_length = this.players.size();
		int randnum = rand.nextInt(bots_length);
		randnum += 1;
		ChatBot chat = new ChatBot();
		String chat_message = chat.chat_message();
		String message = "Chat:" + chat_message + "|" + "User" + "-" + this.players.get(randnum).getM_Name();
		Thread.sleep(1000);
		send_user(message);
	}

	protected boolean check_timer_flag(Date initial) {
		// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

		} else {
			if (after_seconds1 >= initial_seconds1)
				after_diff = after_seconds1 - initial_seconds1;
			else {
				after_seconds1 = after_seconds1 + 60;
				after_diff = after_seconds1 - initial_seconds1;
			}
		}

		if (this.mode == TournamentMode.GUEST) {
			if (this.isFastForward()) {
				return true;
			}
			if (after_diff > 33 && (this.game.m_DealNumber == 1)) {
				return true;
			}
			if (after_diff > 33 && (this.game.m_DealNumber == 2)) {
				return true;
			}
			if (after_diff > 33 && (this.game.m_DealNumber >= 2)) {
				return true;
			}
		}
		else if (this.mode == TournamentMode.SINGLE) {
			if (this.isFastForward()) {
				return true;
			}
			if (after_diff > 33) {
				return true;
			}
		}
		else if (this.mode == TournamentMode.POT || this.mode == TournamentMode.MULTI) {
			if (after_diff > 33 && (this.game.m_DealNumber == 1)) {
				return true;
			}
			if (after_diff > 33 && (this.game.m_DealNumber == 2)) {
				return true;
			}
			if (after_diff > 33 && (this.game.m_DealNumber >= 3)) {
				return true;
			}
		}

		return false;
	}
	
	public void start_tournament() throws InterruptedException {
		Hand hand;
		HandCollection handCollection = new HandCollection();
		Random random = new Random();
		game.StartTournament(); /* changes */
		Card card = new Card();
		String message1 = "total_games:" + this.total_games;
		send_user(message1);
		// tournament_loading_time();
		String message = "";
		if (playerCount < minPlayers && this.mode == TournamentMode.GUEST) {
			tournament_loading_time();
		}
		while (this.game_number <= this.total_games && !this.exit) {
			try {
				if (this.mode == TournamentMode.SINGLE) {
					if (game.m_GameNumber != 1 || this.game.m_ThereIsAWinningHand) {
						Thread.sleep(10000);
					}
					tournament_loading_time();
				}
				else if (this.mode == TournamentMode.GUEST) {
					if (game.m_GameNumber != 1 || this.game.m_ThereIsAWinningHand) {
						Thread.sleep(10000);
					}
				}
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
				this.deal_number = 1;
				Date date = new Date();
				this.timerFlag = true;

				while (!this.game.m_ThereIsAWinningHand && !this.exit) {
					while ((this.timerFlag || this.isFastForward() || check_timer_flag(date)) && !this.exit) {
						System.out.println("!!!!!!!!!!  STARTING DEAL LOOP:  num=" + this.deal_number + ", fastforward=" + this.isFastForward());
						
						this.timerFlag = false;
						if (this.mode == TournamentMode.GUEST) {
							this.discard = false;
							message = "deal:start";
							if (! this.isFastForward()) {
								send_user(message);
								Thread.sleep(1000);
							}
						}
						this.gameStatus = "dealing";
						this.game.deal(this.isFastForward());
						
						//FIXME:  testing, remove
						//FIXME:  need to adjust/normalize timings in potpoker and multi Tournament subclasses!!!
						//if (this.deal_number >= 2) {
						//	System.out.println("!!!!!!!!!!  FAST FORWARD:  deal=" + this.deal_number);
						//	this.setFastForward(true);
						//}
						
						if (! this.isFastForward()) {
							displayHandStates(this.game);
							Thread.sleep(this.deal_number <= 1 ? 2500 : 2000);		//was:  2500
						}
						
						this.game.Discard();
						if (this.isFastForward() && game.m_WinningHandNumber > 0) {
							displayHandStates(this.game);
							Thread.sleep(2000);
							
							this.setFastForward(false);  //don't need to fast-forward anymore, we've reached the end of the game
						}
						
						message = "discard:true";
						this.discard = true;
						date = new Date();
						
						if (! this.isFastForward()) {
							send_user(message);
						}
						
						this.gameStatus = "bettime";
						System.out.println("DEAL NUMBER ------>> " + game.m_DealNumber);
						this.deal_number = this.game.m_DealNumber;
						
						if (! this.isFastForward()) {
							displayHandStates(game);
						}
						String message34 = "discard:false";
						this.discard = false;
						
						if (! this.isFastForward()) {
							send_user(message34);
						}
						System.out.println("WINNIG HAND NUMBER ------>> " + game.m_WinningHandNumber);
						System.out.println("WINNING AMOUNT ------>> " + game.m_AmountWon);
						System.out.println("WINNER NAME ------>> " + game.winnerName);
						this.deal = false;
						if (game.m_WinningHandNumber > 0) {
							this.deal = true;
							message = "winning_hand_number" + ":" + game.m_WinningHandNumber + " ";
							send_user(message);
							message = "";
							message = "winning_hand_type" + ":" + game.m_WinnerTypeName;
							send_user(message);
							for (Player player : players) {
								message = "connected_players" + ":";
								System.out.println("in player creddit");
								message += player.getM_Name() + ",";
								message += (int) player.getM_Credit() + " ";
								send_user(message);
							}

							if (game.m_DealNumber == 1) {
								send_user("redeal");
							} else {
								this.game_number++;
							}
						} else if (! this.isFastForward()) {
							for (Player player : players) {
								message = "connected_players" + ":";
								System.out.println("in player creddit");
								message += player.getM_Name() + ",";
								message += (int) player.getM_Credit() + " ";
								send_user(message);
							}
							message = "timer_flag" + ":" + "true";
							send_user(message);
							
							if (this.mode == TournamentMode.GUEST) {
								bettime_bots(date);
							}
						} else {
							//XXX:  is there anything that needs to be sent in fast-forward mode?
						}
					}
				}
			} catch (Exception e) {
				Log.getLog("InvalidPokerException", "Tournament", Thread.MIN_PRIORITY);
			}

		}
		message = "end" + ":";
		send_user(message);
		if (this.mode == TournamentMode.SINGLE) {
			Saveresults results = new Saveresults(players,tournament_id,tournament_credits);
			results.save_results();
			// save_tournament_results(tournament_id,player);
		}
		send_user("close");
	}

	protected void displayHandStates(Game game) {
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
			
			//if (this.mode == TournamentMode.GUEST) {
				if ((game.m_DealNumber == 1) || (this.discard)) {
					message = "";
					for (Card card : hand.getCards()) {
						System.out.print(card.getShortName() + " " + "F");
						message += "card" + ":" + card.getShortName() + " ";
					}
					// send_user(message);
					try {
						Thread.sleep(HAND_INTERVAL);
						send_user(message);
					} catch (Exception e) {
						System.out.println(e);
					}
				} else {
					for (Card card : hand.getCards()) {
						System.out.print(card.getShortName() + " " + "T");
						message = "card" + ":" + card.getShortName() + " ";

						try {

							Thread.sleep(CARD_INTERVAL);
							send_user(message);
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
			//}
			/*else {
				if ((game.m_DealNumber != 1) && (!this.discard)) {
					for (Card card : hand.getCards()) {
						System.out.print(card.getShortName() + " ");
						message = "card" + ":" + card.getShortName() + " ";
	
						try {
							Thread.sleep(300);
							send_user(message);
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				} else {
					message = "";
					for (Card card : hand.getCards()) {
						System.out.print(card.getShortName() + " ");
						message += "card" + ":" + card.getShortName() + " ";
					}
					// send_user(message);
					try {
						Thread.sleep(100);
						send_user(message);
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}*/
			
			hand.setTotalRating(game.m_TotalRating);
			System.out.print("\t\tRATING ----->> " + hand.getRating() + "\t\t ODDS ----->> " + hand.getOdds() + "\t\t FORMATTED ODDS ----->> " + hand.getFormattedOdds());
			System.out.println("");
			message = "odds" + ":" + hand.getUnFormatedOdds() + " ";
			send_user(message);
			// message += "winning_hand_number"+":"+game.+" ";
			try {
				message = "hand_type" + ":" + hand.getHandTypeName() + " ";
				send_user(message);
			} catch (Exception e) {
				System.out.println(e);
			}
			message = "";
		}
		System.out.println("");
	}

	public final void addPlayers(String id, String Nickname, String no_of_tournaments, String player_level, Socket socket) {
		Player player = new Player(playerManager);
		player.setID(id);
		player.setM_Name(Nickname);
		player.setM_player_level(Double.parseDouble(player_level));
		player.setSocket(socket);
		player.setNo_of_tournaments(Double.parseDouble(no_of_tournaments));
		player.setM_Credit(this.tournament_credits);
		
		this.players.add(player);
		
		String message = "connected_players" + ":";
		message += player.getM_Name() + ",";
		message += (int) player.getM_Credit() + " ";
		send_user(message);
	}

	public final void deal() {
		System.out.println("Deal true Tournament");
		this.timerFlag = true;
	}

	public final void exit() {
		this.exit = true;
	}
	
	public boolean isFastForward() {
		return fastForward;
	}
	
	public void setFastForward(boolean fastForward) {
		if (! fastForward || this.deal_number >= MIN_DEALS_PER_GAME) {
			System.out.println("!!!!!!!!!!  SETTING FAST FORWARD TO:  " + fastForward);
			this.fastForward = fastForward;
		}
	}

	protected final void send_user(String message) {
		for (Player player : players) {
			try {
				PrintWriter out = new PrintWriter(player.getSocket().getOutputStream(), true);
				out.println(message);
			} catch (Exception e) {
				System.out.println("Unsuccessfull");
			}
		}
	}
	
	public final void place_bet(String[] msg, User user) {
		System.out.println("Place bet");
		int hand_number = 0;
		for (String bet : msg) {
			String[] msg_split = bet.split(":");
			if (msg_split[0].equals("hand_number")) {
				hand_number = Integer.parseInt(msg_split[1]);
			}
			if (msg_split[0].equals("bet_amount")) {
				if (this.gameStatus.equals("bettime")) {
					try {

						for (Player player : players) {
							if (player.getID().equals(user.getId())) {
								this.game.PlaceBet(hand_number, Double.parseDouble(msg_split[1]), player);
								String message = "connected_players" + ":";
								System.out.println("in player creddit");
								message += player.getM_Name() + ",";
								message += (int) player.getM_Credit() + ",";
								message += player.getM_bitlets() + ",";
	                            message += player.getM_player_level();
								send_user(message);
							}
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		}

	}
}
