package stranger_chat_service.cache_manager;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import stranger_chat_service.pojo.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserSessionsManager {
    private static final Map<String, User> USER_SESSION = new ConcurrentHashMap<>();

    public void put(User user, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        USER_SESSION.put(sessionId, user);
        System.out.println("After put session:" + USER_SESSION);
    }


    public User getUser(String sessionId) {
        return USER_SESSION.get(sessionId);
    }


    public void remove(String sessionId) {
        USER_SESSION.remove(sessionId);
        System.out.println("After remove session:" + USER_SESSION);

    }
}
