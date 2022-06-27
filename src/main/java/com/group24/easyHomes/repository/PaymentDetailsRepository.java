package com.group24.easyHomes.repository;

import com.group24.easyHomes.model.PaymentDetails;
import com.group24.easyHomes.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Integer> {

    @Query("SELECT t FROM PaymentDetails t WHERE t.user_id = ?1")
    PaymentDetails findByUserId(String user_id);
}
