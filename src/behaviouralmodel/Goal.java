package behaviouralmodel;

public interface Goal {
	
	public void update(float delta);
	
	public boolean isCompleted();
	
	public void setID(String id);
}
