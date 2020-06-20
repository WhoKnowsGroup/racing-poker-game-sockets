/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.core.player.multipot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import CouchdbOperations.Get_details;
import CouchdbOperations.Update_details;

import com.pokerace.gameplay.core.player.Player;

/**
 *
 * @author lokesh
 */
public class Top50_players {
	private final List<Player> players;
	HashMap<Integer, String> highPlayers;
	HashMap<Integer, String> replacedPlayers;

	Top50_players(List<Player> players) {
		this.players = players;
		this.highPlayers = new HashMap<Integer, String>();
		this.replacedPlayers = new HashMap<Integer, String>();
	}

	synchronized void Sort_top50() {
		Get_details get_details = new Get_details();
		this.highPlayers = get_details.getTop50();
		boolean not_found = false;
		for (Iterator<Map.Entry<Integer, String>> it = this.highPlayers.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Integer, String> entry = it.next();
			String replaced_names = entry.getValue().replaceAll(",,", ",");
			this.replacedPlayers.put(entry.getKey(), replaced_names);
		}

		for (Player player : this.players) {
			int level = (int) player.getM_player_level();
			System.out.println(level + "Level");

			String player_name = player.getM_Name();

			System.out.println("Names" + player_name);

			for (Iterator<Map.Entry<Integer, String>> it = this.highPlayers.entrySet().iterator(); it.hasNext();) {
				boolean empty_name = false;
				Map.Entry<Integer, String> entry = it.next();
				System.out.println(entry.getKey());
				System.out.println(entry.getValue());
				String[] names = entry.getValue().split(",");
				for (String name : names) {

					if (name.equals(player_name)) {
						String replace_names = entry.getValue();
						String replaced_names = replace_names.replaceAll(player_name, "");
						System.out.println("Player_name" + replaced_names);
						replaced_names = replaced_names.replaceAll(",,", ",");
						this.replacedPlayers.put(entry.getKey(), replaced_names);
						System.out.println(this.replacedPlayers.get(entry.getKey()));
					}
				}

			}
			try {
				String names = this.replacedPlayers.get(level);
				String[] split_names = names.split(",");
				for (String name : split_names) {
					if (name.equals(player_name)) {
						not_found = true;
					}
				}
				if (!not_found) {
					names += "," + player_name;

					this.replacedPlayers.put(level, names);
				}
			} catch (Exception e) {

				this.replacedPlayers.put(level, player_name);
			}

			not_found = false;

		}
		int count = 0;
		Update_details updateDetails = new Update_details();
		for (Iterator<Map.Entry<Integer, String>> it = this.replacedPlayers.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Integer, String> entry = it.next();
			String replaced_names = entry.getValue().replaceAll(",,", ",");
			this.replacedPlayers.put(entry.getKey(), replaced_names);
		}
		List keys = new ArrayList(this.replacedPlayers.keySet());
		Collections.sort(keys, Collections.reverseOrder());
		for (Iterator r = keys.iterator(); r.hasNext();) {
			Object o = r.next();
			System.out.println(o + "lveel" + this.replacedPlayers.get(o));
			String[] values = new String[2];
			String level = "" + o.toString();
			String name = this.replacedPlayers.get(o);
			if (name.length() > 1 && count <= 49) {
				updateDetails.update("pokerace_top_50_players", "" + count, level, name);
			}
			count++;
			// System.out.println(entry.getValue());
		}

	}
}
