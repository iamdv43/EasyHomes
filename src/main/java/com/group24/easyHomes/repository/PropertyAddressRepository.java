package com.group24.easyHomes.repository;

import com.group24.easyHomes.model.Property;
import com.group24.easyHomes.model.PropertyAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PropertyAddressRepository extends JpaRepository<PropertyAddress,Integer>, JpaSpecificationExecutor<PropertyAddress> {

}