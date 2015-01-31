import java.util.Vector;


public class ReferenceItem extends Paper {
	
	private String page_info;
	
	public ReferenceItem(String newPaperID, String newBibInfo, String newTitle, String newPublish_place, String newYear, String authorsList, Vector newCitationList, String newPage_info){
		super(newPaperID, newBibInfo, newTitle, newPublish_place, newYear, authorsList, newCitationList);
		page_info = newPage_info;
	}
	
	public void setPage_info(String newPage_info){
		page_info = newPage_info;
	}
	public String getPape_info(){
		return page_info;
	}
	

}
