package stranger_chat_service.service.impl;

import org.springframework.stereotype.Service;
import stranger_chat_service.payload.request.LoginRequestDTO;
import stranger_chat_service.payload.response.LoginResponseDTO;
import stranger_chat_service.service.UserService;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        String id = UUID.randomUUID().toString();
        return new LoginResponseDTO(id, requestDTO.username());
    }
}
