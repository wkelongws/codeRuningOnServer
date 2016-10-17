import com.jcraft.jsch.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class IWZPerformanceCalculation 
{
public static void main(String[] args) throws Exception 
{
    String user = "team";
    String password = "ctr3intrans";
    String host = "10.29.19.56";
    int port=22;    

    try
    {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        System.out.println("Establishing Connection...");
        session.connect();
        System.out.println("Connection established.");
        System.out.println("Crating SFTP Channel.");
        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();
        System.out.println("SFTP Channel created.");
        
        // estabish a HashMap to store IWZ list information
        HashMap<String, HashMap<String,String>> IWZList = new HashMap<String, HashMap<String,String>>();
        InputStream IWZlist= sftpChannel.get("Shuo/IWZ_Sensor_List_withOrder.csv");
        BufferedReader br0 = new BufferedReader(new InputStreamReader(IWZlist));
        String line0;
        while ((line0 = br0.readLine()) != null) 
        {
        	String[] columns0 = line0.split(",");
        	IWZList.put(columns0[8], new HashMap<String,String>());        	
        }
        br0.close();
        InputStream IWZlist1= sftpChannel.get("Shuo/IWZ_Sensor_List_withOrder.csv");
        BufferedReader br1 = new BufferedReader(new InputStreamReader(IWZlist1));
        String line1;
        while ((line1 = br1.readLine()) != null) 
        {
        	String[] columns1 = line1.split(",");
        	HashMap<String,String> sensorInfo = IWZList.get(columns1[8]);
        	sensorInfo.put(columns1[5], line1);
        	IWZList.put(columns1[8], sensorInfo);        	
        }
        br1.close();
        //for(String k:IWZList.keySet()){System.out.println(k+":"+IWZList.get(k));}

        
        // Get a listing of the remote directory   
        sftpChannel.cd("Shuo/IWZ"); 
        Vector<ChannelSftp.LsEntry> list = sftpChannel.ls(".");       
        
        // iterate through objects in list, identifying specific file names
        for (ChannelSftp.LsEntry oListItem : list) {
            
            // If it is a file (not a directory)
            if (!oListItem.getAttrs().isDir()) {
                            	
                System.out.println("get: " + oListItem.getFilename());
                //c.get(oListItem.getFilename(), oListItem.getFilename());  // while testing, disable this or all of your test files will be grabbed
                String remoteFile = oListItem.getFilename();                
                
                // estabish a list to store IWZ 5min data
                TreeMap<Integer,HashMap<String,String>> IWZData = new TreeMap<Integer,HashMap<String,String>>();  
                final HashMap<String,String> sensorsInfo = IWZList.get(remoteFile.split(".txt")[0]);
                HashMap<String,String> tempo = new HashMap<String,String>();
                for(String k:sensorsInfo.keySet()){tempo.put(k, sensorsInfo.get(k).split(",")[6]);} 
                
        		for (int h=0; h<24; h++)
        		{
        			for (int m5=0; m5<12; m5++)
        			{
        				IWZData.put(h*100+m5,new HashMap(){{for(String k:sensorsInfo.keySet()){put(k, sensorsInfo.get(k).split(",")[6]);}}});
        			}
        		}                                               
                
                // estabish a Hashmap to store IWZ perforamnce measures
                HashMap<String, String[]> IWZPerformance = new HashMap<String, String[]>();                
                
                InputStream out= null;
                out= sftpChannel.get(remoteFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(out));
                String line;
                //String localFile = remoteFile.split(".txt")[0] + ".csv";
                String localFile = "Historical Raw.csv";
                String localDir = "S:/(S) SHARE/_project CTRE/1_Active Research Projects/Iowa DOT OTO Support/14_Traffic Critical Projects 2/2016/IWZ Data for Tableau/CSV Tableau All in One/";
                System.out.println("append to: " + localFile);
                
                FileWriter pw = new FileWriter(localDir + localFile,true);
                
                File src=new File(localDir + localFile);
                System.out.println(src.exists());
                
                while ((line = br.readLine()) != null) 
                {
                	//System.out.println(line);
                	String[] columns = line.split("\t");
                	
                	String name = columns[0];
                	String dateStr = columns[1];
                	String hour = columns[2];
                	String min5 = columns[3];
                	String speed = columns[4];
                	String count = columns[5];
                	String occup = columns[6];
                	String issue = columns[7];
                	
                	String group_old = columns[8];
                	
                	String group = columns[9];
                	String direction = columns[10];
                	String coded_direction = columns[11];
                	String order = columns[12];
                	        	
            		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
            		Date date = (Date)formatter.parse(dateStr); 
            		Calendar c = Calendar.getInstance();
            		c.setTime(date);
            		String dayofweek = new SimpleDateFormat("EE").format(date);		
            		String timestamp = dateStr + " " + hour + ":" + Integer.toString(Integer.parseInt(min5)*5) + ":00";
            		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");    		
            		String fechaStr = timestamp;  
            		Date fechaNueva = format.parse(fechaStr);
            		String timestamp_formated = format.format(fechaNueva);
            		
            		int timeIndex = Integer.parseInt(min5)+Integer.parseInt(hour)*100;            		
            		            		
            		String info = IWZData.get(timeIndex).get(name);
            		
            		String segmentLength = null;
            		
            		if (info.split(",").length==1)
            		{
            			segmentLength = info;
            		}
            		else if (info.split(",").length==4)
            		{
            			segmentLength = info.split(",")[3];
            		}       
            		            		
            		IWZData.get(timeIndex).put(name, coded_direction+","+speed+","+count+","+ segmentLength);

            		//Thread.sleep(20000);
            		                                       
                }
                
                pw.flush();
                pw.close();
                br.close();
                
//                for(int k:IWZData.keySet())
//        		{System.out.println(k + ":" +IWZData.get(k));}
                //Thread.sleep(20000);
                
                int eventCnt1 = 0;
                int currentEventFlag1 = 0;
                int prevEventFlag1 = 0;
                int eventCnt2 = 0;
                int currentEventFlag2 = 0;
                int prevEventFlag2 = 0;
                HashMap<Integer,String> Events = new HashMap<Integer,String>();
                
                for (int timeKey:IWZData.keySet())
        		{
        			int eventID1 = 0;
        			double travelTime1 = 0.0;
        			double refTravelTime1 = 0.0;
        			double delay1 = 0.0;
        			int eventID2 = 0;
        			double travelTime2 = 0.0;
        			double refTravelTime2 = 0.0;
        			double delay2 = 0.0;
        			double refSpeed = 65.0;
                	prevEventFlag1 = currentEventFlag1;
                	currentEventFlag1 = 0;
                	prevEventFlag2 = currentEventFlag2;
                	currentEventFlag2 = 0;
                	
                	//System.out.println(key + ":" +IWZData.get(key));
                	HashMap<String,String> oneSensorData = IWZData.get(timeKey);
                	for (String sensorKey:oneSensorData.keySet())
                	{
                		String speedcount = oneSensorData.get(sensorKey);
                		
                		double speed = 0.0;
                		int count = 0;
                		double length = 0.0;
                		int direction = 0;
                		if (speedcount.split(",").length==4)
                		{
                			direction = Integer.parseInt(speedcount.split(",")[0]);
	                		speed = Double.parseDouble(speedcount.split(",")[1]);
	                		count = Integer.parseInt(speedcount.split(",")[2]);
	                		length = Double.parseDouble(speedcount.split(",")[3]);;
                		}
                		
                		System.out.println(sensorKey + "_" + timeKey + "_" + speed + "_" + count);
                		
                		if(direction==1){if (speed<45 & speed>0){currentEventFlag1 = 1;}}
                		else if(direction==2){if (speed<45 & speed>0){currentEventFlag2 = 1;}}
                	}
                	
                	if (currentEventFlag1>prevEventFlag1){eventCnt1++;}
                	if (currentEventFlag1>0){eventID1 = eventCnt1;}
                	
        		}
            }
        }
        
        sftpChannel.disconnect();
        session.disconnect();
    }
    catch(Exception e)
{
    System.out.println(e);
}
}
}