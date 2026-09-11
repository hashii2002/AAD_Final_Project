package lk.ijse.aad_final_project.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.aad_final_project.constant.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            if (token.isBlank()) {
                handleJwtException(response, 401, "Token is missing");
                return;
            }

            String username = jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            handleJwtException(response, 401, "Token expired");

        } catch (SignatureException ex) {
            handleJwtException(response, 401, "Invalid token signature");

        } catch (MalformedJwtException ex) {
            handleJwtException(response, 401, "Invalid token format");

        } catch (Exception ex) {
            handleJwtException(response, 401, "Authentication failed");
        }
    }

    private void handleJwtException(HttpServletResponse response, int code, String message) throws IOException {

        SecurityContextHolder.clearContext();
        response.setStatus(code);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setCharacterEncoding("UTF-8");

        CommonResponse errorResponse = new CommonResponse(1, message);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
