package stranger_chat_service.cache_manager;

import org.springframework.stereotype.Component;
import stranger_chat_service.pojo.User;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class WaitingUsersManager {
    private static final Queue<User> WAITING_USERS = new LinkedList<>();


    public User findPartner() {
        return WAITING_USERS.poll();
    }


    public void addWaitingUsers(User user) {
        WAITING_USERS.add(user);
        System.out.println("After add waiting users: " + WAITING_USERS);
    }


    public boolean isWaiting(User user) {
        return WAITING_USERS.contains(user);
    }


    public void remove(User user) {
        WAITING_USERS.remove(user);
        System.out.println("After remove in waiting user: " + WAITING_USERS);
    }
}
