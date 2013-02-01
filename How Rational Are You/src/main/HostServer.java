package main;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;

import main.NetworkListener;
import main.Packet.*;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.*;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.theme.ThemeManager;

public class HostServer extends BasicTWLGameState {

	public static boolean p1ready = false;
	public static boolean p2ready = false;
	
	private final int failed = -3;
	private final int cancelled = -2;
	private final int waiting = 0; 
	private final int joined = 1;
	private final int established = 2;
	private final int ready = 3;
	private final int start = 4;
	
	private int state;
	private boolean back = false;
	public Client client;
	private String password;
	private TextField p1nameTF;
	private String p1name;
	private String p2name;

	int gcw;
	int gch;
	
	DialogLayout hostPanel;
    EditField efName;
    EditField efPassword;
    Label lName, lPassword, lStatus, lPlayer1, lPlayer2;
    Button btnJoin, btnBack, btnCancel, btnReady, btnStart;
    
	private Packet0CreateRequest createRequest;
	private Packet7Ready readyRequest;
	
	public HostServer(int joinserver) {
		client = new Client();
		
		NetworkListener n1 = new NetworkListener();
		n1.init(client);
		client.addListener(n1);
		
		client.start();
		register();
		
		try{
			client.connect(5000, "localhost", 9991, 9992);
			HRRUClient.ConnectionSuccessful = true;
		} catch (IOException e) {
			e.printStackTrace();
			client.stop();
			HRRUClient.ConnectionSuccessful = false;
		}
	}
	
	private void register(){
		Kryo kryo = client.getKryo();
		kryo.register(Packet0CreateRequest.class);
		kryo.register(Packet1CreateAnswer.class);
		kryo.register(Packet2JoinRequest.class);
		kryo.register(Packet3JoinAnswer.class);
		kryo.register(Packet4ConnectionEstablished.class);
		kryo.register(Packet5CancelRequest.class);
		kryo.register(Packet6CancelRequestResponse.class);
		kryo.register(Packet7Ready.class);
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
        
		lStatus = new Label("Enter your name and a password for your game.");
		lPlayer1 = new Label();
		lPlayer2 = new Label();
		
        hostPanel = new DialogLayout();
        hostPanel.setTheme("login-panel");
        
        efName = new EditField();
        efName.setSize(100,20);
        efName.addCallback(new Callback() {
            public void callback(int key) {
                if(key == Event.KEY_RETURN) {
                    efPassword.requestKeyboardFocus();
                }
            }
        });

        efPassword = new EditField();
        efPassword.addCallback(new Callback() {
            public void callback(int key) {
                if(key == Event.KEY_RETURN) {
                    emulateLogin();
                }
            }
        });
        
        lName = new Label("Your Name");
        lName.setLabelFor(efName);
        
        lPassword = new Label("Password");
        lPassword.setLabelFor(efPassword);
        
        btnStart = new Button("Start");
        btnStart.addCallback(new Runnable() {
            public void run() {
                emulateStart();
            }
        });
        btnStart.setVisible(false);
        
        btnReady = new Button("Ready");
        btnReady.addCallback(new Runnable() {
            public void run() {
                emulateReady();
            }
        });
        btnReady.setVisible(false);
        
        btnJoin = new Button("Host");
        btnJoin.addCallback(new Runnable() {
            public void run() {
                emulateLogin();
            }
        });
        
        btnBack = new Button("Back");
        btnBack.addCallback(new Runnable() {
            public void run() {
                back = true;
            }
        });
        
        btnCancel= new Button("Cancel");
        btnCancel.addCallback(new Runnable() {
            public void run() {
                emulateCancel();
            }
        });
        btnCancel.setEnabled(false);

	    hostPanel.setIncludeInvisibleWidgets(false);
        DialogLayout.Group hLabels = hostPanel.createParallelGroup(lName, lPassword);
        DialogLayout.Group hFields = hostPanel.createParallelGroup(efName, efPassword);
        DialogLayout.Group hBtnJoin = hostPanel.createSequentialGroup().addWidget(btnJoin);
        DialogLayout.Group hBtnBack = hostPanel.createSequentialGroup().addWidget(btnBack);
        DialogLayout.Group hBtnCancel = hostPanel.createSequentialGroup().addWidget(btnCancel);
        DialogLayout.Group hBtnReady = hostPanel.createSequentialGroup().addWidget(btnReady);
        DialogLayout.Group hBtnStart = hostPanel.createSequentialGroup().addWidget(btnStart);
        
        DialogLayout.Group hStatus = hostPanel.createSequentialGroup().addWidget(lStatus);
        DialogLayout.Group hPlayer1Ready = hostPanel.createSequentialGroup().addWidget(lPlayer1);
        DialogLayout.Group hPlayer2Ready = hostPanel.createSequentialGroup().addWidget(lPlayer2);
        
        hostPanel.setHorizontalGroup(hostPanel.createParallelGroup()
        		.addGroup(hStatus)
        		.addGroup(hPlayer1Ready)
        		.addGroup(hPlayer2Ready)
                .addGroup(hostPanel.createSequentialGroup(hLabels, hFields))
                .addGroup(hBtnJoin)
                .addGroup(hBtnBack)
                .addGroup(hBtnReady)
                .addGroup(hBtnStart)
                .addGroup(hBtnCancel));
        
        hostPanel.setVerticalGroup(hostPanel.createSequentialGroup()
        		.addWidget(lStatus)
        		.addWidget(lPlayer1)
        		.addWidget(lPlayer2).addGap(10)
        		.addGroup(hostPanel.createParallelGroup(lName, efName))
                .addGroup(hostPanel.createParallelGroup(lPassword, efPassword))
                .addWidget(btnJoin)
        		.addWidget(btnBack)
        		.addWidget(btnReady)
        		.addWidget(btnStart)
        		.addWidget(btnCancel));

		rootPane.add(hostPanel);
		rootPane.setTheme("");		
		resetPosition();
	}
	
	void enableGUI() {
		lName.setVisible(true);
	    lPassword.setVisible(true);
	    efName.setVisible(true);
	    efPassword.setVisible(true);
	    btnJoin.setVisible(true);
	    btnBack.setVisible(true);
	    
	    lPlayer1.setVisible(false);
	    lPlayer2.setVisible(false);
	    btnReady.setVisible(false);
	    btnStart.setVisible(false);
	    p1ready = false;
	    p2ready = false;
	    lPlayer1.setText(null);
	    lPlayer2.setText(null);
	    
	    resetPosition();
	}
	
	void disableGUI() {
	    lName.setVisible(false);
	    lPassword.setVisible(false);
	    efName.setVisible(false);
	    efPassword.setVisible(false);
	    btnJoin.setVisible(false);
	    btnBack.setVisible(false);

	    btnCancel.setEnabled(true);
	    
	    resetPosition();
	}
	
	void emulateStart() {
		
	}
	
	void emulateReady() {
		btnReady.setVisible(false);
		HRRUClient.cs.setState(ready);
		readyRequest = new Packet7Ready();
		readyRequest.sessionID = HRRUClient.cs.getSessionID();
		readyRequest.player = 1;
		client.sendTCP(readyRequest);
		p1ready = true;
		resetPosition();
	}
	
	void emulateCancel() {
		Packet5CancelRequest cancelRequest = new Packet5CancelRequest();
	    cancelRequest.sessionID = HRRUClient.cs.getSessionID();
	    HRRUClient.cs.setState(cancelled);
	    client.sendTCP(cancelRequest);
	    
	    lStatus.setText("Enter your name and a password for your game.");
	    enableGUI();
	}
	
	void emulateLogin() {
	    disableGUI();
	    
		p1name = efName.getText();
		password = efPassword.getText();
		
		createRequest = new Packet0CreateRequest();
		createRequest.password = password;
		createRequest.player1Name = p1name;
		client.sendTCP(createRequest);
		
	    disableGUI();
	}
	
	void resetPosition() {
		hostPanel.adjustSize();
		hostPanel.setPosition(
	               (gcw/2 - hostPanel.getWidth()/2),
	                (gch/2 - hostPanel.getHeight()/2));
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
		gc.setShowFPS(false);
		
		Input input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos= Mouse.getY();	
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// Back to main menu
		if(back) {
			lStatus.setText("Enter your name and a password for your game.");
			sbg.enterState(0);
			back = false;
		}
		
		state = HRRUClient.cs.getState();
		
		// Connection cancelled.
		if(HRRUClient.cs.getState() == cancelled) {
			lStatus.setText("Session cancelled.\nEnter your name and a password for your game.");
			enableGUI();
		}
		// Waiting for player 2.
		else if(state == waiting) {
			lStatus.setText("Hosting Game...\n" +
					"\nSession ID: \t" + HRRUClient.cs.getSessionID() +
					"\nPassword: \t" + HRRUClient.cs.getPassword() + 
					"\n\nWaiting for Player 2.");
			HRRUClient.cs.setP1Name(p1name);
			disableGUI();
		}
		// Connection established with player 2.
		else if(state == established)
		{
			
			HRRUClient.cs.setP1Name(p1name);
			p2name = HRRUClient.cs.getP2Name();
			lStatus.setText("Connection Established!\n" +
					"\nSession ID: \t" + HRRUClient.cs.getSessionID() +
					"\n\nReady?");
			lPlayer1.setText("Player 1: \t" + HRRUClient.cs.getP1Name());
			lPlayer2.setText("Player 2 : \t" + HRRUClient.cs.getP2Name());
		    lPlayer1.setVisible(true);
		    lPlayer2.setVisible(true);
			btnReady.setVisible(true);
			disableGUI();
		}
		else if(state == ready)
		{
			lPlayer1.setText("Player 1: \t" + HRRUClient.cs.getP1Name() + " is ready!");
			if((p1ready == true) && (p2ready == true))
			{
				btnStart.setVisible(true);
				resetPosition();
				state = start;
			}
		}
		if(p2ready == true)
		{
			lPlayer2.setText("Player 2: \t" + HRRUClient.cs.getP2Name() + " is ready!");
		}
	}

	public int getID() {
		return 1;
	}

}