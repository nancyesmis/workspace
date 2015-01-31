package magiccard;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import com.sun.jmx.snmp.tasks.Task;

import magiccard.task.BringController;
import magiccard.task.BringTask;
import magiccard.task.CollectTagsTask;
import magiccard.task.CompoundTask;
import magiccard.task.CompoundTaskController;
import magiccard.task.CollectTagsController;
import magiccard.task.HouseworkTask;
import magiccard.task.PrintCompoundTagTask;
import magiccard.task.PushController;
import magiccard.task.PushTask;
import magiccard.task.SendController;
import magiccard.task.SendTask;
import magiccard.task.TaskCreator;
import magiccard.task.TaskController;
import magiccard.task.VacuumMoreController;
import magiccard.task.VacuumMoreTask;
import magiccard.task.VacuumRoomController;
import magiccard.task.VacuumRoomTask;
import roomba.MagicCardRoombaController;
import magiccard.G;



public class TaskManager extends Observable implements Observer {
	
	public static final String MEMORIZED_FILE_DIR = "C:\\MagicCard\\MemorizedFiles";
	public static final String TAG_IMAGE_DIR = "C:\\MagicCard\\";
	
    private static int CONVERGE_ITERATION_MAX = 100;

    private ConfigFile config;
    private TrackingSystem tsystem;
    private ArrayList<HouseworkTask> tasks;
    private ArrayList<HouseworkTask> sortedTasks;
    private int current_order;
    
    /**
     * A task can have the following number of status
     * NotSorted means the task is not yet sorted according to the order
     * Sorted means the task has been sorted
     * NotStarted means it hasn't been started, but has already been sorted
     * Started means it has started
     * Finished means it is finished
     * Canceled means it has started, but has been canceled by the user
     * Failed means it has started, but failed to finish due to an error
     */
    public static enum Signal {NOTSORTED, SORTED, NOTSTARTED, STARTED, FINISHED, CANCELLED, FAILED};
    
    private Signal[] taskStatus;
    
    private int miCurrentTask = -1; //this variable checks to see which task is the first task 

    public TaskManager(ConfigFile config, TrackingSystem tsystem) {
        this.config = config;
        this.tsystem = tsystem;
    }

    /**
     * if these tasks need to be memorized, memorizedID is not 0, and memorizedTag is not null
     * 
     * @param tags
     * @param memorizeID
     * @param memorizeTag
     * @return
     */
    private ArrayList<HouseworkTask> buildTasks(Tag[] tags, int memorizeID, Tag memorizeTag) {
        ArrayList clusters = makeCluster(tags);
        tasks = TaskCreator.createTasks(clusters, tsystem);
      
        if(tasks == null || tasks.size() == 0){
        	G.debug("There are no tasks, returning");
        	return null;
        }
        
        sortedTasks = sortTasks(tasks);
        
        // add by smh, to add the Collect task
           
        CollectTagsTask collectTask = (CollectTagsTask) TaskCreator.createCollect(tsystem);
        sortedTasks.add(0, collectTask);
        System.out.println("Test: the number is "+collectTask.getTagsNum());
        
        
        if(memorizeID !=0 && memorizeTag != null){
        	PrintCompoundTagTask printTask = (PrintCompoundTagTask) TaskCreator.createPrintTagTask(0, memorizeTag, memorizeID, sortedTasks.size(), tsystem);
        	sortedTasks.add(printTask);
        }
        for (int i=0; i<sortedTasks.size();i++)
        {
        	System.out.println("the "+ i +" task is "+sortedTasks.get(i).toString());
        }
        

        return sortedTasks;
    }
    
    /**
     * sort the tasks here
     * @param unsortedTasks
     * @return
     */
    public static ArrayList<HouseworkTask> sortTasks(ArrayList<HouseworkTask> unsortedTasks){
    int numOfTasks = unsortedTasks.size();
	
	G.debug("num of tasks is "+ numOfTasks);
		
	//create a list of sorted tasks to store
	HouseworkTask[] tempSortedTasks = new HouseworkTask[numOfTasks];
	//create a list to store the status of these tasks and initialize them as not yet sorted tasks 
	//taskStatus = new Signal[numOfTasks];
		
	//for (int i = 0; i < numOfTasks; i++) {		 
	//	taskStatus[i] = Signal.NOTSORTED;
	//}//end of the first for loop 
	
	//this way, I have sorted the tasks into a sorted list
	int noOrderCount = 0;
	for (int i = 0; i < numOfTasks; i++) {		
		//first, check if there are any orders for the tasks
		int myOrder = unsortedTasks.get(i).getOrder();
		if(myOrder != HouseworkTask.NO_ORDER){ 
			tempSortedTasks[myOrder] = unsortedTasks.get(i);
			//taskStatus[myOrder] = Signal.SORTED;
		}//end of if
		else{
			//add the task to the end of the list
			tempSortedTasks[numOfTasks - noOrderCount - 1]= unsortedTasks.get(i);
			//taskStatus[numOfTasks - noOrderCount - 1] = Signal.SORTED;
			noOrderCount++;
		}//end of else
	}//end of the second for loop 
	
	ArrayList<HouseworkTask> mySortedTasks = new ArrayList<HouseworkTask>();
	for(int i=0; i< numOfTasks; i++){
		mySortedTasks.add(tempSortedTasks[i]);
	}
	return mySortedTasks;
    }//end of method

    private static boolean isClusterHead(Tag t) {
    	return t.found > 0 && (t.type == Tag.Type.Action || t.type == Tag.Type.Compound);
    }

    private static Vector2D gravityPoint(ArrayList<Tag> cluster) {
        Vector2D p = new Vector2D(0.0, 0.0);
        int n = cluster.size();
        for (int i = 0; i < n; i++) {
            p.add( cluster.get(i).position );
        }
        p.multiply(1.0 / n);
        return p;
    }

    private static ArrayList makeInitialCluster(Tag[] tags) {
        int n = tags.length;
        int num_cluster = 0;
        for (int i = 0; i < n; i++) {
            Tag t = tags[i];
            if (t != null && t.found > TrackingSystem.FOUND_THRESHOLD && t.type != Tag.Type.Exception && isClusterHead(t))
                num_cluster++;
        }
        
        G.debug("number of cluster is "+ num_cluster);
      //printTags(tags);

        //ArrayList<Tag>[] clusters = (ArrayList<Tag>[]) Array.newInstance(ArrayList.class, num_cluster);
        ArrayList clusters = new ArrayList();
        
        if (num_cluster == 0)
        	return clusters;
        
        for (int i = 0; i < num_cluster; i++)
        	clusters.add(new ArrayList<Tag>());
        
        for (int i = 0, j = 0; i < n; i++) {
            Tag t = tags[i];
            if (t!=null && t.found > TrackingSystem.FOUND_THRESHOLD && t.type != Tag.Type.Exception) {
            	 ((ArrayList<Tag>) clusters.get(j)).add(t);
            	 if (isClusterHead(t) && j < num_cluster-1)
                     j++;
            }
        }
        return clusters;
    }

    private static ArrayList converge(ArrayList clusters) {
        int num_cluster = clusters.size();
        int count = 0;
        boolean changed = true;
        Vector2D[] gpoints = new Vector2D[num_cluster];
        ArrayList new_clusters = new ArrayList();
        
        for (int i = 0; i < num_cluster; i++)
        	new_clusters.add(new ArrayList<Tag>());

        while (count++ < CONVERGE_ITERATION_MAX && changed) {
            changed = false;
            for (int i = 0; i < num_cluster; i++)
                gpoints[i] = gravityPoint((ArrayList<Tag>) clusters.get(i));

            for (int i = 0; i < num_cluster; i++) {
                ArrayList<Tag> c = (ArrayList<Tag>) clusters.get(i);
                int n = c.size();
                for (int j = 0; j < n; j++) {
                    double dmin = 1.0e27f;
                    int nearest = 0;
                    if (isClusterHead(c.get(j))) {
                        nearest = i;
                    } else {
                        for (int k = 0; k < num_cluster; k++) {
                            double d = Vector2D.distance(c.get(j).position,
                                    gpoints[k]);
                            if (d < dmin) {
                                dmin = d;
                                nearest = k;
                            }
                        }
                    }
                    if (nearest != i)
                        changed = true;
                    ((ArrayList<Tag>) new_clusters.get(nearest)).add(((ArrayList<Tag>) clusters.get(i)).get(j));
                }
            }
            ArrayList tmp = clusters;
            clusters = new_clusters;
            new_clusters = tmp;
            for (int i = 0; i < num_cluster; i++)
               ((ArrayList<Tag>) new_clusters.get(i)).clear();
        }
        return clusters;
    }

    public static ArrayList makeCluster(Tag[] tags) {
        ArrayList clusters = makeInitialCluster(tags);
        return converge(clusters);
    }
    
    public ArrayList<HouseworkTask> prepareTasks(Vector<Tag> tags){
    	if(tags != null && tags.size() != 0){
    		Tag[] myTags = new Tag[tags.size()];
    		
    		int maxID = 0;
    		Tag memorizeTag = null;
    		
    		StringBuffer textfile = new StringBuffer();
    		
    		boolean memorizeThisTask = false;
    		
    		printTags(tags);
    		
    		for(int i=0; i< tags.size(); i++){
    			
    			//copy tag 
    			myTags[i] = tags.elementAt(i);
		
    			if(myTags[i].found < TrackingSystem.FOUND_THRESHOLD) continue;
    			
    			//now check the tag to see if it's a memorized tag or not. 
    			if(myTags[i].found > TrackingSystem.FOUND_THRESHOLD && myTags[i].name.startsWith("MemorizeThisSetOfTasks")){
    				//I need to memorize this set of tags into a file
    				//first, open the folder and check to see if there is any files
    				//if there is no files stored, start from the first no 
    				memorizeThisTask = true;
    				memorizeTag = myTags[i];
    				
    				G.debug("MemorizeThisTask is now true");
    				
    				//-------- This is trying to get the maxID ----------------
    				File dir = new File(MEMORIZED_FILE_DIR);
    				if(!dir.exists()){
    					boolean success = dir.mkdir();
    					if(!success){
    						G.message("Can not create directory "+MEMORIZED_FILE_DIR);
    						break;
    					}//end of inner if
    				}//end of outer if
    			    
    			    String[] children = dir.list();
    			    
    			    if (children == null || children.length == 0) {
    			        // Either dir does not exist or is not a directory
    			    	maxID = tsystem.getConfigFile().getIntParam("RESERVED_TAG_NO_START");
    			    } else {
    			        for (int j=0; j<children.length; j++) {
    			            // Get filename of file or directory
    			            String filename = children[j];
    			            if(filename.startsWith(TaskCreator.MEMORIZEDTASK_PREFIX)){
    			            	//get the int value from 
    			            	int index = filename.indexOf(".");
    			            	int aID = Integer.parseInt(filename.substring(TaskCreator.MEMORIZEDTASK_PREFIX.length(),index));
    			            	if(aID > maxID) maxID = aID;
    			            }//end of if
    			        }//end of for
    			        maxID += 1; //increase the id by 1 so that it points to the next one  
    			    }//end of else
    			    
    				//else get the greatest no, and add 1 to it 
    				//and save the file 
    				
    			}//end of if
    			
    			textfile.append(myTags[i].getOriginalText()+"\n");
    		}//end of for loop
    		
    		if(memorizeThisTask){
	    		 //now, we know the id of the max file 
			    if(maxID == 0) {
			    	G.message("Check your " +MEMORIZED_FILE_DIR + " directory, something may be wrong, exiting");
			    	System.exit(0);
			    }
			    else if(maxID > tsystem.getConfigFile().getIntParam("RESERVED_TAG_NO_END")){
			    	G.message("Max id is greater than the maximum number allowed, system exiting");
			    	System.exit(0);
			    }//end of else if
			    
			    try {
			        BufferedWriter out = new BufferedWriter(new FileWriter(MEMORIZED_FILE_DIR+ "/" +TaskCreator.MEMORIZEDTASK_PREFIX+maxID+".txt"));
			        //now, I need to write the tasks here 
			        out.write(textfile.toString());
			        out.close();
			    } catch (IOException e) {
			    	G.debug("exception happened " + e.toString());
			    }//end of catch
			    
			    //after memorizing the task, I need to add a new task 
			    return buildTasks(myTags, maxID, memorizeTag);
    		}
    		
    		
    		return buildTasks(myTags, 0, null);
    	}//end of top if statement
    	return null;
    }
    
    public void start() {
    	startTasks();
    }
    
    /**
     * Instead of calling a loop, I will first analyze the tasks, and 
     * create two things: 1. a task list 2. a status list, 
     * The flow will go as the following: 
     * 
     * if(tasks != null && tasks.size() != 0){
     * 	  analyzeTasks
     * 		create a sorted task list 
     *      create a status list (set all the task status to be NotStarted) 
     *   
     *   start the first task 
     *   set the status for the first task to be started 
     *   
     *   //There are four types of status (NotStarted, Started, Finished, Canceled, Failed) 
     *   
     * }
     * else {
     *   return;
     * }
     */
    public void startTasks() {
    	//------------------  start the first task --------------------
    	if (tasks.size() > 0) {
    		startTaskByOrder(0);
    		taskStatus[0] = Signal.STARTED;
    	}
    }//end of startTasks method
    
    		
    /**
     * startTaskByOrder starts a task by its order. 
     */
    public void startTaskByOrder(int order){
    	if(sortedTasks == null || sortedTasks.size() == 0 || sortedTasks.size() <= order)
    		return;
    	else{
    		TaskController ctrl = sortedTasks.get(order).getController();
    		ctrl.addObserver(this);
    		ctrl.start();
    		miCurrentTask = order;
    		//taskStatus[miCurrentTask] = Signal.STARTED;
    	}//end of else
    }//end of startTask 
    
    /**
     * startTaskByOrder starts a task by its order. 
     */
    public void cancelTaskByOrder(int order){
    	if(sortedTasks == null || sortedTasks.size() == 0 || sortedTasks.size() <= order)
    		return;
    	else{
    		TaskController ctrl = sortedTasks.get(order).getController();
    		ctrl.deleteObserver(this);
    		ctrl.stop();
    		miCurrentTask = order;
    		//taskStatus[miCurrentTask] = Signal.CANCELLED;
    	}//end of else
    }//end of startTask 
    
    public void update(Observable ctl, Object arg) {
    	if (arg == TaskManager.Signal.FINISHED) {
    		ctl.deleteObservers();
    		//taskStatus[miCurrentTask] = Signal.FINISHED;
    		miCurrentTask ++;
    		startTaskByOrder(miCurrentTask);
    	}
    }
    
    public int getCurrentTaskIndex(){
    	return miCurrentTask;
    }
    
    public void incrementCurrentTaskIndex(){
    	miCurrentTask++;
    }
    
    public void setCurrentTaskIndex(int index){
    	miCurrentTask = index;
    }
    
    //this is temporary variable for compilation
    //static int VACUUM_ROOMBA_ID = 0;
    //static int PUSH_ROOMBA_ID = 0;
    
    

    public static void printMemorizedTag(int id){
	    try {
	        // Open the image file
	        InputStream is = new BufferedInputStream(
	            new FileInputStream(TAG_IMAGE_DIR+"MemorizedSetOfTask"+id+".png"));
	
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
    }//end of method

    
     public static void printTags(Tag[] tags){
    	 if(tags != null && tags.length > 0){
    		StringBuffer buf = new StringBuffer();
    		buf.append("[");
    		for(int i=0; i< tags.length; i++){
    			if(tags[i] != null && tags[i].found > TrackingSystem.FOUND_THRESHOLD){
    				buf.append(tags[i].toString());
    				buf.append(",\n");
    			}//end of inner else
    		}//end of for loop
    		buf.append("]\n");
    		G.debug(buf.toString());
    	 }//end of if
     }//end of printTags method
     
     public static void printTags(Vector<Tag> tags){
    	 if(tags != null && tags.size() > 0){
    		StringBuffer buf = new StringBuffer();
    		buf.append("[");
    		for(int i=0; i< tags.size(); i++){
    			if(tags.elementAt(i) != null && tags.elementAt(i).found > TrackingSystem.FOUND_THRESHOLD){
    				buf.append(tags.elementAt(i).toString());
    				buf.append(",\n");
    			}//end of inner else
    		}//end of for loop
    		buf.append("]\n");
    		G.debug(buf.toString());
    	 }//end of if
     }//end of printTags method
 } //end of class
