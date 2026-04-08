package com.adouk.finacombackend.Application.configs;

import com.adouk.finacombackend.domain.aggregates.Permission;
import com.adouk.finacombackend.domain.aggregates.User;
import com.adouk.finacombackend.infrastructure.repositories.RolePermissionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    // TODO: Change this secret key and load it from configs
    private static final String SECRET_KEY = "7336763979244226452948404D635166546A576E5A7234753777217A25432A46";
    private final RolePermissionRepository rolePermissionRepository;

    public JwtService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isExpireToken(token);
    }

    private boolean isExpireToken(String token) {
        return extractExpirate(token).before(new Date());
    }

    private Date extractExpirate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(User user){
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extractClaims, User user){
        // Add can-confirm-responses to authorities
        List<String> roles = getPrivileges(user);

        extractClaims.put("permissions", roles.toArray());
        extractClaims.put("username", user.getUsername());
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(user.getUsername())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 60 * 24)))
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractsAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractsAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String[] extractPermissions(String token){
        try {
            return extractClaim(token, cls -> {
                List<String> items = cls.get("roles",List.class);
                return items.toArray(new String[0]);
            });
        }catch (Exception e){
            return new String[]{};
        }
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

}

