package com.hanghaeblog.hanghaeblog.entity;

import com.hanghaeblog.hanghaeblog.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)     /*  제목  */
    private String title;

    @Column(nullable = false)     /*  작성자명  */
    private String username;

    @Column(nullable = false)    /*  작성내용  */
    private String contents;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.username = user.getUsername();
        this.contents = requestDto.getContents();
        this.user = user;
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
//        this.username = requestDto.getUsername();
        this.contents = postRequestDto.getContents();
//        this.password = requestDto.getPassword();
    }



}
