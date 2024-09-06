import com.xing.filter.Filter;
import com.xing.filter.FilterComponent;
import com.xing.spi.SpiLoader;
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
        PriorityQueue<Filter> consumerFilter = FilterComponent.getConsumerFilter();
        PriorityQueue<Filter> providerFilter = FilterComponent.getProviderFilter();
        System.out.println("消费者过滤器");
        for(Filter filter:consumerFilter){
            System.out.println(filter + " order:" + filter.getOrder());
        }
        System.out.println("生产者过滤器");
        for(Filter filter:providerFilter){
            System.out.println(filter + " order:" + filter.getOrder());
        }
    }

}
