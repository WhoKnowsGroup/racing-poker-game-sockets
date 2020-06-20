/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.core.player.multipot;

import java.util.Collections;
import java.util.List;

import org.lightcouch.CouchDbClient;

import au.com.suncoastpc.util.CouchDBUtil;
import au.com.suncoastpc.util.types.CouchDatabase;

import com.google.gson.JsonObject;

/**
 *
 * @author lokesh
 */
public class Top50_playerlevels {
	List<Player_level> players_level;

	Top50_playerlevels(List<Player_level> players_level) {
		this.players_level = players_level;
	}

	void sort_save_playerlevels() {
		try {
			String names = "";
			String levels = "";
			Collections.sort(this.players_level);
			//XXX:  audit okay; lookup/update document by id
			CouchDbClient db = CouchDBUtil.getClient(CouchDatabase.TOP_50_PLAYERS); // new CouchDbClient(dbProperties);

			for (Player_level player_level : this.players_level) {
				System.out.println(player_level.player_level.intValue());
				names += player_level.player_name + ",";
				levels += "" + player_level.player_level.intValue() + ",";
			}

			JsonObject json = db.find(JsonObject.class, "0");
			// json.addProperty("_id", "0");
			json.addProperty("Levels", levels);
			json.addProperty("Names", names);
			db.update(json);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}
}
