package conn;

import org.newdawn.slick.SlickException;

import conn.Packet.*;
import main.ActivityScore;
import main.HRRUClient;
import main.Play;
import main.Player;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class NetworkListener extends Listener{
	
	private final int failed = -3;
	private final int cancelled = -2;
	private final int waiting = 0; 
	private final int joined = 1;
	private final int start = 4;
	private final int player1char = 5;
	private final int player2char = 6;
	private final int p2_turn = 8;
	private final int start_play = 9;
	private final int play = 10;
	
	public void init(Client client) {
	}

	public void connected(Connection c) {
		Log.info("[SERVER] Someone has connected.");
	}

	public void disconnected(Connection c) {
		Log.info("[SERVER] Someone has disconnected.");		
	}
	
	public void received(Connection c, Object o){
		if(o instanceof Packet1CreateAnswer)
		{
			boolean answer = ((Packet1CreateAnswer)o).accepted;
			if(answer)
			{
				int sessionID = ((Packet1CreateAnswer)o).sessionID;
				String password = ((Packet1CreateAnswer)o).password;	
				HRRUClient.cs.init(sessionID, password, 1);
				HRRUClient.cs.setState(waiting);
				Log.info("Connected to session " + sessionID + " with password " + password + " as player 1");
			}
		}
		if(o instanceof Packet3JoinAnswer)
		{
			boolean answer = ((Packet3JoinAnswer)o).accepted;
			if(answer)
			{
				int sessionID = ((Packet3JoinAnswer)o).sessionID;
				String password = ((Packet3JoinAnswer)o).password;	
				HRRUClient.cs.init(sessionID, password, 2);
				Player player1;
				try {
					player1 = new Player(((Packet3JoinAnswer)o).player1Name);
					HRRUClient.cs.setP1(player1);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HRRUClient.cs.setState(joined);
				Log.info("Connected to previous session " + sessionID + " with password " + password + " as player 2");
			}
			else
			{
				Log.info("Failed to connect.");
				HRRUClient.cs.setState(failed);
			}

		}
		if(o instanceof Packet4ConnectionEstablished)
		{
			String player2Name = ((Packet4ConnectionEstablished)o).player2Name;
			Log.info("Connection established with " + player2Name);
			Player player2;
			try {
				player2 = new Player(player2Name);
				HRRUClient.cs.setP2(player2);
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HRRUClient.cs.setState(2);
		}
		if(o instanceof Packet6CancelRequestResponse)
		{
			Log.info("Cancelled");
			HRRUClient.cs.setState(cancelled);
		}
		if(o instanceof Packet7Ready)
		{
			int player = ((Packet7Ready)o).player;
			if(player == 1)
			{
				HostServer.p1ready = true;
				JoinServer.p1ready = true;
			}
			else if(player == 2)
			{
				HostServer.p2ready = true;
				JoinServer.p2ready = true;
			}
		}
		if(o instanceof Packet8Start)
		{
			HRRUClient.cs.setBoard(((Packet.Packet8Start)o).board);
			HRRUClient.cs.setState(start);
		}
		if(o instanceof Packet9CharacterSelect)
		{
			int characterID = ((Packet.Packet9CharacterSelect)o).characterID;
			int player = ((Packet.Packet9CharacterSelect)o).player;
			System.out.println("derp");
			
			if(player == 1)
			{
				System.out.println("derp1");
				HRRUClient.cs.getP1().setPlayerCharacterID(characterID);
				HRRUClient.cs.setState(player1char);
			}
			if(player == 2)
			{
				System.out.println("derp2");
				HRRUClient.cs.getP2().setPlayerCharacterID(characterID);
				HRRUClient.cs.setState(player2char);
			}
		}
		if(o instanceof Packet10ChatMessage)
		{
			Play.chatFrame.appendRowOther("color1", ((Packet10ChatMessage)o).message);
		}
		if(o instanceof Packet11TurnMessage)
		{
			int player = ((Packet11TurnMessage)o).playerID;
			int moves = ((Packet11TurnMessage)o).moves;
			if(player == 1)
			{
				HRRUClient.cs.getP1().setPosition(moves);
				HRRUClient.cs.setState(p2_turn);
			}
			else if(player == 2)
			{
				if(moves > 0)
					HRRUClient.cs.getP2().setPosition(moves);	
				System.out.println("receive");
				HRRUClient.cs.setState(start_play);
			}
			
		}
		if(o instanceof Packet13Play)
		{
			HRRUClient.cs.getP1().setReady(0);
			HRRUClient.cs.getP2().setReady(0);
			int activity = ((Packet13Play)o).activity;
			int activity_id = ((Packet13Play)o).activity_id;
			HRRUClient.cs.setActivity(activity);
			HRRUClient.cs.setActivity_id(activity_id);
			HRRUClient.cs.setState(play);
			System.out.println(activity);
			System.out.println(activity_id);
			
		}
		if(o instanceof Packet14QuestionComplete)
		{
			int player = ((Packet14QuestionComplete)o).player;
			int question_difficulty = ((Packet14QuestionComplete)o).difficulty;
			int elapsedtime = ((Packet14QuestionComplete)o).elapsedtime;
			int points = ((Packet14QuestionComplete)o).points;
			int overall = ((Packet14QuestionComplete)o).overall;
			boolean correct = ((Packet14QuestionComplete)o).correct;
			ActivityScore otherPlayerResult = new ActivityScore(0,0,0,0,0, false);
			
			otherPlayerResult.setActivity(1);
			otherPlayerResult.setDifficulty(question_difficulty);
			otherPlayerResult.setElapsedtime(elapsedtime);
			otherPlayerResult.setPoints(points);
			otherPlayerResult.setOverall(overall);
			otherPlayerResult.setCorrect(correct);
			
			if(player == 1)
			{
				HRRUClient.cs.getP1().setReady(1);
				HRRUClient.cs.getP1().setActivityScore(otherPlayerResult);
			}
			else
			{
				HRRUClient.cs.getP2().setReady(1);
				HRRUClient.cs.getP2().setActivityScore(otherPlayerResult);
			}
		}
		if(o instanceof Packet15PuzzleComplete)
		{
			int player = ((Packet15PuzzleComplete)o).player;
			int question_difficulty = ((Packet15PuzzleComplete)o).difficulty;
			int elapsedtime = ((Packet15PuzzleComplete)o).elapsedtime;
			int points = ((Packet15PuzzleComplete)o).points;
			int overall = ((Packet15PuzzleComplete)o).overall;
			boolean correct = ((Packet15PuzzleComplete)o).correct;
			ActivityScore otherPlayerResult = new ActivityScore(0,0,0,0,0, false);
			
			otherPlayerResult.setActivity(2);
			otherPlayerResult.setDifficulty(question_difficulty);
			otherPlayerResult.setElapsedtime(elapsedtime);
			otherPlayerResult.setPoints(points);
			otherPlayerResult.setOverall(overall);
			otherPlayerResult.setCorrect(correct);
			
			if(player == 1)
			{
				HRRUClient.cs.getP1().setReady(1);
				HRRUClient.cs.getP1().setActivityScore(otherPlayerResult);
			}
			else
			{
				HRRUClient.cs.getP2().setReady(1);
				HRRUClient.cs.getP2().setActivityScore(otherPlayerResult);
			}
		}
		if(o instanceof Packet00SyncMessage)
		{
			HRRUClient.cs.setSync(true);
		}
		if(o instanceof Packet00SyncMessage)
		{
			HRRUClient.cs.setSync(true);
		}
	}

}
