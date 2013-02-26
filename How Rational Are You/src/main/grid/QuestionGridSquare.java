package main.grid;

import org.newdawn.slick.*;

public class QuestionGridSquare implements GridSquare{

	private Image tile_image;

	SpriteSheet sheet = new SpriteSheet("img/tileset/tile2.png", width,height);
	private Image question_tile_image = sheet.getSprite(0,0);
	// private Image questionTileImage = new Image("res/img/tileset/DirtBlock.png");
	
	public QuestionGridSquare() throws SlickException {
		this.tile_image = question_tile_image;
		
	}
	
	public Image getImage()
	{
		return tile_image;
	}
}
