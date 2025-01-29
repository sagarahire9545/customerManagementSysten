package com.custManagent.mapper;

import com.custManagent.dto.OrderDto;
import com.custManagent.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
//@Service
public interface OrderMapper {

//	@Mapping(target = "orderDate", source = "orderDate", qualifiedByName = "convertDateFormat")
    OrderDto mapEntityToDto(Order order);

//    @Named("convertDateFormat")
//    default String convertDateFormat(LocalDateTime orderDate) {
//        if (orderDate != null) {
//            return orderDate.toString(); // Customize formatting here
//        }
//        return "";
//    }

}
