package main;

import main.item.Item;

public class TrustScoreResult {

	private int noOfTrustScores;
	private int noOfTrustScoreGiver;
	private int noOfTrustScoreReturner;
	private int tsPlayerGiveAvg, tsPlayerReturnAvg, tsPlayerReceiveAvg, tsPlayerGiveProfitAvg, tsPlayerReturnProfitAvg;
	private double tsPlayerGiveTotal, tsPlayerReturnTotal, tsPlayerReceiveTotal, tsPlayerGiveProfitTotal, tsPlayerReturnProfitTotal;
	private int tsAvg;
	private double tsTotal, percentage;
	private int pointsAvailable;
	
	public TrustScoreResult(
			int noOfTrustScores,
			int noOfTrustScoreGiver,
			int noOfTrustScoreReturner,
			int tsPlayerGiveAvg, int  tsPlayerReturnAvg, int  tsPlayerReceiveAvg, int  tsPlayerGiveProfitAvg, int  tsPlayerReturnProfitAvg,
			double tsPlayerGiveTotal, double  tsPlayerReturnTotal2, double  tsPlayerReceiveTotal2, double  tsPlayerGiveProfitTotal2, double  tsPlayerReturnProfitTotal2,
			int tsAvg,
			double tsTotal)
	{
		this.noOfTrustScores = noOfTrustScores;
		this.noOfTrustScoreGiver = noOfTrustScoreGiver;
		this.noOfTrustScoreReturner = noOfTrustScoreReturner;
		this.tsPlayerGiveAvg =  tsPlayerGiveAvg;
		this.tsPlayerReturnAvg =  tsPlayerReturnAvg;
		this.tsPlayerReceiveAvg = tsPlayerReceiveAvg ;
		this.tsPlayerGiveProfitAvg = tsPlayerGiveProfitAvg; 
		this.tsPlayerReturnProfitAvg = tsPlayerReturnProfitAvg;
		this.tsPlayerGiveTotal =  tsPlayerGiveTotal;
		this.tsPlayerReturnTotal =  tsPlayerReturnTotal2;
		this.tsPlayerReceiveTotal =  tsPlayerReceiveTotal2;
		this.tsPlayerGiveProfitTotal =  tsPlayerGiveProfitTotal2;
		this.tsPlayerReturnProfitTotal = tsPlayerReturnProfitTotal2;
		this.tsAvg = tsAvg;
		this.setTsTotal(tsTotal);
		this.percentage = 0;
	}
	public double getPercentage() {
		return percentage;
	}
	
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
	public int getPointsAvailable() {
		return pointsAvailable;
	}
	
	public void setPointsAvailable(int pointsAvailable) {
		this.pointsAvailable = pointsAvailable;
	}
	public double getTsTotal() {
		return tsTotal;
	}
	public void setTsTotal(double tsTotal) {
		this.tsTotal = tsTotal;
	}
}
