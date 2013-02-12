package conn;

import main.Character;
import main.Player;

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
	private Player p1;
	private Player p2;
	private int state = initial;
	private int sessionID;
	private String password;
	private int[] board;
	
	
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

	public Player getP1() {
		return p1;
	}

	public void setP1(Player p1) {
		this.p1 = p1;
	}

	public Player getP2() {
		return p2;
	}

	public void setP2(Player p2) {
		this.p2 = p2;
	}

	public int[] getBoard(){
		return board;
	}
	
	public void setBoard(int[] board) {
		this.board = board;
	}
}
