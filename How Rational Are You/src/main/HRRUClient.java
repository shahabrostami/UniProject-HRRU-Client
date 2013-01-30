package main;

import java.net.URL;
import java.net.URLClassLoader;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import main.Main;
import main.JoinServer;


import main.ConnectionState;

import TWLSlick.TWLStateBasedGame;

public class HRRUClient extends TWLStateBasedGame {

	public static ConnectionState cs;
	public static boolean ConnectionSuccessful = true;
	
	public static final int main = 0;
	public static final int host = 1;
	public static final int join = 2;
	
	protected HRRUClient() {
		super("How Rational Are You");
		cs = new ConnectionState();
		this.addState(new Main(main));
		this.addState(new HostServer(host));
		this.addState(new JoinServer(join));
		this.enterState(main);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new HRRUClient());
			app.setTargetFrameRate(60);
			app.setMaximumLogicUpdateInterval(16);
			app.setMinimumLogicUpdateInterval(16);
			app.setDisplayMode(800, 600, false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected URL getThemeURL() {
		String fileName = "../../resource/simple.xml";
		System.out.println("Loading file: " + fileName);

		System.out.println("Class path set to: ");
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader) cl).getURLs();
		for (URL url : urls) {
			System.out.println("\t" + url.getFile());
		}

		return HRRUClient.class.getResource(fileName);
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
	}
} 