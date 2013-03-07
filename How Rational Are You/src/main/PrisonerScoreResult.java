package main;

import main.item.Item;

public class PrisonerScoreResult {

	private int noOfPrisonScores;
	private int noOfPrisonScoreCoop;
	private int noOfPrisonScoreBetray;
	private int noOfPrisonScoreCC, noOfPrisonScoreCB, noOfPrisonScoreBC, noOfPrisonScoreBB;
	private int psPlayerProfitCC, psOtherPlayerProfitCC;
	private int psPlayerProfitBC, psOtherPlayerProfitBC;
	private int psPlayerProfitCB, psOtherPlayerProfitCB;
	private int psPlayerProfitBB, psOtherPlayerProfitBB;
	private int psCAvg, psAvg, psBAvg;
	private double psTotal, psCTotal, psBTotal;
	private int pointsAvailable;
	
	private double percentage;
	
	public PrisonerScoreResult(
			int noOfPrisonScores,
			int noOfPrisonScoreCoop,
			int noOfPrisonScoreBetray,
			int noOfPrisonScoreCC, 
			int noOfPrisonScoreCB, 
			int noOfPrisonScoreBC, 
			int noOfPrisonScoreBB,
			int psPlayerProfitCC, 
			int psOtherPlayerProfitCC,
			int psPlayerProfitBC,  
			int psOtherPlayerProfitBC,
			int psPlayerProfitCB,  
			int psOtherPlayerProfitCB,
			int psPlayerProfitBB,  
			int psOtherPlayerProfitBB,
			int psCAvg,  
			int psAvg,  
			int psBAvg,
			double psTotal, 
			double psCTotal, 
			double psBTotal)
	{
				this.setNoOfPrisonScores(noOfPrisonScores);
				this.setNoOfPrisonScoreCoop(noOfPrisonScoreCoop);
				this.setNoOfPrisonScoreBetray(noOfPrisonScoreBetray);
				this.noOfPrisonScoreCC =  noOfPrisonScoreCC;
				this.noOfPrisonScoreCB =  noOfPrisonScoreCB;
				this.noOfPrisonScoreBC =  noOfPrisonScoreBC;
				this.noOfPrisonScoreBB = noOfPrisonScoreBB;
				this.psPlayerProfitCC =  psPlayerProfitCC;
				this.psOtherPlayerProfitCC = psOtherPlayerProfitCC;
				this.psPlayerProfitBC =  psPlayerProfitBC;
				this.psOtherPlayerProfitBC = psOtherPlayerProfitBC;
				this.psPlayerProfitCB = psPlayerProfitCB;
				this.psOtherPlayerProfitCB = psOtherPlayerProfitCB;
				this.psPlayerProfitBB =   psPlayerProfitBB;
				this.psOtherPlayerProfitBB = psOtherPlayerProfitBB;
				this.psCAvg = psCAvg;
				this.psAvg = psAvg;
				this.psBAvg = psBAvg;
				this.psTotal =  psTotal;
				this.psCTotal = psCTotal;
				this.psBTotal = psBTotal;
				this.percentage = 0;
				this.setPointsAvailable((noOfPrisonScores * 150));
	}
	public double getPercentage() {
		return percentage;
	}
	
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	public int getNoOfPrisonScoreCoop() {
		return noOfPrisonScoreCoop;
	}
	public int setNoOfPrisonScoreCoop(int noOfPrisonScoreCoop) {
		this.noOfPrisonScoreCoop = noOfPrisonScoreCoop;
		return noOfPrisonScoreCoop;
	}
	public int getNoOfPrisonScores() {
		return noOfPrisonScores;
	}
	public void setNoOfPrisonScores(int noOfPrisonScores) {
		this.noOfPrisonScores = noOfPrisonScores;
	}
	public int getPointsAvailable() {
		return pointsAvailable;
	}
	public void setPointsAvailable(int pointsAvailable) {
		this.pointsAvailable = pointsAvailable;
	}
	public int getNoOfPrisonScoreBetray() {
		return noOfPrisonScoreBetray;
	}
	public void setNoOfPrisonScoreBetray(int noOfPrisonScoreBetray) {
		this.noOfPrisonScoreBetray = noOfPrisonScoreBetray;
	}
}

