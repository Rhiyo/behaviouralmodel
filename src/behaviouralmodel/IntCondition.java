/**
 * 
 */
package behaviouralmodel;

/**
 * @author sean
 * Test for a range or specific int
 */
public class IntCondition implements Condition {
	int minValue;
	int maxValue;
	Integer testValue;
	
	@Override
	public boolean test() {
		return minValue <= testValue.intValue() && testValue.intValue() <= maxValue;
	}

}
