package magiccard.task;

import magiccard.Vector2D;

public class PushTask extends HouseworkTask implements Cloneable {
	int objectID;
	Vector2D goal;
	public String toString(){
		return "Push " + objectID + " to " + goal.toString();
	}
	public PushTask createCopy(){
		PushTask newTask = new PushTask();
		newTask.objectID = this.objectID;
		newTask.goal = this.goal;

		return newTask;
	}
	
}
