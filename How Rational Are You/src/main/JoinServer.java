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

	private final int failed = -3;
	private final int cancelled = -2;
	private final int waiting = 0; 
	private final int joined = 1;
	private final int established = 2;
	
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
    Button btnJoin, btnBack, btnCancel;
    Label lStatus;
    
	private Packet2JoinRequest joinRequest;
	
	public JoinServer(int joinserver) {
		client = new Client();
		NetworkListener n1 = new NetworkListener();
		n1.init(client);
		client.addListener(n1);
		client.start();
		register();
		
		try{
			client.connect(5000, "127.0.0.1", 54555, 54777);
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
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
        
		lStatus = new Label("Enter your name and game details.");
		
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
        
        Label lName = new Label("Your Name");
        lName.setLabelFor(efName);
        
        Label lSessionID = new Label("Session ID");
        lName.setLabelFor(efSessionID);
        
        Label lPassword = new Label("Password");
        lPassword.setLabelFor(efPassword);
        
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
        DialogLayout.Group hStatus = joinPanel.createSequentialGroup().addWidget(lStatus);
        joinPanel.setHorizontalGroup(joinPanel.createParallelGroup()
        		.addGroup(hStatus)
                .addGroup(joinPanel.createSequentialGroup(hLabels, hFields))
                .addGroup(hBtn)
                .addGroup(hBtn2)
                .addGroup(hBtnCancel));
        joinPanel.setVerticalGroup(joinPanel.createSequentialGroup()
        		.addWidget(lStatus).addGap(20)
                .addGroup(joinPanel.createParallelGroup(lName, efName))
                .addGroup(joinPanel.createParallelGroup(lSessionID, efSessionID))
                .addGroup(joinPanel.createParallelGroup(lPassword, efPassword))
                .addWidget(btnJoin)
        		.addWidget(btnBack)
        		.addWidget(btnCancel));


		rootPane.add(joinPanel);
		rootPane.setTheme("");
		
		joinPanel.adjustSize();
		joinPanel.setPosition(
               (gcw/2 - joinPanel.getWidth()/2),
                (gch/2 - joinPanel.getHeight()/2));
	}
	
	void enableGUI() {
	    efName.setEnabled(true);
	    efSessionID.setEnabled(true);
	    efPassword.setEnabled(true);
	    btnJoin.setEnabled(true);
	    btnBack.setEnabled(true);
	    btnCancel.setEnabled(false);
	}
	
	void disableGUI() {
	    efName.setEnabled(false);
	    efSessionID.setEnabled(false);
	    efPassword.setEnabled(false);
	    btnJoin.setEnabled(false);
	    btnBack.setEnabled(false);
	    btnCancel.setEnabled(true);
	}
	
	void emulateCancel() {
	    enableGUI();
	    
	    Packet5CancelRequest cancelRequest = new Packet5CancelRequest();
	    cancelRequest.sessionID = HRRUClient.cs.getSessionID();
	    HRRUClient.cs.setState(cancelled);
	    client.sendTCP(cancelRequest);
	    
	    lStatus.setText("Enter your name and game details.");
	    resetPosition();
		
	}
	
	void emulateLogin() {
	    disableGUI();
	    attempts++;
		joinSessionID = Integer.parseInt(efSessionID.getText());
		joinPassword = efPassword.getText();
		p2name = efName.getText();
		joinRequest = new Packet2JoinRequest();
		joinRequest.sessionID = joinSessionID;
		joinRequest.password = joinPassword;
		joinRequest.player2Name = p2name;
		client.sendTCP(joinRequest);
		
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
			sbg.enterState(0);
			back = false;
		}
		
		state = HRRUClient.cs.getState();
		// Connection cancelled.
		if(state == cancelled) {
			lStatus.setText("Session cancelled.\nEnter your name and game details.");
			enableGUI();
			resetPosition();
		}
		// Connection failed.
		else if(state == failed)
		{
			lStatus.setText("Connection failed.\nSession ID or Password incorrect.\nAttempts: " + attempts + "\n\nEnter your name and game details.");
			enableGUI();
			resetPosition();
		}
		// Connected to player1.
		else if(state == joined)
		{
			HRRUClient.cs.setP2Name(p2name);
			disableGUI();
			lStatus.setText("Connection Established!" +
					"\nSession ID: \t" + HRRUClient.cs.getSessionID() +
					"\nPlayer 1: \t" + HRRUClient.cs.getP1Name() + 
					"\nPlayer 2: \t" + HRRUClient.cs.getP2Name() + 
					"\n\nReady?");
			resetPosition();
		}
	}

	public int getID() {
		return 2;
	}

}