package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.RentalDTO;

import java.util.List;

public interface RentalService {

    void saveRental(RentalDTO rentalDTO);

    List<RentalDTO> getAllRentals();

    RentalDTO selectRental(Long rentalId);

    void updateRental(RentalDTO rentalDTO);

    void deleteRental(Long rentalId);

    List<RentalDTO> getMyRentals(String username);
}
