package com.pokerace.gameplay.core.ext.multipot;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;

import com.pokerace.gameplay.core.Bet;
import com.pokerace.gameplay.core.Game;
import com.pokerace.gameplay.core.PokerException;
import com.pokerace.gameplay.core.player.Player;
import com.pokerace.gameplay.core.player.multipot.Bonus_players;

/// <summary>
/// Returns all the bets for the current game
/// </summary>
/**
 * Represents one instance of a game.
 * 
 * @author User
 */
public class MultipotGame extends Game {
	// FIXME: properly encapsulate fields
	List<Bonus> m_bonus = new ArrayList<Bonus>();
	public List<Bonus_players> m_bonus_players = new ArrayList<Bonus_players>();

	@Override
	public void Reset() {
		if (m_bonus == null) {
			//XXX:  this should never actually happen...
			m_bonus = new ArrayList<Bonus>();
		}
		if (m_bonus_players == null) {
			//XXX:  this should never actually happen...
			m_bonus_players = new ArrayList<Bonus_players>();
		}
		
		if (!this.m_bonus.isEmpty()) {
			for (Bonus d : this.m_bonus) {
				d.total_bets = 0.0;
				d.credits = 0.0;
				// d.profits_added = false ;
			}
		}
		this.m_bonus_players.clear();
		super.Reset();
	}

	@Override
	public void PayWinningHand() {
		// Increase the credit by the corresponding credits of the bets on the hand
		double amtWon = 0;
		for (Bet bet : this.m_Bets) {
			if (bet.getHandNumber() == this.m_WinningHandNumber) {
				amtWon += bet.getCredit();
				bet.getUser().setM_Credit(bet.getUser().getM_Credit() + Math.round(bet.getCredit()));
				for (Bonus b : this.m_bonus) {
					if (b.user.getID().equalsIgnoreCase(bet.getUser().getID())) {
						b.credits += Math.round(bet.getCredit());
					}
				}
				System.out.println("PayWinningHand FUNCTION IS CALLED");
				playerWon = bet.getUser();
				winnerName = playerWon.getM_Name();
				playerWonCredit = playerWon.getM_Credit();
				System.out.println("USER CREDIT ------>> " + bet.getUser().getM_Credit() + "BET CREDIT ------>> " + bet.getCredit());
			}

		}

		for (Bonus b : this.m_bonus) {
			int count = 0;
			String bonus_type = "";
			double bonus_amount = 0.0;
			System.out.println(b.user.getID() + "credits" + b.credits + "totalbets" + b.total_bets);
			System.out.println(b.previous_profits);
			if (b.total_bets < b.credits) {

				for (Boolean c : b.previous_profits) {
					if (c)
						count++;
				}
				if (count == 1)
					bonus_type = "2shot";
				if (count == 2)
					bonus_type = "3shot";
				if (count == 3)
					bonus_type = "4shot";
				if (count == 4)
					bonus_type = "5shot";
				if (count == 5)
					bonus_type = "6shot";
				if (count == 6)
					bonus_type = "7shot";
				if (count == 7)
					bonus_type = "8shot";
				if (count == 8)
					bonus_type = "9shot";
				if (count == 9)
					bonus_type = "10shot";

				/*
				 * if(count >= 0) { b.player_level +=1.0; }
				 */
				if (count >= 0) {
					System.out.println(count + "count");
					/*
					 * if(count == 1) { b.bitlets = 1.0; b.player_level += 1.0; }
					 */
					if (count == 3)
						b.bitlets = 1.0;
					if (count == 4) {
						b.bitlets = 2.0;
						b.player_level = 1.0;
					}
					if (count == 5) {
						b.bitlets = 3.0;
						b.player_level = 1.0;
					}
					if (count == 6) {
						b.bitlets = 4.0;
						b.player_level = 1.0;
					}
					if (count == 7) {
						b.bitlets = 5.0;
						b.player_level = 1.0;
					}
					if (count == 8) {
						b.bitlets = 30.0;
						b.player_level = 3.0;
					}
					if (count == 9) {
						b.bitlets = 50.0;
						b.player_level = 5.0;
					}

					System.out.println(b.bitlets + "bitlets");
					System.out.println(b.bonuses + "bonuses");
				} else {
					b.bitlets = 0.0;
				}
				b.bonuses = count;
				System.out.println(b.bitlets + "bonuses" + b.bonuses);
				bonus_amount = count * 0.1 * b.credits;
				bonus_amount = Math.round(bonus_amount);
				b.bonus_amount = bonus_amount;

				System.out.println(b.user.getID() + " Bonus" + bonus_amount + bonus_type);
				b.previous_profits.add(new Boolean(true));
				System.out.println(b.user.getID() + "total bets" + b.user.getM_Credit());
				if (count > 0) {
					b.user.setM_Credit(b.user.getM_Credit() + bonus_amount);
					System.out.println(b.user.getID() + "bonus bets" + b.user.getM_Credit());
					this.m_bonus_players.add(new Bonus_players(b.user, bonus_type, b.total_bets, bonus_amount, b.bitlets, b.bonuses, b.player_level));
				}
			}

		}
		for (Bet bet : this.m_Bets) {
			if (bet.getHandNumber() == this.m_WinningHandNumber) {

				for (Bonus b : this.m_bonus) {
					if (b.user.getID().equalsIgnoreCase(bet.getUser().getID())) {
						// b.credits += bet.getCredit();
						System.out.println(b.bitlets + "bitlets in the bonuses");
						System.out.println(b.bonuses + "bonuses in the bitlets");
						if (!b.paid_bonus) {
							System.out.println("Bonus_amount" + b.bonus_amount);
							bet.getUser().setM_Credit(bet.getUser().getM_Credit() + b.bonus_amount);

							bet.getUser().setM_bitlets(bet.getUser().getM_bitlets() + b.bitlets);
							bet.getUser().setM_bonuses(bet.getUser().getM_bonuses() + b.bonuses);
							b.paid_bonus = true;
						}
					}
				}

				System.out.println("USER CREDIT ------>> " + bet.getUser().getM_Credit() + bet.getUser().getID());
				System.out.println("Bitlets" + bet.getUser().getM_bitlets());
				System.out.println("Bonuses" + bet.getUser().getM_bonuses());
			}

		}
		this.m_AmountWon = amtWon;
		// this.Credit = this.Credit + amtWon;
		// System.out.println("WINNIG PLAYER NAME ----->> " + playerWon.getM_Name());

	}

	@Override
	public void PlaceBet(int handNumber, double amount, Player player) throws PokerException {
		super.PlaceBet(handNumber, amount, player);
		if (this.m_bonus.isEmpty()) {
			this.m_bonus.add(new Bonus(player, amount));
		} else {
			boolean user_found = false;
			for (Bonus b : this.m_bonus) {
				if (b.user.getID().equalsIgnoreCase(player.getID())) {
					b.total_bets += amount;
					user_found = true;
				}
			}
			if (!user_found) {
				this.m_bonus.add(new Bonus(player, amount));
			}
		}

		for (Bonus b : this.m_bonus) {
			System.out.println(b.user.getID() + "Placed" + b.total_bets + b.credits);
		}
	}
}