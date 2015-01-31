package magiccard.task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import roomba.MagicCardRoombaController;

import magiccard.Tag;
import magiccard.TagDictionary;
import magiccard.TaskManager;
import magiccard.TrackingSystem;
import magiccard.UISystem;
import magiccard.Vector2D;
import magiccard.task.*;
import magiccard.task.TaskController.Status;

public class TaskCreator {
	private static int DEFAULT_TIME_LIMIT = 60000;
	
	public static final String MEMORIZEDTASK_PREFIX = "MemorizedSetOfTask";
	
	
	public static ArrayList<HouseworkTask> createTasks(ArrayList clusters, TrackingSystem tsystem) {
		//initialize the tracking system here 
		
		
		int n = clusters.size();
		ArrayList<HouseworkTask> tasks = new ArrayList<HouseworkTask>();
		for (int i = 0; i < n; i ++) {
			Tag action = findByType((ArrayList<Tag>) clusters.get(i), Tag.Type.Action);
			
			if (action == null)
				continue;
			
			HouseworkTask t = null;
			try{
			if (action.name.equalsIgnoreCase("VacuumThisRoom1") ||
				action.name.equalsIgnoreCase("VacuumThisRoom2"))
				t = createVacuumRoom(i, action, clusters, tsystem);
			else if (action.name.equalsIgnoreCase("AdditionalVacuum@ThisSpot1") ||
					 action.name.equalsIgnoreCase("AdditionalVacuum@ThisSpot2"))
				t = createVacuumMore(i, action, clusters, tsystem);
			else if (action.name.equalsIgnoreCase("SendThisTo\"DestinationA\"") ||
					 action.name.equalsIgnoreCase("SendThisTo\"DestinationB\""))
				t = createPush(i, action, clusters, tsystem);
			else if (action.name.equalsIgnoreCase("SendThisTo\"Stations\""))
				t = createSend(i, action, clusters, tsystem);
			else if (action.name.equalsIgnoreCase("Bring\"items\"here"))
				t = createBring(i, action, clusters, tsystem);
			else if(action.name.startsWith("MemorizedSetOfTask"))
				t = createCompound(i, action, clusters, tsystem);
			
			}catch(Exception e){
				e.printStackTrace();
			}			
			if (action.name.equalsIgnoreCase("AvoidThisObject1") ||
			 		action.name.equalsIgnoreCase("AvoidThisObject2"))
				{
					t.setAvoidFlag(true);
					System.out.println("this tasklist include avoid task!");
				}
			if(t!=null)
				tasks.add(t);
		}
		return tasks;
	}
	
	private static Tag findByType(ArrayList<Tag> tags, Tag.Type type) {
		int n = tags.size();
		for (int i = 0; i < n; i++) {
			Tag t = tags.get(i);
			if (t.type == type)
				return t;
		}
		return null;
	}
	
	private static Tag findByType(ArrayList<Tag> tags, Tag t, Tag.Type type) {
		double dmin = 1.0e27;
		Tag ret = null;
		int n = tags.size();
		for (int i = 0; i < n; i++) {
			if (tags.get(i).type != type)
				continue;
			double d = Vector2D.distance(t.position, tags.get(i).position);
			if (d < dmin) {
				dmin = d;
				ret = tags.get(i);
			}
		}
		return ret;
	}
	
	private static Tag findByType2(ArrayList tags, Tag.Type type) {
		int n1 = tags.size();
		for (int i = 0; i < n1; i++) {
			int n2 = ((ArrayList<Tag>) tags.get(i)).size();
			for (int j = 0; j < n2; j++) {
				if (((ArrayList<Tag>) tags.get(i)).get(j).type == type)
					return ((ArrayList<Tag>) tags.get(i)).get(j);
			}
		}
		return null;
	}
	
	private static Tag findByType2(ArrayList tags, Tag t, Tag.Type type) {
		int n1 = tags.size();
		double dmin = 1.0e27;
		Tag ret = null;
		for (int i = 0; i < n1; i++) {
			int n2 = ((ArrayList<Tag>) tags.get(i)).size();
			for (int j = 0; j < n2; j++) {
				if (((ArrayList<Tag>) tags.get(i)).get(j).type != type)
					continue;
				
				double d = Vector2D.distance(t.position, ((ArrayList<Tag>) tags.get(i)).get(j).position);
				if (d < dmin) {
					dmin = d;
					ret = ((ArrayList<Tag>) tags.get(i)).get(j);
				}
			}
		}
		return ret;
	}
	
	private static Tag findByName(ArrayList<Tag> tags, String name) {
		int n = tags.size();
		for (int i = 0; i < n; i++) {
			if (tags.get(i).name.equalsIgnoreCase(name))
				return tags.get(i);
		}
		return null;
	}
	
	private static Tag findByname2(ArrayList<Tag>[] tags, Tag t, String name) {
		int n1 = tags.length;
		double dmin = 1.0e27;
		Tag ret = null;
		for (int i = 0; i < n1; i++) {
			int n2 = tags[i].size();
			for (int j = 0; j < n2; j++) {
				if (tags[i].get(j).name != name)
					continue;
				double d = Vector2D.distance(t.position, tags[i].get(j).position);
				if (d < dmin) {
					dmin = d;
					ret = tags[i].get(j);
				}
			}
		}
		return ret;
	}
	
	private static int findOrder(Tag t, ArrayList<Tag> tags) {
		int n = tags.size();
		double dmin = 1.0e27;
		Tag found = null;
		for (int i = 0; i < n; i++) {
			Tag tag = tags.get(i);
			if (tag.type == Tag.Type.Order) {
				double d = Vector2D.distance(t.position, tag.position);
				if (d < dmin) {
					dmin = d;
					found = tag;
				}
			}
		}
		if (found == null)
			return HouseworkTask.NO_ORDER;
		else if (found.name.equalsIgnoreCase("DoThisFirst"))
			return HouseworkTask.DO_1ST;
		else if (found.name.equalsIgnoreCase("DoThisSecond"))
			return HouseworkTask.DO_2ND;
		else if (found.name.equalsIgnoreCase("DoThisThird"))
			return HouseworkTask.DO_3RD;
		else if (found.name.equalsIgnoreCase("DoThisFourth"))
			return HouseworkTask.DO_4TH;
		else
			return HouseworkTask.NO_ORDER;
	}
	
	private static HouseworkTask createVacuumRoom(int i, Tag action, ArrayList tags, TrackingSystem tsystem) {
		VacuumRoomTask t = new VacuumRoomTask();
		t.setType( HouseworkTask.Type.VACUUMROOM );
		t.setName( action.name );
		t.setOrder( findOrder(action, (ArrayList<Tag>) tags.get(i)) );
		t.setFinished( false );
		t.timeLimit = DEFAULT_TIME_LIMIT;
		
		MagicCardRoombaController roombacomm = UISystem.getFirstRoombaByType(MagicCardRoombaController.RoombaType.VacuumRoomba);
		
		t.setTimeLimit(tsystem.getConfigFile().getIntParam("DEFAULT_VACUUM_ROOM_TIME", 10000));
		VacuumRoomController ctl = new VacuumRoomController(tsystem, roombacomm, roombacomm.getRoombaId(), t, false, null);
		t.setController(ctl);
		
		return t;
	}
	
	private static HouseworkTask createVacuumMore(int i, Tag action, ArrayList tags, TrackingSystem tsystem) {
		VacuumMoreTask t = new VacuumMoreTask();
		t.setType( HouseworkTask.Type.VACUUMSPOT );
		t.setName( action.name );
		t.setOrder( findOrder(action, (ArrayList<Tag>) tags.get(i)) );
		t.setFinished( false );
		t.spot = action.position;
		t.timeLimit = DEFAULT_TIME_LIMIT;
		
		MagicCardRoombaController roombacomm = UISystem.getFirstRoombaByType(MagicCardRoombaController.RoombaType.VacuumRoomba);
		
		if(roombacomm == null){
			VacuumMoreController ctl = new VacuumMoreController(tsystem, roombacomm, -1, t, false, null);
			t.setController(ctl);
			return t;
		}
			
		t.setTimeLimit(tsystem.getConfigFile().getIntParam("DEFAULT_VACUUM_SPOT_TIME", 30000));
		VacuumMoreController ctl = new VacuumMoreController(tsystem, roombacomm, roombacomm.getRoombaId(), t, false, null);
		t.setController(ctl);
		
		return t;
	}
	
	public static HouseworkTask createCollect(TrackingSystem tsystem) {
		CollectTagsTask t = new CollectTagsTask();
		t.setType( HouseworkTask.Type.COLLECT );
		t.setName( "Collect" );
		
		t.setFinished( false );
		MagicCardRoombaController roombacomm = UISystem.getFirstRoombaByType(MagicCardRoombaController.RoombaType.TagPickupRoomba);
		ArrayList<Vector2D> moTagsPositions = new ArrayList<Vector2D>();
		
		int amount=0;
		Vector<Tag> tagtable = tsystem.getTagTable();
		Tag[] myTags = new Tag[tagtable.size()];
		if(tagtable != null && tagtable.size() != 0)
		{   	
    		for(int j=0; j< tagtable.size(); j++)//&& tagtable.elementAt(i).found > TrackingSystem.FOUND_THRESHOLD
    			if (tagtable.elementAt(j)!=null && tagtable.elementAt(j).found > 1
    					&& tagtable.elementAt(j).type != Tag.Type.Exception&& tagtable.elementAt(j).type != Tag.Type.Object)
    			{
    				myTags[j] = tagtable.elementAt(j);
    				amount++;
    				System.out.println("the tag ("+ j + ") position is ("+ myTags[j].position.x +" , " + myTags[j].position.y+")");
    			}
    	}
		if (myTags!=null)
			t.addTagsPosition(myTags);
		
		CollectTagsController ctl = new CollectTagsController(tsystem, roombacomm, roombacomm.getRoombaId(), t, null);
		
		t.setController(ctl);
		return t;
	}
	
	
private static HouseworkTask createPush(int i, Tag action, ArrayList tags, TrackingSystem tsystem) {
		PushTask t = new PushTask();
		t.setType( HouseworkTask.Type.PUSH );
		t.setName( action.name );
		t.setOrder( findOrder(action, (ArrayList<Tag>) tags.get(i)));
		t.setFinished( false );
		Tag obj = findByType((ArrayList<Tag>) tags.get(i), action, Tag.Type.Object);
		if (obj == null)
			return null;
		
		Tag dest = null;
		for (int j = 0; j < tags.size(); j++) {
			dest = findByName((ArrayList<Tag>) tags.get(j),
					(action.name.equalsIgnoreCase("SendThisTo\"DestinationA\""))
					? "\"DestinationA\"" : "\"DestinationB\"");
			if (dest != null)
				break;
		}
		t.objectID = obj.id;
		t.goal = dest.position;
		
		MagicCardRoombaController roombacomm = UISystem.getFirstRoombaByType(MagicCardRoombaController.RoombaType.PushRoomba);
		
		PushController ctl = new PushController(tsystem, roombacomm, roombacomm.getRoombaId(), t, false, null);
		t.setController(ctl);
		
		return t;
	}
	
	private static HouseworkTask createSend(int i, Tag action, ArrayList tags, TrackingSystem tsystem) {
		SendTask task = new SendTask();
		ArrayList<Tag> stationtags = new ArrayList<Tag>();
		for (int j = 0; j < tags.size(); j++) {
			for (int k = 0; k < ((ArrayList<Tag>) tags.get(j)).size(); k++) {
				Tag t = ((ArrayList<Tag>) tags.get(j)).get(k);
				if (t.name.matches("Station\\d+"))
					stationtags.add(t);
			}
		}
		if (stationtags.size() == 0)
			return null;
		Collections.sort(stationtags, new Comparator(){
			public int compare(Object o1, Object o2) {
				return ((Tag)o2).id - ((Tag)o1).id;
			}
		});
		ArrayList<Vector2D> stations = new ArrayList<Vector2D>();
		for (int j = 0; j < stationtags.size(); j++) {
			stations.add(stationtags.get(j).position);
		}
		
		Tag obj = findByType((ArrayList<Tag>) tags.get(i), action, Tag.Type.Object);
		if (obj == null)
			return null;
		task.setType( HouseworkTask.Type.SEND );
		task.setName( action.name );
		task.setOrder( findOrder(action, (ArrayList<Tag>) tags.get(i))) ;
		task.setFinished( false );
		task.objectID = obj.id;
		task.positionList = stations;
		
		MagicCardRoombaController roombacomm = UISystem.getFirstRoombaByType(MagicCardRoombaController.RoombaType.PushRoomba);
		
		SendController ctl = new SendController(tsystem, roombacomm, roombacomm.getRoombaId(), task, false, null);
		task.setController(ctl);
		
		return task;
	}
	
	private static HouseworkTask createBring(int i, Tag action, ArrayList tags, TrackingSystem tsystem) {
		BringTask task = new BringTask();
		ArrayList<Integer> objects = new ArrayList<Integer>();
		for (int j = 0; j < tags.size(); j++) {
			for (int k = 0; k < ((ArrayList<Tag>) tags.get(j)).size(); k++) {
				Tag t = ((ArrayList<Tag>) tags.get(j)).get(k);
				if (t.name.matches("Item\\d+")) {
					Tag obj = findByType2(tags, t, Tag.Type.Object);
					if (obj != null)
						objects.add(obj.id);
				}
			}
		}
		if (objects.size() == 0)
			return null;
		Collections.sort(objects);
		task.setType( HouseworkTask.Type.BRING );
		task.setName( action.name );
		task.setOrder( findOrder(action, ((ArrayList<Tag>) tags.get(i))) );
		task.setFinished( false );
		task.goal = action.position;
		task.itemList = objects;
		
		MagicCardRoombaController roombacomm = UISystem.getFirstRoombaByType(MagicCardRoombaController.RoombaType.PushRoomba);
		BringController ctl = new BringController(tsystem, roombacomm, roombacomm.getRoombaId(), task, false, null);
		task.setController(ctl);
		
		return task;		
	}
	
	
	/**
	 * I need to create compound tasks recursively. Make sure that works. 
	 * @param i
	 * @param action
	 * @param tags
	 * @return
	 */
	private static HouseworkTask createCompound(int i, Tag action, ArrayList tags, TrackingSystem tsystem) {
		CompoundTask task = new CompoundTask();

		task.setType( HouseworkTask.Type.COMPOUND );
		task.setName( action.name);
		task.setOrder( findOrder(action, (ArrayList<Tag>) tags.get(i)));
		task.setFinished( false );
		task.position = action.position;
		
		
		
		//first, get the card number 
		
		//second, retrieve the file based on the card number 
		//third, analysis the card and create them into tags
		ArrayList<Tag> tagsOnFile =  getTagsFromID(action.id, tsystem);
		
		if(tagsOnFile !=null && tagsOnFile.size() > 0){
			//fourth, create them into clusters 
			int numOfTags = tagsOnFile.size();
			Tag[] foundTags = new Tag[numOfTags];
			for(int j=0; j<numOfTags; j++){
				foundTags[j]= tagsOnFile.get(j);
			}//end of for lo op
			
			ArrayList myCluster = TaskManager.makeCluster(foundTags);
			
			ArrayList<HouseworkTask> hTasks = createTasks(myCluster, tsystem);
			
			if(hTasks!=null && hTasks.size()> 0){
				//sort the tasks
				ArrayList<HouseworkTask> sortedTasks = TaskManager.sortTasks(hTasks);
				for(int k=0; k<sortedTasks.size(); k++)
					task.addSubTask(sortedTasks.get(k));
			}//end of if
		}
		
		CompoundTaskController ctl = new CompoundTaskController(tsystem, task, null);
		task.setController(ctl);
		
		return task;		
	}
	
	/**
	 * I need to create compound tasks recursively. Make sure that works. 
	 * @param i
	 * @param action
	 * @param tags
	 * @return
	 */
	public static HouseworkTask createPrintTagTask(int i, Tag action, int id, int order, TrackingSystem tsystem) {
		PrintCompoundTagTask task = new PrintCompoundTagTask();

		task.setType( HouseworkTask.Type.PRINT);
		task.setName("PrintCompoundTag");
		task.setOrder(order);
		task.setFinished( false );
		task.goal = action.position;
		task.idToBePrinted = id;

		//let's go back to the origin, this is a temporary hack
		task.originalPosition = new Vector2D(0, 0);
		
		MagicCardRoombaController roombacomm = UISystem.getFirstRoombaByType(MagicCardRoombaController.RoombaType.PrinterRoomba);
		
		PrintCompoundTagController ctl = new PrintCompoundTagController(tsystem, roombacomm, roombacomm.getRoombaId(), task, false, null);
		task.setController(ctl);
		
		return task;		
	}
	
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	private static ArrayList<Tag> getTagsFromID(int mid, TrackingSystem tsystem){
		TagDictionary dictionary = new TagDictionary(tsystem.getConfigFile().getParam("TAG_DICTIONARY_NAME"));
		ArrayList<Tag> tags = new ArrayList<Tag>();
		
		//StringBuffer text = new StringBuffer();
		 try {
		        BufferedReader in = new BufferedReader(new FileReader(TaskManager.MEMORIZED_FILE_DIR+"/"+MEMORIZEDTASK_PREFIX + mid +".txt"));
		        String str;
		        while ((str = in.readLine()) != null) {
		            //text.append(str +"\n");
		        	
		        	Scanner s = new Scanner(str);
		            s.findInLine("(.+) (.+) \\((.+), (.+)\\) \\((.+), (.+)\\)");
		            MatchResult result = s.match();

		            int id = Integer.valueOf(result.group(2));
		            double position_x = Double.valueOf(result.group(3));
		            double position_y = Double.valueOf(result.group(4));
		            double orientation_x = Double.valueOf(result.group(5));
		            double orientation_y = Double.valueOf(result.group(6));

		            if (!dictionary.contains(id))
		                continue;
		            
		            Tag t = new Tag();
		            t.id = id;
		            t.position.x = position_x;
		            t.position.y = position_y;
		            t.orientation.x = orientation_x;
		            t.orientation.y = orientation_y;
		            t.found = TrackingSystem.FOUND_THRESHOLD + 1;
		            t.type = dictionary.getTag(id).type;
		            t.name = dictionary.getTag(id).name;
		            tags.add(t);
		        }
		        in.close();
		    } catch (IOException e) {
		    }
		    return tags;
	}//end of method
}
