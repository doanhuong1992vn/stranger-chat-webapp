package stranger_chat_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import stranger_chat_service.payload.request.StrangeMessageDTO;
import stranger_chat_service.pojo.User;
import stranger_chat_service.service.ChatService;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    @MessageMapping("/start")
    public void startChatting(User user, SimpMessageHeaderAccessor headerAccessor) {
        chatService.startChatting(user, headerAccessor);
    }

    @MessageMapping("/chat")
    public void sendMessage(StrangeMessageDTO messageDTO) {
        chatService.sendMessage(messageDTO);
    }


}
