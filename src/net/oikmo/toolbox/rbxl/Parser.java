package net.oikmo.toolbox.rbxl;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Parser {
	public static Roblox loadRBXL(String name) {
		File file = new File("src/net/oikmo/toolbox/rbxl/"+ name +".rbxl");  
		Roblox roblox = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Roblox.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
			roblox = (Roblox) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

		return roblox;
	}
}
