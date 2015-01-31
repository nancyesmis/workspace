package magiccard.task;

/**
 * General descriptive class for housework tasks. The task will be passed to robot controller for 
 * execution 
 * @author Shengdong Zhao
 *
 */
public class HouseworkTask implements Cloneable {

	public enum Type {COLLECT, VACUUMROOM, VACUUMSPOT, PUSH, MOVE, BRING, SEND, COMPOUND, PRINT};
	
	/* public static enum Type {
	        VacuumRoom("VacuumRoom"),
			VacuumMore("VacuumMore"),
			Push("Push"),
			Move("Move"), 
			Bring("Bring"),
			Send("Send");

	        private String name;
	        private Type(String name) {
	            this.name = name;
	        }

	        public String toString() {
	            return name;
	        }
	    }*/
	
	//public enum ORDER {DO_1ST, DO_2ND, DO_3RD, DO_4TH, DO_5TH, DO_6TH, NO_ORDER}; 
	public static int DO_1ST = 0;
	public static int DO_2ND = 1;
	public static int DO_3RD = 2;
	public static int DO_4TH = 3;
	public static int DO_5TH = 4;
	public static int DO_6TH = 5;
	public static int NO_ORDER = 6;
	
	//===============================
	// Fields 
	//===============================
	private TaskController moController;
	private String msName;
	private Type miType;
	private int miHour, miMinute, miSecond;
	private int miOrder;
	private boolean mbFinished;
	public boolean avoidFlag = false;

	//================================
	// Getter and setter methods
	//=================================
	
	/**
	 * @return the avoidFlag
	 */
	public boolean isAvoidFlag() {
		return avoidFlag;
	}


	/**
	 * @param avoidFlag the avoidFlag to set
	 */
	public void setAvoidFlag(boolean avoidFlag) {
		this.avoidFlag = avoidFlag;
	}


	public TaskController getController() {
		return moController;
	}


	public void setController(TaskController moController) {
		this.moController = moController;
	}


	public String getName() {
		return msName;
	}


	public void setName(String msName) {
		this.msName = msName;
	}


	public Type getType() {
		return miType;
	}


	public void setType(Type miType) {
		this.miType = miType;
	}


	public int getHour() {
		return miHour;
	}


	public void setHour(int miHour) {
		this.miHour = miHour;
	}


	public int getMinute() {
		return miMinute;
	}


	public void setMinute(int miMinute) {
		this.miMinute = miMinute;
	}


	public int getSecond() {
		return miSecond;
	}


	public void setSecond(int miSecond) {
		this.miSecond = miSecond;
	}


	public int getOrder() {
		return miOrder;
	}


	public void setOrder(int miOrder) {
		this.miOrder = miOrder;
	}


	public boolean isFinished() {
		return mbFinished;
	}


	public void setFinished(boolean mbFinished) {
		this.mbFinished = mbFinished;
	}

	//==============================
	// toString method
	//==============================
	

	public String toString(){
		
		return "";
	}
}
