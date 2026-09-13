package lk.ijse.aad_final_project.repository;

import lk.ijse.aad_final_project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Find customer by User ID
    @Query("SELECT c FROM Customer c WHERE c.user.userId = :userId")
    Optional<Customer> findCustomerByUserId(@Param("userId") Long userId);

    // Find customer by username
    @Query("SELECT c FROM Customer c WHERE c.user.username = :username")
    Optional<Customer> findCustomerByUsername(@Param("username") String username);

    // Check whether a customer profile already exists for a user
    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.user.userId = :userId")
    boolean existsByUserId(@Param("userId") Long userId);

    // Check duplicate NIC
    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE LOWER(c.nic) = LOWER(:nic)")
    boolean existsByNic(@Param("nic") String nic);

    // Check duplicate NIC excluding current customer
    @Query(" SELECT COUNT(c) > 0 FROM Customer c WHERE LOWER(c.nic) = LOWER(:nic)AND c.customerId <> :customerId")
    boolean existsByNicAndCustomerIdNot(@Param("nic") String nic, @Param("customerId") Long customerId);

    // Check duplicate driving license number
    @Query(" SELECT COUNT(c) > 0 FROM Customer c WHERE LOWER(c.drivingLicenseNumber) = LOWER(:drivingLicenseNumber)")
    boolean existsByDrivingLicenseNumber(@Param("drivingLicenseNumber") String drivingLicenseNumber);

    // Check duplicate driving license excluding current customer
    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE LOWER(c.drivingLicenseNumber) = LOWER(:drivingLicenseNumber)AND c.customerId <> :customerId")
    boolean existsByDrivingLicenseNumberAndCustomerIdNot(@Param("drivingLicenseNumber") String drivingLicenseNumber, @Param("customerId") Long customerId
    );
}