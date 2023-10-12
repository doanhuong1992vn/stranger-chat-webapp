package stranger_chat_service.cache_manager;

import org.springframework.stereotype.Component;
import stranger_chat_service.pojo.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserPairsManager {
    private static final Map<User, User> USER_PAIRS = new ConcurrentHashMap<>();

    public void putPair(User user, User pairedUser) {
        USER_PAIRS.put(user, pairedUser);
        USER_PAIRS.put(pairedUser, user);
        System.out.println("After put pair: " + USER_PAIRS);
    }

    public User getPartner(User sender) {
        return USER_PAIRS.get(sender);
    }

    public void removePair(User user) {
        User partner = USER_PAIRS.get(user);
        if (partner != null) {
            USER_PAIRS.remove(partner);
        }
        USER_PAIRS.remove(user);
        System.out.println("After remove pair: " + USER_PAIRS);
    }
}
