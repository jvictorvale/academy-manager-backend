package br.com.victorvale.academymanagerbackend.controllers;

import br.com.victorvale.academymanagerbackend.dto.security.AccountCredentialsDTO;
import br.com.victorvale.academymanagerbackend.dto.security.TokenDTO;
import br.com.victorvale.academymanagerbackend.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for Managing User Authentication")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/createUser")
    @Operation(summary = "Creates a new user", description = "Registers a new user with encrypted password in the database")
    public ResponseEntity<AccountCredentialsDTO> create(@RequestBody AccountCredentialsDTO data) {
        var createdUser = authService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping(value = "/signin")
    @Operation(summary = "Authenticates a user", description = "Authenticates a user and returns a JWT token")
    public ResponseEntity<TokenDTO> signin(@RequestBody AccountCredentialsDTO data) {
        if (data == null || data.getLogin() == null || data.getLogin().isBlank()
                || data.getPassword() == null || data.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var token = authService.signIn(data);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return token;
    }

    @PutMapping(value = "/refresh/{login}")
    @Operation(summary = "Refreshes token", description = "Refreshes an expired JWT token using the refresh token")
    public ResponseEntity<TokenDTO> refreshToken(@PathVariable("login") String login,
                                                 @RequestHeader("Authorization") String refreshToken) {

        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring("Bearer ".length());
        }

        if (refreshToken == null || refreshToken.isBlank() || login == null || login.isBlank()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var token = authService.refreshToken(login, refreshToken);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return token;
    }
}
