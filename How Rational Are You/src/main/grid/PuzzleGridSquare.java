package main.grid;

import org.newdawn.slick.*;

public class PuzzleGridSquare implements GridSquare{

	private int tile_type = puzzleTile;
	private Image tile_image;
	
	SpriteSheet sheet = new SpriteSheet("res/img/tileset/tileset.png", width,height);
	private Image puzzle_tile_image = sheet.getSprite(0,1);
	// private Image questionTileImage = new Image("res/img/tileset/DirtBlock.png");
	
	public PuzzleGridSquare() throws SlickException {
		this.tile_image = puzzle_tile_image;
		
	}
	
	public Image getImage()
	{
		return tile_image;
	}
}
