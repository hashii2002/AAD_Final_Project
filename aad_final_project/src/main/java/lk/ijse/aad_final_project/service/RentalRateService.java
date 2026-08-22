package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.RentalRateDTO;

import java.util.List;

public interface RentalRateService {

    void saveRentalRate(RentalRateDTO rentalRateDTO);

    List<RentalRateDTO> getAllRentalRates();

    RentalRateDTO selectRentalRate(Long rateId);

    void updateRentalRate(RentalRateDTO rentalRateDTO);

    void deleteRentalRate(Long rateId);
}
