package magiccard.task;

import java.util.Observable;
import java.util.Observer;

import magiccard.TaskManager;
import magiccard.TrackingSystem;
import magiccard.TaskManager.Signal;

public class CompoundTaskController extends TaskController implements Observer {
	//so this guy needs to have all the asks 
	
	Status meOperation_status;
	CompoundTask moCompoundTask;
	private int miCurrentSubTask;
	Signal[] subtaskSignals;
	
	
	public CompoundTaskController(TrackingSystem tsystem, CompoundTask compoundTask, TaskController parent){
		moTsystem = tsystem;
		meOperation_status = Status.NOSTATUS;
		moCompoundTask = compoundTask;
		moParent = parent;
		int noOfSubTasks = compoundTask.getNumOfSubTasks();
		if(noOfSubTasks != 0) {
			subtaskSignals = new Signal[noOfSubTasks];
			for(int i=0; i<noOfSubTasks; i++){
				subtaskSignals[i] = Signal.NOTSTARTED;
			}//end of for
		}//end of if
		
	}//end of method
	
	@Override
	Status getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void start() {
		//timer = new Timer();
		//timer.schedule(new Ticker(this), 100, 200);
		//status = Status.RUNNING;
		if(moCompoundTask.getNumOfSubTasks() > 0){
			TaskController ctrl = moCompoundTask.getHouseworkTask(0).getController();
			ctrl.addObserver(this);
			ctrl.start();
			miCurrentSubTask = 0;
			subtaskSignals[miCurrentSubTask] = Signal.STARTED;
		}
	}

	@Override
	public void update(Observable ctl, Object arg1) {
		// TODO Auto-generated method stub
		if (arg1 == TaskManager.Signal.FINISHED) {
    		ctl.deleteObservers();
    		subtaskSignals[miCurrentSubTask] = Signal.FINISHED;
    		miCurrentSubTask ++;
    		startSubTaskByOrder(miCurrentSubTask);
    		
    		//if all subtasks are finished
    		if(allSubTasksFinished())
    			notifyObservers(Signal.FINISHED);
    	}
	}
	
	private boolean allSubTasksFinished(){
		if(subtaskSignals == null || subtaskSignals.length == 0) return true;
		else{
			for(int i=0; i< subtaskSignals.length; i++){
					if(subtaskSignals[i] != TaskManager.Signal.FINISHED || subtaskSignals[i] != TaskManager.Signal.CANCELLED)
						return false;
			}//end of for loop
			return true;
		}
	}
	
	private void startSubTaskByOrder(int index){
		if(moCompoundTask.getNumOfSubTasks() <= index)
    		return;
    	else{
    		TaskController ctrl = moCompoundTask.getHouseworkTask(index).getController();
    		ctrl.addObserver(this);
    		ctrl.start();
    		miCurrentSubTask = index;
    		subtaskSignals[miCurrentSubTask] = TaskManager.Signal.STARTED;
    	}//end of else
	}
}
