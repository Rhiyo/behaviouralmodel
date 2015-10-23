package behaviouralmodel;
/**
 * @author sean
 * Makes unit throw grenade inside building
 */
public class ActionThrowGrenade extends Action{
	private Unit grenadeThrower; //Time until the grenade has blown up
	private Building building;
	private float grenadeTimer;
	private final static float GRENADE_TIME = 3;

	public ActionThrowGrenade()
	{
		super(null);
	}
	
	public ActionThrowGrenade(Building building, Unit unit, Action next) {
		super(next);
		this.grenadeThrower = unit;
		this.building = building;
	}
	
	public void SetBuilding(Building building) {
		this.building = building;
	}
	
	public void SetUnit(Unit unit) {
		this.grenadeThrower = unit;
	}

	@Override
	public void update(float delta){
		System.out.println("ASD");
		if(this.status == 0){
			grenadeThrower.throwGrenade();
			status = 1;
		}else if(grenadeTimer >= GRENADE_TIME){
				building.setEnemies(false);
				status = 2;
				return;
		}
		grenadeTimer+=delta;	
		
	}
	
	@Override
	public void reset(){
		super.reset();
		grenadeTimer = 0;
	}
}
