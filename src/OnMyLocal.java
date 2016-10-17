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

public class OnMyLocal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
		String output ="S:/(S) SHARE/_project CTRE/1_Active Research Projects/Iowa DOT OTO Support/14_Traffic Critical Projects 2/2016/IWZ Data for Tableau/CSV Tableau All in One/backup_2016_07_24/Historical Raw - Copy.csv";
		PrintStream out = new PrintStream(new FileOutputStream(output, true));
		System.setOut(out);
		BufferedReader br0 = new BufferedReader(new FileReader("S:/(S) SHARE/_project CTRE/1_Active Research Projects/Iowa DOT OTO Support/14_Traffic Critical Projects 2/2016/IWZ Data for Tableau/CSV Tableau All in One/backup_2016_07_24/Historical Raw.csv"));
        String line0;
        int cnt = 0;
        while ((line0 = br0.readLine()) != null) 
        {
        	cnt++;
        	if (cnt>1){
	        	String[] nodes = line0.split(",");
	        	
	        	String strDate = nodes[1].trim();
				
				//String strDate = "6/13/2016 1:09:55 AM";
			    SimpleDateFormat sdfSource = new SimpleDateFormat("MM/dd/yyyyy");
			    Date day = sdfSource.parse(strDate);
			    Date startday = sdfSource.parse("6/13/2016");
			    Date endday = sdfSource.parse("7/03/2016");
			    Date specday1 = sdfSource.parse("6/22/2016");
			    Date specday2 = sdfSource.parse("6/23/2016");
				
			    String group = nodes[10].trim();
			    
			    if (day.before(startday) | day.after(endday))
			    {
			    	System.out.println(line0);
			    }
			    else if ((day.equals(specday1) | day.equals(specday2)) & (group.equals("Group 5 - 4.1 - I29") | group.equals("Group 5 - 4.1 - I80")))
			    {
			    	System.out.println(line0);
			    }
        	}
        	      	
        }
        br0.close();
		}
	 catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}

}
