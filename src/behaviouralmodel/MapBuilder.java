/**
 * 
 */
package behaviouralmodel;

import java.util.Scanner;

import Interpreter.Interpreter;

/**
 * @author rhiyo
 * Builds a new map with buildings.
 */
public class MapBuilder {
	/*
	 * How it plays out:
	 * Asks if you want to load a map or make a new one
	 * If new:
	 * Choose size of map
	 * If load:
	 * type location of file to load
	 * When map is in, you have these commands
	 * New Building - creates new building with startpos, x, and y, then goes to add entry point
	 * Back to Menu - takes back to menu
	 * Remove Building - building id
	 * Add Entry Point - Building ID and then choose location
	 */
	
	private HTN htn;
	
	public MapBuilder(HTN htn){
		this.htn = htn;
	}
	
	/**
	 * Edits a map
	 * @param scanner
	 */
	public void commandPrompt(Scanner scanner){
		
		//TODO reset the buildings list to none
		
		System.out.println("newmap - creates new map");
		System.out.println("loadmap filelocation - loads specified map");
		System.out.println("back - goes back to menu");
		while(scanner.hasNextLine()) {
			
		    String[] line = scanner.nextLine().toLowerCase().split("\\s+");
			
			//New Map
			if(line[0].equals("newmap")){
				System.out.print("Grid Width: ");
				while(scanner.hasNext()) {
						int width;
						width = scanner.nextInt();
						System.out.print("Grid Height: ");
						htn.setGrid(width, scanner.nextInt());
						//TODO make sure these are ints
						break;
				}
				break;
			}
			
			//Load Map
			if(line[0].equals("loadmap")){
				try{
					HTN map = Interpreter.importXMLMap(line[1]);
					htn.addBuildings(map.getBuildings());
					htn.setGrid(map.gridWidth, map.gridHeight);
				}catch(Exception e){
					System.out.println("Could not load map.");
					continue;
				}
				break;
				//TODO check if loading goes okay
			}
			
			//Back
			if(line[0].equals("back")){
				return;
			}
			
			System.out.println("Could not interpret command.");
		}
			
		//Go through commands
		System.out.println("newbuilding x y width height hasEnemies id - creates new map");
		System.out.println("removebuilding id - removes building");
		System.out.println("newentry buildingid - adds new entry point");
		System.out.println("map - drawsmap");
		System.out.println("save fileloc filename - saves map to xml file");
		System.out.println("back - goes back to menu");
		while(scanner.hasNextLine()) {
		    String[] line = scanner.nextLine().toLowerCase().split("\\s+");
		    
			//New building
			if(line[0].equals("newbuilding")){
				if(line.length!=7){
					System.out.println("Couldn't interpret command.");
					continue;
				}
		
				//TODO Check if map is out of bounds
				//TODO Check if something already exists in space
				int x = Integer.parseInt(line[1]); //TODO Check if int
				int y = Integer.parseInt(line[2]); //TODO Check if int
				int width = Integer.parseInt(line[3]); //TODO Check if int
				int height = Integer.parseInt(line[4]); //TODO Check if int
				boolean enemies = false;
				if(line[5].equals("y"))
					enemies = true; //TODO make sure yes/no
				String id = line[6];//TODO Check if ID already exists
				
				//Add the building
				newBuilding(x,y,width,height,enemies,id);
				
				//Print success
				System.out.println("Building " + id + " successfully added.");
				continue;
			}
			
			//New Map
			if(line[0].equals("removebuilding")){
				if(line.length!=2){
					System.out.println("Couldn't interpret command.");
					continue;
				}
				
				String id = line[1];//TODO Check if ID exists
				
				//Remove the building
				removeBuilding(id);
				
				//Print success
				System.out.println("Building " + id + " removed.");
				continue;
			}
			
			if(line[0].equals("newentry")){
				if(line.length!=2){
					System.out.println("Couldn't interpret command.");
					continue;
				}
				
				String id = line[1];//TODO check if building exists
				addEntryPoint(id, scanner);
				
				//Print success
				System.out.println("Entry point added to building '" + id + "'.");
				continue;
			}
			
			if(line[0].equals("map")){
				printMap();
				continue;
			}
			
			if(line[0].equals("save")){
				if(line.length!=2){
					System.out.println("Couldn't interpret command.");
					continue;
				}
				
				//String fileLoc = line[1];
				String fileName = line[1];
				
				Interpreter.exportXML(htn, fileName); //TODO check if saving goes okay
				
				//Print success
				System.out.println("Successfully saved.");
				continue;
			}
			
			if(line[0].equals("back")){
				return;
			}
			
			//No commands understood so print error
			System.out.println("Couldn't interpret command.");
		}
	}
	
	private void newBuilding(int x, int y, int width, int height, boolean enemies, String id){
		htn.addBuilding(x, y, width, height, enemies, id);
	}
	
	private void removeBuilding(String id){
		Building building = htn.getBuilding(id);
		htn.getBuildings().remove(building);
	}
	
	private void addEntryPoint(String id, Scanner scanner){
		int doorX = -1;
        int doorY = -1;
        
        Building b = htn.getBuilding(id);
        int width = (int)b.getWidth();
        int height = (int)b.getHeight();
        printDoorPlacementBuilding(width,height);
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
        		printDoorPlacementBuilding(width,height);
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
		        printDoorPlacementBuilding(width,height);
		        doorX=-1;
		        doorY=-1;
		        System.out.println("Place door on building. O = Door can be placed there. 'cancel' to cancel adding building.");
	        	System.out.println("Door location invalid.");
		        System.out.print("X:");
		        continue;
			}
			
			break;
        }
        
        b.addDoor(doorX, doorY);
		
	}
	
	/**
	 * Prints a planned building with Os as free door space
	 * @param width of planned building
	 * @param height of planned building
	 */
	private static void printDoorPlacementBuilding(int width, int height){
		//Get the character length of the largest int
		int widthLength = numPlaces(width);
		int heightLength = numPlaces(height);
		
		int colWidth = widthLength;
		if(widthLength < heightLength)
			colWidth = heightLength;
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
	
	//Prints the current map
	private void printMap(){
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
