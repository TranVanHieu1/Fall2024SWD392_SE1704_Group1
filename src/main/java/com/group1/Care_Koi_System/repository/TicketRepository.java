package com.group1.Care_Koi_System.repository;

import com.group1.Care_Koi_System.entity.Ponds;
import com.group1.Care_Koi_System.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByIsResolvedFalseAndIsDeletedFalse();
}
