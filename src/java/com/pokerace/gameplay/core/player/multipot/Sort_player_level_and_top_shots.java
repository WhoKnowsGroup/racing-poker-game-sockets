/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.core.player.multipot;

import java.util.ArrayList;
import java.util.List;

import org.lightcouch.CouchDbClient;

import au.com.suncoastpc.util.CouchDBUtil;
import au.com.suncoastpc.util.types.CouchDatabase;

import com.google.gson.JsonObject;
import com.pokerace.ejb.model.User;
import com.pokerace.gameplay.core.player.Player;
import com.pokerace.gameplay.core.player.PlayerManager;

/**
 *
 * @author lokesh
 */
public class Sort_player_level_and_top_shots {
	public synchronized void load_players() {
		//FIXME:  audit failed; this scans all players and all bots in the database (unclear why it does this; called at the start of every tournament)
		CouchDbClient usersDb = CouchDBUtil.getClient(CouchDatabase.USERS); // new CouchDbClient(dbProperties);
		CouchDbClient botsDb = CouchDBUtil.getClient(CouchDatabase.BOTS); // new CouchDbClient(dbProperties1);

		PlayerManager playerManager = new PlayerManager();
		List<Player> players = new ArrayList<>();
		List<Player_level> players_level = new ArrayList<>();
		List<JsonObject> list1 = usersDb.view("_all_docs").query(JsonObject.class);
		for (JsonObject json : list1) {
			String id = json.get("id").toString().replace("\"", "");
			double[] shots = new double[9];
			User user = new User(usersDb.find(JsonObject.class, id));

			// System.out.println(id);
			String Nickname = user.getNickname();
			String player_level = user.getPlayerLevel() + "";
			players_level.add(new Player_level(Double.parseDouble(player_level), Nickname));
			shots[0] = user.getNum2Shots();
			shots[1] = user.getNum3Shots();
			shots[2] = user.getNum4Shots();
			shots[3] = user.getNum5Shots();
			shots[4] = user.getNum6Shots();
			shots[5] = user.getNum7Shots();
			shots[6] = user.getNum8Shots();
			shots[7] = user.getNum9Shots();
			shots[8] = user.getNum10Shots();
			// System.out.println(player_level);
			Player player = new Player(playerManager);
			player.setID(id);
			player.setM_Name(Nickname);
			player.setM_player_level(Double.parseDouble(player_level));//
			player.setM_bonuses(user.getNumBonuses());
			player.setNo_of_shots(shots);
			players.add(player);
		}
		List<JsonObject> list2 = botsDb.view("_all_docs").query(JsonObject.class);
		for (JsonObject json : list2) {
			String id = json.get("id").toString().replace("\"", "");
			double[] shots = new double[9];
			JsonObject json1 = botsDb.find(JsonObject.class, id);
			// PlayerManager playerManager = new PlayerManager();
			// System.out.println(id);
			String Nickname = json1.get("Nickname").toString().replace("\"", "");
			String player_level = json1.get("playerlevel").toString().replace("\"", "");
			players_level.add(new Player_level(Double.parseDouble(player_level), Nickname));
			shots[0] = Double.parseDouble(json1.get("no_of_2shots").toString().replace("\"", ""));
			shots[1] = Double.parseDouble(json1.get("no_of_3shots").toString().replace("\"", ""));
			shots[2] = Double.parseDouble(json1.get("no_of_4shots").toString().replace("\"", ""));
			shots[3] = Double.parseDouble(json1.get("no_of_5shots").toString().replace("\"", ""));
			shots[4] = Double.parseDouble(json1.get("no_of_6shots").toString().replace("\"", ""));
			shots[5] = Double.parseDouble(json1.get("no_of_7shots").toString().replace("\"", ""));
			shots[6] = Double.parseDouble(json1.get("no_of_8shots").toString().replace("\"", ""));
			shots[7] = Double.parseDouble(json1.get("no_of_9shots").toString().replace("\"", ""));
			shots[8] = Double.parseDouble(json1.get("no_of_10shots").toString().replace("\"", ""));
			// System.out.println(player_level);
			Player player = new Player(playerManager);
			player.setID(id);
			player.setM_Name(Nickname);
			player.setM_player_level(Double.parseDouble(player_level));//
			player.setM_bonuses(Double.parseDouble(json1.get("no_of_bonuses").toString().replace("\"", "")));
			player.setNo_of_shots(shots);
			players.add(player);
		}

		Top50_playerlevels toplevels = new Top50_playerlevels(players_level);
		toplevels.sort_save_playerlevels();
		Top_shot_players topshots = new Top_shot_players(players);
		topshots.sort_top_shot_players();
		// Top50_players top50 = new Top50_players(players);
		// top50.Sort_top50();
	}

}
