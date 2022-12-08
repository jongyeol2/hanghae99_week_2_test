package com.hanghaeblog.hanghaeblog.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SignupRequestDto {

    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;

    @Pattern(regexp = "^[A-Za-z0-9]{8,15}$")
    private String password;
}
