import java.util.Comparator;

public class myCompare implements Comparator<queue> {
    @Override
    public int compare(queue o1, queue o2) {
    	Integer time1 = (int)o1.clkcount;
    	Integer time2 = (int)o2.clkcount;
        return time1.compareTo(time2);
    }
}
