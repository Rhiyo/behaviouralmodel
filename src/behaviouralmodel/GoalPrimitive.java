
package behaviouralmodel;

/*
 * Gives direct orders to units and their members
 */
public class GoalPrimitive implements Goal{

	protected Unit orderedUnit;
	protected String id;
	GoalRecursive parent;
	int status;
	private Action start;
	private Action current;
	
	public GoalPrimitive(Action start)
	{
		this.start = start;
	}
	
	public GoalPrimitive(Unit unit, Action start){
		this.start = start;
		current = start;
	}
	

	@Override
	public void update(float delta) {
		if(status == 0)
			status=1;
		
		if(current == null){
			status = 2;
			return;
		}
		
		current.update(delta);
		
		if(current.status == 2 || current.status == -1)
			current = current.transition();
	}

	@Override
	public boolean isCompleted() {
		if(status==2)
			return true;
		return false;
	}

	public void printStructure(int level, Goal current) {
		for(int i=0;i<level;i++)
			System.out.print("-");
		System.out.print(id + " (" + this.getClass().getSimpleName() + ")");
		System.out.println();
	}
	
	public void SetUnit(Unit unit)
	{
		this.orderedUnit = unit;
	}
	
	@Override
	public void setID(String id) {
		this.id = id;
		
	}

	@Override
	public String getID() {
		return this.id;
	}
	
	@Override 
	public GoalRecursive getParent(){
		return this.parent;
	}
	
	@Override
	public void setParent(GoalRecursive goal){
		this.parent = goal;
	}
	
	@Override
	public void reset(){
		status = 0;
		orderedUnit.reset();
		start.reset();
		current = start;
	}
}
