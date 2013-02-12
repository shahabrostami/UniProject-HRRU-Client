package conn;

import conn.*;
import conn.Packet.*;
import main.HRRUClient;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

public class Connection {
	
	private Client client;
	
	public Connection() {
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
		kryo.register(Packet8Start.class);
		kryo.register(Packet9CharacterSelect.class);
		kryo.register(Packet10ChatMessage.class);
		kryo.register(int[].class);
	}
	
	public Client getClient()
	{
		return client;
	}
}
