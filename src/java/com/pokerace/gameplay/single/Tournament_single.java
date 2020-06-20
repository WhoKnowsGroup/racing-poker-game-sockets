/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.single;

import com.google.gson.JsonObject;
import com.pokerace.gameplay.core.Tournament;
import com.pokerace.gameplay.core.TournamentMode;
import com.sockets.players.User;

/**
 *
 * @author lokesh
 */
public class Tournament_single extends Tournament {
	public Tournament_single(int total_games, String tournament_id, String tournament_name, int maxPlayers, int minPlayers, Double credits) {
		super(TournamentMode.SINGLE, total_games, tournament_id, tournament_name, maxPlayers, minPlayers, credits);
	}

	@Override
	protected void tournament_loading_time() {
		try {
			Thread.sleep(1000);  //was: 10000
		} catch (Exception e) {
			System.out.println(e);
		}
	}


	@Override
	public void addPlayers(User user, JsonObject user_details) {
		String id = user_details.get("_id").toString().replace("\"", "");
		// System.out.println(id);
		String Nickname = user_details.get("Nickname").toString().replace("\"", "");
		// System.out.println(Nickname);
		String no_of_tournaments = user_details.get("no_of_tournaments").toString().replace("\"", "");
		// System.out.println(no_of_tournaments);
		String player_level = user_details.get("playerlevel").toString().replace("\"", "");
		// System.out.println(player_level);

		super.addPlayers(id, Nickname, no_of_tournaments, player_level, user.getSocket());
	}
}
