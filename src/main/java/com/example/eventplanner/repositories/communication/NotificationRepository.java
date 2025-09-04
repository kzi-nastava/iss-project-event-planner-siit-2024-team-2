package com.example.eventplanner.repositories.communication;

import com.example.eventplanner.model.communication.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Modifying
    @Query("UPDATE Notification n SET n.active = false WHERE n.id = :id")
    void deleteById(@Param("id") long id);

    @Modifying
    @Query("UPDATE Notification n " +
            "SET n.dismissed = true " +
            "WHERE n.id IN :ids AND n.user.id = :userId")
    void dismiss(@Param("ids") Long[] ids, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Notification n " +
            "SET n.seen = true " +
            "WHERE n.id IN :ids AND n.user.id = :userId")
    void seen(@Param("ids") Long[] ids, @Param("userId") Long userId);

    @Query("SELECT CASE " +
            "   WHEN count(*)>0 THEN FALSE " +
            "   ELSE TRUE " +
            "END " +
            "FROM Notification n " +
            "WHERE n.id IN :ids AND n.user.id != :userId")
    Boolean hasAccess(@Param("ids") Long[] ids, @Param("userId") Long userId);

    Page<Notification> findAllByUserIdAndDismissedFalse(Long userId, Pageable pageable);
    Page<Notification> findAllByUserIdAndDismissedFalseAndSentAtBefore(Long userId, Instant sentAt, Pageable pageable);

}
