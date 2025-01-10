package Prism.Erp.service;


import Prism.Erp.dto.SalesOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import Prism.Erp.model.OrderStatus;
import org.springframework.transaction.annotation.Transactional;

public interface SalesOrderService {
    SalesOrderDTO createOrder(SalesOrderDTO orderDTO);

    @Transactional
    SalesOrderDTO approveOrder(Long id);

    @Transactional
    SalesOrderDTO cancelOrder(Long id);

    SalesOrderDTO getOrderById(Long id);
    SalesOrderDTO updateStatus(Long id, OrderStatus status);
    Page<SalesOrderDTO> getCustomerOrders(Long customerId, Pageable pageable);
    SalesOrderDTO getByOrderNumber(String orderNumber);
}
