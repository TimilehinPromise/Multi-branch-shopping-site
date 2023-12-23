package com.valuemart.shop.domain.util;

import com.valuemart.shop.exception.NotFoundException;
import com.valuemart.shop.persistence.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


import static com.valuemart.shop.domain.models.RoleType.*;


public final class UserUtils {

    private UserUtils() {
    }

    public static User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new NotFoundException("user not found");
        }
        return ((User) authentication.getPrincipal());
    }




        public static String generateCustomerCode(String customerName, Long customerId, LocalDateTime signupDate) {
            // Format the date to get the full month name
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
            String fullMonthName = signupDate.format(monthFormatter);

            // Extract the last two characters of the month name
            String monthPart = fullMonthName.substring(fullMonthName.length() - 2).toUpperCase();

            // Get initials from the customer name
            String initials = getInitials(customerName);

            // Generate a random number (0-99)
            Random rand = new Random();
            int randomPart = rand.nextInt(100); // Random number between 0 and 99

            // Combine all parts
            return initials + customerId + String.format("%02d", randomPart) + monthPart;
        }

        private static String getInitials(String name) {
            String[] nameParts = name.toUpperCase().split("\\s+");
            StringBuilder initials = new StringBuilder();
            for (String part : nameParts) {
                if (!part.isEmpty()) {
                    initials.append(part.charAt(0));
                }
            }
            return initials.toString();
        }




    public static void main(String[] args) {
      String code = generateCustomerCode("Timilehin Awoyeye",20L,LocalDateTime.now());
        System.out.println(code);
    }
    }





