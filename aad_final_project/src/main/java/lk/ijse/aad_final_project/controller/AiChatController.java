package lk.ijse.aad_final_project.controller;

import jakarta.validation.Valid;
import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.AiChatRequestDTO;
import lk.ijse.aad_final_project.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/ai")
@CrossOrigin
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;

    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> chat(@Valid @RequestBody AiChatRequestDTO requestDTO) {
        String response = aiChatService.chat(requestDTO.getMessage());
        return ResponseEntity.ok(new CommonResponse(0, response, "AI Response Generated Successfully"));
    }
}
