package com.hanghaeblog.hanghaeblog.dto;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class PostRequestDto {
//    private Long id;
    private String title;
//    private String username;
    private String contents;
//    private String password;
    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

}

