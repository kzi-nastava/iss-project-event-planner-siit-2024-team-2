package com.example.eventplanner.repositories.communication;

import com.example.eventplanner.model.communication.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Modifying
    @Query("UPDATE ChatMessage m SET m.active = false WHERE m.id = :id")
    void deleteById(@Param("id") long id);

    @Modifying
    @Query("UPDATE ChatMessage m " +
            "SET m.seen = true " +
            "WHERE m.id IN :ids " +
            "AND m.toUser.id = :myId")
    void seen(@Param("ids") Long[] ids,  @Param("myId") Long myId);
}
