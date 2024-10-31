package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Ponds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PondRepository extends JpaRepository<Ponds, Integer> {


    Ponds findByNamePond(String namePond);

    Ponds findById(int id);

    List<Ponds> findByAccount(Account account);

    @Query("SELECT p FROM Ponds p WHERE (:namePond IS NULL OR LOWER(p.namePond) LIKE LOWER(CONCAT('%', :namePond, '%'))) " +
            "AND (:id IS NULL OR p.id = :id)")
    List<Ponds> searchByNamePondAndIdPond(@Param("namePond") String namePond, @Param("id") Integer id);
}
