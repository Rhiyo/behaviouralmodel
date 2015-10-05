package behaviouralmodel;

import java.util.LinkedList;

/*
 * An abstract class for goals that contain more goals
 */
public abstract class GoalRecursive {
	LinkedList<Goal> goals;
	
	public GoalRecursive(){
		goals = new LinkedList<Goal>();
	}
	
	public void addGoal(Goal goal)
	{
		goals.add(goal);
	}
}
