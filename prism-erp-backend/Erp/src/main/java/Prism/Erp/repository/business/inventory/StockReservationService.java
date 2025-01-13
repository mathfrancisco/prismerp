package Prism.Erp.repository.business.inventory;

import Prism.Erp.entity.business.inventory.InventoryItem;
import Prism.Erp.exception.InsufficientStockException;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class StockReservationService {

    private final InventoryItemRepository itemRepository;
    private final ReservationRepository reservationRepository;

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void reserveStock(String sku, BigDecimal quantity, String referenceType, String referenceId) {
        InventoryItem item = itemRepository.findBySku(sku)
                .orElseThrow(() -> new ItemNotFoundException(sku));

        if (item.getQuantity().subtract(getReservedQuantity(sku)).compareTo(quantity) < 0) {
            throw new InsufficientStockException(sku);
        }

        StockReservation reservation = new StockReservation();
        reservation.setItem(item);
        reservation.setQuantity(quantity);
        reservation.setReferenceType(referenceType);
        reservation.setReferenceId(referenceId);
        reservation.setExpiryDate(LocalDateTime.now().plusHours(24));

        reservationRepository.save(reservation);
    }
}
