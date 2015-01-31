package magiccard.task;

import java.util.ArrayList;

import magiccard.Vector2D;

public class SendTask extends HouseworkTask implements Cloneable {
	int objectID;
	ArrayList<Vector2D> positionList = new ArrayList<Vector2D>();
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		
		
		for(int i=0; i< positionList.size(); i++){
			buf.append(positionList.get(i).toString()+ " ");
		}
		
		return "Send " + objectID + " to " + buf.toString(); 
	}
	
	public SendTask createCopy(){
		SendTask newTask = new SendTask();
		newTask.objectID = this.objectID;
		ArrayList<Vector2D> newPositionList = new ArrayList<Vector2D>();
		if(positionList != null && positionList.size() > 0){
			for(int i=0; i < positionList.size(); i++){
				Vector2D oldVec = positionList.get(i);
				Vector2D aVec = new Vector2D(oldVec.x, oldVec.y);
				newPositionList.add(aVec);
			}//end of for
			newTask.positionList = newPositionList;
		}//end of if

		return newTask;
	}
}
