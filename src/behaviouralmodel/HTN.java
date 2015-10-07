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
	
	private GoalRecursive currentWorkingGoal;
	
	public String errorMsg = "";
	
	public HTN()
	{
		goals = new LinkedList<Goal>();
		units = new LinkedList<Unit>();
		buildings = new LinkedList<Building>();
		AStar.htn = this;
		
		rootGoal = new GoalSequential();
		rootGoal.setID("Root");
		goals.add(rootGoal);
		currentWorkingGoal = (GoalRecursive)rootGoal;
	}
	
	/**
	 * Updates all goals of HTN
	 * @param delta
	 */
	public void update(float delta)
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
	
	/**
	 * Resets the simulation.
	 */
	public void reset(){
		currentGoal = 0;
		rootGoal.reset();
	}
	
	/**
	 * Adds goal to current working goal
	 * @param goal
	 */
	public void addGoal(Goal goal)
	{
		currentWorkingGoal.addGoal(goal);
	}
	
	
	/**
	 * Add a unit object to unit list
	 * @param {Unit} unit object to add
	 */
	public void addUnit(Unit unit)
	{
		units.add(unit);
	}
	
	/**
	 * Add a unit object to unit list
	 * @param {int} x start position
	 * @param {int} y start position
	 * @param {id} id of unit
	 */
	public void addUnit(int x, int y, String id)
	{
		if(id.equals("")){
			errorMsg = "ID can't be nothing.";
			return;
		}
		
		if(!isIDFree(id)){
			errorMsg = "ID already in use.";
			return;
		}
		
		if(x < 1 || x >= gridWidth-1 || 
				y < 1 || y >= gridHeight-1){
			errorMsg = "Position out of grid space (Unit takes up 9 cells.)";
			return;
		}
		
		if(isCellFree(x-1, y-1)==false ||
				isCellFree(x+1, y+1)==false ||
				isCellFree(x+1, y-1)==false ||
				isCellFree(x-1, y+1)==false){
			errorMsg = "Something already in this position (Surrounding cells must be free.)";
			return;
		}
				
		units.add(new Unit(x, y, id));
	}
	
	/**
	 * Add building with entity
	 * @param building to add
	 */
	public void addBuilding(Building building)
	{
		buildings.add(building);
	}
	
	/**
	 * Adds a building with a door
	 * @param startx of building
	 * @param starty of building
	 * @param width of building
	 * @param height of building
	 * (retracted)@param doorRelX x of door relative to building
	 * (retracted)@param doorRelY y of door relative to building
	 * @return the added building - null if unsuccessful
	 */
	public Building addBuilding(int x, int y, int width, int height, String id)
	{
		if(x < 0 || x + width-1 >= gridWidth || 
				y < 0 || y + height-1 >= gridHeight){
			errorMsg = "Position out of grid space.";
			return null;
		}
		
		if(id.equals("")){
			errorMsg = "ID can't be nothing.";
			return null;
		}
		
		if(!isIDFree(id)){
			errorMsg = "ID already in use.";
			return null;
		}
		
		//Check if all spaces are empty
		for(int i = x;i<x+width;i++)
			for(int j = y;j<y+height;j++)
				if(positionCheck(i, j)!= null){
					errorMsg = positionCheck(i, j).getId() + " in the way.";
					return null;
				}
					
		/*	
		if(!(doorRelX >= 0 && 
				   (doorRelX > 0 &&
						   doorRelX < width-1 &&
				   (doorRelY == 0 ||
						   doorRelY == height-1))||
				   (doorRelY > 0 &&
						   doorRelY < height-1 &&
					(doorRelX== 0 ||
							doorRelX == width-1)))){
			errorMsg = "Door must be on edge of building and not in corner cell. Door position is relative to building start position.";
			return null;
		}*/
		
		Building building = new Building(x,y, width, height, id);
		//building.addDoor(doorRelX, doorRelY);
		buildings.add(building);
		return building;
	}	
	
	public Goal getGoal(int index)
	{
		if(index > -1 && index < goals.size())
			return goals.get(index);
		return null;
	}
	
	/**
	 * Find unit via id
	 * @param id
	 * @return found unit or null if not found
	 */
	public Unit getUnit(String id)
	{
		for(Unit unit : units)
			if(id.equals(unit.getId().toLowerCase()))
				return unit;
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
	
	/**
	 * Find building via ID
	 * @param id
	 * @return found unit or null if not found
	 */
	public Building getBuilding(String id)
	{
		for(Building building : buildings)
			if(id.equals(building.getId().toLowerCase()))
				return building;
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
		if(currentWorkingGoal.parent == null){
			errorMsg = "Already at root goal.";
			return;
		}
		currentWorkingGoal = currentWorkingGoal.parent;
	}
	
	/**
	 * Go to child goal
	 * @param (int) index of child to traverse to
	 */
	public void goChild(int index){
		if(index < 0 || index >= currentWorkingGoal.goals.size()){
			errorMsg = "Child goal not found.";
			return;
		}
		
		currentWorkingGoal = (GoalRecursive) currentWorkingGoal.goals.get(index);
	}
	
	/**
	 * Set workig goal to goal with ID
	 * @param {String} ID of goal to traverse to
	 */
	public void go(String id){
		GoalRecursive foundGoal = ((GoalRecursive) rootGoal).findID(id);
		
		if(foundGoal == null){
			errorMsg = "Goal not found.";
			return;
		}

		currentWorkingGoal = foundGoal;
	}
	
	/**
	 * Finds what entity is at supplied cell
	 * @param x of cell
	 * @param y of cell
	 * @return entity found
	 */
	public Entity positionCheck(int x, int y){
	
		//Check for unit
		for(Unit unit : units){
			for(UnitMember unitMember : unit.GetUnitMembers()){
				if(x == unit.getX() + unitMember.getX() &&
					y == unit.getY() + unitMember.getY())
					return unit;
			}
		}

		//Check for building and doors
		for(Building building : buildings){
			for(Door door : building.getDoors()){
				if(x == building.getX()+door.getX() && y == building.getY()+door.getY())
					return door;
			}
			if(x >= building.getX() && x < building.getX()+building.getWidth()
					&& y >= building.getY()&& y < building.getY()+building.getHeight())
				return building;
		}
		
		return null;
	}
	
	public boolean isCellFree(int x, int y){
		
		//Check out of bounds
		if(x < 0 || x >= gridWidth || y < 0 || y >= gridHeight){
			return false;
		}
		
		if(positionCheck(x,y)!=null)
			return false;
		
		return true;
	}
	
	/**
	 * Checks if ID is already in use.
	 * @param id to check
	 * @return if id is in use
	 */
	public boolean isIDFree(String id){
		for(Building building : buildings)
			if(id.equals(building.getId()))
				return false;
		for(Unit unit : units)
			if(id.equals(unit.getId()))
				return false;		
		
		return true;
	}
	
	//Setters
	public void setGrid(int width, int height){
		this.gridWidth = width;
		this.gridHeight = height;
	}
	
	/**
	 * Sets the current working goal
	 * @param goal
	 */
	public void setCurrentWorkingGoal(GoalRecursive goal){
		this.currentWorkingGoal = goal;
	}
	
	//Getters
	public LinkedList<Building> getBuildings(){
		return buildings;
	}
	
	/**
	 * Gets root of HTN
	 * @return root of HTN
	 */
	public Goal getRoot(){
		return rootGoal;
	}
	
	/**
	 * Gets current working goal
	 * @return goal being worked in
	 */
	public GoalRecursive getCurrentWorkingGoal(){
		return currentWorkingGoal;
	}
	
	public LinkedList<Unit> getUnits(){
		return units;
	}
}
