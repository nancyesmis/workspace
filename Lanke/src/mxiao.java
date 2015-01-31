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

public class mxiao {
	public static void main(String[] args) throws IOException {
		label1: while (true) {
			Pattern p = Pattern.compile("\"id\":(\\d*)");
			String content = null;
			try {
				FileInputStream fi = new FileInputStream("d:\\tt\\rrfriend.txt");
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
			client.getHostConfiguration().setHost("www.m.xiaonei.com", 80,
					"http");
			client
					.getParams()
					.setCookiePolicy(
							org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
			client.getParams().setContentCharset("UTF-8");
            
			System.out.println(ids.size());
			Pattern pp = Pattern.compile("(.|\\r|\\n)*");
			Pattern ppg = Pattern.compile("返回(.*)的个人主页");
			Pattern pl = Pattern
					.compile("联系方式</div><div class=\"sec\">(.*?)<div class=\"sectitle cblue\">");
			Pattern pinfo = Pattern
					.compile("<b>(.*?)</b><br />(.*?)(<br />|<span|</div)");
			Pattern pt = Pattern.compile(">([^><b].+?)<");
			int index = 0;
			ArrayList<String> arContent = new ArrayList<String>();
			try {
				BufferedReader br = new BufferedReader(new FileReader(
						"d:\\nihaonewyear.txt"));
				while (br.ready()) {
					String str = br.readLine();
					if (str != null && !str.equals(""))
						arContent.add(str);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			FileWriter pw = new FileWriter("d:\\xiaonei.txt", true);

			for (int stk = ids.size() - 10; stk > 420; stk--) {
				String id = ids.get(stk);
				//String id = "225455099";
				System.out.print(id + "\t");
				String sendContent = null;
				PostMethod pm = new PostMethod(
						"http://m.xiaonei.com/wgossip.do?id=" + id);
				pm.setRequestHeader("Referer",
						"http://m.xiaonei.com/profile.do?id=" + id);
				pm
						.setRequestHeader(
								"Cookie",
								"_de=B24367A73FFB086E766626A722DF9EF18ED172744450A224; depovince=ZJ; _r01_=1; mop_uniq_ckid=222.205.95.20_1249436744_309085245; societyguester=688df8ab728d06506eb75c3dc900f1c96; kl=0502d97eab9f9d7a0cd501d674a639a7_230112086; jebecookies=230112086|1|1987-1-1|20|60011001|9001_; hostid=230112086; XNESSESSIONID=c0627b8196b7; xn_app_histo_230112086=7-4-8-6-2-20706-19-20-3-19974; WebOnLineNotice_230112086=1; __utma=204579609.734661358.1249459662.1249459662.1249459662.1; __utmb=204579609; __utmc=204579609; __utmz=204579609.1249459662.1.1.utmccn=(referral)|utmcsr=home.xiaonei.com|utmcct=/Home.do|utmcmd=referral");
				Random r = new Random();

				sendContent = "好久没来踩咯，踩踩~\n 声明:一下内容为网上爬到的新年祝福，不代表本人立场\n"
						+ arContent.get(Math
								.abs(r.nextInt() % arContent.size()));
				// System.out.println(sendContent);
				NameValuePair[] dataSend = {
						new NameValuePair("body", sendContent),
						new NameValuePair("post", "留言") };
				pm.setRequestBody(dataSend);
				GetMethod gm = new GetMethod(
						"http://m.xiaonei.com/profile.do?id=" + id + "&tab=2");
				gm
						.setRequestHeader(
								"Cookie",
								"_de=B24367A73FFB086E766626A722DF9EF18ED172744450A224; depovince=ZJ; _r01_=1; mop_uniq_ckid=222.205.95.20_1249436744_309085245; societyguester=688df8ab728d06506eb75c3dc900f1c96; kl=0502d97eab9f9d7a0cd501d674a639a7_230112086; jebecookies=230112086|1|1987-1-1|20|60011001|9001_; hostid=230112086; XNESSESSIONID=c0627b8196b7; xn_app_histo_230112086=7-4-8-6-2-20706-19-20-3-19974; WebOnLineNotice_230112086=1; __utma=204579609.734661358.1249459662.1249459662.1249459662.1; __utmb=204579609; __utmc=204579609; __utmz=204579609.1249459662.1.1.utmccn=(referral)|utmcsr=home.xiaonei.com|utmcct=/Home.do|utmcmd=referral");
				gm
						.setRequestHeader(
								"Referer",
								"http://m.xiaonei.com/profile.do?id=231400287&sid=a28d63ad424ea0fd78412a73ac7e09506&fan28e");
				try {
					int res = client.executeMethod(pm);

					if ((res == HttpStatus.SC_MOVED_TEMPORARILY)
							|| (res == HttpStatus.SC_MOVED_PERMANENTLY)
							|| (res == HttpStatus.SC_SEE_OTHER)
							|| (res == HttpStatus.SC_TEMPORARY_REDIRECT)) {
						// 读取新的 URL 地址
						Header header = pm.getResponseHeader("location");
						if (header != null) {
							String uri = header.getValue();
							if (uri == null || uri.equals(""))
								System.out.println("header error");
							// System.out.println("new uri is : " + uri);
							GetMethod ggm = new GetMethod(uri);

							client.executeMethod(ggm);
							String ccggm = ggm.getResponseBodyAsString();
							Matcher mg = ppg.matcher(ccggm);
							System.out.println(ccggm);
							if (mg.find())
								System.out.println("踩了  " + mg.group(1)
										+ "\tNo." + index++);
							// \\System.out.println(ccggm);
							else {
								Matcher mgg = pp.matcher(ccggm);
								if (mgg.find()) {
									System.out.println("踩了  " + mgg.group(1)
											+ "\tNo." + index++);
								} else {
									System.out.println("not foud the name ?");
								}
							}
							Thread.sleep(5000);

						} else {
							System.out.println("header 2 error");
						}
					} else if (res == HttpStatus.SC_OK) {
						String cc = gm.getResponseBodyAsString();

						System.out.println(cc);
						if (cc == null)
							return;
						Matcher mm = pp.matcher(cc);
						if (mm.find()) {
							// System.out.println(cc);
							String myrest = mm.group(1) + mm.group(2) + "\t"
									+ id;
							System.out.println(index++ + myrest);
							pw.write(myrest + "\n");

							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							System.out.println("Name not found");
							Thread.sleep(3000);
						}
						Matcher ml = pl.matcher(cc);
						if (ml.find()) {
							String tempforL = ml.group(1);
							Matcher t = pt.matcher(tempforL);
							while (t.find()) {
								pw.write(t.group(1) + "\t");
							}
							pw.write("\n");
						}
						int numd = cc.indexOf("基本信息");
						if (numd > 0)
							cc = cc.substring(numd);
						Matcher mi = pinfo.matcher(cc);
						while (mi.find()) {
							pw.write(mi.group(1) + "\t" + mi.group(2) + "\n");
						}
						pw.write("\n\n\n\n\n");
						pw.flush();
					} else {
						System.out.println("network error");
						Thread.sleep(3000);
					}
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
			pw.close();
		}
	}
}
