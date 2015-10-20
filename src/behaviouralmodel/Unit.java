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
	private int grenadesUsed;
	private Building inBuilding;
	//Constructor
	public Unit(int x, int y, String id){
		super(id);
		
		initialPos = new Vector2(x,y);
		position = initialPos;
		
		unitMembers = new LinkedList<UnitMember>();
		//Default of 4 units
		unitMembers.add(new UnitMember(-1,-1, this, "0"));
		unitMembers.add(new UnitMember(-1,+1, this, "1"));
		unitMembers.add(new UnitMember(+1,-1, this, "2"));
		unitMembers.add(new UnitMember(+1,+1, this, "3"));
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
	
	public void enterBuilding(Building building){
		this.inBuilding = building;
	}
	
	public void throwGrenade(){
		setGrenadesUsed(getGrenadesUsed() + 1);
	}
	
	public Building inBuilding(){
		return inBuilding;
	}
	
	public void leaveBuilding(){
		inBuilding = null;
	}
	
	/**
	 * Resets unit
	 */
	public void reset(){
		position = initialPos;
		for(UnitMember unitMem : unitMembers)
			unitMem.reset();
		setGrenadesUsed(0);
		inBuilding = null;
	}

	public String toString() {
        return id + "(" + position.x + "," + position.y + ")";
    }

	public void setUnitMembers(LinkedList<UnitMember> unitMembers) {
		this.unitMembers = unitMembers;
		
	}

	public int getGrenadesUsed() {
		return grenadesUsed;
	}

	public void setGrenadesUsed(int grenadesUsed) {
		this.grenadesUsed = grenadesUsed;
	}
	
}
