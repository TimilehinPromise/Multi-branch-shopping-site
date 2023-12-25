package com.valuemart.shop.persistence.entity;


import com.valuemart.shop.domain.models.AgentDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



import static com.valuemart.shop.domain.util.FunctionUtil.emptyIfNullStream;
import static javax.persistence.FetchType.EAGER;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BasePersistentEntity implements UserDetails, ToModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String royaltyCode;
    private int branchId;
    @Column(nullable = false)
    private String email;
    private String phone;
    private boolean emailVerified;

    @Column(columnDefinition = " int default '0'")
    private int retries =0;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "user_authority",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "authority_id", referencedColumnName = "id"
            )
    )
    private Collection<Authority> authorities;

    private boolean enabled = false;
    private boolean deleted = false;
    @Column(name = "activated")
    private boolean activated ;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = EAGER)
    private Wallet wallet;





    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Role role = getRole();
        if (role != null) {
            String roleName = "ROLE_" + role.getName();
            authorities.add(new SimpleGrantedAuthority(roleName));
        }

        emptyIfNullStream(this.authorities)
                .map(a -> a.getAuthority().toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        return authorities;
    }

    public Collection<Authority> getUserAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }

    public UserBuilder toBuilder() {
        return builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .phone(phone)
                .emailVerified(emailVerified)
                .enabled(enabled)
                .deleted(deleted)
                .role(role);
    }


    @Override
    public Object toModel() {
        return AgentDto.valueOf(this);
    }
}
