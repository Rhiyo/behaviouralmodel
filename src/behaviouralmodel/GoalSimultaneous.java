package behaviouralmodel;


public class GoalSimultaneous extends GoalRecursive implements Goal{
		
		@Override
		public void update(float delta)
		{
			for(int i = 0; i < goals.size(); i++)
			{
				goals.get(i).update(delta);
				if(goals.get(i).isCompleted())
					goals.remove(goals.get(i));
			}
			
		}

		@Override
		public boolean isCompleted() {
			if(goals.isEmpty())
				return true;
			return false;
		}
		
		
}
