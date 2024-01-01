package com.valuemart.shop.domain.service.concretes;


import com.valuemart.shop.domain.ProductImageModel;
import com.valuemart.shop.domain.models.dto.ProductDTO;
import com.valuemart.shop.domain.service.abstracts.ProductsService;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.persistence.entity.*;
import com.valuemart.shop.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final ProductsService productsService;
    private final ProductRepository  productRepository;
    private final LoyaltyRepository loyaltyRepository;


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
            if (userRepository.findAll().isEmpty()){
                Role customerRole = roleRepository.findFirstByName("CUSTOMER");
                User customer = User.builder()
                        .firstName("Timi")
                        .lastName("Promise")
                        .emailVerified(true)
                        .deleted(false)
                        .enabled(true)
                        .activated(true)
                        .phone("09064183466")
                        .email("timicust@gmail.com")
                        .role(customerRole)
                        .password("")
                        .branchId(1)
                        .retries(0)
                        .authorities(Collections.emptyList())
                        .build();

              User savedCustomer = userRepository.save(customer);
              String customerRoyaltyCode = UserUtils.generateCustomerCode(savedCustomer.getFirstName().concat(" ").concat(savedCustomer.getLastName()),savedCustomer.getId(), LocalDateTime.now());
              savedCustomer.setRoyaltyCode(customerRoyaltyCode);
              userRepository.save(savedCustomer);

              log.info("Customer user successfully created");
              log.info(savedCustomer.toString());

            }
            if (productRepository.findAll().isEmpty()) {

                ProductImageModel imageModel = ProductImageModel.builder()
                        .imageUrl("https://ibb.co/Bz8vqn7")
                        .build();
                ProductDTO productDTO = ProductDTO.builder()
                        .brand("2sure")
                        .name("2SURE MED & ANTIBAC HERBAL PLUS SOAP 65G")
                        .description("bathing soap")
                        .price(BigDecimal.valueOf(950))
                        .enabled(true)
                        .subCategoryId(18L)
                        .categoryId(5L)
                        .images(List.of(imageModel))
                        .availableInBranch2(true)
                        .availableInBranch3(false)
                        .availableInBranch1(true)
                        .build();
                productsService.createProduct(productDTO);
                log.info("Product successfully created");
            }
            if (loyaltyRepository.findAll().isEmpty()){
                Loyalty loyalty = Loyalty.builder()
                        .count(5L)
                        .discountValue(BigDecimal.valueOf(200))
                        .build();
                loyaltyRepository.save(loyalty);
            }

            Optional<User> user = userRepository.findByEmailAndDeletedFalse(DEFAULT_ADMIN_EMAIL);
            if (user.isEmpty()) {
                log.info("creating default admin user");
                User admin = User.builder()
                        .firstName("Timmy")
                        .lastName("Admin")
                        .emailVerified(true)
                        .deleted(false)
                        .enabled(true)
                        .email(DEFAULT_ADMIN_EMAIL)
                        .branchId(1)
                        .password("$2a$12$V/V59RJ7HPAyMFbOrNrbJOz2yBUt7aLlLigKwQsSzMAyAMPuBrqgO")
                        .role(savedRole)
                        .build();

                userRepository.save(admin);

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
