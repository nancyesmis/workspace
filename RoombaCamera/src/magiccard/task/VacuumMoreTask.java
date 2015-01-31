package magiccard.task;

import magiccard.Vector2D;

public class VacuumMoreTask extends HouseworkTask implements Cloneable{
	int timeLimit;
	Vector2D spot;
	boolean firstTimeFlag =true;

	boolean destinationFlag =true;
	
	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public Vector2D getSpot() {
		return spot;
	}

	public void setSpot(Vector2D spot) {
		this.spot = spot;
	}

	public String toString(){
		return "Vacuum at " + spot.toString();
	}
	public VacuumMoreTask createCopy(){
		VacuumMoreTask newTask = new VacuumMoreTask();
		newTask.firstTimeFlag = this.firstTimeFlag;
		newTask.timeLimit = this.timeLimit;
		newTask.spot = this.spot;
		newTask.destinationFlag = this.destinationFlag;
	
		return newTask;
	}
}
