package com.valuemart.shop.domain.service.concretes;


import com.valuemart.shop.persistence.entity.*;
import com.valuemart.shop.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.CompletableFuture;



import static com.valuemart.shop.domain.util.FunctionUtil.emptyIfNullStream;
import static java.util.concurrent.CompletableFuture.runAsync;


@Slf4j
@Service
@RequiredArgsConstructor
public class StartUpService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;


    private static final String DEFAULT_ADMIN_EMAIL = "valuemart@gmail.com";

    public CompletableFuture<Void> loadDefaultData() {
        return runAsync(() -> {
            Optional<Role> role = roleRepository.findByName("ADMIN");
            Role savedRole;
            if (role.isEmpty()) {
                savedRole = roleRepository.save(new Role("ADMIN"));
            } else {
                savedRole = role.get();
            }

            Optional<User> user = userRepository.findByEmailAndDeletedFalse(DEFAULT_ADMIN_EMAIL);
            if (user.isEmpty()) {
                log.info("creating default admin user");
                User userprofile = User.builder()
                        .firstName("Timmy")
                        .lastName("Admin")
                        .emailVerified(true)
                        .deleted(false)
                        .enabled(true)
                        .email(DEFAULT_ADMIN_EMAIL)
//                        .password("$2a$10$cSJfJg1oMODysqTzFeuCKOaTDCqGAWNkuqlUaVH8deHi3sxY.cNZa")
                        .password("$2a$12$V/V59RJ7HPAyMFbOrNrbJOz2yBUt7aLlLigKwQsSzMAyAMPuBrqgO")
                        .role(savedRole)
                        .build();

                userRepository.save(userprofile);


            }
        })
                .thenCompose(__ -> setUpAllAuthoritiesForDefaultUser());
    }

    public CompletableFuture<Void> setUpAllAuthoritiesForDefaultUser() {
        return runAsync(() -> {
            Optional<User> user = userRepository.findByEmailAndDeletedFalse(DEFAULT_ADMIN_EMAIL);
            if (user.isPresent()) {
                var authorities = authorityRepository.findAll();
                var userEntity = user.get();
                userEntity.setAuthorities(authorities);

                userRepository.save(userEntity);
            }
        });
    }


    @SneakyThrows
    private void sleep() {
        Thread.sleep(2000);
    }


}
