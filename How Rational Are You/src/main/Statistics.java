package main;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryonet.Client;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;

public class Statistics extends BasicTWLGameState {

	public Client client;
	DialogLayout questionPanel, puzzlePanel, gamePanel;
	
	int gcw;
	int gch;
	
	// Questions UI
	Label lQAmount, lQEasy, lQEasyCorrect, lQEasyTimeBonusAvg, lQEasyTimeBonusOverall, lQEasyPointsAvg, lQEasyPointsOverall;
	Label lQMedium, lQMediumCorrect, lQMediumTimeBonusAvg, lQMediumTimeBonusOverall, lQMediumPointsAvg, lQMediumPointsOverall;
	Label lQHard, lQHardCorrect, lQHardTimeBonusAvg, lQHardTimeBonusOverall, lQHardPointsAvg, lQHardPointsOverall;
	Label lQTotal, lQTotalCorrect, lQTotalTimeBonusAvg, lQTotalTimeBonusOverall, lQTotalPointsAvg, lQTotalPointsOverall;
	
	Label lQAmountR, lQEasyR, lQEasyCorrectR, lQEasyTimeBonusAvgR, lQEasyTimeBonusOverallR, lQEasyPointsAvgR, lQEasyPointsOverallR;
	Label lQMediumR, lQMediumCorrectR, lQMediumTimeBonusAvgR, lQMediumTimeBonusOverallR, lQMediumPointsAvgR, lQMediumPointsOverallR;
	Label lQHardR, lQHardCorrectR, lQHardTimeBonusAvgR, lQHardTimeBonusOverallR, lQHardPointsAvgR, lQHardPointsOverallR;
	Label lQTotalR, lQTotalCorrectR, lQTotalTimeBonusAvgR, lQTotalTimeBonusOverallR, lQTotalPointsAvgR, lQTotalPointsOverallR;

	
	// Question variables
	private ActivityScore activityScore;
	private ArrayList<ActivityScore> activityScores;
	private ArrayList<ActivityScore> mediumActivityScores;
	private ArrayList<ActivityScore> hardActivityScores;
	
	private int noOfEasyQuestions;
	private int noOfEasyQCorrect;
	private int easyQTimeBonusAvg;
	private double easyQTimeBonusOverall;
	private int easyQPointsAvg;
	private double easyQPointsOverall;
	
	private int noOfMediumQuestions;
	private int noOfMediumQCorrect;
	private int mediumQTimeBonusAvg;
	private double mediumQTimeBonusOverall;
	private int mediumQPointsAvg;
	private double mediumQPointsOverall;
	
	private int noOfHardQuestions;
	private int noOfHardQCorrect;
	private int hardQTimeBonusAvg;
	private double hardQTimeBonusOverall;
	private int hardQPointsAvg;
	private double hardQPointsOverall;
	
	private int noOfTotalQuestions;
	private int noOfTotalQCorrect;
	private int totalQTimeBonusAvg;
	private double totalQTimeBonusOverall;
	private int totalQPointsAvg;
	private double totalQPointsOverall;
	

	private int player;
	private int playerScore;
	
	public Statistics(int main) {
		client = HRRUClient.conn.getClient();
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		
		/*
		HRRUClient.cs.setP1(new Player("derp"));
		ActivityScore activityScore = new ActivityScore(1, 100, 2, 30, 130, true);
		activityScore.setActivity_id(1);
		HRRUClient.cs.getP1().addActivityScore(activityScore);
		
		activityScore = new ActivityScore(1, 100, 3, 30, 330, true);
		activityScore.setActivity_id(2);
		HRRUClient.cs.getP1().addActivityScore(activityScore);
		
		activityScore = new ActivityScore(3, 100, 3, 50, 150, true);
		activityScore.setActivity_id(3);
		HRRUClient.cs.getP1().addActivityScore(activityScore);
		
		activityScore = new ActivityScore(3, 0, 1, 0, 0, false);
		activityScore.setActivity_id(5);
		HRRUClient.cs.getP1().addActivityScore(activityScore);
		
		activityScores = HRRUClient.cs.getP1().getActivityScores();
		*/
		
		player = HRRUClient.cs.getPlayer();
		if(player == 1)
		{
			playerScore = HRRUClient.cs.getP1().getScore();
			activityScores = HRRUClient.cs.getP1().getActivityScores();
		}
		else
		{
			playerScore = HRRUClient.cs.getP2().getScore();
			activityScores = HRRUClient.cs.getP2().getActivityScores();
		}
		
		// Reset Question result variables
	    noOfEasyQuestions = 0;
		easyQTimeBonusAvg = 0;
		easyQTimeBonusOverall = 0;
		easyQPointsAvg = 0;
		easyQPointsOverall = 0;
		
		noOfMediumQuestions = 0;
		mediumQTimeBonusAvg = 0;
		mediumQTimeBonusOverall = 0;
		mediumQPointsAvg = 0;
		mediumQPointsOverall = 0;
		
		noOfHardQuestions = 0;
		hardQTimeBonusAvg = 0;
		hardQTimeBonusOverall = 0;
		hardQPointsAvg = 0;
		hardQPointsOverall = 0;
		
		noOfTotalQuestions = 0;
		totalQTimeBonusAvg = 0;
		totalQTimeBonusOverall = 0;
		totalQPointsAvg = 0;
		totalQPointsOverall = 0;
		
		// Calculate Question statistics
		for(int i = 0; i < activityScores.size(); i++)
		{
			// Calculate Total statistics
			noOfTotalQuestions++;
			activityScore = activityScores.get(i);
			totalQTimeBonusOverall += activityScore.getElapsedtime();
			totalQPointsOverall += activityScore.getOverall();
			if(activityScore.getCorrect())
				noOfTotalQCorrect++;
			
			// Calculate Easy statistics
			if(activityScore.getDifficulty() == 1)
			{
				noOfEasyQuestions++; 
				easyQTimeBonusOverall += activityScore.getElapsedtime();
				easyQPointsOverall += activityScore.getOverall();
				if(activityScore.getCorrect())
					noOfEasyQCorrect++;
			}
			// Calculate Medium statistics
			else if(activityScore.getDifficulty() == 2)
			{
				noOfMediumQuestions++; 
				mediumQTimeBonusOverall += activityScore.getElapsedtime();
				mediumQPointsOverall += activityScore.getOverall();
				if(activityScore.getCorrect())
					noOfMediumQCorrect++;
			}
			// Calculate Hard statistics
			else if(activityScore.getDifficulty() == 3)
			{
				noOfHardQuestions++; 
				hardQTimeBonusOverall += activityScore.getElapsedtime();
				hardQPointsOverall += activityScore.getOverall();
				if(activityScore.getCorrect())
					noOfHardQCorrect++;
			}
		}
		// Calculate Average statistics
		if(noOfEasyQuestions > 0)
		{
			easyQPointsAvg = (int) (easyQPointsOverall / noOfEasyQuestions + 0.5);
			easyQTimeBonusAvg = (int) (easyQTimeBonusOverall / noOfEasyQuestions + 0.5);
		}
		if(noOfMediumQuestions > 0)
		{
			mediumQPointsAvg = (int) (mediumQPointsOverall / noOfMediumQuestions + 0.5);
			mediumQTimeBonusAvg = (int) (mediumQTimeBonusOverall / noOfMediumQuestions + 0.5);
		}
		if(noOfHardQuestions > 0)
		{
			hardQPointsAvg = (int) (hardQPointsOverall / noOfHardQuestions + 0.5);
			hardQTimeBonusAvg = (int) (hardQTimeBonusOverall / noOfHardQuestions + 0.5);
		}
		if(noOfTotalQuestions > 0)
		{
			totalQPointsAvg = (int) (totalQPointsOverall / noOfTotalQuestions + 0.5);
			totalQTimeBonusAvg = (int) (totalQTimeBonusOverall / noOfTotalQuestions + 0.5);
		}
		
		// Create Question UI
		lQAmountR.setText("" + noOfTotalQuestions);
		
		lQEasy.setText("Easy: " + noOfEasyQuestions + " Questions");
		lQEasyR.setText("");
		lQEasyCorrectR.setText("" + noOfEasyQCorrect + "/" + noOfEasyQuestions);
		lQEasyTimeBonusAvgR.setText("" + easyQTimeBonusAvg);
		lQEasyTimeBonusOverallR.setText("" + (int)easyQTimeBonusOverall); 
		lQEasyPointsAvgR.setText("" + easyQPointsAvg); 
		lQEasyPointsOverallR.setText("" + (int)easyQPointsOverall);
		
		lQMedium.setText("Medium: " + noOfMediumQuestions + " Questions");
		lQMediumR.setText("");
		lQMediumCorrectR.setText("" + noOfMediumQCorrect + "/" + noOfMediumQuestions);
		lQMediumTimeBonusAvgR.setText("" + mediumQTimeBonusAvg);
		lQMediumTimeBonusOverallR.setText("" + (int)mediumQTimeBonusOverall); 
		lQMediumPointsAvgR.setText("" + mediumQPointsAvg); 
		lQMediumPointsOverallR.setText("" + (int)mediumQPointsOverall);
        
		lQHard.setText("Hard: " + noOfHardQuestions + " Questions");
		lQHardR.setText("");
		lQHardCorrectR.setText("" + noOfHardQCorrect + "/" + noOfHardQuestions);
		lQHardTimeBonusAvgR.setText("" + hardQTimeBonusAvg);
		lQHardTimeBonusOverallR.setText("" + (int)hardQTimeBonusOverall); 
		lQHardPointsAvgR.setText("" + hardQPointsAvg); 
		lQHardPointsOverallR.setText("" + (int)hardQPointsOverall);
		
		lQTotal.setText("Total: " + noOfTotalQuestions + " Questions");
		lQTotalR.setText("");
		lQTotalCorrectR.setText("" + noOfTotalQCorrect + "/" + noOfTotalQuestions);
		lQTotalTimeBonusAvgR.setText("" + totalQTimeBonusAvg);
		lQTotalTimeBonusOverallR.setText("" + (int)totalQTimeBonusOverall); 
		lQTotalPointsAvgR.setText("" + totalQPointsAvg); 
		lQTotalPointsOverallR.setText("" + (int)totalQPointsOverall);
		
		rootPane.add(questionPanel);
		rootPane.add(puzzlePanel);
		rootPane.add(gamePanel);
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
		
		questionPanel = new DialogLayout();
        questionPanel.setTheme("questionstat-panel");
        questionPanel.setSize(220,435);
        questionPanel.setPosition(20,100);
        
        puzzlePanel = new DialogLayout();
        puzzlePanel.setTheme("puzzlestat-panel");
        puzzlePanel.setSize(220,435);
        puzzlePanel.setPosition(280,100);
        
        gamePanel = new DialogLayout();
        gamePanel.setTheme("gamestat-panel");
        gamePanel.setSize(220,435);
        gamePanel.setPosition(540,100);
        
		lQAmount = new Label("Number of Questions: ");
		lQAmountR = new Label("");
		lQAmount.setTheme("questionatari8");
		lQAmountR.setTheme("questionatari8r");
		
		lQEasy = new Label("Easy: 5 Questions");
		lQEasyR = new Label("");
		lQEasy.setTheme("questionatari9y");
		lQEasyR.setTheme("questionatari9y");
		
		lQEasyCorrect = new Label("Correctly Answered: ");
		lQEasyCorrectR = new Label("");
		lQEasyCorrect.setTheme("questionatari8r");
		lQEasyCorrectR.setTheme("questionatari8r");
		
		lQEasyTimeBonusAvg = new Label("Time Bonus Avg: ");
		lQEasyTimeBonusAvgR = new Label("");
		lQEasyTimeBonusAvg.setTheme("questionatari8r");
		lQEasyTimeBonusAvgR.setTheme("questionatari8r");
		
		lQEasyTimeBonusOverall = new Label("Time Bonus Total: ");
		lQEasyTimeBonusOverallR = new Label("");
		lQEasyTimeBonusOverall.setTheme("questionatari8r");
		lQEasyTimeBonusOverallR.setTheme("questionatari8r");
		
		lQEasyPointsAvg = new Label("Points Avg: ");
		lQEasyPointsAvgR = new Label("");
		lQEasyPointsAvg.setTheme("questionatari8r");
		lQEasyPointsAvgR.setTheme("questionatari8r");
		
		lQEasyPointsOverall = new Label("Points Total: ");
		lQEasyPointsOverallR = new Label("");
		lQEasyPointsOverall.setTheme("questionatari8r");
		lQEasyPointsOverallR.setTheme("questionatari8r");
		
		lQMedium = new Label("Medium: 5 Questions");
		lQMediumR = new Label("");
		lQMedium.setTheme("questionatari9y");
		lQMediumR.setTheme("questionatari9y");
		
		lQMediumCorrect = new Label("Correctly Answered: ");
		lQMediumCorrectR = new Label("");
		lQMediumCorrect.setTheme("questionatari8r");
		lQMediumCorrectR.setTheme("questionatari8r");
		
		lQMediumTimeBonusAvg = new Label("Time Bonus Avg: ");
		lQMediumTimeBonusAvgR = new Label("");
		lQMediumTimeBonusAvg.setTheme("questionatari8r");
		lQMediumTimeBonusAvgR.setTheme("questionatari8r");
		
		lQMediumTimeBonusOverall = new Label("Time Bonus Total: ");
		lQMediumTimeBonusOverallR = new Label("555");
		lQMediumTimeBonusOverall.setTheme("questionatari8r");
		lQMediumTimeBonusOverallR.setTheme("questionatari8r");
		
		lQMediumPointsAvg = new Label("Points Avg: ");
		lQMediumPointsAvgR = new Label("");
		lQMediumPointsAvg.setTheme("questionatari8r");
		lQMediumPointsAvgR.setTheme("questionatari8r");
		
		lQMediumPointsOverall = new Label("Points Total: ");
		lQMediumPointsOverallR = new Label("");
		lQMediumPointsOverall.setTheme("questionatari8r");
		lQMediumPointsOverallR.setTheme("questionatari8r");
		
		lQHard = new Label("Hard: 5 Questions");
		lQHardR = new Label("");
		lQHard.setTheme("questionatari9y");
		lQHardR.setTheme("questionatari9y");
		
		lQHardCorrect = new Label("Correctly Answered: ");
		lQHardCorrectR = new Label("");
		lQHardCorrect.setTheme("questionatari8r");
		lQHardCorrectR.setTheme("questionatari8r");
		
		lQHardTimeBonusAvg = new Label("Time Bonus Avg: ");
		lQHardTimeBonusAvgR = new Label("");
		lQHardTimeBonusAvg.setTheme("questionatari8r");
		lQHardTimeBonusAvgR.setTheme("questionatari8r");
		
		lQHardTimeBonusOverall = new Label("Time Bonus Total: ");
		lQHardTimeBonusOverallR = new Label("");
		lQHardTimeBonusOverall.setTheme("questionatari8r");
		lQHardTimeBonusOverallR.setTheme("questionatari8r");
		
		lQHardPointsAvg = new Label("Points Avg: ");
		lQHardPointsAvgR = new Label("");
		lQHardPointsAvg.setTheme("questionatari8r");
		lQHardPointsAvgR.setTheme("questionatari8r");
		
		lQHardPointsOverall = new Label("Points Total: ");
		lQHardPointsOverallR = new Label("");
		lQHardPointsOverall.setTheme("questionatari8r");
		lQHardPointsOverallR.setTheme("questionatari8r");
		
		lQTotal = new Label("Total: 5 Questions");
		lQTotalR = new Label("");
		lQTotal.setTheme("questionatari9y");
		lQTotalR.setTheme("questionatari9y");
		
		lQTotalCorrect = new Label("Correctly Answered: ");
		lQTotalCorrectR = new Label("");
		lQTotalCorrect.setTheme("questionatari8r");
		lQTotalCorrectR.setTheme("questionatari8r");
		
		lQTotalTimeBonusAvg = new Label("Time Bonus Avg: ");
		lQTotalTimeBonusAvgR = new Label("");
		lQTotalTimeBonusAvg.setTheme("questionatari8r");
		lQTotalTimeBonusAvgR.setTheme("questionatari8r");
		
		lQTotalTimeBonusOverall = new Label("Time Bonus Total: ");
		lQTotalTimeBonusOverallR = new Label("");
		lQTotalTimeBonusOverall.setTheme("questionatari8r");
		lQTotalTimeBonusOverallR.setTheme("questionatari8r");
		
		lQTotalPointsAvg = new Label("Points Avg: ");
		lQTotalPointsAvgR = new Label("");
		lQTotalPointsAvg.setTheme("questionatari8r");
		lQTotalPointsAvgR.setTheme("questionatari8r");
		
		lQTotalPointsOverall = new Label("Points Total: ");
		lQTotalPointsOverallR = new Label("");
		lQTotalPointsOverall.setTheme("questionatari8r");
		lQTotalPointsOverallR.setTheme("questionatari8r");
		
		 DialogLayout.Group hQLeft = questionPanel.createParallelGroup(lQAmount, lQEasy, lQMedium, lQHard, lQTotal);
	        DialogLayout.Group hQRight = questionPanel.createParallelGroup(lQAmountR, lQEasyR, lQMediumR, lQHardR, lQTotalR);
	        
	        DialogLayout.Group hQEasyLeft = questionPanel.createParallelGroup(
	        		lQEasyCorrect, lQEasyTimeBonusAvg, lQEasyTimeBonusOverall, lQEasyPointsAvg, lQEasyPointsOverall, 
	        		lQMediumCorrect, lQMediumTimeBonusAvg, lQMediumTimeBonusOverall, lQMediumPointsAvg, lQMediumPointsOverall,
	        		lQHardCorrect, lQHardTimeBonusAvg, lQHardTimeBonusOverall, lQHardPointsAvg, lQHardPointsOverall,
	        		lQTotalCorrect, lQTotalTimeBonusAvg, lQTotalTimeBonusOverall, lQTotalPointsAvg, lQTotalPointsOverall);
	        
	        DialogLayout.Group hQEasyRight = questionPanel.createParallelGroup(
	        		lQEasyCorrectR, lQEasyTimeBonusAvgR, lQEasyTimeBonusOverallR, lQEasyPointsAvgR, lQEasyPointsOverallR,
	        		lQMediumCorrectR, lQMediumTimeBonusAvgR, lQMediumTimeBonusOverallR, lQMediumPointsAvgR, lQMediumPointsOverallR,
	        		lQHardCorrectR, lQHardTimeBonusAvgR, lQHardTimeBonusOverallR, lQHardPointsAvgR, lQHardPointsOverallR,
	        		lQTotalCorrectR, lQTotalTimeBonusAvgR, lQTotalTimeBonusOverallR, lQTotalPointsAvgR, lQTotalPointsOverallR);
	        
	        questionPanel.setHorizontalGroup(questionPanel.createParallelGroup()
	        		.addGroup(questionPanel.createSequentialGroup(hQLeft, hQRight))
	        		.addGroup(questionPanel.createSequentialGroup(hQEasyLeft, hQEasyRight)));
	        
	        questionPanel.setVerticalGroup(questionPanel.createSequentialGroup()
	        		.addGroup(questionPanel.createParallelGroup(lQAmount, lQAmountR))
	        		
	        		.addGap(10).addGroup(questionPanel.createParallelGroup(lQEasy, lQEasyR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQEasyCorrect, lQEasyCorrectR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQEasyTimeBonusAvg, lQEasyTimeBonusAvgR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQEasyTimeBonusOverall, lQEasyTimeBonusOverallR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQEasyPointsAvg, lQEasyPointsAvgR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQEasyPointsOverall, lQEasyPointsOverallR))
	        		
	        		.addGap(10).addGroup(questionPanel.createParallelGroup(lQMedium, lQMediumR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQMediumCorrect, lQMediumCorrectR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQMediumTimeBonusAvg, lQMediumTimeBonusAvgR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQMediumTimeBonusOverall, lQMediumTimeBonusOverallR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQMediumPointsAvg, lQMediumPointsAvgR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQMediumPointsOverall, lQMediumPointsOverallR))
	        		
	        		.addGap(10).addGroup(questionPanel.createParallelGroup(lQHard, lQHardR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQHardCorrect, lQHardCorrectR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQHardTimeBonusAvg, lQHardTimeBonusAvgR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQHardTimeBonusOverall, lQHardTimeBonusOverallR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQHardPointsAvg, lQHardPointsAvgR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQHardPointsOverall, lQHardPointsOverallR))
	        		
	        		.addGap(20).addGroup(questionPanel.createParallelGroup(lQTotal, lQTotalR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQTotalCorrect, lQTotalCorrectR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQTotalTimeBonusAvg, lQTotalTimeBonusAvgR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQTotalTimeBonusOverall, lQTotalTimeBonusOverallR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQTotalPointsAvg, lQTotalPointsAvgR))
	        		.addGap(5).addGroup(questionPanel.createParallelGroup(lQTotalPointsOverall, lQTotalPointsOverallR)));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
	
	}

	@Override
	public int getID() {
		return 3;
	}

}