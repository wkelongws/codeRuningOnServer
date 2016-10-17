
/*
 * Md Johirul Islam
 * This java Program will write INRIX Files from a Directory  into HDFS
 */
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * 
 * @author mislam
 * This code writes the files from spool directory to hdfs. This file needs to be run using a cron job
 *
 */

public class WriteInrix {
	private static final String FILE_TEXT_EXT = ".txt";
	public static void main(String[] args) throws Exception {
		
		FileSystem hdfs =FileSystem.get(new Configuration());
		Path homeDir=hdfs.getHomeDirectory();
		System.out.println("Home folder -" +homeDir);
		Path workingDir=hdfs.getWorkingDirectory();
		System.out.println(workingDir);
		
			Calendar cal=Calendar.getInstance();
        	int dayt=cal.get(Calendar.DAY_OF_MONTH);
        	int month=cal.get(Calendar.MONTH)+1;
        	int year=cal.get(Calendar.YEAR);
        	String ext=FILE_TEXT_EXT;
    		GenericExtFilter filter = new GenericExtFilter(ext);
    		File curDir = new File(".");
    		String[] list = curDir.list(filter);
    		if(list.length!=0)
    		{
    			for (String file : list) {
					File src=new File(curDir+"/"+file);
					System.out.println(file);
					Path newFolderPath1= new Path(workingDir+"/"+year+"/"+month);
					//newFolderPath=Path.;

					if(!hdfs.exists(newFolderPath1))
					{
					hdfs.mkdirs(newFolderPath1);

					}

					
					
					Path localFilePath1 = new Path(file);
					Path hdfsFilePath1=new Path(newFolderPath1+"/"+file);
					hdfs.copyFromLocalFile(localFilePath1, hdfsFilePath1);
					//Files.delete(src.toPath());
				}
    		}
		 
		}
}

class GenericExtFilter implements FilenameFilter {

	private String ext;

	public GenericExtFilter(String ext) {
		this.ext = ext;
	}

	public boolean accept(File dir, String name) {
		return (name.endsWith(ext));
	}
}
