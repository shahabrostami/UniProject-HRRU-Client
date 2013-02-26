package main;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import main.Main;
import conn.*;

import TWLSlick.TWLStateBasedGame;

public class HRRUClient extends TWLStateBasedGame {

	public static ConnectionState cs;
	public static Connection conn;
	public static boolean ConnectionSuccessful = true;
	
	public static final int main = 0;
	public static final int host = 1;
	public static final int join = 2;
	public static final int tutorial = 3;
	public static final int characterselect  = 4;
	public static final int play = 5;
	public static final int play_question = 6;
	
	public static final int resX = 800;
	public static final int resY = 600;
	
 public HRRUClient() {
		super("How Rational Are You");
		cs = new ConnectionState();
		System.out.println("client");
		conn = new Connection();
		this.addState(new Main(main));
		this.addState(new HostServer(host));
		this.addState(new JoinServer(join));
		this.addState(new Test(tutorial));
		this.addState(new CharacterSelect(characterselect));
		this.addState(new Play(play));
		this.enterState(main);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new HRRUClient());
			app.setTargetFrameRate(60);
			app.setMaximumLogicUpdateInterval(16);
			app.setMinimumLogicUpdateInterval(16);
			app.setDisplayMode(800, 600, false);
			app.setAlwaysRender(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public URL getThemeURL() {
		String fileName = "/simple/simple2.xml";
		System.out.println("Loading file: " + fileName);
		return HRRUClient.class.getResource(fileName);
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
	}
} 