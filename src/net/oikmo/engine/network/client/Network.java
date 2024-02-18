package net.oikmo.engine.network.client;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Network extends Listener {

	public Client client;
	String ip;
	int port;
	public static List<UNDClient> clients = new ArrayList<UNDClient>();
	public static List<Integer> clientsID = new ArrayList<>();
	
	public Network(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	public void connect() {
		try{
			System.out.println("Connecting...");
			client = new Client();
			client.getKryo().register(PacketUpdateX.class);
			client.getKryo().register(PacketUpdateY.class);
			client.getKryo().register(PacketUpdateZ.class);
			client.getKryo().register(PacketNewAgent.class);
			client.getKryo().register(PacketRemoveAgent.class);

			client.start();
			client.connect(5000, "localhost", 27960, 27960);
			client.addListener(this);
			System.out.println("Running networking client!");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void received(Connection c, Object o){
		if(o instanceof PacketUpdateX){
			PacketUpdateX packet = (PacketUpdateX) o;
			getAgentByID(packet.id).x = packet.x;
		} else if(o instanceof PacketUpdateY){
			PacketUpdateY packet = (PacketUpdateY) o;
			getAgentByID(packet.id).y = packet.y;
		} else if(o instanceof PacketUpdateZ){
			PacketUpdateZ packet = (PacketUpdateZ) o;
			getAgentByID(packet.id).z = packet.z;
		} 
		else if(o instanceof PacketUpdateRotX){
			PacketUpdateRotX packet = (PacketUpdateRotX) o;
			getAgentByID(packet.id).rotX = packet.x;
		} else if(o instanceof PacketUpdateRotY){
			PacketUpdateRotY packet = (PacketUpdateRotY) o;
			getAgentByID(packet.id).rotY = packet.y;
		} else if(o instanceof PacketUpdateRotZ){
			PacketUpdateRotZ packet = (PacketUpdateRotZ) o;
			getAgentByID(packet.id).rotZ = packet.z;
		}
		
		if(o instanceof PacketNewAgent){
			PacketNewAgent packet = (PacketNewAgent) o;
			UNDClient client = new UNDClient();
			client.username = "unset";
			client.id = packet.id;
			client.x = packet.x;
			client.y = packet.y;
			client.z = packet.z;
			client.rotX = packet.rotX;
			client.rotY = packet.rotY;
			client.rotZ = packet.rotZ;
			
			clientsID.add(packet.id);
			clients.add(client);
			System.out.println("A new player has joined the world. ID: "+client.id);
			
		}else if(o instanceof PacketRemoveAgent){
			PacketRemoveAgent packet = (PacketRemoveAgent) o;
			clientsID.remove((Integer)packet.id);
			clients.remove(getAgentByID(packet.id));
			System.out.println("A player has left the world. ID: "+packet.id);
			
		}
	}
	
	public static UNDClient getAgentByID(int id){
		for(UNDClient client : clients){
			if(client.id == id) return client;
		}
		return null;
	}
}
