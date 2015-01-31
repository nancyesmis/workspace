import java.io.IOException;
import java.util.Random;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;


public class SellClothes {

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
			String[] content = {"清仓","好衣服","d","up","go"};
			while(true){
				PostMethod pm = new PostMethod("http://www.cc98.org/master_postings.asp?action=uptopic&BoardID=569&TopicID=2545408");
				pm.setRequestHeader("Connection", "Keep-Alive");
				pm.setRequestHeader("Referer", "http://www.cc98.org/master_postings.asp?action=%E6%8F%90%E5%8D%87&BoardID=569&TopicID=2545408");
				pm.setRequestHeader("Cookie", "BoardList=BoardID=Show; aspsky=username=%E7%B1%B3%E7%A5%88&usercookies=3&userid=159686&useranony=&userhidden=2&password=b6e7a7361fd1862d; ASPSESSIONIDASBTASQQ=KHOGJGGCMFPAOHOMLCOPEGOI");
				
				NameValuePair[] data ={		
					new NameValuePair("verifynuminput","7957"),
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
			       Thread.sleep(60000);
			       while(true){
			    	   pm.setRequestHeader("Connection", "Keep-Alive");
			    	   GetMethod gm = new GetMethod("http://www.cc98.org/list.asp?boardid=569&page=1");
			    	   
			    	   gm.setRequestHeader("Referer", "http://www.cc98.org/master_postings.asp?action=%E6%8F%90%E5%8D%87&BoardID=569&TopicID=2545408");
					   
			    	   
			    	   gm.setRequestHeader("Cookie","BoardList=BoardID=Show; aspsky=username=%E7%B1%B3%E7%A5%88&usercookies=3&userid=159686&useranony=&userhidden=2&password=b6e7a7361fd1862d; ASPSESSIONIDASBTASQQ=KHOGJGGCMFPAOHOMLCOPEGOI");
			    	   
			    	   int secres = client.executeMethod(gm);
					   if(secres == HttpStatus.SC_OK){
						   String contt = gm.getResponseBodyAsString();
						   int indexof =  contt.indexOf("米祈衣裤屋");
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

