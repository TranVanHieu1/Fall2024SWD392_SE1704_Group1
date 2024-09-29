package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.WaterParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterParameterRepository extends JpaRepository<WaterParameter, Integer> {
}
