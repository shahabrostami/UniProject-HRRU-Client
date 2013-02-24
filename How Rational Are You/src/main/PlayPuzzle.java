package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import main.textpage.TextPage.TextPageFrame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryonet.Client;

import conn.Packet.*;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.ResizableFrame.ResizableAxis;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

public class PlayPuzzle extends BasicTWLGameState {
	
	private int gameState;
	private final int cancelled = -2;
	private final int play = 5;
	private final int p1_turn = 7;
	public final int puzzle_points_amount = 100;
	
	public Client client;
	DialogLayout choicePanel, puzzlePanel;
	
	int gcw;
	int gch;
	String mouse;
	
	Image scorebackground;
	
	private int playerID;
	private int otherPlayerID;
	private Player player;
	private Player otherPlayer;
	private Player player1;
	private Player player2;
	private int playerScore;
	private int playerScore2;
	private String playerName;
	private int currentAnswer;
	private int otherPlayerReady;
	ActivityScore otherPlayerResult;
	private int elapsedTime = 0;
	private int pointsAvailable = 0;
	private int pointsGained = 0;
	
	private int header_x = 330;
	private int header_y = 50;
	private int timer_x = 600;
	private int timer_y = 550;
	
	private int mainFontSize = 24;
	private int titleFontSize = 36;
	private int puzzleFontSize = 26;
	private int timerFontSize = 40;
	private int timerMFontSize = 18;
	
	private Font loadFont, loadMainFont, loadTitleFont, loadpuzzleFont, loadTimerFont, loadTimerMFont;
	private BasicFont mainFont, titleFont, readyFont, puzzleFont, timerFont, timerMFont;;
	
	private int current_puzzle_id;
	private PuzzleList puzzle_list;
	private Puzzle[] puzzles;
	private Puzzle current_puzzle;
	private String[] current_choices;
	private int correctAnswer;
	private int puzzle_difficulty;
	private int amountOfAnswers;
	private String FILE_NAME;
	private String start_message = "";
	private String full_start_message = "Here's your puzzle...";
	private int full_start_counter = 0;
	private String ticker = "";
	private boolean tickerBoolean = true;
	
	private int clock2,clock3,timer,timer2 = 0;
	private boolean end, win, time_out, finished, resume = false;
	
	TextPageFrame textpageframe;
	DialogLayout p1ResultPanel, p2ResultPanel;
	
	Label lActivity, lTitle, lPoints, lDifficulty, lTime, lOverall, lNew;
	Label lActivity2, lPoints2, lDifficulty2, lTime2, lOverall2, lNew2;
	Label lblPoints1, lblDifficulty1, lblTime1, lblOverall1, lblNew1;
	Label lblPoints2, lblDifficulty2, lblTime2, lblOverall2, lblNew2;
	
	Label lblConfirmation, lblWaiting;
	Button choices[];
	Button btnYes, btnNo;
	SimpleTextAreaModel model;
	DialogLayout.Group hBtn[], hBtnYes, hBtnNo, hpuzzle, hLblConfirmation;
	String description;
	
	Packet00SyncMessage syncMessage;
	Packet15PuzzleComplete completeMessage;
	
	public PlayPuzzle(int state, PuzzleList pl) {
		client = HRRUClient.conn.getClient();
		this.puzzle_list = pl;
	}
	
	void disableChoices()
	{
		for(int i = 0; i < amountOfAnswers; i++)
		{
			choices[i].setVisible(false);
			choices[i].setEnabled(false);
		}
		lblConfirmation.setVisible(true);
		btnYes.setVisible(true);
		btnNo.setVisible(true);
	}
	
	void enableChoices()
	{
		for(int i = 0; i < amountOfAnswers; i++)
		{
			choices[i].setVisible(true);
			choices[i].setEnabled(true);
		}
		lblConfirmation.setVisible(false);
		btnYes.setVisible(false);
		btnNo.setVisible(false);
	}
	
	void disableGUI()
	{
		choicePanel.setVisible(false);
		puzzlePanel.setVisible(false);
	}
	
	void emulateChoice(int choice)
	{
		disableChoices();
		lblConfirmation.setText("Is " + choices[choice].getText() + " your answer?");
		currentAnswer = choice;
	}
	
	void emulateYes()
	{
		disableGUI();
		completeMessage = new Packet15PuzzleComplete();
		completeMessage.difficulty = puzzle_difficulty;
		completeMessage.elapsedtime = 0;
		completeMessage.player = playerID;
		completeMessage.points = 0;
		completeMessage.overall = 0;
		completeMessage.correct = false;
		completeMessage.sessionID = HRRUClient.cs.getSessionID();
		pointsAvailable = 0;
		pointsGained = 0;
		elapsedTime = 0;
		end = true;
		if(currentAnswer == correctAnswer)
		{
			completeMessage.elapsedtime = timer;
			elapsedTime = timer; 
			pointsAvailable = puzzle_points_amount;
			pointsGained = puzzle_points_amount;
			completeMessage.points = pointsGained;
			
			pointsGained *= puzzle_difficulty;
			pointsGained += timer;
			completeMessage.correct = true;
			completeMessage.overall = pointsGained;
			player.addScore(pointsGained);
			win = true;
		}
		else
			win = false;
		client.sendTCP(completeMessage);
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		rootPane.removeAllChildren();
		choicePanel.removeAllChildren();
		
		// Set up puzzle variables
		puzzles = puzzle_list.getPuzzle_list();
		puzzle_list.getNumberOfPuzzles();
		
		current_puzzle_id = HRRUClient.cs.getActivity_id();
		current_puzzle = puzzles[current_puzzle_id];

		current_choices = current_puzzle.getChoices();
		FILE_NAME = current_puzzle.getFile();
		amountOfAnswers = current_puzzle.getAmountOfAnswers();

		correctAnswer = current_puzzle.getAnswer();
		puzzle_difficulty = current_puzzle.getDifficulty();
		System.out.println(FILE_NAME);
		
		// Reset variables
		otherPlayerReady=0;
		win = false; end = false; time_out = false; finished = false; resume = false;
		currentAnswer = 0;
		clock2 = 0; clock3 = 0;
		timer = 50*puzzle_difficulty;
		timer2 = 999;
		elapsedTime = 0;
		pointsAvailable = 0;
		pointsGained = 0;
		
		start_message = "";
		full_start_message = "Here's your puzzle...";
		full_start_counter = 0;
		ticker = "";
		tickerBoolean = true;
		
		// Retrieve player information
		player1 = HRRUClient.cs.getP1();
		player2 = HRRUClient.cs.getP2();
		playerID = HRRUClient.cs.getPlayer();
		
		if(playerID == 1)
		{
			player =  player1;
			otherPlayer = player2;
			otherPlayerID = 2;
		}
		else 
		{
			player =  player2;
			otherPlayer = player1;
			otherPlayerID = 1;
		}
		
		playerName = player.getName();
		playerScore = player.getScore();
		playerScore2 = otherPlayer.getScore();
		
		choicePanel.setTheme("choices-panel");   

		textpageframe = new TextPageFrame(FILE_NAME);
		hBtn = new DialogLayout.Group[amountOfAnswers];
		choices = new Button[amountOfAnswers];
		
		for(int i = 0; i < amountOfAnswers; i++)
		{
			choices[i] = new Button();
			choices[i].setSize(500, 30);
			choices[i].setTheme("choicebutton");
			hBtn[i] =  choicePanel.createSequentialGroup().addWidget(choices[i]);
		}
		// Re-add previous widgets
		hLblConfirmation = choicePanel.createSequentialGroup().addWidget(lblConfirmation);
		hBtnYes = choicePanel.createSequentialGroup().addWidget(btnYes);
		hBtnNo = choicePanel.createSequentialGroup().addWidget(btnNo);
		// Create Dialog Layout
		choicePanel.setHorizontalGroup(choicePanel.createParallelGroup()
				.addGroups(hBtn)
				.addGroup(hLblConfirmation)
				.addGroup(hBtnYes)
				.addGroup(hBtnNo));
		
		choicePanel.setVerticalGroup(choicePanel.createSequentialGroup()
				.addWidgets(choices)
				.addWidget(lblConfirmation)
				.addWidget(btnYes)
				.addWidget(btnNo));
		
		System.out.println("puzzle" + current_choices[0]);
		// Set up buttons for choices
		for(int i = 0; i < amountOfAnswers; i++)
			choices[i].setText(current_choices[i]);
		
		// Set up callbacks for buttons
		choices[0].addCallback(new Runnable() {
            public void run() {
                emulateChoice(0);
            }
        });
		choices[1].addCallback(new Runnable() {
            public void run() {
                emulateChoice(1);
            }
        });
		if(amountOfAnswers>2)
			{
			choices[2].addCallback(new Runnable() {
				public void run() {
					emulateChoice(2);
				}
			});
		}
		if(amountOfAnswers>3)
		{
			choices[3].addCallback(new Runnable() {
				public void run() {
					emulateChoice(3);
				}
			});
			}
		if(amountOfAnswers>4)
		{
			choices[4].addCallback(new Runnable() {
				public void run() {
					emulateChoice(4);
				}
			});
		}
		if(amountOfAnswers>5)
		{
			choices[5].addCallback(new Runnable() {
				public void run() {
					emulateChoice(5);
				}
			});
		}

		
		// Add to root pane
		enableChoices();
		puzzlePanel.setVisible(true);
		choicePanel.setVisible(true);
		lblWaiting.setVisible(false);
		p1ResultPanel.setVisible(false);
		p2ResultPanel.setVisible(false);
		p1ResultPanel.setTheme("incorrect-panel");
		p2ResultPanel.setTheme("incorrect-panel");
		
		rootPane.add(p1ResultPanel);
		rootPane.add(p2ResultPanel);
		rootPane.add(lblWaiting);
		rootPane.add(puzzlePanel);
		rootPane.add(choicePanel);
		
		rootPane.setTheme("");
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
		scorebackground = new Image("res/simple/playerscorebackground.png");
		
		// Create custom font for puzzle
		try {
			loadFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
			        org.newdawn.slick.util.ResourceLoader.getResourceAsStream("res/font/visitor2.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
				}
		loadTitleFont = loadFont.deriveFont(Font.BOLD,titleFontSize);
		titleFont = new BasicFont(loadTitleFont);
		
		loadMainFont = loadFont.deriveFont(Font.BOLD,mainFontSize);
		mainFont = new BasicFont(loadMainFont);
		readyFont = new BasicFont(loadTitleFont, Color.red);
		
		loadpuzzleFont = loadFont.deriveFont(Font.PLAIN, puzzleFontSize);
		loadTimerFont = loadFont.deriveFont(Font.BOLD,timerFontSize);
		loadTimerMFont = loadFont.deriveFont(Font.BOLD,timerMFontSize);
		timerFont = new BasicFont(loadTimerFont);
		timerMFont = new BasicFont(loadTimerMFont);
		puzzleFont = new BasicFont(loadpuzzleFont);
		
		// Set up puzzle GUI widgets
		puzzlePanel = new DialogLayout();
		puzzlePanel.setTheme("question-panel");
		puzzlePanel.setSize(730, 250);
		puzzlePanel.setPosition(
				(100),
				(gch/2 - puzzlePanel.getHeight()/2) - 75);
		choicePanel = new DialogLayout();
		choicePanel.setTheme("choices-panel");   
		choicePanel.setSize(500, 150);
		choicePanel.setPosition(
				(gcw/2 - choicePanel.getWidth()/2),
		        (gch/2 - choicePanel.getHeight()/2) + 150);
				
		textpageframe = new TextPageFrame("demo.html");
		textpageframe.setSize(700, 200);
		textpageframe.setDraggable(false);
		textpageframe.setResizableAxis(ResizableAxis.NONE);
		textpageframe.setTheme("textpageframe");
		
		hpuzzle = puzzlePanel.createSequentialGroup().addWidget(textpageframe);
		
		puzzlePanel.setHorizontalGroup(puzzlePanel.createParallelGroup()
				.addGroup(hpuzzle));	
		puzzlePanel.setVerticalGroup(puzzlePanel.createSequentialGroup()
				.addWidgets(textpageframe));
		
		lblConfirmation = new Label("");
		choices = new Button[5];
		btnYes = new Button("Yes");
		btnNo = new Button("No");
		hBtn = new DialogLayout.Group[5];
		
		lblConfirmation.setTheme("atarigreen16");
		btnYes.setTheme("choicebutton");
		btnNo.setTheme("choicebutton");
				
		hLblConfirmation = choicePanel.createSequentialGroup().addWidget(lblConfirmation);
		hBtnYes = choicePanel.createSequentialGroup().addWidget(btnYes);
		hBtnNo = choicePanel.createSequentialGroup().addWidget(btnNo);
				
		btnYes.addCallback(new Runnable() {
            public void run() {
                emulateYes();
            }
        });
				
		btnNo.addCallback(new Runnable() {
            public void run() {
            	enableChoices();
            }
        });
		
		choicePanel.setIncludeInvisibleWidgets(false);
		
		btnNo.setVisible(false);
		btnYes.setVisible(false);
		
		// RESULTS PANEL SETUP
		lblWaiting = new Label("");
		lblWaiting.setSize(800, 100);
		lblWaiting.setPosition(0,500);
		lblWaiting.setTheme("labelscoretotal");
		
        p1ResultPanel = new DialogLayout();
        p1ResultPanel.setTheme("incorrect-panel");
		
		p1ResultPanel.setSize(310, 310);
		p1ResultPanel.setPosition(
               (gcw/2 - p1ResultPanel.getWidth()/2 - 220),
                (gch/2 - p1ResultPanel.getHeight()/2));
		
		p2ResultPanel = new DialogLayout();
        p2ResultPanel.setTheme("incorrect-panel");
		p2ResultPanel.setSize(310, 310);
		p2ResultPanel.setPosition(
               (gcw/2 - p2ResultPanel.getWidth()/2 + 180),
                (gch/2 - p2ResultPanel.getHeight()/2));
		
		lActivity = new Label("");
		lActivity.setTheme("labelmiddle");
		lPoints = new Label("Points:");
		lDifficulty = new Label("Difficulty:");
		lTime = new Label("Time Bonus:");
		lOverall = new Label("Overall Points:");
		lNew = new Label("New Total:");
		lNew.setTheme("labelscoretotal");
		
		lActivity2 = new Label("");
		lActivity2.setTheme("labelmiddle");
		lPoints2 = new Label("Points:");
		lDifficulty2 = new Label("Difficulty:");
		lTime2 = new Label("Time Bonus:");
		lOverall2 = new Label("Overall Points:");
		lNew2 = new Label("New Total:");
		lNew2.setTheme("labelscoretotal");
				
		lblPoints1 = new Label("");
		lblPoints1.setTheme("labelright");
		lblDifficulty1 = new Label("");
		lblDifficulty1.setTheme("labelright");
		lblTime1 = new Label("");
		lblTime1.setTheme("labelright");
		lblOverall1 = new Label("");
		lblOverall1.setTheme("labelright");
		lblNew1 = new Label("");
		lblNew1.setTheme("labelscoretotalright");
		
		lblPoints2 = new Label("");
		lblPoints2.setTheme("labelright");
		lblDifficulty2 = new Label("");
		lblDifficulty2.setTheme("labelright");
		lblTime2 = new Label("");
		lblTime2.setTheme("labelright");
		lblOverall2 = new Label("");
		lblOverall2.setTheme("labelright");
		lblNew2 = new Label("");
		lblNew2.setTheme("labelscoretotalright");
		
		DialogLayout.Group hLabels1 = p1ResultPanel.createParallelGroup(lPoints, lDifficulty, lTime, lOverall, lNew).addGap(200);
		DialogLayout.Group hP1Result1 = p1ResultPanel.createParallelGroup(lblPoints1, lblDifficulty1, lblTime1, lblOverall1, lblNew1);
		DialogLayout.Group hActivity = p1ResultPanel.createSequentialGroup(lActivity);
		
		p1ResultPanel.setHorizontalGroup(p1ResultPanel.createParallelGroup()
				.addGap(120)
				.addGroup(hActivity)
				.addGroup(p1ResultPanel.createSequentialGroup(hLabels1, hP1Result1)));
		
		p1ResultPanel.setVerticalGroup(p1ResultPanel.createSequentialGroup()
				.addGap(60).addWidget(lActivity)
				.addGap(30).addGroup(p1ResultPanel.createParallelGroup(lPoints, lblPoints1))
				.addGap(30).addGroup(p1ResultPanel.createParallelGroup(lDifficulty, lblDifficulty1))
				.addGap(30).addGroup(p1ResultPanel.createParallelGroup(lTime, lblTime1))
				.addGap(30).addGroup(p1ResultPanel.createParallelGroup(lOverall, lblOverall1))
				.addGap(30).addGroup(p1ResultPanel.createParallelGroup(lNew, lblNew1)));
		
		DialogLayout.Group hLabels2 = p2ResultPanel.createParallelGroup(lPoints2, lDifficulty2, lTime2, lOverall2, lNew2).addGap(200);
		DialogLayout.Group hP1Result2 = p2ResultPanel.createParallelGroup(lblPoints2, lblDifficulty2, lblTime2, lblOverall2, lblNew2);
		DialogLayout.Group hActivity2 = p2ResultPanel.createSequentialGroup(lActivity2);
		
		p2ResultPanel.setHorizontalGroup(p2ResultPanel.createParallelGroup()
				.addGap(120)
				.addGroup(hActivity2)
				.addGroup(p2ResultPanel.createSequentialGroup(hLabels2, hP1Result2)));
		
		p2ResultPanel.setVerticalGroup(p2ResultPanel.createSequentialGroup()
				.addGap(60).addWidget(lActivity2)
				.addGap(30).addGroup(p2ResultPanel.createParallelGroup(lPoints2, lblPoints2))
				.addGap(30).addGroup(p2ResultPanel.createParallelGroup(lDifficulty2, lblDifficulty2))
				.addGap(30).addGroup(p2ResultPanel.createParallelGroup(lTime2, lblTime2))
				.addGap(30).addGroup(p2ResultPanel.createParallelGroup(lOverall2, lblOverall2))
				.addGap(30).addGroup(p2ResultPanel.createParallelGroup(lNew2, lblNew2)));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if(!end)
		{
		g.drawImage(new Image("res/simple/questionbg.png"), 0, 0);
		g.setFont(titleFont.get());
		g.drawString("> " + start_message + "" + ticker, header_x, header_y);
		g.drawImage(scorebackground, 0,0);
		g.setFont(mainFont.get());
		
		g.drawImage(player1.getPlayerCharacter().getCharacterImage(), 13,13);
		g.drawImage(player2.getPlayerCharacter().getCharacterImage(), 13,55);
		g.drawString("" + player1.getName(), 65, 22);
		g.drawString("" + player2.getName(), 65, 64);
		g.drawString("" + player1.getScore(), 204, 22);
		g.drawString("" + player2.getScore(), 204, 64);
		

		g.drawString(mouse, 50, 550);
		
		g.setFont(timerFont.get());
		if(timer<100)
			g.drawString("TIME: 0" + timer, timer_x, timer_y);
		else if(timer<10)
			g.drawString("TIME: 00" + timer, timer_x, timer_y);
		else
		    g.drawString("TIME: " + timer, timer_x, timer_y);
		g.setFont(timerMFont.get());
		if(timer2<100)
			g.drawString("0" + timer2, timer_x+145, timer_y-10);
		else if(timer2<10)
			g.drawString("00" + timer2, timer_x+145, timer_y-10);
		else
			g.drawString("" + timer2, timer_x+145, timer_y-10);
		
		g.setFont(readyFont.get());
		
		if(otherPlayerReady == 1)
		{
			if(otherPlayerID == 1)
				g.drawString("FINISHED", 92, 19);
			else
				g.drawString("FINISHED", 92, 61);
		}
		}
		
		g.setFont(puzzleFont.get());
		if(end)
		{
			g.drawImage(new Image("res/simple/questionbg.png"), 0, 0);
			if(finished)
			{
				g.setFont(titleFont.get());
				g.drawString("> " + start_message + "" + ticker, 50, 50);
				g.drawString("" + timer, 750, 550);
				g.drawImage(new Image("/res/simple/playerbg.png"), 124, 175);
				g.drawImage(new Image("/res/simple/playerbg.png"), 524, 175);
				g.drawImage(player1.getPlayerCharacter().getCharacterImage(), 124, 175);
				g.drawImage(player2.getPlayerCharacter().getCharacterImage(), 524, 175);
				g.setFont(mainFont.get());
				g.drawString("" + player1.getName(), 174, 184);
				g.drawString("" + player2.getName(), 574, 184);
			}
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		gc.getInput();
		int xpos = Mouse.getX();
		int ypos= Mouse.getY();
		mouse = "xpos: " + xpos + "\nypos: " + ypos;
		clock2 += delta;
		clock3 += delta;
		timer2 -= delta;
		gameState = HRRUClient.cs.getState();
		if(gameState == cancelled) {
			if(playerID == 1)
				sbg.enterState(1);
			else sbg.enterState(2);
		}
		
		// Check if other player is finished
		if(otherPlayerID == 1)
			otherPlayerReady = HRRUClient.cs.getP1().getReady();
		else
			otherPlayerReady = HRRUClient.cs.getP2().getReady();
					
		if(clock3 > 100){
			if(full_start_counter < full_start_message.length())
			{
				start_message += full_start_message.substring(full_start_counter, full_start_counter+1);
				full_start_counter++;
				clock3-=100;
			}
		}
		
		if(end==false)
		{
			if(clock2>999)
			{
				timer--;
				timer2=999;
				if(timer<=0)
				{
					disableGUI();
					end = true;
					time_out = true;
					completeMessage = new Packet15PuzzleComplete();
					completeMessage.difficulty = puzzle_difficulty;
					completeMessage.elapsedtime = 0;
					completeMessage.overall = 0;
					completeMessage.correct = false;
					completeMessage.player = playerID;
					completeMessage.points = 0;
					completeMessage.sessionID = HRRUClient.cs.getSessionID();
					client.sendTCP(completeMessage);
				}
				clock2-=1000;
				if(tickerBoolean) 
				{
					ticker = "|";
					tickerBoolean = false;
				}
				else
				{
					ticker = "";
					tickerBoolean = true;
				}
			}
		}
		if(end && !finished)
		{
			if(otherPlayerReady == 1)
			{
				// Setup new UI
				timer = 7; // should be 10
				timer2 = 999;
				clock2 = 0;
				clock3 = 0;
				lblWaiting.setVisible(false);
				puzzlePanel.setVisible(false);
				full_start_message = "The results are in...";
				full_start_counter = 0;
				ticker = "";
				start_message = "";
				tickerBoolean = true;
				p1ResultPanel.setVisible(true);
				p2ResultPanel.setVisible(true);
				// Setup players results
				if(playerID == 1)
				{
					lblPoints1.setText("" + pointsAvailable);
					
					if(puzzle_difficulty==1)
						lblDifficulty1.setText("Easy X" + puzzle_difficulty);
					else if(puzzle_difficulty==2)
						lblDifficulty1.setText("Medium X" + puzzle_difficulty);
					else if(puzzle_difficulty==3)
						lblDifficulty1.setText("Hard X" + puzzle_difficulty);
					
					lblOverall1.setText("" + pointsGained);
					lblTime1.setText("" + elapsedTime);	
					lblNew1.setText("" + HRRUClient.cs.getP1().getScore());
					lActivity.setText("Answered a question...");
					if(win)
					{
						p1ResultPanel.setTheme("correct-panel");
						p1ResultPanel.reapplyTheme();
					}
					
					otherPlayerResult = HRRUClient.cs.getP2().getActivityScore();
					lblPoints2.setText("" + otherPlayerResult.getPoints());
					
					if(puzzle_difficulty==1)
						lblDifficulty2.setText("Easy X" + otherPlayerResult.getDifficulty());
					else if(puzzle_difficulty==2)
						lblDifficulty2.setText("Medium X" + otherPlayerResult.getDifficulty());
					else if(puzzle_difficulty==3)
						lblDifficulty2.setText("Hard X" +  otherPlayerResult.getDifficulty());
					
					lblOverall2.setText("" +  otherPlayerResult.getOverall());
					lblTime2.setText("" +  otherPlayerResult.getElapsedtime());
					lblNew2.setText("" + (playerScore2 + otherPlayerResult.getOverall()));
					
					int activity = otherPlayerResult.getActivity();
					if(activity== 1)
						lActivity2.setText("Answered a question...");
					else if(activity == 2)
						lActivity2.setText("Solved a puzzle...");
					else if(activity == 3)
						lActivity2.setText("Completed a game...");
					
					if(otherPlayerResult.getCorrect())
					{
						p2ResultPanel.setTheme("correct-panel");
						p2ResultPanel.reapplyTheme();
					}
					finished = true;
				}
				else if(playerID == 2)
				{
					otherPlayerResult = HRRUClient.cs.getP1().getActivityScore();
					lblPoints1.setText("" + otherPlayerResult.getPoints());
					
					if(puzzle_difficulty==1)
						lblDifficulty1.setText("Easy X" + otherPlayerResult.getDifficulty());
					else if(puzzle_difficulty==2)
						lblDifficulty1.setText("Medium X" + otherPlayerResult.getDifficulty());
					else if(puzzle_difficulty==3)
						lblDifficulty1.setText("Hard X" +  otherPlayerResult.getDifficulty());
					
					lblOverall1.setText("" +  otherPlayerResult.getOverall());
					lblTime1.setText("" +  otherPlayerResult.getElapsedtime());
					lblNew1.setText("" + (playerScore2 + otherPlayerResult.getOverall()));
					
					lActivity.setText("Answered a question...");
					
					if(otherPlayerResult.getCorrect())
					{
						p1ResultPanel.setTheme("correct-panel");
						p1ResultPanel.reapplyTheme();
					}
					
					lblPoints2.setText("" + pointsAvailable);
					if(puzzle_difficulty==1)
						lblDifficulty2.setText("Easy X" + puzzle_difficulty);
					else if(puzzle_difficulty==2)
						lblDifficulty2.setText("Medium X" + puzzle_difficulty);
					else if(puzzle_difficulty==3)
						lblDifficulty2.setText("Hard X" + puzzle_difficulty);
					lblOverall2.setText("" + pointsGained);
					lblTime2.setText("" + elapsedTime);
				
					lblNew2.setText("" + HRRUClient.cs.getP2().getScore());
					
					int activity = otherPlayerResult.getActivity();
					if(activity== 1)
						lActivity2.setText("Answered a question...");
					else if(activity == 2)
						lActivity2.setText("Solved a puzzle...");
					else if(activity == 3)
						lActivity2.setText("Completed a game...");
						
					if(win)
					{
						p2ResultPanel.setTheme("correct-panel");
						p2ResultPanel.reapplyTheme();
					}
					finished = true;
				}
			}
			else if(otherPlayerReady == 0)
			{
				puzzlePanel.setVisible(true);
				lblWaiting.setText( "You answered " + choices[currentAnswer].getText() + "\n" +"Waiting for " + otherPlayer.getName());
				lblWaiting.setVisible(true);
			}
		}
		else if(finished)
		{
			if(clock2>999)
			{
				timer--;
				timer2=999;
				clock2-=1000;
				if(timer<=0)
				{
					if(playerID == 1)
						HRRUClient.cs.getP2().addScore(otherPlayerResult.getOverall());
					else
						HRRUClient.cs.getP1().addScore(otherPlayerResult.getOverall());
					syncMessage = new Packet00SyncMessage();
					syncMessage.player = playerID;
					syncMessage.sessionID = HRRUClient.cs.getSessionID();
					client.sendTCP(syncMessage);
					finished = false;
					resume = true;
				}
			}
		}
		if(resume)
		{
			if(HRRUClient.cs.isSync())
			{
				HRRUClient.cs.setState(p1_turn);
				HRRUClient.cs.setSync(false);
				sbg.enterState(play);
			}
		}
	}
	
	@Override
	public int getID() {
		return 7;
	}
}