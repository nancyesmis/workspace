/**
 * 
 */
package magiccard.task;

import magiccard.Tag;
import magiccard.TrackingSystem;
import magiccard.Vector2D;
import magiccard.task.TaskController.Status;
import roomba.MagicCardRoombaController;
import roombacomm.RoombaCommSerial;

/**
 * @author Shengdong Zhao
 *
 */
public class PushController extends TaskController{

	
	int miCargoId;
	Vector2D moGoal;
	Vector2D moTargetOrientation;
	
	int miDuration;
	Status meOperation_status;
	int miCount;
	Vector2D backGoal= new Vector2D(200, 450);
	double mdCargo_radius= 40;
	
	PushTask moPushTask;
	MoveController moMoveController;
	
	
	public PushController(TrackingSystem tsystem, MagicCardRoombaController comm, int roombaid, PushTask pushTask, boolean internal, TaskController parent){
		moTsystem = tsystem;
		moComm = comm;
		miRoombaId = roombaid;
		miDuration = 10;
		meOperation_status = Status.NOSTATUS;
		miCount = 0;
		mbMoving = true;
		mbInternal = false;
		
		moPushTask = pushTask.createCopy();
		miCargoId = moPushTask.objectID;
		
		moGoal = moPushTask.goal;
		
		moParent = parent;
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

		if (meOperation_status == Status.GOBACK)
		{
		
			/*
			System.out.println(" the task is finished!");
			moComm.playSong();
			moPushTask.setFinished(true);
			status = Status.FINISHED;
			//
			return;*/
			moMoveController.tick();
			if (moMoveController.getStatus() != Status.RUNNING) {
				mbMoving = false;
				moComm.stop(); //stop roomba
				if (!mbInternal){
					//play music
					moComm.playSong();
				}
				moPushTask.setFinished(true);
			}
			return;
		}

		//get the roomba tag
		Tag roombaTag = moTsystem.getTag(moComm.getRoombaId());
		
		//get the cargo tag
		Tag cargoTag  = moTsystem.getTag(miCargoId);		

		if(roombaTag.found == 0)
			return;
		
		double distance = Vector2D.distance(cargoTag.position, moGoal);
		if (distance < ROOMBA_RADIUS + mdCargo_radius) {
			if (mbInternal) {
				mbMoving = false;
			} else {
				moMoveController = new MoveController(moTsystem, moComm, miRoombaId, backGoal, this);
				
				meOperation_status = Status.GOBACK;
			}
			return;
		}

		if (miCount++ > miDuration)
			return;
		
		miCount = 0;
		//stop the roomba here
		moComm.stop();

		if (meOperation_status == Status.MOVING) {
			meOperation_status = Status.PAUSED;
			miDuration = PAUSE_LENGTH;
			return;
		}

		Vector2D behind_cargo = getBehindCargo(cargoTag.position, moGoal);
		if (Vector2D.distance(roombaTag.position, behind_cargo) > ROOMBA_RADIUS){
			// approach to behind cargo
			approach(roombaTag, behind_cargo, cargoTag.position);
		}
		else
		{
			// approach to goal
			approach(roombaTag, moGoal);
		}

	}
	
	public void approach(Tag roombaTag, Vector2D goal_position){
		Vector2D target_orientation = new Vector2D(roombaTag.position, moGoal);
		
		double distance = Vector2D.distance(roombaTag.position, moGoal);
		
		double angle = Vector2D.get_angle_360(target_orientation, roombaTag.orientation);
		
		if (angle < ANGLE_PER_TICK || angle > 360-ANGLE_PER_TICK){
			moComm.goForward();
			miDuration = (int)(1+ (distance-(GOAL_RADIUS + CARGO_RADIUS)) / DISTANCE_PER_TICK);
		}
		else if (angle > 180-ANGLE_PER_TICK && angle < 180+ANGLE_PER_TICK){
			moComm.goBackward();
			miDuration = (int)(1+ (distance-(GOAL_RADIUS + CARGO_RADIUS)) / DISTANCE_PER_TICK);
		}
		else if (angle < 360 && angle > 270){
			moComm.spinLeft();
			miDuration = (int)( (360-angle) / ANGLE_PER_TICK);
		}
		else if (angle > 90 && angle < 180){
			moComm.spinLeft();
			miDuration = (int)( (180-angle) / ANGLE_PER_TICK);
		}
		else if (angle > 0 && angle < 90 ){
			moComm.spinRight();
			miDuration = (int)( angle / ANGLE_PER_TICK);
		}		
		else if (angle >180 && angle < 270){
			moComm.spinRight();
			miDuration = (int)( (angle-180) / ANGLE_PER_TICK);
		}		
		meOperation_status = Status.MOVING;
	}
	
	public void approach(Tag roombaTag, Vector2D goal_position, Vector2D obstacle){
			
		Vector2D target_orientation = getVector(roombaTag.position, goal_position, obstacle);
		
		double angle = 360 - Vector2D.get_angle_360(target_orientation, roombaTag.orientation);
		if (angle < ANGLE_PER_TICK || angle > 360-ANGLE_PER_TICK){
			moComm.goForward();
			miDuration = 1;
		}
		else if (angle > 180-ANGLE_PER_TICK && angle < 180+ANGLE_PER_TICK){
			moComm.goBackward();
			miDuration = 1;
		}
		else if (angle < 360 && angle > 270){
			moComm.spinLeft();
			miDuration = (int)( (360-angle) / ANGLE_PER_TICK);
		}
		else if (angle > 90 && angle < 180){
			moComm.spinLeft();
			miDuration = (int)( (180-angle) / ANGLE_PER_TICK);
		}
		else if (angle > 0 && angle < 90 ){
			moComm.spinRight();
			miDuration = (int)( angle / ANGLE_PER_TICK);
		}		
		else if (angle >180 && angle < 270){
			moComm.spinRight();
			miDuration = (int)( (angle-180) / ANGLE_PER_TICK);
		}		
		meOperation_status = Status.MOVING;
		
	}
	
	public Vector2D getBehindCargo(Vector2D cargo_position, Vector2D goal){
		Vector2D cargo_to_goal = new Vector2D(cargo_position, goal);
		cargo_to_goal.normalize();
		
		Vector2D behind_cargo = Vector2D.subtract(cargo_position, Vector2D.multiply(cargo_to_goal, 
				ROOMBA_RADIUS+mdCargo_radius)); 
		return behind_cargo;
	}
	
	static final double REPULSION_RADIUS = 160.0;
	static final double REPULSION_STRENGTH  = 2.0;

	static Vector2D getVector(Vector2D position, Vector2D goal, Vector2D cargo){
		Vector2D attraction_force = new Vector2D(position, goal);
		attraction_force.normalize();
		
		double d = Vector2D.distance(cargo, position);
		if (d < REPULSION_RADIUS){
			Vector2D repulsion_force = new Vector2D(cargo, position);
			repulsion_force.normalize();
			repulsion_force.multiply(REPULSION_STRENGTH * (REPULSION_RADIUS - d) / REPULSION_RADIUS );
			
			attraction_force.add(repulsion_force);
		}	
		
		attraction_force.normalize();
		return attraction_force;
	}


}
