package com.sparta.moviefeed.dto;

import com.sparta.moviefeed.dto.requestdto.BoardRequestDto;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BoardDtoTest {

    @Test
    @DisplayName("게시글 DTO 등록 성공 테스트")
    void boardDtoTest() {
        // given
        String title = "제목1";
        String content = "게시글 내용1";
        // when
        BoardRequestDto requestDto = new BoardRequestDto();
        requestDto.setTitle("제목1");
        requestDto.setContent("게시글 내용1");
        // then
        assertEquals(title, requestDto.getTitle());
        assertEquals(content, requestDto.getContent());
    }
}
