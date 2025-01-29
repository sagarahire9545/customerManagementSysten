package com.custManagent.mapper;

import com.custManagent.dto.CustomerDto;
import com.custManagent.entity.Customer;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface CustomerMapper {

	 @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "convertDateFormat")
	    CustomerDto mapEntityToDto(Customer customer);

	    @Named("convertDateFormat")
	    default String convertDateFormat(LocalDateTime createdDate) {
	        if (createdDate != null) {
	            return createdDate.toString();
	        }
	        return "";
	    }
}
