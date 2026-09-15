package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;

    @Override
    public String chat(String message) {

        return chatClient
                .prompt()
                .system("""
                        You are an AI assistant for a Vehicle Rental and Fleet Management System.

                        Your job is to help customers with vehicle rental related information.

                        You can answer questions about:
                        - Vehicles
                        - Vehicle categories
                        - Vehicle models
                        - Rental rates
                        - Rental information
                        - General vehicle rental guidance

                        Always give clear, friendly and simple answers.

                        Do not make up vehicle availability, prices or other database information.
                        If actual system data is not available, clearly tell the customer.
                        """)
                .user(message)
                .call()
                .content();
    }
}