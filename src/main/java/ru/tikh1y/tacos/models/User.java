package ru.tikh1y.tacos.models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

@Data
public class User implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;

    private final String username;
    private final String password;

    private final String fullName;
    private final String street;
    private final String city;
    private final String state;
    private final String zip;
    private final String phone;

    public User(long id, String username, String password, String fullName, String street, String city, String state, String zip, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
    }

    public User(String username, String password, String fullName, String street, String city, String state, String zip, String phone) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
