package behaviouralmodel;
/*
 * An individual unit member in a unit
 */
public class UnitMember {

	//Vector2 localPos - position relative to the entire unit position
	//Boolean isDead
	//int grenade count - amount of grenades used
	
	//Variables
	private Vector2 localPos;
	private Vector2 initialLocalPos;
	
	//Constructor
	public UnitMember(int relX, int relY){
		initialLocalPos = new Vector2(relX,relY);
		reset();
	}
	
	/**
	 * Returns x
	 * @return local x
	 */
	public float getX(){
		return localPos.x;
	}
	
	/**
	 * Returns y
	 * @return local y
	 */
	public float getY(){
		return localPos.y;
	}
	
	/**
	 * Resets unit to default settings
	 */
	public void reset(){
		localPos = initialLocalPos;
	}
}
