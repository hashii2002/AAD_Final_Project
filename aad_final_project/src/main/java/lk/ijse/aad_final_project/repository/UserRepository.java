package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
