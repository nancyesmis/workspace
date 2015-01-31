import java.util.*;

//这里面用到的信息都是用由用户产生的...通过传输这些数据给recommending algorithm来返回suggest的结果
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
		//把上面保存好的用户指定好的一些关键字，作者，包括一些会议名称，时间信息利用xml格式用webservice的方式发送给backend处理，并通过该函数返回结果
		//ReadXMLFile();
		//SendXMLFile();
		return "";
	}
}
