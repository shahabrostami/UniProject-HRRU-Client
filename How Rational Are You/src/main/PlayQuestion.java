package main;
 

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import org.lwjgl.input.Mouse;

import TWLSlick.BasicTWLGameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.Random;

public class PlayQuestion extends BasicTWLGameState{
	
	public String mouse = "No input yet!";
	
	private int current_question_id;
	private QuestionList question_list;
	private Question[] questions;
	private Question current_question;
	private String[] current_choices;
	private int noOfAnswers;
	private int correctAnswer;
	private int question_difficulty;
	private int no_of_questions;
	
	private Font loadFont, loadMainFont, loadQuestionFont, loadQuestionBoldFont, loadTimerFont, loadTimerMFont;
	private BasicFont mainFont, questionFont, questionSelectedFont, timerFont, timerMFont;
	
	private int mainFontSize = 36;
	private int questionFontSize = 26;
	private int timerFontSize = 40;
	private int timerMFontSize = 18;
	
	private int choice_x = 100;
	private int choice_y = 400;
	private int description_x = 100;
	private int description_y = 200;
	private int timer_x = 560;
	private int timer_y = 520;
	private int header_x = 100;
	private int header_y = 100;
	private int answer_x = 180;	
	private int answer_y = 300;
	
	private String full_question_description;
	private String start_message = "";
	private String full_start_message = "";
	private int full_start_counter = 0;
	private String ticker = "";
	private boolean tickerBoolean = true;

	private String currentPlayer;
	
	private int backgroundx,backgroundx2 = 0;
	private int clock,clock2,clock3,timer,timer2 = 0;
	private int currentAnswer = 0;
	private boolean confirmation, end, win, time_out = false;
	private int confirmation_selected = 0;
	private int question_check_counter;
	private int points = 0;
	private Random rand = new Random();
	
	public PlayQuestion(int state, QuestionList ql){	
		this.question_list = ql;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		// Initialize question list
		
		// Create custom font for question
		 try {
			loadFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
			        org.newdawn.slick.util.ResourceLoader.getResourceAsStream("font/visitor2.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 // Load all required fonts
		loadMainFont = loadFont.deriveFont(Font.BOLD,mainFontSize);
		loadQuestionFont = loadFont.deriveFont(Font.PLAIN, questionFontSize);
		loadQuestionBoldFont = loadFont.deriveFont(Font.BOLD, questionFontSize);
		loadTimerFont = loadFont.deriveFont(Font.BOLD,timerFontSize);
		loadTimerMFont = loadFont.deriveFont(Font.BOLD,timerMFontSize);
		mainFont = new BasicFont(loadMainFont);
		timerFont = new BasicFont(loadTimerFont);
		timerMFont = new BasicFont(loadTimerMFont);
		questionFont = new BasicFont(loadQuestionFont);
		questionSelectedFont = new BasicFont(loadQuestionBoldFont, Color.gray);
		
		
		// mainFont = new BasicFont("Arial", Font.PLAIN, mainFontSize);
		// questionFont = new BasicFont("Arial", Font.PLAIN, 26);
		
		/*
		// Load all images
		menubuttona = new Image("img/menubuttona.png");
		menubuttonb = new Image("img/menubuttonb.png");
		menubuttonc = new Image("img/menubuttonc.png");
		menubuttond = new Image("img/menubuttond.png");
		menubuttonah = new Image("img/menubuttonahover.png");
		menubuttonbh = new Image("img/menubuttonbhover.png");
		menubuttonch = new Image("img/menubuttonchover.png");
		menubuttondh = new Image("img/menubuttondhover.png");
		*/
		
	} 
	
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		
		questions = question_list.getQuestion_list();
		no_of_questions = question_list.getNumberOfQuestions();
		
		current_question_id = HRRUClient.cs.getActivity_id();
		current_question = questions[current_question_id];

		current_choices = current_question.getChoices();
		full_question_description = current_question.getFile();
		noOfAnswers = current_question.getAmountOfAnswers();
		correctAnswer = current_question.getAnswer();
		question_difficulty = current_question.getDifficulty();
		backgroundx = 0;
		backgroundx2 = -959;
		
		timer = 50*question_difficulty;
		timer2 = 999;
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		g.drawImage(new Image("img/questionbg.png"), 0, 0);
		g.drawString(mouse, 650, 50);
		
		g.setFont(mainFont.get());
		g.drawString("> " + start_message + "" + ticker, header_x, header_y);
		
		g.setFont(questionFont.get());


		for(int i = 0; i < current_question.getAmountOfAnswers(); i++)
		{
			if(currentAnswer==i)
			{
				g.setFont(questionSelectedFont.get());
				g.drawString(">", choice_x-15, choice_y+(i*30));
			}
			else
				g.setFont(questionFont.get());
			g.drawString(i+1 + ". " + current_choices[i], choice_x, choice_y+(i*30));
		}
		
		g.setFont(questionFont.get());
		if(confirmation==true)
		{
			g.drawImage(new Image("img/answerbox.png"), answer_x, answer_y);
			g.drawString("Is your answer: " + (currentAnswer+1) + "?", answer_x+15, answer_y+15);
			
			if(confirmation_selected==1)
				g.setFont(questionSelectedFont.get());
			
			g.drawString("Yes", answer_x+100, answer_y+40);
			g.setFont(questionFont.get());
			
			if(confirmation_selected==0)
				g.setFont(questionSelectedFont.get());
			g.drawString("No", answer_x+300, answer_y+40);
		}

		
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
			g.drawImage(new Image("img/questionui.png"), 0, 0);
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
				g.drawString("+" + points, 500, 400);
				g.drawString(currentPlayer + "'s new score: playername", 100, 450);
			}
			else if(time_out)
			{
				g.drawString("You have ran out of time!", 100, 100);
				g.drawString("The correct answer was " + current_choices[correctAnswer], 100, 150);
				g.drawString("Overall Points: ", 100, 400);
				g.drawString("+" + points, 500, 400);
				g.drawString(currentPlayer + "'s score:  playername", 100, 450);
			}
			else if(win==false)
			{
				g.drawString("INCORRECT!", 100, 100);
				g.drawString("The correct answer was " + current_choices[correctAnswer], 100, 150);
				g.drawString("Overall Points: ", 100, 400);
				g.drawString("+" + points, 500, 400);
				g.drawString(currentPlayer + "'s score:  playername", 100, 450);
			}
			g.drawString("Press Q to continue...", 500, 530);
		}
		
	} // render
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		Input input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos= Mouse.getY();
		mouse = "xpos: " + xpos + "\nypos: " + ypos;
		currentPlayer = null;
		full_start_message = "Question for " + currentPlayer + "...";
		clock += delta;
		
		if(end==false)
		{
			clock2 += delta;
			clock3 += delta;
			timer2--;
			
			if(confirmation == false)
			{
				if(input.isKeyPressed(Input.KEY_W))
					if(currentAnswer == 0)
						currentAnswer = noOfAnswers - 1;
					else
						currentAnswer--;
				else if(input.isKeyPressed(Input.KEY_S))
					if(currentAnswer == noOfAnswers-1)
						currentAnswer = 0;
					else
						currentAnswer++;
				else if(input.isKeyPressed(Input.KEY_Q))
					confirmation = true;
			}
			else
			{
				if(input.isKeyPressed(Input.KEY_A))
					confirmation_selected = 1;
				else if(input.isKeyPressed(Input.KEY_D))
					confirmation_selected = 0;
				else if(input.isKeyPressed(Input.KEY_Q))
					if(confirmation_selected == 0)
					{
						confirmation = false;
					}
					else if(confirmation_selected == 1)
					{
						clock = 0;
						end = true;
						if(currentAnswer == correctAnswer)
						{
							points = 100 + timer;
							points *= question_difficulty;
							win = true;
						}
						else
							win = false;
					}
						
			}
			
			if(clock>50)
			{
				backgroundx++;
				if(backgroundx>959)
					backgroundx=-959;
				backgroundx2++;
				if(backgroundx2>959)
					backgroundx2=-959;
				clock-=50;
			}	
			
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
		
		if(end==true){
			if(input.isKeyPressed(Input.KEY_Q))
			{
				reset();
				sbg.enterState(1);
			}
		}


		
	} // main
	
	private void reset()
	{
		current_question.setAppeared(1);
		start_message = "";
		full_start_message = "";
		full_start_counter = 0;
		tickerBoolean = true;
		
		current_question_id = rand.nextInt(no_of_questions);
		current_question = questions[current_question_id];
		question_check_counter = 0;
		while(current_question.getAppeared() == 1)
		{
			current_question_id = rand.nextInt(no_of_questions);
			current_question = questions[current_question_id];
			question_check_counter++;
			if(question_check_counter > (no_of_questions*2))
				for(int i = 0; i<no_of_questions; i++)
					questions[i].setAppeared(0);
		}

		current_choices = current_question.getChoices();
		full_question_description = current_question.getFile();
		noOfAnswers = current_question.getAmountOfAnswers();
		correctAnswer = current_question.getAnswer();
		question_difficulty = current_question.getDifficulty();
		
		currentAnswer = 0;
		timer = 50*question_difficulty;
		timer2 = 999;
		clock = 0;
		clock2 = 0;
		clock3 = 0;
		
		confirmation = false;
		win = false;
		end = false;
		time_out = false;
		confirmation_selected = 0;
		currentAnswer = 0;
		points = 0;
	}
	
	public int getID(){
		return 6;
	}
	
}