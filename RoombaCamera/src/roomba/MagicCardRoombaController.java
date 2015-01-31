package roomba;
//
// RoombaController.java
//

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
import java.util.ArrayList;

import magiccard.G;

import roombacomm.RoombaCommSerial;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
public final class MagicCardRoombaController {

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	private static boolean DEBUG = true;
	private static boolean HARDWARE_HANDSHAKE = false;

	private static boolean FLUSH = false;

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	private RoombaCommSerial roombaComm;
	private boolean enabled;
	
	private int roombaID = -1; 
	
	public static enum RoombaType {GeneralPurposeRoomba, VacuumRoomba, PushRoomba, TagPickupRoomba, PrinterRoomba};
	
	
	private ArrayList<RoombaType> roombaType = new ArrayList<RoombaType>();
	
	//add by Shen
	//a roomba controller needs to be able to link to its control panel. 
	MagicCardRoombaPanel parentPanel = null;
	
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	

	
	public MagicCardRoombaController(String port) {
		enabled = setup(port, DEBUG, HARDWARE_HANDSHAKE, FLUSH);
	}

	public MagicCardRoombaController(String port, boolean debug) {
		enabled = setup(port, debug, HARDWARE_HANDSHAKE, FLUSH);
	}

	public MagicCardRoombaController(String port, boolean debug,
			boolean hardwareHandshake) {
		enabled = setup(port, debug, hardwareHandshake, FLUSH);
	}

	public MagicCardRoombaController(String port, boolean debug,
			boolean hardwareHandshake, boolean flush) {
		enabled = setup(port, debug, hardwareHandshake, flush);
	}
	
	/**
	 * Setting roomba panel
	 * @param magicCardRoombaPanelBlack
	 */
	public void setRoombaPanel(MagicCardRoombaPanel magicCardRoombaPanel){
		parentPanel = magicCardRoombaPanel;
	}
	
	

	public MagicCardRoombaPanel getRoombaPanel(){
		return parentPanel;
	}
	
	//----------------------------------------------------------------
	public void setRoombaID(int id){
		roombaID = id;
	}
	
	public int getRoombaId(){
		return roombaID;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public boolean isEnabled() {
		return enabled;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void drive(int v, int r) {
		checkEnabled();
		roombaComm.drive(v, r);
	}

	public void setSpeed(int s) {
		checkEnabled();
		roombaComm.setSpeed(s);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void clean() {
		checkEnabled();

		roombaComm.clean();
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void spot() {
		checkEnabled();

		roombaComm.spot();
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void control() {
		checkEnabled();

		roombaComm.control();
		
		G.debug("roomba control");
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void pause(int millis) {
		checkEnabled();

		roombaComm.pause(millis);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void goForward() {
		checkEnabled();

		roombaComm.goForward();
		
		G.debug("go forward");
	}
	
	public void goForward(int distance) {
		checkEnabled();

		roombaComm.goForward(distance);
		
		G.debug("go forward");
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void goBackward() {
		checkEnabled();

		roombaComm.goBackward();
		
		G.debug("go backward");
	}
	
	public void goBackward(int distance) {
		checkEnabled();

		roombaComm.goBackward(distance);
		
		G.debug("go backward");
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void spinLeft() {
		checkEnabled();

		roombaComm.spinLeft();
		
		G.debug("spinLeft");
	}
	
	public void spinLeft(int angle) {
		checkEnabled();

		roombaComm.spinLeft(angle);
		
		G.debug("spinLeft");
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void spinRight() {
		checkEnabled();

		roombaComm.spinRight();
		G.debug("spinRight");
	}

	public void spinRight(int angle) {
		checkEnabled();

		roombaComm.spinRight(angle);
		G.debug("spinRight");
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void turnLeft() {
		checkEnabled();

		roombaComm.turnLeft();
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void turnRight() {
		checkEnabled();

		roombaComm.turnRight();
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void stop() {
		checkEnabled();

		roombaComm.stop();
		G.debug("roomba stop");
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void quit() {
		//checkEnabled();

		System.out.println("Disconnecting");
		roombaComm.disconnect();

		System.out.println("Done");
	}
	
	public void playSong(){
		checkEnabled();
		
		roombaComm.playSong(1);		
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// private
	private boolean setup(String port, boolean debug,
			boolean hardwareHandshake, boolean flush) {
		int flag =0;
		if ("COM41".equals(port))
			{
				flag =1;
				System.out.println("this is the black one");
			}
		else 
			{
				flag =2;
				System.out.println("this is the white one");
			}
		roombaComm = new RoombaCommSerial(flag);
		

		roombaComm.debug = debug;
		roombaComm.waitForDSR = hardwareHandshake;
		roombaComm.flushOutput = flush;

		String portList[] = roombaComm.listPorts();
		System.out.println("Available ports:");
		for (int i = 0; i < portList.length; i++)
			System.out.println("  " + i + ": " + portList[i]);

		if (!roombaComm.connect(port)) {
			System.out.println("Couldn't connect to " + port);
			return false;
		}

		System.out.println("Roomba startup on port" + port);
		
		System.out.println("Roomba startup");
		
		roombaComm.startup();
		if (flag ==2)
		{
			System.out.println("i am white one, this is contrl ()");
			roombaComm.control();
			roombaComm.pause(30);
		}
		
		if (flag ==1)
		{
			System.out.println("i am Black one, this is safe()");
			roombaComm.safe();
			roombaComm.pause(50);			
		}
		
	

		//roombaComm.safe();
		

		System.out.println("Checking for Roomba... ");
		if (!roombaComm.updateSensors()) {
			System.out.println("No Roomba. :(  Is it turned on?");
			return false;
		}
		System.out.println("Roomba found!");

		System.out.println(roombaComm.capacity());
		System.out.println(roombaComm.charge());

		return true;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// private
	private void checkEnabled() throws IllegalStateException {
		if (!enabled)
			throw new IllegalStateException("Setup Failed!");
	}

	public ArrayList<RoombaType> getRoombaTypes() {
		return roombaType;
	}

	public void addRoombaType(RoombaType roombaType) {
		//this.roombaType = roombaType;
		if(this.roombaType.size() == 0) this.roombaType.add(roombaType);
		else{
			for(int i= 0; i< this.roombaType.size(); i++){
				if(this.roombaType.get(i) == roombaType){
					return;
				}
			}//end of fore
			this.roombaType.add(roombaType);
		}//end of else
	}//end of addRoombaType
	
	public boolean containsRoombaType(RoombaType aRoombaType){
		if(this.roombaType.size() == 0) return false;
		else{
			for(int i= 0; i< this.roombaType.size(); i++){
				if(this.roombaType.get(i) == aRoombaType){
					return true;
				}
			}//end of fore
			return false;
		}//end of else
	}//end of method

	
}
