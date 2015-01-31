package wisc.db.classification;


import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Makes transition between update trigger and classification view when new entity comes
 * <p>
 * It parses trigger's input and recreates the view
 * 
 * @author lkoc
 *
 */
public class ClsViewUpdateTransition {
	static String szDelim = "\\|\\|\\|\\|"; // this is |||| but escaped since its a regex
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		ClsViewSetup.loadHazyConfParameters();
		ClsViewSetup clsVSetup = ClsViewSetupFactory.getClsViewSetup();
		
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
			echoServer = new ServerSocket(9998);
		}
		catch (IOException e) {
			System.out.println(e);
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
				
				String updateEntityName = szAcross[0];
				int newEntityId = Integer.parseInt(szAcross[1]);
				String newEntityName = szAcross[2];
				
				int viewId = 0;
				String viewName = "";
				String posExTable = "";
				String negExTable = "";
				int withReservoir = 0;
				int noOp = 0;
				
				//retrieve view id and view name
				ArrayList<String> viewIdName = clsVSetup.retrieveViewIdNameFromGeneralCatalog(updateEntityName);
				viewId = Integer.parseInt(viewIdName.get(0));
				viewName = viewIdName.get(1);
				//retrieve pos ex, neg ex, with reservoir and no op options
				ArrayList<String> retList = clsVSetup.retrievePosNegExTableReservoirNoOpFromUpdateTable(viewId);
				posExTable = retList.get(0);
				negExTable = retList.get(1);
				withReservoir = Integer.parseInt(retList.get(2));
				noOp = Integer.parseInt(retList.get(3));
				double m_factor = clsVSetup.prepareTrainWithNewEntity(viewId, updateEntityName, newEntityId, newEntityName, posExTable, negExTable);
				int featureSize = clsVSetup.getNumberOfFeatures(viewId);
				clsVSetup.updateMFactorInCatalog(viewId, m_factor);	//sgd updates dimension (feature size)
				int retValue = clsVSetup.train(viewId, featureSize, withReservoir, noOp);
				if(retValue == -1)
					System.err.println("re-train after new entity is unsuccessful");
				clsVSetup.prepareGeneralRTableForHazy(viewId, updateEntityName);
				clsVSetup.createViewAfterTraining(viewId, viewName);
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
		}
	}
}
