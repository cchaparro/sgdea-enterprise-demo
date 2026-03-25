package demo.infrastructure.adapters.in.rest;

import demo.application.dto.LoginCommand;
import demo.application.dto.LoginResult;
import demo.domain.ports.in.LoginUseCase;
import demo.infrastructure.api.ApiResponse;
import demo.infrastructure.adapters.in.rest.request.LoginRequest;
import demo.infrastructure.adapters.in.rest.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        log.info("Received login request for user '{}'", request.username());

        LoginResult result = loginUseCase.login(new LoginCommand(request.username(), request.password()));
        LoginResponse response = new LoginResponse(result.user(), result.token());

        return ResponseEntity.ok(ApiResponse.success(response, "Login successful", MDC.get("traceId")));
    }
}
