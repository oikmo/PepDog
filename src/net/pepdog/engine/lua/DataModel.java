package net.pepdog.engine.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.pepdog.engine.Loader;
import net.pepdog.engine.audio.AudioMaster;

public class DataModel extends TwoArgFunction {
	
	/* Bitch i have to implement this shit and now I want to hang myshelf.
	 * 
	 * (datamodel.cpp)
	 * 
	 *	void RBX::Datamodel::open() {
     *		workspace = new Workspace();
     *		runService = new RunService();
     *		lighting = new Lighting();
     *		scene = new Scene(); //java note: at least I have that ;-;
     *		controllerService = new ControllerService();
     *		thumbnailGenerator = new ThumbnailGenerator();
     * 		scriptContext = new ScriptContext();
     * 		soundService = new SoundService();
     *		players = new RBX::Network::Players();   
     *		jointService = new JointsService();
     *		guiRoot = Gui::singleton();
     *		runService->scriptContext = scriptContext;
     *		yieldingThreads = new Lua::YieldingThreads(scriptContext);
	 *
     *		fillExplorerWindow();
	 *
     *		//if not server
     *		//guiRoot->initFont();
	 * }
	 */
	
	public int load(String url) {
		String asset = new String(url);
		int result = 0;
		if(asset.contains("rbxassetid://")) {
			asset = asset.replace("rbxassetid://", "content/");
		}
		
		if(asset.substring(Math.max(asset.length() - 4, 0)).contentEquals(".png")) {
			result = Loader.loadTexture("/" + asset.replace(".png", ""));
		} 
		else if(asset.substring(Math.max(asset.length() - 4, 0)).contentEquals(".wav")) {
			System.out.println(asset.replace(".wav", ""));
			
			result = AudioMaster.getSound(asset.replace(".wav", ""));
		}
		
		return result;
	}
	
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue game = tableOf();
		game.set("penis", new penis());
		game.set("GetSomethingFromThis", new GetSomethingFromThis());
		env.set("game", game);
		return game;
	}
	
	static class penis extends ZeroArgFunction {
		public LuaValue call() {
			return LuaValue.valueOf("penis");
		}
	}
	
	static class GetSomethingFromThis extends TwoArgFunction {
		public LuaValue call(LuaValue self, LuaValue input) {
			return LuaValue.valueOf("you have put in -> " + input.checkjstring());	
		}
	}
}
