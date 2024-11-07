package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.Pond_Feeding;
import com.group1.Care_Koi_System.entity.Pond_KoiFish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Pond_FeedingRepository extends JpaRepository<Pond_Feeding, Integer> {
    List<Pond_Feeding> findByPondsId(int id);
}