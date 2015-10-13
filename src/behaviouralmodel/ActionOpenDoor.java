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
	public Unit opener;
	public Action enemiesFound;
	
	public ActionOpenDoor(Door door, Unit unit, Action enemiesFound, Action next){
		super(next);
		this.doorToOpen = door;
		this.opener = unit;
		this.enemiesFound = enemiesFound;
	}
	
	@Override
	public void update(float delta) {
		doorToOpen.setOpened(true);
		status = 2;
		for(UnitMember unitMember : opener.GetUnitMembers()){
			int ux = (int)unitMember.getWorldPosition().x;
			int uy = (int)unitMember.getWorldPosition().y;
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
				return;
			}else{
				
			}
		}
		status = -1;
	}
	
	@Override
	public Action transition(){
		if(this.doorToOpen.getOwner().isEnemies())
			return enemiesFound;
		else return next;
	}
	
	@Override
	public void reset(){
		super.reset();
		if(enemiesFound != null)
			enemiesFound.reset();
	}

}
