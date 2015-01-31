package magiccard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import magiccard.Tag.Type;
import magiccard.task.HouseworkTask;
import magiccard.task.MoveController;
import magiccard.task.SendController;
import magiccard.task.SendTask;
import magiccard.task.TaskController.Status;

import roomba.MagicCardRoombaController;
import roomba.MagicCardRoombaPanel;
import roomba.MagicCardRoombaController.RoombaType;

 
/**
 * The interface for the Magic Card system 
 * @author Shengdong Zhao
 *
 */
public class UISystem extends JFrame {
	
	TrackingSystem tsystem;
	TaskManager taskmanager;
	MoveController moMoveController;
	MagicCardRoombaController roombacomm;
	Vector<Tag> tagtable = new Vector<Tag>();
	JScrollPane tagScrollPane;
	JScrollPane roombaControlScrollPane;
	Box taskBox = new Box(BoxLayout.Y_AXIS);
	Timer updateTimer;
	Box mainBox = new Box(BoxLayout.Y_AXIS);
	Box topBox = new Box(BoxLayout.X_AXIS);
	Box bottomBox = new Box(BoxLayout.Y_AXIS);
	
    Action prepareAction, startAction, quitAction, clearAction;
	private Vector<String> listData;
	JList tagList;
	
    
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;
	
    public static ArrayList<MagicCardRoombaController> ROOMBAS = new ArrayList<MagicCardRoombaController>();
    
	 public class PrepareAction extends AbstractAction {
	        public PrepareAction(String text, ImageIcon icon,
	                          String desc, Integer mnemonic) {
	            super(text, icon);
	            putValue(SHORT_DESCRIPTION, desc);
	            putValue(MNEMONIC_KEY, mnemonic);
	        }
	        public void actionPerformed(ActionEvent e) {
	            //displayResult("Action for first button/menu item", e);
	        	prepare();
	        	//collectTags();	        	
	        }
	    }
	 
	 public class ClearAction extends AbstractAction {
	        public ClearAction(String text, ImageIcon icon,
	                          String desc, Integer mnemonic) {
	            super(text, icon);
	            putValue(SHORT_DESCRIPTION, desc);
	            putValue(MNEMONIC_KEY, mnemonic);
	        }
	        public void actionPerformed(ActionEvent e) {
	            //displayResult("Action for first button/menu item", e);
	        	clear();
	        	//collectTags();	        	
	        }
	    }

	    public class StartAction extends AbstractAction {
	        public StartAction(String text, ImageIcon icon,
	                            String desc, Integer mnemonic) {
	            super(text, icon);
	            putValue(SHORT_DESCRIPTION, desc);
	            putValue(MNEMONIC_KEY, mnemonic);
	        }
	        
	        public void actionPerformed(ActionEvent e) {
	            start();
	        }
	    }

	    public class QuitAction extends AbstractAction {
	        public QuitAction(String text, ImageIcon icon,
	                           String desc, Integer mnemonic) {
	            super(text, icon);
	            putValue(SHORT_DESCRIPTION, desc);
	            putValue(MNEMONIC_KEY, mnemonic);
	        }
	        public void actionPerformed(ActionEvent e) {
	            quit();
	        }
	    }

	/**
	 * Constructor
	 */
	public UISystem(){
		
		//add the proper window and keyboard listeners to allow proper quit
		//of the program
		addWindowListener(
				  new WindowAdapter(){
					public void windowClosing(WindowEvent e) {System.exit(0);}}
					);
		
		addKeyListener(
				 new KeyAdapter(){
				    public void keyPress(KeyEvent e) {
						if (e.getKeyCode() == e.VK_C)
							System.exit(0);}}
			        );
		
		
		createMenus();
		
		JComponent tagView = createTagView();
	
		startupTrackingSystem();
		
		setupRoombaControlPanel();
		startupTaskManager();

		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		//taskPane.setPreferredSize(new Dimension(200,200));
		topBox.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT/2));
		topBox.add(tagView);
		taskBox.setPreferredSize(new Dimension(DEFAULT_WIDTH/2,DEFAULT_HEIGHT/2));
		topBox.add(taskBox);		
		//mainBox.add(topBox);
		
		bottomBox.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT/2));
		bottomBox.setBorder(BorderFactory.createLineBorder(Color.black));
		//mainBox.add(bottomBox);
		this.getContentPane().add(topBox, BorderLayout.CENTER);
		this.getContentPane().add(bottomBox,BorderLayout.SOUTH);
	
		
		this.pack();
		this.setVisible(true);
		
	}
	
	class UpdateTagsTask extends TimerTask  {
	    public void run (  )   {
	      updateTrackingInfo();
	    }
	  }

	
	//==================================
	// Allowed actions in the UI system
	//==================================
	
	public void prepare(){
		//add a method to read the tagtable 20 times in intervals of 50 milliseconds
		//test and get all the stable reading tags 
		//tagtable = this.getStableTag();
		tagtable = tsystem.getTagTable();
		//build tasks here 
		ArrayList<HouseworkTask> sortedTasks = taskmanager.prepareTasks(tagtable);
		
		
		//task controllers are already installed, it's time to get things done properly
		//system.out.println(sortedTasks.toString());
		createTaskPane(sortedTasks);
		
		
		//update the task pane 
		G.debug("prepare system for executing robot tasks");
	}
	
	//==================================
	// Collect tags after preparing 
	//==================================
	
	public void collectTags(){
		//add a method to read the tagtable 20 times in intervals of 50 milliseconds
		//test and get all the stable reading tags 
		int amount=0;
		Tag[] myTags = new Tag[tagtable.size()];
		if(tagtable != null && tagtable.size() != 0)
		{   	
    		for(int i=0; i< tagtable.size(); i++)//&& tagtable.elementAt(i).found > TrackingSystem.FOUND_THRESHOLD
    			if (tagtable.elementAt(i)!=null && tagtable.elementAt(i).found > 1
    					&& tagtable.elementAt(i).type != Tag.Type.Exception&& tagtable.elementAt(i).type != Tag.Type.Object)
    			{
    				myTags[i] = tagtable.elementAt(i);
    				amount++;
    				System.out.println("the tag ("+ i + ") position is ("+ myTags[i].position.x +" , " + myTags[i].position.y+")");
    			}
    	}
		if (amount!=0)
			for (int i =0; i< tagtable.size();i++)
			{
    				MagicCardRoombaController roombacomm = UISystem.getFirstRoombaByType(MagicCardRoombaController.RoombaType.TagPickupRoomba);
    				if (myTags[i]!= null)
    				{
    					System.out.println("find it");
    					
    					moMoveController = new MoveController(tsystem, roombacomm, roombacomm.getRoombaId(), myTags[i].position, null);
    					System.out.println("this is No "+ i +"thread!");
    					moMoveController.addObserver(taskmanager);
    					moMoveController.start();
    					
        				if (moMoveController.getStatus() != Status.RUNNING) {
        					moMoveController.setMoving(false);
        					roombacomm.stop(); //stop roomba
        					System.out.println("the tag ("+ i + ") position is collected!");
        				}
    				}
    				
				}
	}

	
	public void start(){
		taskmanager.start();
		G.debug("start robot tasks");
	}
	
	public void quit(){
		G.debug("System quit");
		System.exit(0);
	}
	
	public void clear(){
		G.debug("System resetting");
		
		updateTimer.cancel();
		tsystem = null;
		
		tsystem = new TrackingSystem(ConfigFile.CONFIG_FILE);
		updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(new UpdateTagsTask(), 100, 1000);
		
		taskmanager = null;
		taskmanager = new TaskManager(ConfigFile.CONFIG_FILE, tsystem);		
		
		Component[] components = taskBox.getComponents();
		
		if(!(components == null || components.length == 0)){
			int size = components.length;
			for(int i=0; i< size; i++){
				if(components[i] instanceof TaskComponent) taskBox.remove(components[i]);
			}
		}
		
		taskBox.revalidate();
		taskBox.repaint();
	}
	
	public void allTasksFinished(){
		//JOptionPane.
	}
	
	public void startupTrackingSystem(){
		tsystem = new TrackingSystem(ConfigFile.CONFIG_FILE);
		updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(new UpdateTagsTask(), 100, 1000);
	}
	
	public void startupTaskManager(){
		taskmanager = new TaskManager(ConfigFile.CONFIG_FILE, tsystem);
	}

	//===========================================================
	// Setup the UI components
	// include "roomba control pane", "Tag view pane", "Task pane"
	//============================================================
	
	/**
	 * Setup the menu for this system
	 */
	public void createMenus(){
		
		 // Create the menu bar
	    JMenuBar magicMenuBar = new JMenuBar();
	    
	    // Create a menu
	    JMenu menu = new JMenu("Magic Card");
	    magicMenuBar.add(menu);
	    
		prepareAction = new PrepareAction("Prepare", null, "Remember all the tags and prepare for robot tasks", new Integer(KeyEvent.VK_P));
		startAction = new StartAction("Start", null, "Start robot tasks", new Integer(KeyEvent.VK_S));
		quitAction = new QuitAction("Quit", null, "Quit application", new Integer(KeyEvent.VK_Q));
		clearAction = new ClearAction("Clear", null, "Clear tasks, so we can prepare again", new Integer(KeyEvent.VK_C));
		
		Action[] actions = {clearAction, prepareAction, startAction, quitAction};
		
		for (int i = 0; i < actions.length; i++) {
            JMenuItem menuItem = new JMenuItem(actions[i]);
            menuItem.setIcon(null); //arbitrarily chose not to use icon
            menu.add(menuItem);
        }
		
	    // Install the menu bar in the frame
	    setJMenuBar(magicMenuBar);
	}

	public void setupRoombaControlPanel(){
		
		//first get the number of roombas from the config file 
		//then, lookup the comm port number from the idmap for each roomba
		//create a roomba panel for each of the comm numbers 
		//add panels to the main frame 
		int numOfRoomba = ConfigFile.CONFIG_FILE.getIntParam("NUM_ROOMBA");
		
		for(int i=0; i< numOfRoomba; i++){
			String port = ConfigFile.CONFIG_FILE.getParam("ROOMBA"+i+"_PORT");
			// pay more attention
			MagicCardRoombaPanel panel = new MagicCardRoombaPanel(port);
			
			int id = ConfigFile.CONFIG_FILE.getIntParam("ROOMBA"+i+"_TAGID");
			if(!MagicCardRoombaPanel.DEBUG_GUI){
				panel.getRoombaController().setRoombaID(id);
				if(id == ConfigFile.CONFIG_FILE.getIntParam("VACUUM_ROOMBA_ID"))
					panel.getRoombaController().addRoombaType(RoombaType.VacuumRoomba);
				if(id == ConfigFile.CONFIG_FILE.getIntParam("PUSH_ROOMBA_ID"))
					panel.getRoombaController().addRoombaType(RoombaType.PushRoomba);
				if(id == ConfigFile.CONFIG_FILE.getIntParam("PICKUP_ROOMBA_ID"))
					panel.getRoombaController().addRoombaType(RoombaType.TagPickupRoomba);
				if(id == ConfigFile.CONFIG_FILE.getIntParam("PRINT_ROOMBA_ID"))
					panel.getRoombaController().addRoombaType(RoombaType.PrinterRoomba);
				}
			bottomBox.add(panel);
			ROOMBAS.add(panel.getRoombaController());
		}
		
		//bottomBox.setPreferredSize(new Dimension(400, 200));
		//ControlScrollPane = new JScrollPane(bottomBox);
	}
	
	public JComponent createTagView(){
		//listModel = new DefaultListModel();
	    
	    listData = new Vector<String>();
	    listData.add("Not found");
	    tagList = new JList(listData);
	    
		tagScrollPane = new JScrollPane();
	    tagScrollPane.getViewport().add(tagList);
		tagScrollPane.validate();
		//topBox.add(tagScrollPane);
		return tagScrollPane;
	}
	
	public void createTaskPane(ArrayList<HouseworkTask> tasks){
		
		
		
		if(tasks == null) {
			G.debug("no tasks are found in the environment ... ");
			return;
		}
		//now, we know the list is already sorted 
		for(int i=0; i< tasks.size(); i++){
			HouseworkTask tempTask = tasks.get(i);
			//here, we will create a task panel
			TaskComponent aComp = new TaskComponent(tempTask.toString(), tempTask.getController(), taskmanager);
			aComp.setOrder(i);
			taskBox.add(aComp);
		}
		taskBox.revalidate();
		taskBox.repaint();
	}
	
	// the main method of getting the stable tags
	public Vector<Tag> getStableTag()
	{
		
		int maxTagID  = tsystem.getDefaultDictionary().maxId();
		int numOfLoops = 20;
		int sleepInterval = 50;
		
		
		Vector<Tag> tempTagtable = new Vector<Tag>();
		Vector<Tag> finalTagtable = new Vector<Tag>();
	
	
		int[] index= new int[maxTagID+1];
		for (int i=0;i<numOfLoops;i++)
		{
			tempTagtable = tsystem.getTagTable();
			
			for(int j=0;j<tempTagtable.size(); j++)
				finalTagtable.addElement(tempTagtable.elementAt(j));
			try {
				Thread.currentThread().sleep(sleepInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		for (int j=0;j<numOfLoops;j++)
			for(int i=0; i< (maxTagID+1); i++)
				if(finalTagtable.elementAt(j*(maxTagID+1)+i) != null && finalTagtable.elementAt(j*(maxTagID+1)+i).found > TrackingSystem.FOUND_THRESHOLD)
				{
					index[i]+=1;					
				}
		tempTagtable = tsystem.getTagTable();

		for (int i=0;i<(maxTagID+1);i++)
		{
			if (index[i]<TrackingSystem.FOUND_THRESHOLD)
			{
				if (index[i]!=0)
				{
					//System.out.println("No of Tag less than threshold is: "+ i);	
					//System.out.println("the index is "+ index[i]);
				}
				
				tempTagtable.elementAt(i).found=0;
			}
		}
		return tempTagtable;
	}
	
	
	//================================
	//Tracking related methods
	//================================
	
	/**
	 * update the tag information to the tag view 
	 */
	public void updateTrackingInfo(){
		Vector<Tag> tagTable = tsystem.getTagTable();
		int n= tagTable.size();
		ArrayList<String> list = new ArrayList<String>();
		for(int i= 0; i< n; i++){
			if(tagTable.elementAt(i)== null) continue;
			if(tagTable.elementAt(i).found > TrackingSystem.FOUND_THRESHOLD){
				String line;
				StringBuffer buf = new StringBuffer();
				Tag tempTag = tagTable.elementAt(i);
				buf.append(tempTag.id + ",");
				buf.append(tempTag.type.toString()+",");
				buf.append(tempTag.name+",");
				buf.append("("+tempTag.position.x+", "+tempTag.position.y+")");
				buf.append("(" + tempTag.orientation.x + ", " + tempTag.orientation.y + ")");
				buf.append(tempTag.size);
				list.add(buf.toString());
			}//end of if
		}//end of for
		//what should I do? I am not exactly sure
		if(list.size() > 0){
			listData.clear();
			
			for(int i=0; i<list.size(); i++){
				listData.add(i, list.get(i));
			}//end of for loop
			tagList.setListData(listData);
			tagScrollPane.revalidate();
			tagScrollPane.repaint();
			
			//repaint();
		}//end of if size > 0
	}//end of updateTrackingInfo method
	
	public static MagicCardRoombaController getRoombaByID(int id){
		if(ROOMBAS.size() == 0) return null;
		else{
			for(int i=0; i< ROOMBAS.size(); i++){
				if(ROOMBAS.get(i).getRoombaId() == id) return ROOMBAS.get(i);
			}//end of for loop
			return null;
		}//end of else
	}//end of method
	
	public static MagicCardRoombaController getFirstRoombaByType(MagicCardRoombaController.RoombaType type){
		if(ROOMBAS.size() == 0) return null;
		else{
			for(int i=0; i< ROOMBAS.size(); i++){
				if(ROOMBAS.get(i).containsRoombaType(type)) return ROOMBAS.get(i);
			}//end of for loop
			return null;
		}//end of else
	}
	
	/**
	 * Main method of entry to the UI system. 
	 * @param args
	 */
	public static void main(String[] args){
		
		UISystem magicCardUI = new UISystem();
	}
	
}
