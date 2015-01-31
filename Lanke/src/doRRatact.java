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

public class doRRatact {
	public static void main(String[] args) throws IOException {
		label1: while (true) {
			Pattern p = Pattern.compile("\"id\":(\\d*)");
			PrintWriter pw = new PrintWriter(new File("out.txt"));
			String content = null;
			pw.println("start");
			pw.flush();
			try {
				FileInputStream fi = new FileInputStream("rrfriend.txt");
				byte[] buf = new byte[fi.available()];
				fi.read(buf);
				content = new String(buf);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pw.println(e.getMessage());
				pw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pw.println(e.getMessage());
				pw.flush();
			}
			try{
			Matcher m = p.matcher(content);
			ArrayList<String> ids = new ArrayList<String>();
			while (m.find())
				ids.add(m.group(1));

			HttpClient client = new HttpClient();
			client.getHostConfiguration().setHost("www.m.xiaonei.com", 80,
					"http");
			client
					.getParams()
					.setCookiePolicy(
							org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
			client.getParams().setContentCharset("UTF-8");

			System.out.println(ids.size());
			String cookieStr = "_r01_=1; p=42b94cfe9b8eef8a38f101a0984982dc6; ap=230112086; depovince=GW; WebOnLineNotice_230112086=1; __utmb=151146938; t=d54826881140ffc1b592714469c70cf16; societyguester=d54826881140ffc1b592714469c70cf16; id=230112086; xnsid=672bbc3f; kl=kl_230112086; __utma=151146938.1003606478.1269579401.1269579401.1269579401.1; __utmc=151146938; __utmz=151146938.1269579401.1.1.utmccn=(direct)|utmcsr=(direct)|utmcmd=(none); XNESSESSIONID=dc4c6142a6f6; _urm_230112086=8";

			for (int stk = 0; stk < ids.size(); stk++) {
				String id = ids.get(stk);
				// String id = "225455099";
				pw.println(id + "\t");
				pw.flush();
				String sendContent = null;
				PostMethod pm = new PostMethod(
						"http://act.renren.com/yuren/op/doAttack");
				pm.setRequestHeader("Referer",
						"http://act.renren.com/yuren/home#nogo");
				pm.setRequestHeader("Cookie", cookieStr);
				NameValuePair[] dataSend = {
						new NameValuePair("uid",ids.get(stk) ),
						new NameValuePair("weaponId", "1") };
				//ids.get(stk)
				pm.setRequestBody(dataSend);
				try {
					int res = client.executeMethod(pm);
					if (res == HttpStatus.SC_OK) {
						pw.println(pm.getResponseBodyAsString());
						pw.println("Attacked"+ids.get(stk));
					} else {
						pw.println("network error");
						Thread.sleep(3000);
					}
					pw.flush();
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
			try {
				Thread.sleep(3000000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				pw.println(e.getMessage());
				pw.flush();
			}
			}catch(Exception e)
			{
				pw.println(e.getMessage());
				pw.flush();
			}
		}
	}
}
