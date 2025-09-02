package com.example.eventplanner.repositories.communication;

import com.example.eventplanner.model.communication.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Modifying
    @Query("UPDATE Chat c SET c.active = false WHERE c.id = :id")
    void deleteById(@Param("id") long id);

    @Query("SELECT c FROM Chat c " +
            "WHERE c.user1.id = :userId OR c.user2.id = :userId")
    Page<Chat> findAllMine(Long userId, Pageable pageable);

    Optional<Chat> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

}
