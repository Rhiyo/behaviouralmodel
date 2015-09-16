
public class Building {
	Vector2 position;
	int width,height;
	
	Vector2 doorPosition; //Relative to startPosition, can only be on edge, no corners;
	
	public float Left() { return position.x; }
	public float Right() { return position.x + width; }
	public float Top() { return position.y; }
	public float Bottom() { return position.y + height; }
	
	public Building(Vector2 position, int width, int height)
	{
		this.position = position;
		this.width = width;
		this.height = height;
	}
	
	public void SetDoor(Vector2 door)
	{
		int doorx = (int)(position.x + door.x);
		int doory = (int)(position.y + door.y);
		
		if((doorx == (int)Left() || doorx == (int)Right()) && (doory < (int)Top() && doory > (int)Bottom()))
		{
			doorPosition = door;
		}
		
		if((doory == (int)Top() || doory == (int)Bottom()) && (doorx < (int)Right() && doorx > (int)Left()))
		{
			doorPosition = door;
		}
	}
}
