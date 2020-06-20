/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.potpoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONObject;
import org.lightcouch.CouchDbClient;

import au.com.suncoastpc.util.CouchDBUtil;
import au.com.suncoastpc.util.types.CouchDatabase;

import com.google.gson.JsonObject;
import com.pokerace.ejb.model.User;
import com.pokerace.gameplay.core.player.Player;

/**
 *
 * @author lokesh
 */
public class SaveTournament_results {
	private List<Player> players;
	private String tournament_id;
	private Double starting_credit_points;

	SaveTournament_results(List<Player> players, String tournament_id, Double credits) {
		this.players = players;
		this.tournament_id = tournament_id;
		this.starting_credit_points = credits;
	}

	void save_results() {
		//XXX:  audit neutral; mainly looks up/updates documents by id, however may perform many such operations depending upon how large 'players' is
		CouchDbClient resultsDb = CouchDBUtil.getClient(CouchDatabase.TOURNAMENT_RESULTS); // new CouchDbClient(dbProperties4);
		CouchDbClient usersDb = CouchDBUtil.getClient(CouchDatabase.USERS); // new CouchDbClient(dbProperties5);
		CouchDbClient botsDb = CouchDBUtil.getClient(CouchDatabase.BOTS); // new CouchDbClient(dbProperties6);
		CouchDbClient winnersDb = CouchDBUtil.getClient(CouchDatabase.TOURNAMENT_WINNERS); // new CouchDbClient(dbProperties7);
		CouchDbClient historyDb = CouchDBUtil.getClient(CouchDatabase.CREDIT_HISTORY); // new CouchDbClient(dbProperties8);

		JSONObject obj = new JSONObject();
		obj.put("_id", tournament_id);
		JSONObject obj7 = new JSONObject();
		obj7.put("_id", tournament_id);
		String playername = "";
		String creditpoint = "";
		String bitletspoint = "";
		List<Player> rank = new ArrayList<>();
		Collections.sort(players);
		for (Player player : players) {
			System.out.println(player.getM_Credit());
		}
		double player_length = players.size();
		double player_rank = -1;
		double player_percentage = 1 / player_length;
		System.out.println(player_percentage);
		for (Player ply : players) {
			player_rank++;
			if (ply.getM_Name().length() > 1) {
				playername += ply.getM_Name() + ",";
				double con = new Double(ply.getM_Credit());
				int int_con = (int) con;
				creditpoint += int_con + ",";
				bitletspoint += (int) ply.getM_bitlets() + ",";
				// sort_player_ranking(ply);
				try {
					JsonObject obj3 = usersDb.find(JsonObject.class, ply.getID());
					User user = new User(obj3);
					
					String revision = obj3.get("_rev").toString().replace("\"", "");
					String no_of_tournaments = user.getNumTournaments() + "";
					String no_of_bitlets = user.getBitlets() + "";
					String no_of_bonuses = user.getNumBonuses() + "";
					String no_of_2shots = user.getNum2Shots() + "";
					String no_of_3shots = user.getNum3Shots() + "";
					String no_of_4shots = user.getNum4Shots() + "";
					String no_of_5shots = user.getNum5Shots() + "";
					String no_of_6shots = user.getNum6Shots() + "";
					String no_of_7shots = user.getNum7Shots() + "";
					String no_of_8shots = user.getNum8Shots() + "";
					String no_of_9shots = user.getNum9Shots() + "";
					String no_of_10shots = user.getNum10Shots() + "";
					String Credit = user.getCredits() + "";
					
					Double Credits = Double.parseDouble(Credit) + ply.getM_Credit();
					
					Double no_tournaments = Double.parseDouble(no_of_tournaments) + 1;
					Double no_bitlets = Double.parseDouble(no_of_bitlets) + ply.getM_bitlets();
					Double no_bonuses = Double.parseDouble(no_of_bonuses) + ply.getM_bonuses();
					Double no_2shots = Double.parseDouble(no_of_2shots) + ply.getM_shots(0);
					Double no_3shots = Double.parseDouble(no_of_3shots) + ply.getM_shots(1);
					Double no_4shots = Double.parseDouble(no_of_4shots) + ply.getM_shots(2);
					Double no_5shots = Double.parseDouble(no_of_5shots) + ply.getM_shots(3);
					Double no_6shots = Double.parseDouble(no_of_6shots) + ply.getM_shots(4);
					Double no_7shots = Double.parseDouble(no_of_7shots) + ply.getM_shots(5);
					Double no_8shots = Double.parseDouble(no_of_8shots) + ply.getM_shots(6);
					Double no_9shots = Double.parseDouble(no_of_9shots) + ply.getM_shots(7);
					Double no_10shots = Double.parseDouble(no_of_10shots) + ply.getM_shots(8);
					Double playerlevel = ply.getM_player_level() + (player_length - player_rank) * player_percentage;
					
					ply.setM_player_level(playerlevel);
					
					user.setCredits(Credits.longValue());
					user.setNumTournaments(no_tournaments.longValue());
					user.setBitlets(no_bitlets);
					user.setNumBonuses(no_bonuses.longValue());
					user.setNum2Shots(no_2shots.longValue());
					user.setNum3Shots(no_3shots.longValue());
					user.setNum4Shots(no_4shots.longValue());
					user.setNum5Shots(no_5shots.longValue());
					user.setNum6Shots(no_6shots.longValue());
					user.setNum7Shots(no_7shots.longValue());
					user.setNum8Shots(no_8shots.longValue());
					user.setNum9Shots(no_9shots.longValue());
					user.setNum10Shots(no_10shots.longValue());
					user.setPlayerLevel(playerlevel);
					
					obj3 = user.toGson();
					obj3.addProperty("_rev", revision);
					
					JsonObject obj11 = new JsonObject();

					try {

						obj11.addProperty("_id", ply.getID());
						obj11.addProperty("Tournament_id", tournament_id);
						obj11.addProperty("Starting_Credits", starting_credit_points.intValue());
						obj11.addProperty("Ending_Credits", (int) ply.getM_Credit());
						obj11.addProperty("Available_Credits", Credits);
						obj11.addProperty("Bitlets", ((int) ply.getM_bitlets() + ","));
						historyDb.save(obj11);
					} catch (Exception e) {
						obj11 = historyDb.find(JsonObject.class, ply.getID());
						String tour_id = obj11.get("Tournament_id").toString().replace("\"", "");
						tour_id += "," + tournament_id;
						String initial_credits = obj11.get("Starting_Credits").toString().replace("\"", "");
						initial_credits += "," + starting_credit_points.intValue();
						String end_credits = obj11.get("Ending_Credits").toString().replace("\"", "");
						end_credits += "," + (int) ply.getM_Credit();
						String available_credits = obj11.get("Available_Credits").toString().replace("\"", "");
						available_credits += "," + Credits;
						String available_bitlets = obj11.get("Bitlets").toString().replace("\"", "");
						available_bitlets += (int) ply.getM_bitlets() + ",";

						obj11.addProperty("Starting_Credits", initial_credits);
						obj11.addProperty("Ending_Credits", end_credits);
						obj11.addProperty("Available_Credits", available_credits);
						obj11.addProperty("Tournament_id", tour_id);
						obj11.addProperty("Bitlets", available_bitlets);
						historyDb.update(obj11);
					}

					usersDb.update(obj3);

				} catch (Exception e) {
					System.out.println(e);
					try {
						JsonObject obj4 = botsDb.find(JsonObject.class, ply.getID());
						String revision = obj4.get("_rev").toString().replace("\"", "");
						String Credit = obj4.get("Credit").toString().replace("\"", "");
						String no_of_tournaments = obj4.get("no_of_tournaments").toString().replace("\"", "");
						String no_of_bitlets = obj4.get("no_of_bitlets").toString().replace("\"", "");
						String no_of_bonuses = obj4.get("no_of_bonuses").toString().replace("\"", "");
						String no_of_2shots = obj4.get("no_of_2shots").toString().replace("\"", "");
						String no_of_3shots = obj4.get("no_of_3shots").toString().replace("\"", "");
						String no_of_4shots = obj4.get("no_of_4shots").toString().replace("\"", "");
						String no_of_5shots = obj4.get("no_of_5shots").toString().replace("\"", "");
						String no_of_6shots = obj4.get("no_of_6shots").toString().replace("\"", "");
						String no_of_7shots = obj4.get("no_of_7shots").toString().replace("\"", "");
						String no_of_8shots = obj4.get("no_of_8shots").toString().replace("\"", "");
						String level = obj4.get("playerlevel").toString().replace("\"", "");
						Double Credits = Double.parseDouble(Credit) + ply.getM_Credit();
						Double no_tournaments = Double.parseDouble(no_of_tournaments) + 1;
						Double no_bitlets = Double.parseDouble(no_of_bitlets) + ply.getM_bitlets();
						Double no_bonuses = Double.parseDouble(no_of_bonuses) + ply.getM_bonuses();
						Double no_2shots = Double.parseDouble(no_of_2shots) + ply.getM_shots(0);
						Double no_3shots = Double.parseDouble(no_of_3shots) + ply.getM_shots(1);
						Double no_4shots = Double.parseDouble(no_of_4shots) + ply.getM_shots(2);
						Double no_5shots = Double.parseDouble(no_of_5shots) + ply.getM_shots(3);
						Double no_6shots = Double.parseDouble(no_of_6shots) + ply.getM_shots(4);
						Double no_7shots = Double.parseDouble(no_of_7shots) + ply.getM_shots(5);
						Double no_8shots = Double.parseDouble(no_of_8shots) + ply.getM_shots(6);
						Double playerlevel = ply.getM_player_level() + (player_length - player_rank) * player_percentage;
						ply.setM_player_level(playerlevel);
						obj4.addProperty("Credit", Credits);
						obj4.addProperty("no_of_tournaments", no_tournaments);
						obj4.addProperty("no_of_bitlets", no_bitlets);
						obj4.addProperty("no_of_bonuses", no_bonuses);
						obj4.addProperty("no_of_2shots", no_2shots);
						obj4.addProperty("no_of_3shots", no_3shots);
						obj4.addProperty("no_of_4shots", no_4shots);
						obj4.addProperty("no_of_5shots", no_5shots);
						obj4.addProperty("no_of_6shots", no_6shots);
						obj4.addProperty("no_of_7shots", no_7shots);
						obj4.addProperty("no_of_8shots", no_8shots);
						obj4.addProperty("playerlevel", playerlevel);
						obj4.addProperty("_rev", revision);
						botsDb.update(obj4);

					} catch (Exception e1) {
						System.out.println(e1);
					}
				}
			}
		}

		// aroth: this step should be unnecessary (and is probably even wrong)
		// CouchDBUtil.shutdownClient(CouchDatabase.USERS);
		// CouchDBUtil.shutdownClient(CouchDatabase.BOTS);
		// db5.shutdown();
		// db6.shutdown();

		obj.put("Playername", playername);
		obj.put("Creditpoints", creditpoint);
		obj.put("Tournamentpoints", starting_credit_points);
		obj.put("Tournament_name", tournament_id);
		obj.put("Bitlets", bitletspoint);
		obj7.put("Player1", players.get(0).getM_Name());
		obj7.put("Player2", players.get(1).getM_Name());
		obj7.put("Player3", players.get(2).getM_Name());
		winnersDb.save(obj7);
		resultsDb.save(obj);

		// aroth: this step should be unnecessary (and is probably even wrong)
		// CouchDBUtil.shutdownClient(CouchDatabase.TOURNAMENT_RESULTS);
		// CouchDBUtil.shutdownClient(CouchDatabase.TOURNAMENT_WINNERS);
		// CouchDBUtil.shutdownClient(CouchDatabase.CREDIT_HISTORY);
		// db4.shutdown();
		// db7.shutdown();
		// db8.shutdown();
	}

	public void save_recent_winners() {
		//FIXME:  audit failed; iterates all items in the ranking database (several times; once for each of the top three players)
		CouchDbClient rankingsDb = CouchDBUtil.getClient(CouchDatabase.TOURNAMENT_RANKING); // new CouchDbClient(dbProperties4);
		CouchDbClient usersDb = CouchDBUtil.getClient(CouchDatabase.USERS); // new CouchDbClient(dbProperties5);
		CouchDbClient botsDb = CouchDBUtil.getClient(CouchDatabase.BOTS); // new CouchDbClient(dbProperties6);

		Collections.sort(players);

		for (int j = 0; j < 3; j++) {
			JsonObject json = new JsonObject();
			List<JsonObject> list = rankingsDb.view("_all_docs").query(JsonObject.class);
			String nickname = "";
			String player_level = "";
			try {
				JsonObject user = usersDb.find(JsonObject.class, players.get(j).getID());
				nickname = user.get("Nickname").toString().replace("\"", "");
				player_level = user.get("playerlevel").toString().replace("\"", "");

			} catch (Exception e) {
				JsonObject user = botsDb.find(JsonObject.class, players.get(j).getID());
				nickname = user.get("Nickname").toString().replace("\"", "");
				player_level = user.get("playerlevel").toString().replace("\"", "");
				// List <JsonObject> list = db4.view("_all_docs").query(JsonObject.class);
			}

			try {
				if (list.size() > 0)
				// for(int i=0; i < list.size(); i++)
				{
					// JsonObject rank_user = db4.find(JsonObject.class, Integer.toString(i));
					// String rank_name = rank_user.get("Nickname").toString().replace("\"", "");
					// String player_level1 = rank_user.get("player_level").toString().replace("\"", "");

					JsonObject rank_user = new JsonObject();

					rank_user.addProperty("_id", Integer.toString(list.size() + 1));
					rank_user.addProperty("Nickname", nickname);
					rank_user.addProperty("rank", j);
					rank_user.addProperty("playerlevel", player_level);
					rankingsDb.save(rank_user);

				} else {
					JsonObject rank_user = new JsonObject();

					rank_user.addProperty("_id", "1");
					rank_user.addProperty("Nickname", nickname);
					rank_user.addProperty("rank", j);
					rank_user.addProperty("playerlevel", player_level);
					rankingsDb.save(rank_user);
				}

			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}

		}
		// aroth: this step should be unnecessary (and is probably even wrong)
		// CouchDBUtil.shutdownClient(CouchDatabase.USERS);
		// CouchDBUtil.shutdownClient(CouchDatabase.TOURNAMENT_RANKING);
		// db5.shutdown();
		// db4.shutdown();

	}
}
