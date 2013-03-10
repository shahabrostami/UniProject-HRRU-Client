package main;

public class TrustScore {
	private int playerGive;
	private int playerReturn;
	private int maxToGive;
	private int maxToReturn;
	private int playerGiveValue;
	private int playerReturnValue;
	private int multiplier;
	private int playerGiveProfit;
	private int playerReturnProfit;
	
	public TrustScore(int playerGive, int playerReturn, int maxToGive, int playerGiveValue, int playerReturnValue, int multiplier)
	{
		this.setPlayerGive(playerGive);
		this.setPlayerReturn(playerReturn);
		this.setMaxToGive(maxToGive);
		this.setPlayerGiveValue(playerGiveValue);
		this.setPlayerReturnValue(playerReturnValue);
		this.setMultiplier(multiplier);
	}
	public TrustScore(int playerGive, int playerReturn, int maxToGive, int maxToReturn, 
			int playerGiveValue, int playerReturnValue, int multiplier, int playerGiveProfit, int playerReturnProfit)
	{
		this.setPlayerGive(playerGive);
		this.setPlayerReturn(playerReturn);
		this.setMaxToGive(maxToGive);
		this.setMaxToReturn(maxToReturn);
		this.setPlayerGiveValue(playerGiveValue);
		this.setPlayerReturnValue(playerReturnValue);
		this.setMultiplier(multiplier);
		this.setPlayerGiveProfit(playerGiveProfit);
		this.setPlayerReturnProfit(playerReturnProfit);
	}

	public int getPlayerGive() {
		return playerGive;
	}

	public void setPlayerGive(int playerGive) {
		this.playerGive = playerGive;
	}

	public int getPlayerReturn() {
		return playerReturn;
	}

	public void setPlayerReturn(int playerReturn) {
		this.playerReturn = playerReturn;
	}

	public int getMaxToGive() {
		return maxToGive;
	}

	public void setMaxToGive(int maxToGive) {
		this.maxToGive = maxToGive;
	}

	public int getPlayerGiveValue() {
		return playerGiveValue;
	}

	public void setPlayerGiveValue(int playerGiveValue) {
		this.playerGiveValue = playerGiveValue;
	}

	public int getPlayerReturnValue() {
		return playerReturnValue;
	}

	public void setPlayerReturnValue(int playerReturnValue) {
		this.playerReturnValue = playerReturnValue;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}
	public int getMaxToReturn() {
		return maxToReturn;
	}
	public void setMaxToReturn(int maxToReturn) {
		this.maxToReturn = maxToReturn;
	}
	public int getPlayerGiveProfit() {
		return playerGiveProfit;
	}
	public void setPlayerGiveProfit(int playerGiveProfit) {
		this.playerGiveProfit = playerGiveProfit;
	}
	public int getPlayerReturnProfit() {
		return playerReturnProfit;
	}
	public void setPlayerReturnProfit(int playerReturnProfit) {
		this.playerReturnProfit = playerReturnProfit;
	}
}
	
	
