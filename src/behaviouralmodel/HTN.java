package behaviouralmodel;
import java.util.LinkedList;

public class HTN {
	
	int currentGoal = 0;
	int gridWidth = 0;
	int gridHeight = 0;
	
	private LinkedList<Goal> goals;
	
	private LinkedList<Unit> units;
	
	private LinkedList<Building> buildings;
	
	private Goal rootGoal;
	
	private Goal currentWorkingGoal;
	
	public HTN()
	{
		goals = new LinkedList<Goal>();
		units = new LinkedList<Unit>();
		buildings = new LinkedList<Building>();
		AStar.htn = this;
		
		rootGoal = new GoalSequential();
		rootGoal.setID("Root");
		currentWorkingGoal = rootGoal;
	}
	
	public void Update(float delta)
	{
		
		if(currentGoal > -1 && currentGoal < goals.size())
		{
			Goal goal = goals.get(currentGoal);
			goal.update(delta);
			if(goal.isCompleted())
				currentGoal++;
		}
		
		for(int i = 0; i < units.size(); i++)
		{
			units.get(i).Update(delta);
		}
	}
	
	public void addGoal(Goal goal)
	{
		goals.add(goal);
	}
	
	public void addUnit(Unit unit)
	{
		units.add(unit);
	}
	
	public void addBuilding(Building building)
	{
		buildings.add(building);
	}
	
	public Goal getGoal(int index)
	{
		if(index > -1 && index < goals.size())
			return goals.get(index);
		return null;
	}
	
	public Unit getUnit(int index)
	{
		if(index > -1 && index < units.size())
			return units.get(index);
		return null;
	}
	
	public Building getBuilding(int index)
	{
		if(index > -1 && index < buildings.size())
			return buildings.get(index);
		return null;
	}
	
	public void removeGoal(int index)
	{
		if(index > -1 && index < goals.size())
			goals.remove(index);
	}
	
	public void removeUnit(int index)
	{
		if(index > -1 && index < units.size())
			units.remove(index);
	}
	
	public void removeBuidling(int index)
	{
		if(index > -1 && index < buildings.size())
			buildings.remove(index);
	}
	
	//Checks to see if there is a buiding in point
	public boolean isPassable(int x, int y){
		if(x < 0 || x >= gridWidth || y < 0 || y >= gridHeight)
			return false;

		for(Building building : buildings){
			if(x >= building.getX() && x < building.getX()+building.getWidth()
					&& y >= building.getY()&& y < building.getY()+building.getHeight())
				return false;
		}
		return true;
	}
	
	//Traversing
	
	/**
	 * Set current goal to next sibling goal.
	 */
	public void next(){
		//Check parent of currentWorkingGoal
		//If no parent return
		//find index of currentWorkingGoal and +1
		//if index out of range, return
		//make currentWorkingGoal index
	}
	
	/**
	 * Set current goal to previous sibling goal.
	 */
	public void previous(){
		//Check parent of currentWorkingGoal
		//If no parent return
		//find index of currentWorkingGoal and -1
		//if index out of range, return
		//make currentWorkingGoal index
	}
	
	/**
	 * Go up the tree
	 */
	public void up(){
		//Check parent of currentWorkingGoal
		//If no parent return
		//set currentWorkingGoal to parent
	}
	
	/**
	 * Go to child goal
	 * @param (int) index of child to traverse to
	 */
	public void goChild(int index){
		//if index out of range return
		//set currentWorkingGoal to child goal with id
	}
	
	/**
	 * Go to goal with id
	 * @param {String} ID of goal to traverse to
	 */
	public void go(String id){
		//Check all goals for id
		//If no goal found return
		//set currentWorkingGoal to found goal
	}
	
	//Setters
	public void setGrid(int width, int height){
		this.gridWidth = width;
		this.gridHeight = height;
	}
	
	//Getters
	public LinkedList<Building> getBuildings(){
		return buildings;
	}
	
	public LinkedList<Unit> getUnits(){
		return units;
	}
}
