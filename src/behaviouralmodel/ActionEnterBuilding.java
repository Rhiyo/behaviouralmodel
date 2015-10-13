package behaviouralmodel;

/**
 * @author sean
 * Makes a unit enter a building for a period to clear it
 */
public class ActionEnterBuilding extends Action {
	
	private Unit enteringUnit;
	private Building buildingToEnter;
	private float timer; //time in building
	private static final float CLEAR_TIME = 3; //Time it takes for the building to be cleared
	public ActionEnterBuilding(Building building, Unit unit, Action next) {
		super(next);
		this.enteringUnit = unit;
		this.buildingToEnter = building;
	}

	@Override
	public void update(float delta){
		System.out.println("OUT");
		if(status == 0){
			enteringUnit.enterBuilding(buildingToEnter);
			status = 1;
		}else{
			
			timer+=delta;
			
			if(timer >= CLEAR_TIME){
				
				enteringUnit.leaveBuilding();
				status = 2;
			}
		}
	}
	
	@Override
	public void reset(){
		super.reset();
		timer = 0;
	}
	
}
