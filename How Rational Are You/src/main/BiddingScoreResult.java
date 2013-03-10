package main;

public class BiddingScoreResult {

	double bsOtherPlayerBidTotalW;
	double bsPlayerBidTotalW;
	double bsItemValueTotalW;
	double bsAmountWonTotalW;
	double bsAmountWonTotalL;
	double bsOtherPlayerBidTotalL;
	double bsItemValueTotalL;
	double bsPlayerBidTotalL;
	private double percentage;
	private double pointsAvailable;
	
	public BiddingScoreResult(
			int noOfBidScores,
			int noOfBidScoreWin,
			int noOfBidScoreLose,
			int bsItemValueAvgW, 
			double bsItemValueTotalW2, 
			int bsPlayerBidAvgW, 
			double bsPlayerBidTotalW2, 
			int bsOtherPlayerBidAvgW, 
			double bsOtherPlayerBidTotalW2, 
			int bsAmountWonAvgW, 
			double bsAmountWonTotalW2,
			int bsItemValueAvgL, 
			double bsItemValueTotalL2, 
			int bsPlayerBidAvgL, 
			double bsPlayerBidTotalL2, 
			int bsOtherPlayerBidAvgL, 
			double bsOtherPlayerBidTotalL2, 
			int bsAmountWonAvgL, 
			double bsAmountWonTotalL2,
			int itemValueW, 
			int playerBidW, 
			int otherPlayerBidW, 
			int amountWonW,
			int itemValueL, 
			int playerBidL, 
			int otherPlayerBidL,
			int amountWonL)
	{
		this.setBsItemValueTotalW(bsItemValueTotalW2);
		this.setBsPlayerBidTotalW(bsPlayerBidTotalW2);
		this.bsOtherPlayerBidTotalW = bsOtherPlayerBidTotalW2;
		this.setBsAmountWonTotalW(bsAmountWonTotalW2);
		this.setBsItemValueTotalL(bsItemValueTotalL2);
		this.setBsPlayerBidTotalL(bsPlayerBidTotalL2);
		this.bsOtherPlayerBidTotalL = bsOtherPlayerBidTotalL2;
		this.bsAmountWonTotalL = bsAmountWonTotalL2;
		this.percentage = 0;
		this.setPointsAvailable((bsItemValueTotalL2 + bsItemValueTotalW2)/2);
	}

	public double getPercentage() {
		return percentage;
	}
	
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getBsPlayerBidTotalW() {
		return bsPlayerBidTotalW;
	}

	public void setBsPlayerBidTotalW(double bsPlayerBidTotalW2) {
		this.bsPlayerBidTotalW = bsPlayerBidTotalW2;
	}

	public double getBsPlayerBidTotalL() {
		return bsPlayerBidTotalL;
	}

	public void setBsPlayerBidTotalL(double bsPlayerBidTotalL2) {
		this.bsPlayerBidTotalL = bsPlayerBidTotalL2;
	}

	public double getBsItemValueTotalW() {
		return bsItemValueTotalW;
	}

	public void setBsItemValueTotalW(double bsItemValueTotalW2) {
		this.bsItemValueTotalW = bsItemValueTotalW2;
	}

	public double getBsItemValueTotalL() {
		return bsItemValueTotalL;
	}

	public void setBsItemValueTotalL(double bsItemValueTotalL2) {
		this.bsItemValueTotalL = bsItemValueTotalL2;
	}

	public double getPointsAvailable() {
		return pointsAvailable;
	}

	public void setPointsAvailable(double d) {
		this.pointsAvailable = d;
	}

	public double getBsAmountWonTotalW() {
		return bsAmountWonTotalW;
	}

	public void setBsAmountWonTotalW(double bsAmountWonTotalW2) {
		this.bsAmountWonTotalW = bsAmountWonTotalW2;
	}
}