package main;

import java.awt.Font;
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
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ColumnLayout.Panel;
import de.matthiasmann.twl.ResizableFrame.ResizableAxis;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.renderer.DynamicImage;
import main.Chat.ChatFrame;
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
	private int playerID;
	private boolean currentPlayer, sendPosition;
	private Player player;
	private Player player1;
	private Player player2;
	
	private final int p1_turn = 7;
	private final int p2_turn = 8;
	
	public static final int playquestion = 8;
	public static final int puzzlequestion = 9;
	public static final int gamequestion = 10;
	
	// states: 0 = idle, 1 = rolling, 2 = navigate board
	private int state, gameState;
	private int dice_counter = 0;
	private int clock;
	
	String mouse = "no input";
	
	DialogLayout playerPanel;
	Label lStatus, lPlayer1, lPlayer2, lPlayer1Score, lPlayer2Score;
	Label player1turn;
	Label lImgPlayer1, lImgPlayer2; 
	Button btnRoll;
	DialogLayout rollPanel;
	BasicFont header; 
	
	public static ChatFrame chatFrame;
	
	Image scorebackground, background;
	
	Packet11TurnMessage turnMessage;
	
	public Play(int main) {
		client = HRRUClient.conn.getClient();
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		
		state = 0;
		clock = 0;

		playerID = HRRUClient.cs.getPlayer();
		player1 = HRRUClient.cs.getP1();
		player2 = HRRUClient.cs.getP2();
		
		board = new Board(10);
		
		if(HRRUClient.cs.getPlayer() == 1)
		{
			btnRoll.setEnabled(true);
			currentPlayer = true;
			player = HRRUClient.cs.getP1();
		}
		else {
			btnRoll.setEnabled(false);
			currentPlayer = false;
			player = HRRUClient.cs.getP2();
		}
		
		chatFrame = new ChatFrame();
        chatFrame.setSize(296, 200);
        chatFrame.setDraggable(false);
        chatFrame.setResizableAxis(ResizableAxis.NONE);
		
        lStatus.setText(player1.getName() + ", it's your turn!");
        lStatus.setPosition(305, 0);
        lStatus.setTheme("scoreatarititle");
        lStatus.setSize(495, 106);
        
		rollPanel.setTheme("roll-panel");
		rollPanel.setPosition(0,105);
		rollPanel.setSize(304, 270);
		btnRoll.setTheme("rollbutton");
		
		rootPane.removeAllChildren();
		rootPane.add(rollPanel);
		rootPane.add(lStatus);
		rootPane.add(chatFrame);
		resetPosition();
		
		turnMessage = new Packet11TurnMessage();
	}
	
	void resetPosition() {
        chatFrame.setPosition(0, 397);
	}

	@Override
	protected RootPane createRootPane() {
		assert rootPane == null : "RootPane already created";

		RootPane rp = new RootPane(this);
		rp.setTheme("");
		rp.getOrCreateActionMap().addMapping(this);
		return rp;
	}

	void emulateRoll() {
		state = 1;
		btnRoll.setEnabled(false);
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// Game 
		CharacterSheet characterSheet = new CharacterSheet();
		Character[] characters = characterSheet.getCharacters();

		/*
		Player player1 = new Player("player1");
		Player player2 = new Player("player2");
		player1.setPlayerCharacterID(2);
		player1.setPlayerCharacter(characters[2]);
		player2.setPlayerCharacter(characters[4]);
		player2.setPlayerCharacterID(4);
		player1.setScore(5000);
		HRRUClient.cs.setState(6);
		HRRUClient.cs.setP1(player1);
		HRRUClient.cs.setP2(player2);
		HRRUClient.cs.setPlayer(1);
		*/
		
		gcw = gc.getWidth();
		gch = gc.getHeight();
		scorebackground = new Image("res/simple/playerscorebackground.png");
		background = new Image("res/simple/background.png");
		
		header = new BasicFont("Atari Font Full Version", Font.PLAIN, 12);
		
		try {
			question_list = new QuestionList("Question.txt");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dice = new Dice(1);
		
		rollPanel = new DialogLayout();
		lStatus = new Label("");
		btnRoll = new Button("ROLL");
		btnRoll.addCallback(new Runnable() {
            public void run() {
                emulateRoll();
            }
        });
		
        DialogLayout.Group hBtnRoll = rollPanel.createSequentialGroup(btnRoll);
        
        rollPanel.setHorizontalGroup(rollPanel.createParallelGroup()
                .addGroup(hBtnRoll));
        
        rollPanel.setVerticalGroup(rollPanel.createSequentialGroup()
        		.addGap(180).addWidget(btnRoll));
		
		sbg.addState(new PlayQuestion(playquestion, question_list));
		sbg.addState(new PlayPuzzle(puzzlequestion));
		sbg.addState(new PlayGame(gamequestion));
		sbg.getState(playquestion).init(gc, sbg);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(background, 0,0);
		for(int i = 0; i < board.getScale()*3-3; i++)
			g.drawImage(board.gridSquares[i].gridSquare.getImage(), board.gridSquares[i].getx(), board.gridSquares[i].gety());
		
		for(int j = board.getSize()-1; j >= board.getScale()*3-3; j--)
			g.drawImage(board.gridSquares[j].gridSquare.getImage(), board.gridSquares[j].getx(), board.gridSquares[j].gety());
		
		g.drawImage(scorebackground, 0,0);
		
		g.drawImage(player1.getPlayerCharacter().getCharacterImage(), 13,13);
		g.drawImage(player2.getPlayerCharacter().getCharacterImage(), 13,55);
		g.setFont(header.get());
		g.drawString("" + player1.getName(), 70, 30);
		g.drawString("" + player2.getName(), 70, 70);
		g.drawString("" + player1.getScore(), 200, 30);
		g.drawString("" + player2.getScore(), 200, 70);

		g.drawImage(player1.getPlayerCharacter().getCharacterImage(), board.gridSquares[player1.getPosition()].getx(), board.gridSquares[player1.getPosition()].gety());
		g.drawImage(player2.getPlayerCharacter().getCharacterImage(), board.gridSquares[player2.getPosition()].getx(), board.gridSquares[player2.getPosition()].gety());
		
		g.drawString(mouse, 700, 550);
		
		g.scale(1.25f, 1.25f);
		if(state>0 && state < 3)
			dice.dice.draw(105, 97+dice.getY());
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos= Mouse.getY();
		mouse = "xpos: " + xpos + "\nypos: " + ypos;
		
		gameState = HRRUClient.cs.getState();
		
		if(state == 0)
		{
			if(playerID == 1)
			{
				if(gameState == p1_turn)
				{
					lStatus.setText(player1.getName() + ", it's your turn!");
					currentPlayer = true;
					btnRoll.setText("ROLL");
					btnRoll.setEnabled(true);
				}
				else
				{
					lStatus.setText("It's " + player2.getName() + "'s turn.");
					currentPlayer = false;
					btnRoll.setText("WAITING FOR PLAYER2");
					btnRoll.setEnabled(false);
				}
			}
			else if(playerID == 2)
			{
				if(gameState == p2_turn)
				{
					btnRoll.setText("ROLL");
					lStatus.setText(player2.getName() + ", it's your turn!");
					currentPlayer = true;
					btnRoll.setEnabled(true);
				}
				else
				{
					lStatus.setText("It's " + player1.getName() + "'s turn.");
					currentPlayer = false;
					btnRoll.setText("WAITING FOR PLAYER2");
					btnRoll.setEnabled(false);
				}
			}
		}
		
		if(currentPlayer)
		{
			//roll dice
			if(state==1)
			{
				clock += delta;
				if(clock>=60)
				{
					dice.rollDice();
					clock-=60;
					if(dice.getPosition()==0)
					{
						dice_counter = dice.getCurrentNumber();
						state = 2;
						sendPosition = true;
					}
				}
			}
			if(sendPosition)
			{
				turnMessage.sessionID = HRRUClient.cs.getSessionID();
				turnMessage.playerID = playerID;
				turnMessage.moves = dice_counter;
				client.sendTCP(turnMessage);
				sendPosition = false;
			}
			
			//navigating board
			if(state == 2)
			{
				clock += delta;
				if(clock>=200)
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
			if(state == 3)
			{
				if(playerID == 1)
					HRRUClient.cs.setState(p2_turn);
				else
					HRRUClient.cs.setState(p1_turn);
				btnRoll.setText("WAITING FOR PLAYER2");
				btnRoll.setEnabled(false);
				state = 0;
			}
			/*
			if(state == 3)
			{
				clock += delta;
				if(clock>=2000)
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
			*/
		}
		
		
	}

	@Override
	public int getID() {
		return 5;
	}

}