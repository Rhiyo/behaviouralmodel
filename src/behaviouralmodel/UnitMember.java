package behaviouralmodel;

public class UnitMember {

	//Vector2 localPos - position relative to the entire unit position
	//Boolean isDead
	//int grenade count - amount of grenades used
	
	//Variables
	private Vector2 localPos;
	
	//Constructor
	public UnitMember(int relX, int relY){
		localPos = new Vector2(relX,relY);
	}
	
	//Getters
	public float getX(){
		return localPos.x;
	}
	
	public float getY(){
		return localPos.y;
	}
	//Setters
	
}
