package roomba;
//
// RoombaController.java
//

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
import roombacomm.RoombaCommSerial;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
public final class RoombaController {

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	private static boolean DEBUG = false;
	private static boolean HARDWARE_HANDSHAKE = false;
	private static boolean FLUSH = false;

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	private RoombaCommSerial roombaComm;
	private boolean enabled;
	
	private int roombaID = -1; 

	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public RoombaController(String port) {
		enabled = setup(port, DEBUG, HARDWARE_HANDSHAKE, FLUSH);
	}

	public RoombaController(String port, boolean debug) {
		enabled = setup(port, debug, HARDWARE_HANDSHAKE, FLUSH);
	}

	public RoombaController(String port, boolean debug,
			boolean hardwareHandshake) {
		enabled = setup(port, debug, hardwareHandshake, FLUSH);
	}

	public RoombaController(String port, boolean debug,
			boolean hardwareHandshake, boolean flush) {
		enabled = setup(port, debug, hardwareHandshake, flush);
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
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void goBackward() {
		checkEnabled();

		roombaComm.goBackward();
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void spinLeft() {
		checkEnabled();

		roombaComm.spinLeft();
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	public void spinRight() {
		checkEnabled();

		roombaComm.spinRight();
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
		roombaComm = new RoombaCommSerial();

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
		roombaComm.startup();
		roombaComm.control();
		roombaComm.pause(30);

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
}
