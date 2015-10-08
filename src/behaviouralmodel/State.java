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
	LinkedList<Transition> transitions = new LinkedList<Transition>();
	LinkedList<Action> actions = new LinkedList<Action>();
	
	public void update(){
		
	}
	
	public LinkedList<Transition> getTransitions(){
		return transitions;
	}
	
	
}
