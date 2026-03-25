package demo.infrastructure.adapters.out.inmemory;

import demo.domain.ports.out.UserCredentialsPort;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class InMemoryUserCredentialsAdapter implements UserCredentialsPort {

    private static final Map<String, String> USERS = Map.of(
            "admin", "admin",
            "user1", "1234");

    @Override
    public boolean isValid(String username, String password) {
        return USERS.containsKey(username) && USERS.get(username).equals(password);
    }
}
