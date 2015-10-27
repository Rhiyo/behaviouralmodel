package Interpreter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
	
	/*
	 * Running the Interpreter.
	 * use begin() to launch it, then end() to finish it and print the execution trace
	 */
	/**
	 * Begins the Interpreter
	 * 
	 * This takes the user through the Interpreter pipeline
	 * 
	 * read map file -> build htn from map file -> read plan file 
	 * -> prompt user to assign map variables to plan -> build concrete plan from this data 
	 * -> build the behaviour tree for the HTN from the concrete file -> run the HTN simulation.
	 * 
	 * @throws IOException
	 */
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
			
			// FOR TESTING
			exportXML(htn, "C:/ICT XML Files/TEST_EXPORTED_MAP");
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
				
				System.out.println("Building concrete plan...");
				// build the concrete plan and save it to 'planContent'
				LinkedList<String> concretePlanContent = buildXMLConcretePlan(htn, vars, varValues, planContent);
				System.out.println("Concrete plan complete.");
				
				System.out.println("Saving concrete plan...");
				saveAsXMLFile(concretePlan, concretePlanContent);
				System.out.println("Concrete plan saved.");
				
				System.out.println(concretePlanContent.toString());
				
				// add the concrete plan to the htn
				htn = importConcretePlan(concretePlan, htn);
				
				System.out.println(htn.getRoot().toString());
				
			}
		}
		System.out.println("Running simulation..");
	}
	/**
	 * Ends the interpreter
	 * 
	 * This ends the interpreter and asks the user where to save the execution trace file
	 * 
	 * @param executionTrace		A list of events that happened in the simulation in chronological order
	 */
	public static void end(LinkedList<String> executionTrace){
		
		String planExecutionTrace;
		
		Scanner scanner = new Scanner(System.in);
		
		// shows the simulation has been completed
		System.out.println("Simulation complete");
		
		// write plan execution trace to file
		System.out.println("Please specify where to save plan execution trace file");
		planExecutionTrace = scanner.next();
		saveAsXMLFile(planExecutionTrace, executionTrace);
		System.out.println("Plan execution trace file saved");
	}
	
	
	
	/*
	 * Utility
	 */
	/**
	 * 
	 * @param file
	 * @return words
	 * @throws IOException
	 */
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
	/**
	 * 
	 * @param list
	 * @param start
	 * @param end
	 * @return
	 */
	private static LinkedList<String> cutLinkedList(LinkedList<String> list, int start, int end){
		
		LinkedList newList = new LinkedList<String>();
		
		for(int x = start; x <= end; x++){
			newList.add(list.get(x));
		}
		
		return newList;
		
	}

	
	
	/*
	 * Concrete file construction and saving
	 * concrete file pipeline
	 * User defines variables -> variables are applied to concrete file -> 
	 * concrete file is saved -> HTN is updates with concrete file content
	 */
	/**
	 * Applies the user selected variables to the plan variables (planUnit1 = mapUnit7)
	 * 
	 * This is only called from buildConcreteFile(). This read through the plan file 
	 * and swaps all occurrences of plan variables with their newly assigned map variables, 
	 * the result is the concretePlan.
	 * 
	 * @param newPlanVars			All the variables with their new values (between <Vars> and </Vars>)
	 * @param planContent			The content of the plan file in linkedList format
	 * @param vars					A list of plan variables with the corresponding assigned variables.
	 * 								Odd indexes is plan variables, even is map variables, 
	 * 								eg, (planUnit1, mapUnit7, planBuilding9, ...)
	 * 
	 * @return concretePlan			the new concreteFile, now with the new variables applied to it
	 */
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
	 * @param planContent 	Used as a parameter for "applyVars()"
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
	}
	/**
	 * This displays to the screen the variables that are available (specified by the map file) and the variables that have not yet
	 * been assigned (plan file) and allows the user to link the plan variables with the map variables
	 * 
	 * @param htn			The HTN where the available map variables are taken from to be shown on the screen
	 * @param variables		The variables given in the vars section of the plan (between "<Vars>" and "</Vars>")
	 * 
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
	
	
	
	/*
	 * File saving
	 */
	/**
	 * Saves as a XML file.
	 * 
	 * Takes in a linked list of XML tags, their values and end tags and saves
	 * them to the specified file
	 * 
	 * @param file				The location to save to
	 * @param fileContent		The content of the file to be saved
	 */
	private static void saveAsXMLFile(String file, LinkedList<String> fileContent) {
		
		String content = "";
		int depth = -1;
		
		for(int k = 0; k < fileContent.size(); k++){
			
			// hit starting tag, increase depth
			if(!(fileContent.get(k).startsWith("</")) & fileContent.get(k).startsWith("<")){
				depth++;
			}
			
			if(!fileContent.get(k).startsWith("<")){
				depth++;
				content = content + new String(new char[depth]).replace("\0", "\t") + fileContent.get(k) + System.lineSeparator();
				depth--;
			}else{
				content = content + new String(new char[depth]).replace("\0", "\t") + fileContent.get(k) + System.lineSeparator();
			}
			
			// hit ending tag, decrease depth
			if(fileContent.get(k).startsWith("</")){
				depth--;
			}
		}
		
		// save content to a file
		try{
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file + ".XML"));
			fileWriter.write(content);
			fileWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	
	
	/*
	 *  Importing
	 */
	
	// File Importing
	/**
	 * Imports a concrete file to the htn
	 * @param file
	 * @param htn
	 * @return
	 */
	private static HTN importConcretePlan(String file, HTN htn){
		
		// reset counter and xmlWords
		counter = 0;
		xmlWords = null;
		
		try {
			xmlWords = processXMLMap(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// read through file
		counter = xmlWords.indexOf("<Plan>");
		
		while(!xmlWords.get(counter).equals("</Plan>")){
			counter++;
			
			// this will add the root goal, and then recursion code will be ran
			if(xmlWords.get(counter).equals("<GoalSequential>")){
				htn.addGoal(importGoalSequential(htn));
			}
			else if(xmlWords.get(counter).equals("<GoalSimultaneous>")){
				htn.addGoal(importGoalSimultaneous(htn));
			}
			else if(xmlWords.get(counter).equals("<GoalPrimitive>")){
				htn.addGoal(importGoalPrimitive(htn));
			}
		}
		
		return htn;
	}
	/**
	 * Builds a HTN from the given XML file
	 * 
	 * @param file				The XML file to be converted
	 * @return newMap			The HTN build from the XML file
	 */
  	public static HTN importXMLMap(String file) throws IOException{
		
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
	
  	// The importing methods below are only used within the file importing methods above
  	
  	// importing Goals
	/**
	 * Imports a GoalSequential
	 * 
	 * Starting from "<importGoalSequential>" until "</importGoalSequential>", 
	 * builds a GoalSequential from the given XML tags and values
	 * 
	 * @param htn				The HTN to reference for Buildings and Units received from the map file
	 * @return goal				The GoalSequential built from the given XML
	 */
	private static GoalSequential importGoalSequential(HTN htn){
		
		System.out.println("importGoalSequential");
		
		GoalSequential goal = new GoalSequential();
		
		while(!(xmlWords.get(counter).equals("</GoalSequential>"))){
			counter++;
				
			if(xmlWords.get(counter).equals("<GoalSequential>")){
				goal.addGoal(importGoalSequential(htn));
			}
			else if(xmlWords.get(counter).equals("<GoalSimultaneous>")){
				goal.addGoal(importGoalSimultaneous(htn));
			}
			else if(xmlWords.get(counter).equals("<GoalPrimitive>")){
				goal.addGoal(importGoalPrimitive(htn));
			}
		}
		return goal;
	}
	/**
	 * Imports a GoalSimultaneous
	 * 
	 * Starting from "<importGoalSimultaneous>" until "</importGoalSimultaneous>", 
	 * builds a GoalSimultaneous from the given XML tags and values
	 * 
	 * @param htn				The HTN to reference for Buildings and Units received from the map file
	 * @return goal				The GoalSimultaneous built from the given XML
	 */
	private static GoalSimultaneous importGoalSimultaneous(HTN htn){
		
		System.out.println("importGoalSimultaneous");
		
		GoalSimultaneous goal = new GoalSimultaneous();
		
		while(!(xmlWords.get(counter).equals("</GoalSequential>"))){
			counter++;
			
			if(xmlWords.get(counter).equals("<GoalSequential>")){
				goal.addGoal(importGoalSequential(htn));
			}
			else if(xmlWords.get(counter).equals("<GoalSimultaneous>")){
				goal.addGoal(importGoalSimultaneous(htn));
			}
			else if(xmlWords.get(counter).equals("<GoalPrimitive>")){
				goal.addGoal(importGoalPrimitive(htn));
			}
		}
		
		return goal;
	}
	/**
	 * Imports a GoalPrimitive
	 * 
	 * Starting from "<importGoalPrimitive>" until "</importGoalPrimitive>", 
	 * builds a GoalPrimitive from the given XML tags and values
	 * 
	 * @param htn				The HTN to reference for Buildings and Units received from the map file
	 * @return goal				The GoalPrimitive built from the given XML
	 */
	private static GoalPrimitive importGoalPrimitive(HTN htn){
		
		System.out.println("importGoalPrimitive");
		
		GoalPrimitive goal = null;
		
		LinkedList<Action> actions = new LinkedList<Action>();
		
		Action startAction;
		Unit orderedUnit;
		
		while(!(xmlWords.get(counter).equals("</GoalSequential>"))){
			counter++;
			
			if(xmlWords.get(counter).equals("<ActionEnterBuilding>")){
				actions.add(importActionEnterBuilding(htn));
			}
			else if(xmlWords.get(counter).equals("<ActionMove>")){
				actions.add(importActionMove(htn));

			}
			else if(xmlWords.get(counter).equals("<ActionOpenDoor>")){
				actions.add(importActionOpenDoor(htn));

			}
			else if(xmlWords.get(counter).equals("<ActionThrowGrenade>")){
				actions.add(importActionThrowGrenade(htn));
			}
		}
		
		goal = new GoalPrimitive(actions.get(0));
		
		return goal;
	}
	
	// importing Actions
	/**
	 * Imports an ActionEnterBuilding
	 * 
	 * Starting from "<importActionEnterBuilding>" until "</importActionEnterBuilding>", 
	 * builds an ActionEnterBuilding from the given XML tags and values
	 * 
	 * @param htn				The HTN to reference for Buildings and Units received from the map file
	 * @return action			The importActionEnterBuilding built from the given XML
	 */
	private static ActionEnterBuilding importActionEnterBuilding(HTN htn){
		
		System.out.println("importActionEnterBuilding");
		
		String unitId = "";
		String buildingId = "";
		
		ActionEnterBuilding action;

		while(!(xmlWords.get(counter).equals("</ActionEnterBuilding>"))){
			counter++;
			
			if(xmlWords.get(counter).equals("<Unit>")){
				counter++;
				unitId = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Building>")){
				counter++;
				buildingId = xmlWords.get(counter);
				counter++;
			}
		}
		
		action = new ActionEnterBuilding(htn.getBuilding(buildingId), htn.getUnit(unitId), null);
		
		return action;
	}
	/**
	 * Imports an ActionMove
	 * 
	 * Starting from "<importActionMove>" until "</importActionMove>", 
	 * builds an ActionMove from the given XML tags and values
	 * 
	 * @param htn				The HTN to reference for Buildings and Units received from the map file
	 * @return action			The importActionMove built from the given XML
	 */
	private static ActionMove importActionMove(HTN htn){
		
		System.out.println("importActionMove");
		
		String unitId = "";
		String goalX = "";
		String goalY = "";
		String next = "";
		
		ActionMove action;
		

		while(!(xmlWords.get(counter).equals("</ActionMove>"))){
			counter++;
			
			if(xmlWords.get(counter).equals("<Unit>")){
				counter++;
				unitId = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Goal>")){
				counter++;
				if(xmlWords.get(counter).equals("<X>")){
					counter++;
					goalX = xmlWords.get(counter);
					counter++;
				}
				counter++;
				if(xmlWords.get(counter).equals("<Y>")){
					counter++;
					goalY = xmlWords.get(counter);
					counter++;
				}
			}
		}
		
		action = new ActionMove(htn.getUnit(unitId), new Vector2((int)Float.parseFloat(goalX), (int)Float.parseFloat(goalY)), null);
		
		return action;
	}
	/**
	 * Imports an ActionOpenDoor
	 * 
	 * Starting from "<importActionOpenDoor>" until "</importActionOpenDoor>", 
	 * builds an ActionOpenDoor from the given XML tags and values
	 * 
	 * @param htn				The HTN to reference for Buildings and Units received from the map file
	 * @return action			The importActionOpenDoor built from the given XML
	 */
	private static ActionOpenDoor importActionOpenDoor(HTN htn){
		
		System.out.println("importActionOpenDoor");
			
		String unitId = "";
		String doorId = "";
		String actionIfEnemiesFound = "";
		
		Action enemiesFound = null;
		
		ActionOpenDoor action;
		

		while(!(xmlWords.get(counter).equals("</ActionOpenDoor>"))){
			counter++;
			
			if(xmlWords.get(counter).equals("<Unit>")){
				counter++;
				unitId = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Door>")){
				counter++;
				doorId = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<EnemiesFound>")){
				counter++;
				actionIfEnemiesFound = xmlWords.get(counter);
				
				// add the action to be perform if enemies are found 
				if(actionIfEnemiesFound.equals("<ActionEnterBuilding>")){
					enemiesFound = importActionEnterBuilding(htn);
				}
				else if(actionIfEnemiesFound.equals("<ActionMove>")){
					enemiesFound = importActionMove(htn);
				}
				else if(actionIfEnemiesFound.equals("<ActionOpenDoor>")){
					enemiesFound = importActionOpenDoor(htn);
				}
				else if(actionIfEnemiesFound.equals("<ActionThrowGrenade>")){
					enemiesFound = importActionThrowGrenade(htn);
				}
			}
		}
		
		// find the door
		Door door = null;
		
		for(int k = 0; k < htn.getBuildings().size(); k++){
			if(htn.getBuildings().get(k).getDoor(doorId) != null){
				door = htn.getBuildings().get(k).getDoor(doorId);
				break;
			}
		}
		
		action = new ActionOpenDoor(door, htn.getUnit(unitId), enemiesFound, null);
		
		return action;
	}
	/**
	 * Imports an ActionThrowGrenade
	 * 
	 * Starting from "<ActionThrowGrenade>" until "</ActionThrowGrenade>", 
	 * builds an ActionThrowGrenade from the given XML tags and values
	 * 
	 * @param htn				The HTN to reference for Buildings and Units received from the map file
	 * @return action			The ActionThrowGrenade built from the given XML
	 */
	private static ActionThrowGrenade importActionThrowGrenade(HTN htn){
			
		System.out.println("importActionThrowGrenade");
		
		String unitId = "";
		String buildingId = "";
		
		ActionThrowGrenade action;

		while(!(xmlWords.get(counter).equals("</ActionThrowGrenade>"))){
			counter++;
			
			if(xmlWords.get(counter).equals("<Unit>")){
				counter++;
				unitId = xmlWords.get(counter);
				counter++;
			}
			else if(xmlWords.get(counter).equals("<Building>")){
				counter++;
				buildingId = xmlWords.get(counter);
				counter++;
			}
		}
		
		action = new ActionThrowGrenade(htn.getBuilding(buildingId), htn.getUnit(unitId), null);
		
		return action;
	}
	
	// Importing map objects
  	/**
  	 * Import a Unit
	 * Starting from "<Unit>" until "</Unit>", builds a unit from the given XML tags and values
	 * 
	 * @return unit			The unit built from the given XML
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
	 * Import a Unit Member
	 * Starting from "<UnitMember>" until "</UnitMember>", builds a unit member from the given XML tags and values
	 * 
	 * @param unit					The unit this unit member belongs to
	 * @return unitMember			The unit member built from the given XML
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
	 * Import a Building
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
	 * Import a Door
	 * Starting from "<Door>" until "</Door>", builds a door from the given XML tags and values
	 * 
	 * @return door			The door built from the given XML
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

	
	
	/*
	 *  Exporting
	 */
	
	// File exporting
	/**
	 * Exports the given HTN to a file in XML format
	 * 
	 * @param map			The htn to be converted to XML
	 * @param fileLoc		The file location
	 * @param fileName		The name of the file
	 */
 	public static void exportXML(HTN htn, String file){
		 
 		// List version
 		LinkedList<String> list = new LinkedList<String>();
 		  
 		list.add("<Map>");
 		list.add("<Width>");
 		list.add(htn.gridWidth + "");
 		list.add("</Width>");
 		list.add("<Height>");
 		list.add(htn.gridHeight + "");
 		list.add("</Height>");
 		  
 		for(int x = 0; x < htn.getBuildings().size(); x++){
			list.addAll(buildingToXMLList(htn.getBuilding(x)));
		}
				
		//Write all the Squads to the XML
		for(int x = 0; x < htn.getUnits().size(); x++){
				list.addAll(unitToXMLList(htn.getUnit(x)));
		}
 		  
 		list.add("</Map>");
 		
 		saveAsXMLFile(file, list);	
	}
	
 	// Exporting map objects
 	/**
	 * Transforms a building into XML format (via list)
	 * @param building		The building to be transformed into XML format
	 * @return list 		the XML representation of the building in a linkedList
	 */
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
	 * Transforms a door into XML format (via list)
	 * @param door			The door to be transformed into XML format
	 * @return list 		the XML representation of the door in a linkedList
	 */
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
	 * Transforms a unit into XML format (via list)
	 * @param unit			The unit to be transformed into XML format
	 * @return list 		the XML representation of the unit in a linkedList
	 */
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
	 * Transforms a unit member into XML format (via list)
	 * @param unitMember	The unit member to be transformed into XML format
	 * @return list 		the XML representation of the unit member in a linkedList
	 */
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
