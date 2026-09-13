package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.VehicleBrandDTO;
import lk.ijse.aad_final_project.entity.VehicleBrand;
import lk.ijse.aad_final_project.exception.NotFoundException;
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
        VehicleBrand vehicleBrand = new VehicleBrand();

        vehicleBrand.setBrandName(vehicleBrandDTO.getBrandName());
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
        Optional<VehicleBrand> optionalVehicleBrand = vehicleBrandRepository.findById(vehicleBrandDTO.getBrandId());

        if (optionalVehicleBrand.isEmpty()) {
            throw new NotFoundException("Vehicle brand not found");
        }

        VehicleBrand vehicleBrand = optionalVehicleBrand.get();

        vehicleBrand.setBrandName(vehicleBrandDTO.getBrandName());
        vehicleBrand.setCountry(vehicleBrandDTO.getCountry());

        vehicleBrandRepository.save(vehicleBrand);

    }

    @Override
    @Transactional
    public void deleteVehicleBrand(Long brandId) {
        if (!vehicleBrandRepository.existsById(brandId)) {
            throw new NotFoundException("Vehicle brand not found");
        }

        vehicleBrandRepository.deleteById(brandId);

    }
}
