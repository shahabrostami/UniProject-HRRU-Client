package main;



import main.textpage.TextPage;
import main.textpage.TextPage.TextPageFrame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


import com.esotericsoftware.kryonet.Client;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.ResizableFrame.ResizableAxis;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.TextArea;

public class Test extends BasicTWLGameState {

	public Client client;
	DialogLayout p1ResultPanel, p2ResultPanel;
	
	int gcw;
	int gch;
	
	
	public Test(int main) {
		client = HRRUClient.conn.getClient();
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		TextPageFrame textpageframe = new TextPageFrame("demo.html");
		textpageframe.setSize(600, 200);
		textpageframe.setDraggable(false);
		textpageframe.setResizableAxis(ResizableAxis.NONE);
		textpageframe.setTheme("textpageframe");
        rootPane.add(textpageframe);
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

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
	
	}

	@Override
	public int getID() {
		return 3;
	}

}