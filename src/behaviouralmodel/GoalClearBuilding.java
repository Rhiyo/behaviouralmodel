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
	
	public GoalClearBuilding(Building toClear, Unit unit, Door entryPoint){
		this.toClear = toClear;
		super.orderedUnit = unit;
		this.entryPoint = entryPoint;
		super.id = "clear" + toClear.getId();
		
		Vector2 doorDir = toClear.getDoorDir(entryPoint);
		moveToPos = new Vector2();
		moveToPos.x = toClear.getX() + entryPoint.getX() + doorDir.x*2;
		moveToPos.y = toClear.getY() + entryPoint.getY() + doorDir.y*2;
		
		Vector2Condition movedToGoal = new Vector2Condition();
		movedToGoal.goalValue = moveToPos;
		movedToGoal.testValue = unit.getPosition();
		
		Transition test = new Transition();
		test.condition = movedToGoal;
	}
	
	@Override
	public void update(float delta){
		if(status == 0){
			status=1;
		}
	}
	
}
