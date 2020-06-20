/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sockets.players;

import java.net.Socket;

import com.pokerace.gameplay.core.Game;

/**
 *
 * @author lokesh
 */
public class User {
	private final Socket socket;
	private Game game;
	private String id;
	private String firstName;
	private String lastName;
	private Double credits;
	
	public User(Socket socket) {
		this.socket = socket;
	}
	
	public User(User user) {
		this.id = user.id;
		this.socket = user.getSocket();
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Double getCredits() {
		return this.credits;
	}
	public void setCredits(Double credits) {
		this.credits = credits;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

	public Socket getSocket() {
		return socket;
	}
}
