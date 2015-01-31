import java.util.Scanner;

import junit.framework.TestResult;


public class GenerateReport {
	String testResultF = "";
	String standardsF = "";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void generateReport()
	{
		Scanner scan = new Scanner(testResultF);
		while(scan.hasNext())
		{
			String line = scan.nextLine();
			String[] parts = line.split(line);
			
		}
		
	}
}
