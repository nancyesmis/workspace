import java.io.*;


public class DataGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		
		int num_items = 15;
		
		//Paper columns
		String paperTitle 		= null;
		String paperAuthor		= null;
		String paperConference  = null;
		String paperAbstract    = null;
		String paperAnalysis    = null;
		
		//CitedInfo columns
		String sourcePaper = null;
		String targetPaper = null;
		
		// ContextualInfo
		int Contextual_Info_ID   = 0;
		String keywords          = null;
		String keySentences      = null;
		int    publication_year  = 1986;
		String author            = null;
		
		//refInfo ����Ϣ
		int refPos = 0;
		String title  = null;
		String Contextual_Info = null;
		
		String sourcePaperTitle = null;
		String targetPaperTitle = null;
		
		//��Ҫ����paper����Ϣ��Ȼ������Citeinfo��Ϣ���ٿ���refInfo�����Ϣ���룬��󵥶�����һЩContextualInfo����Ϣ
		while (num_items != 0){
			
			Contextual_Info_ID += 2;
			sourcePaperTitle = paperTitle       = "Magic Cards : A Paper Tag Interface for Implicit Robot Control" + Contextual_Info_ID;
			paperAuthor      = "Zhao Shengdong" + Contextual_Info_ID;
			paperConference  = "CHI" + Contextual_Info_ID;
			publication_year += Contextual_Info_ID;
			paperAbstract   = "Typical Human Robot Interaction (HRI) assumes that the user explicitly interacts with robots.[ " + Contextual_Info_ID + " ]"; 
			paperAnalysis   = "Content that shows what other people write to cite the suggested paper";
			
			//���paper�Ĳ��빤��
			String insert1  = "insert into paper values('" + paperTitle + "', '" + paperAuthor + "', '" + paperConference + " ', '" + publication_year + " ', '" + paperAbstract+"', '" + paperAnalysis + "');";
			insert1 += "\n";
	    	
			try{
	    		writeToFile(insert1);
	    	}catch(Exception ex)
	    	{
	    		System.out.println("Exception Captured");
	    	}
	    	
			int temp = Contextual_Info_ID + 1 ;
			targetPaperTitle = paperTitle       = "Magic Cards : A Paper Tag Interface for Implicit Robot Control" + temp ;
			paperAuthor      = "Zhao Shengdong" + temp;
			paperConference  = "CHI" + temp;
			publication_year += temp;
			paperAbstract   = "Typical Human Robot Interaction (HRI) assumes that the user explicitly interacts with robots.[" + Contextual_Info_ID + "]"; 
			paperAnalysis   = "Content that shows what other people write to cite the suggested paper";
			
			insert1 = "insert into paper values('" + paperTitle + "', '" + paperAuthor + "', '" + paperConference + " ', '" + publication_year + " ', '" + paperAbstract+"', '" + paperAnalysis + "');";
			insert1 += "\n";
	    	try{
	    		writeToFile(insert1);
	    	}catch(Exception ex)
	    	{
	    		System.out.println("Exception Captured");
	    	}
			
			//����ContextualInfo�ı���Ϣ
			keywords = "magic card[ " + Contextual_Info_ID + " ]";
			keySentences = "provide the intuitive way to help user interact with robots";
			publication_year = 1987 + Contextual_Info_ID;
			author = "ZHao Shengdong[ " + Contextual_Info_ID + " ]";
			
			//���Contextual��Ĳ������
			String insert2 = "insert into Contextual_Info values(" + Contextual_Info_ID + ", '" + keywords + "', '" + keySentences + " ', '" + publication_year + " ', '" + paperConference + "', '" + author + "');";
			insert2 += "\n";
			
			try{
	    		writeToFile(insert2);
	    	}catch(Exception ex)
	    	{
	    		System.out.println("Exception Captured");
	    	}
			//ȥ����refInfo����Ϣ
			refPos = Contextual_Info_ID + 50;
			title  = paperTitle;
			
			String insert3 = "insert into RefInfo values(" + refPos + ", '" + title + "', " + Contextual_Info_ID + ");";
			insert3 += "\n";
			
	    	try{
	    		writeToFile(insert3);
	    	}catch(Exception ex)
	    	{
	    		System.out.println("Exception Captured");
	    	}
	    	
			//���refInfo�ı�������
			String insert4 = "insert into CitedInfo values('" + sourcePaperTitle + "', '" + targetPaperTitle + "');";
			insert4 += "\n";
			
	    	try{
	    		writeToFile(insert4);
	    	}catch(Exception ex)
	    	{
	    		System.out.println("Exception Captured");
	    	}
	    	
			num_items--;
		}
	}
	
    public static void writeToFile( String text)throws IOException{
    	
    	OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("data.sql", true));
    	osw.append(text);
    	osw.flush();
    	osw.close();
    }

}
