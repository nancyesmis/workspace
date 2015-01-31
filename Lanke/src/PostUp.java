/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 *
 * @author Jacob
 */
public class PostUp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
		HttpClient httpClient = new HttpClient();
		httpClient.getHostConfiguration().setHost("www.cc98.org", 80, "http");
		httpClient.getParams().setCookiePolicy(org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
		String url = "/login.asp?action=chk";
		PostMethod postMethod = new PostMethod(url);
		NameValuePair[] data = {
			new NameValuePair("username","来找妞的"),
			new NameValuePair("password","confidence")
		};
		postMethod.setRequestBody(data);

			int status = httpClient.executeMethod(postMethod);
			if( status == HttpStatus.SC_OK ){
				String con="新年快乐";
				String cookie;
				String referer;
				String[] mess = new String[10000];
				BufferedReader br = new BufferedReader(new FileReader("d:\\nihaonewyear.txt"));
				int i=0;
				while(br.ready())
				{
					mess[i++] = br.readLine();
					//System.out.println(i+"."+mess[i-1]);
				}
                int number =0;
				while(true){
                    number ++;
                    if(number%970 ==0)
                        number = 0;
					int kk=0;
                    httpClient.getParams().setContentCharset("UTF-8");
					PostMethod pp = new PostMethod("/SaveReAnnounce.asp?method=fastreply&BoardID=146");
					pp.setRequestHeader("Cookie", "BoardList=BoardID=Show; aspsky=usercookies=3&userid=245533&useranony=1&userclass=%E5%8D%9A%E5%A3%AB%E5%90%8E&username=%E6%9D%A5%E6%89%BE%E5%A6%9E%E7%9A%84&userhidden=2&password=43ddf159ed36ad0f; ASPSESSIONIDQQCRBDCT=CFGIFFBCOBPENJJPOPOCOIEG; __utma=72018561.4342226502763061000.1228635055.1230782642.1230785197.112; __utmz=72018561.1228635055.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmb=72018561.28.10.1230785197; __utmc=72018561");
                    pp.setRequestHeader("Referer", "http://www.cc98.org/dispbbs.asp?boardID=146&ID=2544954&page=");
					NameValuePair[] data2 ={
						new NameValuePair("followup","136951469"),
						new NameValuePair("RootID","2544954"),
						new NameValuePair("star","1"),
						new NameValuePair("TotalUseTable","bbs12"),
						new NameValuePair("Content",con)

					};
					pp.setRequestBody(data2);
					int st = httpClient.executeMethod(pp);
					if(st==HttpStatus.SC_OK){
						try {
							kk++;
							System.out.println(number+". yes");
							//System.out.println(pp.getResponseBodyAsString());
                            Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						System.out.println("failure");
						System.out.println(pp.getResponseBodyAsString());
					}
					Random r = new Random();
					while(mess[number%900].equals("")||mess[number%900].equals("\n")||mess[number%900].equals(" "))
                    {
                        number++;
                        if(number%910 ==0)
                        {
                            number = 0;
                        }
                    }
                    con = mess[number%900];
                    con+="[em";
                    int t = r.nextInt(92);
                    if(t<10)
                    {
                        con=con+"0"+t+"]";
                    }
                    else
                    {
                        con=con+t+"]";
                    }
                   // System.out.println(con);
                    if(number%400 == 0 )
                    {
                        con="**********\nLet me have a rest.... T_T**********";
                        Thread.sleep(50000);
                    }
					data2[4].setValue(con);
					pp.releaseConnection();
				}
			}
		} catch (InterruptedException ex) {
            Logger.getLogger(PostUp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PostUp.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

    

}

