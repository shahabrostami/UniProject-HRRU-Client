package conn;

import main.Character;

public class ConnectionState {
	
	private final int failed = -3;
	private final int cancelled = -2;
	private final int initial = -1;
	private final int waiting = 0; 
	private final int joined = 1;
	private final int established = 2;
	private final int ready = 3;
	private final int start = 4;
	private final int p1_charselect = 5;
	
	private int player;
	private String p1name;
	private String p2name;
	private int p1Character;
	private int p2Character;
	private int state = initial;
	private int sessionID;
	private String password;
	
	
	public ConnectionState(){
	}
	
	public void init(int sessionID, String password, int player){
		this.player = player;
		this.sessionID = sessionID;
		this.password = password;
		this.state = 0;
	}
	
	public int getPlayer()
	{
		return player;
	}
	
	public int getSessionID()
	{
		return sessionID;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setState(int state){
		this.state = state;
	}
	
	public int getState(){
		return state;
	}

	public void setP1Name(String p1name) {
		this.p1name = p1name;
	}
	
	public String getP1Name() {
		return p1name;
	}
	
	public void setP2Name(String p2name) {
		this.p2name = p2name;
	}
	
	public String getP2Name() {
		return p2name;
	}

	public int getP1Character() {
		return p1Character;
	}

	public void setP1Character(int character) {
		this.p1Character = character;
	}

	public int getP2Character() {
		return p2Character;
	}

	public void setP2Character(int character) {
		this.p2Character = character;
	}


}
