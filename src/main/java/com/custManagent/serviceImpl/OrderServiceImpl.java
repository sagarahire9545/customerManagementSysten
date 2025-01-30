package com.custManagent.serviceImpl;

import com.custManagent.dto.OrderDto;
import com.custManagent.entity.Customer;
import com.custManagent.entity.Order;
import com.custManagent.mapper.OrderMapper;
import com.custManagent.repository.CustomerRepository;
import com.custManagent.repository.OrderRepository;
import com.custManagent.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderMapper orderMapper;
    
    @Override
    public Order placeOrder(Long customerId, Double orderAmount) {
        try {
            Customer customer = customerRepository.findById(customerId).orElse(null);

            if (customer == null) {
                log.info("Customer not found with ID: {}", customerId);
                return null;
            }

            Order order = new Order();

            order.setCustomer(customer);
            order.setOrderAmount(orderAmount);
            order.setStatus(Order.Status.PENDING); //pending kr diya jav wo place
            
            order.setOrderDate(LocalDateTime.now()); //current date & time save when place order

            orderRepository.save(order);

            log.info("Successfully placed order for this customer ID: {}", customerId);
            return order;

        } catch (Exception e) {
            log.error("Error getting while placing order for Customer ID {}: {}", customerId, e.getMessage());
            return null;
        }
    }

    @Override
    public Order updateOrderStatusById(Long id, String status) {
        try {

            Order order = orderRepository.findById(id).orElse(null);

            if (order == null) {
                log.info("Order not found with ID: {}", id);
                return null;
            }
            //update when admin pass status
            if (status != null) {
                switch (status.toUpperCase()) {
                    case "PENDING":
                        order.setStatus(Order.Status.PENDING);
                        break;
                    case "COMPLETED":
                        order.setStatus(Order.Status.COMPLETED);
                        break;
                    case "CANCELLED":
                        order.setStatus(Order.Status.CANCELLED);
                        break;
                    default:
                        log.info("Invalid status value: {}", status);
                        return null;
                }
            } else {
                log.info("Status cannot be null.");
                return null;
            }

            orderRepository.save(order);

            log.info("Successfully updated status for Order ID: {}", id);
            return order;

        } catch (Exception e) {
            log.error("Error getting while updating status for Order ID {}: {}", id, e.getMessage());
            return null;
        }
    }

    @Override
    public OrderDto getOrderById(Long id) {
        try {
            Order order = orderRepository.findById(id).orElse(null);

            if (order != null) {
                OrderDto orderDto = orderMapper.mapEntityToDto(order);
                log.info("Successfully retrieved order for Order ID: {}", id);
                return orderDto;

            }else {
                log.info("Order not found with ID: {}", id);
                return null;
            }

        } catch (Exception e) {
            log.error("Error getting while retrieving order with ID {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        try {

            List<Order> orders = orderRepository.findByCustomerId(customerId);

            if (!orders.isEmpty() && orders != null) {

                List<OrderDto> orderDtos = new ArrayList<>();

                for (Order order : orders) {
                    OrderDto orderDto = orderMapper.mapEntityToDto(order);
                    orderDtos.add(orderDto);
                }
                log.info("Successfully retrieved orders for Customer ID: {}", customerId);
                return orderDtos;
            }else {
                log.info("No orders found for Customer ID: {}", customerId);
                return null;
            }

        } catch (Exception e) {
            log.error("Error getting while retrieving orders for Customer ID {}: {}", customerId, e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<OrderDto> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();

            if (!orders.isEmpty() && orders != null) {

                List<OrderDto> orderDtos = new ArrayList<>();

                for (Order order : orders) {
                    OrderDto orderDto = orderMapper.mapEntityToDto(order);
                    //add customer Id in orders details
                    orderDto.setCustomerId(order.getCustomer().getId());
                    orderDtos.add(orderDto);
                }

                log.info("Successfully retrieved all orders.");
                return orderDtos;

            }else {
                log.info("Order not found..!!");
                return null;
            }

        } catch (Exception e) {
            log.error("Error occurred while retrieving all orders: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<OrderDto> getOrdersWithinDateRange(String startDate, String endDate) {

        try {

//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startLocalDate = LocalDate.parse(startDate, dateFormatter);
            LocalDate endLocalDate = LocalDate.parse(endDate, dateFormatter);

            LocalDateTime startDateTime = startLocalDate.atStartOfDay(); 
            LocalDateTime endDateTime = endLocalDate.atTime(23, 59, 59); // 2025-01-30T23:59:59


            List<Order> orders = orderRepository.findByOrderDateBetween(startDateTime, endDateTime);

            if (!orders.isEmpty() && orders != null) {

                List<OrderDto> orderDtos = new ArrayList<>();

                for (Order order : orders) {
                    OrderDto orderDto = orderMapper.mapEntityToDto(order);
                    //add customer Id in orders details
                    orderDto.setCustomerId(order.getCustomer().getId());
                    orderDtos.add(orderDto);
                }
                log.info("Successfully retrieved orders within the date range: {} to {}", startDate, endDate);
                return orderDtos;

            }else {
                log.info("No orders found within the date range: {} to {}", startDate, endDate);
                return null;
            }
        } catch (Exception e) {
            log.error("Error getting while retrieving orders within the date range: {} to {}: {}", startDate, endDate, e.getMessage());
            return null;
        }
    }

    @Override
    public Double getTotalAmountSpentByCustomer(Long customerId) {
        try {

            List<Order> orders = orderRepository.findByCustomerId(customerId);

            if (!orders.isEmpty() && orders != null) {

                double totalAmountSpent = 0.0;

                //suppose customer have many orders.
                for (Order order : orders) {
                    totalAmountSpent += order.getOrderAmount();
                }

                log.info("Total amount spent by Customer ID {}: {}", customerId, totalAmountSpent);
                return totalAmountSpent;
            } else {
                log.info("No orders found for Customer ID: {}", customerId);
                return 0.0;
            }

        } catch (Exception e) {
            log.error("Error occurred while calculating total amount spent by Customer ID {}: {}", customerId, e.getMessage());
            return 0.0;
        }
    }
}
