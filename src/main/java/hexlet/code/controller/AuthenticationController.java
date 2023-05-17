package hexlet.code.controller;

import hexlet.code.dto.LoginDto;
import hexlet.code.utils.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import static hexlet.code.config.SecurityConfig.LOGIN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

@RestController
@RequestMapping("${base-url}" + LOGIN)
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtil jwtTokenUtil;

    @Operation(summary = "Authentication")
    @PostMapping
    @ResponseStatus(OK)
    public String createAuthenticationToken(@RequestBody LoginDto dto) {
        Authentication authResult;
        try {
            final var authRequest = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
            authResult = authenticationManager.authenticate(authRequest); // пытаемся авторизоваться
        } catch (AuthenticationException e) { // если нет, ошибка 401
            // добавил 401 in BaseExceptionHandler, иначе BadCredentialsException выдает код 500
            throw new BadCredentialsException("Wrong username or password", e);
//            throw new UsernameNotFoundException("Wrong email or password", e);
        }

        final UserDetails user = (UserDetails) authResult.getPrincipal();
        return jwtTokenUtil.generateToken(Map.of(SPRING_SECURITY_FORM_USERNAME_KEY, user.getUsername()));
//        return jwtTokenUtil.generateToken(user);
    }
}
