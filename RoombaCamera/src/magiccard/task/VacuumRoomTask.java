package magiccard.task;

import java.util.ArrayList;

import magiccard.Vector2D;

public class VacuumRoomTask extends HouseworkTask implements Cloneable {

	int timeLimit ; //default time
	boolean firstTimeFlag =true;
	
	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String toString(){
		return "Vacuum Room";
	}
	public VacuumRoomTask createCopy(){
		VacuumRoomTask newTask = new VacuumRoomTask();
		newTask.firstTimeFlag = this.firstTimeFlag;
		newTask.timeLimit = this.timeLimit;
	
		return newTask;
	}
}
