package com.sparta.moviefeed.entity;

import com.sparta.moviefeed.dto.requestdto.BoardRequestDto;
import com.sparta.moviefeed.dto.requestdto.UserSignupRequestDto;
import com.sparta.moviefeed.enumeration.LikeType;
import com.sparta.moviefeed.enumeration.UserStatus;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LikeEntityTest {

    UserSignupRequestDto signupRequestDto;
    BoardRequestDto boardRequestDto;

    @BeforeEach
    void setUp() {
        signupRequestDto = new UserSignupRequestDto();
        signupRequestDto.setUserName("junbin");
        signupRequestDto.setUserId("useridtest123");
        signupRequestDto.setPassword("Useruser1234@");
        signupRequestDto.setEmail("user12@email.com");
        signupRequestDto.setIntro("한 줄 소개 입니다.");

        boardRequestDto = new BoardRequestDto();
        boardRequestDto.setTitle("게시글 제목");
        boardRequestDto.setContent("게시글 내용");
    }

    @Test
    @DisplayName("좋아요 엔티티 등록 성공 테스트")
    void likeEntityTest() {
        // given
        String username = "junbin";
        String title = "게시글 제목";
        // when
        User user = new User(signupRequestDto, UserStatus.NORMAL);
        Board board = new Board(boardRequestDto, user);
        Like like = new Like(user, board, LikeType.BOARD);
        // then
        assertEquals(username, like.getUser());
        assertEquals(title, like.getBoard());
    }
}
