package conn;

import conn.*;
import conn.Packet.*;
import main.HRRUClient;
import main.Player;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

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
	private final int initial = -1;
	private final int waiting = 0; 
	private final int joined = 1;
	private final int established = 2;
	private final int ready = 3;
	private final int start = 4;
	private final int p1_charselect = 5;
	
	private int attempts = 0;
	int clock;
	
	public String mouse = "No input yet!";
	private int state;
	private boolean back = false;
	public Client client;
	private int joinSessionID;
	private String joinPassword;
	private String p2name;
	private Player player1, player2;

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
		client = HRRUClient.conn.getClient();
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		
		rootPane.removeAllChildren();
		clock = 300;
		
        joinPanel.setTheme("login-panel");
        lName.setLabelFor(efName);
        lName.setLabelFor(efSessionID);
        lPassword.setLabelFor(efPassword);
        btnReady.setVisible(false);
        btnCancel.setEnabled(false);

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
	
	void disableAllGUI() {
		lName.setVisible(false);
	    lSessionID.setVisible(false);
	    lPassword.setVisible(false);
	    efName.setVisible(false);
	    efSessionID.setVisible(false);
	    efPassword.setVisible(false);
	    btnJoin.setVisible(false);
	    btnBack.setVisible(false);
	    lPlayer1.setVisible(false);
	    lPlayer2.setVisible(false);
	    btnReady.setVisible(false);
		btnCancel.setEnabled(false);
		
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
		try {
	        String test = String.valueOf(joinSessionID);
	        joinSessionID = Integer.parseInt(efSessionID.getText());
	        joinPassword = efPassword.getText();
	        p2name = efName.getText();
	        attempts++;
			joinRequest = new Packet2JoinRequest();
			joinRequest.sessionID = joinSessionID;
			joinRequest.password = joinPassword;
			joinRequest.player2Name = p2name;
			client.sendTCP(joinRequest);
		
	    	disableGUI();
		 } catch (NumberFormatException e) {
			 	HRRUClient.cs.setState(initial);
		        lStatus.setText("Please enter numbers only for the Session ID.");
		        resetPosition();
		 }
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
		
		lStatus = new Label("Enter your name and game details.");
		lPlayer1 = new Label();
		lPlayer2 = new Label();
        joinPanel = new DialogLayout();
        efName = new EditField();
        efSessionID = new EditField();
        efPassword = new EditField();
        lName = new Label("Your Name");
        lSessionID = new Label("Session ID");
        lPassword = new Label("Password");
        btnReady = new Button("Ready");
        btnJoin = new Button("Join");
        btnBack = new Button("Back");
        btnCancel= new Button("Cancel");
        
        efName.addCallback(new Callback() {
            public void callback(int key) {
                if(key == Event.KEY_RETURN) {
                    efSessionID.requestKeyboardFocus();
                }
            }
        });
        efSessionID.addCallback(new Callback() {
            public void callback(int key) {
                if(key == Event.KEY_RETURN) {
                    efPassword.requestKeyboardFocus();
                }
            }
        });
        efPassword.addCallback(new Callback() {
            public void callback(int key) {
                if(key == Event.KEY_RETURN) {
                    emulateLogin();
                }
            }
        });
        btnReady.addCallback(new Runnable() {
            public void run() {
                emulateReady();
            }
        });
        btnJoin.addCallback(new Runnable() {
            public void run() {
                emulateLogin();
            }
        });
        btnBack.addCallback(new Runnable() {
            public void run() {
                back = true;
            }
        });
        btnCancel.addCallback(new Runnable() {
            public void run() {
                emulateCancel();
            }
        });

        
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
			player2 = new Player(p2name);
			HRRUClient.cs.setP2(player2);
			lStatus.setText("Connection Established!\n" +
					"\nSession ID: \t" + HRRUClient.cs.getSessionID() +
					"\n\nReady?");
			lPlayer1.setText("Player 1: \t" + HRRUClient.cs.getP1().getName());
			lPlayer2.setText("Player 2 : \t" + HRRUClient.cs.getP2().getName());
		    lPlayer1.setVisible(true);
		    lPlayer2.setVisible(true);
			btnReady.setVisible(true);
			disableGUI();
		}
		// Player 2 is ready
		else if(state == ready)
		{
			lPlayer2.setText("Player 2: \t" + HRRUClient.cs.getP2().getName() + " is ready!");
			resetPosition();
		}
		else if(state == start)
		{
			HRRUClient.cs.setP2(player2);
			clock--;
			disableAllGUI();
			lStatus.setText("Game Starting in " + (clock/100+1) + "...");
			if(clock<0)
			{
				sbg.enterState(4);
			}
		}
		// Player 1 is ready
		if(p1ready == true)
		{
			lPlayer1.setText("Player 1: \t" + HRRUClient.cs.getP1().getName() + " is ready!");
			resetPosition();
		}
		
	}

	public int getID() {
		return 2;
	}

}