/**
 * 
 */
package behaviouralmodel;

/**
 * @author sean
 * A goal with the steps to clear a building
 */
public class GoalClearBuilding extends GoalPrimitive implements Goal{
	private Building toClear;
	private Door entryPoint;
	private Vector2 moveToPos; //Position for unit to move too
	private Action start;
	private Action current;
	
	public GoalClearBuilding(Building toClear, Unit unit, Door entryPoint){
		this.toClear = toClear;
		super.orderedUnit = unit;
		this.entryPoint = entryPoint;
		super.id = "clear" + toClear.getId();
		
		Vector2 doorDir = toClear.getDoorDir(entryPoint);
		moveToPos = new Vector2();
		moveToPos.x = toClear.getX() + entryPoint.getX() + doorDir.x*2;
		moveToPos.y = toClear.getY() + entryPoint.getY() + doorDir.y*2;
		System.out.println(moveToPos);

		
		//Transition test = new Transition();
		//test.condition = movedToGoal;
		
		Action enterBuilding = new ActionEnterBuilding(toClear, unit, null);
		
		Action throwGrenade = new ActionThrowGrenade(toClear, unit, enterBuilding);
		
		Action openDoor = new ActionOpenDoor(entryPoint, unit, throwGrenade, enterBuilding);
		
		start = new ActionMove(unit, moveToPos, openDoor);
		
		current = start;
	}
	
	@Override
	public void update(float delta){
		super.update(delta);
		
		if(current == null){
			status = 2;
			return;
		}
		
		current.update(delta);
		
		if(current.status == 2 || current.status == -1)
			current = current.transition();
	}
	
	
	@Override
	public void reset(){
		super.reset();
		start.reset();
		current = start;
	}
}
