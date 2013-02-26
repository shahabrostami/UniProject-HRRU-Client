package conn;

import conn.Packet.*;
import main.HRRUClient;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class Connection {
	
	private Client client;
	
	public Connection() {
		client = new Client(65536, 16384);
		
		NetworkListener n1 = new NetworkListener();
		n1.init(client);
		client.addListener(n1);
		client.start();
		register();

		try{
			client.connect(5000, "2.27.42.249", 9991);
			HRRUClient.ConnectionSuccessful = true;
		} catch (IOException e) {
			e.printStackTrace();
			client.stop();
			HRRUClient.ConnectionSuccessful = false;
		}
	}
	
	private void register(){
		Kryo kryo = client.getKryo();
		kryo.register(Packet00SyncMessage.class);
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
		kryo.register(Packet11TurnMessage.class);
		kryo.register(Packet12PlayReady.class);
		kryo.register(Packet13Play.class);
		kryo.register(Packet14QuestionComplete.class);
		kryo.register(Packet15PuzzleComplete.class);
		kryo.register(Packet16SendBid.class);
		kryo.register(Packet17EndBid.class);
		kryo.register(int[].class);
	}
	
	public Client getClient()
	{
		return client;
	}
}
