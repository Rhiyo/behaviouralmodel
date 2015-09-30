package behaviouralmodel;

public class PrimitiveMove extends GoalPrimitive implements Goal {

	Vector2[] path;
	int pathIndex = 0;
	Vector2 goal;
	Vector2 immediateGoal;
	public PrimitiveMove(Unit unit, int x, int y){
		this.orderedUnit = unit;
		this.goal = new Vector2(x,y);
	}
	
	@Override
	public void update(float delta){
		Vector2 newPos = new Vector2();
		if(this.path == null){
			this.path = AStar.calc(orderedUnit.getPosition(), goal);
			this.immediateGoal = new Vector2(orderedUnit.getX()+path[0].x,
					orderedUnit.getY()+path[0].y);
		}
		newPos.x = orderedUnit.getPosition().x+path[pathIndex].x*delta;
		if((path[pathIndex].x==-1 && newPos.x < immediateGoal.x) || 
				(path[pathIndex].x==1 && newPos.x > immediateGoal.x))
			newPos.x = immediateGoal.x;
		newPos.y = orderedUnit.getPosition().y+path[pathIndex].y*delta;
		if((path[pathIndex].y==-1 && newPos.y < immediateGoal.y) || 
				(path[pathIndex].y==1 && newPos.y > immediateGoal.y))
			newPos.y = immediateGoal.y;
		
		orderedUnit.setPosition(newPos);
		//Make sure unit is at next point before moving again
		if(orderedUnit.getPosition().x == immediateGoal.x && 
				orderedUnit.getPosition().y == immediateGoal.y){
			pathIndex++;
			System.out.println(""+pathIndex);
			if(pathIndex < path.length)
				this.immediateGoal = new Vector2(orderedUnit.getX()+path[pathIndex].x,
					orderedUnit.getY()+path[pathIndex].y);
		}
		System.out.println("C " + orderedUnit.getX() + " " + orderedUnit.getY()
				+ " G " + immediateGoal.x + " " + immediateGoal.y);
		
	}
	
	@Override
	public boolean isCompleted(){
		if(pathIndex==path.length){
			System.out.println("Asd");
			return true;
		}
		return false;
	}
}
