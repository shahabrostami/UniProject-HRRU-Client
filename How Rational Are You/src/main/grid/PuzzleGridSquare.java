package main.grid;

import org.newdawn.slick.*;

public class PuzzleGridSquare implements GridSquare{

	private Image tile_image;
	
	SpriteSheet sheet = new SpriteSheet("img/tileset/tile2.png", width,height);
	private Image puzzle_tile_image = sheet.getSprite(1,0);
	// private Image questionTileImage = new Image("res/img/tileset/DirtBlock.png");
	
	public PuzzleGridSquare() throws SlickException {
		this.tile_image = puzzle_tile_image;
		
	}
	
	public Image getImage()
	{
		return tile_image;
	}
}
