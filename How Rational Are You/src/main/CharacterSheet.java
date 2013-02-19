package main;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class CharacterSheet  {

	private int size;
	private int scale = 5;
	private Character characters[];
	private SpriteSheet characterSheet;
	
	public CharacterSheet() throws SlickException {
		size = scale*scale;
		this.characterSheet = new SpriteSheet("res/simple/characters.png", 36,36);
		characters = new Character[size];
		
		characters[0] = new Character(0, 0, 0, "Sir Clown");
		characters[1] = new Character(1, 1, 0, "Sir Clown");
		characters[2] = new Character(2, 2, 0, "Sir Clown");
		characters[3] = new Character(3, 3, 0, "Sir Clown");
		characters[4] = new Character(4, 4, 0, "Sir Clown");
		characters[5] = new Character(5, 0, 1, "Sir Snow");
		characters[6] = new Character(6, 1, 1, "Sir Snow");
		characters[7] = new Character(7, 2, 1, "Sir Snow");
		characters[8] = new Character(8, 3, 1, "Sir Snow");
		characters[9] = new Character(9, 4, 1, "Sir Snow");
		characters[10] = new Character(10, 0, 2, "Sir Bat");
		characters[11] = new Character(11, 1, 2, "Sir Bat");
		characters[12] = new Character(12, 2, 2, "Sir Bat");
		characters[13] = new Character(13, 3, 2, "Sir Bat");
		characters[14] = new Character(14, 4, 2, "Sir Bat");
		characters[15] = new Character(15, 0, 3, "Sir Panda");
		characters[16] = new Character(16, 1, 3, "Sir Panda");
		characters[17] = new Character(17, 2, 3, "Sir Panda");
		characters[18] = new Character(18, 3, 3, "Sir Panda");
		characters[19] = new Character(19, 4, 3, "Sir Panda");
		characters[20] = new Character(20, 0, 4, "Sir Ghost");
		characters[21] = new Character(21, 1, 4, "Sir Ghost");
		characters[22] = new Character(22, 2, 4, "Sir Ghost");
		characters[23] = new Character(23, 3, 4, "Sir Ghost");
		characters[24] = new Character(24, 4, 4, "Sir Ghost");
	}
	
	public Character[] getCharacters(){
		return characters;	
	}
	
	public int getSize(){
		return size-1;
	}
	
	public int getScale(){
		return scale;
	}
	
	public SpriteSheet getCharacterSheet() {
		return characterSheet;
	}
}