package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.Order;
import com.halalhomemade.backend.models.OrderStatus;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
	Optional<Order> findOneById(Long id); 
	Optional<Order> findOneByUuid(String uuid);
	List<Order> findAllByStatus(OrderStatus status);
	List<Order> findAllByStatusIn(List<OrderStatus> statusList);
	
	Optional<Order> findOneByUuidAndDeviceIdentifier(String uuid, String deviceIdentifier);
	List<Order> findAllByStatusInAndDeviceIdentifier(List<OrderStatus> statusList, String deviceIdentifier);
}
