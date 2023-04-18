package hexlet.code.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

public class JWTFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer";

    private final JWTUtil jwtUtil;

    private final RequestMatcher publicUrls;

    public JWTFilter(final RequestMatcher publicUrls, final JWTUtil jwtUtilsValue) {
        this.publicUrls = publicUrls;
        this.jwtUtil = jwtUtilsValue;
    }

    // исключаем из фильтра публичные пути
    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        return publicUrls.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestHeader = request.getHeader(AUTHORIZATION);

        // если не сделать проверку на 0, попытка удаления пользователей до авторизации выдает ошибку 500
        if (requestHeader != null) {
//            String jwt = requestHeader.substring(7);
            String jwt = requestHeader.replaceFirst("^" + BEARER, "").trim();
            Map<String, Object> claims;
            try {
                claims = jwtUtil.verify(jwt);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        claims.get(SPRING_SECURITY_FORM_USERNAME_KEY).toString(),
                        null,
                        SecurityConfig.DEFAULT_AUTHORITIES);

                SecurityContextHolder.getContext().setAuthentication(authToken);
                chain.doFilter(request, response);

            } catch (ExpiredJwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
//                SecurityContextHolder.getContext().setAuthentication(null);
                SecurityContextHolder.clearContext();
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access denied"); //401
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        }
    }
}
