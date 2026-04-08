package com.adouk.finacombackend.Application.configs;

import com.adouk.finacombackend.Application.services.Interfaces.UserService;
import com.adouk.finacombackend.domain.aggregates.Permission;
import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.infrastructure.repositories.RolePermissionRepository;
import com.adouk.finacombackend.infrastructure.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final RolePermissionRepository rolePermissionRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final List<String> openPrefixes = List.of(
                "/swagger-resources",
                "/swagger-ui",
                "/v3/api-docs/swagger-config",
                "/api-docs",
                "/api/v1/auth/login",
                "/api/v1/auth/generate-qr-code",
                "/public",
                "/api/v1/extra",
                "/v3/api-docs",
                "/api/v1/public/auth/admin/login",
                "/api/v1/public"
        );

        String path = request.getRequestURI();
        String ctx = request.getContextPath();
        if (ctx != null && !ctx.isEmpty() && path.startsWith(ctx)) {
            path = path.substring(ctx.length());
        }

        if (openPrefixes.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }



        String authHeader = request.getHeader( "Authorization");

        if (authHeader == null){
            authHeader = request.getParameter("auth");
        }

        if(authHeader != null){

            final String jwt;
            final String username;

            jwt          = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            try {
                username = jwtService.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                errorResponse(response, "Le jeton est expiré !");
                return;
            } catch (MalformedJwtException e) {
                errorResponse(response, "Le token est invalide !");
                return;
            }
            User user = userRepository.findByUsername(username).get();

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if(jwtService.isTokenValid(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, this.getGrantedAuthorities(this.getPrivileges(user)));

                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }else {
            errorResponse(response, "Veuillez fournir le jeton d'authentification !");
        }

    }

    private void errorResponse(@NonNull HttpServletResponse response, String message) throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        Map<String,Object> error = new HashMap<>();
        error.put("message", message);
        error.put("errorCode", 401);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),error);
    }


    public List<String> getPrivileges(User user) {
        Set<Permission> permissions = new HashSet<>();

        if (user.getRole() != null) {
            List<Permission> rolePermissions = rolePermissionRepository.findPermissionByRole(user.getRole().getId());
            if (rolePermissions != null) {
                permissions.addAll(rolePermissions);
            }
        }

        return permissions.stream()
                .map(Permission::getCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        return privileges.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}

