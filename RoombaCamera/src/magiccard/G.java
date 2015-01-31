/*
 * Created on Nov 4, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package magiccard;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import magiccard.task.TaskCreator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
/**
 * @author shen
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class G {
	public static final boolean DEBUG = true;
	
	public static final boolean MESSAGE_DEBUG = true;

	public static final boolean LABEL_DEBUG = true;
	
	//this is used to determine how the stage can help to print out
	//only the nessary debug information in the output. To use
	//this, one will set the current stage to certain number, and 
	//use debug("str", num) for debug info. Make sure one will
	//turn of the global debug flag at this time
	public static final int CURRENT_STAGE = 1;

//	public static Random RAND = new Random();
	
	private static JFrame FRAME;
	
	//this label will be used in sunburst menu for debugging
	public static JLabel moLabel = new JLabel();
	
	/**
	 * This variable determine how long we will wait for the next timed execution 
	 * in highlight and scrolling object. The smaller the number, the longer the 
	 * duration in between actions, and the quality will be less, but it will be 
	 * less CPU intensive. 
	 */
	public static final int TIME_DIVISION_FACTOR = 10;
	
	static{
		FRAME = new JFrame("");
	}
	
	public static void debug(String s){
		if(DEBUG){
			System.out.println(s);
		}
	}
	
	public static void debug(String s, int stage){
		if(stage == CURRENT_STAGE){
			System.out.println("Stage: " + stage+"->"+s);
		}
	}
	
	
	
	public static void message(String s){
		if(MESSAGE_DEBUG){
		
//		Modal dialog with OK button.
		 JOptionPane.showMessageDialog(FRAME, s);
		}
	}
	public static String input(String s){
	
//	Modal dialog with OK/cancel and a text field
	 String text = JOptionPane.showInputDialog(FRAME, s);
	 return text;
	}
	
	public static void label(String s){
		if(LABEL_DEBUG){
			moLabel.setText(s);
		}
	}
	
	/**
	 * return the distance between two int points
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double intPointDistance(int x1, int y1, int x2, int y2){
		double distance = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
		return distance;
	}
	
	private static final String AUDIO_FILE_PREFIX =  "C:\\Development\\Java\\AudioUI";
	
	/**
	 * static method to return the absolute path of the sound file
	 * @param filename
	 * @return
	 * String
	 */
	public static String getAbsolutePath(String filename){
	    if(filename.indexOf(AUDIO_FILE_PREFIX)!= -1){
	        return filename;
	    }else{
	        return AUDIO_FILE_PREFIX + "\\"+filename;
	    }
	}
	
	public static ArrayList getListFromStrArray(String[] strArray){
		ArrayList<String> list = new ArrayList<String>();
		if(strArray == null || strArray.length ==0) return null;
		for(String arg: strArray) list.add(arg);
		return list;
	}
	
	public static void main(String[] args){
		/*String a = TaskCreator.MEMORIZEDTASK_PREFIX+98+".txt";
		 if(a.startsWith(TaskCreator.MEMORIZEDTASK_PREFIX)){
         	//get the int value from 
         	int index = a.indexOf(".");
         	int myInt = Integer.parseInt(a.substring(TaskCreator.MEMORIZEDTASK_PREFIX.length(),index));
         	debug("int is " + myInt);
         }*/
	            try {
	                // Open the image file
	                InputStream is = new BufferedInputStream(
	                    new FileInputStream("C:\\MagicCard\\MemorizedSetOfTask74.png"));
	    
	             // Find the default service
	                DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
	                PrintService service = PrintServiceLookup.lookupDefaultPrintService();
	    
	                // Create the print job
	                DocPrintJob job = service.createPrintJob();
	                Doc doc = new SimpleDoc(is, flavor, null);
	    
	                // Monitor print job events; for the implementation of PrintJobWatcher,
	                // see e702 Determining When a Print Job Has Finished
	                PrintJobWatcher pjDone = new PrintJobWatcher(job);
	    
	                // Print it
	                job.print(doc, null);
	    
	                // Wait for the print job to be done
	                pjDone.waitForDone();
	    
	                // It is now safe to close the input stream
	                is.close();
	            } catch (PrintException e) {
	            } catch (IOException e) {
	            }
	        }//end of main method 
	
}
