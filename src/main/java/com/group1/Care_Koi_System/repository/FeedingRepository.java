package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Feeding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedingRepository extends JpaRepository<Feeding, Integer> {
}