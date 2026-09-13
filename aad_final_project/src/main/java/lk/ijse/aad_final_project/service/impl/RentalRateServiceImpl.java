package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.RentalRateDTO;
import lk.ijse.aad_final_project.entity.RentalRate;
import lk.ijse.aad_final_project.entity.VehicleCategory;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.repository.RentalRateRepository;
import lk.ijse.aad_final_project.repository.VehicleCategoryRepository;
import lk.ijse.aad_final_project.service.RentalRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentalRateServiceImpl implements RentalRateService {

    private final RentalRateRepository rentalRateRepository;
    private final VehicleCategoryRepository vehicleCategoryRepository;

    @Override
    @Transactional
    public void saveRentalRate(RentalRateDTO rentalRateDTO) {
        Optional<VehicleCategory> optionalCategory = vehicleCategoryRepository.findById(rentalRateDTO.getCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException("Vehicle category not found");
        }

        VehicleCategory vehicleCategory = optionalCategory.get();

        RentalRate rentalRate = new RentalRate();

        rentalRate.setDailyRate(rentalRateDTO.getDailyRate());
        rentalRate.setMonthlyRate(rentalRateDTO.getMonthlyRate());
        rentalRate.setFreeKmPerDay(rentalRateDTO.getFreeKmPerDay());
        rentalRate.setExtraKmPrice(rentalRateDTO.getExtraKmPrice());
        rentalRate.setCategory(vehicleCategory);

        rentalRateRepository.save(rentalRate);
    }

    @Override
    public List<RentalRateDTO> getAllRentalRates() {
        List<RentalRate> rentalRates = rentalRateRepository.findAll();

        List<RentalRateDTO> rentalRateDTOList = new ArrayList<>();

        for (RentalRate rentalRate : rentalRates) {

            RentalRateDTO rentalRateDTO = new RentalRateDTO();

            rentalRateDTO.setRateId(rentalRate.getRateId());
            rentalRateDTO.setDailyRate(rentalRate.getDailyRate());
            rentalRateDTO.setMonthlyRate(rentalRate.getMonthlyRate());
            rentalRateDTO.setFreeKmPerDay(rentalRate.getFreeKmPerDay());
            rentalRateDTO.setExtraKmPrice(rentalRate.getExtraKmPrice());
            rentalRateDTO.setCategoryId(rentalRate.getCategory().getCategoryId());

            rentalRateDTOList.add(rentalRateDTO);
        }

        return rentalRateDTOList;
    }

    @Override
    public RentalRateDTO selectRentalRate(Long rateId) {
        Optional<RentalRate> optionalRentalRate = rentalRateRepository.findById(rateId);

        if (optionalRentalRate.isEmpty()) {
            throw new NotFoundException("Rental rate not found");
        }

        RentalRate rentalRate = optionalRentalRate.get();

        RentalRateDTO rentalRateDTO = new RentalRateDTO();

        rentalRateDTO.setRateId(rentalRate.getRateId());
        rentalRateDTO.setDailyRate(rentalRate.getDailyRate());
        rentalRateDTO.setMonthlyRate(rentalRate.getMonthlyRate());
        rentalRateDTO.setFreeKmPerDay(rentalRate.getFreeKmPerDay());
        rentalRateDTO.setExtraKmPrice(rentalRate.getExtraKmPrice());
        rentalRateDTO.setCategoryId(rentalRate.getCategory().getCategoryId());

        return rentalRateDTO;
    }

    @Override
    @Transactional
    public void updateRentalRate(RentalRateDTO rentalRateDTO) {
        Optional<RentalRate> optionalRentalRate = rentalRateRepository.findById(rentalRateDTO.getRateId());

        if (optionalRentalRate.isEmpty()) {
            throw new NotFoundException("Rental rate not found");
        }

        Optional<VehicleCategory> optionalCategory = vehicleCategoryRepository.findById(rentalRateDTO.getCategoryId());

        if (optionalCategory.isEmpty()) {
            throw new NotFoundException("Vehicle category not found");
        }

        RentalRate rentalRate = optionalRentalRate.get();

        VehicleCategory vehicleCategory = optionalCategory.get();
        rentalRate.setDailyRate(rentalRateDTO.getDailyRate());
        rentalRate.setMonthlyRate(rentalRateDTO.getMonthlyRate());
        rentalRate.setFreeKmPerDay(rentalRateDTO.getFreeKmPerDay());
        rentalRate.setExtraKmPrice(rentalRateDTO.getExtraKmPrice());
        rentalRate.setCategory(vehicleCategory);

        rentalRateRepository.save(rentalRate);

    }

    @Override
    @Transactional
    public void deleteRentalRate(Long rateId) {

        if (!rentalRateRepository.existsById(rateId)) {
            throw new NotFoundException("Rental rate not found");
        }

        rentalRateRepository.deleteById(rateId);

    }
}
