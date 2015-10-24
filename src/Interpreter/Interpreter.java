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
	
	// begin the interpreter
	public static void begin() throws IOException{
		
		HTN htn = new HTN();
		
		String map = "";
		String plan = "";
		String concretePlan = "";
		
		Scanner scanner = new Scanner(System.in);
		
		// import the map
		while(map.equals("")){
			
			// ask them for the map file, this builds the map
			System.out.println("Please specify map file and press Enter.");
			System.out.println("Eg, C:/Example folder/Example Map file");
			map = scanner.nextLine();
			map = "C:/ICT XML Files/TEST_MAP";
			if(!map.equals("")){
				htn = importXMLMap(map);
			}
		}

		System.out.println("Map loaded.");
		
		while(plan.equals("")){
			
			// ask them for the parameterised plan
			// this specifies HOW to perform goals (goalClearBuilding needs to take in array of actions)
			System.out.println("Please specify parameterised plan file and press Enter.");
			System.out.println("Eg, C:/Example folder/Example Parameterised file");
			plan = scanner.nextLine();
			plan = "C:/ICT XML Files/TEST_HTN_PARAMETERISED_PLAN";
			
			if(!plan.equals("")){
				
				System.out.println("Parameterised plan loaded.");
				
				System.out.println(processXMLMap(plan));
				
				// this will get all the variable names given in the plan file
				LinkedList<String> vars = cutLinkedList(processXMLMap(plan), 
						processXMLMap(plan).indexOf("<Vars>"), 
								processXMLMap(plan).indexOf("</Vars>"));
				
				LinkedList<String> planContent = processXMLMap(plan);
				
				// shows the user the available builds and units in the map
				// also shows the user the values that need to be set
				
				LinkedList<String> varValues = showVariables(htn, vars);
				
				// saving the concrete file
				System.out.println("Please specify where to save concrete execution file and press Enter.");
				System.out.println("Eg, C:/Example folder/Example Concrete file");
				concretePlan = scanner.nextLine();
				concretePlan = "C:/ICT XML Files/TEST_HTN_CONCRETE_PLAN";
				
				// build the concrete plan and save it to 'planContent'
				LinkedList<String> concretePlanContent = buildXMLConcretePlan(htn, vars, varValues, planContent);
				
				saveConcreteFile(concretePlan, concretePlanContent);
				
				System.out.println("Building concrete plan...");
				// build concrete plan
					// keep a list of all of the variables listed in the parameterised plan and allow the user to
				
				
				System.out.println("Concrete plan complete.");
			}
		}
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
	
	// Utility
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
				
		// the content of the file line
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
				
				if(content.charAt(x) != '\t'){
					curWord += content.charAt((x));
				}
			}
			if(curWord != ""){
				words.add(curWord);
			}
			curWord = "";
		}
		
		// returns a linked list of all of the tags and their values from the xml file
		return words;
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
	
	private static LinkedList<String> cutLinkedList(LinkedList<String> list, int start, int end){
		
		LinkedList newList = new LinkedList<String>();
		
		for(int x = start; x <= end; x++){
			newList.add(list.get(x));
		}
		
		return newList;
		
	}
	
	
	// Concrete file construction and saving
	
	private static void saveConcreteFile(String concretePlan, LinkedList<String> concretePlanContent) {
		
		String content = "";
		int depth = -1;
		
		for(int k = 0; k < concretePlanContent.size(); k++){
			
			// hit starting tag, increase depth
			if(!(concretePlanContent.get(k).startsWith("</")) & concretePlanContent.get(k).startsWith("<")){
				depth++;
			}
			
			if(!concretePlanContent.get(k).startsWith("<")){
				depth++;
				content = content + new String(new char[depth]).replace("\0", "\t") + concretePlanContent.get(k) + System.lineSeparator();
				depth--;
			}else{
				content = content + new String(new char[depth]).replace("\0", "\t") + concretePlanContent.get(k) + System.lineSeparator();
			}
			
			// hit ending tag, decrease depth
			if(concretePlanContent.get(k).startsWith("</")){
				depth--;
			}
		}
		
		// save content to a file
		try{
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(concretePlan + ".XML"));
			fileWriter.write(content);
			fileWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	private static LinkedList<String> applyVars(LinkedList<String> newPlanVars, LinkedList<String> planContent, LinkedList<String> vars) {
		
		LinkedList<String> concretePlan = new LinkedList<String>();
		concretePlan.add("<HTNParameterisedPlan>");
		concretePlan.addAll(newPlanVars);
		
		for(int k = 0; k < vars.size(); k = k + 2){
			for(int x = planContent.indexOf("<Plan>"); x < planContent.size(); x++){
				if(vars.get(k).equals(planContent.get(x))){
					planContent.set(x, vars.get(k+1));
				}
			}
		}
		
		// add this updated plan with the <vars> </vars> from building the concrete file
		for(int x = planContent.indexOf("<Plan>"); x < planContent.size(); x++){
			concretePlan.add(planContent.get(x));
		}
		
		return concretePlan;
	}
	
	/**
	 * Using the Map and the Parameterised plan, this builds a concrete plan, which is the Parameterised plan but with distinct values given.
	 * 
	 * @param file			The file location and name to save the concrete file to
	 * @param map			The map to get available object data from
	 * @param plan			The plan to convert to a concrete plan
	 * @param vars			A linked list of all the plan variables, and the map variables they have been assigned to
	 * @param planContent 
	 */
	public static LinkedList<String> buildXMLConcretePlan(HTN map, LinkedList<String> plan, LinkedList<String> vars, LinkedList<String> planContent){
		
		LinkedList<String> newVars = new LinkedList<String>();
		newVars.add("<Vars>");
		String currentWord = "";
		
		
		// building the <Vars> sections of the XML
		for(int k = 0; k < vars.size(); k = k + 2){
			for(int x = 0; x < plan.size(); x++){
				if(plan.get(x).equals("<Building>") | plan.get(x).equals("<Unit>") | plan.get(x).equals("<Door>") | plan.get(x).equals("<UnitMember>")){
					currentWord = plan.get(x).substring(1, plan.get(x).length()-1);
				}

				if(plan.get(x).equals(vars.get(k))){
					if(plan.get(x-1).equals("<Id>") & plan.get(x+1).equals("</Id>")){
						if(currentWord.equals("Unit")){
							newVars.addAll(unitToXMLList(map.getUnit(vars.get(k+1))));
						}
						else if(currentWord.equals("Building")){
							newVars.addAll(buildingToXMLList(map.getBuilding(vars.get(k+1))));
						}
					}
					else{
						System.out.println("Formatting error in XML Plan file, the id " + plan.get(x) + 
								" is not currounded with the correct <Id> </Id> tags");
					}
				}
			}
		}
		
		newVars.add("</Vars>");
		
		// all plan values with actual values
		LinkedList<String> concretePlanContent = applyVars(newVars, planContent, vars);
			
		
		return concretePlanContent;
		
		/* vars
		 * 	unit
		 * 		id ui -id
		 * 	-unit
		 * 
		 * -vars
		 * 
		 * rootgoal
		 * 	goalSeq
		 * 		goalAction
		 * 
		 * 			actionMoveTo
		 * 				next actionOpenDoor -next
		 * 			-actionMoveTo
		 * 
		 * 			actionOpenDoor
		 * 				next actionThrowGrenade -next
		 * 			-actionOpenDoor
		 * 
		 * 			actionThrowGrenade
		 *
		 * 			-actionThrowGrenade
		 * 
		 * 		-goalAction
		 * 	-goalSeq
		 * 
		 * 	goalSim
		 * 		goalAction
		 * 			// chain of actions
		 * 		-goalAction
		 * 			// chain of actions
		 * 		goalAction
		 * 
		 * 		-goalAction
		 * 
		 * 		goalSeq
		 * 
		 * 		-goalSeq
		 * 	-goalSim
		 *-rootgoal
		 */
		
		
	}
	
	/**
	 * This displays to the screen the variables that are available (specified by the map file) and the variables that have not yet
	 * been assigned (plan file) and allows the user to link the plan variables with the map variables
	 * 
	 * @param htn			The HTN where the available map variables are taken from to be shown on the screen
	 * @param variables		The variables given in the vars section of the plan (between "<Vars>" and "</Vars>")
	 * @return vars			A LinkedList of strings, odd indexes being the variables from the plan, even indexes being the map objects 
	 * 						assigned to them. eg (planUnit1, mapUnit1, planBuilding1, mapBuilding1)
	 */
	private static LinkedList<String> showVariables(HTN htn, LinkedList<String> variables){
		
		// the list to be returned
		LinkedList<String> vars = new LinkedList<String>();
		
		//list of buildings given in the variables list
		LinkedList<String> buildings = new LinkedList<String>();
		
		//list of units given in the variables list
		LinkedList<String> units = new LinkedList<String>();
		
		Scanner scanner = new Scanner(System.in);
		
		// split the given variables list into 2 lists, units and buildings
		for(int k = 0; k < variables.size(); k++){
			if(variables.get(k).equals("<Unit>")){
				// adding to units
				k++;
				if(variables.get(k).equals("<Id>")){
					k++;
					units.add(variables.get(k));
				}
			}
			else if(variables.get(k).equals("<Building>")){
				// adding to buildings
				k++;
				if(variables.get(k).equals("<Id>")){
					k++;
					buildings.add(variables.get(k));
				}
			}
		}
		
		// show all the unit ids given from the map file
		System.out.println("Available Units:" + '\n');
		for(int k = 0; k < htn.getUnits().size(); k++){
			System.out.println("Unit" + (k+1) + ' ' + htn.getUnit(k).getId());
		}
		System.out.println('\n');
		System.out.println("Available Buildings:" + '\n');
		// show all the building ids given from the map file
		for(int k = 0; k < htn.getBuildings().size(); k++){
			System.out.println("Building" + (k+1) + ' ' + htn.getBuilding(k).getId());
			
		}
		System.out.println('\n');
		
		//loop through all the variables from the parameterised plan
		System.out.println("Variables to be set");
		System.out.println("Set each given variable with the objects id from the list of available objects above.");
		System.out.println("Units to be set: " + '\n');
		System.out.println(units.toString());
		for(int k = 0; k < units.size(); k++){
			  System.out.println(units.get(k) + " = ");
			  String input = scanner.next();
			  
			  // adds the 2 ids to a linked list
			  vars.add(units.get(k));
			  vars.add(input);
		}
		
		System.out.println("Buildings to be set: " + '\n');
		System.out.println(buildings.toString());
		for(int k = 0; k < buildings.size(); k++){
			  System.out.println(buildings.get(k) + " = " + "...");
			  String input = scanner.next();
			  
			  // adds the 2 ids to a linked list
			  vars.add(buildings.get(k));
			  vars.add(input);
		}
		
		// vars will be a linked list of the varaibles in the param and the values they are set to in the map
		//(paramval1, mapval1, paramval2, mapva2l2)
		return vars;
	}

	
	
	
	// Importing
	/**
	 * Builds a HTN from the given XML file
	 * 
	 * @param file				The XML file to be converted
	 * @return newMap			The HTN build from the XML file
	 */
 	public static HTN importXMLMap(String file){
		
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

	/**
	 * Starting from "<Unit>" until "</Unit>", builds a unit from the given XML tags and values
	 * 
	 * @return unit			The unit build from the given XML
	 */
	private static Unit importUnit(){
		
		String id = "";
		String x = "";
		String y = "";
		Unit unit = null;
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
				unitMembers.add(importUnitMember(unit));
			}
		}
		
		unit = new Unit((int)Float.parseFloat(x), (int)Float.parseFloat(y), id);
		unit.setUnitMembers(unitMembers);
		
		return unit;
	}
	
	/**
	 * Starting from "<UnitMember>" until "</UnitMember>", builds a unit member from the given XML tags and values
	 * 
	 * @return unitMember			The unit member build from the given XML
	 */
	private static UnitMember importUnitMember(Unit unit){
		
		String x = "";
		String y = "";
		String id = "";
		
		UnitMember unitMember;
		
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
		
		unitMember = new UnitMember((int)Float.parseFloat(x), (int)Float.parseFloat(y), unit, id);
		
		return unitMember;
	}
	
	/**
	 * Starting from "<Building>" until "</Building>", builds a building from the given XML tags and values
	 * 
	 * @return building			The building build from the given XML
	 */
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

	/**
	 * Starting from "<Door>" until "</Door>", builds a door from the given XML tags and values
	 * 
	 * @return door			The door build from the given XML
	 */
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
	/**
	 * Exports the given HTN to a file in XML format
	 * 
	 * @param map			The htn to be converted to XML
	 * @param fileLoc		The file location
	 * @param fileName		The name of the file
	 */
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
	
	/**
	 * Takes a value and a tag and puts them in XML format
	 * @param tag			The XML tag
	 * @param value			The value to be placed within the tag
	 * @return _tag 		the XML representation of the tag and value
	 */
	private static String XMLTag(String tag, String value){
		
		String _tag;
			
		_tag = '<' + tag + '>' + value + "</" + tag + '>';
		
		return _tag;
	}
	
	/**
	 * Transforms a building into XML format
	 * @param building		The building to be transformed into XML format
	 * @return _tag 		the XML representation of the building
	 */
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
	
	private static LinkedList<String> buildingToXMLList(Building building){
		
		LinkedList<String> list = new LinkedList<String>();
		
		list.add("<Building>");
		list.add("<Id>");
		list.add(building.getId());
		list.add("</Id>");
		list.add("<X>");
		list.add(building.getX() + "");
		list.add("</X>");
		list.add("<Y>");
		list.add(building.getY() + "");
		list.add("</Y>");
		list.add("<Enemies>");
		list.add(building.enemiesInitial + "");
		list.add("</Enemies>");
		list.add("<Width>");
		list.add(building.getWidth() + "");
		list.add("</Width>");
		list.add("<Height>");
		list.add(building.getHeight() + "");
		list.add("</Height>");
		
		for(int k = 0; k < building.getDoors().size(); k++){
			list.addAll(list.size(), (doorToXMLList(building.getDoors().get(k))));
		}
		
		list.add("</Building>");
		
		return list;
	}
	
	/**
	 * Transforms a door into XML format
	 * @param door			The door to be transformed into XML format
	 * @return _tag 		the XML representation of the door
	 */
	private static String doorToXML(Door door){
		
		String _tag = System.lineSeparator() + '\t' + '\t' + "<Door>" + System.lineSeparator();
		
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Id", door.getId()) + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("X", door.getX() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Y", door.getY() + "") + System.lineSeparator();
		
		return _tag + '\t' + '\t' + "</Door>" + System.lineSeparator();
	}
	
	private static LinkedList<String> doorToXMLList(Door door){
		
		LinkedList<String> list = new LinkedList<String>();
		
		list.add("<Door>");
		list.add("<Id>");
		list.add(door.getId());
		list.add("</Id>");
		list.add("<X>");
		list.add(door.getX() + "");
		list.add("</X>");
		list.add("<Y>");
		list.add(door.getY() + "");
		list.add("</Y>");
		list.add("</Door>");
		
		return list;
	}
	
	/**
	 * Transforms a unit into XML format
	 * @param unit			The unit to be transformed into XML format
	 * @return _tag 		the XML representation of the unit
	 */
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
	
	private static LinkedList<String> unitToXMLList(Unit unit){
		
		LinkedList<String> list = new LinkedList<String>();
		
		list.add("<Unit>");
		list.add("<Id>");
		list.add(unit.getId());
		list.add("</Id>");
		list.add("<X>");
		list.add(unit.getX() + "");
		list.add("</X>");
		list.add("<Y>");
		list.add(unit.getY() + "");
		list.add("</Y>");
		list.add("<Grenades Used>");
		list.add(unit.getGrenadesUsed() + "");
		list.add("</Grenades Used>");
		
		if(unit.inBuilding() != null){
			list.add("<InBuildingId>");
			list.add(unit.inBuilding().getId());
			list.add("</InBuildingId>");
		}else{
			list.add("<InBuildingId>");
			list.add("NULL");
			list.add("</InBuildingId>");
		}
		
		for(int x = 0; x < unit.GetUnitMembers().size(); x++){
			list.addAll(list.size(), (unitMemberToXMLList(unit.GetUnitMembers().get(x))));
		}
		
		list.add("</Unit>");
		
		// loop through unit members
		return list;
	}
	
	/**
	 * Transforms a unit member into XML format
	 * @param unitMember	The unit member to be transformed into XML format
	 * @return _tag 		the XML representation of the unit member
	 */
 	private static String unitMemberToXML(UnitMember unitMember){
		
		String _tag = System.lineSeparator() + '\t' + '\t' + "<UnitMember>" + System.lineSeparator();
		
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Id", unitMember.getId()) + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("X", unitMember.getX() + "") + System.lineSeparator();
		_tag = _tag + '\t' + '\t' + '\t' + XMLTag("Y", unitMember.getY() + "") + System.lineSeparator();
		
		return _tag + '\t' + '\t' + "</UnitMember>" + System.lineSeparator();
	}

 	private static LinkedList<String> unitMemberToXMLList(UnitMember unitMember){
 		LinkedList<String> list = new LinkedList<String>();
 		
 		list.add("<UnitMember>");
 		list.add("<Id>");
 		list.add(unitMember.getId());
 		list.add("</Id>");
 		list.add("<X>");
 		list.add(unitMember.getX() + "");
 		list.add("</X>");
 		list.add("<Y>");
 		list.add(unitMember.getY() + "");
 		list.add("</Y>");
 		list.add("</UnitMember>");
 		
 		return list;
 	}
 	
}
