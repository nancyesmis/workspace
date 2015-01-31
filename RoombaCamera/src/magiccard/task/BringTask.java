package magiccard.task;

import java.util.ArrayList;
import magiccard.Vector2D;

public class BringTask extends HouseworkTask implements Cloneable {
	ArrayList<Integer> itemList;
	Vector2D goal;
	
	public String toString(){
		//need to return the number of items it collects
		StringBuffer buf = new StringBuffer();
		
		for(int i=0; i< itemList.size(); i++){
			buf.append(itemList.get(i).toString()+ " ");
		}
		
		return "Bring multiple items ("+ buf.toString()+") to " + goal.toString();
	}
	
	public BringTask createCopy(){
		BringTask newTask = new BringTask();
		newTask.goal = this.goal.duplicate();
		ArrayList<Integer> myItemList = new ArrayList<Integer>();
		if(itemList != null && itemList.size() > 0){
			for(int i=0; i < itemList.size(); i++){
				Integer oldVec = itemList.get(i);
				Integer aVec = new Integer(oldVec.intValue());
				myItemList.add(aVec);
			}//end of for
			newTask.itemList = myItemList;
		}//end of if

		return newTask;
	}
}
