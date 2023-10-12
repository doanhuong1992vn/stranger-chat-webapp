package stranger_chat_service.cache_manager;

import org.springframework.stereotype.Component;
import stranger_chat_service.pojo.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledManager {
    private static final Map<User, ScheduledFuture<?>> SCHEDULED_TASKS = new ConcurrentHashMap<>();

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);


    public void cancelTask(User user) {
        ScheduledFuture<?> scheduledFuture = SCHEDULED_TASKS.remove(user);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }


    public void scheduleNewTask(User user, Runnable runnable) {
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(runnable, 5, TimeUnit.MINUTES);
        SCHEDULED_TASKS.put(user, scheduledFuture);
    }

}
