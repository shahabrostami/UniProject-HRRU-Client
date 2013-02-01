package main;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import main.NetworkListener;
import main.Packet.*;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.*;
import de.matthiasmann.twl.EditField.Callback;

public class JoinServer extends BasicTWLGameState {

	public static boolean p1ready = false;
	public static boolean p2ready = false;
	
	private final int failed = -3;
	private final int cancelled = -2;
	private final int waiting = 0; 
	private final int joined = 1;
	private final int established = 2;
	private final int ready = 3;
	
	private int attempts = 0;
	
	public String mouse = "No input yet!";
	private int state;
	private boolean back = false;
	public Client client;
	private int joinSessionID;
	private String joinPassword;
	private String p1name;
	private String p2name;

	int gcw;
	int gch;
	
	DialogLayout joinPanel;
    EditField efName;
    EditField efSessionID;
    EditField efPassword;
    Button btnJoin, btnBack, btnCancel, btnReady;
    Label lName, lSessionID, lPassword, lStatus, lPlayer1, lPlayer2;
    
	private Packet2JoinRequest joinRequest;
	private Packet7Ready readyRequest;
	
	public JoinServer(int joinserver) {
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
        
		lStatus = new Label("Enter your name and game details.");
		lPlayer1 = new Label();
		lPlayer2 = new Label();
		
        joinPanel = new DialogLayout();
        joinPanel.setTheme("login-panel");
        
        efName = new EditField();
        efName.addCallback(new Callback() {
            public void callback(int key) {
                if(key == Event.KEY_RETURN) {
                    efSessionID.requestKeyboardFocus();
                }
            }
        });
        
        efSessionID = new EditField();
        efSessionID.addCallback(new Callback() {
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
        
        lSessionID = new Label("Session ID");
        lName.setLabelFor(efSessionID);
        
        lPassword = new Label("Password");
        lPassword.setLabelFor(efPassword);
        
        btnReady = new Button("Ready");
        btnReady.addCallback(new Runnable() {
            public void run() {
                emulateReady();
            }
        });
        btnReady.setVisible(false);
        
        
        btnJoin = new Button("Join");
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
        
        
        DialogLayout.Group hLabels = joinPanel.createParallelGroup(lName, lSessionID, lPassword);
        DialogLayout.Group hFields = joinPanel.createParallelGroup(efName, efSessionID, efPassword);
        DialogLayout.Group hBtn = joinPanel.createSequentialGroup().addWidget(btnJoin);
        DialogLayout.Group hBtn2 = joinPanel.createSequentialGroup().addWidget(btnBack);
        DialogLayout.Group hBtnCancel = joinPanel.createSequentialGroup().addWidget(btnCancel);
        DialogLayout.Group hBtnReady = joinPanel.createSequentialGroup().addWidget(btnReady);
        DialogLayout.Group hStatus = joinPanel.createSequentialGroup().addWidget(lStatus);
        DialogLayout.Group hPlayer1Ready = joinPanel.createSequentialGroup().addWidget(lPlayer1);
        DialogLayout.Group hPlayer2Ready = joinPanel.createSequentialGroup().addWidget(lPlayer2);
        joinPanel.setIncludeInvisibleWidgets(false);
        
        joinPanel.setHorizontalGroup(joinPanel.createParallelGroup()
        		.addGroup(hStatus)
        		.addGroup(hPlayer1Ready)
        		.addGroup(hPlayer2Ready)
        		.addGroup(hBtnReady)
                .addGroup(joinPanel.createSequentialGroup(hLabels, hFields))
                .addGroup(hBtn)
                .addGroup(hBtn2)
                .addGroup(hBtnCancel));
        
        joinPanel.setVerticalGroup(joinPanel.createSequentialGroup()
        		.addWidget(lStatus)
        		.addWidget(lPlayer1)
        		.addWidget(lPlayer2).addGap(10)
                .addGroup(joinPanel.createParallelGroup(lName, efName))
                .addGroup(joinPanel.createParallelGroup(lSessionID, efSessionID))
                .addGroup(joinPanel.createParallelGroup(lPassword, efPassword))
                .addWidget(btnJoin)
        		.addWidget(btnBack)
        		.addWidget(btnReady)
        		.addWidget(btnCancel));


		rootPane.add(joinPanel);
		rootPane.setTheme("");
		
		joinPanel.adjustSize();
		joinPanel.setPosition(
               (gcw/2 - joinPanel.getWidth()/2),
                (gch/2 - joinPanel.getHeight()/2));
	}
	
	void enableGUI() {
		lName.setVisible(true);
	    lSessionID.setVisible(true);
	    lPassword.setVisible(true);
	    efName.setVisible(true);
	    efSessionID.setVisible(true);
	    efPassword.setVisible(true);
	    btnJoin.setVisible(true);
	    btnBack.setVisible(true);
	    
	    lPlayer1.setVisible(false);
	    lPlayer2.setVisible(false);
	    btnReady.setVisible(false);
	    p1ready = false;
	    p2ready = false;
	    lPlayer1.setText(null);
	    lPlayer2.setText(null);
	    
	    resetPosition();
	}
	
	void disableGUI() {
	    lName.setVisible(false);
	    lSessionID.setVisible(false);
	    lPassword.setVisible(false);
	    efName.setVisible(false);
	    efSessionID.setVisible(false);
	    efPassword.setVisible(false);
	    btnJoin.setVisible(false);
	    btnBack.setVisible(false);

	    btnCancel.setEnabled(true);
	    
	    resetPosition();
	}
	
	void emulateReady() {
		btnReady.setVisible(false);
		HRRUClient.cs.setState(ready);
		readyRequest = new Packet7Ready();
		readyRequest.sessionID = HRRUClient.cs.getSessionID();
		readyRequest.player = 2;
		client.sendTCP(readyRequest);
		resetPosition();
	}
	
	void emulateCancel() {
		Packet5CancelRequest cancelRequest = new Packet5CancelRequest();
	    cancelRequest.sessionID = HRRUClient.cs.getSessionID();
	    HRRUClient.cs.setState(cancelled);
	    client.sendTCP(cancelRequest);
	    
	    lStatus.setText("Enter your name and game details.");
	    enableGUI();
	}
	
	void emulateLogin() {
	    attempts++;
		joinSessionID = Integer.parseInt(efSessionID.getText());
		joinPassword = efPassword.getText();
		p2name = efName.getText();
		
		joinRequest = new Packet2JoinRequest();
		joinRequest.sessionID = joinSessionID;
		joinRequest.password = joinPassword;
		joinRequest.player2Name = p2name;
		client.sendTCP(joinRequest);
		
	    disableGUI();
	}
	
	void resetPosition() {
		joinPanel.adjustSize();
		joinPanel.setPosition(
	               (gcw/2 - joinPanel.getWidth()/2),
	                (gch/2 - joinPanel.getHeight()/2));
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
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {


	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if(back)
		{
			lStatus.setText("Enter your name and game details.");
			sbg.enterState(0);
			back = false;
		}
		
		state = HRRUClient.cs.getState();
		// Connection cancelled.
		if(state == cancelled) {
			lStatus.setText("Session cancelled.\nEnter your name and game details.");
			enableGUI();
		}
		// Connection failed.
		else if(state == failed)
		{
			lStatus.setText("Connection failed.\nSession ID or Password incorrect.\nAttempts: " + attempts + "\n\nEnter your name and game details.");
			enableGUI();
		}
		// Connected to player1.
		else if(state == joined)
		{
			HRRUClient.cs.setP2Name(p2name);
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
			lPlayer2.setText("Player 2: \t" + HRRUClient.cs.getP2Name() + " is ready!");
		}
		
		if(p1ready == true)
		{
			lPlayer1.setText("Player 1: \t" + HRRUClient.cs.getP1Name() + " is ready!");
		}
	}

	public int getID() {
		return 2;
	}

}