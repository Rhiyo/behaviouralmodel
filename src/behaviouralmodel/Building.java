package behaviouralmodel;

import java.util.LinkedList;

public class Building extends Entity{
	
	Vector2 position;
	int width,height;
	
	//Vector2 doorPosition; //Relative to startPosition, can only be on edge, no corners;
	
	LinkedList<Door> doors;
	
	public float Left() { return position.x; }
	public float Right() { return position.x + width; }
	public float Top() { return position.y; }
	public float Bottom() { return position.y + height; }
	
	public Building(int x, int y, int width, int height, String id)
	{
		super(id);
		this.position = new Vector2(x,y);
		this.width = width;
		this.height = height;
		
		this.doors = new LinkedList<Door>();
	}
	
	/**
	 * Adds door to building
	 * @param relX
	 * @param relY
	 */
	public void addDoor(int relX, int relY)
	{
		Door door = new Door(relX, relY, "door"+doors.size());
		door.setId(getId() + "d" + doors.size());
		doors.add(door);
		//doorPosition = door;
		/*
		if((doorx == (int)Left() || doorx == (int)Right()) && (doory < (int)Top() && doory > (int)Bottom()))
		{
			doorPosition = door;
		}
		
		if((doory == (int)Top() || doory == (int)Bottom()) && (doorx < (int)Right() && doorx > (int)Left()))
		{
			doorPosition = door;
		}
		*/
	}
	
	public Vector2 getDoorDir(Door door){
		if(!doors.contains(door))
			return null;
		Vector2 dir = new Vector2(0,0);
		if(door.getX() == 0)
			dir.x = -1;
		if(door.getX() == width)
			dir.x = 1;
		if(door.getY() == 0)
			dir.y = -1;
		if(door.getY() == height)
			dir.y = 1;
				
		return dir;
	}
	
	
	
	public boolean openDoor(){
		
		return false;
	}
	
	//Getter
	public float getX() { return position.x; }
	public float getY() { return position.y; }
	public float getWidth() { return width; };
	public float getHeight() { return height; };
	public LinkedList<Door> getDoors() { return doors; };
	//public float getDoorX() { return doorPosition.x; };
	//public float getDoorY() { return doorPosition.y; };

}
