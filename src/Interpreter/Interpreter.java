package Interpreter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import behaviouralmodel.*;

public class Interpreter {
	
	// used in importing files
	private static int counter = 0;
	private static LinkedList<String> xmlWords = new LinkedList<String>();
	
	// Utility
	
	// begin the interpreter
	public static void begin(){
		
		HTN htn = new HTN();
		
		String map;
		String plan;
		String concretePlan;
		
		Scanner scanner = new Scanner(System.in);
		
		// ask them for the map file, this builds the map
		System.out.println("Please specify map file.");
		map = scanner.next();
		System.out.println("Map loaded.");
		
		// ask them for the parameterised plan
		// this specifies HOW to perform goals (goalClearBuilding needs to take in array of actions)
		System.out.println("Please specify parameterised plan file.");
		plan = scanner.next();
		System.out.println("Parameterised plan loaded.");
		
		// import the map
		htn = importXMLMap(map, htn);
		
		// import the paramterised plan
		htn = importXMLParamaterisedPlan(plan, htn);
		
		System.out.println("Please specify where to save concrete execution file.");
		System.out.println("Building concrete plan...");
		concretePlan = scanner.next();
		// build concrete plan
			// keep a list of all of the variables listed in the parameterised plan and allow the user to 
		
		System.out.println("Concrete plan complete.");
		
		System.out.println("Running simulation..");

	}
	
	// called when the interpreter is finished
	public static void end(String executionTrace){
		
		String planExecutionTrace;
		
		Scanner scanner = new Scanner(System.in);
		
		// shows the simulation has been completed
		System.out.println("Simulation complete");
		
		// write plan execution trace to file
		System.out.println("Please specify where to save plan execution trace file");
		planExecutionTrace = scanner.next();
		printExecutionTrace(executionTrace, planExecutionTrace);
		System.out.println("Plan execution trace file saved");
		
	}
	
	private static LinkedList<String> processXMLMap(String file) throws IOException{
		
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
				
		// returns a linked list of all of the tags and their values from the xml file
		return words;
		
	}
	
	private static boolean checkWord(String word){
		
		if(word.equals("\t") | word.equals("\t\t") | word.equals("\t\t\t")){
			return false;
		}
		
		return true;
	}
	
	private static void printExecutionTrace(String executionTrace, String file){
		
		try{
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file + ".XML"));
			fileWriter.write(executionTrace);
			fileWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	// Editor
	public static HTN buildXMLConcretePlan(String file, HTN map){
		
		return map;
	}
	
	//display variables
	private static LinkedList<String> showVariables(HTN htn, LinkedList<String> variables){
		
		LinkedList<String> vars = new LinkedList<String>();
		
		Scanner scanner = new Scanner(System.in);
		
		// show all the unit ids given from the map file
		System.out.println("Available Units");
		for(int k = 0; k < htn.getUnits().size(); k++){
			System.out.println("Unit" + (k+1) + htn.getUnit(k).getId());
		}
		
		System.out.println("Available Buildings");
		// show all the building ids given from the map file
		for(int k = 0; k < htn.getBuildings().size(); k++){
			System.out.println("Building" + (k+1) + htn.getBuilding(k).getId());
			
		}
		
		//loop through all the variables from the parameterised plan
		System.out.println("Variables to be set");
		System.out.println("Set each given variable with the objects id from the list of available objects above.");
		for(int k = 0; k < variables.size(); k++){
			  System.out.println(variables.get(k) + " = " + "...");
			  String input = scanner.next();
			  
			  // adds the 2 ids to a linked list
			  vars.add(variables.get(k));
			  vars.add(input);
		}
		
		return vars;
	}
	
	// Importing
	public static HTN importXMLParamaterisedPlan(String file, HTN map){
		
		
		
		return map;
	}
	
 	public static HTN importXMLMap(String file, HTN map){
		
		try {
			xmlWords = processXMLMap(file);
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
		
		newMap.addBuildings(buildings);
		newMap.addUnits(units);
		
		return newMap;
	}

	private static Unit importUnit(){
		
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
				unitMembers.add(importUnitMember(newUnit));
			}
		}
		
		newUnit = new Unit((int)Float.parseFloat(x), (int)Float.parseFloat(y), id);
		newUnit.setUnitMembers(unitMembers);
		
		return newUnit;
	}
	
	private static UnitMember importUnitMember(Unit unit){
		
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
	
	private static Building importBuilding(){
		
		String id = "";
		String x = "";
		String y = "";
		String enemies = "";
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
			else if(xmlWords.get(counter).equals("<Enimies>")){
				counter++;
				enemies = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Door>")){
				doors.add(importDoor());
			}
		}
		
		building = new Building((int)Float.parseFloat(x), (int)Float.parseFloat(y), 
				(int)Float.parseFloat(width), (int)Float.parseFloat(height), 
				Boolean.parseBoolean(enemies), id);
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

	// Exporting
 	public static void exportXML(HTN map, String fileLoc, String fileName){
		 
		String XMLFile = "<Map>";
		  
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
		
		XMLFile = XMLFile +  "</Map>";
		
		
		try{
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileLoc + fileName + ".XML"));
			fileWriter.write(XMLFile);
			fileWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private static String XMLTag(String tag, String value){
		
		String _tag;
			
		_tag = '<' + tag + '>' + value + "</" + tag + '>';
		
		return _tag;
	}
	
	private static String buildingToXML(Building building){
		
		String _tag = System.lineSeparator() + '\t' + "<Building>" + System.lineSeparator();
		
		_tag = _tag + '\t' + '\t' + XMLTag("Id", building.getId()) + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("X", building.getX() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("Y", building.getY() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("Enimies", building.enemiesInitial + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("Width", building.getWidth() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("Height", building.getHeight() + "") + System.lineSeparator();
		
		for(int k = 0; k < building.getDoors().size(); k++){
			_tag = _tag + doorToXML(building.getDoors().get(k));
		}
		
		return _tag + '\t' + "</Building>" + System.lineSeparator();
	}
	
	private static String doorToXML(Door door){
		
		String _tag = System.lineSeparator() + '\t' + '\t' + "<Door>" + System.lineSeparator();
		
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Id", door.getId()) + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("X", door.getX() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Y", door.getY() + "") + System.lineSeparator();
		
		return _tag + '\t' + '\t' + "</Door>" + System.lineSeparator();
	}

	private static String unitToXML(Unit unit){
		
		String _tag = System.lineSeparator() + '\t' + "<Unit>" + System.lineSeparator();
		
		_tag = _tag + '\t' + '\t' + XMLTag("Id", unit.getId()) + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("X", unit.getX() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("Y", unit.getY() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + XMLTag("GrenadesUsed", unit.getGrenadesUsed() + "") + System.lineSeparator();
		if(unit.inBuilding() != null){
			_tag = _tag + '\t' + '\t' + XMLTag("InBuildingId", unit.inBuilding().getId() + "") + System.lineSeparator();
		}else{
			_tag = _tag + '\t' + '\t' + XMLTag("InBuildingId", "NULL") + System.lineSeparator();
		}
		
		
		for(int x = 0; x < unit.GetUnitMembers().size(); x++){
			_tag = _tag + unitMemberToXML(unit.GetUnitMembers().get(x));
		}
		
		return _tag + '\t' + "</Unit>" + System.lineSeparator();
	}
	
	private static String unitMemberToXML(UnitMember unitMember){
		
		String _tag = System.lineSeparator() + '\t' + '\t' + "<UnitMember>" + System.lineSeparator();
		
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Id", unitMember.getId()) + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("X", unitMember.getX() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Y", unitMember.getY() + "") + System.lineSeparator();
		
		return _tag + '\t' + '\t' + "</UnitMember>" + System.lineSeparator();
	}

}
