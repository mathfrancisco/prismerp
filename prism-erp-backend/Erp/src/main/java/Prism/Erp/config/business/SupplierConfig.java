package Prism.Erp.config.business;

@Configuration
@EnableScheduling
public class SupplierConfig {

    @Bean
    public ScheduledExecutorService supplierScheduler() {
        return Executors.newScheduledThreadPool(2);
    }

    @Bean
    public RestTemplate supplierRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }
}