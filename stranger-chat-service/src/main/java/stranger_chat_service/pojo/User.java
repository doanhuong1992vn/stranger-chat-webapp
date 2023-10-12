package stranger_chat_service.pojo;

public record User (String id, String username){
    @Override
    public String toString() {
        return username;
    }
}
