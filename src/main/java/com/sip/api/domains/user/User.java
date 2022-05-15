package com.sip.api.domains.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sip.api.domains.TimeTrackable;
import com.sip.api.domains.enums.UserStatus;
import com.sip.api.domains.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_data")
public class User extends TimeTrackable implements UserDetails {
    @Column(nullable = false, unique = true)
    private int dni;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private int phone;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    UserStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_data_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @Fetch(FetchMode.JOIN)
    private Set<Role> roles = new LinkedHashSet<>();

    // Use by UserFactory to convert from UserDTO to User
    public User(int dni, String password, String email, String firstName, String lastName, LocalDate birthDate, int phone) {
        super();
        this.dni = dni;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override // is the email
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !(getStatus() == UserStatus.DEACTIVATED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getStatus() == UserStatus.ACTIVE;
    }
}
