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
	 * Is the door opened?
	 * @return whether the door is opened or not
	 */
	public boolean isOpened(){ return opened; }
	
	/**
	 * Returns relative y of door
	 * @return relative y
	 */
	public int getY(){ return this.y; }
}
