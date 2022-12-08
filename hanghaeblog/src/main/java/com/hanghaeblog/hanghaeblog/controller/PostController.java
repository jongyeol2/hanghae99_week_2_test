package com.hanghaeblog.hanghaeblog.controller;

import com.hanghaeblog.hanghaeblog.dto.PostDeleteRequestDto;
import com.hanghaeblog.hanghaeblog.dto.PostDeleteResponseDto;
import com.hanghaeblog.hanghaeblog.dto.PostRequestDto;
import com.hanghaeblog.hanghaeblog.dto.PostResponseDto;
import com.hanghaeblog.hanghaeblog.service.PostService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts")                               /* 게시글 전체 조회*/
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    @PostMapping("/api/posts")                                /* 게시글 작성*/
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }

    @GetMapping("/api/posts/{id}")                      //게시글 선택 조회
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }



    @PutMapping("/api/posts/{id}")                        /* 게시글 수정*/
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.updatePost(id, requestDto, request);
    }

    @DeleteMapping("/api/post/{id}") //선택한 게시글 삭제
    public PostDeleteResponseDto deletePost(@PathVariable Long id, HttpServletRequest request){
        return postService.deletePost(id, request);
    }

//    @DeleteMapping("/api/posts/{id}")                      /* 게시글 삭제*/
//    public String deletePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
//        return postService.deletePost(id, postRequestDto);
//    }


}
