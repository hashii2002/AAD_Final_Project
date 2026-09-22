package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    //Check username already exists
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    boolean existsByUsername(@Param("username") String username);

    //Check username duplication
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.username) = LOWER(:username)AND u.userId <> :userId")
    boolean existsByUsernameAndUserIdNot(@Param("username") String username, @Param("userId") Long userId);

    //Check email already exists
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existsByEmail(@Param("email") String email);

    // Check email duplication
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(:email)AND u.userId <> :userId")
    boolean existsByEmailAndUserIdNot(@Param("email") String email, @Param("userId") Long userId);

}
