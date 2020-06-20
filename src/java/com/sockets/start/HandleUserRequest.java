/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sockets.start;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.rmi.runtime.Log;
import CouchdbOperations.Get_details;
import au.com.suncoastpc.auth.util.StringUtilities;
import au.com.suncoastpc.conf.Configuration;
import au.com.suncoastpc.util.types.CouchDatabase;

import com.google.gson.JsonObject;
import com.sockets.commands.AbstractCommandFactory;
import com.sockets.commands.AddPlayer;
import com.sockets.commands.ExitPlayer;
import com.sockets.commands.GameController;
import com.sockets.commands.PlaceBet;
//import com.sockets.game.Dealer;
import com.sockets.players.User;

/**
 *
 * @author lokesh
 */
public class HandleUserRequest extends Thread {
	private final User user;
	private final GameController gameController;
	private final HashMap<String, AbstractCommandFactory> commands;
	private final Server_State serverState;
	private final ThreadPoolExecutor executor;
	private Dealer dealer;

	HandleUserRequest(Socket socket, Server_State serverState, ThreadPoolExecutor threadPool) {
		this.user = new User(socket);
		this.commands = new HashMap<>();
		this.commands.put("PlaceBet", new PlaceBet());
		this.commands.put("AddPlayer", new AddPlayer());
		this.commands.put("ExitPlayer", new ExitPlayer());
		this.gameController = new GameController(commands);
		this.serverState = serverState;
		this.executor = threadPool;
	}
	
	private void closeSocket(String message) {
		try {
			if (! StringUtilities.isEmpty(message)) {
				user.getSocket().getOutputStream().write(message.getBytes());
			}
		}
		catch (Throwable ignored) {}
		finally {
			try {
				user.getSocket().close();
			}
			catch (Throwable ignored) {}
		}
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.user.getSocket().getInputStream()));

			String message = reader.readLine();
			String[] msg = message.split(",");
			//FIXME:  we really need to validate the user using a nonce or similar (for non-guest users); pass in through server, maybe as 4th param
			if (msg.length >= 3 && msg[0].equals("connect")) {
				Get_details dbHandle = new Get_details();
				JsonObject dbUser = dbHandle.read_db(CouchDatabase.USERS, msg[1]);
				JsonObject dbTournament = dbHandle.read_db(CouchDatabase.ACTIVE_TOURNAMENTS, msg[2]);
				
				boolean validTournament = "guest_mode".equalsIgnoreCase(msg[2]) || 
							       (dbTournament != null && "active".equalsIgnoreCase(new com.pokerace.ejb.model.Tournament(dbTournament).getStatus()));
				boolean validUser = "guest_mode".equalsIgnoreCase(msg[2]) || 
							       (dbUser != null && "active".equalsIgnoreCase(new com.pokerace.ejb.model.User(dbUser).getStatus()));
				
				if (validUser && validTournament) {
					this.user.setId(msg[1]);
					
					if (executor.getActiveCount() < Configuration.getWebsocketThreadpoolSize()) {
						this.dealer = new Dealer(this.user, msg[2], this.serverState);
						//this.dealer.start();
						executor.execute(this.dealer);
						this.dealer.handle_request();
					}
					else {
						//we're at capacity; drop the incoming connection
						closeSocket("disconnect:too_many_clients");
					}
				}
				else {
					closeSocket(null);
				}
			}
			else {
				Log.getLog("InvalidMessageException", "HandleUserRequest", MIN_PRIORITY);
				closeSocket(null);
			}

		} catch (IOException e) {
			Log.getLog("IOException", "HandleUserRequest", MIN_PRIORITY);
			closeSocket(null);
		} catch (InterruptedException ex) {
			Logger.getLogger(HandleUserRequest.class.getName()).log(Level.SEVERE, null, ex);
			closeSocket(null);
		}
	}

}
