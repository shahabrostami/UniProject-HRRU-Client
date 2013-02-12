package main;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import org.lwjgl.input.Mouse;

import TWLSlick.BasicTWLGameState;

import java.awt.Font;

public class PlayGame extends BasicTWLGameState{
	
	public PlayGame(int state){
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		
	} 
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		g.drawString("Game  playername", 50, 50);
	} // render
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{

	} // main
	
	public int getID(){
		return 10;
	}
	
}
