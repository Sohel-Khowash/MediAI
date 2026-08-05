package com.sohel.healthcare.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserProfileDto {

    private String name;
    private String email;
    private String password;
}
