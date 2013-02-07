package main.grid;

import org.newdawn.slick.Image;

public interface GridSquare {

	public Image getImage();
	
	public static final int normalTile = 0;
	public static final int questionTile = 1;
	public static final int puzzleTile = 2;
	public static final int gameTile = 3;
	public static int width = 36;
	public static int height = 36;

}
