package behaviouralmodel;

import java.util.LinkedList;

/*
 * A squad of units, default of 4
 */

public class Unit {
	
	//Variables
	private Vector2 position;
	private LinkedList<UnitMember> unitMembers;
	private String id;
	
	//Constructor
	public Unit(int x, int y){
		position = new Vector2(x,y);
		
		unitMembers = new LinkedList<UnitMember>();
		//Default of 4 units
		unitMembers.add(new UnitMember(-1,-1));
		unitMembers.add(new UnitMember(-1,+1));
		unitMembers.add(new UnitMember(+1,-1));
		unitMembers.add(new UnitMember(+1,+1));
	}
	
	//GETTERSETTER
	public void setPosition(Vector2 value )
	{
		this.position = value;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public Vector2 getPosition()
	{
		return position;
	}
	
	public int getX(){
		return (int) position.x;
	}
	
	public int getY(){
		return (int) position.y;
	}
	
	public LinkedList<UnitMember> GetUnitMembers(){
		return unitMembers;
	}
	
	public String getId(){
		return id;
	}
	
	
	public void Update(float delta)
	{
		
	}
}
