package stranger_chat_service.payload.response;

public record NotificationDTO(boolean success, String partner, String message) {
    public NotificationDTO(String message) {
        this(false, null, message);
    }

    public NotificationDTO(String partner, String message) {
        this(true, partner, message);
    }
}
