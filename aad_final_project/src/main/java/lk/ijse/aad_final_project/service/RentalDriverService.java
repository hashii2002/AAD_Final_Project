package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.RentalDriverDTO;

import java.util.List;

public interface RentalDriverService {

    void saveRentalDriver(RentalDriverDTO rentalDriverDTO);

    List<RentalDriverDTO> getAllRentalDrivers();

    RentalDriverDTO selectRentalDriver(Long rentalDriverId);

    void updateRentalDriver(RentalDriverDTO rentalDriverDTO);

    void deleteRentalDriver(Long rentalDriverId);

    List<RentalDriverDTO> getMyRentals(String username);
}
