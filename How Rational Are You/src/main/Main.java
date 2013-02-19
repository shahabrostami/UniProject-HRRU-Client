package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;

public class Main extends BasicTWLGameState {

	public Main(int main) {
		// TODO Auto-generated constructor stub
	}

	int enterState = 0;
	Label lConnection;
	Button btnHost, btnJoin;
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		
		lConnection = new Label("Connection Successful");
		
		lConnection.setPosition(gc.getWidth()/2-100, gc.getHeight()/2-85);
		
		btnHost = new Button("Host Server");
		btnHost.setSize(100, 20);
		btnHost.setPosition(gc.getWidth()/2-50, gc.getHeight()/2-55);
		btnHost.addCallback(new Runnable() {
			@Override
			public void run() {
				enterState = 1;
			}
		});
		
		btnJoin = new Button("Join Server");
		btnJoin.setSize(100, 20);
		btnJoin.setPosition(gc.getWidth()/2-50, gc.getHeight()/2-25);
		btnJoin.addCallback(new Runnable() {
			@Override
			public void run() {
				enterState = 2;
			}
		});
		
		rootPane.add(lConnection);
		rootPane.add(btnJoin);
		rootPane.add(btnHost);
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
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if(!HRRUClient.ConnectionSuccessful)
		{
			btnJoin.setEnabled(false);
			btnHost.setEnabled(false);
			lConnection.setText("Connection Failed...\nPlease restart the application.");
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if(enterState == 1)
		{
			sbg.enterState(1);
			enterState = 0;
		}
		else if(enterState == 2)
		{
			sbg.enterState(2);
			enterState = 0;
		}
	}

	@Override
	public int getID() {
		return 0;
	}

}