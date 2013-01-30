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

	private final int failed = -3;
	private final int cancelled = -2;
	private final int waiting = 0; 
	private final int joined = 1;
	private final int established = 2;
	
	public String mouse = "No input yet!";
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
    Label lStatus;
    Button btnJoin, btnBack, btnCancel;
    
	private Packet0CreateRequest createRequest;
	
	public HostServer(int joinserver) {
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
        
		lStatus = new Label("Enter your name and a password for your game.");

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
        
        Label lName = new Label("Your Name");
        lName.setLabelFor(efName);
        
        Label lPassword = new Label("Password");
        lPassword.setLabelFor(efPassword);
        
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

        DialogLayout.Group hLabels = hostPanel.createParallelGroup(lName, lPassword);
        DialogLayout.Group hFields = hostPanel.createParallelGroup(efName, efPassword);
        DialogLayout.Group hBtnJoin = hostPanel.createSequentialGroup().addWidget(btnJoin);
        DialogLayout.Group hBtnBack = hostPanel.createSequentialGroup().addWidget(btnBack);
        DialogLayout.Group hBtnCancel = hostPanel.createSequentialGroup().addWidget(btnCancel);
        DialogLayout.Group hStatus = hostPanel.createSequentialGroup().addWidget(lStatus);
        hostPanel.setHorizontalGroup(hostPanel.createParallelGroup()
        		.addGroup(hStatus)
                .addGroup(hostPanel.createSequentialGroup(hLabels, hFields))
                .addGroup(hBtnJoin)
                .addGroup(hBtnBack)
                .addGroup(hBtnCancel));
        
        hostPanel.setVerticalGroup(hostPanel.createSequentialGroup()
        		.addWidget(lStatus).addGap(20)
        		.addGroup(hostPanel.createParallelGroup(lName, efName))
                .addGroup(hostPanel.createParallelGroup(lPassword, efPassword))
                .addWidget(btnJoin)
        		.addWidget(btnBack)
        		.addWidget(btnCancel));

		rootPane.add(hostPanel);
		rootPane.setTheme("");		
		resetPosition();
	}
	
	void enableGUI() {
		efName.setEnabled(true);
	    efPassword.setEnabled(true);
	    btnJoin.setEnabled(true);
	    btnBack.setEnabled(true);
	    btnCancel.setEnabled(false);
	}
	
	void disableGUI() {
		efName.setEnabled(false);
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
	    
	    lStatus.setText("Enter your name and a password for your game.");
	    resetPosition();
		
	}
	
	void emulateLogin() {
	    disableGUI();
	    
		p1name = efName.getText();
		password = efPassword.getText();
		
		createRequest = new Packet0CreateRequest();
		createRequest.password = password;
		createRequest.player1Name = p1name;
		client.sendTCP(createRequest);
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
		mouse = "xpos: " + xpos + "\nypos: " + ypos;
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// Back to main menu
		if(back) {
			sbg.enterState(0);
			back = false;
		}
		
		state = HRRUClient.cs.getState();
		
		// Connection cancelled.
		if(HRRUClient.cs.getState() == cancelled) {
			enableGUI();
			lStatus.setText("Session cancelled.\nEnter your name and a password for your game.");
			resetPosition();
		}
		// Waiting for player 2.
		else if(state == waiting) {
			disableGUI();
			lStatus.setText("Hosting Game..." +
					"\nSession ID: \t" + HRRUClient.cs.getSessionID() +
					"\nPassword: \t" + HRRUClient.cs.getPassword() + 
					"\n\nWaiting for Player 2.");
			HRRUClient.cs.setP1Name(p1name);
			resetPosition();
		}
		// Connection established with player 2.
		else if(state == established)
		{
			
			HRRUClient.cs.setP1Name(p1name);
			p2name = HRRUClient.cs.getP2Name();
			lStatus.setText("Connection Established!" +
					"\nSession ID: \t" + HRRUClient.cs.getSessionID() +
					"\nPlayer 1: \t" + HRRUClient.cs.getP1Name() + 
					"\nPlayer 2: \t" + HRRUClient.cs.getP2Name() + 
					"\n\nReady?");
			resetPosition();
		}
	}

	public int getID() {
		return 1;
	}

}