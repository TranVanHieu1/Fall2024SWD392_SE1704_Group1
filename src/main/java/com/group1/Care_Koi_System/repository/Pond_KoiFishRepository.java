package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.KoiFish;
import com.group1.Care_Koi_System.entity.Pond_KoiFish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Pond_KoiFishRepository extends JpaRepository<Pond_KoiFish, Integer> {
    List<Pond_KoiFish> findKoiFishByPondsId(int id);
}
