package main;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Player {
	
	SpriteSheet sheet;
	private String name;
	private int score;
	private int position;
	private Image playerImage;
	
	public Player(String name) throws SlickException
	{
		this.setName(name);
		this.setScore(0);
		this.setPosition(0);
		sheet = new SpriteSheet("res/simple/characters.png", 36,36);
		setPlayerImage(sheet.getSprite(0,0));
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
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Image getPlayerImage() {
		return playerImage;
	}

	public void setPlayerImage(Image playerImage) {
		this.playerImage = playerImage;
	}

	public void addScore(int i) {
		this.score += i;
	}
}
