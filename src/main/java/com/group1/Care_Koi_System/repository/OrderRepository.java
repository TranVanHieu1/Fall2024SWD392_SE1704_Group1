package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}