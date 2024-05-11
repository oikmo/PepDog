package net.pepdog.toolbox.rbxl;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Parser {
	
	public static Roblox loadRBXL(String name) {
		Roblox roblox = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Roblox.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
			roblox = (Roblox) jaxbUnmarshaller.unmarshal(Parser.class.getResourceAsStream("/assets/rbxl/"+ name +".rbxl"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}  

		return roblox;
	}
	
	public static Roblox loadRBXLFromContent(String content) {
		Roblox roblox = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Roblox.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
			StringReader reader = new StringReader(content);

			roblox = (Roblox) jaxbUnmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			e.printStackTrace();
		}  

		return roblox;
	}
}
