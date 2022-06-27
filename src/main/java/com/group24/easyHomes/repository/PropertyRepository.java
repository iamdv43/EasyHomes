package com.group24.easyHomes.repository;

import com.group24.easyHomes.model.Property;
import com.group24.easyHomes.model.PropertyListQuery;
import com.group24.easyHomes.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property,Integer>, JpaSpecificationExecutor<Property> {

    @Query("SELECT t FROM Property t WHERE t.property_id = ?1")
    Property findByProperty(int property_id);

}
