package com.group24.easyHomes.repository;

import com.group24.easyHomes.model.AppUser;
import com.group24.easyHomes.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Services,Long>, JpaSpecificationExecutor<Services> {


    @Query("SELECT t FROM Services t WHERE t.service_id = ?1")
    Services findByServices(Long service_id);

}
