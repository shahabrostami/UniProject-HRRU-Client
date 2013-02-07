package main.grid;

import org.newdawn.slick.*;

public class GameGridSquare implements GridSquare{

	private int tile_type = gameTile;
	private Image tile_image;
	
	SpriteSheet sheet = new SpriteSheet("res/img/tileset/tileset.png", width,height);
	private Image game_tile_image = sheet.getSprite(1,1);
	// private Image questionTileImage = new Image("res/img/tileset/DirtBlock.png");

	
	public GameGridSquare() throws SlickException {
		this.tile_image = game_tile_image;
		
	}
	
	public Image getImage()
	{
		return tile_image;
	}
}
