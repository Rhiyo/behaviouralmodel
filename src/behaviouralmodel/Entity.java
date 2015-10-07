/**
 * 
 */
package behaviouralmodel;

/**
 * @author sean
 * An abstract class with an ID, for any object on the grid
 */
public abstract class Entity {
	
	protected String id;
	
	public Entity(String id){
		this.id = id;
	}
	
	/**
	 * Sets the id of this entity
	 * @param id
	 */
	public void setId(String id){
		this.id = id;
	}
	
	/**
	 * get id
	 * @return id of this entity
	 */
	public String getId(){
		return id;
	}
	
}
