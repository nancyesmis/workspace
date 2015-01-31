package wisc.db.equivalence;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Makes transition between update trigger and equivalence view (graph) object
 * <p>
 * It parses trigger's input and calls update function of the {@link Graph} object
 * 
 * @author lkoc
 *
 */
public class EqViewUpdateTransition {
	static String szDelim = "\\|\\|\\|\\|"; // this is |||| but escaped since its a regex
	// taken from http://www.javaworld.com/jw-12-1996/jw-12-sockets.html?page=4
	
	@SuppressWarnings("deprecation")
	public static void main(String args[]) {
		EqViewSetup.loadHazyConfParameters();
	
		LinkedHashMap<Integer, Graph> eqViewList = new LinkedHashMap<Integer, Graph>();
		EqViewSetup eqViewSetup = EqViewSetupFactory.getEqViewSetup();
		// declaration section:
		// declare a server socket and a client socket for the server
		// declare an input and an output stream
		ServerSocket echoServer = null;
		String line;
		DataInputStream is;
		PrintStream os;
		Socket clientSocket = null;
		// Try to open a server socket on port 9999
		// Note that we can't choose a port less than 1023 if we are not
		// privileged users (root)
		try {
			echoServer = new ServerSocket(9999);
		}
		catch (IOException e) {
			System.out.println(e);
		}   

		// Warm up all classes/methods we will use
		try {
			Sizeof.runGC ();
			Sizeof.usedMemory ();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Create a socket object from the ServerSocket to listen and accept 
		// connections.
		// Open input and output streams
		try {
			while(true) {
				clientSocket = echoServer.accept();
				is = new DataInputStream(clientSocket.getInputStream());
				os = new PrintStream(clientSocket.getOutputStream());
				
				line = is.readLine();
				String[] szAcross = line.split(szDelim);
				if(szAcross[0].equals("finish"))
					break;
				
				String updatedTableName = szAcross[0];
				String updateType = szAcross[1];
				int src = Integer.parseInt(szAcross[2]);
				int dst = Integer.parseInt(szAcross[3]);
				ArrayList<Object> viewIdWeight = eqViewSetup.getViewIdWeightFromRuleTableName(updatedTableName);
				int viewId = ((Integer) viewIdWeight.get(0)).intValue();
				double weight = ((Double) viewIdWeight.get(1)).doubleValue();
				
				if(!eqViewList.containsKey(viewId)) {
					Graph graph = new Graph(viewId);
					eqViewList.put(viewId, graph);
				}
				
				long heap1 = Sizeof.usedMemory ();
				eqViewList.get(viewId).update(updatedTableName, updateType, src, dst, weight);
				Sizeof.runGC ();
				long heap2 = Sizeof.usedMemory ();
				if(heap2 - heap1 > EqViewConstants.MEMORY_USAGE_LIMIT)
					eqViewList.get(viewId).forceToDisk();

				os.println("done!!!\n");
				clientSocket.close();
			}   
		}
		catch (IOException e) {
			try {
				PrintStream st = new PrintStream("/tmp/exception");
				e.printStackTrace(st);
				st.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
