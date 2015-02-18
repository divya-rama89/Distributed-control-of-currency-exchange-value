import java.net.*;
import java.io.*;

public class KKMultiServerThread extends Thread {
    private Socket socket = null;
    private int ID = 0;
    private PrintWriter out;
    private BufferedReader in;
    Client p;
   
    public KKMultiServerThread(Socket socket, int KKID) {
        super("KKMultiServerThread");
        this.socket = socket;
        this.ID = KKID;
        
    }
    
    public void run() {
   	    	
    	try {
    			out = new PrintWriter(socket.getOutputStream(), true);      	
    			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	    
    			//System.out.println(" Successfully connected with " + socket);
                //out.println("Hey Client I got you ");
                String temp;
                //System.out.println("Me server. Received : " + socket.getPort());
                while(true) {

                	if(in.ready())
                	{
                			
                	temp = in.readLine();
                	
                	if(temp!=null) {
                	      // push data into queue
                		 synchronized(Client.queue_out) {    
 	    	           		Client.queue_out.add(temp);
 	    	           		System.out.println("Received: "+Client.queue_out.get(Client.queue_outCtr));
 	    	           		Client.queue_outCtr++;
 	    	        	      	      	     } 	
                  	                	
                	if(temp.equals("Begin")) { 
                		Client.increaseConnCtr(); 
                		out.println("Begin");
                	}
                	              	
                	}
                	}
                	 if ((Client.flag1 == 1) && (this.ID == 1)) {
                	  //System.out.println("Server sending"+Client.MessageListen);
       	        	  out.println(Client.Message1);
       	        	  Client.flag1 = 0;
       	          }
                	 if ((Client.flag2 == 1) && (this.ID == 2)) {
                   	  //System.out.println("Server sending"+Client.MessageListen);
          	        	  out.println(Client.Message2);
          	        	  Client.flag2 = 0;
          	          }
                }
                
              //  socket.close();	
                              
                
            } catch (IOException e) {
                e.printStackTrace();
            }
    	
    	
          }
    
}
