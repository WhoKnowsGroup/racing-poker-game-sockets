/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sockets.start;

/**
 *
 * @author root
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import au.com.suncoastpc.conf.Configuration;

/**
 *
 * @author User
 */
public class Client_connection {

	public static void main(String[] args) {

		try {
			Socket socket = new Socket(Configuration.getWebsocketGameplayHost(), Configuration.getWebsocketGameplayPort());
			System.out.println("connection made");
			String msg = "connect" + "," + "bujo000007@gmail.com" + "," + "Racing-Pot-Poker-Mid-Lev";
			System.out.println(socket.getOutputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(msg);
			// System.out.println(socket.getInputStream());
			// out.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {
				// reader =
				System.out.println(" reader");
				msg = reader.readLine();
				System.out.println(msg);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
