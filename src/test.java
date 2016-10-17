import com.jcraft.jsch.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class test
{
public static void main(String[] args) throws Exception 
{


	String a = "I-35/80 EB to 2nd AVENUE-WB,20160803,030620,030640,operational,3,1,1,1,1,47,1,1,null,null,null,null,2,null,null,0,111,null,null,null,null,null,null,3,1,1,2,97,null,null,null,null,1,1";
	String[] nodes = a.split(",");
	int numoflane = nodes.length-6;
	int b = numoflane % 11;
	  
	  System.out.println(numoflane);
	  System.out.println(b);
	  System.out.println(numoflane % 11 == 0);
     
    

}
}