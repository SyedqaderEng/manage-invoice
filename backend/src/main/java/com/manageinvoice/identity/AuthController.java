package com.manageinvoice.identity;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manageinvoice.common.security.AuthenticatedUser;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthService.AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(new AuthService.RegisterRequest(request.email(), request.password(), request.firstName(), request.lastName()));
    }

    @PostMapping("/login")
    public AuthService.AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(new AuthService.LoginRequest(request.email(), request.password()));
    }

    @PostMapping("/logout")
    public Map<String, Boolean> logout() {
        return Map.of("loggedOut", true);
    }

    @GetMapping("/me")
    public AuthService.UserResponse me(@AuthenticationPrincipal AuthenticatedUser principal) {
        return new AuthService.UserResponse(authService.findRequired(principal.id()));
    }

    public record RegisterRequest(@Email @NotBlank String email, @Size(min = 8, max = 72) String password,
                                  @NotBlank String firstName, @NotBlank String lastName) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
}
