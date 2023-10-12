package stranger_chat_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${app.ws.path.destinationPrefixes}")
    private String destinationPrefixes;

    @Value("${app.ws.path.prefixes}")
    private String prefixes;

    @Value("${app.ws.path.endpoint}")
    private String endpointPath;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(destinationPrefixes);
        config.setApplicationDestinationPrefixes(prefixes);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(endpointPath).setAllowedOrigins("*");
    }


}
