/**
 * 
 */
package behaviouralmodel;

/**
 * @author sean
 * Test for a range or specific int
 */
public class Vector2Condition implements Condition {
	Vector2 goalValue;
	Vector2 testValue;
	
	@Override
	public boolean test() {
		return testValue.equals(goalValue);
	}

}
