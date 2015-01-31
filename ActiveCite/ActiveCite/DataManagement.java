import java.util.Collection;  
import java.util.Enumeration;  
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;    
import java.util.Set;  
import java.util.Vector;

public class DataManagement {
	
	private static Vector referencePoint = new Vector();
	private static Vector position       = new Vector();
	private static Vector ReferenceList  = new Vector();
	private static String paperTitle     = "";
	private static Vector referencePos   = new Vector();
	private static String text = "";
	
    //后来加上的用于保存suggested paper的信息的！
	private static Hashtable suggestedPaper = new Hashtable();
	
	
	public void setRefencePoint(Vector newReferencePoint){
		referencePoint = newReferencePoint;
	}
	public Vector getReferencePoint(){
		return referencePoint;
	}
	public void setPosition(Vector newPosition){
		position = newPosition;
	}
	public Vector getPosition(){
		return position;
	}
	public void setRerenceList(Vector newReferenceList){
		ReferenceList = newReferenceList;	
	}
	public Vector getReferenceList(){
		return ReferenceList;
	}
	public void setSuggestedPaper(Hashtable newSuggestedPaper){
		suggestedPaper = newSuggestedPaper;
	}
	public Hashtable getSuggestedPaper(){
		return suggestedPaper;
	}
	public void setPaperTitle(String newPaperTitle){
		
		paperTitle = newPaperTitle;
	}
	public String getPaperTitle(){
		
		return paperTitle;
	}
	public Vector getReferencePos(){
		return referencePos;
	}
	public void setText(String newText){
		text = newText;
	}
	public String getText(){
		return text;
	}
	
}
