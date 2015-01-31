/**
 * 
 */
package magiccard.task;


import roomba.MagicCardRoombaController;
import roombacomm.RoombaCommSerial;
import magiccard.G;
import magiccard.Tag;
import magiccard.TrackingSystem;
import magiccard.Vector2D;
import magiccard.task.TaskController.Status;

/**
 * @author Shengdong Zhao
 *
 */
public class MoveController extends TaskController{
	
	Vector2D moGoal;
	Vector2D moTargetOrientation;
	int miDuration;
	Status meOperation_status;
	int miCount;
	boolean mbMoving;
	int radius = 500 ;
	int length = 500 ;
	
	enum MoveStatus {
		Stop,
		SpinLeft,
		SpinRight,
		GoForward,
		GoBackward,
	}
	MoveStatus meMove_status;
	
	public MoveController(TrackingSystem tsystem, MagicCardRoombaController comm, int roombaid, Vector2D goal, TaskController parent){
		moTsystem = tsystem;
		moComm = comm;
		miRoombaId = roombaid;
		moGoal = goal;
		miDuration = 10;
		meOperation_status = Status.NOSTATUS;
		miCount = 0;
		mbMoving = true;
		moParent = parent;
		
		meMove_status = MoveStatus.Stop;
	}
	
	public void setMoving(boolean myMoving)
	{
		this.mbMoving=myMoving;
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
		
		if(!mbMoving)
			return;
		
		Tag roombaTag = moTsystem.getTag(moComm.getRoombaId()); //i need to put the 
		try
		{
			if(roombaTag.found == 0)
				return;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if (miCount < miDuration) {
			miCount++;
			return;
		}
		double distance = Vector2D.distance(roombaTag.position, moGoal);
		Vector2D target_orientation = new Vector2D(roombaTag.position, moGoal);		
		double angle = Vector2D.get_angle_360(target_orientation, roombaTag.orientation);
		
		miCount = 0;
		
		/* Fix distance or angle for next tick */
		if (meMove_status == MoveStatus.GoForward)
			distance -= DISTANCE_PER_TICK;
		else if (meMove_status == MoveStatus.GoBackward)
			distance += DISTANCE_PER_TICK;
		else if (meMove_status == MoveStatus.SpinLeft)
			angle -= ANGLE_PER_TICK;
		else if (meMove_status == MoveStatus.SpinRight)
			angle += ANGLE_PER_TICK;
		
		G.debug("angle:" + angle + " distance:" + distance);
		
		if (this.avoidFlag)
			if (distance < radius+ length)
			{
				if (meMove_status == MoveStatus.GoForward)
				{
					meMove_status = MoveStatus.GoBackward;
					moComm.goBackward(length);
					miDuration = 0;
				}	
				else if (meMove_status == MoveStatus.GoBackward)
				{
					meMove_status = MoveStatus.GoForward;
					moComm.goForward(length);
					miDuration = 0;
				}
			}
		
		if(distance < ROOMBA_RADIUS){
			mbMoving = false;
			return;
		}
		
		if (angle < 20 || (360-angle) < 20) {
			meMove_status = MoveStatus.GoForward;
			moComm.goForward();
			/* If distance is long, run roughly */
			if (distance > DISTANCE_PER_TICK*3)
				miDuration = 2;
			else
				miDuration = 0;
		} else if (angle < 45) {
			meMove_status = MoveStatus.SpinLeft;
			moComm.spinLeft(15);
			miDuration = 3;
		} else if ((360-angle) < 45) {
			meMove_status = MoveStatus.SpinRight;
			moComm.spinRight(15);
			miDuration = 3;
		} else if (angle <= 180) {
			meMove_status = MoveStatus.SpinLeft;
			moComm.spinLeft();
			miDuration = 0;
		} else {
			meMove_status = MoveStatus.SpinRight;
			moComm.spinRight();
			miDuration = 0;
		}
	}

}
