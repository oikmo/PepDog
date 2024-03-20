package net.pepdog.engine.network.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class UNDServer extends Listener {
	
	static Server server;
	
	static List<UNDClient> clients = new ArrayList<UNDClient>();
	
	public static void main(String[] args) throws IOException{
		server = new Server();
		server.bind(27960, 27960);
		server.getKryo().register(PacketUpdateX.class);
		server.getKryo().register(PacketUpdateY.class);
		server.getKryo().register(PacketUpdateZ.class);
		server.getKryo().register(PacketNewAgent.class);
		server.getKryo().register(PacketRemoveAgent.class);
		server.start();
		server.addListener(new UNDServer());
		System.out.println("Server ready on port 27960");
	}
	
	public void received(Connection c, Object o){
		if(o instanceof PacketUpdateX){
			PacketUpdateX packet = (PacketUpdateX) o;
			getAgentByID(c.getID()).x = packet.x;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
			
		} else if(o instanceof PacketUpdateY){
			PacketUpdateY packet = (PacketUpdateY) o;
			getAgentByID(c.getID()).y = packet.y;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
			
		} else if(o instanceof PacketUpdateZ){
			PacketUpdateZ packet = (PacketUpdateZ) o;
			getAgentByID(c.getID()).z = packet.z;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
			
		} else if(o instanceof PacketUpdateRotX){
			PacketUpdateRotX packet = (PacketUpdateRotX) o;
			getAgentByID(c.getID()).rotX = packet.x;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
			
		} else if(o instanceof PacketUpdateRotY){
			PacketUpdateRotY packet = (PacketUpdateRotY) o;
			getAgentByID(c.getID()).rotY = packet.y;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
			
		} else if(o instanceof PacketUpdateRotZ){
			PacketUpdateRotZ packet = (PacketUpdateRotZ) o;
			getAgentByID(c.getID()).rotZ = packet.z;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
			
		}
	}
	
	public void connected(Connection c){
		System.out.println("Connection from "+c.getRemoteAddressTCP().getHostString());
		UNDClient client = new UNDClient();
		client.connection = c;
		client.username = "unset";
		client.x = 0;
		client.y = 0;
		client.z = 0;
		client.rotX = 0;
		client.rotY = 0;
		client.rotZ = 0;
		clients.add(client);
		
		PacketNewAgent newCoPacket = new PacketNewAgent();
		newCoPacket.id = c.getID();
		newCoPacket.x = 0;
		newCoPacket.y = 0;
		newCoPacket.z = 0;
		newCoPacket.rotX = 0;
		newCoPacket.rotY = 0;
		newCoPacket.rotZ = 0;
		server.sendToAllExceptTCP(c.getID(), newCoPacket);
		
		for(UNDClient cl : clients){
			if(cl.connection.getID() == c.getID()) continue;
			PacketNewAgent thirdPacket = new PacketNewAgent();
			thirdPacket.id = cl.connection.getID();
			thirdPacket.x = cl.x;
			thirdPacket.y = cl.y;
			thirdPacket.z = cl.z;
			thirdPacket.rotX = cl.rotX;
			thirdPacket.rotY = cl.rotY;
			thirdPacket.rotZ = cl.rotZ;
			c.sendTCP(thirdPacket);
		}
	}
	
	public void disconnected(Connection c){
		System.out.println("Connection dropped.");
		PacketRemoveAgent removePacket = new PacketRemoveAgent();
		removePacket.id = c.getID();
		server.sendToAllExceptTCP(c.getID(), removePacket);
		clients.remove(getAgentByID(c.getID()));
	}
	
	public static UNDClient getAgentByID(int id){
		for(UNDClient client : clients){
			if(client.connection.getID() == id){
				return client;
			}
		}
		return null;
	}
	
}
