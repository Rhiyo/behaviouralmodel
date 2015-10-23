package behaviouralmodel;

import java.util.LinkedList;
import java.util.Scanner;

import Interpreter.Interpreter;

public class PlanEditor {
	
	private HTN htn;
	private int autoInt = 0;
	private Action currentAction;
	
	private LinkedList<Action> actions = new LinkedList<Action>();
	
	private int GetAuto()
	{
		return autoInt++;
	}
	
	public PlanEditor(HTN htn)
	{
		this.htn = htn;
	}
	
	public void CommandPrompt(Scanner scanner) {
		
		System.out.println("Please pick an option:");
		System.out.println("(N)ew - (O)pen - (B)ack");
		
		
		String startPrompt = scanner.nextLine().toLowerCase();
		
		while(!(startPrompt.equals("n") || startPrompt.equals("o") || startPrompt.equals("b")))
		{
			System.out.println("Incorrect, please pick a valid option");
			System.out.println("(N)ew - (O)pen - (B)ack");
			
			startPrompt = scanner.nextLine().toLowerCase();
		}
				
		//Return out of this.
		if(startPrompt.equals("b"))
			return;
		
		if(startPrompt.equals("o"))
		{
			System.out.println("Open is not implemented.");
			return;
		}
		
		while(scanner.hasNextLine()) {
		    String[] line = scanner.nextLine().toLowerCase().split("\\s+");
		    
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
		    
		    //Actions
		    if(line[0].equals("actionmove"))
		    {
		    	ActionMove action = new ActionMove();
		    	
		    	ProcessAction(action);
		    }
		    
		    if(line[0].equals("actionopendoor"))
		    {
		    	ActionOpenDoor action = new ActionOpenDoor();
		    	
		    	ProcessAction(action);
		    }
		    
		    if(line[0].equals("actionenterbuilding"))
		    {
		    	ActionEnterBuilding action = new ActionEnterBuilding();
		    	
		    	ProcessAction(action);
		    }
		    
		    if(line[0].equals("actionthrowgrenade"))
		    {
		    	ActionThrowGrenade action = new ActionThrowGrenade();
		    	
		    	ProcessAction(action);
		    }
		    
		    if(line[0].equals("actionset"))
		    {
		    	if(currentAction == null) return;
		    	Action a = null;
		    	
		    	if(line.length == 2)
		    	{
		    		a = GetActionFromID(line[1]);
		    	}
		    	else
		    	{
		    		currentAction.next = null;
		    	}
		    	
		    	if(a == null)
		    		System.out.println("No action found!");
		    	else
		    	{
		    		currentAction.next = a;
		    	}
		    }
		    
		    if(line[0].equals("actiongo"))
		    {
		    	Action a = null;
		    	
		    	if(line.length == 2)
		    	{
		    		a = GetActionFromID(line[1]);
		    	}
		    	
		    	if(a == null)
		    		System.out.println("No action found!");
		    	
		    	currentAction = a;
		    }
		    
		    if(line[0].equals("actionnone"))
		    {
		    	currentAction = null;
		    }
		    
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
		    
		    //Print htn structure
		    if(line[0].equals("htn")) {
		    	htn.getRoot().printStructure(0, htn.getCurrentWorkingGoal());
		    	System.out.println();
		    }
		    
		    //Print help commands
		    if(line[0].equals("help")) {
		        //printHelp();
		    }
		}
	}
	
	private void ProcessAction(Action action)
	{
		action.SetID("Action" + GetAuto());
    	
    	if(currentAction != null)
    	{
    		Action actionToSet = currentAction;
    		
    		while(actionToSet.next != null)
    		{
    			actionToSet = actionToSet.next;
    		}
    		
    		actionToSet.next = action;
    	}
    	else
    	{
    		GoalPrimitive goal = new GoalPrimitive(action);
    		
    		htn.getCurrentWorkingGoal().addGoal(goal);
    	}
    	
    	actions.add(action);
	}
	
	private Action GetActionFromID(String id)
	{
		for(int i  = 0; i < actions.size(); i++)
		{
			if(actions.get(i).GetID().equals(id))
			{
				return actions.get(i);
			}
		}
		return null;
	}
}
