package behaviouralmodel;

public interface Goal {
	
	public void update(float delta);
	
	public boolean isCompleted();
	
	public void setID(String id);
	
	public String getID();
	
	public void printStructure(int level, Goal current);
	
	public void setParent(GoalRecursive goal);
	
	public GoalRecursive getParent();
	
	public void reset();
}
