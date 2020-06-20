/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.core;

import java.util.ArrayList;
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
 * @author root
 */
public class Saveresults {
	private List<Player> players;
	private String tournament_id;
	private Double starting_credit_points;

	public Saveresults(List players, String tournament_id, Double credits) {
		this.players = players;
		this.tournament_id = tournament_id;
		this.starting_credit_points = credits;
	}

	public void save_results() {
		//XXX:  audit neutral; mainly looks up/updates documents by id, however may perform many such operations depending upon how large 'players' is
		CouchDbClient resultsDb = CouchDBUtil.getClient(CouchDatabase.TOURNAMENT_RESULTS); // new CouchDbClient(dbProperties4);
		CouchDbClient usersDb = CouchDBUtil.getClient(CouchDatabase.USERS); // new CouchDbClient(dbProperties5);
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
		// Collections.sort(players);
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
				creditpoint += ply.getM_Credit() + ",";
				bitletspoint += "0" + ",";
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
					
					Double Credits = ply.getM_Credit();
					Double no_tournaments = Double.parseDouble(no_of_tournaments) + 1;
					Double no_bitlets = Double.parseDouble(no_of_bitlets);
					Double no_bonuses = Double.parseDouble(no_of_bonuses);
					Double no_2shots = Double.parseDouble(no_of_2shots);
					Double no_3shots = Double.parseDouble(no_of_3shots);
					Double no_4shots = Double.parseDouble(no_of_4shots);
					Double no_5shots = Double.parseDouble(no_of_5shots);
					Double no_6shots = Double.parseDouble(no_of_6shots);
					Double no_7shots = Double.parseDouble(no_of_7shots);
					Double no_8shots = Double.parseDouble(no_of_8shots);
					Double no_9shots = Double.parseDouble(no_of_9shots);
					Double no_10shots = Double.parseDouble(no_of_10shots);
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
						obj11.addProperty("Starting_Credits", starting_credit_points);
						obj11.addProperty("Ending_Credits", ply.getM_Credit());
						obj11.addProperty("Available_Credits", Credits);
						obj11.addProperty("Bitlets", "0,");
						historyDb.save(obj11);
					} catch (Exception e) {
						obj11 = historyDb.find(JsonObject.class, ply.getID());
						String tour_id = obj11.get("Tournament_id").toString().replace("\"", "");
						tour_id += "," + tournament_id;
						String initial_credits = obj11.get("Starting_Credits").toString().replace("\"", "");
						initial_credits += "," + starting_credit_points;
						String end_credits = obj11.get("Ending_Credits").toString().replace("\"", "");
						end_credits += "," + ply.getM_Credit();
						String available_credits = obj11.get("Available_Credits").toString().replace("\"", "");
						available_credits += "," + Credits;
						String available_bitlets = obj11.get("Bitlets").toString().replace("\"", "");
						available_bitlets += "0" + ",";
						obj11.addProperty("Starting_Credits", initial_credits);
						obj11.addProperty("Ending_Credits", end_credits);
						obj11.addProperty("Available_Credits", available_credits);
						obj11.addProperty("Tournament_id", tour_id);
						obj11.addProperty("Bitlets", available_bitlets);
						historyDb.update(obj11);
					}

					usersDb.update(obj3);

				} catch (Exception e) {

				}
			}
		}
		// aroth: this step should be unnecessary (and is probably even wrong)
		// CouchDBUtil.shutdownClient(CouchDatabase.USERS);
		// db5.shutdown();

		obj.put("Playername", playername);
		obj.put("Creditpoints", creditpoint);
		obj.put("Tournamentpoints", starting_credit_points);
		obj.put("Tournament_name", tournament_id);
		obj.put("Bitlets", bitletspoint);
		obj7.put("Player1", players.get(0).getM_Name());
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
}
