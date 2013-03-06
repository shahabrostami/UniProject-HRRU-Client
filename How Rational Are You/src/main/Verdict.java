package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;

public class Verdict extends BasicTWLGameState {

	public Client client;
	DialogLayout questionPanel, puzzlePanel, gamePanel;
	private final int number_of_achievements = 6;
	private ResultPercentage[] resultPercentages = new ResultPercentage[number_of_achievements];
	
	int gcw;
	int gch;
	int clock;
	boolean done, enter;
	private final int questionstats = 17;
	private final int bidstats = 20;
	private final int prisonerstats = 21;
	private final int truststats = 22;
	private final int ultstats = 23;
	
	// Ticker variables
	private int titleFontSize = 60;
	private Font loadFont, loadTitleFont;
	private BasicFont titleFont;
	private String start_message = "";
	private String full_start_message = "HOW RATIONAL ARE YOU?";
	private int full_start_counter = 0;
	private String ticker = "";
	private boolean tickerBoolean = true;
	private int clock3, clock2 = 0;
	
	boolean win, highRoller;
	private int bidderPercentage;
	private int coopPercentage;
	private int betrayPercentage;
	private int fastPercentage;
	private int slowPercentage;
	private int knowledgePercentage;

	// Verdict GUI
	DialogLayout verdictPanel;
	Button btnQuestion, btnGame, btnScoreboard, btnEnd;
	
	// Achievement Images
	Image imgBetray, imgHighBidder, imgCoop, imgKnowledge, imgQuick;
	Image imgRoller, imgSlow, imgWinner, imgLose;
	Image ach1, ach2, ach3;
	
	// Rank Images;
	Image ranks[];
	Image rankS, rankA, rankB, rankC, rankD, rankE, rankLetter;
	int rankCounter, rankClockM;
	int rankClock, rankClock1;

	// Player variables
	int playerRank = 0;
	Player player, otherPlayer;
	
	// Achievement coordinates
	private int ach1_x = 71;
	private int ach1_y = 152;
	private int ach2_x = 297;
	private int ach2_y = 152;
	private int ach3_x = 523;
	private int ach3_y = 152;
	
	private int playerID;
	private int playerScore;
	private int otherPlayerScore;
	private int enterState;
	
	public Verdict(int main) {
		client = HRRUClient.conn.getClient();
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		/*
		System.out.println(playerScore);
		activityScore = new ActivityScore(1, 100, 3, 30, 330, true);
		activityScore.setActivity_id(2);
		HRRUClient.cs.getP1().addActivityScore(activityScore);
		
		activityScore = new ActivityScore(3, 100, 3, 50, 150, true);
		activityScore.setActivity_id(3);
		HRRUClient.cs.getP1().addActivityScore(activityScore);
		
		activityScore = new ActivityScore(3, 0, 1, 0, 0, false);
		activityScore.setActivity_id(5);
		HRRUClient.cs.getP1().addActivityScore(activityScore);
		
		TrustScore trustScore = new TrustScore(1, 2, 200, 600, 100, 150, 3, 250, 150);
		HRRUClient.cs.getP1().addTrustScore(trustScore);
		
		trustScore = new TrustScore(1, 2, 100, 300, 50, 75, 3, 125, 75);
		HRRUClient.cs.getP1().addTrustScore(trustScore);
		
		trustScore = new TrustScore(2, 1, 200, 600, 100, 150, 3, 250, 150);
		HRRUClient.cs.getP1().addTrustScore(trustScore);
		
		trustScore = new TrustScore(2, 1, 100, 300, 50, 75, 3, 125, 75);
		HRRUClient.cs.getP1().addTrustScore(trustScore);
		
		PrisonScore prisonScore = new PrisonScore(0, 0, 0, 0, true, true);
		HRRUClient.cs.getP1().addPrisonScore(prisonScore);
		prisonScore = new PrisonScore(0, 0, 0, 0, true, true);
		HRRUClient.cs.getP1().addPrisonScore(prisonScore);
		prisonScore = new PrisonScore(0, 0, 0, 0, true, true);
		HRRUClient.cs.getP1().addPrisonScore(prisonScore);
		prisonScore = new PrisonScore(0, 1, 0, 0, true, true);
		HRRUClient.cs.getP1().addPrisonScore(prisonScore);
		prisonScore = new PrisonScore(0, 1, 0, 0, true, true);
		HRRUClient.cs.getP1().addPrisonScore(prisonScore);
		prisonScore = new PrisonScore(0, 1, 0, 0, true, true);
		HRRUClient.cs.getP1().addPrisonScore(prisonScore);
		prisonScore = new PrisonScore(1, 1, 0, 0, true, true);
		HRRUClient.cs.getP1().addPrisonScore(prisonScore);
		
		UltimatumScore ultimatumScore = new UltimatumScore(1, 2, 100, 150, 250, true);
		HRRUClient.cs.getP1().addUltimatumScore(ultimatumScore);
		ultimatumScore = new UltimatumScore(1, 2, 50, 150, 200, true);
		HRRUClient.cs.getP1().addUltimatumScore(ultimatumScore);
		ultimatumScore = new UltimatumScore(1, 2, 100, 50, 150, true);
		HRRUClient.cs.getP1().addUltimatumScore(ultimatumScore);
		*/
		// RESET VARIABLES
		start_message = "";
		full_start_message = "HOW RATIONAL ARE YOU?";
		full_start_counter = 0;
		ticker = "";
		tickerBoolean = true;
		clock2 = 0;
		clock3 = 0;
		enterState = 0;
		
		playerID = HRRUClient.cs.getPlayer();
		if(playerID == 1)
		{
			player = HRRUClient.cs.getP1();
			otherPlayer = HRRUClient.cs.getP2();
			playerScore = HRRUClient.cs.getP1().getScore();
			otherPlayerScore = HRRUClient.cs.getP2().getScore();
		}
		else
		{
			player = HRRUClient.cs.getP2();
			otherPlayer = HRRUClient.cs.getP1();
			playerScore = HRRUClient.cs.getP2().getScore();
			otherPlayerScore = HRRUClient.cs.getP2().getScore();
		}
	
		
		if(!enter)
		{
			sbg.addState(new QuestionStatistics(questionstats));
			sbg.getState(questionstats).init(gc, sbg);
			sbg.addState(new BidStatistics(bidstats));
			sbg.getState(bidstats).init(gc, sbg);
			sbg.addState(new PrisonerStatistics(prisonerstats));
			sbg.getState(prisonerstats).init(gc, sbg);
			sbg.addState(new TrustStatistics(truststats));
			sbg.getState(truststats).init(gc, sbg);
			sbg.addState(new UltimatumStatistics(ultstats));
			sbg.getState(ultstats).init(gc, sbg);
			
			if(playerScore > otherPlayerScore)
			{
				win = true;
				ach1 = imgWinner;
			}
			else
			{
				win = false;
				ach1 = imgLose;
			}
			BiddingScoreResult biddingScore = player.getBiddingScoreResult();
			if((biddingScore.getBsItemValueTotalL() + biddingScore.getBsItemValueTotalW()) > 0)
				bidderPercentage =  (int) (((biddingScore.getBsPlayerBidTotalL() + biddingScore.getBsPlayerBidTotalL()) /
					(biddingScore.getBsItemValueTotalL() + biddingScore.getBsItemValueTotalW())) + 0.5 * 100);
			
			if(player.getTotalRolled() > otherPlayer.getTotalRolled())
				highRoller = true;
			
			QuestionScoreResult questionScore = player.getQuestionScoreResult();
			if(questionScore.getNoOfTotalQuestions()>0)
			{
				fastPercentage = (int) (questionScore.getTotalQTimeBonusOverall() / (questionScore.getNoOfTotalQuestions() * 80) + 0.5 * 100);
				slowPercentage = (int) (100 -  fastPercentage);
				knowledgePercentage = questionScore.getNoOfTotalQCorrect() / questionScore.getNoOfTotalQuestions();
			}
			
			PrisonerScoreResult prisonScore = player.getPrisonerScoreResult();
			if(prisonScore.getNoOfPrisonScores() > 0)
			{
				coopPercentage = prisonScore.getNoOfPrisonScoreCoop() / prisonScore.getNoOfPrisonScores();
				betrayPercentage = 100 - coopPercentage;
			}
			
			resultPercentages[0] = new ResultPercentage("bid", imgHighBidder, bidderPercentage);
			resultPercentages[1] = new ResultPercentage("fast", imgQuick, fastPercentage);
			resultPercentages[2] = new ResultPercentage("slow", imgSlow, slowPercentage);
			resultPercentages[3] = new ResultPercentage("knowledge", imgKnowledge, knowledgePercentage);
			resultPercentages[4] = new ResultPercentage("coop", imgCoop, coopPercentage);
			resultPercentages[5] = new ResultPercentage("betray", imgBetray, betrayPercentage);
			
			Arrays.sort(resultPercentages);
			
			ach2 = resultPercentages[0].getImgRank();
			ach3 = resultPercentages[1].getImgRank();
			
			if(ach3 == null)
				ach3 = imgRoller;
			enter = true;
		}
		// Create Question UI
		rootPane.removeAllChildren();
		rootPane.add(verdictPanel);
		rootPane.add(btnQuestion);
		rootPane.add(btnGame);
		rootPane.add(btnScoreboard);
		rootPane.add(btnEnd);
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
		/*
		player = new Player("player1");
		player.setPlayerCharacterID(2);
		player.setPlayerCharacter(characters[2]);
		player.setScore(10000);
		HRRUClient.cs.setP1(player);
		HRRUClient.cs.setP2(player);
		HRRUClient.cs.setPlayer(1);
		*/
		
		enter = false;
		// GUI
		btnQuestion = new Button("Question\nFeedback");
		btnQuestion.setSize(100, 50);
		btnQuestion.setPosition(75, 490);
		btnQuestion.addCallback(new Runnable() {
			@Override
			public void run() {
				enterState = 1;
			}
		});
		btnQuestion.setTheme("verdictbutton");
		
		btnGame = new Button("Game\nFeedback");
		btnGame.setSize(100, 50);
		btnGame.setPosition(180,490);
		btnGame.addCallback(new Runnable() {
			@Override
			public void run() {
				enterState = 2;
			}
		});
		btnGame.setTheme("verdictbutton");
		
		btnScoreboard = new Button("Scoreboard");
		btnScoreboard.setSize(100, 50);
		btnScoreboard.setPosition(285,490);
		btnScoreboard.addCallback(new Runnable() {
			@Override
			public void run() {
				enterState = 3;
			}
		});
		btnScoreboard.setTheme("verdictbutton");
		
		btnEnd = new Button("Finish");
		btnEnd.setSize(100, 50);
		btnEnd.setPosition(390,490);
		btnEnd.addCallback(new Runnable() {
			@Override
			public void run() {
				enterState = 4;
			}
		});
		btnEnd.setTheme("verdictbutton");
		
		
		gcw = gc.getWidth();
		gch = gc.getHeight();
		done = false;
		// setup font variables
		try {
			loadFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
					      org.newdawn.slick.util.ResourceLoader.getResourceAsStream("font/visitor2.ttf"));
		} catch (FontFormatException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
		loadTitleFont = loadFont.deriveFont(Font.BOLD,titleFontSize);
		titleFont = new BasicFont(loadTitleFont);
		
		// setup achievement images
		imgBetray = new Image("achievement/achievement_betrayer.png");
		imgHighBidder = new Image("achievement/achievement_coin.png");
		imgCoop = new Image("achievement/achievement_coop.png");
		imgKnowledge = new Image("achievement/achievement_knowledgeable.png");
		imgQuick = new Image("achievement/achievement_quick.png");
		imgRoller = new Image("achievement/achievement_roller.png");
		imgSlow = new Image("achievement/achievement_slow.png");
		imgWinner = new Image("achievement/achievement_winner2.png");
		imgLose = new Image("achievement/achievement_lose.png");
		
		// setup rank images
		rankA = new Image("achievement/rank/A.png");
		rankB = new Image("achievement/rank/B.png");
		rankC = new Image("achievement/rank/C.png");
		rankD = new Image("achievement/rank/D.png");
		rankE = new Image("achievement/rank/E.png");
		rankS = new Image("achievement/rank/S.png");
		ranks = new Image[6];
		ranks[0] = rankS;
		ranks[1] = rankA;
		ranks[2] = rankB;
		ranks[3] = rankC;
		ranks[4] = rankD;
		ranks[5] = rankE;
		rankLetter = rankE;
		
		verdictPanel = new DialogLayout();
		verdictPanel.setPosition(50,100);
		verdictPanel.setSize(701-170-10, 462-270-20);
		verdictPanel.setTheme("verdict-panel");
		
		/*
		verdictPanel.setHorizontalGroup(verdictPanel.createParallelGroup()
				.addGroup(verdictPanel.createSequentialGroup(btnQStats, btnGStats, btnFeedback, btnEnd)));
		
		verdictPanel.setVerticalGroup(verdictPanel.createParallelGroup()
				.addGroup(verdictPanel.createParallelGroup(btnQStats, btnGStats, btnFeedback, btnEnd)));
				*/
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(new Image("simple/questionbg.png"), 0, 0);
		g.drawImage(ach1, ach1_x, ach1_y);
		g.drawImage(ach2, ach2_x, ach2_y);
		g.drawImage(ach3, ach3_x, ach3_y);
		g.drawImage(rankLetter, 525, 355);
		g.setFont(titleFont.get());
		g.drawString("> " + start_message + "" + ticker, 30, 25);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		clock+=delta;
		rankClock+=delta;
		clock3 += delta;
		clock2 += delta;
		
		// full message ticker
		if(clock3 > 100){
			if(full_start_counter < full_start_message.length()) {
				start_message += full_start_message.substring(full_start_counter, full_start_counter+1);
				full_start_counter++;
				clock3-=100;
			}
		}
		// ticker symbol
		if(clock2>999) {
			clock2-=1000;
			if(tickerBoolean) {
				ticker = "|";
				tickerBoolean = false;
			}
			else {
				ticker = "";
				tickerBoolean = true;
			}
		}
		
		if(!done) {
			// timer tick for rank image
			if(clock < 1000)
				rankClockM = 20;
			else if(clock <2000)
				rankClockM = 40;
			else if(clock <3000)
				rankClockM = 80;
			else if(clock <4000)
				rankClockM = 120;
			else if(clock <5000)
				rankClockM = 240;
			else if(clock <6000)
				rankClockM = 480;
			else
				rankClock = 1;
		
			if(rankClock % rankClockM < 10 && rankClock != 1) {
					rankLetter = ranks[rankCounter];
					rankCounter++;
					if(rankCounter==6)
						rankCounter=0;
					rankClock = 0;
			}
			// set rank image
			else if(rankClock == 1) {
				rankLetter = ranks[playerRank];
				done = true;
			}
		}
		
		if(enterState == 1)
		{
			sbg.enterState(questionstats);
		}
		else if(enterState == 2)
		{
			sbg.enterState(bidstats);
		}
	}

	@Override
	public int getID() {
		return 15;
	}

}