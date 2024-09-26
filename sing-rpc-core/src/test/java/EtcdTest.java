import cn.hutool.json.JSONUtil;
import io.github.wxinnng.model.ServiceStrategyInfo;
import io.github.wxinnng.registry.RegistryKeys;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.options.GetOption;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class EtcdTest {

    @Test
    public void test() throws  Exception{

        KV kvClient = Client.builder()
                .endpoints("http://localhost:2380")
                .connectTimeout(Duration.ofMillis(1000))
                .build().getKVClient();

        List<ServiceStrategyInfo> serviceMetaInfo = null;
        String searchPrefix = RegistryKeys.SRSM_ROOT_PATH;
        // 前缀查询
//        try{
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                            ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            // 解析服务信息
            serviceMetaInfo = keyValues.stream()
                    .map(keyValue -> {
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceStrategyInfo.class);
                    })
                    .collect(Collectors.toList());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        System.err.println(serviceMetaInfo.size() +"========================================");
        serviceMetaInfo.forEach(System.err::println);
    }


}
