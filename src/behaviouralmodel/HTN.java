package behaviouralmodel;
import java.util.LinkedList;

public class HTN {
	
	int currentGoal = 0;
	int gridWidth = 0;
	int gridHeight = 0;
	int speed = 3;
	
	private LinkedList<Goal> goals;
	
	private LinkedList<Unit> units;
	
	private LinkedList<Building> buildings;
	
	public HTN()
	{
		goals = new LinkedList<Goal>();
		units = new LinkedList<Unit>();
		buildings = new LinkedList<Building>();
	}
	
	public void Update(float delta)
	{
		
		if(currentGoal > -1 && currentGoal < goals.size())
		{
			Goal goal = goals.get(currentGoal);
			goal.update(delta * speed);
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
		for(Building building : buildings){
			if(x >= building.getX() && x < building.getX()+building.getWidth()
					&& y >= building.getY()&& y < building.getY()+building.getHeight())
				return false;
		}
		return true;
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
