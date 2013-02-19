package conn;

import conn.Packet.*;
import main.HRRUClient;
import main.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryonet.Client;

import TWLSlick.BasicTWLGameState;
import TWLSlick.RootPane;
import de.matthiasmann.twl.*;
import de.matthiasmann.twl.EditField.Callback;

public class HostServer extends BasicTWLGameState {

	public static boolean p1ready = false;
	public static boolean p2ready = false;
	
	private final int cancelled = -2;
	private final int waiting = 0; 
	private final int established = 2;
	private final int ready = 3;
	private final int start = 4;
	
	int clock;
	
	private int state;
	private boolean back = false;
	public Client client;
	private String password;
	private String p1name;
	private Player player1;

	int gcw;
	int gch;
	
	DialogLayout hostPanel;
    EditField efName;
    EditField efPassword;
    Label lName, lPassword, lStatus, lPlayer1, lPlayer2;
    Button btnJoin, btnBack, btnCancel, btnReady, btnStart;
    
	private Packet0CreateRequest createRequest;
	private Packet7Ready readyRequest;
	
	public HostServer(int hostserver) {
		client = HRRUClient.conn.getClient();
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.enter(gc, sbg);
		clock = 300;
        rootPane.removeAllChildren();
        hostPanel.setTheme("login-panel");

        efName.setSize(100,20);
        lName.setLabelFor(efName);

        lPassword.setLabelFor(efPassword);
        btnStart.setVisible(false);
        btnReady.setVisible(false);
        btnCancel.setEnabled(false);

	    hostPanel.setIncludeInvisibleWidgets(false);
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
	
	void disableAllGUI() {
		lName.setVisible(false);
	    lPassword.setVisible(false);
	    efName.setVisible(false);
	    efPassword.setVisible(false);
	    btnJoin.setVisible(false);
	    btnBack.setVisible(false);
	    lPlayer1.setVisible(false);
	    lPlayer2.setVisible(false);
	    btnReady.setVisible(false);
	    btnStart.setVisible(false);
		btnCancel.setEnabled(false);
		
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
		Packet8Start startRequest = new Packet8Start();
		startRequest.sessionID = HRRUClient.cs.getSessionID();
		client.sendTCP(startRequest);
		HRRUClient.cs.setState(start);
	}
	
	void emulateReady() {
		btnReady.setVisible(false);
		HRRUClient.cs.setState(ready);
		readyRequest = new Packet7Ready();
		readyRequest.sessionID = HRRUClient.cs.getSessionID();
		readyRequest.player = 1;
		client.sendTCP(readyRequest);
		p1ready = true;
		lPlayer1.setText("Player 1: \t" + player1.getName() + " is ready!");
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
		
		lStatus = new Label("Enter your name and a password for your game.");
		lPlayer1 = new Label();
		lPlayer2 = new Label();
		
        hostPanel = new DialogLayout();
        efName = new EditField();
        efPassword = new EditField();
        lName = new Label("Your Name");
        lPassword = new Label("Password");
        btnStart = new Button("Start");
        btnReady = new Button("Ready");
        btnJoin = new Button("Host");
        btnBack = new Button("Back");
        btnCancel= new Button("Cancel");
        
        efName.addCallback(new Callback() {
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
        btnStart.addCallback(new Runnable() {
            public void run() {
                emulateStart();
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
			player1 = new Player(p1name);
			HRRUClient.cs.setP1(player1);
			disableGUI();
		}
		// Connection established with player 2.
		else if(state == established)
		{
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
		else if(state == ready)
		{
			if((p1ready == true) && (p2ready == true))
			{
				btnStart.setVisible(true);
				resetPosition();
				state = start;
			}
		}
		else if(state == start)
		{
			HRRUClient.cs.setP1(player1);
			clock--;
			disableAllGUI();
			lStatus.setText("Game Starting in " + (clock/100+1) + "...");
			if(clock<0)
			{
				sbg.enterState(4);
			}
		}
		if(p2ready == true)
		{
			lPlayer2.setText("Player 2: \t" + HRRUClient.cs.getP2().getName() + " is ready!");
			resetPosition();
		}
	}

	public int getID() {
		return 1;
	}

}