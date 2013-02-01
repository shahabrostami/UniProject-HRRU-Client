package main;

import main.Packet.*;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class NetworkListener extends Listener{
	
	private Client client;
	
	private final int failed = -3;
	private final int cancelled = -2;
	private final int waiting = 0; 
	private final int joined = 1;
	private final int established = 2;
	private final int ready = 3;
	
	public void init(Client client) {
		this.client = client;
	}

	public void connected(Connection c) {
		Log.info("[SERVER] Someone has connected.");
	}

	public void disconnected(Connection c) {
		Log.info("[SERVER] Someone has disconnected.");		
	}
	
	public void received(Connection c, Object o) {
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
				HRRUClient.cs.setP1Name(((Packet3JoinAnswer)o).player1Name);
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
			HRRUClient.cs.setState(2);
			String player2Name = ((Packet4ConnectionEstablished)o).player2Name;
			Log.info("Connection established with " + player2Name);
			HRRUClient.cs.setP2Name(player2Name);
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
		
	}


}
