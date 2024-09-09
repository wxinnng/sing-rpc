import com.xing.filter.Filter;
import com.xing.filter.FilterChain;
import org.junit.Test;

import java.util.PriorityQueue;

public class FilterTest {


    @Test
    public void testQueue(){
        PriorityQueue<Integer> integers = new PriorityQueue<>();
        integers.add(3);
        integers.add(1);
        integers.add(2);
        integers.add(4);
        for(Integer i:integers){
            System.out.println(i);
        }
    }

    @Test
    public void testFilter(){

    }

}
