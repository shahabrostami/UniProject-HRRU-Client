package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryonet.Client;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

public class PlayQuestionTest extends BasicTWLGameState {

	public Client client;
	DialogLayout choicePanel, questionPanel;
	
	int gcw;
	int gch;
	String mouse;
	
	Image scorebackground;
	
	private Player player;
	private Player player1;
	private Player player2;
	private int playerScore;
	private String playerName;
	private int currentAnswer;
	
	private int header_x = 330;
	private int header_y = 50;
	private int timer_x = 600;
	private int timer_y = 550;
	
	private int mainFontSize = 24;
	private int titleFontSize = 36;
	private int questionFontSize = 26;
	private int timerFontSize = 40;
	private int timerMFontSize = 18;
	
	private Font loadFont, loadMainFont, loadTitleFont, loadQuestionFont, loadTimerFont, loadTimerMFont;
	private BasicFont mainFont, titleFont,  questionFont, timerFont, timerMFont;;
	
	private int current_question_id;
	private QuestionList question_list;
	private Question[] questions;
	private Question current_question;
	private String[] current_choices;
	private int correctAnswer;
	private int question_difficulty;
	private String[] full_question_description;
	private String start_message = "";
	private String full_start_message = "Here's your question...";
	private int full_start_counter = 0;
	private String ticker = "";
	private boolean tickerBoolean = true;
	
	private int clock2,clock3,timer,timer2 = 0;
	private boolean end, win, time_out = false;
	
	Label lblConfirmation;
	Button choices[];
	Button btnYes, btnNo;
	SimpleTextAreaModel model;
	TextArea question;
	DialogLayout.Group hBtn[], hBtnYes, hBtnNo, hQuestion, hLblConfirmation;
	String description;
	
	public PlayQuestionTest(int state, QuestionList ql) {
		client = HRRUClient.conn.getClient();
		this.question_list = ql;
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		
		timer = 50*10;
		timer2 = 999;
		
		questions = question_list.getQuestion_list();
		question_list.getNumberOfQuestions();
		
		current_question_id = HRRUClient.cs.getActivity_id();
		current_question = questions[current_question_id];

		current_choices = current_question.getChoices();
		full_question_description = current_question.getDescription();
		current_question.getAmountOfAnswers();
		correctAnswer = current_question.getAnswer();
		question_difficulty = current_question.getDifficulty();
		
		player1 = HRRUClient.cs.getP1();
		player2 = HRRUClient.cs.getP2();
		
		if(HRRUClient.cs.getPlayer() == 1)
			player = HRRUClient.cs.getP1();
		else 
			player = HRRUClient.cs.getP2();
		
		playerName = player.getName();
		playerScore = player.getScore();
		
		choicePanel = new DialogLayout();
		questionPanel = new DialogLayout();
		questionPanel.setTheme("question-panel");
        choicePanel.setTheme("choices-panel");
		
        questionPanel.setSize(700, 400);
        questionPanel.setPosition(
                (gcw/2 - questionPanel.getWidth()/2),
                 (gch/2 - questionPanel.getHeight()/2) + 50);
        
		choicePanel.setSize(500, 150);
		choicePanel.setPosition(
				(gcw/2 - choicePanel.getWidth()/2),
                (gch/2 - choicePanel.getHeight()/2) + 150);
		
		lblConfirmation = new Label("");
		choices = new Button[5];
		btnYes = new Button("Yes");
		btnNo = new Button("No");
		hBtn = new DialogLayout.Group[5];

		lblConfirmation.setVisible(false);
		lblConfirmation.setTheme("atarigreen16");
		btnYes.setVisible(false);
		btnYes.setTheme("choicebutton");
		btnNo.setVisible(false);
		btnNo.setTheme("choicebutton");
		
		model = new SimpleTextAreaModel("Question: \n" + full_question_description[0]);
		question = new TextArea(model);
		question.setTheme("questiontextarea");
		
		hQuestion = questionPanel.createSequentialGroup().addWidget(question);
		
		questionPanel.setHorizontalGroup(questionPanel.createParallelGroup()
				.addGroup(hQuestion));
		
		questionPanel.setVerticalGroup(questionPanel.createSequentialGroup()
				.addWidgets(question));
		
		for(int i = 0; i < 5; i++)
		{
			choices[i] = new Button(current_choices[i]);
			choices[i].setSize(500, 30);
			choices[i].setTheme("choicebutton");
			hBtn[i] =  choicePanel.createSequentialGroup().addWidget(choices[i]);
		}
		
		hLblConfirmation = choicePanel.createSequentialGroup().addWidget(lblConfirmation);
		hBtnYes = choicePanel.createSequentialGroup().addWidget(btnYes);
		hBtnNo = choicePanel.createSequentialGroup().addWidget(btnNo);
		
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
		choices[2].addCallback(new Runnable() {
            public void run() {
                emulateChoice(2);
            }
        });
		choices[3].addCallback(new Runnable() {
            public void run() {
                emulateChoice(3);
            }
        });
		choices[4].addCallback(new Runnable() {
            public void run() {
                emulateChoice(4);
            }
        });
		
		btnYes.addCallback(new Runnable() {
            public void run() {
                emulateYes();
            }
        });
		
		btnNo.addCallback(new Runnable() {
            public void run() {
                emulateNo();
            }
        });
		
		
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
		
		choicePanel.setIncludeInvisibleWidgets(false);
		
		rootPane.add(questionPanel);
		rootPane.add(choicePanel);
		rootPane.setTheme("");
	}
	
	void disableChoices()
	{
		for(int i = 0; i < 5; i++)
		{
			choices[i].setVisible(false);
		}
		lblConfirmation.setVisible(true);
		btnYes.setVisible(true);
		btnNo.setVisible(true);
	}
	
	void enableChoices()
	{
		for(int i = 0; i < 5; i++)
		{
			choices[i].setVisible(true);
		}
		lblConfirmation.setVisible(false);
		btnYes.setVisible(false);
		btnNo.setVisible(false);
	}
	
	void disableGUI()
	{
		choicePanel.setVisible(false);
		questionPanel.setVisible(false);
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
		end = true;
		if(currentAnswer == correctAnswer)
		{
			playerScore = 100 + timer;
			playerScore *= question_difficulty;
			win = true;
		}
		else
			win = false;
	}
	
	void emulateNo()
	{
		enableChoices();
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
		
		// Create custom font for question
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
		
		loadQuestionFont = loadFont.deriveFont(Font.PLAIN, questionFontSize);
		loadTimerFont = loadFont.deriveFont(Font.BOLD,timerFontSize);
		loadTimerMFont = loadFont.deriveFont(Font.BOLD,timerMFontSize);
		timerFont = new BasicFont(loadTimerFont);
		timerMFont = new BasicFont(loadTimerMFont);
		questionFont = new BasicFont(loadQuestionFont);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(new Image("res/img/questionbg.png"), 0, 0);
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
		
		g.setFont(questionFont.get());
		if(end)
		{
			g.drawImage(new Image("res/img/questionbg.png"), 0, 0);
			if(win)
			{
				g.drawString("CORRECT!", 100, 100);
				g.drawString("Points: ", 100, 130);
				g.drawString("100", 500, 130);
				g.drawString("Time Bonus: ", 100, 160);
				g.drawString("" + timer, 500, 160);
				g.drawString("Difficulty Multiplyer ", 100, 190);
				if(question_difficulty==1)
					g.drawString("Easy: x1", 500, 190);
				g.drawString("Overall Points: ", 100, 400);
				g.drawString("+" + playerScore, 500, 400);
				g.drawString(playerName + "'s new score: " + playerScore, 100, 450);
			}
			else if(time_out)
			{
				g.drawString("You have ran out of time!", 100, 100);
				g.drawString("The correct answer was " + current_choices[correctAnswer], 100, 150);
				g.drawString("Overall Points: ", 100, 400);
				g.drawString("+" + playerScore, 500, 400);
				g.drawString(playerName + "'s score: " + playerScore, 100, 450);
			}
			else if(win==false)
			{
				g.drawString("INCORRECT!", 100, 100);
				g.drawString("The correct answer was " + current_choices[correctAnswer], 100, 150);
				g.drawString("Overall Points: ", 100, 400);
				g.drawString("+" + playerScore, 500, 400);
				g.drawString(playerName + "'s score:  " + playerScore, 100, 450);
			}
			g.drawString("Waiting to continue...", 500, 530);
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		gc.getInput();
		int xpos = Mouse.getX();
		int ypos= Mouse.getY();
		mouse = "xpos: " + xpos + "\nypos: " + ypos;
		
		if(end==false)
		{
			clock2 += delta;
			clock3 += delta;
			timer2--;
			
			if(clock3 > 100){
				if(full_start_counter < full_start_message.length())
				{
					start_message += full_start_message.substring(full_start_counter, full_start_counter+1);
					full_start_counter++;
					clock3-=100;
				}
			}
			if(clock2>999)
			{
				timer--;
				if(timer<=0)
				{
					end = true;
					time_out = true;
				}
				timer2=999;
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
	}

	@Override
	public int getID() {
		return 6;
	}

}