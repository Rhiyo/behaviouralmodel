package behaviouralmodel;

import java.util.LinkedList;

/*
 * An abstract class for goals that contain more goals
 */
public abstract class GoalRecursive implements Goal{
	LinkedList<Goal> goals;
	GoalRecursive parent;
	String id;
	int status;
	
	public GoalRecursive(){
		goals = new LinkedList<Goal>();
	}
	
	@Override
	public void update(float delta){
		if(status == 0)
			status=1;
	}
	
	public void addGoal(Goal goal)
	{
		goals.add(goal);
	}
	
	/**
	 * Looks at this and all children for ID
	 * @param {Stirng} id of goal to search for
	 * @return found goal, null if none found
	 */
	public GoalRecursive findID(String id)
	{
		if(this.getID().toLowerCase().equals(id)){
			return this;
		}
		for(Goal goal : goals){
			if(goal instanceof GoalRecursive){
				GoalRecursive foundGoal = ((GoalRecursive) goal).findID(id);
				if(foundGoal!=null)
					return foundGoal;
			}
		}
		return null;
	}
	

	@Override
	public void printStructure(int level, Goal current) {
		for(int i=0;i<level;i++)
			System.out.print("-");
		System.out.print(id + " (" + this.getClass().getSimpleName() + ")");
		if(this.equals(current))
			System.out.print("<<");
		System.out.println();
		for(Goal goal : goals)
			goal.printStructure(level+1, current);
	}
	
	@Override
	public void setID(String id) {
		this.id = id;
	}

	@Override
	public String getID() {
		return id;
	}
	
	@Override
	public GoalRecursive getParent(){
		return this.parent;
	}
	
	@Override
	public void setParent(GoalRecursive goal){
		this.parent = goal;
	}
	
	@Override
	public void reset(){
		for(Goal goal : goals)
			goal.reset();
		status = 0;
	}
	
	@Override
	public boolean isCompleted(){
		if(status == 2)
			return true;
		return false;			
	}
}
