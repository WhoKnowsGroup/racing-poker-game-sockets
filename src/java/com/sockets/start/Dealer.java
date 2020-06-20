/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sockets.start;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.simple.JSONObject;

import sun.rmi.runtime.Log;
import CouchdbOperations.Get_details;
import au.com.suncoastpc.conf.Configuration;

import com.google.gson.JsonObject;
import com.pokerace.gameplay.guest.Tournament_guest_mode;
import com.pokerace.gameplay.multi.Tournament_multiplayer;
import com.pokerace.gameplay.potpoker.Tournament_potpoker;
import com.pokerace.gameplay.single.Tournament_single;
import com.sockets.players.User;

/**
 *
 * @author lokesh
 */
public class Dealer extends Thread {
	private Server_State serverState;
	private final User user;
	private Tournament_multiplayer tournament_multiplayer;
	private Tournament_single tournament_singleplayer;
	private Tournament_potpoker tournament_potpoker;
	private Tournament_guest_mode tournament_guest_mode;
	private final Get_details get_details;
	private String tournament_id;
	private final String tournament_name;

	Dealer(User user, String tournament_name, Server_State serverState) {
		this.get_details = new Get_details();
		this.user = user;
		this.tournament_name = tournament_name;
		this.serverState = serverState;
	}

	public JsonObject check_for_validUser(User user) {
		JsonObject json = this.get_details.read_db("pokerace_users", user.getId());
		return json;
	}

	public void setServerState(Server_State serverState) {
		this.serverState = serverState;
	}

	public JsonObject getTournament_details(String tournament_name) {
		JsonObject json = this.get_details.read_db("pokerace_tournaments_active", tournament_name);
		return json;
	}

	public boolean send_email(String email) {
		JSONObject json = new JSONObject();
		
		String to = "bujo000007@gmail.com"; // FIXME: this should be configurable
		// to = request.getParameter("to");
		// Sender's email ID needs to be mentioned
		String from = Configuration.getAdminEmailAddress();

		// Assuming you are sending email from localhost
		String host = Configuration.getEmailHost();

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.user", from);
		properties.setProperty("mail.smtp.password", Configuration.getEmailPass());
		if (Configuration.getEmailSecure()) {
			properties.put("mail.smtp.starttls.enable", "true");
		}

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(Configuration.getAdminEmailAddress(), Configuration.getAdminEmailName()));
			// message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("User," + email + " played a tournament" + "," + tournament_name);

			// Now set the actual message
			Date date = new Date();
			String content = "<p> Dear Peter </p>" + "<h2> A user just started playing a tournament.  Below are the user details.</h2>  " + "<h4> Username :" + email + "</h4>" + ""
							+ "Logging Date and Time:" + date.toString() + "<br/>" + "Playing Tournament:" + tournament_name + "," + tournament_id;

			content += "<br/>" + "<p> Regards </p>";
			content += "<p> Admin Team , </p>" + "<p> Racing Poker(brought you by Pokerace Pty Ltd. ) </p>";

			/*
			 * String content = "<h1> Hi Dirk, </h1>" + "<p> We noticed that you are having some fun with our game. " +
			 * " To thank you and show you how much you mean to us,we credited you with 25k extra credits to keep you busy having fun with our game." + " Now, we hope you will have much fun. </p>";
			 * 
			 * content += "<br/>"+ "<p> Regards </p>" +"<p> Admin Team , </p>"+ "<p> Racing Poker( brought you by Pokerace Pty Ltd. ) </p>" ;
			 */

			message.setContent(content, "text/html");

			// Send message
			//XXX:  it's actually pretty creepy that the system sends an e-mail whenever anyone plays a game; this has been disabled
			/*Transport tr = session.getTransport("smtp");
			tr.connect(Configuration.getEmailHost(), Configuration.getAdminEmailAddress(), Configuration.getEmailPass());
			message.saveChanges();
			tr.sendMessage(message, message.getAllRecipients());
			tr.close();
			json.put("result", "success");
			System.out.println("Sent message successfully....");*/
			return true;
		} catch (Exception mex) {
			mex.printStackTrace();
			json.put("result", "failed");
		}
		return false;
	}

	public void check_for_loading_tournaments(String tournament_id) {

	}

	public String generateTournament_id() {
		JsonObject json1 = new JsonObject();
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		month = month;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		// String date_toString = date.toString();
		String initial_seconds = "RP" + year + month + day + hour + minute + second;

		return initial_seconds;
	}

	public void handle_request() throws InterruptedException {
		if (this.tournament_name.equalsIgnoreCase("guest_mode")) {
			this.tournament_id = generateTournament_id();
			int total_games = 3;
			int maxPlayers = 10;
			int minPlayers = 5;
			this.user.setCredits(3000.0);
			this.tournament_guest_mode = new Tournament_guest_mode(total_games, this.tournament_id, this.tournament_name, maxPlayers, minPlayers, 3000.0);
			this.tournament_guest_mode.addPlayers(this.user, null);
			this.tournament_guest_mode.start_tournament();
		} else {
			JsonObject user_details = this.check_for_validUser(this.user);
			this.user.setCredits(Double.parseDouble(user_details.get("Credit").toString().replace("\"", "")));
			if (user_details.get("result").toString().replace("\"", "").equals("success")) {
				JsonObject tournament_details = this.getTournament_details(this.tournament_name);
				if (tournament_details.get("result").toString().replace("\"", "").equals("success")) {
					this.tournament_id = generateTournament_id();
					int total_games = Integer.parseInt(tournament_details.get("Number_of_games").toString().replace("\"", ""));
					int maxPlayers = Integer.parseInt(tournament_details.get("Number_of_MaxPlayers").toString().replace("\"", ""));
					int minPlayers = Integer.parseInt(tournament_details.get("Number_of_MinPlayers").toString().replace("\"", ""));
					String tournament_type = tournament_details.get("Type").toString().replace("\"", "");
					this.send_email(this.user.getId());
					if (tournament_type.equalsIgnoreCase("One_player_level")) {
						Double credits = Double.parseDouble(tournament_details.get("starting_credit_points").toString().replace("\"", ""));
						credits = this.user.getCredits();
						System.out.println(credits);
						this.tournament_singleplayer = new Tournament_single(total_games, this.tournament_id, this.tournament_name, maxPlayers, minPlayers, credits);
						this.tournament_singleplayer.addPlayers(this.user, user_details);
						this.tournament_singleplayer.start_tournament();
					} else {
						if (tournament_type.equals("Pot_poker")) {
							Double credits = Double.parseDouble(tournament_details.get("starting_credit_points").toString().replace("\"", ""));
							int bitlets_required = Integer.parseInt(tournament_details.get("Required_bitlets").toString().replace("\"", ""));
							try {
								if (this.serverState.Game_command(this.tournament_name, this.tournament_potpoker, "check") == null) {
									this.tournament_potpoker = new Tournament_potpoker(total_games, this.tournament_id, this.tournament_name, maxPlayers, minPlayers, credits, this.serverState,
													bitlets_required);
									this.tournament_potpoker.addPlayers(this.user, user_details);
									this.serverState.Game_command(this.tournament_name, this.tournament_potpoker, "load");
									this.tournament_potpoker.start_tournament();

								} else {
									this.tournament_potpoker = this.serverState.Game_command(this.tournament_name, this.tournament_potpoker, "check");
									this.tournament_potpoker.addPlayers(this.user, user_details);
								}
							} catch (Exception e) {
								System.out.println(e);
							}
						} else {
							Double credits = Double.parseDouble(tournament_details.get("starting_credit_points").toString().replace("\"", ""));
							try {
								if (this.serverState.Game_commands(this.tournament_name, this.tournament_multiplayer, "check") == null) {
									this.tournament_multiplayer = new Tournament_multiplayer(total_games, this.tournament_id, this.tournament_name, maxPlayers, minPlayers, credits, this.serverState);
									this.tournament_multiplayer.addPlayers(this.user, user_details);
									this.serverState.Game_commands(this.tournament_name, this.tournament_multiplayer, "load");
									this.tournament_multiplayer.start_tournament();

								} else {
									this.tournament_multiplayer = this.serverState.Game_commands(this.tournament_name, this.tournament_multiplayer, "check");
									this.tournament_multiplayer.addPlayers(this.user, user_details);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				}
			} else {
				Log.getLog("InvalidUserException", "HandleUserRequest", MIN_PRIORITY);
			}
		}
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.user.getSocket().getInputStream()));
			while (true) {	//loop ends if/when an exception is thrown
				String message = reader.readLine();
				System.out.println(message + "Serious");
				String[] msg = message.split(",");
				if (msg[0].equals("bet:true") && ! "fastforward:true".equals(msg[msg.length - 1])) {
					try {
						this.tournament_multiplayer.place_bet(msg, this.user);
					} catch (Exception e) {
						try {
							this.tournament_singleplayer.place_bet(msg, this.user);
						} catch (Exception e1) {
							try {
								this.tournament_potpoker.place_bet(msg, this.user);
							} catch (Exception e2) {
								this.tournament_guest_mode.place_bet(msg, user);
							}
						}
					}
				}
				if (msg[0].equals("deal:true")) {
					this.tournament_singleplayer.deal();
				}
				if (msg[0].equals("exit:true")) {
					System.out.println("Exit");
					this.tournament_singleplayer.exit();
				}
				if (msg[0].equals("Chat:true")) {
					System.out.println(msg[1]);
					this.tournament_multiplayer.receive_chat("Chat:" + msg[1].replaceAll(" ", ","));
				}
				if ("fastforward:true".equals(msg[0]) || "fastforward:true".equals(msg[msg.length - 1])) {
					//FIXME:  doesn't appear to work??? (add debug messages)
					System.out.println("!!!!!!!!!!  GOT FASTFORWARD MESSAGE");
					try {
						System.out.println("!!!!!!!!!!  TRYING SINGLEPLAYER");
						this.tournament_singleplayer.setFastForward(true);
						System.out.println("!!!!!!!!!!  SINGLEPLAYER SUCCESS");
					}
					catch (Exception notSinglePlayer) {
						try {
							System.out.println("!!!!!!!!!!  TRYING GUEST");
							this.tournament_guest_mode.setFastForward(true);
							System.out.println("!!!!!!!!!!  GUEST SUCCESS");
						}
						catch (Exception notGuestMode) {
							//multiplayer modes don't support fast-forward
							System.out.println("!!!!!!!!!!  FAILED TO FASTFORWARD");
							notGuestMode.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			try {
				if (this.tournament_singleplayer != null) {
					this.tournament_singleplayer.exit();
				}
			}
			catch (Exception ignored) {
				//ignored
			}
			Log.getLog("ReceiveUserException", "HandleUserRequest", MIN_PRIORITY);
		}
	}
}
