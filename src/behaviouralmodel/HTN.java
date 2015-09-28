package behaviouralmodel;
import java.util.LinkedList;

public class HTN {
	
	int currentGoal = 0;
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
			Goal goal = goals.get(0);
			goal.Update();
			if(goal.completed)
				currentGoal++;
		}
		
		for(int i = 0; i < units.size(); i++)
		{
			units.get(i).Update(delta);
		}
	}
	
	public void AddGoal(Goal goal)
	{
		goals.add(goal);
	}
	
	public void AddUnit(Unit unit)
	{
		units.add(unit);
	}
	
	public void AddBuilding(Building building)
	{
		buildings.add(building);
	}
	
	public Goal GetGoal(int index)
	{
		if(index > -1 && index < goals.size())
			return goals.get(index);
		return null;
	}
	
	public Unit GetUnit(int index)
	{
		if(index > -1 && index < units.size())
			return units.get(index);
		return null;
	}
	
	public Building GetBuilding(int index)
	{
		if(index > -1 && index < buildings.size())
			return buildings.get(index);
		return null;
	}
	
	public void RemoveGoal(int index)
	{
		if(index > -1 && index < goals.size())
			goals.remove(index);
	}
	
	public void RemoveUnit(int index)
	{
		if(index > -1 && index < units.size())
			units.remove(index);
	}
	
	public void RemoveBuidling(int index)
	{
		if(index > -1 && index < buildings.size())
			buildings.remove(index);
	}
}
