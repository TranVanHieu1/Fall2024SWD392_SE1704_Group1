package com.group1.Care_Koi_System.entity;

import com.group1.Care_Koi_System.entity.Enum.AccountProviderEnum;
import com.group1.Care_Koi_System.entity.Enum.AccountRole;
import com.group1.Care_Koi_System.entity.Enum.AccountStatus;
import com.group1.Care_Koi_System.entity.Enum.GenderEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String userName;

    @Column(nullable = false)
    private String passWord;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountProviderEnum provider;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Transient
    private List<Order> orders;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Transient
    private List<Ponds> ponds;

    @Transient
    private String refreshToken;

    @Transient
    private String tokens;

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(this.role.name()));
        return authorities;
    }

    @Transient
    @Override
    public String getPassword() {
        return this.passWord;
    }

    @Transient
    @Override
    public String getUsername() {
        return this.userName;
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }


}
