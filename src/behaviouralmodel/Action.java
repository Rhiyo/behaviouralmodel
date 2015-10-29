/**
 * 
 */
package behaviouralmodel;

/**
 * @author sean
 * An action interface
 */
public abstract class Action {
	/*
	 * 
	 */
	
	protected String id = "";
	protected int status;
	protected Action next;
	protected Unit orderedUnit;
	
	public Action(Action next){
		this.next = next;
	}
	/**
	 * Updates per delta
	 * @param delta
	 */
	public void update(float delta){
		if(status == 0)
			status = 1;
	};
	
	public void SetAction(Action next)
	{
		this.next = next;
	}
	
	public void SetID(String id)
	{
		this.id = id;
	}
	
	public String GetID() { return this.id; }
	
	public Unit getOrderedUnit(){
		return orderedUnit;
	}
	
	public String toString(){
		return "";
	};
	
	/**
	 * Resets the action
	 */
	public void reset(){
		if(next != null)
			next.reset();
		status = 0;
	}
	
	/**
	 * Where to go next
	 * @return the next action
	 */
	public Action transition(){
		return next;
	}
	
	public void printOut(int level){
		for(int i=0;i<level;i++)
			System.out.print("-");
		System.out.print(id + " (" + this.getClass().getSimpleName() + ")");
		System.out.println();
		if(next != null)
			next.printOut(level);
	}
}
