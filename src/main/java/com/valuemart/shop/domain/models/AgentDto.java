package com.valuemart.shop.domain.models;


import com.valuemart.shop.domain.models.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@NoArgsConstructor
@Data
@Slf4j
public class AgentDto extends UserDto {

    private Long id;
    private static final String BASE_LINK = "https://play.google.com/store/apps/details?id=je.data4me.jara&referrer=utm_campaign%3D";
    private String agentCode;
    private String link;
    private String photo;
    private boolean emailVerified;
    private String superAgentCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean kycVerified;
    private boolean activated;



//    @Builder
//    public AgentDto(Long id,
//                    String firstname,
//                    String lastname,
//                    String email,
//                    String phone,
//                    String roleType,
//                    String agentCode,
//                    String link,
//                    String photo,
//                    boolean emailVerified,
//                    boolean activated,
//                    boolean enabled,
//                    LocalDateTime createdAt, LocalDateTime updatedAt) {
//        super(id, firstname, lastname, email, phone, roleType, emptySet(), enabled);
//        this.agentCode = agentCode;
//        this.link = link;
//        this.photo = photo;
//        this.emailVerified = emailVerified;
//        this.activated = activated;
//        this.kycVerified = kycVerified;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.id = id;
//    }
//
//


//
//    public static AgentDto valueOf(User user, String photo) {
//        AgentDtoBuilder builder = builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .firstname(user.getFirstName())
//                .lastname(user.getLastName())
//                .phone(user.getPhone())
//                .emailVerified(user.isEmailVerified())
//                .roleType(user.getRole().getName())
//                .enabled(user.isEnabled())
//                .createdAt(user.getCreatedAt())
//                .updatedAt(user.getUpdatedAt())
//                .photo(photo)
//                        .activated(user.isActivated());
//
//
//
//
//
//        var agentDto = builder.build();
//
//        return agentDto;
//    }
}
