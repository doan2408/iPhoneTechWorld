package org.example.websitetechworld.Services.LoginServices;

import org.example.websitetechworld.Repository.JointAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final JointAccount account;

    public CustomUserDetails(JointAccount account) {
        this.account = account;
    }

    public Integer getId() {
        return account.getId(); // đảm bảo JointAccount có getId()
    }

    public String getFullName() {
        return account.getFullName();
    }

    public String gettrangThai() {
        return account.getTrangThai();
    }

    public JointAccount getAccount() {
        return account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(account.getRole()));
    }

    @Override
    public String getPassword() {
        return account.mat_khau();
    }

    @Override
    public String getUsername() {
        return account.tai_khoan();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}


