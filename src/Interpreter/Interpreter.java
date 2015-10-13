package Interpreter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import behaviouralmodel.*;

public class Interpreter {
	
	// used in importing files
	static int counter = 0;
	static LinkedList<String> xmlWords = new LinkedList<String>();
	
	// THIS IS FOR TESTING
	public static void main1(String[] args) throws IOException{
		
		HTN testHTN = new HTN();
		HTN importedHTN;
		
		//TODO add boolean for eniemies in XML file
		Building building = new Building(5, 5, 10, 10, false, "0");
		building.addDoor(1, 1);
		Unit unit = new Unit(1, 1, "5");
		
		testHTN.addBuilding(building);
		testHTN.addUnit(unit);
		
		String fileLoc = "C:/";
		String fileName = "TEST_XML_FILE";
		
		Interpreter.exportXML(testHTN, fileLoc, fileName);
		
		importedHTN = Interpreter.importXML(fileLoc + fileName, testHTN);
	}
	
	// Importing
	public static HTN importXML(String file, HTN map){
		
		try {
			xmlWords = processXML(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// the HTN to be build from the XML file
		HTN newMap = new HTN();
		
		LinkedList<Unit> units = new LinkedList<Unit>();
		LinkedList<Building> buildings = new LinkedList<Building>();
		
		for(counter = 0; counter < xmlWords.size(); counter++){
			if(xmlWords.get(counter).equals("<Building>")){
				buildings.add(importBuilding());
			}
			else if(xmlWords.get(counter).equals("<Unit>")){
				units.add(importUnit());
			}
			
		}
		
		//newMap.add
		
		return newMap;
	}
	
	public static LinkedList<String> processXML(String file) throws IOException{
		
		// read the file and place it in a string
		BufferedReader in = new BufferedReader(new FileReader(file + ".XML"));
		LinkedList<String> lines = new LinkedList<String>();
				
				
		// put all the lines into 'lines' and read over each line
		String line;
		try {
			while((line = in.readLine()) != null)
			{
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		in.close();
				
		// the content of the file
		String content = "";
		// stores the current tag or content being read
		String curWord = "";
		// stores all the tags and their content in an array
		LinkedList<String> words = new LinkedList<String>();
				
		//loop over the ArrayList of the lines
		for(int z = 0; z < lines.size(); z++){
			content = lines.get(z);
			// loop over the contents of the line and separate them into tags and content
			for(int x = 0; x < content.length(); x++){
						
				// first tag
				if(content.charAt(x) == '<'){

					while(content.charAt(x) != '>'){
						curWord += content.charAt(x);
						x++;
					}
					curWord += content.charAt(x);
							
					if(checkWord(curWord)){
						words.add(curWord);
					}
					curWord = "";
							
					// content of tag
					if(content.charAt(x) == '>' & x != content.length()-1){
						x++;
						while(content.charAt(x) != '<'){
							curWord += content.charAt(x);
							x++;
						}
								
						if(checkWord(curWord)){
							words.add(curWord);
						}
						curWord = "";
								
						// end of tag
						if(content.charAt(x) == '<'){
							while(content.charAt(x) != '>'){
								curWord += content.charAt(x);
								x++;
							}
							curWord += content.charAt(x);
							if(checkWord(curWord)){
								words.add(curWord);
							}
							curWord = "";
						}
					}
				}
			}			
		}
				
		return words;
		
	}
	
	public static Unit importUnit(){
		
		String id = "";
		String x = "";
		String y = "";
		Unit newUnit = null;
		LinkedList<UnitMember> unitMembers = new LinkedList<UnitMember>();
		
		while(!(xmlWords.get(counter).equals("</Unit>"))){
			
			counter++;
			
			if(xmlWords.get(counter).equals("<Id>")){
				counter++;
				id = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<X>")){
				counter++;
				x = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Y>")){
				counter++;
				y = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<UnitMember>")){
				counter++;
				unitMembers.add(importUnitMember(newUnit));
				counter++;
			}
		}
		
		newUnit = new Unit((int)Float.parseFloat(x), (int)Float.parseFloat(y), id);
		newUnit.setUnitMembers(unitMembers);
		
		return newUnit;
	}
	
	public static UnitMember importUnitMember(Unit unit){
		
		String x = "";
		String y = "";
		String id = "";
		
		UnitMember newUnitMember;
		
		while(!(xmlWords.get(counter).equals("</UnitMember>"))){
			
			counter++;
			
			if(xmlWords.get(counter).equals("<Id>")){
				counter++;
				id = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<X>")){
				counter++;
				x = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Y>")){
				counter++;
				y = xmlWords.get(counter);
				counter++;
			}
		}
		
		newUnitMember = new UnitMember((int)Float.parseFloat(x), (int)Float.parseFloat(y), unit, id);
		
		return newUnitMember;
	}
	
	public static Building importBuilding(){
		
		String id = "";
		String x = "";
		String y = "";
		String width = "";
		String height = "";
		
		Building building = null;
		
		LinkedList<Door> doors = new LinkedList<Door>();
		
		while(!(xmlWords.get(counter).equals("</Building>"))){
			
			counter++;
			
			if(xmlWords.get(counter).equals("<Id>")){
				counter++;
				id = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<X>")){
				counter++;
				x = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Y>")){
				counter++;
				y = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Width>")){
				counter++;
				width = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Height>")){
				counter++;
				height = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Door>")){
				counter++;
				doors.add(importDoor());
				counter++;
			}
		}
		
		building = new Building((int)Float.parseFloat(x), (int)Float.parseFloat(y), 
				(int)Float.parseFloat(width), (int)Float.parseFloat(height), false, id);
		building.addDoors(doors);
		
		return building;
	}

	private static Door importDoor() {
		
		String x = "";
		String y = "";
		String id = "";
		
		Door door = null;
		
		while(!(xmlWords.get(counter).equals("</Door>"))){
			counter++;
			
			if(xmlWords.get(counter).equals("<Id>")){
				counter++;
				id = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<X>")){
				counter++;
				x = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Y>")){
				counter++;
				y = xmlWords.get(counter);
				counter++;
			}
		}
		
		door = new Door((int)Float.parseFloat(x), (int)Float.parseFloat(y), id);
		
		return door;
	}

	public static boolean checkWord(String word){
		
		if(word.equals("\t") | word.equals("\t\t") | word.equals("\t\t\t")){
			return false;
		}
		
		return true;
	}
	
	// Exporting
 	public static void exportXML(HTN map, String fileLoc, String fileName){
		 
		String XMLFile = "<XMLMap>";
		  
		//Write currentMap Size
		XMLFile = XMLFile + System.lineSeparator() + '\t' + XMLTag("Width", map.gridWidth + "");
		XMLFile = XMLFile + System.lineSeparator() + '\t' + XMLTag("Height", map.gridHeight + "") + System.lineSeparator();
		 
		//Write all the Buildings to the XML
		for(int x = 0; x < map.getBuildings().size(); x++){
			XMLFile = XMLFile + buildingToXML(map.getBuilding(x));
		}
				
		//Write all the Squads to the XML
		for(int x = 0; x < map.getUnits().size(); x++){
			XMLFile = XMLFile + unitToXML(map.getUnit(x));
		}
		
		XMLFile = XMLFile +  "</XMLMap>";
		
		
		try{
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileLoc + fileName + ".XML"));
			fileWriter.write(XMLFile);
			fileWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static String XMLTag(String tag, String value){
		
		String _tag;
			
		_tag = '<' + tag + '>' + value + "</" + tag + '>';
		
		return _tag;
	}
	
	public static String buildingToXML(Building building){
		
		String _tag = System.lineSeparator() + '\t' + "<Building>" + System.lineSeparator();
		
		_tag = _tag + '\t' + '\t' + XMLTag("Id", building.getId()) + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("X", building.getX() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("Y", building.getY() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("Width", building.getWidth() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("Height", building.getHeight() + "") + System.lineSeparator();
		
		for(int k = 0; k < building.getDoors().size(); k++){
			_tag = _tag + doorToXML(building.getDoors().get(k));
		}
		
		return _tag + '\t' + "</Building>" + System.lineSeparator();
	}
	
	public static String doorToXML(Door door){
		
		String _tag = System.lineSeparator() + '\t' + '\t' + "<Door>" + System.lineSeparator();
		
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Id", door.getId()) + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("X", door.getX() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Y", door.getY() + "") + System.lineSeparator();
		
		return _tag + '\t' + '\t' + "</Door>" + System.lineSeparator();
	}

	public static String unitToXML(Unit unit){
		
		String _tag = System.lineSeparator() + '\t' + "<Unit>" + System.lineSeparator();
		
		_tag = _tag + '\t' + '\t' + XMLTag("Id", unit.getId()) + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("X", unit.getX() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("Y", unit.getY() + "") + System.lineSeparator();
		
		
		for(int x = 0; x < unit.GetUnitMembers().size(); x++){
			_tag = _tag + unitMemberToXML(unit.GetUnitMembers().get(x));
		}
		
		return _tag + '\t' + "</Unit>" + System.lineSeparator();
	}
	
	public static String unitMemberToXML(UnitMember unitMember){
		
		String _tag = System.lineSeparator() + '\t' + '\t' + "<UnitMember>" + System.lineSeparator();
		
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Id", unitMember.getId()) + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("X", unitMember.getX() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Y", unitMember.getY() + "") + System.lineSeparator();
		
		return _tag + '\t' + '\t' + "</UnitMember>" + System.lineSeparator();
	}
}
