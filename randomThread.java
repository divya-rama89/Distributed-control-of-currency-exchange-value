

public class randomThread extends Thread{

	   
	public randomThread() {
		// TODO Auto-generated constructor stub
	}
		   synchronized public void run() {
		    	int delta_x;
		    	int delta_y;
		    	while(Client.RandomQCtr < Currency.nopr)
		    	   {
		    		    try {
		    			   Thread.sleep(generateRandomTime());
		    		   } catch (InterruptedException e) {
		    	System.out.println("did not start random generation thread!");
		    		 e.printStackTrace();
		    	    
		    		   }
					delta_x = generateRandomDeltas();
					delta_y = generateRandomDeltas();
					//System.out.println(" delta_x, delta_y = " + " "+delta_x+ " " + delta_y+" Client.RandomQCtr"+Client.RandomQCtr);
			synchronized(Client.RandomQ){		
					
				    Client.RandomQ.add(new randomQ(delta_x, delta_y)); 
					
					//if (Client.RandomQCtr!=0)
					//System.out.println(" delta_x, delta_y = " + " "+Client.RandomQ.get(Client.RandomQCtr).x+ " " + Client.RandomQ.get(Client.RandomQCtr).y+" Client.RandomQCtr"+Client.RandomQCtr);    
					Client.RandomQCtr++;
		    	   }
		    	   }
		    //	for(int i = 0; i<Client.RandomQCtr; i++)
		   // 		System.out.println(" delta_x, delta_y = " + " "+Client.RandomQ.get(i).x+ " " + Client.RandomQ.get(i).y+" Client.RandomQCtr"+Client.RandomQCtr);
		    	
		      }
		    	

		    static int generateRandomDeltas()
		    {
		    	int Min = -80;
		    	int Max = 80;
		    	int ans = 0;
		    	do { 
		    		ans = (Min + (int)(Math.random() * ((Max - Min) + 1)));
		    	} while(ans == 0);
		    	//System.out.println("Random number generated is "+ ans);
		    	return ans;
		    }
		    
		    static int generateRandomTime()
			{
	    	
	    	int ans = 0;
	    	do { 
	    		ans = (int)(Math.random() * (1000 + 1));
	    	} while(ans == 0);
	    	//System.out.println("Thread slept for  "+ ans+" ms.");
	    	return ans;


		    		
	}
}


