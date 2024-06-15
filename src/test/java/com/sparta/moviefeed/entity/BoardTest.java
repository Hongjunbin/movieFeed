package com.sparta.moviefeed.entity;

import com.sparta.moviefeed.dto.requestdto.BoardRequestDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    BoardRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new BoardRequestDto();
        requestDto.setTitle("제목1");
        requestDto.setContent("게시글내용1");
    }

    @Test
    @DisplayName("BoardRequestDto 성공 테스트")
    void boardDtoTest() {
        // given-when
        String title = "제목1";
        String content = "게시글내용1";

        // then
        assertEquals(title, requestDto.getTitle());
        assertEquals(content , requestDto.getContent());
    }


}
