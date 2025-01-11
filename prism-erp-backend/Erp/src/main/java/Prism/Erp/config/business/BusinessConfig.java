package Prism.Erp.config.business;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.prism.erp.repository.business")
public class BusinessConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setPropertyCondition(context -> context.getSource() != null);
        return modelMapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
