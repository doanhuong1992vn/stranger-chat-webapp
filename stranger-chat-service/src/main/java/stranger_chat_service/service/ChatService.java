package stranger_chat_service.service;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import stranger_chat_service.payload.request.StrangeMessageDTO;
import stranger_chat_service.pojo.User;

public interface ChatService {
    void startChatting(User user, SimpMessageHeaderAccessor headerAccessor);

    void sendMessage(StrangeMessageDTO messageDTO);
}
