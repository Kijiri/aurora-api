package com.kijiri.aurora.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_user_names", columnList = "last_name,first_name"),
        @Index(name = "idx_user_first_name", columnList = "first_name"),
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_user_name", columnList = "user_name")
})
public class User implements UserDetails, Principal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String userName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String hashedPassword;
    
    private Integer age;

    private String bio;

    private String avatarUrl;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(insertable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.VERIFIED;
    }
}
