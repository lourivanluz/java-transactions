package br.com.lourivanrluz.tutorial.users;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import br.com.lourivanrluz.tutorial.wallet.Wallet;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "users")

public class Users implements UserDetails {
    private @Id @GeneratedValue(strategy = GenerationType.UUID) UUID id;
    private String fullName;
    @NotNull(message = "Field password is requered")
    private String password;
    private Long cpf;
    private String email;
    private Integer type;
    private UserRoleType role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    Wallet wallet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (this.role == UserRoleType.ADM) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIM"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'getAuthorities'");
    }

    @Override
    public String getPassword() {
        return password;
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'getPassword'");
    }

    @Override
    public String getUsername() {
        return email;
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'getUsername'");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'isAccountNonExpired'");
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'isAccountNonLocked'");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'isCredentialsNonExpired'");
    }

    @Override
    public boolean isEnabled() {
        return true;
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
    }
}
