package com.example.eventplanner.repositories.user;

import com.example.eventplanner.model.serviceproduct.ServiceProductReview;
import com.example.eventplanner.model.user.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    @Modifying
    @Query("UPDATE UserReport e SET e.active = false WHERE e.id = :id")
    void deleteById(@Param("id") long id);
}
