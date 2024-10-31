package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Integer> {
    Optional<FoodItem> findByItemName(String itemName);
}
