package behaviouralmodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class BehaviouralModel {

	static HTN htn;
	
	static Scanner scanner;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		htn = new HTN();
		   
		scanner = new Scanner(System.in);
		
		setMap();
		
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
		setEntities();
	}
	
	/**
	 * Placing units and buildings
	 */
	private static void setEntities(){
		drawMap();
	}
	
	/**
	 * Draws map of grid
	 */
	private static void drawMap(){
		for(int x=0;x<htn.gridWidth;x++){
			String line = "";
			for(int y=0;y<htn.gridHeight;y++){
				line+="0";
			}	
			System.out.println(line);
		}
	}

}
