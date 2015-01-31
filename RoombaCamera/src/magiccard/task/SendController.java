/**
 * 
 */
package magiccard.task;

import roomba.MagicCardRoombaController;
import roombacomm.RoombaCommSerial;
import magiccard.Tag;
import magiccard.TrackingSystem;
import magiccard.task.TaskController.Status;

/**
 * @author Shengdong Zhao
 *
 */
public class SendController extends TaskController{
	
	MoveController moGobackMoveController;
	PushController moPushController;
	PushTask moPushtask;
	SendTask moSendtask;
	
	public SendController(TrackingSystem tsystem, MagicCardRoombaController comm, int roombaid, SendTask task, boolean internal, TaskController parentController){
		Tag t = tsystem.getTag(comm.getRoombaId());
		moComm = comm;
		moTsystem = tsystem;
		moGobackMoveController = new MoveController(tsystem, comm, roombaid, t.position, this);
		miRoombaId = roombaid;
		moSendtask =  task.createCopy();
	
		
		moPushtask = new PushTask();
		
		moPushtask.objectID = moSendtask.objectID;
		moPushtask.goal = moSendtask.positionList.get(0); //what does front do?
		moSendtask.positionList.remove(0); //what does pop_front do?
		mbInternal = internal;
		moParent = parentController;
		moPushController = new PushController(tsystem, comm, roombaid, moPushtask, true, this);
	}
	
	/* (non-Javadoc)
	 * @see magiccard.task.TaskController#getStatus()
	 */
	@Override
	public Status getStatus() {
		if(status != Status.FINISHED) return Status.RUNNING;
		else return Status.FINISHED;
	}

	/* (non-Javadoc)
	 * @see magiccard.task.TaskController#tick()
	 */
	@Override
	public void tick() {
		if(status == Status.FINISHED){
		}
		else if(status == Status.RUNNING){
			moPushController.execTick();
			if (moPushController.getStatus() == Status.FINISHED) {
				//delete push;
				moComm.playSong();

				if (moSendtask.positionList.isEmpty()) {
					status = Status.GOBACK;
				} else {
					
					//it seems to be a stack here, I need to implement this as a stack? 
					moPushtask.goal = moSendtask.positionList.get(0); //the original code here is front()
					moSendtask.positionList.remove(0);
					moPushController = new PushController(moTsystem, moComm, miRoombaId, moPushtask, true, this);
				}
			}
		}//end of else if
		else if(status  == Status.GOBACK){
			moGobackMoveController.execTick();// changed by smh
			if (moGobackMoveController.getStatus() == Status.FINISHED) {
				moComm.playSong();
				moSendtask.setFinished(true);
				status = Status.FINISHED;
			}
		}//end of else if
	}//end of tick method

}
