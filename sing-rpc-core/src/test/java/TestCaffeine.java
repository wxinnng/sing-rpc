import cn.hutool.core.collection.ListUtil;
import com.github.benmanes.caffeine.cache.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.*;

public class TestCaffeine {





//    public static void main(String[] args) {
////        Cache<String,String> cache = Caffeine.newBuilder()
////                .maximumSize(1000)
////                .build();
////        String key = "hello";
////        String str = cache.getIfPresent(key);
////        System.out.println("getIfPresent(key) ===> " + str);
////
////
////        str = cache.get(key,k -> "world");
////        System.out.println("get(k -> world) ==> " + str);
////
////        cache.put(key,"aaa");
////        System.out.println("put(key,aaa),then getIfPresent() ==> " + cache.getIfPresent(key));
////
////        cache.invalidate(key);
////        System.out.println("invalidate");
//        LoadingCache<String, String> cache = Caffeine.newBuilder()
//                .maximumSize(10_000)
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .build(TestCaffeine::create);
//
//        String key = "hello";
//        String str = cache.get(key);
//        System.out.println("cache.get(key) ==> " + str);
//        ArrayList<String> list = ListUtil.toList("a", "b", "c", "d");
//        //批量查找缓存元素，如果缓存不存在，则生成缓存元素。
//        Map<String, String> maps = cache.getAll(list);
//        System.out.println("cache.getAll(list) ==> " + maps);
//    }
//
//
//    public static String create(String key){
//        return key + " world !" ;
//    }

    static CacheLoader<String, String> loader = new CacheLoader<String,String>() {
        @Override
        public String load(String key) throws Exception {
            // 模拟数据加载过程
            Thread.sleep(5000);
            return key.toString().toUpperCase();
        }

        @Override
        public CompletableFuture<String> asyncLoad(String key, Executor executor) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return load(key);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, executor);
        }
    };

    private final static AsyncCache<String, String> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .executor(Executors.newSingleThreadExecutor())
            .buildAsync(loader);
    public void test() throws ExecutionException, InterruptedException {
        // 异步获取
        CompletableFuture<String> future = cache.get("dddd",e->"AAA");
        CompletableFuture<String> ss = cache.get("ss",e->"ss");
        CompletableFuture<String> ssss = cache.get("ssss",e->"ssss");
        CompletableFuture<String> sss = cache.get("sss",e->"sss");
        future.thenAcceptAsync(System.out::println);
    }

    public static void main(String[] args) {
        // 基于缓存内元素的个数，尝试回收最近或者未经常使用的元素
        LoadingCache<String, String> values = Caffeine.newBuilder().maximumSize(10_000).build(key -> key);
        // 也可以基于缓存元素的权重，进行驱除
        LoadingCache<String, String> graphs = Caffeine.newBuilder().maximumWeight(10_000).weigher((String key, String value) -> value.length()).build(key -> key);

        // 删除
        // 直接删除
        values.invalidate("key");
        // 批量删除
        values.invalidateAll(ListUtil.toList("a", "b", "c", "d", "e"));
        // 删除所有
        values.invalidateAll();

    }



}
