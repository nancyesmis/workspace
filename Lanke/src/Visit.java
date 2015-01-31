import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class Visit {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		loop: while (true) {
			try {
				
				PrintStream ps = new PrintStream("output.txt");
				//PrintStream ps = new PrintStream(System.out);
				HttpClient client = new HttpClient();
				client.getHostConfiguration().setHost("www.m.renren.com", 80,
						"http");
				client
						.getParams()
						.setCookiePolicy(
								org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
				client.getParams().setContentCharset("UTF-8");
				GetMethod getids = new GetMethod(
						"http://friend.renren.com/myfriendlistx.do");

				getids.setRequestHeader("Referer",
						"http://www.renren.com");

				getids
						.setRequestHeader(
								"Cookie",
								"_r01_=1; p=564c75c84777570a789346a0e951129f6; ap=230112086; depovince=GW; kl=kl_230112086; xnsid=8579d4e9; id=230112086; societyguester=c4c6a05875007a94dc6295a6a813ba9f6; t=c4c6a05875007a94dc6295a6a813ba9f6");
				client.executeMethod(getids);
				
				Pattern p = Pattern.compile("\"id\":(\\d+),");
				String content = getids.getResponseBodyAsString();
				Matcher m = p.matcher(content);
				ArrayList<String> ids = new ArrayList<String>();
				while (m.find())
				{
					if(!ids.contains(m.group(1)) && m.group(1) != null)
						ids.add(m.group(1));							
				}
				getids.releaseConnection();
				ps.println(ids.size());
				for (int i = ids.size() -1 ; i >= 0; i--) {
					String id = ids.get(i);
					GetMethod pm = new GetMethod(
							"http://3g.renren.com/profile.do?htf=706&id="+id + "&sid=nLt_WcT65oj8RROWKsAoOc&4h5swa");

					pm.setRequestHeader("Referer",
							"http://3g.renren.com/profile.do?id=" + id);

					pm
							.setRequestHeader(
									"Cookie",
									"_r01_=1; p=564c75c84777570a789346a0e951129f6; ap=230112086; depovince=GW; kl=kl_230112086; xnsid=8579d4e9; id=230112086; societyguester=c4c6a05875007a94dc6295a6a813ba9f6; t=c4c6a05875007a94dc6295a6a813ba9f6");

					try {
						int res = client.executeMethod(pm);
						if (res == HttpStatus.SC_OK) {
							 String temp = pm.getResponseBodyAsString();
							// ps.println(temp);
							// Pattern patt =
							// Pattern.compile("- (.*) ((.*))</title>");
							// Matcher mat = patt.matcher(temp);
							// if (mat.find())
							// ps.println(mat.group(1));
							ps.println("" + i + ". visited : " + id);
							boolean hasf = true;
							ArrayList<String> fid = new ArrayList<String>();
							int curpage = 1;
							while (hasf) {
								GetMethod methodf = new GetMethod(
										"http://friend.renren.com/GetFriendList.do?curpage="
												+ curpage + "&id=" + id);
								methodf.setRequestHeader("Referer",
										"http://3g.renren.com/profile.do?id=" + id);

								methodf
										.setRequestHeader(
												"Cookie",
												"_r01_=1; p=564c75c84777570a789346a0e951129f6; ap=230112086; depovince=GW; kl=kl_230112086; xnsid=8579d4e9; id=230112086; societyguester=c4c6a05875007a94dc6295a6a813ba9f6; t=c4c6a05875007a94dc6295a6a813ba9f6");
								client.executeMethod(methodf);
								String tf = methodf.getResponseBodyAsString();
								// ps.println(methodf.getResponseBodyAsString());
								if (!tf.contains("ÏÂÒ»Ò³")) {
									hasf = false;
								}
								Pattern patf = Pattern
										.compile("http://www.renren.com/profile.do\\?id=(\\d+)");
								Matcher mf = patf.matcher(tf);
								while (mf.find()) {
									if (!mf.group(1).equals(id)
											&& !fid.contains(mf.group(1))
											&& mf.group(1) != null)
										fid.add(mf.group(1));
								}
								curpage++;
								methodf.releaseConnection();
							}
							for (int j = 0; j < fid.size(); j++) {
								GetMethod vf = new GetMethod(
										"http://3g.renren.com/profile.do?htf=706&id="+fid.get(j) + "&sid=nLt_WcT65oj8RROWKsAoOc&4h5swa");

								vf.setRequestHeader("Referer",
										"http://3g.renren.com/profile.do?id=" + id);

								vf
										.setRequestHeader(
												"Cookie",
												"_r01_=1; p=564c75c84777570a789346a0e951129f6; ap=230112086; depovince=GW; kl=kl_230112086; xnsid=8579d4e9; id=230112086; societyguester=c4c6a05875007a94dc6295a6a813ba9f6; t=c4c6a05875007a94dc6295a6a813ba9f6");
								
								client.executeMethod(vf);
								//ps.println(vf.getResponseBodyAsString());
								vf.releaseConnection();
								ps.println("\t" + j + "  " + fid.get(j));
								Thread.sleep(2500);
							}
						} else {
							ps.println("error: not connected");
						}
						Thread.sleep(2500);
					} catch (HttpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						ps.print(e.getMessage());
						Thread.sleep(1000);
						continue;
					}
					pm.releaseConnection();
				}
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				continue loop;
			}
		}
	}
}
