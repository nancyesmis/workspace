import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class regular {
	public static void main(String[] args) {
		Pattern p = Pattern.compile("2[0-9]{8}");
		String content = null;
		try {
			FileInputStream fi = new FileInputStream("d:\\friend.txt");
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
		client.getHostConfiguration().setHost("www.xiaonei.com",80,"http");
		client.getParams().setCookiePolicy(org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setContentCharset("UTF-8");
		System.out.println(ids.size());
		Pattern pp = Pattern.compile("<title>Ð£ÄÚ - (.*)\n.*</title>");
		Pattern p2 = Pattern.compile("<dd>\n(.*)</dd>");
		int index =0;
		for(String id:ids){
			GetMethod gm = new GetMethod("http://xiaonei.com/profile.do?portal=profileFriendlist&id="+id);
			gm.setRequestHeader("Cookie", "depovince=ZJ; _r01_=1; _de=B24367A73FFB086E766626A722DF9EF18ED172744450A224; mop_uniq_ckid=219.234.81.115_1247617905_93774761; __utma=204579609.1810150862.1247618525.1248852327.1248868244.21; __utmz=204579609.1248868244.21.11.utmccn=(referral)|utmcsr=home.xiaonei.com|utmcct=/Home.do|utmcmd=referral; home_notice230112086=0; kl=8c9ed25c4c39888dd0c8b896bee55b38_230112086; jebecookies=230112086|1|1987-1-1|20|60011001|9001_; hostid=230112086; XNESSESSIONID=141b4ce3b212; xn_app_histo_230112086=7-4-8-6-2-20706-19-20-3-19974; shaftcookies=230112086|1000000018-35|1000000027-78|1000000020-94|1000000013-98|1000000022-79|1000000015-3|1000000008-53|1000000024-72|1000000017-27|1000000026-6|1000000019-99|1000000028-6|1000000021-22|1000000014-36|1000000023-21|1000000016-19|1000000007-55|1000000006-22|1000000004-67|1000000005-57|1000000003-29|; alxn=3974e58c2a352f3515ffe627fff7fe29fc3106392ef0b93f; societyguester=a28d63ad424ea0fd78412a73ac7e09506");
			gm.setRequestHeader("Referer", "http://xiaonei.com/profile.do?portal=homeUmayknow&id=244489064");
			try {
				int res = client.executeMethod(gm);
				if(res == HttpStatus.SC_OK)
				{
					String cc = gm.getResponseBodyAsString();					
					Matcher mm = pp.matcher(cc);
					Matcher m2 = p2.matcher(cc);
					if(m2.find())
						System.out.print(m2.group(1)+"\t");
					if(mm.find()){
						//System.out.println(cc);
						System.out.println(mm.group(1)+"\t"+index++);
						
					    try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    
					}else{
						System.out.println("Name not found");
						Thread.sleep(3000);
					}
						
				}else{
					System.out.println("network error");
					Thread.sleep(3000);
				}
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
