package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryonet.Client;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.Button;


public class About extends BasicTWLGameState {

	public Client client;
	private int enterState;
	Button btnBack;
	Image aboutbg, questionbg;
	
	int gcw;
	int gch;
	
	public About(int main) {
		client = HRRUClient.conn.getClient();
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		enterState = 0;
		// SETUP GUI
		rootPane.removeAllChildren();
		rootPane.add(btnBack);
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
		aboutbg = new Image("simple/about.png");
		questionbg = new Image("simple/questionbg.png");
		
		
		//btn back
		btnBack = new Button("Back");
		btnBack.setSize(700, 30);
		btnBack.setPosition(50,550);
		btnBack.addCallback(new Runnable() {
			@Override
			public void run() {
				enterState = 1;
			}
		});
		btnBack.setTheme("menubutton");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(questionbg, 0, 0);
		g.drawImage(aboutbg, 0, 0);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
	
		if(enterState == 1)
			sbg.enterState(0);
	}

	@Override
	public int getID() {
		return 5;
	}

}