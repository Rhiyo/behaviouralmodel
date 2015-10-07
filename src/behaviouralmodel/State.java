/**
 * 
 */
package behaviouralmodel;

import java.util.LinkedList;

/**
 * @author sean
 * a task for a specified unit to act on. Able to transition to another task
 */
public class State {
	protected Unit unit;
	protected LinkedList<Transition> transitions;
	
	public void update(){
		
	}
	
	public LinkedList<Transition> getTransitions(){
		return transitions;
	}
	
	
}
