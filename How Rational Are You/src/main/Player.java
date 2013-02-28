package main;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Player {
	
	SpriteSheet sheet;
	private String name;
	private int score;
	private int position;
	private int ready;
	private int playerCharacterID;
	private Character playerCharacter;
	CharacterSheet characterSheet = new CharacterSheet();
	Character[] characters = characterSheet.getCharacters();
	
	private ActivityScore currentActivityScore;
	private ArrayList<ActivityScore> activityScores;
	
	private BiddingScore currentBiddingScore;
	private ArrayList<BiddingScore> biddingScores;
	
	private TrustScore currentTrustScore;
	private ArrayList<TrustScore> trustScores;
	
	public Player(String name) throws SlickException
	{
		this.setName(name);
		this.setScore(0);
		this.setReady(0);
		this.setPosition(0);
		this.currentActivityScore = new ActivityScore(0,0,0,0,0, false);
		this.activityScores = new ArrayList<ActivityScore>();
		this.currentBiddingScore = new BiddingScore(0,0,0, false);
		this.biddingScores = new ArrayList<BiddingScore>();
		this.setCurrentTrustScore(new TrustScore(0,0,0,0,0,0));
		this.trustScores = new ArrayList<TrustScore>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPosition() {
		return position;
	}
	
	public void updatePosition(){
		this.position++;
		if(this.position == 36)
			this.position = 0;
	}

	public void setPosition(int position) {
		this.position += position;
	}

	public Character getPlayerCharacter() {
		return playerCharacter;
	}
	
	public void setPlayerCharacter(Character playerCharacter) {
		this.playerCharacter = playerCharacter;
	}
	
	public int getPlayerCharacterID() {
		return playerCharacterID;
	}
	
	public void setPlayerCharacterID(int playerCharacterID) {
		this.playerCharacterID = playerCharacterID;
		setPlayerCharacter(characters[playerCharacterID]);
	}
	
	public void addScore(int i) {
		this.score += i;
	}

	public int getReady() {
		return ready;
	}

	public void setReady(int ready) {
		this.ready = ready;
	}

	public ActivityScore getActivityScore(){
		return currentActivityScore;
	}
	
	public void setActivityScore(ActivityScore activityScore){
		this.currentActivityScore = activityScore;
	}

	public ArrayList<ActivityScore> getActivityScores() {
		return activityScores;
	}

	public void setActivityScores(ArrayList<ActivityScore> activityScores) {
		this.activityScores = activityScores;
	}
	
	public void addActivityScore(ActivityScore activityScore) {
		this.activityScores.add(activityScore);
	}

	public BiddingScore getCurrentBiddingScore() {
		return currentBiddingScore;
	}

	public void setCurrentBiddingScore(BiddingScore currentBiddingScore) {
		this.currentBiddingScore = currentBiddingScore;
	}

	public ArrayList<BiddingScore> getBiddingScores() {
		return biddingScores;
	}

	public void setBiddingScores(ArrayList<BiddingScore> biddingScores) {
		this.biddingScores = biddingScores;
	}

	public void addBiddingScore(BiddingScore biddingScore) {
		this.biddingScores.add(biddingScore);
	}

	
	public ArrayList<TrustScore> getTrustScores() {
		return trustScores;
	}

	
	public void setTrustScores(ArrayList<TrustScore> trustScores) {
		this.trustScores = trustScores;
	}

	public void addTrustScore(TrustScore trustScore) {
		this.trustScores.add(trustScore);
	}

	public TrustScore getCurrentTrustScore() {
		return currentTrustScore;
	}

	public void setCurrentTrustScore(TrustScore currentTrustScore) {
		this.currentTrustScore = currentTrustScore;
	}
}
