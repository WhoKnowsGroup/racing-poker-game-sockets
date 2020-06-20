package com.sockets.start;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lokesh
 */
public class Pocker_Socket extends Thread {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		Server_Socket serverSocket = new Server_Socket();
		serverSocket.start();
	}

}
