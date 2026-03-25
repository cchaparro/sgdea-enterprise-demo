package demo.domain.ports.in;

import demo.application.dto.LoginCommand;
import demo.application.dto.LoginResult;

public interface LoginUseCase {

    LoginResult login(LoginCommand command);
}
