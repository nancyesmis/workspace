/**
 * 
 */
package magiccard.task;

import java.util.Observable;
import java.util.Observer;

import magiccard.Tag;
import magiccard.TaskManager;
import magiccard.TrackingSystem;
import magiccard.Vector2D;
import magiccard.TaskManager.Signal;
import magiccard.task.TaskController.Status;
import roomba.MagicCardRoombaController;
import roombacomm.RoombaCommSerial;

/**
 * @author Shengdong Zhao
 *
 */
public class PrintCompoundTagController extends TaskController{

	
	Vector2D moGoal;
	
	Status meOperation_status;
	int miCount;
	
	int miDuration;
	double mdCargo_radius;
	
	PrintCompoundTagTask moPrintTask;
	MoveController moMoveController;
	
	Vector2D backGoal;
	
	private int tickCount = 0;
	private int countLimit = 100;
	
	private boolean startTickCount = false;
	private boolean printed = false;
	
	
	public PrintCompoundTagController(TrackingSystem tsystem, MagicCardRoombaController comm, int roombaid, PrintCompoundTagTask PTask, boolean internal, TaskController parent){
		moTsystem = tsystem;
		moComm = comm;
		miRoombaId = roombaid;
		meOperation_status = Status.NOSTATUS;
		miCount = 0;
		mbMoving = true;
		mbInternal = false;
		miDuration = 10;
		
		meOperation_status = Status.RUNNING;
		
		moPrintTask = PTask;
		
		moGoal = moPrintTask.goal;
		backGoal = moPrintTask.originalPosition;
		
		moParent = parent;
		moMoveController = new MoveController(moTsystem, moComm, miRoombaId, moGoal, this);
	}


	/* (non-Javadoc)
	 * @see magiccard.task.TaskController#getStatus()
	 */
	@Override
	public Status getStatus() {
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

		if (meOperation_status == Status.RUNNING)
		{
			moMoveController.tick();
			if (moMoveController.getStatus() != Status.RUNNING) {
				moComm.stop(); //stop roomba
				if (!mbInternal){
					//play music
					moComm.playSong();
				}
				if(!printed){
					TaskManager.printMemorizedTag(moPrintTask.idToBePrinted);
					printed = true;
					startTickCount = true;
				}//end of if
			}//end of outer if
		}//end of outer outer if
		else if(meOperation_status == Status.GOBACK)
		{
			moMoveController.tick();
			if (moMoveController.getStatus() != Status.RUNNING) {
				moComm.stop(); //stop roomba
				if (!mbInternal){
					//play music
					moComm.playSong();
				}
				mbMoving = false;
				meOperation_status = Status.FINISHED;
				this.notifyObservers(Signal.FINISHED);
			}
		}
		
		if(startTickCount){
			if(tickCount < countLimit)
				tickCount++;
			else{
				meOperation_status = Status.GOBACK;
				moMoveController = new MoveController(moTsystem, moComm, miRoombaId, backGoal, this);
			}
		}

	}//end of method

}
