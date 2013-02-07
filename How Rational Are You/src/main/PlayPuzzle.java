package main;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import org.lwjgl.input.Mouse;

import TWLSlick.BasicTWLGameState;

import java.awt.Font;

public class PlayPuzzle extends BasicTWLGameState{
	
	public PlayPuzzle(int state){
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		
	} 
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		g.drawString("Puzzle " + Play.currentPlayer.getName(), 50, 50);
	} // render
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{

	} // main
	
	public int getID(){
		return 9;
	}
	
}
