package magiccard.task;

import java.util.ArrayList;

import magiccard.Vector2D;

public class CompoundTask extends HouseworkTask {
	
	boolean mbHierarchicalCompoundTask = false;
	public Vector2D position;
	
	ArrayList<HouseworkTask> moSubTasks = new ArrayList<HouseworkTask>();
	
	public void addSubTask(HouseworkTask task){
		moSubTasks.add(task);
	}
	
	public int getNumOfSubTasks(){
		return moSubTasks.size();
	}
	
	public HouseworkTask getHouseworkTask(int i){
		if(i >- 1 && i< moSubTasks.size()){
			return moSubTasks.get(i);
		}//end of if
		else return null;
	}//end of method
	
	public String toString(){
		StringBuffer compoundtaskStr = new StringBuffer();
		compoundtaskStr.append("[Compound task: ");
		for(int i=0; i< moSubTasks.size(); i++){
			compoundtaskStr.append("(");
			compoundtaskStr.append(moSubTasks.get(i).toString());
			compoundtaskStr.append("),\n");
		}//end of for loop
		compoundtaskStr.append("]");
		return compoundtaskStr.toString();	
	}
}
