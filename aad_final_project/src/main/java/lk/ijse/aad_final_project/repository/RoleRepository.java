package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Role;
import lk.ijse.aad_final_project.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    //Check role name already exists
    @Query(" SELECT COUNT(r) > 0 FROM Role r WHERE r.roleName = :roleName")
    boolean existsByRoleName(@Param("roleName") RoleName roleName);

    // Check duplicate role
    @Query(" SELECT COUNT(r) > 0 FROM Role r WHERE r.roleName = :roleName AND r.roleId <> :roleId")
    boolean existsByRoleNameAndRoleIdNot(@Param("roleName") RoleName roleName, @Param("roleId") Long roleId);

    // Check whether users are assigned to this role
    @Query("  SELECT COUNT(u) > 0 FROM User u WHERE u.role.roleId = :roleId")
    boolean existsUsersByRoleId(@Param("roleId") Long roleId);
}
