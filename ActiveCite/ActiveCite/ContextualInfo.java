import java.util.*;

//�������õ�����Ϣ���������û�������...ͨ��������Щ���ݸ�recommending algorithm������suggest�Ľ��
public class ContextualInfo {
	
	private String authorList ;
	private String year;
	private String conference ;
	private String keySentence ;
	private String keywords ;
	
	public ContextualInfo(){
		authorList = "";
		year       = "";
		conference = "";
		keySentence= "";
		keywords   = "";
	}
	public ContextualInfo(String newAuthorList, String newYear, String newConference, String newKeySentence, String newKeywords){
		authorList = newAuthorList;
		year       = newYear;
		conference = newConference;
		keySentence = newKeySentence;
		keywords    = newKeywords;
	}
	public String getKeywords(){
		return keywords;
	}
	public String getYear(){
		return year;
	}
	public String getConference(){
		return conference;
	}
	public String getKeySentence(){
		return keySentence;
	}
	public String getAuthor(){
		return authorList;
	}
	public String getSuggestedPaper(){
		//�����汣��õ��û�ָ���õ�һЩ�ؼ��֣����ߣ�����һЩ�������ƣ�ʱ����Ϣ����xml��ʽ��webservice�ķ�ʽ���͸�backend������ͨ���ú������ؽ��
		//ReadXMLFile();
		//SendXMLFile();
		return "";
	}
}
