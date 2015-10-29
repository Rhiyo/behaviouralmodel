/**
 * 
 */
package behaviouralmodel;

import java.util.Scanner;

/**
 * @author rhiyo
 * Plays the HTN simulation
 */
public class SimPlayer {

	private HTN htn;
	
	public SimPlayer(HTN htn){
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
		    //Commands usable whilst playing the simulation
			    if(line[0].equals("") || line[0].equals("next")) {
			    	htn.update(1);
			    	printMap();
			    	System.out.println("Type 'help' for list of play commands.");
			    }
			    
			    if(line[0].equals("stop")) {
			    	htn.reset();
			    	return;
			    }
			    
			    if(line[0].equals("reset")) {
			    	htn.reset();
			    	System.out.println("Simulation reset.");
			    }
		}
	}
	
	/**
	 * Draws map of grid
	 */
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
	 * Prints the amount of empty space supplied
	 * @param {int} num of empty space to print
	 */
	private static void printSpace(int qty){
		if(qty > 0)
		for(int i=0;i<qty;i++)
			System.out.print(" ");
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
}
