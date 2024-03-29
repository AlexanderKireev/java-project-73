package hexlet.code.config;

import hexlet.code.controller.UserController;
import hexlet.code.security.JWTFilter;
import hexlet.code.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    public static final String LOGIN = "/login";


    public static final List<GrantedAuthority> DEFAULT_AUTHORITIES = List.of(new SimpleGrantedAuthority("USER"));

    private final RequestMatcher publicUrls;
    private final RequestMatcher loginRequest;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public SecurityConfig(@Value("${base-url}") final String baseUrl,
                          final UserDetailsService userDetailsServiceValue,
                          final PasswordEncoder passwordEncoderValue, final JWTUtil jwtUtil) {
        this.loginRequest = new AntPathRequestMatcher(baseUrl + LOGIN, POST.toString());
        this.publicUrls = new OrRequestMatcher(
                loginRequest,
                new AntPathRequestMatcher(baseUrl + UserController.USER_CONTROLLER_PATH, POST.toString()),
                new AntPathRequestMatcher(baseUrl + UserController.USER_CONTROLLER_PATH, GET.toString()),
                new NegatedRequestMatcher(new AntPathRequestMatcher(baseUrl + "/**"))
        );
        this.userDetailsService = userDetailsServiceValue;
        this.passwordEncoder = passwordEncoderValue;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {

//        final var authenticationFilter = new JWTAuthenticationFilter(
//                authenticationManagerBean(),
//                loginRequest,
//                jwtHelper
//        );
//
        final var jwtFilter = new JWTFilter(
                publicUrls,
                jwtUtil
        );

        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers(publicUrls).permitAll()
                .anyRequest().authenticated()
                .and()
//                .addFilter(authenticationFilter)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();

        http.headers().frameOptions().disable();

    }
}
