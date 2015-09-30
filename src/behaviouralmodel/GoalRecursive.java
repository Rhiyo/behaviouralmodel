package behaviouralmodel;

import java.util.LinkedList;

/*
 * An abstract class for goals that contain more goals
 */
public abstract class GoalRecursive {
	LinkedList<GoalSimultaneous> goals;
	
	public void AddGoal(GoalSimultaneous goal)
	{
		goals.add(goal);
	}
}
