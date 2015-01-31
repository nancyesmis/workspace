/**
 * 
 */
package magiccard.task;


import magiccard.Tag;
import magiccard.TaskManager;
import magiccard.TrackingSystem;
import magiccard.Vector2D;
import magiccard.TaskManager.Signal;
import magiccard.task.TaskController.Status;
import magiccard.task.TaskController.Ticker;
import roomba.MagicCardRoombaController;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Shengdong Zhao
 *
 */
public class CollectTagsController extends TaskController implements Observer {

	private int miCurrentSubTask;
	Status meOperation_status;
	private Timer timer;
	MoveController moMoveController;
	Vector2D backGoal =new Vector2D(0, -200);
	CollectTagsController moCollectTagsController;
	public CollectTagsTask moCollectTagsTask;
	Signal[] subtaskSignals;
	public ArrayList<Tag> myCollectTags = new ArrayList<Tag>();
	Vector2D negativeLengthOfRoomba = new Vector2D(2,2);
	Vector2D positiveLengthOfRoomba = new Vector2D(-2,-2);
	
	public class Ticker extends TimerTask {
		private CollectTagsController ctl;
		
		Ticker(CollectTagsController ctl) {
			this.ctl = ctl;
		}
		
		public void run() {
			ctl.execTick();
			
		}
	}
	
	
	public CollectTagsController(TrackingSystem tsystem, MagicCardRoombaController roombacomm, int PICKUP_ROOMBA_ID, CollectTagsTask collectTagsTask, TaskController parent){
			
			moTsystem = tsystem;
			moComm = roombacomm;
			miRoombaId = PICKUP_ROOMBA_ID;
			meOperation_status = Status.NOSTATUS;
			mbMoving = false;
			moParent = parent; 
			moCollectTagsTask =  collectTagsTask.createCopy();
			moMoveController = new MoveController(moTsystem, moComm, miRoombaId, backGoal, this);
			int noOfSubTasks = moCollectTagsTask.getTagsNum()*2;
			if(noOfSubTasks != 0) {
				subtaskSignals = new Signal[noOfSubTasks];
				for(int i=0; i<noOfSubTasks; i++){
					subtaskSignals[i] = Signal.NOTSTARTED;
				}//end of for
			}//end of if		
			
			//moMoveController = new MoveController(moTsystem, moComm, miRoombaId, moVacuumMoreTask.spot, this);
		}
	
	

	/* (non-Javadoc)
	 * @see magiccard.task.TaskController#getStatus()
	 */
	@Override
	public Status getStatus() {
		// TODO Auto-generated method stub
		if(mbMoving) return Status.RUNNING;
		else return Status.FINISHED;
	}

	/* (non-Javadoc)
	 * @see magiccard.task.TaskController#tick()
	 */
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
		if (!mbMoving)
			return;

		if (meOperation_status == Status.GOBACK)
		{
			moMoveController.tick();
			if (moMoveController.getStatus() != Status.RUNNING) {
				mbMoving = false;
				moComm.stop(); //stop roomba
				if (!mbInternal){
					//play music
					moComm.playSong();
				}
				this.setChanged();
      			notifyObservers(TaskManager.Signal.FINISHED);
			}
			return;
		}
		
	}

	@Override
	public void update(Observable ctl, Object arg1) {
		// TODO Auto-generated method stub
		System.out.println("this is the collect update");
		if (arg1 == TaskManager.Signal.FINISHED) {
			System.out.println("finish one!!");
			
			
    		ctl.deleteObservers();
    		subtaskSignals[miCurrentSubTask] = Signal.FINISHED;
    		miCurrentSubTask ++;
    		startSubTaskByOrder(miCurrentSubTask);
    		
    		//if all subtasks are finished
    		if(allSubTasksFinished())  
    		{
    			moComm.stop(); 		
    			System.out.println("Collect all tags!!");
    			mbMoving = true;
    			meOperation_status = Status.GOBACK;
    			
    			this.startTimer(); 
    			/*timer = new Timer();
    			timer.schedule(new Ticker(moMoveController), 100, 100);
    			status = Status.RUNNING;*/
    		}
    		  
    			
    	}
	}
	
	private void startTimer() {
		// TODO Auto-generated method stub
		timer = new Timer();
		timer.schedule(new Ticker(this), 100, 100);
		status = Status.RUNNING;
	}



	public void start()
	{
		if(moCollectTagsTask.getTagsNum()> 0){
			
			prepareTags(moCollectTagsTask.myTag);
			
		//	TaskController moveCtrl = new MoveController(this.moTsystem, this.moComm, this.miRoombaId, myCollectTags.get(0).position, null);
			TaskController moveCtrl = new MoveController(this.moTsystem, this.moComm, this.miRoombaId, moCollectTagsTask.myTag.get(0).position, null);
			moveCtrl.addObserver(this);
			moveCtrl.start();
			miCurrentSubTask = 0;
			subtaskSignals[miCurrentSubTask] = Signal.STARTED;
		}
	}
	

	private void prepareTags(ArrayList<Tag> myTag) {
		
		System.out.println("before change ");
		for (int i=0;i<myTag.size();i++)
		{
			System.out.println(myTag.get(i));
		}
		// TODO Auto-generated method stub
		

		for (int i=0;i< myTag.size();i++)
		{
			Tag tempTag1 = myTag.get(i);
			myCollectTags.add(new Tag(tempTag1.type,tempTag1.id, tempTag1.name, tempTag1.found, new Vector2D(positiveLengthOfRoomba, myTag.get(i).position), myTag.get(i).orientation));

			myCollectTags.add(new Tag(tempTag1.type,tempTag1.id, tempTag1.name, tempTag1.found, new Vector2D(negativeLengthOfRoomba, myTag.get(i).position), myTag.get(i).orientation));
			  
		}
		myTag=(ArrayList)myCollectTags.clone();
		
		System.out.println("After change ");
		for (int i=0;i<myTag.size();i++)
		{
			System.out.println(myCollectTags.get(i));
		} 
		
	}



	private boolean allSubTasksFinished(){
		if(subtaskSignals == null || subtaskSignals.length == 0) return true;
		else
		{
			for(int i=0; i< subtaskSignals.length; i++)
			{
				System.out.println(subtaskSignals[i].toString());
				if(!(subtaskSignals[i].name().equals("FINISHED")))
					{
						System.out.println("tasksignal False!!");
						return false;
					}
			}//end of for loop
		return true;
		}
	}
	
	public void startSubTaskByOrder(int index){  
		if(myCollectTags.size()<= index)    
    		return;
    	else{
    		TaskController moveCtrl = new MoveController(this.moTsystem, this.moComm, this.miRoombaId, myCollectTags.get(index).position, null);
			moveCtrl.addObserver(this);
			moveCtrl.start();
    		miCurrentSubTask = index;
    		subtaskSignals[miCurrentSubTask] = TaskManager.Signal.STARTED;
    	}//end of else
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

}
