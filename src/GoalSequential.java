
public class GoalSequential extends Goal{
	int index = 0;

	@Override
	public void Update() {
		if(index >= 0 && index < goals.size())
		{
			goals.get(index).Update();
		}
	}
	
}
