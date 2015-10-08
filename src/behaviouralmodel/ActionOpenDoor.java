/**
 * 
 */
package behaviouralmodel;

/**
 * @author sean
 * Makes UnitMember open door
 */
public class ActionOpenDoor extends Action{

	public Door doorToOpen;
	public UnitMember opener;
	
	public ActionOpenDoor(Door door, UnitMember unitMember, Action next){
		super(next);
		this.doorToOpen = door;
		this.opener = unitMember;
	}
	
	@Override
	public void update(float delta) {
		doorToOpen.setOpened(true);
		status = 2;
		int ux = (int)opener.getWorldPosition().x;
		int uy = (int)opener.getWorldPosition().y;
		int dx = (int)doorToOpen.getWorldPosition().x;
		int dy = (int)doorToOpen.getWorldPosition().y;
		if(ux == dx-1 && uy == dy ||
				ux == dx-1 && uy == dy-1 ||
				ux == dx && uy == dy-1 ||
				ux == dx+1 && uy == dy || 
				ux == dx && uy == dy+1 ||
				ux == dx+1 && uy == dy+1 ||
				ux == dx+1 && uy == dy-1 ||
				ux == dx-1 && uy == dy+1){
			doorToOpen.setOpened(true);
			status = 2;
		}else{
			status = -1;
		}
	}

}
