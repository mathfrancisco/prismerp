package Prism.Erp.repository.business.supplier;

import Prism.Erp.dto.business.supplier.SupplierPortalAccount;
import Prism.Erp.exception.SupplierException;
import Prism.Erp.security.PasswordEncoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SupplierPortalService {

    private final SupplierPortalAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtTokenProvider tokenProvider;

    public SupplierPortalAccountDTO createAccount(CreateAccountDTO createAccountDTO) {
        validateNewAccount(createAccountDTO);

        SupplierPortalAccount account = new SupplierPortalAccount();
        account.setSupplier(supplierRepository.findById(createAccountDTO.getSupplierId())
                .orElseThrow(() -> new SupplierException.SupplierNotFoundException(createAccountDTO.getSupplierId())));
        account.setUsername(createAccountDTO.getUsername());
        account.setPassword(passwordEncoder.encode(createAccountDTO.getPassword()));
        account.setEmail(createAccountDTO.getEmail());
        account.setActive(true);
        account.setEmailVerified(false);
        account.setRoles(Set.of("ROLE_SUPPLIER"));

        SupplierPortalAccount savedAccount = accountRepository.save(account);

        sendVerificationEmail(savedAccount);

        return modelMapper.map(savedAccount, SupplierPortalAccountDTO.class);
    }

    public JwtAuthenticationResponse authenticate(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SupplierPortalAccount account = accountRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        account.setLastLogin(LocalDateTime.now());
        accountRepository.save(account);

        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(jwt);
    }

    public List<PurchaseOrderDTO> getSupplierOrders(String username) {
        SupplierPortalAccount account = findAccountByUsername(username);
        return purchaseOrderRepository.findBySupplierIdOrderByDateDesc(account.getSupplier().getId())
                .stream()
                .map(order -> modelMapper.map(order, PurchaseOrderDTO.class))
                .collect(Collectors.toList());
    }

    public void createNotification(String username, CreateNotificationDTO notificationDTO) {
        SupplierPortalAccount account = findAccountByUsername(username);

        PortalNotification notification = new PortalNotification();
        notification.setAccount(account);
        notification.setTitle(notificationDTO.getTitle());
        notification.setMessage(notificationDTO.getMessage());
        notification.setType(notificationDTO.getType());
        notification.setReferenceType(notificationDTO.getReferenceType());
        notification.setReferenceId(notificationDTO.getReferenceId());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        notificationRepository.save(notification);

        if (notificationDTO.isSendEmail()) {
            emailService.sendNotificationEmail(account.getEmail(), notification);
        }
    }
}
