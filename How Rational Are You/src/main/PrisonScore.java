package main;

import main.item.Item;

public class PrisonScore {
	private int playerChoice;
	private int otherPlayerChoice;
	private int playerProfit;
	private int otherPlayerProfit;
	private int playerTime;
	private int otherPlayerTime;
	private boolean bPlayerProfit;
	private boolean bOtherPlayerProfit;
	
	public PrisonScore(int playerChoice, int otherPlayerChoice, int playerProfit, int otherPlayerProfit, 
			boolean bPlayerProfit, boolean bOtherPlayerProfit)
	{
		this.setPlayerChoice(playerChoice);
		this.setOtherPlayerChoice(otherPlayerChoice);
		this.setPlayerProfit(playerProfit);
		this.setOtherPlayerProfit(otherPlayerProfit);
		this.setbPlayerProfit(bPlayerProfit);
		this.setbOtherPlayerProfit(bOtherPlayerProfit);
	}

	public int getPlayerChoice() {
		return playerChoice;
	}

	public void setPlayerChoice(int playerChoice) {
		this.playerChoice = playerChoice;
	}

	public int getOtherPlayerChoice() {
		return otherPlayerChoice;
	}

	public void setOtherPlayerChoice(int otherPlayerChoice) {
		this.otherPlayerChoice = otherPlayerChoice;
	}

	public int getPlayerProfit() {
		return playerProfit;
	}

	public void setPlayerProfit(int playerProfit) {
		this.playerProfit = playerProfit;
	}

	public int getOtherPlayerProfit() {
		return otherPlayerProfit;
	}

	public void setOtherPlayerProfit(int otherPlayerProfit) {
		this.otherPlayerProfit = otherPlayerProfit;
	}

	public boolean isbPlayerProfit() {
		return bPlayerProfit;
	}

	public void setbPlayerProfit(boolean bPlayerProfit) {
		this.bPlayerProfit = bPlayerProfit;
	}

	public boolean isbOtherPlayerProfit() {
		return bOtherPlayerProfit;
	}

	public void setbOtherPlayerProfit(boolean bOtherPlayerProfit) {
		this.bOtherPlayerProfit = bOtherPlayerProfit;
	}

	public int getPlayerTime() {
		return playerTime;
	}

	public void setPlayerTime(int playerTime) {
		this.playerTime = playerTime;
	}

	public int getOtherPlayerTime() {
		return otherPlayerTime;
	}

	public void setOtherPlayerTime(int otherPlayerTime) {
		this.otherPlayerTime = otherPlayerTime;
	}
}
