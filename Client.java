import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Client {
    
	static int PID;
	static int clk;
	
	String hostName;
	int  portNumber;
	String otherHost1;
	int  otherPort1;
	String otherHost2;
	int  otherPort2;
	
	Socket echoSocket;
    static PrintWriter out1;
    static BufferedReader in1;
    static PrintWriter out2;
    static BufferedReader in2;
    ServerSocket serverSocket;
    
    static int flag1 = 0;
    static int flag2 = 0;
    static String Message1;
    static String Message2;
    
    static int ConnectionEstablished = 0;
    
    static String ReadInput1;
    static String ReadInput2;
    static boolean flagReadInput1 = false;
    static boolean flagReadInput2 = false;
    
	 // Queue to store the currency updates
    static ArrayList<queue> dataQ = new ArrayList<queue>();
    /* column 0 of queue = delta_x, column 1 = delta_y, column 3 = process# of sender, column 4 = operation ID */
    static int dataQCtr = 0;
    static int dataQCtrHead = 0;
	
	//Currency rates
	static int currency_x = 100;
	static int currency_y = 100;
	
	static int updateCtr = 0;
	static boolean completionFlag = false;
	static int allCompletionFlag = 0;
	
	static List<String> queue_in = new ArrayList<String>();
	static int queue_inCtr = 0;
	static int queue_inCtrHead = 0;
	static ArrayList<String> queue_out = new ArrayList<String>();
	static int queue_outCtr = 0;
	static int queue_outCtrHead = 0;
	static volatile ArrayList <randomQ> RandomQ = new ArrayList<randomQ>();
	static volatile int RandomQCtr = 0;
	static volatile int RandomQCtrHead = 0;
	
	public static volatile PrintWriter fwriter;
	
	Thread endThread;
	Thread t3;
	Thread t4;

	Thread t5;
	Thread t6;

	Thread chkAdd;
	Thread tsend;
	Thread treadQ;
	    
	public Client (int p, int clock, String host, int  port, String ohost1, int oport1, String ohost2,  int oport2) {
        PID = p;
		clk = clock;
        hostName = host;
        portNumber = port;
        otherHost1 = ohost1;
        otherPort1 = oport1;
        otherHost2 = ohost2;
        otherPort2 = oport2;
        
        
      //  System.out.println("p has been created with obj + " + " following parameters : PID =" +PID+ "hostName = "+hostName+ " portNumber = "+portNumber+ "otherHost1 ="+otherHost1+ "otherPort1 = "+otherPort1+"otherHost2 ="+otherHost2+ "otherPort2 = "+otherPort2);
        
	}
	
	public void walk() {
		
		if (PID != 2) {
			try {
				//System.out.println("NEW DEBUGS: Creating socket for port = " + portNumber);
				System.out.println("I am PID = " + " bound to " + portNumber);
				serverSocket = new ServerSocket(portNumber);
			} catch (IOException e1) {
				System.out.println("Socket not created!");
				e1.printStackTrace();
			}
		}
		
    	switch(PID)
        {
		
        case 0:
        	
        	Client.fwriter.println(getTimestamp()+" P" + Client.PID + "(" + this.hostName + ") is listening on " + this.portNumber + "...");
        	this.listen(1);
        	Client.fwriter.println("Waiting for all to be connected...");
        	Client.fwriter.flush();
        	//System.out.println("between two listens");
        	this.listen(2);
        	
        	while(Client.ConnectionEstablished < 2)
        	{
        		//System.out.println("ConnectionEstablished = "+Client.ConnectionEstablished);
        		try {
    				Thread.currentThread();
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				System.out.println("Thread not sleeping!");
    				e.printStackTrace();
    			} 	
        	}
        	System.out.println("connection established");
        	break;
		
        case 1:	
        	       	
        	Client.fwriter.println(getTimestamp()+" P" + Client.PID + "(" + this.hostName + ") is listening on " + this.portNumber + "...");
        	Client.fwriter.flush();
        	
        	class myThreadStuff3 implements Runnable {
    	        Client o;
    	        myThreadStuff3(Object o) { this.o = (Client)o; }
    	        public void run() {
    	            o.connect1();
    	        }
    	    }
    		t3 = new Thread(new myThreadStuff3(this));
       		class myThreadStuff4 implements Runnable {
        	        Client o;
        	        myThreadStuff4(Object o) { this.o = (Client)o; }
        	        public void run() {
        	            o.listen(1);
        	        }
        	    }
        		t4 = new Thread(new myThreadStuff4(this));
        	  
        	t4.start();
        	try {
				t4.join();
			} catch (InterruptedException e) {
				System.out.println("Not able to perform the thread join!");
				e.printStackTrace();
			}
        	
        	Client.fwriter.println("Waiting for all to be connected...");
        	Client.fwriter.flush();
        	t3.start(); 
        	
        	while(Client.ConnectionEstablished < 2)
        	{
        		//System.out.println("ConnectionEstablished = "+Client.ConnectionEstablished);
        		try {
    				Thread.currentThread();
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				System.out.println("Not able to perform the thread sleep!");
    				e.printStackTrace();
    			} 	
        	}
        	System.out.println("connection established");
        	
            break;
    		
        case 2:
        	// Keep talking to P0
                	
        	class myThreadStuff5 implements Runnable {
    	        Client o;
    	        myThreadStuff5(Object o) { this.o = (Client)o; }
    	        public void run() {
    	            o.connect1();
    	        }
    	    }
    		t5 = new Thread(new myThreadStuff5(this));
        	
        	t5.start();
        	 Client.fwriter.println("Waiting for all to be connected...");
        	 Client.fwriter.flush();
        	class myThreadStuff6 implements Runnable {
    	        Client o;
    	        myThreadStuff6(Object o) { this.o = (Client)o; }
    	        public void run() {
    	            o.connect2();
    	        }
    	    }
    		t6 = new Thread(new myThreadStuff6(this));
    		try {
				Thread.currentThread();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("Not able to perform the thread sleep!");
				e.printStackTrace();
			} 	
        	t6.start();
        	
        	while(Client.ConnectionEstablished < 2)
        	{
        		//System.out.println("ConnectionEstablished = "+Client.ConnectionEstablished);
        		try {
    				Thread.currentThread();
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				System.out.println("Not able to perform the thread sleep!");
    				e.printStackTrace();
    			} 	
        		
        	}
        	System.out.println("connection established");
        	        	
        	break; 
        	
    	}
	     	
    	//Generate random integers at random intervals after all the connections have been established
        if (Client.ConnectionEstablished == 2) {
          (new randomThread()).start();
         // System.out.println("yes! all connected and random generation started!");
          Client.fwriter.println("All connected.");
          Client.fwriter.flush();
        }
    	
    	//Check for updates from queue containing random numbers for x and y and create an update message to add into thread
    	class chkUpdateThread implements Runnable {
	        Client o;
	        chkUpdateThread(Object o) { this.o = (Client)o; }
	        public void run() {
	            o.ChkUpdateAdd2Q();
	        }
	    }
    	chkAdd = new Thread(new chkUpdateThread(this));
    	chkAdd.start();
    	
    	// send messages to output queue when its free
     	class sendThread implements Runnable {
	        Client o;
	        sendThread(Object o) { this.o = (Client)o; }
	        public void run() {
	         	
	            Client.send();
	        }
	    }
    	
     	tsend = new Thread(new sendThread(this));
     	tsend.setPriority( Thread.NORM_PRIORITY + 1 );
    	tsend.start();
     	
    	// read from input queue for messages and send them to process
    	class readThread implements Runnable {
	        Client o;
	        readThread(Object o) { this.o = (Client)o; }
	        public void run() {
	            o.readfromQ();
	        }
	    }
    	treadQ = new Thread(new readThread(this));
    	treadQ.setPriority( Thread.NORM_PRIORITY + 1 );
    	treadQ.start();
           
    	class endStuff implements Runnable {
	        Client o;
	        endStuff(Object o) { this.o = (Client)o; }
	        public void run() {
	            o.chkAndClose();
	        }
	    }
    	endThread = new Thread(new endStuff(this));
    	
	}
	
	void chkAndClose() {
		// time to send finish message if all processes done
		if ((Client.dataQ.isEmpty()) && Client.completionFlag != true && queue_in.isEmpty() && queue_out.isEmpty() && RandomQ.isEmpty() && (Client.allCompletionFlag <= 2)) { 
			   Client.queue_in.add(new String("3." + Client.PID + ".FIN"));
			   Client.queue_inCtr++;
			   System.out.println("time to send finish!");
			   Client.completionFlag = true;
		       Client.fwriter.println(getTimestamp() + " P"+ Client.PID + " finished.");
		       Client.fwriter.flush();
		       System.out.println(getTimestamp() + " P"+ Client.PID + " finished.");
			
		}
	}
	
	 
   void listen(int ID) {
	   
	   
	   try  {
		   //ServerSocket serverSocket = new ServerSocket(portNumber);
		   
		  // System.out.println("inside listen " + PID + " contacting" + portNumber);
	            new KKMultiServerThread(serverSocket.accept(), ID).start();
	            String x;
	            
	            if (Client.PID == 0 && ID == 1) {Client.fwriter.println(getTimestamp() + " P0 is connected from P1("+ this.otherHost1 + ")") ; } 
	            else { if (Client.PID == 0 && ID == 2) {Client.fwriter.println(getTimestamp() + " P0 is connected from P2("+ this.otherHost2 + ")") ; }
	            else { if (Client.PID == 1 && ID == 1) {Client.fwriter.println(getTimestamp() + " P1 is connected from P2("+ this.otherHost2 + ")") ; }}
	            } 

	            Client.fwriter.flush();
	           // System.out.println("Server that is listening is "+PID);
	    } catch (IOException e) {
           System.err.println("Could not listen on port " + portNumber);
           e.printStackTrace();
           System.exit(-1);
       }
   }

   void connect1()  {
		  
		  String otherHost = this.otherHost1;
	      Integer otherPort = this.otherPort1;
		  try {
	          
			  this.echoSocket = new Socket(otherHost, otherPort);
			  Client.fwriter.println(getTimestamp() + " P" + Client.PID + " is connected to P0 (" + otherHost + ":" + otherPort + ")");
			  Client.fwriter.flush();
			//  System.out.println(this.echoSocket.getPort());
	          Client.out1 = new PrintWriter(this.echoSocket.getOutputStream(), true);
	          
	          Client.in1 =  new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
	          
	        //  System.out.println("I am process " + PID + " trying to contact 0" + "my port = " + echoSocket.getLocalPort());
	        //  Client.out1.println("Hello Process 0. I am Process " + PID + ". Wassup?");
	          
	          Client.out1.println("Begin");
	          	          
	          String temp;
            //  System.out.println("I am Client Process " + PID + "and I got back ");
              while(true) {
    	          if (Client.in1.ready()) {
    	        	  temp = Client.in1.readLine();
    	              if(temp != null) {
    	            	  if(temp.equals("Begin")) {
    	            		  Client.increaseConnCtr();
       	                	}
       	            	  //Push data into queue
    	                  synchronized(Client.queue_out) {    
    	    	           		Client.queue_out.add(temp);
    	    	           		System.out.println("Received: "+Client.queue_out.get(Client.queue_outCtr));
    	    	           		Client.queue_outCtr++;
    	    	        	      	      	     } 	
    	              }
    	          }
    	          
     	          if (Client.flag2 == 1) {
    	        	  Client.out1.println(Client.Message2);
    	        	  Client.flag2 = 0;
    	          }
    	      }
	          
	      } catch (UnknownHostException e) {
	          System.err.println("Don't know about host " + otherHost);
	          System.exit(1);
	      } catch (IOException e) {
	          System.err.println("Couldn't get I/O for the connection to " + otherHost + e.getMessage());
	          System.exit(1);
	      }
	  }
   
   void connect2()  {
		  
		  String otherHost = this.otherHost2;
	      Integer otherPort = this.otherPort2;
		  try {
	          		  
			  this.echoSocket = new Socket(otherHost, otherPort);
			//  System.out.println(this.echoSocket.getPort());
			  Client.fwriter.println(getTimestamp() + " P" + Client.PID + " is connected to P1 (" + otherHost + ":" + otherPort + ")");
			  Client.fwriter.flush();
	          Client.out2 = new PrintWriter(this.echoSocket.getOutputStream(), true);
	          Client.in2 =  new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
	          
	       //   System.out.println("I am process " + PID + " trying to contact 1" + "my port = " + echoSocket.getLocalPort());
		    //  Client.out2.println("Hello Process 1. I am Process " + PID + ". Wassup?");
	         
		      Client.out2.println("Begin");
		      
	          String temp;
	      //    System.out.println("I am Client Process " + PID + "and I got back " + "port socket " + echoSocket);
	          while(true) {
	          if (Client.in2.ready()) {
	        	  temp = Client.in2.readLine();
	              if(temp != null)
	              {
	            	  if(temp.equals("Begin")) {
	            		  Client.increaseConnCtr();
 	                	}
	                  // Push data into queue
	            	  synchronized(Client.queue_out) {    
	    	           		Client.queue_out.add(temp);
	    	           		System.out.println("Received: "+Client.queue_out.get(Client.queue_outCtr));
	    	           		Client.queue_outCtr++;
	    	        	      	      	     } 	
	              }
	        	 
	          }
	          if (Client.flag1 == 1) {
	        	  Client.out2.println(Client.Message1);
	        	  Client.flag1 = 0;
	          }
	          }
	      } catch (UnknownHostException e) {
	          System.err.println("Don't know about host " + otherHost);
	          System.exit(1);
	      } catch (IOException e) {
	          System.err.println("Couldn't get I/O for the connection to " + otherHost);
	          System.exit(1);
	      }
	  }
   
   static void send()
   {
	   while(true) {
		  // String x;
		   
		//   if(queue_in.isEmpty()) { x = ".0" ;} else {x =".1";};
		   
	 //  System.out.println("Send thread"+Client.flag1+Client.flag2 +" "+ Client.queue_inCtrHead +" "+ Client.queue_inCtr+"."+x);
	   
	  // System.out.println("entered send string: "+x);
      // Format: 
	  // Start Message: begin (sent within connection codes)
	  // UPDATE message: 1.PID.delta_x.delta_y.oprnID.clkCntr.timestamp
	  // ACK message: 2.PID.originalSender.oprnID.timestamp
	  // FIN message: 3.PID.FIN.timestamp 
	   
	   String msg =  " ";
	    //Checking queue counter
	   // System.out.println("cnt cnts "+Client.queue_inCtrHead + Client.queue_inCtr);
	   if ((Client.flag1 == 0) && (Client.flag2 == 0) && (Client.queue_inCtrHead < Client.queue_inCtr) && (!(queue_in.isEmpty()))) {
		    Clock.inc();
		   //send message and remove from queue
		   msg = new String(Client.queue_in.get(queue_inCtrHead))+"."+(Double.toString(Clock.get()));
		   queue_inCtrHead++;
		   //System.out.println("queue_inCtr" + queue_inCtr);
		   //System.out.println("queue_inCtrHead" + queue_inCtrHead);
	       System.out.println("Sending: " + msg);
	   
		   Client.Message1 = msg;
		   Client.flag1 = 1;
		   Client.Message2 = msg;
		   Client.flag2 = 1;
		   
		   //add to own data queue if it was an update message
		   String[] splits = msg.split("\\.");
		   if ((Integer.parseInt(splits[0]) == 1)) {
		   int id = Integer.parseInt(splits[1]);
		   int delx = Integer.parseInt(splits[2]);
		   int dely = Integer.parseInt(splits[3]);
		   int uID = Integer.parseInt(splits[4]);
		   double clkcount = Double.parseDouble(splits[5]+"."+splits[6]);
		      
		   
		   synchronized(Client.dataQ) {
			   //System.out.println("Lock in1");
			   queue c;//queue(delx, dely, id, uID, clkcount);
			   c = new queue(delx, dely, id, uID, clkcount);
			   Client.dataQ.add(c);
			 
			   Client.dataQCtr++;
			   System.out.println("dataQCtr" + Client.dataQCtr);
			 //  System.out.println("Lock out1");
			   }
			   
			   // Sorting queue
			   Collections.sort(Client.dataQ,new myCompare());
		   }
	   }
	   
	 /*  // Display 
		if (Client.RandomQCtr == 30) {
			for (int j = Client.dataQCtrHead; j < Client.dataQCtr; j++)
				System.out.println(j+" Q in contents " + Client.dataQ.get(j));
		} */
	   Thread.currentThread();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("Thread could not sleep!");
			e.printStackTrace();
		}
	   
	   
	   }
   }  
   
   void readfromQ()
   {
	   while(true) {
		   
		   Thread.currentThread();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Thread could not sleep!");
				e.printStackTrace();
			}
		   
		   //System.out.println("empty = "+ Client.queue_out.isEmpty());
		   //System.out.println(" Inside readfromQ: Client.queue_outCtrHead" + Client.queue_outCtrHead +"Client.queue_outCtr"+Client.queue_outCtr);
	   if (Client.queue_outCtrHead != Client.queue_outCtr && Client.queue_outCtr!=0 && !Client.queue_out.isEmpty()) {   		
     		
		   System.out.println("Processing   "+Client.queue_out.get(Client.queue_outCtrHead));
     		processMsg(new String(Client.queue_out.get(Client.queue_outCtrHead)));
     		Client.queue_outCtrHead++;
	     } 	
	   }
   }
     
  void processMsg(String recvddata)
   {
	   int type, id, delx, dely, uID, origSender ;
	   double clkcount;
	   
	   System.out.println("In processMsg-Received data "+recvddata);
	   String[] splits = recvddata.split("\\.");
	   	   
	   if(splits.length > 1)
	   {
	   type = Integer.parseInt(splits[0]);
	  
	   switch(type)
	   {
	     
	    case 1:
		   System.out.println("Update msg detected");
		   id = Integer.parseInt(splits[1]);
		   delx = Integer.parseInt(splits[2]);
		   dely = Integer.parseInt(splits[3]);
		   uID = Integer.parseInt(splits[4]);
		   clkcount = Double.parseDouble(splits[5]+"."+splits[6]);
		  
		   //On receiving an Update message
		   //Update queue entry as |delx|dely|senderID|operationID|clok count|
		   synchronized(dataQ) {
		   queue c;//queue(delx, dely, id, uID, clkcount);
		   c = new queue(delx, dely, id, uID, clkcount);
		   dataQ.add(c);
		   
		   Client.dataQCtr++;
		  
		   // Sorting queue
		   Collections.sort(dataQ,new myCompare());
		   }
		   //correct clk
		   Currency.clock.algo(clkcount);
		   
		   //Send ACK
		   synchronized(queue_in) {
		   Client.queue_in.add(new String("2" + "." + Client.PID + "." + Integer.toString(id) + "." + Integer.toString(uID)));
		   Client.queue_inCtr++;
		   System.out.println("queue_inCtr" + queue_inCtr);
		   }
           break;
           
	   case 2:
		   //ACK message
		   System.out.println("ACK msg detected");
		   
		   id = Integer.parseInt(splits[1]);
		   origSender = Integer.parseInt(splits[2]);
		   uID = Integer.parseInt(splits[3]);
		   clkcount = Double.parseDouble(splits[4]+"."+splits[5]);
		   
		   Currency.clock.algo(clkcount);
		   
		   //Update actual values
		   // head of the queue = 0th value
		   if(Client.dataQCtrHead != Client.dataQCtr && !Client.dataQ.isEmpty()) {
		   if ((Client.dataQ.get(Client.dataQCtrHead).id == origSender) && (Client.dataQ.get(Client.dataQCtrHead).uID == uID)) {
			   Client.currency_x = Client.currency_x + Client.dataQ.get(Client.dataQCtrHead).delx;
			   Client.currency_y = Client.currency_y + Client.dataQ.get(Client.dataQCtrHead).dely;
			   Client.dataQCtrHead++;  
			   
			   Client.updateCtr++; //Exclusive for updates
			   Client.fwriter.println(getTimestamp() + " [ OP" + Client.updateCtr + " : C" + Clock.get() +  "] Currency value is set to ( " + Client.currency_x + ","+ Client.currency_y + ") by ( " + dataQ.get(dataQCtrHead-1).delx + "," + dataQ.get(dataQCtrHead-1).dely + " )");
			   System.out.println(getTimestamp() + " [ OP" + Client.updateCtr + " : C" + Clock.get() +  "] Currency value is set to ( " + Client.currency_x + ","+ Client.currency_y + ") by ( " + dataQ.get(dataQCtrHead-1).delx + "," + dataQ.get(dataQCtrHead-1).dely + " )");
			   fwriter.flush();
		   }
		   }
		   break;
		   		   
	   case 3:
		   System.out.println("fin msg detected");
		   Client.allCompletionFlag++;
		   int senderid = Integer.parseInt(splits[1]);
		   
	       Client.fwriter.println(getTimestamp() + " P"+ senderid + " finished.");
	       System.out.println(getTimestamp() + " P"+ senderid + " finished.");
	       fwriter.flush();
		   //Finish Message
	       if ((dataQ.isEmpty()) && Client.completionFlag != true && queue_in.isEmpty() && queue_out.isEmpty() && RandomQ.isEmpty() && (Client.allCompletionFlag <= 2)) { 

	       Client.completionFlag = true;
	       synchronized(queue_in) {
	       Client.queue_in.add(new String("3." + Client.PID + ".FIN"));
		   Client.queue_inCtr++;
		   System.out.println("queue_inCtr" + queue_inCtr);
	       }
		  
	       Client.fwriter.println(getTimestamp() + " P"+ Client.PID + " finished.");
	       System.out.println(getTimestamp() + " P"+ Client.PID + " finished.");
	       fwriter.flush();
	       }
	       if(Client.allCompletionFlag == 2 && Client.completionFlag == true)
	    	   Client.allCompletionFlag = 3;
	       
	       if (Client.allCompletionFlag == 3) {
	    	   
	       Client.fwriter.println(getTimestamp() + " All finished. P"+ Client.PID + " terminating.");
	       System.out.println(getTimestamp() + " All finished. P"+ Client.PID + " terminating.");
	       fwriter.flush();
	       closeEverything();
	       }
     
	     return;
	     	     
	     default:
				   //no action
		    	break;
	   }
	   
   }}
   
   private void closeEverything() {
	Client.fwriter.close(); 
	try {
		endThread.stop();
		chkAdd.stop();
		treadQ.stop();
		tsend.stop();
		
		if(Client.PID == 1) {
			t4.stop();
			t3.stop();
		}
		if(Client.PID == 2) {
			t6.stop();
			t5.stop();
		}
		
		if(Client.PID!=0) {
			echoSocket.close();	
		}
		
		if(Client.PID!=2) {
		   serverSocket.close();
		}
	} catch (IOException e) {
		e.printStackTrace();
		System.out.println("Unable to close");
	} 
}
  
void ChkUpdateAdd2Q()
{
	while(true) {
	
	if (Client.RandomQCtr!=0 && Client.RandomQCtrHead != Client.RandomQCtr && !(Client.RandomQ.isEmpty())) 
	{
		// working
		//System.out.println("In add2Q + random ctr = "+ Client.RandomQCtrHead+ " " +Client.RandomQCtr);
		while(Client.RandomQCtrHead != Client.RandomQCtr) {
			
			if (Client.RandomQ.get(Client.RandomQCtrHead).x != 0) {
				if (Client.RandomQ.get(Client.RandomQCtrHead).y != 0) {
					synchronized(Client.queue_in) {
						//System.out.println("Lock in");
					Client.queue_in.add(new String( "1"+"."+Client.PID+"."+Integer.toString(Client.RandomQ.get(RandomQCtrHead).x)+"."+Integer.toString(Client.RandomQ.get(RandomQCtrHead).y)+"."+Integer.toString(RandomQCtr)));
					Client.queue_inCtr++;
					if(Client.queue_inCtr == 1)
						endThread.start();
				// working
				// System.out.println(new String( "1"+"."+Client.PID+"."+Integer.toString(Client.RandomQ.get(RandomQCtrHead).x)+"."+Integer.toString(Client.RandomQ.get(RandomQCtrHead).y)+"."+Integer.toString(RandomQCtr)));
					Client.RandomQCtrHead++;
					//System.out.println("Lock out");
					}
			
					}
				}
			}
		}
	/*// Display - working
	if (RandomQCtr == 30) {
		for (int j = Client.queue_inCtrHead; j < Client.queue_inCtr; j++)
			System.out.println(j+" Q in contents " + Client.queue_in.get(j));
	} */
		}	
	}

synchronized static void increaseConnCtr()
   {
	//System.out.println("Lock in");
	   Client.ConnectionEstablished++;
	//   System.out.println("Lock out");
   }  
      
String getTimestamp()
{
	String x = new SimpleDateFormat("MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
	String z = "[ "+x+" ]";
	return z;
}


}
