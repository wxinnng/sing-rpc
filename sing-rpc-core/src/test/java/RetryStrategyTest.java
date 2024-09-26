import io.github.wxinnng.fault.retry.FixedIntervalRetryStrategy;
import io.github.wxinnng.fault.retry.RetryStrategy;
import io.github.wxinnng.model.RpcResponse;
import org.junit.Test;

public class RetryStrategyTest {

    RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void doRetry(){
        try{
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("测试");
                throw new RuntimeException("模拟失败");
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
