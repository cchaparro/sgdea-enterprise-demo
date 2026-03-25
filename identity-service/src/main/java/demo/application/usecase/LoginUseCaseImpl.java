package demo.application.usecase;

import demo.application.dto.LoginCommand;
import demo.application.dto.LoginResult;
import demo.domain.exception.InvalidCredentialsException;
import demo.domain.ports.in.LoginUseCase;
import demo.domain.ports.out.UserCredentialsPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {

    private final UserCredentialsPort userCredentialsPort;

    @Override
    public LoginResult login(LoginCommand command) {
        log.info("Authenticating user '{}'", command.username());

        if (!userCredentialsPort.isValid(command.username(), command.password())) {
            log.warn("Authentication failed for user '{}'", command.username());
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = UUID.randomUUID().toString();
        log.info("Authentication succeeded for user '{}'", command.username());
        return new LoginResult(command.username(), token);
    }
}
