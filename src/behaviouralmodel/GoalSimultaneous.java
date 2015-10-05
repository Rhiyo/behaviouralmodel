package behaviouralmodel;


public class GoalSimultaneous extends GoalRecursive implements Goal{
		
		int completed = 0;
		@Override
		public void update(float delta)
		{
			for(int i = 0; i < goals.size(); i++)
			{
				if(!goals.get(i).isCompleted()){
					goals.get(i).update(delta);
					if(goals.get(i).isCompleted())
						completed++;
				}

			}
			
		}

		@Override
		public boolean isCompleted() {
			if(completed == goals.size())
				return true;
			return false;
		}

		@Override
		public void setID(String id) {
			// TODO Auto-generated method stub
			
		}
		
		
}
