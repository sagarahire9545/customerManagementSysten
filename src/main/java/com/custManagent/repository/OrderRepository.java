package com.custManagent.repository;

import com.custManagent.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    //to get user using customer id
    List<Order> findByCustomerId(Long customerId);

    //get orders during provided dates
    List<Order> findByOrderDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
