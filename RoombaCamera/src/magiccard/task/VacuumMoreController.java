package magiccard.task;

import magiccard.Tag;
import magiccard.TrackingSystem;
import magiccard.task.TaskController.Status;
import roomba.MagicCardRoombaController;

/**
 * 
 * @author Shengdong Zhao
 *
 */
public class VacuumMoreController extends TaskController {
	
	VacuumMoreTask moVacuumMoreTask;
	long firstTime=0;
	long currentTime;
	MoveController moMoveController;
	Status meOperation_status;
	boolean mbMoving;
	
	public VacuumMoreController(TrackingSystem tsystem, MagicCardRoombaController roombacomm, int VACUUM_ROOMBA_ID, VacuumMoreTask vacuumMoreTask, boolean internal, TaskController parent){
		
		moTsystem = tsystem;
		moComm = roombacomm;
		miRoombaId = VACUUM_ROOMBA_ID;
		meOperation_status = Status.NOSTATUS;
		mbMoving = true;
		moParent = parent; 
		moVacuumMoreTask = vacuumMoreTask.createCopy();
		
		
		moMoveController = new MoveController(moTsystem, moComm, miRoombaId, moVacuumMoreTask.spot, this);
	}

	@Override
	public Status getStatus() {
		// TODO Auto-generated method stub
		if(mbMoving) return Status.RUNNING;
		else return Status.FINISHED;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
			if(!mbMoving)
				return;
			if (moVacuumMoreTask.isAvoidFlag())
				moMoveController.avoidFlag = true;
			moMoveController.tick();
			if (moMoveController.getStatus() != Status.RUNNING) 
			{	
				if (moVacuumMoreTask.firstTimeFlag)
				{
					firstTime=System.currentTimeMillis(); 
					moVacuumMoreTask.firstTimeFlag = false;
				}
				currentTime = System.currentTimeMillis(); 
				
				long distance = currentTime - firstTime;
				
				if(distance > moVacuumMoreTask.timeLimit){
					
					//moMoveController.stop();
					this.status = Status.FINISHED;
					mbMoving = false;
					System.out.println("the time is out, vacuum is over.");
					moComm.control(); //need to stop the particular roomba
					return;
				}
				else 
				{
					moComm.pause(40);
					moComm.spot();
					System.out.println(" spot thread is working!");
					
				}
			}
		}

}
