package behaviouralmodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class BehaviouralModel {

	static HTN htn;
	
	static Scanner scanner;
	
	static boolean playing;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		htn = new HTN();
		   
		scanner = new Scanner(System.in);
		
		setMap();
		
		while(scanner.hasNextLine()) {
		    String[] line = scanner.nextLine().toLowerCase().split("\\s+");
		    
		    //Commands usable whilst playing the simulation
		    if(playing){
			    if(line[0].equals("") || line[0].equals("next")) {
			    	htn.update(1);
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
		    
		    //Commands usable whilst creating or editing scenario
		    if(!playing){
			    if(line[0].equals("")) {
			    	System.out.println("Type 'help' for list of commands.");
			    }    	
			    
			    /*
			     * Adding entities
			     */
			    
			    //Add a unit
			    if(line[0].equals("unit")) {
			        if(line.length != 4){
			        	System.out.println("Couldn't interpret command.");
			        	continue;
			        }
			        
			        try{
			        	htn.addUnit(Integer.parseInt(line[1]), Integer.parseInt(line[2]), line[3]);
			        }catch(Exception e){
			        	System.out.println("Couldn't interpret command.");
			        	continue;
			        }
			        
			        if(htn.errorMsg==""){
			        	System.out.println("Unit '" + line[3] + "' successfully added.");
			        }
			    }
			    
			    //Add a building
			    if(line[0].equals("building")) {
			        if(line.length != 6){
			        	System.out.println("Couldn't interpret command.");
			        	continue;
			        }
			        
			        int bx = 0;
			        int by = 0;
			        int width = 0;
			        int height = 0;
			        String id = "";
			        try{
				        bx = Integer.parseInt(line[1]);
				        by = Integer.parseInt(line[2]);
				        width = Integer.parseInt(line[3]);
				        height = Integer.parseInt(line[4]);
				        id = line[5];
			        }catch(Exception e){
			        	System.out.println("Couldn't interpret command.");
			        	continue;
			        }
			        
			        Building newBuilding = htn.addBuilding(bx, by, width, height, id);
			        
			        if(htn.errorMsg != ""){
			        	System.out.println(htn.errorMsg);
			        	htn.errorMsg = "";
				        continue;
			        }
			        
			        int doorX = -1;
			        int doorY = -1;
			        
			        printBuilding(width,height);
			        System.out.println("Place door on building. O = Door can be placed there. 'cancel' to cancel adding building.");
			        System.out.print("X:");
			        while(scanner.hasNext()){
			        	String commandline = scanner.next();
			        	int enteredInt = 0;
			        	
			        	if(commandline.equals("cancel"))
			        		break;
			        	
			        	//Make sure it's int
			        	try{
			        		enteredInt = Integer.parseInt(commandline);
			        	}catch(Exception e){
			        		System.out.println("Integer only");
			        		continue;
			        	}
			        	
			        	if(enteredInt < 0){
			        		printBuilding(width,height);
					        doorX=-1;
					        doorY=-1;
					        System.out.println("Place door on building. O = Door can be placed there. 'cancel' to cancel adding building.");
				        	System.out.println("Must be 0 or higher.");
					        System.out.print("X:");
			        	}
			        	//Entering X
				        if(doorX==-1){
				        	doorX=enteredInt;
				        	//System.out.println();
				        	System.out.print("Y:");
				        	continue;
				        }
				        
				        //If have X and Y, check if in bounds
				        doorY=enteredInt;
						if(!(doorX >= 0 && 
								   (doorX > 0 &&
										   doorX < width-1 &&
								   (doorY == 0 ||
										   doorY == height-1))||
								   (doorY > 0 &&
										   doorY < height-1 &&
									(doorX== 0 ||
											doorX == width-1)))){
					        printBuilding(width,height);
					        doorX=-1;
					        doorY=-1;
					        System.out.println("Place door on building. O = Door can be placed there. 'cancel' to cancel adding building.");
				        	System.out.println("Door location invalid.");
					        System.out.print("X:");
					        continue;
						}
						
						break;
			        }
			        
			        newBuilding.addDoor(doorX, doorY);
			        
			        if(htn.errorMsg==""){
			        	System.out.println("Building '" + line[5] + "' successfully added with door " + newBuilding.getDoors().get(0).getId() + ",");
			        }
			    }
			    
			    /*
			     * Adding goals
			     */
			    
			    //Add sequential goal
			    if(line[0].equals("goalsequential")) {
			        if(line.length != 2){
			        	System.out.println("Couldn't interpret command.");
			        	continue;
			        }
			        
			        GoalSequential goal = new GoalSequential();
			        goal.setID(line[1]);
			        goal.parent = htn.getCurrentWorkingGoal();
			        htn.getCurrentWorkingGoal().addGoal(goal);
			        htn.setCurrentWorkingGoal(goal);
			        System.out.println("Added sequential goal " + line[1] + ".");
			    }
			    
			    //Add simultaneous goal
			    if(line[0].equals("goalsimultaneous")) {
			        if(line.length != 2){
			        	System.out.println("Couldn't interpret command.");
			        	continue;
			        }
			        
			        GoalSimultaneous goal = new GoalSimultaneous();
			        goal.setID(line[1]);
			        goal.parent = htn.getCurrentWorkingGoal();
			        htn.getCurrentWorkingGoal().addGoal(goal);
			        htn.setCurrentWorkingGoal(goal);
			        System.out.println("Added simultaneous goal " + line[1] + ".");
			    }
			    
			    //Add move goal
			    if(line[0].equals("goalmove")) {
			        if(line.length != 5){
			        	System.out.println("Couldn't interpret command.");
			        	continue;
			        }
			        
			        Unit unit = htn.getUnit(line[1]);

			        if(unit == null){
			        	System.out.println("Couldn't find unit.");
			        	continue;
			        }
			        
			        int x;
			        int y;
			        
			        try{
			        	x = Integer.parseInt(line[2]);
			        	y = Integer.parseInt(line[3]);
			        	if(!htn.isPassable(x, y)){
				        	System.out.println("Position blocked.");
				        	continue;
			        	}
			        }catch(Exception e){
			        	System.out.println("Couldn't interpret command.");
			        	continue;
			        }
			        
			        PrimitiveMove goal = new PrimitiveMove(unit, x, y);
			        goal.setID(line[4]);
			        goal.parent = htn.getCurrentWorkingGoal();
			        htn.addGoal(goal);
			        System.out.println("Added move goal " + line[4] + ".");
			    }
			    
			    /*
			     * Traversing the HTN
			     */
			    
			    //Go to goal of ID
			    if(line[0].equals("go")) {
			        if(line.length != 2){
			        	System.out.println("Couldn't interpret command.");
			        	continue;
			        }
			        
			        htn.go(line[1]);
			        
			        if(htn.errorMsg==""){
			        	System.out.println(line[1] + " set to current working goal.");
			        }
			    }
			    
			    //Go to child of index
			    if(line[0].equals("child")) {
			        if(line.length != 2){
			        	System.out.println("Couldn't interpret command.");
			        	continue;
			        }
			        
			        try{
			        	htn.goChild(Integer.parseInt(line[1]));
			        }catch(Exception e){
			        	System.out.println("Couldn't interpret command.");
			        	continue;	
			        }
			        
			        if(htn.errorMsg==""){
			        	System.out.println(line[1] + " set to current working goal.");
			        }
			    }
			    
			    //Go up the tree
			    if(line[0].equals("up")) {
			        htn.up();
			        
			        if(htn.errorMsg==""){
			        	System.out.println(htn.getCurrentWorkingGoal().getID() + " set to current working goal.");
			        }
			    }
			    
			    //Play simulation
			    if(line[0].equals("play") || line[0].equals("start")) {
			    	playing = true;
			    	htn.update(1);
			    	printMap();
			    	System.out.println("Type 'help' for list of play commands.");
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
		}
		
	}
	
	
	/**
	 * Prints a list of commands
	 */
	private static void printHelp(){
		System.out.println("Commands:");
		System.out.println("map | Draws map to console.");
	}
	
	/**
	 * Sets the width and height of the map
	 */
	private static void setMap(){
		int width;
		System.out.print("Grid Width: ");
		width = scanner.nextInt();
		System.out.print("Grid Height: ");
		htn.setGrid(width, scanner.nextInt());
		//setEntities();
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
				else if(ent instanceof Door)
					cell = "D ";
				System.out.print(cell);
			}	
			System.out.println();
		}
	}
	
	/**
	 * Prints a building with Os as free door space
	 * @param width of building
	 * @param height of building
	 */
	private static void printBuilding(int width, int height){
		//Get the character length of the largest int
		int widthLength = numPlaces(width);
		int heightLength = numPlaces(height);
		
		int colWidth = widthLength;
		if(widthLength < heightLength)
			colWidth = heightLength;
		System.out.println("test");
		//Print top left empty space
		printSpace(colWidth+1);
		//Disply column index
		for(int x=0;x<width;x++){
			printSpace(colWidth-numPlaces(x));
			System.out.print(x + " ");
		}
		
		System.out.println();
		for(int y=0;y<height;y++){
			//Display row index
			printSpace(colWidth-numPlaces(y));
			System.out.print(y + " ");	
			//Display spaces door is placeable as O rest as X
			for(int x=0;x<width;x++){
				printSpace(colWidth-1);
				String cell = "X ";
				if(x >= 0 && 
						   (x > 0 &&
								   x < width-1 &&
						   (y == 0 ||
								   y == height-1))||
						   (y > 0 &&
								   y < height-1 &&
							(x== 0 ||
									x == width-1))){
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
