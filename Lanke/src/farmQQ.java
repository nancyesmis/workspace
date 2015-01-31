import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.xml.internal.bind.v2.runtime.Name;

public class farmQQ {
	public static void main(String[] args) throws IOException {
		label1: while (true) {
			Pattern p = Pattern.compile("\"userId\":([0-9]*),");
			String content = null;
			try {
				FileInputStream fi = new FileInputStream("d:\\tt\\farmFriends.txt");
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
				ids.add(m.group(1));
			
			
			
			HttpClient client = new HttpClient();
			client.getHostConfiguration().setHost("http://user.qzone.qq.com", 80,
					"http");
			client
					.getParams()
					.setCookiePolicy(
							org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
			client.getParams().setContentCharset("UTF-8");

			for(int i=0;i<ids.size();i++){
			//	PostMethod pm = new PostMethod(
			//			"http://nc.qzone.qq.com/cgi-bin/cgi_farm_steal?mod=farmlandstatus&act=scrounge");
			PostMethod pm = new PostMethod("http://happyfarm.qzone.qq.com/api.php?mod=farmlandstatus&act=clearWeed");
				pm.setRequestHeader("Referer",
						"http://appimg.qq.com/happyfarm/module/Main_v_10.swf");
				pm
						.setRequestHeader(
								"Cookie",
								"pvid=813906953; flv=10.0; pt2gguin=o0077168304; ptcz=a8dd1c7765e1a3dc4837ce2130ac2ca5e30e39e472492bd04ffbaf61011db357; r_cookie=99292172321; o_cookie=77168304; comment_skey=7f2d83d7fd5af8898c9f79ef6b7f3ab7; comment_uin=77168304 %u3005%u84dd%u002f%u006b%u0075%u006b%u5ba2%u3005; qqinfo_135300054=qq*135300054+age*21+birthday*0+sex*1+province*+city*; uin=o0077168304; skey=@ASeKn5X0K; ssid=s3163124427; randomSeed=3687686; login_time=8EDCD20C347A4FFE2FFB733EF24224EF1DB2A63B5B57A2AB");
				
				NameValuePair[] dataSend = {
						new NameValuePair("place", "0,1,2,3,4,5,6,7,8,9"),
						new NameValuePair("ownerId", ids.get(i)),
						new NameValuePair("farmTime","1254883515"),
						new NameValuePair("farmKey","f3d0d23b906fe439c6238c5f8f8992f9")
						//new NameValuePair("tName","しovの紬"),
						//new NameValuePair("fName","？清/:+人？")
						
						};
				pm.setRequestBody(dataSend);
					try {
					int res = client.executeMethod(pm);

				    if (res == HttpStatus.SC_OK) {
						String cc = pm.getResponseBodyAsString();

						System.out.println(cc);
					
					} else {
							System.out.println("Name not found");
							Thread.sleep(3000);
						}
				    Thread.sleep(3000);
					
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue label1;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue label1;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue label1;
				}

			}
		}
		}
	}

