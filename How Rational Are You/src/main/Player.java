package main;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Player {
	
	SpriteSheet sheet;
	private String name;
	private int score;
	private int position;
	private int ready;
	private ActivityScore activityScore;
	private int playerCharacterID;
	private Character playerCharacter;
	CharacterSheet characterSheet = new CharacterSheet();
	Character[] characters = characterSheet.getCharacters();
	
	public Player(String name) throws SlickException
	{
		this.setName(name);
		this.setScore(0);
		this.setReady(0);
		this.setPosition(0);
		this.activityScore = new ActivityScore(0,0,0,0,0, false);
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
		if(this.position == 35)
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
		return activityScore;
	}
	
	public void setActivityScore(ActivityScore activityScore){
		this.activityScore = activityScore;
	}
	
}
