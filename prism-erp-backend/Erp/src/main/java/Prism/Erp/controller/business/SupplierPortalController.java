package Prism.Erp.controller.business;

import Prism.Erp.dto.business.purchase.PurchaseOrderDTO;
import Prism.Erp.repository.business.supplier.SupplierPortalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/supplier-portal")
@RequiredArgsConstructor
@Validated
public class SupplierPortalController {

    private final SupplierPortalService portalService;

    @PostMapping("/register")
    public ResponseEntity<SupplierPortalAccountDTO> createAccount(
            @Valid @RequestBody CreateAccountDTO createAccountDTO) {
        return ResponseEntity.ok(portalService.createAccount(createAccountDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(
            @Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(portalService.authenticate(loginDTO));
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<List<PurchaseOrderDTO>> getOrders() {
        return ResponseEntity.ok(portalService.getSupplierOrders(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ));
    }

    @GetMapping("/notifications")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<List<PortalNotificationDTO>> getNotifications(
            @RequestParam(defaultValue = "false") boolean unreadOnly) {
        return ResponseEntity.ok(portalService.getNotifications(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                unreadOnly
        ));
    }

    @PostMapping("/notifications/{id}/read")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        portalService.markNotificationAsRead(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                id
        );
        return ResponseEntity.ok().build();
    }
}

