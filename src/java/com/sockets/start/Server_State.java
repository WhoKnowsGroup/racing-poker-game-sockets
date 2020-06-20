/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sockets.start;

//import com.sockets.Single_player_game.Game;
import java.util.HashMap;

import com.pokerace.gameplay.multi.Tournament_multiplayer;
import com.pokerace.gameplay.potpoker.Tournament_potpoker;

/**
 *
 * @author lokesh
 */
public class Server_State {
	private HashMap<String, Tournament_multiplayer> loading_games;
	private HashMap<String, Tournament_multiplayer> running_games;
	private HashMap<String, Tournament_multiplayer> finished_games;
	private HashMap<String, Tournament_potpoker> pot_poker_loading_games;
	private HashMap<String, Tournament_potpoker> pot_poker_running_games;
	private HashMap<String, Tournament_potpoker> pot_poker_finished_games;

	Server_State() {
		this.loading_games = new HashMap<>();
		this.running_games = new HashMap<>();
		this.finished_games = new HashMap<>();
		this.pot_poker_loading_games = new HashMap<>();
		this.pot_poker_running_games = new HashMap<>();
		this.pot_poker_finished_games = new HashMap<>();

	}

	public synchronized Tournament_multiplayer Game_commands(String id, Tournament_multiplayer tournament, String command) {
		System.out.println(command);
		switch (command) {
			case "start":
				System.out.println("Start");
				if (this.loading_games.containsKey(id)) {
					this.loading_games.remove(id);
					return tournament;
				}
				break;

			case "check":
				System.out.println("Check");
				if (this.loading_games.containsKey(id)) {
					return this.loading_games.get(id);
				} else {
					return null;
				}

			case "load":
				System.out.println("load");
				this.loading_games.put(id, tournament);
				System.out.println("load");
				return tournament;

			case "end":
				System.out.println("end");
				break;

			default:
				System.out.println("Invalid Operation");
				break;

		}
		return null;
	}

	public synchronized Tournament_potpoker Game_command(String id, Tournament_potpoker tournament, String command) {
		System.out.println(command);
		switch (command) {
			case "start":
				System.out.println("Start");
				if (this.pot_poker_loading_games.containsKey(id)) {
					this.pot_poker_loading_games.remove(id);
					return tournament;
				}
				break;

			case "check":
				System.out.println("Check");
				if (this.pot_poker_loading_games.containsKey(id)) {
					return this.pot_poker_loading_games.get(id);
				} else {
					return null;
				}

			case "load":
				System.out.println("load");
				this.pot_poker_loading_games.put(id, tournament);
				System.out.println("load");
				return tournament;

			case "end":
				System.out.println("end");
				break;

			default:
				System.out.println("Invalid Operation");
				break;

		}
		return null;
	}
}
