package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
