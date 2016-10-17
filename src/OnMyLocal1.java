import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class OnMyLocal1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
		String output ="S:/(S) SHARE/_project CTRE/1_Active Research Projects/Iowa DOT OTO Support/14_Traffic Critical Projects 2/Report/New Text Document.txt";
		PrintStream out = new PrintStream(new FileOutputStream(output, true));
		System.setOut(out);
		BufferedReader br0 = new BufferedReader(new FileReader("S:/(S) SHARE/_project CTRE/1_Active Research Projects/Iowa DOT OTO Support/14_Traffic Critical Projects 2/Report/07252016_AM.txt"));
        String line0;
        
        while ((line0 = br0.readLine()) != null) 
        {
        	String[] nodes = line0.split(",");
        	int time = Integer.parseInt(nodes[2]);
        	
        	if (time<115940){
	        	
			    	System.out.println(line0);
			    }        	      	
        }
        br0.close();
        
        BufferedReader br1 = new BufferedReader(new FileReader("S:/(S) SHARE/_project CTRE/1_Active Research Projects/Iowa DOT OTO Support/14_Traffic Critical Projects 2/Report/07252016.txt"));
        String line1;
        
        while ((line1 = br1.readLine()) != null) 
        {
        	String[] nodes1 = line1.split(",");
        	int time = Integer.parseInt(nodes1[2]);
        	
        	if (time>=115940){
	        	
			    	System.out.println(line1);
			    }        	      	
        }
        br1.close();
		}
	 catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}

}
