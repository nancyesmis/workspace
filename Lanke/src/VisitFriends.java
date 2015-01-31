import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;


public class VisitFriends {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pattern p = Pattern.compile("2[0-9]{8}");
		String content = null;
		try {
			FileInputStream fi = new FileInputStream("d:\\tt\\friend.txt");
			byte[] buf = new byte[fi.available()];
			fi.read(buf);
			content = new String(buf);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Matcher m = p.matcher(content);
		ArrayList<String> ids = new ArrayList<String>();
		while (m.find())
			ids.add(m.group());

		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost("www.m.renren.com", 80,
		"http");
		client
		.getParams()
		.setCookiePolicy(
				org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setContentCharset("UTF-8");

		System.out.println(ids.size());
		for(int i=0;i<ids.size();i++){
			String id = ids.get(i);
			GetMethod pm = new GetMethod("http://m.renren.com/profile.do?id="+id+"&sid=da203ad0740ba93b5831af1044fcc2ab6");
			pm.setRequestHeader("Referer", "http://m.renren.com/whome.do?&sid=da203ad0740ba93b5831af1044fcc2ab6&mpahap");
			pm.setRequestHeader("Cookie", "t=1e338a381bc0aa0c19e2bc7b1eca751d6; societyguester=1e338a381bc0aa0c19e2bc7b1eca751d6; id=230112086; xnsid=2a28d58; kl=kl_230112086; BeforeReferer=null; Referer=http://www.renren.com/SysHome.do; XNESSESSIONID=b4c4000e8729; jebecookies=230112086|1|1987-1-1|20|60011001|9001_; _r01_=1; _de=B24367A73FFB086E766626A722DF9EF18ED172744450A224; ap=true; __utma=151146938.2115033193.1254620067.1258373428.1258381217.23; __utmz=151146938.1258355186.21.14.utmccn=(referral)|utmcsr=home.renren.com|utmcct=/Home.do|utmcmd=referral; p=0abb46bf203e817de83171404928d2906; wpi_menu_app_tip=fix; depovince=GW; __utmb=151146938; __utmc=151146938; alxn=3974e58c2a352f3515ffe627fff7fe29fc3106392ef0b93f; mt=da203ad0740ba93b5831af1044fcc2ab6");
			
			
			try {
			       int res = 	client.executeMethod(pm);
			       if(res == HttpStatus.SC_OK){
			    	   String temp = pm.getResponseBodyAsString();
			    	   System.out.println(temp);
			    	   Pattern patt = Pattern.compile("<title>手机校内 - (.*) ((.*))</title>");
			    	   //System.out.println(temp);
			    	   Matcher mat = patt.matcher(temp);
			    	   if(mat.find())
			    		   System.out.println(mat.group(1));
			    	   System.out.println(""+i+". visited : "+ id  );
			       }
			       else 
			    	   System.out.println("error: not connected");
			       Thread.sleep(5000);
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
