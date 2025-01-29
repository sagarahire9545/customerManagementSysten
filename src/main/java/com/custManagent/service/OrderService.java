package com.custManagent.service;

import com.custManagent.dto.OrderDto;
import com.custManagent.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    //place order
    public Order placeOrder(Long customerId, Double orderAmount);

    //update order status (order id)
    public Order updateOrderStatusById(Long id, String status);

    //get order by using id
    public OrderDto getOrderById(Long id);

    //bet order details using customer id
    public List<OrderDto> getOrdersByCustomerId(Long customerId);

    //get all orders
    public List<OrderDto> getAllOrders();

    //get order details within dates-
    public List<OrderDto> getOrdersWithinDateRange(String startDate, String endDate);

    public Double getTotalAmountSpentByCustomer(Long customerId);
}
