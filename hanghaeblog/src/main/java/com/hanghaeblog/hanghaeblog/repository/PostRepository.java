package com.hanghaeblog.hanghaeblog.repository;

import com.hanghaeblog.hanghaeblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    /*내림차순 설정*/


}
