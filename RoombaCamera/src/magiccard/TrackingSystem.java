package magiccard;

import java.util.*;
import java.util.regex.*;
import java.net.*;
import java.io.*;
import javax.swing.*;

public class TrackingSystem {
	
	public static final boolean USE_VISION_CACHE = false;
    private Vector<Tag> tagtable;
    private TagDictionary dictionary;
    private ConfigFile config;
    
    private VisionLocalCache visionCache;
    public static final int FOUND_THRESHOLD = 15;
    
    //Shen: I will fix the problem of unstable signal from the server
    //currently, there are 2 servers and 4 cameras. It can have two problems
    //Problem 1: if an id is at an edge of the camera, the signal will be unstable
    //the reading of the signal can be one value at one point and another value at another point
    //however, since we have four cameras, it's likely that the same object has a stable reading
    //from another camera, so we need to use that camera's value instead 
    
    //the second problem is that more than one camera can have the reading of an object 
    //since more than one camera can have the reading of the object, the value returned are 
    //not consistent, we need to pick up the first stable reading of the value and only use
    //that value. 
    
    //I will solve this problem in the following ways. 
    //Currently, the value from 4 clients are directly input into the vector 
    //but this will result in unstable value being used. 
    //instead, I will create 4 caches for the 4 cameras, and read values from the 4 caches
    //and compare among them, and only update with the stable value from one camera to the 
    //system

    public TrackingSystem(ConfigFile config) {
        this.config = config;
        dictionary = new TagDictionary(config.getParam("TAG_DICTIONARY_NAME"));
        initializeTagTable();
        
        visionCache = new VisionLocalCache(dictionary);

        int n = config.getIntParam("NUM_VISION_SERVER");
        try {
            for (int i = 0; i < n; i++) {
                String addr = config.getParam("VISION_SERVER" + i + "_ADDR");
                int port = config.getIntParam("VISION_SERVER" + i + "_PORT");
                System.out.println("start TrackingSystem Client " + i);
                System.out.println("  address : " + addr);
                System.out.println("  port    : " + port);
                new Client(new Socket(addr, port), tagtable, dictionary, i, visionCache);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
       
    } 
    
    public TagDictionary getDefaultDictionary(){
    	return dictionary;
    }

    public Vector<Tag> getTagTable() {
        return tagtable;
    }
    
    public ConfigFile getConfigFile(){
    	return config;
    }
    
    /**
     * 
     * @param index (the index of the tag)
     * @return
     */
    public Tag getTag(int index){
    	if(tagtable.elementAt(index).found >10){
    		return tagtable.elementAt(index);
    	}
    	else
    		return null;
    }

    private void initializeTagTable() {
        int n = dictionary.maxId() + 1;
        tagtable = new Vector<Tag>(n);
        tagtable.setSize(n);
        for (int i = 0; i < n; i++) {
            if (dictionary.contains(i)) {
                Tag t = dictionary.getTag(i);
                t.found = 0;
                tagtable.set(i, t);
            }
            else{
            	Tag t = new Tag();
            	t.found = 0;
            	tagtable.set(i, t);
            }//end of else
        }
    }
}

class Client extends Thread {
    
	private Vector<Tag> table;
    private TagDictionary dictionary;
    private Socket socket;
    private BufferedReader in;
    private int miServerNo;
    private VisionLocalCache moCache;

    public Client(Socket socket, Vector<Tag> table, TagDictionary dictionary, int serverNo, VisionLocalCache cache) {
        this.socket = socket;
        this.table = table;
        this.dictionary = dictionary;
        miServerNo = serverNo;
        moCache = cache;
        this.start();
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String info;

            while ((info = in.readLine()) != null)
                parse(info);
            in.close();
            socket.close();
        } catch (Exception e) {
            try {
                socket.close();
            } catch (IOException ex) {
            }
        }
    }

    private void parse(String info) {
    	
    	//G.debug(miServerNo + " " + info);
    	if(!TrackingSystem.USE_VISION_CACHE){
        Scanner s = new Scanner(info);
        //G.debug(miServerNo + " " + info);
        if(info.startsWith("frame")) return;
        
        if (info.startsWith("frame"))
        		return;
        s.findInLine("(.+) (.+) \\((.+), (.+)\\) \\((.+), (.+)\\)");
        MatchResult result = s.match();
        
      

        int id = Integer.valueOf(result.group(2));
        double position_x = Double.valueOf(result.group(3));
        double position_y = Double.valueOf(result.group(4));
        double orientation_x = Double.valueOf(result.group(5));
        double orientation_y = Double.valueOf(result.group(6));

        if (!dictionary.contains(id))
            return;
        Tag t = table.elementAt(id);
        if (t.found == 0 || t.miss > 5) {
        	t.position.x = position_x;
        	t.position.y = position_y;
        	t.orientation.x = orientation_x;
        	t.orientation.y = orientation_y;
        	t.miss = 0;
        } else {
        	double diff = t.position.x - position_x;
        	if (-40 < diff && diff < 40)
        		t.position.x = position_x;
        	else
        		t.miss++;
        	diff = t.position.y - position_y;
        	if (-40 < diff && diff < 40)
        		t.position.y = position_y;
        	else
        		t.miss++;
        
        	diff = Vector2D.get_angle_180(t.orientation, new Vector2D(orientation_x, orientation_y));
        	if (diff < 90) {
        		t.orientation.x = orientation_x;
        		t.orientation.y = orientation_y;
        	} else
        		t.miss++;
        }
        
        t.found++;
        t.setOriginalText(info); //setting the original text. 
        table.set(id, t);
    	}
    	else 
    		moCache.parseInfo(info, miServerNo, table);
    }
}
