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

    Optional<Ponds> findByIdAndAccountId(int pondId, int accountId);

    Ponds findByNamePondAndAccountId(String namePond, int id);

    Ponds findById(int id);

    List<Ponds> findByAccount(Account account);

    @Query("SELECT p FROM Ponds p LEFT JOIN FETCH p.koiFishList WHERE " +
            "(:namePond IS NULL OR p.namePond LIKE %:namePond%) AND " +
            "(:minSize IS NULL OR p.size >= :minSize) AND " +
            "(:maxSize IS NULL OR p.size <= :maxSize)")
    List<Ponds> searchPond(@Param("namePond") String namePond,
                           @Param("minSize") Double minSize,
                           @Param("maxSize") Double maxSize);

}
