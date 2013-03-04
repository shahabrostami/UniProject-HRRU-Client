package main;

import main.item.Item;

public class UltimatumScore {
	private int playerProp;
	private int playerDec;
	private int playerPropValue;
	private int playerDecValue;
	private int overallValue;
	private boolean success;
	
	public UltimatumScore(int playerProp, int playerDec, int playerPropValue, int playerDecValue, int overallValue, boolean success)
	{
		this.setPlayerProp(playerProp);
		this.setPlayerDec(playerDec);
		this.setPlayerPropValue(playerPropValue);
		this.setPlayerDecValue(playerDecValue);
		this.setOverallValue(overallValue);
		this.setSuccess(success);
	}

	public int getPlayerProp() {
		return playerProp;
	}

	public void setPlayerProp(int playerProp) {
		this.playerProp = playerProp;
	}

	public int getPlayerDec() {
		return playerDec;
	}

	public void setPlayerDec(int playerDec) {
		this.playerDec = playerDec;
	}

	public int getPlayerPropValue() {
		return playerPropValue;
	}

	public void setPlayerPropValue(int playerPropValue) {
		this.playerPropValue = playerPropValue;
	}

	public int getPlayerDecValue() {
		return playerDecValue;
	}

	public void setPlayerDecValue(int playerDecValue) {
		this.playerDecValue = playerDecValue;
	}

	public int getOverallValue() {
		return overallValue;
	}

	public void setOverallValue(int overallValue) {
		this.overallValue = overallValue;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
	
	
