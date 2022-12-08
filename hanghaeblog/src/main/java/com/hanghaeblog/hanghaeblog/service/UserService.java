package com.hanghaeblog.hanghaeblog.service;


import com.hanghaeblog.hanghaeblog.dto.LoginRequestDto;
import com.hanghaeblog.hanghaeblog.dto.LoginResponseDto;
import com.hanghaeblog.hanghaeblog.dto.SignupRequestDto;
import com.hanghaeblog.hanghaeblog.dto.SignupResponseDto;
import com.hanghaeblog.hanghaeblog.entity.User;
import com.hanghaeblog.hanghaeblog.entity.UserRoleEnum;
import com.hanghaeblog.hanghaeblog.jwt.JwtUtil;
import com.hanghaeblog.hanghaeblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();


        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(username, password, role);
        userRepository.save(user);

        return new SignupResponseDto("회원가입 성공", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();


        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        if(!user.getPassword().equals(password)){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        return new LoginResponseDto("로그인 성공", HttpStatus.OK.value());

    }

}