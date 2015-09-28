package behaviouralmodel;
import java.util.LinkedList;


public class Goal {
		protected LinkedList<Goal> goals;
		protected boolean completed;
		
		public void Update()
		{
			for(int i = 0; i < goals.size(); i++)
			{
				goals.get(i).Update();
			}
		}
		
		public void AddGoal(Goal goal)
		{
			goals.add(goal);
		}
}
