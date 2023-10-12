package stranger_chat_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import stranger_chat_service.payload.request.LoginRequestDTO;
import stranger_chat_service.payload.response.CommonResponseDTO;
import stranger_chat_service.payload.response.LoginResponseDTO;
import stranger_chat_service.service.UserService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @PostMapping("/api/login")
    public ResponseEntity<CommonResponseDTO> login (@RequestBody LoginRequestDTO requestDTO) {
        LoginResponseDTO data = userService.login(requestDTO);
        CommonResponseDTO body = new CommonResponseDTO(true, "Login successfully!", data);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
