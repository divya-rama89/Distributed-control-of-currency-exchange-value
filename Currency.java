
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;

public class Currency {
static int PID;
	static int nopr;
	static int clk;
	static Clock clock;
	    public static void main(String[] args) throws IOException {
        
        if (args.length != 3) {
            System.err.println(
                "Usage: java Currency <process ID> <#operations> <clock rate>");
            System.exit(1);
        }

        PID = Integer.parseInt(args[0]);
        nopr = Integer.parseInt(args[1]);
        clk = Integer.parseInt(args[2]);
 /* working
        System.out.println("PID"+PID);
        System.out.println("nopr"+nopr);
        System.out.println("clk"+clk);
        */
        
        Timer timer = new Timer();
        clock = new Clock();
        clock.set(clk, PID);
        //Runs Clock.run() every 1000 msecond
        timer.schedule(clock, 0, 1000);
        
//Opening a file to write log
        Client.fwriter = new PrintWriter("log"+PID, "UTF-8");
       // File to arrayList
        
        ArrayList<String> hostList = new ArrayList<String>();
        ArrayList<Integer> portList = new ArrayList<Integer>();
    try {	    
    	Scanner layer = new Scanner(new File("info.txt"));
    int x = 0;
    while (layer.hasNext())
    {
    	String tempHost = layer.next();
        String tempPort = layer.next();
    	hostList.add(new String(tempHost));
    	portList.add(new Integer(tempPort));
    	
        //printing on console - working
       // System.out.println(hostList.get(x)+"  "+portList.get(x));
    	Client.fwriter.println("[P"+x+"]"+hostList.get(x)+":"+portList.get(x));
    	Client.fwriter.flush();
    	x++; 
   	}
   	layer.close();    	
             
	}
    catch (Exception e)
    {
    	 System.out.println("Unable to read file.");
    }
    
    //Creating an instance of the class to represent each process    
    Client P;
        
        //P0 listens, P1 contacts P0 and listens, P2 contacts P0 and P1.
        switch(PID)
        {
        case 0:
        default:
        	//System.out.println("case 0");
        	P = new Client(PID,clk,hostList.get(PID),portList.get(PID),hostList.get(1),portList.get(1),hostList.get(2),portList.get(2));
            
           	break;
        case 1:	
        	// System.out.println("case 1");
        	P = new Client(PID,clk,hostList.get(PID),portList.get(PID),hostList.get(0),portList.get(0),hostList.get(2),portList.get(2));
        	
        	break;
        case 2:
        	// System.out.println("case 2");
        	P = new Client(PID,clk,hostList.get(PID),portList.get(PID),hostList.get(0),portList.get(0),hostList.get(1),portList.get(1));
        	
        	break;       	
        }
        
        P.walk();
        
        } 
    }
