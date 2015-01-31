/**
 * 
 */
package magiccard.task;

import java.util.ArrayList;

import magiccard.Tag;
import magiccard.Vector2D;

/**
 * Collecting all the tags
 * @author Shengdong Zhao
 *
 */
public class CollectTagsTask extends HouseworkTask {
	
	public ArrayList<Tag> myTag = new ArrayList<Tag>();
	
	public String toString(){
		return "Collect tags";
	}
	
	public int getTagsNum()
	{
		return myTag.size();
		
	}
	
	public void addTagsPosition(Tag[] myTags)
	{
		for (int i=0; i<myTags.length ;i++)
			if (myTags[i]!=null)
				myTag.add(myTags[i]);
	}
	
	public ArrayList<Tag> getMyTags()
	{
		return myTag;
	}
	

	public CollectTagsTask createCopy() {
		// TODO Auto-generated method stub
		CollectTagsTask newTask = new CollectTagsTask();
		for (int i=0; i< this.myTag.size();i++)
		{
			newTask.myTag.add(this.myTag.get(i));
			
		}
		return newTask;

	}
}
