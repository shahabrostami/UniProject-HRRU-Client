package conn;

import main.Player;

public class ConnectionState {
	
	private final int initial = -1;
	private int timer = 50000;	
	private int player;
	private Player p1;
	private Player p2;
	private int state = initial;
	private int activity;
	private int activity_id;
	private int sessionID;
	private String password;
	private int[] board;
	private boolean sync;
	
	
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
	
	public void setPlayer(int player)
	{
		this.player = player;
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

	public int getTimer() {
		return timer;
	}
	
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
	public void updateTimer(int timer) {
		this.timer -= timer;
	}
	
	public int getActivity() {
		return this.activity;
	}
	
	public void setActivity(int activity) {
		this.activity = activity;
	}

	public int getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}

	public boolean isSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}
	
}
