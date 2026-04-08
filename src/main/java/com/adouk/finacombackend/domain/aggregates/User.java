package com.adouk.finacombackend.domain.aggregates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.LazyInitializationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    @Column(nullable = false, unique = true)
    private String username;
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    private Date createdAt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean locked;

    @JsonIncludeProperties({"label"})
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Role role;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    @JsonIncludeProperties({"firstname", "lastname"})
    private Agent agent;


    private Date lastAuthDate;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean hasToChangePassword = true;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled2FA = false;

    private String totpSecretKey;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !locked;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
