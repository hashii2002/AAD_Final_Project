package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleBrandDTO;
import lk.ijse.aad_final_project.entity.VehicleBrand;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.VehicleBrandRepository;
import lk.ijse.aad_final_project.service.VehicleBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleBrandServiceImpl implements VehicleBrandService {

    private final VehicleBrandRepository vehicleBrandRepository;

    @Override
    @Transactional
    public void saveVehicleBrand(VehicleBrandDTO vehicleBrandDTO) {
        if (vehicleBrandDTO == null) {
            throw new ValidationException("Vehicle brand data is required");
        }
        if (vehicleBrandDTO.getBrandName() == null || vehicleBrandDTO.getBrandName().isBlank()) {
            throw new ValidationException("Brand name is required");
        }
        if (vehicleBrandDTO.getCountry() == null) {
            throw new ValidationException("Country is required");
        }

        String brandName = vehicleBrandDTO.getBrandName().trim();

        if (vehicleBrandRepository.existsByBrandName(brandName)) {
            throw new DuplicateException("Vehicle brand name already exists");
        }

        VehicleBrand vehicleBrand = new VehicleBrand();
        vehicleBrand.setBrandName(brandName);
        vehicleBrand.setCountry(vehicleBrandDTO.getCountry());

        vehicleBrandRepository.save(vehicleBrand);
    }

    @Override
    public List<VehicleBrandDTO> getAllVehicleBrands() {
        List<VehicleBrand> vehicleBrands = vehicleBrandRepository.findAll();
        List<VehicleBrandDTO> vehicleBrandDTOList = new ArrayList<>();

        for (VehicleBrand vehicleBrand : vehicleBrands) {
            VehicleBrandDTO vehicleBrandDTO = new VehicleBrandDTO();

            vehicleBrandDTO.setBrandId(vehicleBrand.getBrandId());
            vehicleBrandDTO.setBrandName(vehicleBrand.getBrandName());
            vehicleBrandDTO.setCountry(vehicleBrand.getCountry());

            vehicleBrandDTOList.add(vehicleBrandDTO);
        }

        return vehicleBrandDTOList;
    }

    @Override
    public VehicleBrandDTO selectVehicleBrand(Long brandId) {
        if (brandId == null) {
            throw new ValidationException("Brand ID is required");
        }

        Optional<VehicleBrand> optionalVehicleBrand = vehicleBrandRepository.findById(brandId);
        if (optionalVehicleBrand.isEmpty()) {
            throw new NotFoundException("Vehicle brand not found");
        }

        VehicleBrand vehicleBrand = optionalVehicleBrand.get();

        VehicleBrandDTO vehicleBrandDTO = new VehicleBrandDTO();
        vehicleBrandDTO.setBrandId(vehicleBrand.getBrandId());
        vehicleBrandDTO.setBrandName(vehicleBrand.getBrandName());
        vehicleBrandDTO.setCountry(vehicleBrand.getCountry());

        return vehicleBrandDTO;
    }

    @Override
    @Transactional
    public void updateVehicleBrand(VehicleBrandDTO vehicleBrandDTO) {
        if (vehicleBrandDTO == null) {
            throw new ValidationException("Vehicle brand data is required");
        }
        if (vehicleBrandDTO.getBrandId() == null) {
            throw new ValidationException("Brand ID is required");
        }
        if (vehicleBrandDTO.getBrandName() == null || vehicleBrandDTO.getBrandName().isBlank()) {
            throw new ValidationException("Brand name is required");
        }
        if (vehicleBrandDTO.getCountry() == null) {
            throw new ValidationException("Country is required");
        }

        Optional<VehicleBrand> optionalVehicleBrand = vehicleBrandRepository.findById(vehicleBrandDTO.getBrandId());
        if (optionalVehicleBrand.isEmpty()) {
            throw new NotFoundException("Vehicle brand not found");
        }

        String brandName = vehicleBrandDTO.getBrandName().trim();

        if (vehicleBrandRepository.existsByBrandNameAndBrandIdNot(brandName, vehicleBrandDTO.getBrandId())) {
            throw new DuplicateException("Vehicle brand name already exists");
        }

        VehicleBrand vehicleBrand = optionalVehicleBrand.get();
        vehicleBrand.setBrandName(brandName);
        vehicleBrand.setCountry(vehicleBrandDTO.getCountry());

        vehicleBrandRepository.save(vehicleBrand);
    }

    @Override
    @Transactional
    public void deleteVehicleBrand(Long brandId) {
        if (brandId == null) {
            throw new ValidationException("Brand ID is required");
        }

        Optional<VehicleBrand> optionalVehicleBrand = vehicleBrandRepository.findById(brandId);
        if (optionalVehicleBrand.isEmpty()) {
            throw new NotFoundException("Vehicle brand not found");
        }

        vehicleBrandRepository.deleteById(brandId);
    }
}