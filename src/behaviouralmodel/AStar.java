package behaviouralmodel;

import java.util.LinkedList;

public class AStar {
	private static class Node {
	    Node parent;
	    int x, y;
	    float f, g, h;
	    
	    public Node(){
	    	
	    }
	    public LinkedList<Node> getSuccessors(){
	    	LinkedList<Node> successors = new LinkedList<Node>();
	    	
	    	//Check to see if node is free
	    	for(int i=0;i<8;i++){
		    	Node node = new Node();
		    	node.parent = this;	
		    	node.x = x;
		    	node.y = y;
		    	successors.add(node);
	    	}

	    	successors.get(0).x-=1;
	    	successors.get(0).y-=1;
	    	
	    	successors.get(1).x-=1;
	    	successors.get(1).y+=1;
	    	
	    	successors.get(2).x+=1;
	    	successors.get(2).y-=1;	 
	    	
	    	successors.get(3).x+=1;
	    	successors.get(3).y+=1;

	    	successors.get(4).y+=1;	
	    	
	    	successors.get(5).y-=1;	 
	    	
	    	successors.get(6).x-=1;
	    	
	    	successors.get(7).x+=1;
	    	return successors;
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
			LinkedList<Node> openList = new LinkedList<Node>();
			LinkedList<Node> closedList = new LinkedList<Node>();
			Node startNode = new Node();
			startNode.x = (int) start.x;
			startNode.y = (int)start.y;

			openList.add(startNode);
			while(!openList.isEmpty()){
			    //find the node with the least f on the open list, call it "q"
			    Node lowestNode = null;
			    for(Node node : openList){
			    	if(lowestNode == null){
			    		lowestNode = node;
			    	}else if(node.f<lowestNode.f)
			    		lowestNode = node;
			    }
			    openList.remove(lowestNode);
			    System.out.println("X:"+lowestNode.x + " " + lowestNode.y);
			    for(Node successor : lowestNode.getSuccessors()){
					if(successor.x == goal.x && successor.y == goal.y)
			    		return createPath(successor);
			        successor.g = lowestNode.g + 1;
			        successor.h = (float) Math.sqrt(Math.pow(goal.x-successor.x,2)+
			        		Math.pow(goal.y-successor.y,2));
			        successor.f = successor.g + successor.h;
		            
			        //which has a lower f than successor, skip this successor
			        for(Node node : openList){
			        	if(node.x == successor.x && node.y == successor.y &&
			        			node.f < successor.f){
			        		continue;
			        	}
			        }
 
			        for(Node node : closedList){
			        	if(node.x == successor.x && node.y == successor.y &&
			        			node.f < successor.f){
			        		continue;
			        	}
			        }
			        openList.add(successor);
			    }
			    closedList.add(lowestNode);
			}
			System.out.println("Failure");
			return null;
	}
}
