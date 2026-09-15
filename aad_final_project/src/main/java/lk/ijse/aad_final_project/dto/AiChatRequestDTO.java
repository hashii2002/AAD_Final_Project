package lk.ijse.aad_final_project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiChatRequestDTO {
    @NotBlank(message = "Message is required")
    private String message;
}
