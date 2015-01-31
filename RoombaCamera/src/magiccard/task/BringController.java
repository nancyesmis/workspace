/**
 * 
 */
package magiccard.task;

import roomba.MagicCardRoombaController;
import magiccard.Tag;
import magiccard.TrackingSystem;
import magiccard.task.TaskController.Status;

/**
 * @author Shengdong Zhao
 *
 */
public class BringController extends TaskController {

	MoveController moGobackMoveController;
	PushController moPushController;
	PushTask moPushtask;
	BringTask moBringtask;
	
	/**
	 * Constructor
	 */
	public BringController(TrackingSystem tsystem, MagicCardRoombaController comm, int roombaid, BringTask task, boolean internal, TaskController parent){
		moParent = parent;
		moTsystem = tsystem;
		moComm = comm;
		miRoombaId = roombaid;
		moBringtask = task.createCopy();
		status = Status.RUNNING;
		mbInternal = internal;
		moParent = parent;
		
		moPushtask = new PushTask();
		
		Tag t = tsystem.getTag(comm.getRoombaId());
		moGobackMoveController = new MoveController(tsystem, comm, roombaid, t.position, this);
	
		moPushtask.objectID = moBringtask.itemList.get(0).intValue();
		moBringtask.itemList.remove(0); //what does pop_front do?
		moPushtask.goal = moBringtask.goal; //what does front do?
		
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
			moPushController.tick();
			if (moPushController.getStatus() == Status.FINISHED) {
				moComm.playSong();
				

				if (moBringtask.itemList.isEmpty()) {
					status = Status.GOBACK;
				} else {
					moPushtask.objectID = moBringtask.itemList.get(0);
					moBringtask.itemList.remove(0);
					moPushController = new PushController(moTsystem, moComm, miRoombaId, moPushtask, true, this);
				}
			}
		}//else if
		else if(status == Status.GOBACK){
			moGobackMoveController.tick();
			if (moGobackMoveController.getStatus() == Status.FINISHED) {
				moComm.playSong();
				moBringtask.setFinished(true);
				status = Status.FINISHED;
			}
		}//end of else if
	}//end of tick

}//end of class
