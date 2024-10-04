package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<FoodItem, Integer> {

}
