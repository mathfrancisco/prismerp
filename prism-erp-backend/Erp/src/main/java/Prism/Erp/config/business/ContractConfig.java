@Configuration
public class ContractConfig {
    
    @Bean
    public ContractNumberGenerator contractNumberGenerator() {
        return new SequentialContractNumberGenerator();
    }
    
    @Bean
    public ContractValidator contractValidator() {
        return new DefaultContractValidator();
    }
    
    @Bean
    public ContractNotificationStrategy contractNotificationStrategy(
            NotificationService notificationService,
            EmailService emailService) {
        return new DefaultContractNotificationStrategy(notificationService, emailService);
    }
}
