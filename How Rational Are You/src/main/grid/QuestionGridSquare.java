package main.grid;

import org.newdawn.slick.*;

public class QuestionGridSquare implements GridSquare{

	private Image tile_image;

	SpriteSheet sheet = new SpriteSheet("img/tileset/tile2.png", width,height);
	private Image easy_tile_image = sheet.getSprite(4,0);
	private Image medium_tile_image = sheet.getSprite(6,0);
	private Image hard_tile_image = sheet.getSprite(5,0);
	// private Image questionTileImage = new Image("res/img/tileset/DirtBlock.png");
	
	public QuestionGridSquare(int difficulty) throws SlickException {
		if(difficulty == 0)
			this.tile_image = easy_tile_image;
		else if(difficulty == 1)
			this.tile_image = medium_tile_image;
		else if (difficulty == 2)
			this.tile_image = hard_tile_image;	
	}
	
	public Image getImage()
	{
		return tile_image;
	}
}
