package br.com.victorvale.academymanagerbackend.services;

import br.com.victorvale.academymanagerbackend.dto.security.AccountCredentialsDTO;
import br.com.victorvale.academymanagerbackend.dto.security.TokenDTO;
import br.com.victorvale.academymanagerbackend.exception.RequiredObjectIsNullException;
import br.com.victorvale.academymanagerbackend.model.User;
import br.com.victorvale.academymanagerbackend.repository.UserRepository;
import br.com.victorvale.academymanagerbackend.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getLogin(),
                            credentials.getPassword()
                    )
            );

            var user = userRepository.findByLogin(credentials.getLogin());
            if (user == null) {
                throw new UsernameNotFoundException("Login " + credentials.getLogin() + " not found!");
            }

            var roles = user.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .toList();

            var tokenResponse = jwtTokenProvider.createAccessToken(credentials.getLogin(), roles);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid login/password supplied!");
        }
    }

    public ResponseEntity<TokenDTO> refreshToken(String login, String refreshToken) {
        var user = userRepository.findByLogin(login);

        if (user != null) {
            var roles = user.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .toList();

            var tokenResponse = jwtTokenProvider.createAccessToken(login, roles);
            return ResponseEntity.ok(tokenResponse);
        } else {
            throw new UsernameNotFoundException("Login " + login + " not found");
        }
    }

    public AccountCredentialsDTO create(AccountCredentialsDTO userCredentials) {
        if (userCredentials == null) throw new RequiredObjectIsNullException();

        logger.info("Creating a new User!");

        if (userRepository.findByLogin(userCredentials.getLogin()) != null) {
            throw new IllegalArgumentException("Login already exists!");
        }

        var entity = User.builder()
                .login(userCredentials.getLogin())
                .password(generateHashedPassword(userCredentials.getPassword()))
                .role("ADMIN")
                .build();

        userRepository.save(entity);

        return new AccountCredentialsDTO(entity.getLogin(), "********");
    }

    private String generateHashedPassword(String password) {
        Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder.encode(password);
    }
}
