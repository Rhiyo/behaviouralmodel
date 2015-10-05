package behaviouralmodel;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class AStar {
	
	static HTN htn;
	
	private static class Node {
	    Node parent;
	    int x, y;
	    float f, g, h;
	    
	    public Node(Node parent, int x, int y, int g){
	    	this.parent = parent;
	    	this.x = x;
	    	this.y = y;
	    	this.g = g;
	    }
	    
	    public LinkedList<Node> getSuccessors(){
	    	LinkedList<Node> successors = new LinkedList<Node>();
	    	//Up
	    	if(htn.isPassable(x, y+1))
	    		successors.add(new Node(this, x,y+1, 10));
	    	
	    	//Up-Right
	    	if(htn.isPassable(x, y+1)&& htn.isPassable(x+1, y) &&
	    			htn.isPassable(x+1, y+1)){
	    		successors.add(new Node(this, x+1,y+1, 14));
	    	}
	    	
	    	//Right
	    	if(htn.isPassable(x+1, y))
	    		successors.add(new Node(this, x+1,y, 10));
	    	
	    	//Bottom-Right
	    	if(htn.isPassable(x, y-1)&& htn.isPassable(x+1, y) &&
	    			htn.isPassable(x+1, y-1))
	    		successors.add(new Node(this, x+1,y-1, 14));
	    	
	    	//Bottom
	    	if(htn.isPassable(x, y-1))
	    		successors.add(new Node(this, x,y-1, 10));
	    	
	    	//Bottom-Left
	    	if(htn.isPassable(x, y-1)&& htn.isPassable(x-1, y) &&
	    			htn.isPassable(x-1, y-1))
	    		successors.add(new Node(this, x-1,y-1, 14));
	    	
	    	//Left
	    	if(htn.isPassable(x-1, y))
	    		successors.add(new Node(this, x-1,y, 10));
	    	
	    	//Top-Left
	    	if(htn.isPassable(x, y+1)&& htn.isPassable(x-1, y) &&
	    			htn.isPassable(x-1, y+1))
	    		successors.add(new Node(this, x-1,y+1, 14));
	    	
	    	return successors;
	    }
	    
	    @Override 
	    public boolean equals(Object other) {
	    	if(other instanceof Node){
	    		Node node = (Node) other;
	    		if(node.x == x && node.y == y)
	    			return true;	    			
	    	}

	    	return false;
	    	
	    }
	}
	
	private static Vector2[] createPath(Node nodePath){
		int pathSize = 0;
		Node currentNode = nodePath;
		while(currentNode != null){
			currentNode=currentNode.parent;
			pathSize++;
		}
		Vector2[] path = new Vector2[pathSize];
		
		currentNode = nodePath;
		for(int i = path.length-1;i>= 0;i--){
			if(currentNode.parent != null){
				path[i] = new Vector2(currentNode.x-currentNode.parent.x,
						currentNode.y-currentNode.parent.y);
				currentNode = currentNode.parent;
			}else{
				path[i] = new Vector2(0,0);
			}
			System.out.println(path[i].x + " " + path[i].y);
		}
		
		return path;
	}
	
	public static Vector2[] calc(Vector2 start, Vector2 goal){
			if(!htn.isPassable((int)goal.x, (int)goal.y))
				return null;
			PriorityQueue<Node> openList = new PriorityQueue<Node>(new Comparator<Node>(){

				@Override
				public int compare(Node a, Node b) {
			        if (a.f < b.f) return -1;
			        if (a.f > b.f) return 1;
			        return 0;
				}
				
			});
			HashSet<Node> closedList = new HashSet<Node>();
			//LinkedList<Node> openList = new LinkedList<Node>();//Heap priority queue
			//LinkedList<Node> closedList = new LinkedList<Node>();//Set or Hashmap
			Node startNode = new Node(null, (int)start.x, (int)start.y, 0);
			
			openList.add(startNode);

			while(!openList.isEmpty()){
			    Node currentNode = openList.poll();
				if(currentNode.x == goal.x && currentNode.y == goal.y)
		    		return createPath(currentNode);
				closedList.add(currentNode);
			    for(Node successor : currentNode.getSuccessors()){
			    	if(closedList.contains(successor))
			    		continue;
			    	
		            int x = successor.x;
		            int y = successor.y;

		            // get the distance between current node and the neighbor
		            // and calculate the next g score
		            float ng = (float) (currentNode.g + ((x - currentNode.x == 0 || y - currentNode.y == 0) ? 1 : Math.sqrt(2)));
		            
		            if(!openList.contains(successor) || ng < successor.g){
		            	successor.g = ng;
			        	float dx = Math.abs(goal.x - successor.x);
			        	float dy = Math.abs(goal.y - successor.y);
			        	float h = (float) (Math.sqrt(2) - 1);
			        	successor.h = (dx < dy) ? h * dx + dy : h * dy + dx;
			        	successor.f = successor.g + successor.h;
			        	successor.parent = currentNode;
		                if (!openList.contains(successor)) {
		                    openList.add(successor);
		                } else {
		                    openList.remove(successor);
		                    openList.add(successor);
		                }

		            }
			    }
			    
			}
			System.out.println("Failure");
			return null;
	}
}
