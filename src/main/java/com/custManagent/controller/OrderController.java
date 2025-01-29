package com.custManagent.controller;

import com.custManagent.auth.JwtTokenUtil;
import com.custManagent.dto.OrderDto;
import com.custManagent.entity.Customer;
import com.custManagent.entity.Order;
import com.custManagent.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	// note : roleId = 1 for admin,and roleId = 2 for Customer

	@PostMapping("/placeOrder")
	public ResponseEntity<?> placeOrder(HttpServletRequest request, @RequestParam(required = true) Long customerId,
			 @RequestParam(required = true) Double orderAmount) {
		try {
			String token = request.getHeader("Authorization");
			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("2".equals(roleId)) {
					Order order = orderService.placeOrder(customerId , orderAmount);
					return new ResponseEntity<>(order, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			log.error("Error in place order api : ", e);
			return new ResponseEntity<>("Error in place order api : " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/updateOrderStatusById")
	public ResponseEntity<?> updateOrderStatusById(HttpServletRequest request, @RequestParam(required = true) Long id,
			@RequestParam(required = true) String status) {
		try {

			String token = request.getHeader("Authorization");
			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("1".equals(roleId)) {
					Order order = orderService.updateOrderStatusById(id, status);
					return new ResponseEntity<>(order, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			log.error("Error in update status order api : ", e);
			return new ResponseEntity<>("Error in update status order api : " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getOrderById")
	public ResponseEntity<?> getOrderById(HttpServletRequest request, @RequestParam(required = true) Long id) {
		try {
			String token = request.getHeader("Authorization");
			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("1".equals(roleId)) {
					OrderDto order = orderService.getOrderById(id);
					return new ResponseEntity<>(order, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			log.error("Error in getOrderById api : ", e);
			return new ResponseEntity<>("Error in getOrderById api : " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getOrdersByCustomerId")
	public ResponseEntity<?> getOrdersByCustomerId(HttpServletRequest request,
			@RequestParam(required = true) Long customerId) {
		try {
			String token = request.getHeader("Authorization");
			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("1".equals(roleId) || "2".equals(roleId)) {
//            OrderDto order =  orderService.getOrdersByCustomerId(customerId);
					return new ResponseEntity<>(orderService.getOrdersByCustomerId(customerId), HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.error("Error in getOrdersByCustomerId api : ", e);
			return new ResponseEntity<>("Error in getOrdersByCustomerId api : " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getAllOrders")
	public ResponseEntity<?> getAllOrders(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");

			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("1".equals(roleId)) {
//        	List<OrderDto> orders  =orderService.getAllOrders();

					return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			log.error("Error in getAllOrders api : ", e);
			return new ResponseEntity<>("Error in getAllOrders api : " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getOrdersWithinDateRange")
	public ResponseEntity<?> getOrdersWithinDateRange(HttpServletRequest request,
			@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate) {
		try {
			String token = request.getHeader("Authorization");

			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("1".equals(roleId)) {
					return new ResponseEntity<>(orderService.getOrdersWithinDateRange(startDate, endDate),
							HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.error("Error in getOrdersWithinDateRange api : ", e);
			return new ResponseEntity<>("Error in getOrdersWithinDateRange api : " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getTotalAmountSpentByCustomer")
	public ResponseEntity<?> getTotalAmountSpentByCustomer(HttpServletRequest request,
			@RequestParam(required = true) Long customerId) {
		try {
			String token = request.getHeader("Authorization");

			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("1".equals(roleId)) {
//            Double order =  orderService.getTotalAmountSpentByCustomer(customerId);

					return new ResponseEntity<>(orderService.getTotalAmountSpentByCustomer(customerId), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.error("Error in getTotalAmountSpentByCustomer api : ", e);
			return new ResponseEntity<>("Error in getTotalAmountSpentByCustomer api : " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
