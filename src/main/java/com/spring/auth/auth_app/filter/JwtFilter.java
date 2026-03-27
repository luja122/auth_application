package com.spring.auth.auth_app.filter;

import com.spring.auth.auth_app.Exception.ResourceNotFoundException;
import com.spring.auth.auth_app.config.SecurityConfig;
import com.spring.auth.auth_app.helper.UserHelper;
import com.spring.auth.auth_app.model.Users;
import com.spring.auth.auth_app.repo.UserRepo;
import com.spring.auth.auth_app.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService service;
    private final UserRepo repo;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().startsWith("/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        //  Check header properly
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);

            // Check token type
            if (!service.isAcessToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            //  Parse token
            Jws<Claims> jws = service.parse(token);

            String userId = jws.getBody().getSubject();
            UUID uuid = UserHelper.praseUuid(userId);
            // Fetch user
            Users user = repo.findById(uuid)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // . Check enabled
            if (!user.isEnabled()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Set authentication
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            //  Proper error handling
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }

        //  Continue request
        filterChain.doFilter(request, response);
    }


}