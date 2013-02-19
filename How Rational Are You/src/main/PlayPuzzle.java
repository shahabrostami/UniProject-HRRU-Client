package main;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import TWLSlick.BasicTWLGameState;

public class PlayPuzzle extends BasicTWLGameState{
	
	public PlayPuzzle(int state){
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		
	} 
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		g.drawString("Puzzle  playername", 50, 50);
	} // render
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{

	} // main
	
	public int getID(){
		return 9;
	}
	
}
