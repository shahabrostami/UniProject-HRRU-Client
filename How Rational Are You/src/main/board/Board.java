package main.board;

import main.HRRUClient;
import main.grid.*;

import org.newdawn.slick.SlickException;

public class Board {
	private int size;
	private int scale;
	
	private int maxxpos;
	private int maxypos;
	private int shiftx;
	private int shifty;
	private int middlex = HRRUClient.resX/2 + 150;
	private int middley = HRRUClient.resY/2 - 50;
	
	public GridSquareContainer[] gridSquares;
	private int counter = 0;
	private double tile_random_number = 0;
	
	public Board(int scale) throws SlickException
	{
		this.size = (scale*4)-4;
		this.setScale(scale);
		this.maxxpos = GridSquare.width * (scale-1);
		this.maxypos = GridSquare.height * (scale-1);
		
		int tempxpos = 0;
		int tempypos = 0;
		
		shiftx = middlex - ((scale*GridSquare.width)/2);
		shifty = middley -((scale*GridSquare.height)/2);
		
		
		gridSquares = new GridSquareContainer[size];
		
		int[] tileOrder = new int[size];
		
		tileOrder[0] = 0;
		
		
		// calculate order of tiletypes
		for(int i = 0; i < size; i++)
		{
			tile_random_number = Math.random();
			if(tile_random_number <= 0.2)
				tileOrder[i] = 1;
			else if(tile_random_number <= 0.4)
				tileOrder[i] = 2;
			else if(tile_random_number <= 0.6)
				tileOrder[i] = 3;
			else tileOrder[i] = 0;
		}
		
		// create board grid
		for(int row1 = 0; row1 < scale-1; row1++)
		{
			gridSquares[counter] = new GridSquareContainer(tileOrder[counter], shiftx+tempxpos, shifty);
			tempxpos+=GridSquare.width;
			counter++;
		}
		
		for(int row2 = 0; row2 < scale-1; row2++)
		{
			gridSquares[counter] = new GridSquareContainer(tileOrder[counter], shiftx+tempxpos, shifty+tempypos);
			tempypos+=GridSquare.height;
			counter++;
		}
		
		for(int row3 = 0; row3 < scale-1; row3++)
		{
			gridSquares[counter] = new GridSquareContainer(tileOrder[counter], shiftx+tempxpos, shifty+tempypos);
			tempxpos-=GridSquare.width;
			counter++;
		}
		
		for(int row4 = 0; row4 < scale-1; row4++)
		{
			gridSquares[counter] = new GridSquareContainer(tileOrder[counter], shiftx, shifty+tempypos);
			tempypos-=GridSquare.height;
			counter++;
		}
	
		
	}
	
	public GridSquareContainer[] getGridSquares()
	{
		return this.gridSquares;
	}
	
	public int getCounter() 
	{
		return this.counter;
	}
	
	public int getSize() 
	{
		return this.size;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

}
