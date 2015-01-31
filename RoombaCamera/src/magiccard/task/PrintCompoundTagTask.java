package magiccard.task;

import magiccard.Vector2D;

public class PrintCompoundTagTask extends HouseworkTask implements Cloneable {
	int idToBePrinted;
	Vector2D goal;
	Vector2D originalPosition;
	public String toString(){
		return "Print " + idToBePrinted + " at " + goal.toString();
	}
}
