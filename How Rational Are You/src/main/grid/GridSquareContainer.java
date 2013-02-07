package main.grid;

import org.newdawn.slick.SlickException;

public class GridSquareContainer {
	private int id = 0;
	private int x;
	private int y;
	private int tile_type;
	public GridSquare gridSquare;
	
	public GridSquareContainer(int tileType, int x, int y) throws SlickException
	{
		this.setId(this.getId() + 1);
		this.x = x;
		this.y = y;
		this.tile_type = tileType;
		
		if(this.tile_type == GridSquare.normalTile)
		{
			gridSquare = new NormalGridSquare();
		}
		else if(this.tile_type == GridSquare.questionTile)
		{
			gridSquare = new QuestionGridSquare();
		}
		else if(this.tile_type == GridSquare.puzzleTile)
		{
			gridSquare = new PuzzleGridSquare();
		}
		else if(this.tile_type == GridSquare.gameTile)
		{
			gridSquare = new GameGridSquare();
		}
	}
	
	public void setx(int x) {
		this.x = x;
	}
	
	public void sety(int y) {
		this.y = y;
	}
	
	public int getx() {
		return x;
	}
	
	public int gety() {
		return y;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTileType() {
		return tile_type;
	}
}
