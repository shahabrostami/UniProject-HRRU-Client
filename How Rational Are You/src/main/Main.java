package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryonet.Client;

import conn.Connection;
import conn.Packet.Packet0CreateRequest;
import conn.Packet.Packet16SendBid;
import conn.Packet.Packet2JoinRequest;
import conn.Packet.Packet8Start;
import conn.Packet.Packet9CharacterSelect;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;

public class Main extends BasicTWLGameState {

	public Main(int main) {
		// TODO Auto-generated constructor stub
	}

	Client client;
	
	// GUI variables
	boolean connectionSuccessful;
	int enterState = 0;
	int attempts = 0;
	Label lConnection;
	Button btnHost, btnJoin;
	Button btnHT1, btnHT2, btnHT3;
	Button btnTestStart;
	EditField efSessionID;
	Button btnJT1, btnJT2;
	Button btnRetry;
	
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
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		client = HRRUClient.conn.getClient();
		attempts = 0;
		enterState = 0;
		rootPane.removeAllChildren();
		
		// btnRetry.setEnabled(false);
		btnHT2.setEnabled(false);
		btnHT3.setEnabled(false);
		btnJT2.setEnabled(false);
		btnTestStart.setEnabled(false);
		
		rootPane.add(lConnection);
		//rootPane.add(btnRetry);
		rootPane.add(btnHT1);
		rootPane.add(efSessionID);
		rootPane.add(btnJT1);
		rootPane.add(btnHT2);
		rootPane.add(btnJT2);
		rootPane.add(btnHT3);
		rootPane.add(btnTestStart);
		rootPane.add(btnJoin);
		rootPane.add(btnHost);
		rootPane.setTheme("");
		
		// RESET VARIABLES
		start_message = "";
		full_start_message = "HOW RATIONAL ARE YOU?";
		full_start_counter = 0;
		ticker = "";
		tickerBoolean = true;
		clock2 = 0;
		clock3 = 0;
	}

	@Override
	protected RootPane createRootPane() {
		assert rootPane == null : "RootPane already created";

		RootPane rp = new RootPane(this);
		rp.setTheme("");
		rp.getOrCreateActionMap().addMapping(this);
		return rp;
	}

	void emulateRetry() {
		Connection conn = new Connection();
		HRRUClient.conn = conn;
		attempts++;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
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
		
		lConnection = new Label("");
		lConnection.setPosition(gc.getWidth()/2-100, gc.getHeight()/2-125);
		
		/*
		btnRetry = new Button("Retry Server");
		
		btnRetry.setSize(100, 20);
		btnRetry.setPosition(gc.getWidth()/2-50, gc.getHeight()/2-85);
		btnRetry.addCallback(new Runnable() {
			@Override
			public void run() {
				emulateRetry();
			}
		});
		*/
		// setup gui variables
		btnHost = new Button("Host Server");
		btnHost.setSize(400, 30);
		btnHost.setPosition(gc.getWidth()/2-200, gc.getHeight()/2-95);
		btnHost.addCallback(new Runnable() {
			@Override
			public void run() {
				enterState = 1;
			}
		});
		btnHost.setTheme("menubutton");
		btnJoin = new Button("Join Server");
		btnJoin.setSize(400, 30);
		btnJoin.setPosition(gc.getWidth()/2-200, gc.getHeight()/2-60);
		btnJoin.addCallback(new Runnable() {
			@Override
			public void run() {
				enterState = 2;
			}
		});
		btnJoin.setTheme("menubutton");
		
		
		btnHT1 = new Button("1) Host Server Create");
		btnHT1.setSize(200, 20);
		btnHT1.setPosition(gc.getWidth()/2-100, gc.getHeight()/2+15);
		btnHT1.addCallback(new Runnable() {
			@Override
			public void run() {
				Packet0CreateRequest createRequest = new Packet0CreateRequest();
				createRequest.password = "";
				createRequest.player1Name = "p1name";
				Player player1;
				try {
					player1 = new Player("p1name");
					HRRUClient.cs.setP1(player1);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				client.sendTCP(createRequest);
				btnHT2.setEnabled(true);
				btnHT1.setEnabled(false);
				btnJT1.setEnabled(false);
				btnJT2.setEnabled(false);
				enterState = 3;
			}
		});
		
		efSessionID = new EditField();
		efSessionID.setPosition(gc.getWidth()/2-50, gc.getHeight()/2+45);
		efSessionID.setSize(100,20);
		btnJT1 = new Button("2) Join Server Request");
		btnJT1.setSize(200, 20);
		btnJT1.setPosition(gc.getWidth()/2-100, gc.getHeight()/2+75);
		btnJT1.addCallback(new Runnable() {
			@Override
			public void run() {
				Packet2JoinRequest joinRequest = new Packet2JoinRequest();
				joinRequest.sessionID = Integer.parseInt(efSessionID.getText());
				joinRequest.password = "";
				joinRequest.player2Name = "p2name";
				Player player2;
				try {
					player2 = new Player("p2name");
					HRRUClient.cs.setP2(player2);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				btnJT2.setEnabled(true);
				btnJT1.setEnabled(false);
				btnHT1.setEnabled(false);
				btnHT2.setEnabled(false);
				btnHT3.setEnabled(false);
				client.sendTCP(joinRequest);
			}
		});
		
		btnHT2 = new Button("3) Host Server Start");
		btnHT2.setSize(200, 20);
		btnHT2.setPosition(gc.getWidth()/2-100, gc.getHeight()/2+105);
		btnHT2.addCallback(new Runnable() {
			@Override
			public void run() {
				Packet8Start startRequest = new Packet8Start();
				startRequest.sessionID = HRRUClient.cs.getSessionID();
				client.sendTCP(startRequest);
				btnHT2.setEnabled(false);
				btnHT3.setEnabled(true);
			}
		});
		
		btnHT3 = new Button("4) Host Server Character");
		btnHT3.setSize(200, 20);
		btnHT3.setPosition(gc.getWidth()/2-100, gc.getHeight()/2+135);
		btnHT3.addCallback(new Runnable() {
			@Override
			public void run() {
				Packet9CharacterSelect characterRequest = new Packet9CharacterSelect();
				characterRequest.sessionID = HRRUClient.cs.getSessionID();
				characterRequest.player = 1;
				characterRequest.characterID = 3;
				HRRUClient.cs.getP1().setPlayerCharacterID(3);
				client.sendTCP(characterRequest);
				btnHT3.setEnabled(false);
				btnTestStart.setEnabled(true);
			}
		});
		
		btnJT2 = new Button("5) Join Server Character");
		btnJT2.setSize(200, 20);
		btnJT2.setPosition(gc.getWidth()/2-100, gc.getHeight()/2+165);
		btnJT2.addCallback(new Runnable() {
			@Override
			public void run() {
				Packet9CharacterSelect characterRequest = new Packet9CharacterSelect();
				characterRequest.sessionID = HRRUClient.cs.getSessionID();
				characterRequest.player = 2;
				characterRequest.characterID = 4;
				HRRUClient.cs.getP2().setPlayerCharacterID(4);
				client.sendTCP(characterRequest);
				btnJT2.setEnabled(false);
				btnTestStart.setEnabled(true);
			}
		});
		
		btnTestStart = new Button("6) Start Test Game");
		btnTestStart.setSize(200, 20);
		btnTestStart.setPosition(gc.getWidth()/2-100, gc.getHeight()/2+195);
		btnTestStart.addCallback(new Runnable() {
			@Override
			public void run() {
				enterState = 4;
			}
		});
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setFont(titleFont.get());
		g.drawString("> " + start_message + "" + ticker, 50, 50);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		clock3 += delta;
		clock2 += delta;
		// full message ticker
		if(clock3 > 100){
			if(full_start_counter < full_start_message.length())
			{
				start_message += full_start_message.substring(full_start_counter, full_start_counter+1);
				full_start_counter++;
				clock3-=100;
			}
		}
		// ticker symbol
		if(clock2>999)
		{
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
		// Is connection successful?
		connectionSuccessful = HRRUClient.ConnectionSuccessful;
		if(connectionSuccessful)
		{
			btnJoin.setEnabled(true);
			btnHost.setEnabled(true);
			// btnRetry.setVisible(false);
			lConnection.setText("Connection Successful");
		}
		else
		{
			btnJoin.setEnabled(false);
			btnHost.setEnabled(false);
			// btnRetry.setVisible(true);
			// btnRetry.setEnabled(true);
			lConnection.setText("Connection Failed...\nPlease restart application.\nAttemtps: " + attempts);
		}
		if (enterState == 1)
		{
			sbg.enterState(1);
			enterState = 0;
		}
		else if (enterState == 2)
		{
			sbg.enterState(2);
			enterState = 0;
		}
		else if (enterState == 3)
		{
			efSessionID.setText("" + HRRUClient.cs.getSessionID());
		}
		else if(enterState == 4)
		{
			HRRUClient.cs.getP1().setScore(1000);
			HRRUClient.cs.getP2().setScore(1000);
			HRRUClient.cs.setState(7);
			sbg.enterState(5);
			enterState = 0;
		}
	}

	@Override
	public int getID() {
		return 0;
	}

}