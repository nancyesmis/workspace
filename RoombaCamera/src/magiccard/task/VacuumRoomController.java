/**
 * 
 */
package magiccard.task;

import magiccard.TrackingSystem;
import magiccard.Vector2D;
import magiccard.task.TaskController.Status;
import roomba.MagicCardRoombaController;
import roombacomm.RoombaComm;

/**
 * @author Shengdong Zhao
 *
 */
public class VacuumRoomController extends TaskController {
		

	VacuumRoomTask moVacuumTask;
	long firstTime=0;
	long currentTime;
		

	
	public VacuumRoomController(TrackingSystem tsystem, MagicCardRoombaController roombacomm, int VACUUM_ROOMBA_ID, VacuumRoomTask vacuumTask, boolean internal, TaskController parent){
		
		
		moTsystem = tsystem;
		moComm = roombacomm;
		miRoombaId = VACUUM_ROOMBA_ID;
	
		mbMoving = true;
		moParent = parent; 
		moVacuumTask= vacuumTask.createCopy();
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
		
		
		
		if (moVacuumTask.firstTimeFlag)
		{
			firstTime=System.currentTimeMillis(); 
			moVacuumTask.firstTimeFlag = false;
		}
			
		if (!mbMoving)
			return;
		
		moComm.clean();
		currentTime = System.currentTimeMillis(); 
		
		long distance = currentTime - firstTime;
		
		if(distance > moVacuumTask.timeLimit){
			this.status = Status.FINISHED;
			mbMoving = false;
			System.out.println("the clean mission complete.");
			moComm.control(); //need to stop the particular roomba
			
			return;
		}
	}
}

