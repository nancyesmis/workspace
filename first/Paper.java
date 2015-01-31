import java.util.Vector;

public class Paper {
	
	private String paperID;
	private String BibInfo;
	private String title;
	private String publish_place;
	private String year;
	private String authors = "";
	private Vector citationList = new Vector();
	
	public Paper(){
		
	}
	public Paper(String newPaperID, String newBibInfo, String newTitle, String newPublish_place, String newYear, String authorsList, Vector newCitationList){
		paperID = newPaperID;
		BibInfo = newBibInfo;
		title   = newTitle;
		publish_place = newPublish_place;
		year = newYear;
		authors = authorsList;
		citationList = newCitationList;
	}
	
	public void setPaperID(String newPaperID){
		paperID = newPaperID;
	}
	public String getPaperID(){
		return paperID;
	}
	public void setBibInfo(String newBibInfo){
		BibInfo = newBibInfo;
	}
	public String getBibInfo(){
		return BibInfo;
	}
	public void setTitle(String newTitle){
		title = newTitle;
	}
	public String getTitle(){
		return title;
	}
	public void setPublish_place(String newPublish_place){
		publish_place = newPublish_place;
	}
	public String getPublish_place(){
		return publish_place;
	}
	public void setYear(String newYear){
		year = newYear;
	}
	public String getYear(){
		return year;
	}
	public void setAuthors(String authorsList){
		authors = authorsList;
	}
	public String getAuthors(){
		return authors;
	}
	public void setCitationList(Vector newCitationList){
		citationList = newCitationList;
	}
	public Vector getCitationList(){
		return citationList;
	}


}
