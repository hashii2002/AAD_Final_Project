package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.entity.Vehicle;
import lk.ijse.aad_final_project.repository.VehicleRepository;
import lk.ijse.aad_final_project.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;
    private final VehicleRepository vehicleRepository;

    @Override
    public String chat(String message) {

        List<Vehicle> availableVehicles = vehicleRepository.findAvailableVehicles();
        StringBuilder vehicleInformation = new StringBuilder();

        if (availableVehicles.isEmpty()) {
            vehicleInformation.append("There are currently no available vehicles in the database.");

        } else {
            for (Vehicle vehicle : availableVehicles) {
                vehicleInformation
                        .append("Vehicle Number: ")
                        .append(vehicle.getVehicleNo())

                        .append(", Brand: ")
                        .append(vehicle.getModel().getBrand().getBrandName())

                        .append(", Model: ")
                        .append(vehicle.getModel().getModelName())

                        .append(", Category: ")
                        .append(vehicle.getCategory().getCategory())

                        .append(", Color: ")
                        .append(vehicle.getColor())

                        .append(", Year: ")
                        .append(vehicle.getYear())

                        .append(", Fuel Type: ")
                        .append(vehicle.getModel().getFuelType())

                        .append(", Seating Capacity: ")
                        .append(vehicle.getModel().getSeatingCapacity())

                        .append(", Transmission: ")
                        .append(vehicle.getModel().getTransmissionType())

                        .append("\n");
            }
        }

        return chatClient
                .prompt()
                .system("""
                        You are an AI assistant for a Vehicle Rental and Fleet Management System.

                        Your job is to help customers with vehicle rental related information.

                        You can answer questions about:
                        - Vehicles
                        - Vehicle brands
                        - Vehicle models
                        - Vehicle categories
                        - Vehicle availability
                        - Vehicle specifications
                        - General vehicle rental guidance

                        IMPORTANT RULES:

                        1. Use the database information provided below when answering questions.
                        2. Do not invent vehicle information.
                        3. Do not claim that a vehicle is available unless it appears in the database information.
                        4. Do not make up vehicle numbers, brands, models or specifications.
                        5. If the requested information is not available in the database information, clearly tell the customer.
                        6. Give clear, friendly and simple answers.
                        7. Answer the customer's question directly.
                        8. Do not mention technical database details unless necessary.

                        CURRENT AVAILABLE VEHICLES:

                        """ + vehicleInformation)
                .user(message)
                .call()
                .content();
    }
}