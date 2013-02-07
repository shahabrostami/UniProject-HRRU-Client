package main.grid;

import org.newdawn.slick.*;

public class NormalGridSquare implements GridSquare{
	
	private int tile_type = normalTile;
	private Image tile_image;
	
	SpriteSheet sheet = new SpriteSheet("res/img/tileset/tileset.png", width,height);
	private Image normal_tile_image = sheet.getSprite(0,0);
	
	// private Image normalTileImage = new Image ("/res/img/tileset/WoodBlock.png");
	
	public NormalGridSquare() throws SlickException {
		this.tile_image = normal_tile_image;
		
	}
	
	public Image getImage()
	{
		return tile_image;
	}

	public int getTileType() {
		return tile_type;
	}

}
