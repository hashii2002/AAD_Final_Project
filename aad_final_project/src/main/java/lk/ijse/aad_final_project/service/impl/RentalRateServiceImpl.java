package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.RentalRateDTO;
import lk.ijse.aad_final_project.entity.RentalRate;
import lk.ijse.aad_final_project.entity.VehicleCategory;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
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
        validateRentalRateDTO(rentalRateDTO);

        if (rentalRateRepository.existsByCategory_CategoryId(rentalRateDTO.getCategoryId())) {
            throw new DuplicateException("A rental rate already exists for this vehicle category");
        }

        Optional<VehicleCategory> optionalCategory = vehicleCategoryRepository.findById(rentalRateDTO.getCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException("Vehicle category not found");
        }

        RentalRate rentalRate = new RentalRate();
        rentalRate.setDailyRate(rentalRateDTO.getDailyRate());
        rentalRate.setMonthlyRate(rentalRateDTO.getMonthlyRate());
        rentalRate.setFreeKmPerDay(rentalRateDTO.getFreeKmPerDay());
        rentalRate.setExtraKmPrice(rentalRateDTO.getExtraKmPrice());
        rentalRate.setCategory(optionalCategory.get());

        rentalRateRepository.save(rentalRate);
    }

    @Override
    public List<RentalRateDTO> getAllRentalRates() {
        List<RentalRate> rentalRates = rentalRateRepository.findAll();
        List<RentalRateDTO> rentalRateDTOList = new ArrayList<>();

        for (RentalRate rentalRate : rentalRates) {
            rentalRateDTOList.add(mapToRentalRateDTO(rentalRate));
        }

        return rentalRateDTOList;
    }

    @Override
    public RentalRateDTO selectRentalRate(Long rateId) {
        if (rateId == null) {
            throw new ValidationException("Rental Rate ID is required");
        }

        Optional<RentalRate> optionalRentalRate = rentalRateRepository.findById(rateId);
        if (optionalRentalRate.isEmpty()) {
            throw new NotFoundException("Rental rate not found");
        }

        return mapToRentalRateDTO(optionalRentalRate.get());
    }

    @Override
    public RentalRateDTO getRentalRateByCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new ValidationException("Category ID is required");
        }

        Optional<RentalRate> optionalRentalRate = rentalRateRepository.findByCategory_CategoryId(categoryId);
        if (optionalRentalRate.isEmpty()) {
            throw new NotFoundException("Rental rate not found for the specified category");
        }

        return mapToRentalRateDTO(optionalRentalRate.get());
    }

    @Override
    @Transactional
    public void updateRentalRate(RentalRateDTO rentalRateDTO) {
        if (rentalRateDTO == null || rentalRateDTO.getRateId() == null) {
            throw new ValidationException("Rental Rate ID is required for update");
        }

        validateRentalRateDTO(rentalRateDTO);

        Optional<RentalRate> optionalRentalRate = rentalRateRepository.findById(rentalRateDTO.getRateId());
        if (optionalRentalRate.isEmpty()) {
            throw new NotFoundException("Rental rate not found");
        }

        if (rentalRateRepository.existsByCategory_CategoryIdAndRateIdNot(rentalRateDTO.getCategoryId(), rentalRateDTO.getRateId())) {
            throw new DuplicateException("A rental rate already exists for this vehicle category");
        }

        Optional<VehicleCategory> optionalCategory = vehicleCategoryRepository.findById(rentalRateDTO.getCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException("Vehicle category not found");
        }

        RentalRate rentalRate = optionalRentalRate.get();
        rentalRate.setDailyRate(rentalRateDTO.getDailyRate());
        rentalRate.setMonthlyRate(rentalRateDTO.getMonthlyRate());
        rentalRate.setFreeKmPerDay(rentalRateDTO.getFreeKmPerDay());
        rentalRate.setExtraKmPrice(rentalRateDTO.getExtraKmPrice());
        rentalRate.setCategory(optionalCategory.get());

        rentalRateRepository.save(rentalRate);
    }

    @Override
    @Transactional
    public void deleteRentalRate(Long rateId) {
        if (rateId == null) {
            throw new ValidationException("Rental Rate ID is required");
        }

        if (!rentalRateRepository.existsById(rateId)) {
            throw new NotFoundException("Rental rate not found");
        }

        rentalRateRepository.deleteById(rateId);
    }

    private void validateRentalRateDTO(RentalRateDTO dto) {
        if (dto == null) {
            throw new ValidationException("Rental rate data is required");
        }
        if (dto.getCategoryId() == null) {
            throw new ValidationException("Category ID is required");
        }
        if (dto.getDailyRate() == null || dto.getDailyRate() <= 0) {
            throw new ValidationException("Daily rate must be greater than zero");
        }
        if (dto.getMonthlyRate() == null || dto.getMonthlyRate() <= 0) {
            throw new ValidationException("Monthly rate must be greater than zero");
        }
        if (dto.getFreeKmPerDay() == null || dto.getFreeKmPerDay() < 0) {
            throw new ValidationException("Free KM per day cannot be negative");
        }
        if (dto.getExtraKmPrice() == null || dto.getExtraKmPrice() < 0) {
            throw new ValidationException("Extra KM price cannot be negative");
        }
    }

    private RentalRateDTO mapToRentalRateDTO(RentalRate rentalRate) {
        RentalRateDTO dto = new RentalRateDTO();
        dto.setRateId(rentalRate.getRateId());
        dto.setDailyRate(rentalRate.getDailyRate());
        dto.setMonthlyRate(rentalRate.getMonthlyRate());
        dto.setFreeKmPerDay(rentalRate.getFreeKmPerDay());
        dto.setExtraKmPrice(rentalRate.getExtraKmPrice());

        if (rentalRate.getCategory() != null) {
            dto.setCategoryId(rentalRate.getCategory().getCategoryId());
        }

        return dto;
    }
}