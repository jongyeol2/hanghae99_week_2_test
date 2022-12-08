package com.hanghaeblog.hanghaeblog.service;

import com.hanghaeblog.hanghaeblog.dto.PostDeleteResponseDto;
import com.hanghaeblog.hanghaeblog.dto.PostRequestDto;
import com.hanghaeblog.hanghaeblog.dto.PostResponseDto;
import com.hanghaeblog.hanghaeblog.entity.Post;
import com.hanghaeblog.hanghaeblog.entity.User;
import com.hanghaeblog.hanghaeblog.jwt.JwtUtil;
import com.hanghaeblog.hanghaeblog.repository.PostRepository;
import com.hanghaeblog.hanghaeblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        // Request 에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Post post = postRepository.saveAndFlush(new Post(requestDto, user));

            return new PostResponseDto(post);
        } else {
            return null;
        }
        // 토큰이 있는 경우에만 post 작성 가능

    }

    @Transactional(readOnly = true) // 스프링에서 제공하는 읽기 전용 모드로 설정
    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            Post post = null;
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
                String username = claims.getSubject(); //토큰  안에 있는 username을 가져온 것.

                post = postRepository.findById(id).orElseThrow(//memoRepository (memo Entity)DB에서 가져온다.
                        () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );
                if (username.equals(post.getUsername())) {
                    //update 진행
                    post.update(requestDto); //
                } else {
                    //에러 알람.
                    throw new IllegalArgumentException("사용자가 다릅니다.");
                }
            }
            return new PostResponseDto(post);
        } else {
            throw new IllegalArgumentException("Token Error");
        }
    }

    @Transactional
    public PostDeleteResponseDto deletePost(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {

            Post post = null;

            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
                String username = claims.getSubject(); // 토큰안에 username 가져오기

                post = postRepository.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );
                if (username.equals(post.getUsername())) {
                    postRepository.deleteById(id);
                } else {
                    throw new IllegalArgumentException("사용자가 다릅니다.");
                }
            }
            return new PostDeleteResponseDto("게시글 삭제 성공", HttpStatus.OK.value());
        }else {
            throw new IllegalArgumentException("Token Error");
        }

    }

    @Transactional (readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new PostResponseDto(post);
    }
}