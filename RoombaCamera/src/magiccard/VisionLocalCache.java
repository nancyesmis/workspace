package magiccard;

import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.MatchResult;

public class VisionLocalCache {
	
	private static final double OFF_LIMIT_STABLE = 50;

	private static final double OFF_LIMIT_MOVE = 150;

	private static final int ROUND_LIMIT = 5;
	
	private static final int QUEUE_LIMIT = ROUND_LIMIT*3;
	
	private static int TAG_NO_LIMIT = 108; //we only support up to 120 tags for now
	

	//definition
	//frame: server will send out all the tags it detects in a particular camera in a given frame 
	//round: 
	//Since we have 2 servers, and it's possible that one server sends our several frame and the other server
	//the other server send out only 0 or 1 frame. Since we need to get all information from two servers, 
	//we define a round as at least one frame from both servers 
	//bigRound: 
	//bigRound consists of a fixed number of rounds (maybe 3 rounds) 
	
	//The algorithm works as follows:
	//there are 4 cache file for each camera
	//in each round, tags will be stored into each into each server's cache in a queue 
	//and in each round, we will remove a value from the queue of all the tags, this is to ensure no old data 
	//will be stored to be confused with new data. 
	
	TagDictionary moDictionary;
	
	
	Vector<ArrayBlockingQueue<Tag>> cam1Cache = new Vector<ArrayBlockingQueue<Tag>>(); //sever 0, camera 0
	Vector<ArrayBlockingQueue<Tag>> cam2Cache = new Vector<ArrayBlockingQueue<Tag>>(); //server 0, camera 1
	Vector<ArrayBlockingQueue<Tag>> cam3Cache = new Vector<ArrayBlockingQueue<Tag>>(); //server 1, camera 0
	Vector<ArrayBlockingQueue<Tag>> cam4Cache = new Vector<ArrayBlockingQueue<Tag>>(); //server 1, camera 1
	
	private int s1Counter = 0;
	private int s2Counter = 0;
	
	private int round =0;
	
	public VisionLocalCache(TagDictionary dictionary){
		moDictionary = dictionary;
		TAG_NO_LIMIT = moDictionary.maxId()+1;
		initializeCaches();
	}
	
	private void initializeCache(Vector<ArrayBlockingQueue<Tag>> camCache){
		for(int i=0; i<TAG_NO_LIMIT; i++){
			ArrayBlockingQueue<Tag> tempQueue = new ArrayBlockingQueue<Tag>(QUEUE_LIMIT);
			camCache.add(tempQueue);
		}
	}
	
	private void initializeCaches(){
		initializeCache(cam1Cache);
		initializeCache(cam2Cache);
		initializeCache(cam3Cache);
		initializeCache(cam4Cache);
	}
	
	
	public synchronized void parseInfo(String info, int server, Vector<Tag> table){
		if(info.startsWith("frame")){
			
			//G.debug(info + " server is: " + server + " round is: "+ round);
			
			//int frameNumber = Integer.parseInt(info.trim().substring("frame ".length()));
			if(server==0){
				s1Counter++;
			}//end if
			else if(server == 1){
				s2Counter++;
			}
			
			if(s1Counter>=1 && s2Counter >=1){
				round ++;
				
				s1Counter = 0;
				s2Counter = 0;
				
				if(round < QUEUE_LIMIT) {
					s1Counter = 0;
					s2Counter = 0;
					return;
				}
				
				//this statement is added so that time is created so that 
				//there will be time for system to read the table
				//since table is thread safe. 
				if(round % 3 != 0) return;
				
				//G.debug("Accessed by thread " + server);
				//we call this one round
				//check to see if we can add anything here 
				Tag[] tags = new Tag[TAG_NO_LIMIT];
				setTags(tags, cam1Cache);
				setTags(tags, cam2Cache);
				setTags(tags, cam3Cache);
				setTags(tags, cam4Cache);
				
				for(int i=0; i<TAG_NO_LIMIT; i++)
					if(tags[i] != null){
						 Tag t = table.elementAt(i);
					        t.position.x = tags[i].position.x;
					        t.position.y = tags[i].position.y;
					        t.orientation.x = tags[i].orientation.x;
					        t.orientation.y = tags[i].orientation.y;
					        t.found += tags[i].found;
					        t.setOriginalText(tags[i].getOriginalText()); //setting the original text. 
					        table.set(i, t);
						//G.debug("Add tag: " + t.toString());
					}
				
				//then, remove the last item from the queue
				removeOneForAll(tags);
				
				//G.debug("b. Accessed by thread " + server);
				//reset the counters
				
			}//end of if
		}//end of start with frame
		else{
				//G.debug("Info is "+info);
			 	Scanner s = new Scanner(info);
		        
		        s.findInLine("(.+) (.+) \\((.+), (.+)\\) \\((.+), (.+)\\)");
		        MatchResult result = s.match();
		        
		        int cameraId = Integer.valueOf(result.group(1));
		        
		        int id = Integer.valueOf(result.group(2));
		        double position_x = Double.valueOf(result.group(3));
		        double position_y = Double.valueOf(result.group(4));
		        double orientation_x = Double.valueOf(result.group(5));
		        double orientation_y = Double.valueOf(result.group(6));

		        if (!moDictionary.contains(id))
		            return;
		        
		        Tag t = new Tag();
		        t.type = moDictionary.getTag(id).type;
		        t.id = id;
		        t.name = moDictionary.getTag(id).name;
		        t.position.x = position_x;
		        t.position.y = position_y;
		        t.orientation.x = orientation_x;
		        t.orientation.y = orientation_y;
		        t.found++;
		        t.setOriginalText(info); //setting the original text. 
		        
		        addTagByCamera(server, cameraId, id, t);
		        
		}//end of else (not starts with frame
	}//end of method
	
	private void addTagByCamera(int server, int cameraId, int id, Tag t) {

		if(server == 0 && cameraId == 0){
			addToCache(cam1Cache, id, t);
		}
		else if(server == 0 && cameraId == 1){
			addToCache(cam2Cache, id, t);
		}
		else if(server == 1 && cameraId == 0){
			addToCache(cam3Cache, id, t);
		}
		else if(server == 1 && cameraId == 1){
			addToCache(cam4Cache, id, t);
		}
	}
	
	private void addToCache(Vector<ArrayBlockingQueue<Tag>> camCache, int id, Tag t){
		if(camCache.get(id).remainingCapacity() > 0)
			camCache.get(id).add(t);
		else{
			camCache.get(id).poll();
			camCache.get(id).add(t);
		}
	}
	
	//only remove the tag if it's not used last time
	private void removeOneTagForCam(Vector<ArrayBlockingQueue<Tag>> camCache, Tag[] tags){
		for(int i=0; i<TAG_NO_LIMIT; i++){
			if(tags[i] == null){
				ArrayBlockingQueue<Tag> tempQueue = camCache.elementAt(i);
				tempQueue.poll();
			}
		}
	}
	
	private void removeOneForAll(Tag[] tags){
		removeOneTagForCam(cam1Cache, tags);
		removeOneTagForCam(cam2Cache, tags);
		removeOneTagForCam(cam3Cache, tags);
		removeOneTagForCam(cam4Cache, tags);
	}
	
	public void setTags(Tag[] tags, Vector<ArrayBlockingQueue<Tag>> camCache){
		
		for(int i=0; i<TAG_NO_LIMIT; i++){
			ArrayBlockingQueue<Tag> tempQueue = camCache.elementAt(i);
			
			if(tempQueue.size() < QUEUE_LIMIT) continue;
			
			boolean movable = false;
			
			if(moDictionary.getTag(i).type == Tag.Type.Exception || moDictionary.getTag(i).type == Tag.Type.Object)
				movable = true;
			
			Object[] myTags =  tempQueue.toArray();

			Tag t = null;
			
			if(myTags[0] instanceof Tag)
				t = (Tag) myTags[0];
			
			boolean addable = true;
				
			for(int j=0; j< myTags.length; j++){
				if(!(myTags[j] instanceof Tag)) continue;
				else if(t == null){
					t = (Tag) myTags[j];
					continue;
				}
				
				Tag t1 = (Tag) myTags[j];
				double xoff = t1.position.x - t.position.x;
				double yoff = t1.position.y - t.position.y;
				
				if(!movable && addable){
					if(Math.abs(xoff) > OFF_LIMIT_STABLE || Math.abs(yoff) > OFF_LIMIT_STABLE){
						addable = false;
						break;
					}//
				}//
				else if(movable && addable){
					if(Math.abs(xoff) > OFF_LIMIT_MOVE || Math.abs(yoff) > OFF_LIMIT_MOVE){
						addable = false;
						break;
					}//
				}//end of else if
				t = t1;
			}//end of for 
			
			if(addable && tags[i] == null){
				if(t.found < TrackingSystem.FOUND_THRESHOLD)
					t.found = TrackingSystem.FOUND_THRESHOLD + 1;
				tags[i] = t;
			}
			//}//end of if
		}//end of outer for
	}

	public static void main(String[] args){
		
	}
		
}
