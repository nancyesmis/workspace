import java.io.IOException;
import java.util.Random;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class ShiCheng {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost("http://bbs.sgchinese.com/", 80, "http");
		client
				.getParams()
				.setCookiePolicy(
						org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setContentCharset("UTF-8");

		Random ranNumber = new Random();
		String[] content = { "dddd", "upup", "ddddddd", "up", "go" };
		
		
		String cookieStr = "s3Q_sid=GDjL82; __utma=228476878.50036056.1269431658.1269758800.1269952654.6; __utmz=228476878.1269952655.6.14.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=%E7%8B%AE%E5%9F%8E%E8%AE%BA%E5%9D%9B; s3Q_visitedfid=138D235D234D1; smile=2D1; s3Q_cookietime=2592000; s3Q_auth=4a33snIzApprCvg9ObJ7Q9kNhNG%2B7QxJG1uV0gjuL%2F55OYeE0OyCU6iwaDlRek8nNP8SEZHJXnpMotabauT%2FdLNeI4xR; __utmb=228476878.1.10.1269952654; __utmc=228476878; checkpm=1; __utma=52109867.2020415042.1269756707.1269756707.1269756707.1; __utmz=52109867.1269756707.1.1.utmcsr=bbs.sgchinese.com|utmccn=(referral)|utmcmd=referral|utmcct=/memcp.php";
		
		
		while (true) {
			PostMethod pm = new PostMethod(
					"http://bbs.sgchinese.com/post.php?action=reply&fid=138&tid=2956123&extra=page%3D1&replysubmit=yes&infloat=yes&handlekey=fastpost&inajax=1");
			pm.setRequestHeader("Connection", "Keep-Alive");
			pm
					.setRequestHeader(
							"Referer",
							"http://bbs.sgchinese.com/thread-2956123-1-1.html");
			pm
					.setRequestHeader(
							"Cookie",
							cookieStr);
			pm.setRequestHeader("Host", "bbs.sgchinese.com");
			pm.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) ; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2)");
			pm.setRequestHeader("Connection", "Keep-Alive");
			pm.setRequestHeader("Accept-Language", "zh-cn");
			pm.setRequestHeader("Accept-Encoding", "gzip, deflate");
			
			NameValuePair[] data = {
					new NameValuePair("formhash", "2bf8661a"),
					new NameValuePair("message", content[Math.abs(ranNumber
							.nextInt() % 5)]),
					new NameValuePair("subject", ""),
					new NameValuePair("usesig","0")
			};
			pm.setRequestBody(data);
			try {
				int res = client.executeMethod(pm);
				if (res == HttpStatus.SC_OK) {
					String temp = pm.getResponseBodyAsString();
					System.out.println("yes" + temp);
					System.out.println("yes");
				} else
					System.out.println("error: not connected");
				Thread.sleep(60000);
				while (true) {
					pm.setRequestHeader("Connection", "Keep-Alive");
					GetMethod gm = new GetMethod(
							"http://bbs.sgchinese.com/forum-138-1.html");

					gm
							.setRequestHeader(
									"Referer",
									"http://bbs.sgchinese.com/thread-2875300-1-1.html");

					gm
							.setRequestHeader(
									"Cookie",cookieStr);
					
					pm.setRequestHeader("Host", "bbs.sgchinese.com");
					pm.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) ; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2)");
					pm.setRequestHeader("Connection", "Keep-Alive");
					pm.setRequestHeader("Accept-Language", "zh-cn");
					pm.setRequestHeader("Accept-Encoding", "gzip, deflate");
					
					int secres = client.executeMethod(gm);
					if (secres == HttpStatus.SC_OK) {
						String contt = gm.getResponseBodyAsString();
						int indexof = contt.indexOf("Buona Vista");
						System.out.println(indexof);
						if (indexof > 49439 || indexof < 0)
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
