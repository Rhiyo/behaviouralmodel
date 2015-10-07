/**
 * 
 */
package behaviouralmodel;

/**
 * @author sean
 *
 */
public class Transition {
	State targetState;
	Condition condition;
	
	public boolean isTriggered(){
		return condition.test();
	}
}
