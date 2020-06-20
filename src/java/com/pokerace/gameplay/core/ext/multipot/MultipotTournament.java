package com.pokerace.gameplay.core.ext.multipot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.lightcouch.CouchDbClient;

import au.com.suncoastpc.util.CouchDBUtil;
import au.com.suncoastpc.util.types.CouchDatabase;

import com.google.gson.JsonObject;
import com.pokerace.gameplay.core.ChatBot;
import com.pokerace.gameplay.core.PokerException;
import com.pokerace.gameplay.core.Tournament;
import com.pokerace.gameplay.core.TournamentMode;
import com.pokerace.gameplay.core.cards.Hand;
import com.pokerace.gameplay.core.player.Player;
import com.sockets.players.User;
import com.sockets.start.Server_State;

public abstract class MultipotTournament extends Tournament {
	protected int bitlets_required;
	protected Double credits_required;
	protected Server_State serverState;
	
	private int smart_bot_index = -1;
	private int stupid_bot_index = -1;
	private Map<String, Set<Integer>> betHistory = new HashMap<>();

	protected List<Bots> bots;

	public MultipotTournament(TournamentMode mode, int total_games, String tournament_id, String tournament_name, int maxPlayers, int minPlayers, Double credits, Server_State serverState, int bitlets_required) {
		super(mode, total_games, tournament_id, tournament_name, maxPlayers, minPlayers, credits);

		this.game = new MultipotGame();
		this.bitlets_required = bitlets_required;
		this.serverState = serverState;

		this.playerCount = 0;
		this.credits_required = 0.0;
		this.bots = new ArrayList<>();
	}
	
	@Override
	public boolean isFastForward() {
		return false;	//XXX:  cannot fast-forward in a multiplayer tournament
	}

	@Override
	public String add_bot() {
		if (this.playerCount >= maxPlayers) {
			return null;
		}
		
		String nickname = super.add_bot();
		Bots bot = new Bots(playerCount, nickname);
		this.bots.add(bot);
		
		return nickname;
	}

	@Override
	protected void chat() throws InterruptedException {
		Random rand = new Random();
		int bots_length = this.bots.size();
		int randnum = rand.nextInt(bots_length);
		ChatBot chat = new ChatBot();
		String chat_message = chat.chat_message();
		String message = "Chat:" + chat_message + "|" + "User" + "-" + this.players.get(this.bots.get(randnum).id).getM_Name();
		Thread.sleep(1000);
		send_user(message);
	}

	public void receive_chat(String message) {
		System.out.println(message);
		send_user(message);
	}
	
	@Override
	protected void bettime_bots(Date date) throws InterruptedException, PokerException {
		System.out.println("Bettime");
		
		Random rand = new Random();
		int bots_length = this.bots.size();
		
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
		
		//identify the bots
		Set<Integer> botIds = new HashSet<>();
		for (Bots bot : this.bots) {
			botIds.add(bot.id);
		}
		
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
				
				//allocate 'smart' and 'stupid' bots
				if (smart_bot_index == -1) {
					smart_bot_index = randnum;
				}
				else if (stupid_bot_index == -1) {
					stupid_bot_index = randnum;
				}
				
				//find our strongest opponent
				Player opponent = null;
				for (int index = 0; index < this.players.size(); index++) {
					if (botIds.contains(index)) {
						//not a human opponent
						continue;
					}
					Player opp = this.players.get(index);
					if (opponent == null || opp.getM_Credit() > opponent.getM_Credit()) {
						opponent = opp;
					}
				}
				
				//get the player that's acting
				Bots bot = this.bots.get(randnum);
				Player player = this.players.get(bot.id);
				
				//determine max credits pool available for betting
				int creditsPool = (int)Math.max(opponent.getM_Credit(), this.tournament_credits);
				creditsPool = (int)Math.min(creditsPool, player.getM_Credit());
				
				
				int amount = 0;
				int minBet = 0;
				int handnum = rand.nextInt(5) + 1;
				
				//apply betting strategies
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
					int divisor = (int)Math.max(gamesRemaining, this.mode == TournamentMode.MULTI ? 4 : 2);//(int) Math.ceil(total_games / 2.0);
					if (minBet >= creditsPool / divisor) {
						minBet = (int)Math.floor(creditsPool / divisor);
					}
					
					amount = rand.nextInt((int) (creditsPool / divisor)) - minBet;
					amount =  minBet + ( amount > 0 ? amount : 0 );
				}
				
				//bet in increments of 10
				if (amount % 10 != 0) {
					int rem = amount % 10;
					amount = amount - rem;
				}
				
				this.game.PlaceBet(handnum, amount, this.players.get(bot.id));
				
				String message = "connected_players" + ":";
				message += player.getM_Name() + ",";
				message += player.getM_Credit() + " ";
				send_user(message);
				
				int div_rand = rand.nextInt(10) + 1;
				int div_rand1 = rand.nextInt(10) + 1;
				if (div_rand <= 2 && div_rand1 <= 2)
					chat();
				
				betHistory.get(betKey).add(randnum);

			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	@Override
	public void addPlayers(User user, JsonObject user_details) {
		//XXX:  audit okay; lookup/update document by id
		CouchDbClient db = CouchDBUtil.getClient(CouchDatabase.USERS); // new CouchDbClient(dbProperties);
		com.pokerace.ejb.model.User detailsUser = new com.pokerace.ejb.model.User(user_details);
		
		String id = detailsUser.getEmail();
		// System.out.println(id);
		String Nickname = detailsUser.getNickname();
		// System.out.println(Nickname);
		String no_of_tournaments = detailsUser.getNumTournaments() + "";
		// System.out.println(no_of_tournaments);
		String player_level = detailsUser.getPlayerLevel() + "";

		long player_credits = detailsUser.getCredits();
		player_credits -= this.tournament_credits;
		detailsUser.setCredits(player_credits);
		
		user_details.addProperty("Credit", player_credits);
		db.update(detailsUser.toGson());

		// user_details.addProperty("credits", discard);
		// System.out.println(player_level);
		Player player = new Player(playerManager);
		player.setID(id);
		player.setM_Name(Nickname);
		player.setM_player_level(Double.parseDouble(player_level));
		player.setSocket(user.getSocket());
		player.setNo_of_tournaments(Double.parseDouble(no_of_tournaments));
		player.setM_Credit(this.tournament_credits);
		if (this.mode == TournamentMode.POT) {
			player.setM_bitlets(0);
		}
		this.players.add(player);
		this.playerCount++;
		for (Player player1 : this.players) {
			String message = "connected_players" + ":";
			message += player1.getM_Name() + ",";
			message += player1.getM_Credit() + " ";
			send_user(message);
		}
		// aroth: this step should be unnecessary (and is probably even wrong)
		// CouchDBUtil.shutdownClient(CouchDatabase.USERS);
		// db.shutdown();
	}
}
