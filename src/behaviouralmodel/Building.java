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
	
	//Getter
	public float getX() { return position.x; }
	public float getY() { return position.y; }
	public float getWidth() { return width; };
	public float getHeight() { return height; };
	public LinkedList<Door> getDoors() { return doors; };
	//public float getDoorX() { return doorPosition.x; };
	//public float getDoorY() { return doorPosition.y; };

}
