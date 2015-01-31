import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;


public class DealContact {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkedList<String> data = new LinkedList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("d:\\tt\\nihao.txt"));
			String num = null;
			while(br.ready()){
			num = br.readLine();
			num =num.replace("::::::::::::",":::");
			num = num.replace(":::::::::",":::");
			num = num.replace("::::::",":::");
			
			
			if(num.startsWith(":::"))
			{	
				num = num.substring(3);
				
			}
			if(num.endsWith(":::"))
				num = num.substring(0, num.length()-3);
			num = num.replace(":::", " ::: ");
			data.add(num);
			System.out.println(num);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
