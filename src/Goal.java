import java.util.LinkedList;


public class Goal {
		private LinkedList<Goal> goals;
		
		public void test(){
			
		}
		
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
