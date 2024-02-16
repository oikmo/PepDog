package net.oikmo.toolbox;

//import org.lwjgl.opengl.Display;

/*import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;*/

public class Discord {
	/*public static String details = "null";
	public static String state = "null";
	
	public static Thread thread;
	
	public static void start() {
		DiscordRPC lib = DiscordRPC.INSTANCE;
		String appID = "1044313022849630220";
		String steamID = "";
		DiscordEventHandlers handlers = new DiscordEventHandlers();
		handlers.ready = (user) -> System.out.println("Ready!");
		lib.Discord_Initialize(appID, handlers, true, steamID);
		DiscordRichPresence presence = new DiscordRichPresence();
		presence.startTimestamp = System.currentTimeMillis() / 1000; //epoch second
		
		//presence.partySize = 1;
		//presence.partyMax = 5;
		//presence.partyId = "ae488379-351d-4a4f-ad32-2b9b01c91657";
		//presence.joinSecret = "MTI4NzM0OjFpMmhuZToxMjMxMjM=";
		
		presence.largeImageKey = "icon";
		presence.details = details;
		presence.state = state;
		lib.Discord_UpdatePresence(presence);
		//in a worker thread
		thread = new Thread(() -> {
			while (!Display.isCloseRequested()) {
				lib.Discord_RunCallbacks();
				try {
					Thread.sleep(0);
				} catch (InterruptedException ignored) {}
			}
		}, "RPC-Callback-Handler");
		thread.start();
	}
	
	public static void setValues(String details, String state) {
		Discord.details = details;
		Discord.state = state;
	}*/
}
