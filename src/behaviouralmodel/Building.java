package behaviouralmodel;


public class Building {
	Vector2 position;
	int width,height;
	
	Vector2 doorPosition; //Relative to startPosition, can only be on edge, no corners;
	
	public float Left() { return position.x; }
	public float Right() { return position.x + width; }
	public float Top() { return position.y; }
	public float Bottom() { return position.y + height; }
	public float getX() { return position.x; }
	public float getY() { return position.y; }
	public float getWidth() { return width; };
	public float getHeight() { return height; };
	public float getDoorX() { return doorPosition.x; };
	public float getDoorY() { return doorPosition.y; };
	
	public Building(int x, int y, int width, int height)
	{
		this.position = new Vector2(x,y);
		this.width = width;
		this.height = height;
	}
	
	public void SetDoor(int relX, int relY)
	{
		Vector2 door = new Vector2(relX, relY);
		int doorx = (int)(position.x + door.x);
		int doory = (int)(position.y + door.y);
		doorPosition = door;
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
}
