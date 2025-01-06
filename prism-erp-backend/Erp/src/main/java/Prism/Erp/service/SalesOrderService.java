package Prism.Erp.service;


import Prism.Erp.dto.SalesOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import Prism.Erp.model.OrderStatus;

public interface SalesOrderService {
    SalesOrderDTO createOrder(SalesOrderDTO orderDTO);
    SalesOrderDTO getOrderById(Long id);
    SalesOrderDTO updateStatus(Long id, OrderStatus status);
    Page<SalesOrderDTO> getCustomerOrders(Long customerId, Pageable pageable);
}
