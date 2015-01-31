import java.io.File;


public class ChangeDirectory {
	public static void main(String[] args) {
		String filename = "C:\\Users\\Lanke\\Desktop\\emotions";
		File file = new File(filename);
		if(file.isDirectory())
		{
			String[] filelist = file.list();
			for(int i=0;i<filelist.length;i++)
			{
				String oneFileName = filename + "\\"+filelist[i];
				File oneFile = new File(oneFileName);
				System.out.println(oneFileName);
				
				int index = oneFileName.lastIndexOf('.');
				oneFileName = oneFileName.substring(0, index);
			
				oneFileName = oneFileName + ".gif";
				System.out.println(oneFileName);
				File modify = new File(oneFileName);
				oneFile.renameTo(modify);
				
			}
			
		}
	}
}
