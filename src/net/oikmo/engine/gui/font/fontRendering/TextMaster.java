package net.oikmo.engine.gui.font.fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.oikmo.engine.Loader;
import net.oikmo.engine.gui.component.GuiText;
import net.oikmo.engine.gui.font.fontMeshCreator.FontType;
import net.oikmo.engine.gui.font.fontMeshCreator.TextMeshData;
import net.oikmo.engine.renderers.gui.font.FontRenderer;

public class TextMaster {
	
	private static Loader loader;
	private static Map<FontType, List<GuiText>> texts = new HashMap<FontType, List<GuiText>>();
	private static FontRenderer renderer;
	
	public static void init(){
		renderer = new FontRenderer();
		loader = Loader.getInstance();
	}
	
	public static void render(){
		renderer.render(texts);
	}
	
	public static void loadText(GuiText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GuiText> textBatch = texts.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<GuiText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(GuiText text){
		List<GuiText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()){
			texts.remove(texts.get(text.getFont()));
		}
	}
	
	public static void cleanUp(){
		renderer.cleanUp();
	}

}
