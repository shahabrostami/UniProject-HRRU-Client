package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryonet.Client;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;

public class Tutorial extends BasicTWLGameState {

	public Client client;
	DialogLayout p1ResultPanel, p2ResultPanel;
	
	int gcw;
	int gch;
	
	Label lTitle, lPoints, lDifficulty, lTime, lOverall, lNew;
	Label lPoints2, lDifficulty2, lTime2, lOverall2, lNew2;
	Label lblPoints1, lblDifficulty1, lblTime1, lblOverall1, lblNew1;
	Label lblPoints2, lblDifficulty2, lblTime2, lblOverall2, lblNew2;
	
	public Tutorial(int main) {
		client = HRRUClient.conn.getClient();
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		lTitle = new Label("How did everyone do...");
        lTitle.setPosition(80,80);
		lTitle.setTheme("title");
		
        p1ResultPanel = new DialogLayout();
        p1ResultPanel.setTheme("correct-panel");
		
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
		
		lPoints = new Label("Points:");
		lDifficulty = new Label("Difficulty:");
		lTime = new Label("Time Bonus:");
		lOverall = new Label("Overall Points:");
		lNew = new Label("New Total:");
		lNew.setTheme("labelscoretotal");
		
		lPoints2 = new Label("Points:");
		lDifficulty2 = new Label("Difficulty:");
		lTime2 = new Label("Time Bonus:");
		lOverall2 = new Label("Overall Points:");
		lNew2 = new Label("New Total:");
		lNew2.setTheme("labelscoretotal");
				
		lblPoints1 = new Label("100");
		lblPoints1.setTheme("labelright");
		lblDifficulty1 = new Label("Easy X1");
		lblDifficulty1.setTheme("labelright");
		lblTime1 = new Label("230");
		lblTime1.setTheme("labelright");
		lblOverall1 = new Label("330");
		lblOverall1.setTheme("labelright");
		lblNew1 = new Label("900");
		lblNew1.setTheme("labelscoretotalright");
		
		lblPoints2 = new Label("422");
		lblPoints2.setTheme("labelright");
		lblDifficulty2 = new Label("Hard X3");
		lblDifficulty2.setTheme("labelright");
		lblTime2 = new Label("22");
		lblTime2.setTheme("labelright");
		lblOverall2 = new Label("45");
		lblOverall2.setTheme("labelright");
		lblNew2 = new Label("900");
		lblNew2.setTheme("labelscoretotalright");
		
		DialogLayout.Group hLabels1 = p1ResultPanel.createParallelGroup(lPoints, lDifficulty, lTime, lOverall, lNew).addGap(200);
		DialogLayout.Group hP1Result1 = p1ResultPanel.createParallelGroup(lblPoints1, lblDifficulty1, lblTime1, lblOverall1, lblNew1);
	
		p1ResultPanel.setHorizontalGroup(p1ResultPanel.createParallelGroup()
				.addGap(120).addGroup(p1ResultPanel.createSequentialGroup(hLabels1, hP1Result1)));
		
		p1ResultPanel.setVerticalGroup(p1ResultPanel.createSequentialGroup()
				.addGap(80).addGroup(p1ResultPanel.createParallelGroup(lPoints, lblPoints1))
				.addGap(30).addGroup(p1ResultPanel.createParallelGroup(lDifficulty, lblDifficulty1))
				.addGap(30).addGroup(p1ResultPanel.createParallelGroup(lTime, lblTime1))
				.addGap(30).addGroup(p1ResultPanel.createParallelGroup(lOverall, lblOverall1))
				.addGap(50).addGroup(p1ResultPanel.createParallelGroup(lNew, lblNew1)));
		
		DialogLayout.Group hLabels2 = p2ResultPanel.createParallelGroup(lPoints2, lDifficulty2, lTime2, lOverall2, lNew2).addGap(200);
		DialogLayout.Group hP1Result2 = p2ResultPanel.createParallelGroup(lblPoints2, lblDifficulty2, lblTime2, lblOverall2, lblNew2);
		
		p2ResultPanel.setHorizontalGroup(p2ResultPanel.createParallelGroup()
				.addGap(120).addGroup(p2ResultPanel.createSequentialGroup(hLabels2, hP1Result2)));
		
		p2ResultPanel.setVerticalGroup(p2ResultPanel.createSequentialGroup()
				.addGap(80).addGroup(p2ResultPanel.createParallelGroup(lPoints2, lblPoints2))
				.addGap(30).addGroup(p2ResultPanel.createParallelGroup(lDifficulty2, lblDifficulty2))
				.addGap(30).addGroup(p2ResultPanel.createParallelGroup(lTime2, lblTime2))
				.addGap(30).addGroup(p2ResultPanel.createParallelGroup(lOverall2, lblOverall2))
				.addGap(50).addGroup(p2ResultPanel.createParallelGroup(lNew2, lblNew2)));
		
		rootPane.add(lTitle);
		rootPane.add(p1ResultPanel);
		rootPane.add(p2ResultPanel);
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
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(new Image("/res/simple/questionbg.png"), 0, 0);
		g.drawImage(new Image("/res/simple/playerbg.png"), 124, 175);
		g.drawImage(new Image("/res/simple/playerbg.png"), 524, 175);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
	
	}

	@Override
	public int getID() {
		return 3;
	}

}