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
	protected int status;
	protected Action next;
	
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
}
