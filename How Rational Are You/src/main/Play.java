package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import conn.Packet.*;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import com.esotericsoftware.kryonet.Client;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ResizableFrame.ResizableAxis;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ToggleButton;
import main.Chat.ChatFrame;
import main.board.*;

public class Play extends BasicTWLGameState {

	public Client client;
	DialogLayout firstPanel;
	
	Input input;
	int gcw;
	int gch;
	
	private Board board;
	private Dice dice;
	private QuestionList question_list;
	private PuzzleList puzzle_list;
	private int playerID;
	private int sessionID;
	private boolean currentPlayer;
	private Player player;
	private Player player1;
	private Player player2;
	private boolean p1ShowRollBanner = false;
	private boolean p2ShowRollBanner = false;
	
	private Font loadFont, loadMainFont;
	private BasicFont mainFont;
	private int mainFontSize = 24;
	
	private final int serverlost = -4;
	private final int cancelled = -2;
	private final int p1_turn = 7;
	private final int p2_turn = 8;
	private final int start_play = 9;
	private final int play = 10;
	
	private final int play_question = 6;
	private final int play_bidgame = 7;
    private final int play_trustgame = 8;
    private final int play_prisongame = 9;
	private final int statistics = 15;
	
	// states: 0 = idle, 1 = rolling, 2 = navigate board
	private int state, gameState;
	private int dice_counter, dice_counter_copy = 0;
	private int clock, timer;
	
	String mouse = "no input";
	
	DialogLayout playerPanel;
	Label lStatus, lPlayer1, lPlayer2, lPlayer1Score, lPlayer2Score;
	Label player1turn, lblYourTurn;
	Label lImgPlayer1, lImgPlayer2; 
	ToggleButton btnRoll;
	DialogLayout rollPanel;
	BasicFont header; 
	
	public static ChatFrame chatFrame;
	
	Image scorebackground, background, yourTurnBG;
	
	Packet00SyncMessage syncMessage;
	Packet11TurnMessage turnMessage;
	Packet12PlayReady readyMessage;
	
	public Play(int main) {
		client = HRRUClient.conn.getClient();
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		rootPane.removeAllChildren();
		state = 0;
		clock = 0;
		timer = HRRUClient.cs.getTimer();
		
		sessionID = HRRUClient.cs.getSessionID();
		playerID = HRRUClient.cs.getPlayer();
		player1 = HRRUClient.cs.getP1();
		player2 = HRRUClient.cs.getP2();
				
		syncMessage = new Packet00SyncMessage(); 
		syncMessage.sessionID = sessionID;
		syncMessage.player = playerID;
		
		board = new Board(10);
		System.out.println("Time After: " + timer);
		
		if(playerID == 1)
		{
			btnRoll.setVisible(true);
			currentPlayer = true;
			player = HRRUClient.cs.getP1();
			p1ShowRollBanner = true;
			/*
			ActivityScore list;
			if(!(player.getActivityScores().isEmpty()))
			{
				for(int i = 0; i < player.getActivityScores().size(); i++)
				{
					list = player.getActivityScores().get(i);
					System.out.println(list.getActivity());
					System.out.println(list.getActivity_id());
					System.out.println(list.getCorrect());
					System.out.println(list.getDifficulty());
					System.out.println(list.getElapsedtime());
					System.out.println(list.getOverall());
					System.out.println(list.getPoints());
				}
			}
			
			
			TrustScore list;
			if(!(player.getTrustScores().isEmpty()))
			{
				for(int i = 0; i < player.getTrustScores().size(); i++)
				{
					list = player.getTrustScores().get(i);
					System.out.println(list.getMaxToGive());
					System.out.println(list.getMaxToReturn());
					System.out.println(list.getMultiplier());
					System.out.println(list.getPlayerGive());
					System.out.println(list.getPlayerReturn());
					System.out.println(list.getPlayerReturnProfit());
					System.out.println(list.getPlayerReturnValue());
				}
			}
			
			*/
		}
		else {
			p2ShowRollBanner = true;
			btnRoll.setVisible(true);
			currentPlayer = false;
			player = HRRUClient.cs.getP2();
		}
		btnRoll.setActive(false);
		
		lblYourTurn = new Label();
		lblYourTurn.setSize(800, 600);
		lblYourTurn.setTheme("labelyourturn");
		if(playerID == 1)
			lblYourTurn.setVisible(true);
		else
			lblYourTurn.setVisible(false);
		lblYourTurn.setPosition(0,0);
		
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
		
		rootPane.add(rollPanel);
		rootPane.add(lStatus);
		rootPane.add(chatFrame);
		rootPane.add(lblYourTurn);
		resetPosition();
		
		turnMessage = new Packet11TurnMessage();
		readyMessage = new Packet12PlayReady();
		readyMessage.sessionID = HRRUClient.cs.getSessionID();
		readyMessage.player = playerID;
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
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// Game 
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
		scorebackground = new Image("simple/playerscorebackground.png");
		background = new Image("simple/background.png");
		yourTurnBG = new Image("simple/yourturn.png");
		
		header = new BasicFont("Atari Font Full Version", Font.PLAIN, 12);
		// Create custom font for question
		try {
			loadFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
			        org.newdawn.slick.util.ResourceLoader.getResourceAsStream("font/visitor2.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
				}
		
		loadMainFont = loadFont.deriveFont(Font.BOLD,mainFontSize);
		mainFont = new BasicFont(loadMainFont);
		
		try {
			question_list = new QuestionList("Question.txt");
			puzzle_list = new PuzzleList("Puzzle.txt");
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
		btnRoll = new ToggleButton("ROLL");
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
		
		sbg.addState(new PlayQuestionTest(play_question, question_list));
		sbg.addState(new PlayGame_Bid(play_bidgame));
		sbg.addState(new PlayGame_Trust(play_trustgame));
		sbg.addState(new PlayGame_Prisoners(play_prisongame));
		sbg.addState(new Statistics(statistics));
		//sbg.addState(new PlayPuzzle(play_puzzle, puzzle_list));
		//sbg.addState(new PlayPuzzle(9));
		//sbg.addState(new PlayGame(10));

		sbg.getState(play_question).init(gc, sbg);
		sbg.getState(play_bidgame).init(gc, sbg);
		sbg.getState(play_trustgame).init(gc, sbg);
		sbg.getState(play_prisongame).init(gc, sbg);
		sbg.getState(statistics).init(gc, sbg);
		//sbg.getState(play_puzzle).init(gc, sbg);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(background, 0,0);
		for(int i = 0; i < board.getScale()*3-3; i++)
			g.drawImage(board.gridSquares[i].gridSquare.getImage(), board.gridSquares[i].getx(), board.gridSquares[i].gety());
		
		for(int j = board.getSize()-1; j >= board.getScale()*3-3; j--)
			g.drawImage(board.gridSquares[j].gridSquare.getImage(), board.gridSquares[j].getx(), board.gridSquares[j].gety());
		
		g.drawImage(scorebackground, 0,0);
		g.setFont(mainFont.get());
		g.drawImage(player1.getPlayerCharacter().getCharacterImage(), 13,13);
		g.drawImage(player2.getPlayerCharacter().getCharacterImage(), 13,55);
		g.drawString("" + player1.getName(), 65, 22);
		g.drawString("" + player2.getName(), 65, 64);
		g.drawString("" + player1.getScore(), 204, 22);
		g.drawString("" + player2.getScore(), 204, 64);
		
		g.drawImage(player1.getPlayerCharacter().getCharacterImage(), board.gridSquares[player1.getPosition()].getx(), board.gridSquares[player1.getPosition()].gety());
		g.drawImage(player2.getPlayerCharacter().getCharacterImage(), board.gridSquares[player2.getPosition()].getx(), board.gridSquares[player2.getPosition()].gety());
		
		g.drawString(mouse, 650, 550);
		
		g.scale(1.25f, 1.25f);
		if(state>0 && state < 3)
			dice.dice.draw(105, 97+dice.getY());
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos= Mouse.getY();
		mouse = "timer:" + ((timer/1000)+1) + "\nxpos: " + xpos + "\nypos: " + ypos;
		gameState = HRRUClient.cs.getState();
		timer -= delta;
		if(gameState == serverlost)
			sbg.enterState(0);
		
		if(gameState == cancelled) {
			if(playerID == 1)
				sbg.enterState(1);
			else sbg.enterState(2);
		}

		else 
		
		if(timer <0)
			sbg.enterState(15);
		
		if(state == 0)
		{
			if(playerID == 1)
			{
				if(gameState == p1_turn)
				{
					if(p1ShowRollBanner)
					{
						if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
						{
							p1ShowRollBanner = false;
							lblYourTurn.setVisible(false);
						}
					}
					lStatus.setText(player1.getName() + ", it's your turn!");
					currentPlayer = true;
					btnRoll.setText("ROLL");
					btnRoll.setVisible(true);
				}
				else
				{
					lStatus.setText("It's " + player2.getName() + "'s turn.");
					currentPlayer = false;
					btnRoll.setText("WAITING FOR " + player2.getName());
					btnRoll.setVisible(false);
				}
			}
			else if(playerID == 2)
			{
				if(gameState == p2_turn)
				{
					if(p2ShowRollBanner)
					{
						lblYourTurn.setVisible(true);
						if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
						{
							p2ShowRollBanner = false;
							lblYourTurn.setVisible(false);
						}
					}
					btnRoll.setText("ROLL");
					lStatus.setText(player2.getName() + ", it's your turn!");
					currentPlayer = true;
					btnRoll.setVisible(true);
				}
				else
				{
					lStatus.setText("It's " + player1.getName() + "'s turn.");
					currentPlayer = false;
					btnRoll.setText("WAITING FOR " + player1.getName());
					btnRoll.setVisible(false);
				}
			}
		}
		
		if(currentPlayer)
		{
			//roll dice
			if(state==1)
			{
				clock += delta;
				if(clock>=60) // should be 60
				{
					dice.rollDice();
					clock-=60; // should be 60
					if(dice.getPosition()==0)
					{
						dice_counter = dice.getCurrentNumber();
						dice_counter_copy = dice.getCurrentNumber();
						state = 2;
						btnRoll.setVisible(false);
					}
				}
			}
			
			//navigating board
			if(state == 2)
			{
				clock += delta;
				if(clock>=200) // should be 200
				{
					player.updatePosition();
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
				turnMessage.sessionID = HRRUClient.cs.getSessionID();
				turnMessage.playerID = playerID;
				turnMessage.moves = dice_counter_copy;
				turnMessage.tile = board.gridSquares[player.getPosition()].getTileType();
				client.sendTCP(turnMessage);
				
				if(playerID == 1)
				{
					HRRUClient.cs.setState(p2_turn);
					btnRoll.setText("WAITING FOR " + player2.getName());
				}
				else
					btnRoll.setText("WAITING FOR " + player1.getName());
				btnRoll.setVisible(false);
				state = 4;
				clock = 200; // should be 200
			}
		}
	
		if(state == 4)
		{
			if(gameState == start_play)
			{
				clock--;
				lStatus.setText("Starting in " + (clock/100+1) + "...");
				if(clock<=0)
				{
					clock=0;
					client.sendTCP(readyMessage);
					state = 5;
				}
			}
		}
		else if(state == 5)
		{
			state = 6;
		}
		
		if(state == 6)
		{
			if(gameState == play)
			{
				System.out.println(timer + "timer");
				int currentTile = board.gridSquares[player.getPosition()].getTileType();
				int otherPlayerTile;
				HRRUClient.cs.setTimer(timer);
				if(HRRUClient.cs.getPlayer() == 1)
					otherPlayerTile = board.gridSquares[HRRUClient.cs.getP2().getPosition()].getTileType();
				else
					otherPlayerTile = board.gridSquares[HRRUClient.cs.getP1().getPosition()].getTileType();
					
				int activity_id = HRRUClient.cs.getActivity_id();
				System.out.println(otherPlayerTile + "the tile");
				
				if(otherPlayerTile == 3 || currentTile == 3)
				{
					if(activity_id == 1)
						sbg.enterState(play_bidgame);
					else if(activity_id == 2)
						sbg.enterState(play_trustgame);
					else if(activity_id == 3)
							sbg.enterState(play_prisongame);
					}
					else if(currentTile == 1)	
					{
						sbg.enterState(play_question);
					}
					else if(currentTile == 2)
					{
						// sbg.enterState(play_puzzle);
					}
			}
		}
	}

	@Override
	public int getID() {
		return 5;
	}

}