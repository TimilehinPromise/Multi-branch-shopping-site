package com.valuemart.shop.domain.models;


import com.valuemart.shop.persistence.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserAuthentication implements Authentication {

    private static final long serialVersionUID = 4161658715015553004L;
        private final User user;
        private boolean authenticated = true;

        public UserAuthentication(User user) {
            this.user = user;
        }

        @Override
        public String getName() {
            return user.getEmail();
        }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }


    @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public User getDetails() {
            return user;
        }

        @Override
        public Object getPrincipal() {
            return user;
        }

        @Override
        public boolean isAuthenticated() {
            return authenticated;
        }

        @Override
        public void setAuthenticated(boolean authenticated) {
            this.authenticated = authenticated;
        }

    public User getUser() {
        return user;
    }
}
