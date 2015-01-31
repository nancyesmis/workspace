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


public class C_Competition {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost("www.cc98.org", 80,
		"http");
		client
		.getParams()
		.setCookiePolicy(
				org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setContentCharset("UTF-8");
		
			Random ranNumber = new Random();
			String[] content = {"dd","upup","d","up","go"};
			while(true){
				PostMethod pm = new PostMethod("http://www.cc98.org/SaveReAnnounce.asp?method=fastreply&BoardID=100");
				pm.setRequestHeader("Connection", "Keep-Alive");
				pm.setRequestHeader("Referer","http://www.cc98.org/dispbbs.asp?BoardID=100&id=3006562&page=2&replyID=3006562&star=48"); 
				pm.setRequestHeader("Cookie", "BoardList=BoardID=Show; aspsky=username=%E8%93%9D%E5%AE%A2&usercookies=3&userid=189190&useranony=&userhidden=2&password=43ddf159ed36ad0f; ASPSESSIONIDCSBTBQQS=HKGMBGFCDMPCAPMFAKHFFFGK");
				
				NameValuePair[] data ={		
					new NameValuePair("followup","3006562"),
					new NameValuePair("RootID","3006562"),
					new NameValuePair("TotalUseTable","bbs11"),
					new NameValuePair("passwd","43ddf159ed36ad0f"),
					new NameValuePair("UserName","蓝客"),
					new NameValuePair("Content",content[Math.abs(ranNumber.nextInt()%5)])
				};
				pm.setRequestBody(data);
				try {
			       int res = 	client.executeMethod(pm);
			       if(res == HttpStatus.SC_OK){
			    	  String temp = pm.getResponseBodyAsString();
			    	  System.out.println("yes"+temp);
			       }
			       else 
			    	   System.out.println("error: not connected");
			       Thread.sleep(10000);
			       while(true){
			    	   pm.setRequestHeader("Connection", "Keep-Alive");
			    	   GetMethod gm = new GetMethod("http://www.cc98.org/list.asp?boardid=100&page=1");
			    	   
			    	   gm.setRequestHeader("Referer", "http://www.cc98.org/dispbbs.asp?BoardID=100&id=3006562&page=2&replyID=3006562&star=48"); 
				
			    	   
			    	   gm.setRequestHeader("Cookie","BoardList=BoardID=Show; aspsky=username=%E8%93%9D%E5%AE%A2&usercookies=3&userid=189190&useranony=&userhidden=2&password=43ddf159ed36ad0f; ASPSESSIONIDCSBTBQQS=HKGMBGFCDMPCAPMFAKHFFFGK");
						
			    	   int secres = client.executeMethod(gm);
					   if(secres == HttpStatus.SC_OK){
						   String contt = gm.getResponseBodyAsString();
						   int indexof =  contt.indexOf("计算机学院与软件学院C语言比赛");
						   System.out.println(indexof);
						   if(indexof>36000 || indexof < 0 )
							   break;
					   }
					   Thread.sleep(10000);
			       }
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

