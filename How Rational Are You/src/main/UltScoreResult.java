package main;

import main.item.Item;

public class UltScoreResult {

	private int noOfUltimatumScores;
	private int noOfUltimatumScoreProp, noOfUltimatumScoreDec;
	private int noOfPropSuccess, noOfDecSuccess;
	private int usOverallValueAvgP, usPlayerPropAvgP, usPlayerDecAvgP;
	private int usOverallValueAvgD, usPlayerPropAvgD, usPlayerDecAvgD;
	private double usOverallValueTotalP, usPlayerPropTotalP, usPlayerDecTotalP;
	private double usOverallValueTotalD, usPlayerPropTotalD, usPlayerDecTotalD;
	private int usAvg, usTotal;
	private double percentage;
	
	public UltScoreResult(
			int noOfUltimatumScores,
			int noOfUltimatumScoreProp, 
			int noOfUltimatumScoreDec,
			int noOfPropSuccess, 
			int noOfDecSuccess,
			int usOverallValueAvgP, 
			int usPlayerPropAvgP, 
			int usPlayerDecAvgP,
			int usOverallValueAvgD, 
			int usPlayerPropAvgD, 
			int usPlayerDecAvgD,
			double usOverallValueTotalP, 
			double usPlayerPropTotalP2, 
			double usPlayerDecTotalP2,
			double usOverallValueTotalD, 
			double usPlayerPropTotalD2, 
			double usPlayerDecTotalD2,
			int usAvg, 
			int usTotal)
	{
		this.noOfUltimatumScores = noOfUltimatumScores;
		this.noOfUltimatumScoreProp = noOfUltimatumScoreProp; 
		this.noOfUltimatumScoreDec = noOfUltimatumScoreDec;
		this.noOfPropSuccess = noOfPropSuccess;
		this.noOfDecSuccess = noOfDecSuccess;
		this.usOverallValueAvgP = usOverallValueAvgP;
		this.usPlayerPropAvgP = usPlayerPropAvgP;
		this.usPlayerDecAvgP = usPlayerDecAvgP;
		this.usOverallValueAvgD = usOverallValueAvgD;
		this.usPlayerPropAvgD = usPlayerPropAvgD;
		this.usPlayerDecAvgD = usPlayerDecAvgD;
		this.usOverallValueTotalP = usOverallValueTotalP;
		this.usPlayerPropTotalP = usPlayerPropTotalP2;
		this.usPlayerDecTotalP = usPlayerDecTotalP2;
		this.usOverallValueTotalD = usOverallValueTotalD;
		this.usPlayerPropTotalD = usPlayerPropTotalD2;
		this.usPlayerDecTotalD = usPlayerDecTotalD2;
		this.usAvg = usAvg;
		this.usTotal = usTotal;
		this.percentage = 0;
	}
	public double getPercentage() {
		return percentage;
	}
	
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
}
