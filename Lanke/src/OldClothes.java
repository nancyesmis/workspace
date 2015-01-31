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


public class OldClothes {

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
			String[] content = {"好衣服","d","up","go"};
			while(true){
				PostMethod pm = new PostMethod("http://www.cc98.org/SaveReAnnounce.asp?method=fastreply&BoardID=563");
				
				pm.setRequestHeader("Connection", "Keep-Alive");
				pm.setRequestHeader("Referer", "http://www.cc98.org/dispbbs.asp?boardid=563&id=2992472&star=1");
				pm.setRequestHeader("Cookie", "BoardList=BoardID=Show; ASPSESSIONIDAQBTBTQR=LJGNMJPDPOIHBIEIPFIOMACH; upNum=0; aspsky=username=%E8%93%9D%E5%AE%A2&usercookies=3&userhidden=2&password=43ddf159ed36ad0f&userid=189190&useranony=");
				NameValuePair[] data ={		
					new NameValuePair("followup","262245391"),
					new NameValuePair("Content",content[Math.abs(ranNumber.nextInt()%4)]),
					new NameValuePair("RootID","2992472"),
					new NameValuePair("star","1"),
					new NameValuePair("TotalUseTable","bbs11"),
					new NameValuePair("UserName","蓝客"),
					new NameValuePair("passwd","43ddf159ed36ad0f")
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
			    	   GetMethod gm = new GetMethod("http://www.cc98.org/list.asp?boardid=563");
			    	   gm.setRequestHeader("Referer", "http://www.cc98.org/SaveReAnnounce.asp?method=fastreply&BoardID=563");
			    	   
			    	   gm.setRequestHeader("Cookie","BoardList=BoardID=Show; ASPSESSIONIDAQBTBTQR=LJGNMJPDPOIHBIEIPFIOMACH; upNum=0; aspsky=username=%E8%93%9D%E5%AE%A2&usercookies=3&userhidden=2&password=43ddf159ed36ad0f&userid=189190&useranony=");
			    	   int secres = client.executeMethod(gm);
					   if(secres == HttpStatus.SC_OK){
						   String contt = gm.getResponseBodyAsString();
						   int indexof =  contt.indexOf("自己穿都不好看");
						   //System.out.println(indexof);
						   if(indexof>30000 || indexof < 0 )
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

