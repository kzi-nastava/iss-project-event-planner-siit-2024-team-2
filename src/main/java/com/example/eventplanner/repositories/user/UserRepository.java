package com.example.eventplanner.repositories.user;

import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<BaseUser, Long> {
    Optional<BaseUser> findByIdAndPassword(long id, String password);
    Optional<BaseUser> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    Optional<BaseUser> findByEmail(String email);
    Optional<BaseUser> findFirstByUserRole(UserRole userRole);

    @Modifying
    @Query(value = "UPDATE baseuser " +
            "SET dtype = 'EventOrganizer', " +
            "   address = :address, " +
            "   phonenumber = :phonenumber, " +
            "   firstname = :firstname, " +
            "   lastname = :lastname, " +
            "   userrole = :userrole, " +
            "   password = :password " +
            "WHERE id = :id",
        nativeQuery = true)
    void upgradeToEventOrganizer(
        @Param("id") Long id,
        @Param("address") String address,
        @Param("phonenumber") String phoneNumber,
        @Param("firstname") String firstName,
        @Param("lastname") String lastName,
        @Param("userrole") int userRole,
        @Param("password") String password
    );

    @Modifying
    @Query(value = "UPDATE baseuser " +
            "SET dtype = 'ServiceProductProvider', " +
            "   address = :address, " +
            "   phonenumber = :phonenumber, " +
            "   firstname = :firstname, " +
            "   lastname = :lastname, " +
            "   userrole = :userrole, " +
            "   password = :password, " +
            "   companyname = :companyname, " +
            "   companydescription = :companydescription " +
            "WHERE id = :id",
        nativeQuery = true)
    void upgradeToServiceProductProvider(
        @Param("id") Long id,
        @Param("address") String address,
        @Param("phonenumber") String phoneNumber,
        @Param("firstname") String firstName,
        @Param("lastname") String lastName,
        @Param("userrole") int userRole,
        @Param("password") String password,
        @Param("companyname") String companyName,
        @Param("companydescription") String companyDescription
    );

    @Query(value = """
        SELECT CASE WHEN EXISTS (
            FROM BaseUser u
            JOIN u.blockedUsers b
            WHERE u.id = :id
              AND b.id = :blockedUserId
        ) THEN TRUE ELSE FALSE END
        """)
        
    boolean hasBlocked(long id, long blockedUserId);
}
