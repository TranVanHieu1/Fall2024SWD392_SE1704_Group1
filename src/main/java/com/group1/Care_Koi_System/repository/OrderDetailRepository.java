package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByIsDeletedFalse();
}
