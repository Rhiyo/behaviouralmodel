package behaviouralmodel;

import java.util.LinkedList;

/*
 * A squad of units, default of 4
 */

public class Unit extends Entity{
	
	//Variables
	private Vector2 initialPos;
	private Vector2 position;
	private LinkedList<UnitMember> unitMembers;
	
	//Constructor
	public Unit(int x, int y, String id){
		super(id);
		
		initialPos = new Vector2(x,y);
		position = initialPos;
		
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
	
	public Vector2 getPosition()
	{
		return position;
	}
	
	public float getX(){
		return position.x;
	}
	
	public float getY(){
		return position.y;
	}
	
	public LinkedList<UnitMember> GetUnitMembers(){
		return unitMembers;
	}
	
	public void Update(float delta)
	{
		
	}
	
	/**
	 * Resets unit
	 */
	public void reset(){
		position = initialPos;
		for(UnitMember unitMem : unitMembers)
			unitMem.reset();
	}

	public String toString() {
        return id + "(" + position.x + "," + position.y + ")";
    }
	
}
