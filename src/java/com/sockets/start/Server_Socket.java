/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sockets.start;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import au.com.suncoastpc.auth.util.StringUtilities;
import au.com.suncoastpc.conf.Configuration;

/**
 *
 * @author lokesh
 */
public class Server_Socket extends Thread {

	private ServerSocket serverSocket;
	private Server_State serverState;
	private ThreadPoolExecutor executor;

	Server_Socket() {
		try {
			this.executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(Configuration.getWebsocketThreadpoolSize());
			this.serverSocket = new ServerSocket(Configuration.getWebsocketGameplayPort());
			this.serverState = new Server_State();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void closeSocket(Socket socket, String message) {
		try {
			if (! StringUtilities.isEmpty(message)) {
				socket.getOutputStream().write(message.getBytes());
			}
		}
		catch (Throwable ignored) {}
		finally {
			try {
				socket.close();
			}
			catch (Throwable ignored) {}
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					if (executor.getActiveCount() < Configuration.getWebsocketThreadpoolSize()) {
						HandleUserRequest handle = new HandleUserRequest(socket, this.serverState, this.executor);
						//handle.start();
						executor.execute(handle);
					}
					else {
						//we're at capacity; drop the incoming connection
						closeSocket(socket, "disconnect:too_many_clients");
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
		finally {
			executor.shutdown();
		}
	}

}
