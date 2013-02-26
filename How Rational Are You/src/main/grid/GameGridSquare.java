package main.grid;

import org.newdawn.slick.*;

public class GameGridSquare implements GridSquare{

	private Image tile_image;
	
	SpriteSheet sheet = new SpriteSheet("img/tileset/tile2.png", width,height);
	private Image game_tile_image = sheet.getSprite(2,0);
	// private Image questionTileImage = new Image("res/img/tileset/DirtBlock.png");

	
	public GameGridSquare() throws SlickException {
		this.tile_image = game_tile_image;
		
	}
	
	public Image getImage()
	{
		return tile_image;
	}
}
