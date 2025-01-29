package com.custManagent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDto {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private List<OrderDto> orders;
    private String createdAt;
    
    private Integer roleId; // 1 = ADMIN, 2 = CUSTOMER

	
}
