package com.sparta.moviefeed.entity;

import com.sparta.moviefeed.dto.requestdto.UserSignupRequestDto;
import com.sparta.moviefeed.enumeration.UserStatus;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserEntityTest {

    UserSignupRequestDto signupRequestDto;

    @BeforeEach
    void setUp() {
        signupRequestDto = new UserSignupRequestDto();
        signupRequestDto.setUserName("junbin");
        signupRequestDto.setUserId("useridtest123");
        signupRequestDto.setPassword("Useruser1234@");
        signupRequestDto.setEmail("user12@email.com");
        signupRequestDto.setIntro("한 줄 소개 입니다.");
    }

    @Test
    @DisplayName("유저 엔티티 등록 성공 테스트")
    void userEntityTest() {
        // given
        String username = "junbin";
        String userId = "useridtest123";
        String password = "Useruser1234@";
        String email = "user12@email.com";
        String intro = "한 줄 소개 입니다.";
        // when
        User user = new User(signupRequestDto, UserStatus.NORMAL);
        // then
        assertEquals(username, user.getUserName());
        assertEquals(userId, user.getUserId());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(intro, user.getIntro());
    }
}
