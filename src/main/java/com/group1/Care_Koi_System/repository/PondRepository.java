package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Ponds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PondRepository extends JpaRepository<Ponds, Integer> {

    Optional<Ponds> findByIdAndAccountId(int pondId, int accountId);

    Ponds findByNamePondAndAccountId(String namePond, int id);
}
