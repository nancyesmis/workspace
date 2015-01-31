
public class ReferenceDataUnit {
	
	private int startPos = 0;
	private int endPos   = 0;
	
	public ReferenceDataUnit(int newStartPos, int newEndPos){
		startPos = newStartPos;
		endPos   = newEndPos;
	}
	public int getStartPos(){
		return startPos;
	}
	public int getEndPos(){
		return endPos;
	}

}
