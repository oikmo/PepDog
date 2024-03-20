package net.pepdog.engine.lua;

import java.io.StringReader;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaLoader {
	
	public LuaLoader() {}
	
	private static String script = "print(game:penis())"
			+ "print( game:GetSomethingFromThis('HELP ME'))"
			+ "print(1 + 1)";
	
	public static void main(String[] args) {
		Globals globals = JsePlatform.standardGlobals();
		globals.load(new DataModel());
		globals.load(new StringReader(script), "main.lua").call();
	}
	
	
}
