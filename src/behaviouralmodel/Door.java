/**
 * 
 */
package behaviouralmodel;

/**
 * @author sean
 *
 */
public class Door extends Entity {
	private int x, y;
	private boolean opened;
	private Building owner;
	
	public Door(int relX, int relY, String id) {
		super(id);
		this.x = relX;
		this.y = relY;
	}
	
	

	
	//Getters
	/**
	 * Returns relative x of door
	 * @return relative x
	 */	
	public int getX(){ return this.x; }

	/**
	 * Returns relative y of door
	 * @return relative y
	 */
	public int getY(){ return this.y; }
	
	/**
	 * Returns door owner
	 * @return building this door is on
	 */
	public Building getOwner(){
		return this.owner;
	}
	
	/**
	 * Is the door opened?
	 * @return whether the door is opened or not
	 */
	public boolean isOpened(){ return opened; }
	

	
	public Vector2 getWorldPosition(){
		return new Vector2(owner.getX()+x, owner.getY()+y);
	}
	
	//Setters
	/**
	 * Sets door as opened
	 * @param opened
	 */
	public void setOpened(boolean opened){
		this.opened = opened;
	}
	
	/**
	 * Sets the owner of this door
	 * @param building
	 */
	public void setOwner(Building building){
		this.owner = building;
	}
	
}
