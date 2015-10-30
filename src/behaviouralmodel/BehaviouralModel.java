package behaviouralmodel;

import java.util.LinkedList;
import java.util.Scanner;

import Interpreter.Interpreter;


public class BehaviouralModel {

	static HTN htn;
	
	private LinkedList<String> executionTrace = new LinkedList<String>();
	
	static Scanner scanner;
	
	static boolean playing;
	
	public static void main(String[] args) {

		htn = new HTN();
		   
		scanner = new Scanner(System.in);
		
		System.out.println("Beginning HTN simmulation system:" + '\n');
		System.out.println("HTN Construction and System pipeline:");
		System.out.println("MapBuilder -> PlanEditor -> Concrete file Creation -> Run Simmulation -> Save Execution trace" + '\n');
		
		System.out.println("Step 1/5");
		System.out.println("MapBuilder" + '\n');
		
		MapBuilder mb = new MapBuilder(htn);
		mb.commandPrompt(scanner);
		
		System.out.println("");
		System.out.println("Step 2/5");
		System.out.println("PlanEditor" + '\n');
		
		// concrete file construction happens in here
		PlanEditor pe = new PlanEditor(htn);
		pe.CommandPrompt(scanner);
		
		System.out.println("Step 4/5");
		System.out.println("Running Simmulation" + '\n');
		playing = true;
		
		
		while(scanner.hasNextLine()) {
		    String[] line = scanner.nextLine().toLowerCase().split("\\s+");
		    
		    //Commands usable whilst playing the simulation
		    if(playing){
			    if(line[0].equals("") || line[0].equals("next")) {
			    	htn.update(1);
			    	//TODO ADD TO THE EXECUTION TRACE HERE
			    	printMap();
			    	System.out.println("Type 'help' for list of play commands.");
			    }
			    
			    if(line[0].equals("stop")) {
			    	playing = false;
			    	htn.reset();
			    	System.out.println("Type 'help' for list of commands.");
			    }
			    
			    if(line[0].equals("reset")) {
			    	htn.reset();
			    	System.out.println("Simulation reset.");
			    }
		    }

		    //Print help commands
		    if(line[0].equals("help")) {
		        printHelp();
		    }
		    
		    //Print the map
		    if(line[0].equals("map")) {
		        printMap();
		    }
		    
		    //Print unit list
		    if(line[0].equals("unitlist") || line[0].equals("units") ) {
		    	for(Unit unit : htn.getUnits())
		    		System.out.println(unit.toString());
		    }
		    
		    //Print htn structure
		    if(line[0].equals("htn")) {
		    	htn.getRoot().printStructure(0, htn.getCurrentWorkingGoal());
		    	System.out.println();
		    }

		    //Exit program
		    if(line[0].equals("exit")) {
		        break;
		    }
		    
		    //Print and reset any errors
		    if(htn.errorMsg!=""){
		    	System.out.println(htn.errorMsg);
		    	htn.errorMsg = "";
		    }
		    
		    // IF THERE ARE NO GOALS LEFT
		    	// PROMPT FOR USER TO ENTER A LOCATION TO SAVE TO
		    	// CALL saveAsXMLFile(filename, executionTrace)
		}
	}
	
	/**
	 * Prints a list of commands
	 */
	private static void printHelp(){
		System.out.println("Commands:");
		System.out.println("map | Draws map to console.");
		System.out.println("unit 'x' 'y' 'unitid' | Creates unit with specified ID.");
		System.out.println("building 'x' 'y' 'width' 'height' 'enemies y/n' 'id' | Creates building with specs.");
		System.out.println("goalsequential 'id' | creates sequential goal with ID");
		System.out.println("goalsimiltaneous 'id' | creates similtaneous goal with ID");
		System.out.println("start | starts simulation");
		System.out.println("reset | resets simulation");
		System.out.println("stop | stops simulation");
	}

	/**
	 * Draws map of grid
	 */
	private static void printMap(){
		//Get the character length of the largest int
		int widthLength = numPlaces(htn.gridWidth);
		int heightLength = numPlaces(htn.gridHeight);
		
		int colWidth = widthLength;
		if(widthLength < heightLength)
			colWidth = heightLength;
		
		//Print top left empty space
		printSpace(colWidth+1);
		//Disply column index
		for(int x=0;x<htn.gridWidth;x++){
			printSpace(colWidth-numPlaces(x));
			System.out.print(x + " ");
		}
		
		System.out.println();
		for(int y=0;y<htn.gridHeight;y++){
			//Display row index
			printSpace(colWidth-numPlaces(y));
			System.out.print(y + " ");			
			for(int x=0;x<htn.gridWidth;x++){
				printSpace(colWidth-1);
				String cell = ". ";
				Entity ent = htn.positionCheck(x, y);
				if(ent instanceof Unit)
					cell = "U ";
				else if(ent instanceof Building)
					cell = "B ";
				else if(ent instanceof Door){
					cell = "D ";
					if(((Door)ent).isOpened())
						cell = "O ";
				}
					
				System.out.print(cell);
			}	
			System.out.println();
		}
	}
	
	/**
	 * Gets the length of a number for columns
	 * @param {int} num to calculate length of
	 */
	private static int numPlaces (int x) {
		if(x<10)
			  return 1;
			if(x<100)
			  return 2;
			if(x<1000)
			  return 3;
		return -1;
	}
	
	/**
	 * Prints the amount of empty space supplied
	 * @param {int} num of empty space to print
	 */
	private static void printSpace(int qty){
		if(qty > 0)
		for(int i=0;i<qty;i++)
			System.out.print(" ");
	}

}
