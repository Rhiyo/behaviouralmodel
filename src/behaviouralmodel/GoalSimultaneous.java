package behaviouralmodel;


public class GoalSimultaneous extends GoalRecursive implements Goal{
		
		int completed = 0;
		@Override
		public void update(float delta)
		{
			super.update(delta);
			for(int i = 0; i < goals.size(); i++)
			{
				if(!goals.get(i).isCompleted()){
					goals.get(i).update(delta);
					if(goals.get(i).isCompleted())
						completed++;
				}

			}
			if(completed == goals.size())
				status=2;
			
		}		
		
}
