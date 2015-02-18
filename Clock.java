import java.util.TimerTask;

public class Clock extends TimerTask {
	
	public volatile int rate;
	private static double cnt = 0;
	
	public void set(int clkrate, int pid)
	{
		rate = clkrate;
		cnt = 0.1*pid;
	}
		
	public static double get()
	{
		return cnt;
	}
	
	public void run()
	{
		cnt += rate;
	}
	
	public static void inc()
	{
		cnt++;
	}
	
	public synchronized void algo(double clk) //lamports algorithm
	{
		int c1 = (int)clk;
		int c2 = (int)cnt;
		if(c1>c2)
		{
			cnt = cnt + (c1-c2)+1;
		}
	}

}
