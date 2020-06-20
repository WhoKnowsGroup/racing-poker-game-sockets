/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.core.player.multipot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lightcouch.CouchDbClient;

import au.com.suncoastpc.util.CouchDBUtil;
import au.com.suncoastpc.util.types.CouchDatabase;

import com.google.gson.JsonObject;
import com.pokerace.gameplay.core.player.Player;

/**
 *
 * @author lokesh
 */
public class Top_shot_players {

	List<Player> players = new ArrayList<>();
	String[] playernames = new String[9];
	double[] no_of_shots = new double[9];
	String[] database_playernames = new String[7];
	String[] database_shots = new String[7];
	List<Player_shot> shots_players;

	Top_shot_players(List<Player> players) {
		this.players = players;
		for (int i = 0; i < 7; i++) {
			database_playernames[i] = "";
			database_shots[i] = "";
		}
	}

	public void sort_top_shot_players() {
		//XXX:  audit okay; lookup/update document by id
		CouchDbClient db = CouchDBUtil.getClient(CouchDatabase.SHOTS); // new CouchDbClient(dbProperties);
		for (int j = 0; j < 7; j++) {
			this.shots_players = new ArrayList<>();
			for (int i = 0; i < this.players.size(); i++) {
				// if(this.players.get(i).getNo_of_shots())

				double[] player_shots = new double[9];
				player_shots = this.players.get(i).getNo_of_shots();
				if (player_shots[j + 2] > 0) {
					this.shots_players.add(new Player_shot(player_shots[j + 2], this.players.get(i).getM_Name()));
				}
			}
			Collections.sort(this.shots_players);
			for (Player_shot player_shot : this.shots_players) {
				database_playernames[j] += player_shot.Player_name + ",";
				database_shots[j] += player_shot.shot + ",";
			}

		}

		JsonObject json = db.find(JsonObject.class, "0");
		// json.addProperty("_id", "0");
		json.addProperty("4shots_players", database_playernames[0]);
		json.addProperty("4shots", database_shots[0]);
		json.addProperty("5shots_players", database_playernames[1]);
		json.addProperty("5shots", database_shots[1]);
		json.addProperty("6shots_players", database_playernames[2]);
		json.addProperty("6shots", database_shots[2]);
		json.addProperty("7shots_players", database_playernames[3]);
		json.addProperty("7shots", database_shots[3]);
		json.addProperty("8shots_players", database_playernames[4]);
		json.addProperty("8shots", database_shots[4]);
		json.addProperty("9shots_players", database_playernames[5]);
		json.addProperty("9shots", database_shots[5]);
		json.addProperty("10shots_players", database_playernames[6]);
		json.addProperty("10shots", database_shots[6]);

		db.update(json);
	}
}
