package main;

import main.item.Item;

public class BiddingScoreResult {

	private int noOfBidScores;
	private int noOfBidScoreWin;
	private int noOfBidScoreLose;
	private int bsItemValueAvgW, bsItemValueTotalW, bsPlayerBidAvgW, bsPlayerBidTotalW, bsOtherPlayerBidAvgW, bsOtherPlayerBidTotalW, bsAmountWonAvgW, bsAmountWonTotalW;
	private int bsItemValueAvgL, bsItemValueTotalL, bsPlayerBidAvgL, bsPlayerBidTotalL, bsOtherPlayerBidAvgL, bsOtherPlayerBidTotalL, bsAmountWonAvgL, bsAmountWonTotalL;
	private int itemValueW, playerBidW, otherPlayerBidW, amountWonW;
	private int itemValueL, playerBidL, otherPlayerBidL, amountWonL;
	private double percentage;
	
	public BiddingScoreResult(
			int noOfBidScores,
			int noOfBidScoreWin,
			int noOfBidScoreLose,
			int bsItemValueAvgW, 
			int bsItemValueTotalW, 
			int bsPlayerBidAvgW, 
			int bsPlayerBidTotalW, 
			int bsOtherPlayerBidAvgW, 
			int bsOtherPlayerBidTotalW, 
			int bsAmountWonAvgW, 
			int bsAmountWonTotalW,
			int bsItemValueAvgL, 
			int bsItemValueTotalL, 
			int bsPlayerBidAvgL, 
			int bsPlayerBidTotalL, 
			int bsOtherPlayerBidAvgL, 
			int bsOtherPlayerBidTotalL, 
			int bsAmountWonAvgL, 
			int bsAmountWonTotalL,
			int itemValueW, 
			int playerBidW, 
			int otherPlayerBidW, 
			int amountWonW,
			int itemValueL, 
			int playerBidL, 
			int otherPlayerBidL,
			int amountWonL)
	{
		this.noOfBidScores = noOfBidScores;
		this.noOfBidScoreWin = noOfBidScoreWin;
		this.noOfBidScoreLose = noOfBidScoreLose;
		this.bsItemValueAvgW = bsItemValueAvgW;
		this.setBsItemValueTotalW(bsItemValueTotalW);
		this.bsPlayerBidAvgW = bsPlayerBidAvgW;
		this.setBsPlayerBidTotalW(bsPlayerBidTotalW);
		this.bsOtherPlayerBidAvgW = bsOtherPlayerBidAvgW;
		this.bsOtherPlayerBidTotalW = bsOtherPlayerBidTotalW;
		this.bsAmountWonAvgW = bsAmountWonAvgW;
		this.bsAmountWonTotalW = bsAmountWonTotalW;
		this.bsItemValueAvgL = bsItemValueAvgL;
		this.setBsItemValueTotalL(bsItemValueTotalL);
		this.bsPlayerBidAvgL = bsPlayerBidAvgL;
		this.setBsPlayerBidTotalL(bsPlayerBidTotalL);
		this.bsOtherPlayerBidAvgL = bsOtherPlayerBidAvgL;
		this.bsOtherPlayerBidTotalL = bsOtherPlayerBidTotalL;
		this.bsAmountWonAvgL = bsAmountWonAvgL;
		this.bsAmountWonTotalL = bsAmountWonTotalL;
		this.itemValueW = itemValueW;
		this.playerBidW = playerBidW;
		this.otherPlayerBidW = otherPlayerBidW;
		this.amountWonW = amountWonW;
		this.itemValueL = itemValueL;
		this.playerBidL = playerBidL;
		this.otherPlayerBidL = otherPlayerBidL;
		this.amountWonL = amountWonL;
		this.percentage = 0;
	}

	public double getPercentage() {
		return percentage;
	}
	
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public int getBsPlayerBidTotalW() {
		return bsPlayerBidTotalW;
	}

	public void setBsPlayerBidTotalW(int bsPlayerBidTotalW) {
		this.bsPlayerBidTotalW = bsPlayerBidTotalW;
	}

	public int getBsPlayerBidTotalL() {
		return bsPlayerBidTotalL;
	}

	public void setBsPlayerBidTotalL(int bsPlayerBidTotalL) {
		this.bsPlayerBidTotalL = bsPlayerBidTotalL;
	}

	public int getBsItemValueTotalW() {
		return bsItemValueTotalW;
	}

	public void setBsItemValueTotalW(int bsItemValueTotalW) {
		this.bsItemValueTotalW = bsItemValueTotalW;
	}

	public int getBsItemValueTotalL() {
		return bsItemValueTotalL;
	}

	public void setBsItemValueTotalL(int bsItemValueTotalL) {
		this.bsItemValueTotalL = bsItemValueTotalL;
	}
}