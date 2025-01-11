package Prism.Erp.config.business;

@Configuration
@EnableAsync
public class PurchaseConfig {

    @Bean
    public AsyncExecutor purchaseOrderExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("PurchaseOrder-");
        executor.initialize();
        return executor;
    }

    @Bean
    public CacheManager purchaseCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
            new ConcurrentMapCache("purchaseOrders"),
            new ConcurrentMapCache("purchaseOrderItems"),
            new ConcurrentMapCache("purchaseAnalytics")
        ));
        return cacheManager;
    }
}
