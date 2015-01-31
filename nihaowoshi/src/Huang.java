import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class Huang {
public static PrintWriter pw;
public static void main(String[] args)
{

	try {
		pw = new PrintWriter("d:\\text.txt");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
   int f()
   {
	   return 1;
   }
}
