package com.example.eventplanner.repositories.event;

import com.example.eventplanner.model.event.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @Modifying
    @Query("UPDATE Budget b SET b.active = false WHERE b.id = :id")
    void deleteById(@Param("id") long id);
}
