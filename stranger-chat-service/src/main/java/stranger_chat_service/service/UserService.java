package stranger_chat_service.service;

import stranger_chat_service.payload.request.LoginRequestDTO;
import stranger_chat_service.payload.response.LoginResponseDTO;

public interface UserService {
    LoginResponseDTO login(LoginRequestDTO requestDTO);
}
