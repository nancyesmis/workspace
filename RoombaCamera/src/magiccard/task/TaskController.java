package magiccard.task;

import java.util.*;

import magiccard.TaskManager;
import magiccard.TrackingSystem;
import roomba.MagicCardRoombaController;

public abstract class TaskController extends Observable {
	
	public class Ticker extends TimerTask {
		private TaskController ctl;
		
		Ticker(TaskController ctl) {
			this.ctl = ctl;
		}
		
		public void run() {
			ctl.execTick();
			
		}
	}
	
	private Timer timer;
	
	/*static int tickMS = 100;
	static int anglePerTick =  40;
	static int distancePerTick = 12; 
	static int roombaRadius = 40; */
	
	static final int ANGLE_PER_TICK = 60;
	static final int DISTANCE_PER_TICK = 32;
	static final int ROOMBA_RADIUS = 40;
	static final int CARGO_RADIUS = 30;
	static final int GOAL_RADIUS = 40;
	
	static final int PAUSE_LENGTH = 3;
	
	TrackingSystem moTsystem;
	MagicCardRoombaController moComm;
	int miRoombaId;
	
	boolean mbMoving = false;
	
	TaskController moParent;
	
	boolean avoidFlag = false;
	boolean mbInternal; // use this controller for another task controller

	
	Status status = Status.NOSTATUS;
	
	public enum Status {NOSTATUS, FINISHED, FAILED, RUNNING, MOVING, GOBACK, PAUSED}; 
	
	/**
	 * I want to check to see how things go here
	 */
	public TaskController(){
	}
	
	public void start() {
		timer = new Timer();
		timer.schedule(new Ticker(this), 100, 100);
		status = Status.RUNNING;
	}
	
	public void stop() {
		timer.cancel();
	}
	
	public void execTick() {
		if (this.getStatus() == Status.RUNNING)
			this.tick();
		else {
			timer.cancel();
			System.out.println("timer is canceled!");
			this.setChanged();
			notifyObservers(TaskManager.Signal.FINISHED);
			

		}
	}
	
	abstract public void tick();
	abstract Status getStatus();
}
