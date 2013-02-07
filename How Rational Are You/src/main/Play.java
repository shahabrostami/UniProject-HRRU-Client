package main;

import java.io.IOException;

import conn.*;
import conn.Packet.*;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

import com.esotericsoftware.kryonet.Client;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ColumnLayout.Panel;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.renderer.DynamicImage;
import main.board.*;
import main.grid.*;

public class Play extends BasicTWLGameState {

	public Client client;
	DialogLayout firstPanel;
	
	int gcw;
	int gch;
	
	private Board board;
	private Dice dice;
	private QuestionList question_list;
	static Player currentPlayer;
	private static Player player;
	
	public static final int playquestion = 8;
	public static final int puzzlequestion = 9;
	public static final int gamequestion = 10;
	
	// states: 0 = idle, 1 = rolling, 2 = navigate board
	private int state;
	private int dice_counter = 0;
	private int clock;
	
	String mouse = "no input";
	
	DialogLayout playerPanel;
	Label lStatus, lPlayer1, lPlayer2, lPlayer1Score, lPlayer2Score;
	Label lImgPlayer1, lImgPlayer2; 
	
	
	
	
	public Play(int main) {
		client = HRRUClient.conn.getClient();
	}

	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
        
		playerPanel = new DialogLayout();
		playerPanel.setSize(300,100);
		playerPanel.setTheme("login-panel");
		
		lStatus = new Label("Score:");
		lPlayer1 = new Label("Player1");
		lPlayer2 = new Label("Player2");
		lPlayer1Score = new Label("100");
		lPlayer2Score = new Label("200");
		lImgPlayer1 = new Label();
		lImgPlayer2 = new Label();
		
		lImgPlayer1.setTheme("labelsnowman");
		lImgPlayer2.setTheme("labelcat");
		
	    DialogLayout.Group hStatus = playerPanel.createSequentialGroup().addWidget(lStatus);
        DialogLayout.Group hPictures = playerPanel.createParallelGroup(lImgPlayer1, lImgPlayer2);
	    DialogLayout.Group hLabels = playerPanel.createParallelGroup(lPlayer1, lPlayer2);
        DialogLayout.Group hScores = playerPanel.createParallelGroup(lPlayer1Score, lPlayer2Score);
	        
	    playerPanel.setHorizontalGroup(playerPanel.createParallelGroup()
	        		.addGroup(hStatus)
	        		.addGroup(playerPanel.createSequentialGroup(hPictures, hLabels, hScores)));
	        
	      playerPanel.setVerticalGroup(playerPanel.createSequentialGroup()
	        		.addWidget(lStatus)
	        		.addGroup(playerPanel.createParallelGroup(lImgPlayer1, lPlayer1, lPlayer1Score))
	        		.addGroup(playerPanel.createParallelGroup(lImgPlayer2, lPlayer2, lPlayer2Score)));
		
		rootPane.add(playerPanel);
		rootPane.setTheme("");		
		resetPosition();
	}
	
	void resetPosition() {
		playerPanel.setPosition(0,0);
	}


	@Override
	protected RootPane createRootPane() {
		assert rootPane == null : "RootPane already created";

		RootPane rp = new RootPane(this);
		rp.setTheme("");
		rp.getOrCreateActionMap().addMapping(this);
		return rp;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gcw = gc.getWidth();
		gch = gc.getHeight();
		
		state = 0;
		clock = 0;
		
		board = new Board(10);
		dice = new Dice(1);
		player = new Player("player1");
		
		try {
			question_list = new QuestionList("Question.txt");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sbg.addState(new PlayQuestion(playquestion, question_list));
		sbg.addState(new PlayPuzzle(puzzlequestion));
		sbg.addState(new PlayGame(gamequestion));
		sbg.getState(playquestion).init(gc, sbg);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for(int i = 0; i < board.getScale()*3-3; i++)
			g.drawImage(board.gridSquares[i].gridSquare.getImage(), board.gridSquares[i].getx(), board.gridSquares[i].gety());
		
		for(int j = board.getSize()-1; j >= board.getScale()*3-3; j--)
			g.drawImage(board.gridSquares[j].gridSquare.getImage(), board.gridSquares[j].getx(), board.gridSquares[j].gety());

		
		if(state>0 && state < 3)
			dice.dice.draw(HRRUClient.resX - 100, HRRUClient.resY-250+dice.getY());
		
		
		g.drawImage(player.getPlayerImage(), board.gridSquares[player.getPosition()].getx(), board.gridSquares[player.getPosition()].gety());
		
		g.drawString("Roll!", 650, 470);
		g.drawString(mouse, 650, 500);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos= Mouse.getY();
		mouse = "xpos: " + xpos + "\nypos: " + ypos;
        
		// State Management
		if(state==0)
		{
			currentPlayer = player;
			if((xpos>650&&xpos<700)&&(ypos>110&&ypos<130))
			{
				if(input.isMouseButtonDown(0))
				{
					state = 1;
				}
			}
			if((xpos>0&&xpos<500)&&(ypos>110&&ypos<130))
			{
				if(input.isMouseButtonDown(0))
				{
					sbg.enterState(0);
				}
			}
		}
		
		// Dice State
		if(state==1)
		{
			clock += delta;
			if(clock>60)
			{
				dice.rollDice();
				clock-=60;
				if(dice.getPosition()==0)
				{
					dice_counter = dice.getCurrentNumber();
					state = 2;
				}
			}
		}
		
		// Navigating Board State
		if(state == 2)
		{
			clock += delta;
			if(clock>200)
			{
				if(player.getPosition() >= board.getSize()-1)
					player.setPosition(0);
				else player.updatePosition();
				clock-=200;
				dice_counter--;
				if(dice_counter==0)
				{
					dice.reset();
					state = 3;
				}
				
			}
		}
		
		if(state ==3)
		{
			clock += delta;
			if(clock>2000)
			{
				int currentTile = board.gridSquares[player.getPosition()].getTileType();
				if(currentTile == 1)	
				{
					state=0;
					sbg.enterState(8, null, new HorizontalSplitTransition());
				}
				else if(currentTile == 2)	
				{
					sbg.enterState(9);
				}
				else if(currentTile == 3)
				{
					sbg.enterState(10);
				}
				else state = 0;
				clock-=2000;
			}
		}
	}

	@Override
	public int getID() {
		return 5;
	}

}