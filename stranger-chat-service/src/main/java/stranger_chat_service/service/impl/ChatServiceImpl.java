package stranger_chat_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import stranger_chat_service.cache_manager.ScheduledManager;
import stranger_chat_service.cache_manager.UserSessionsManager;
import stranger_chat_service.payload.request.StrangeMessageDTO;
import stranger_chat_service.payload.response.NotificationDTO;
import stranger_chat_service.pojo.User;
import stranger_chat_service.cache_manager.UserPairsManager;
import stranger_chat_service.cache_manager.WaitingUsersManager;
import stranger_chat_service.service.ChatService;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final SimpMessagingTemplate messagingTemplate;

    private final WaitingUsersManager waitingUsersManager;

    private final UserPairsManager userPairsManager;

    private final UserSessionsManager userSessionsManager;

    private final ScheduledManager scheduledManager;

    @Value("${app.ws.topic.chat}")
    private String chatTopic;

    @Value("${app.ws.topic.notification}")
    private String notificationTopic;


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        User user = userSessionsManager.getUser(sessionId);
        if (user != null) {
            waitingUsersManager.remove(user);
            userPairsManager.removePair(user);
            scheduledManager.cancelTask(user);
            userSessionsManager.remove(sessionId);
        }
    }


    @Override
    public void startChatting(User user, SimpMessageHeaderAccessor headerAccessor) {
        userSessionsManager.put(user, headerAccessor);
        User pairedUser = waitingUsersManager.findPartner();
        if (pairedUser != null) {
            userPairsManager.putPair(user, pairedUser);
            scheduledManager.cancelTask(pairedUser);
            scheduledManager.cancelTask(user);
            sendConnectedNotificationToUser(user, pairedUser);
            sendConnectedNotificationToUser(pairedUser, user);
        } else {
            setUpWaiting(user);
        }
    }


    @Override
    public void sendMessage(StrangeMessageDTO messageDTO) {
        User sender = new User(messageDTO.senderId(), messageDTO.senderName());
        User partner = userPairsManager.getPartner(sender);
        if (partner != null) {
            sendMessageToUser(partner, messageDTO.content());
        } else {
            sendNotificationToUser(sender, new NotificationDTO("The partner user has left the sendMessage!"));
        }
    }


    private void sendMessageToUser(User recipient, String message) {
        String userTopic = chatTopic + recipient.id();
        messagingTemplate.convertAndSend(userTopic, message);
    }


    private void sendConnectedNotificationToUser(User user, User pairedUser) {
        sendNotificationToUser(user, new NotificationDTO(
                        pairedUser.username(),
                        String.format("You are connected to %s!", pairedUser.username())
                )
        );
    }


    private void sendNotificationToUser(User user, NotificationDTO notificationDTO) {
        String userNotificationTopic = notificationTopic + user.id();
        messagingTemplate.convertAndSend(userNotificationTopic, notificationDTO);
    }


    private void setUpWaiting(User user) {
        waitingUsersManager.addWaitingUsers(user);
        Runnable runnable = () -> {
            if (waitingUsersManager.isWaiting(user)) {
                waitingUsersManager.remove(user);
                sendNotificationToUser(user, new NotificationDTO("Couldn't find someone to talk with you!"));
            }
        };
        scheduledManager.scheduleNewTask(user, runnable);
    }

}
