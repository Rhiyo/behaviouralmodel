package behaviouralmodel;


public class GoalSequential extends GoalRecursive implements Goal{
	int index = 0;
	
	public void update(float delta) {
		super.update(delta);
		if(index >= 0 && index < goals.size())
		{
			goals.get(index).update(delta);
			
			if(goals.get(index).isCompleted())
				index++;
		}
		if(index == goals.size())
			status=2;

	}
	
	@Override
	public void reset(){
		super.reset();
		index = 0;
	}
	
}
