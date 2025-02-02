package com.sparta.moviefeed.controller;

import com.sparta.moviefeed.dto.requestdto.BoardRequestDto;
import com.sparta.moviefeed.dto.responsedto.BoardResponseDto;
import com.sparta.moviefeed.dto.responsedto.CommonResponse;
import com.sparta.moviefeed.security.UserDetailsImpl;
import com.sparta.moviefeed.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 게시글 등록 기능 ( 인가 필요 )
     * @param requestDto : 등록할 게시글의 정보
     * @return : 등록 된 게시글의 정보 및 username
     */
    @PostMapping // @AuthenticationPrincipal UserDetails
    public ResponseEntity<CommonResponse<BoardResponseDto>> postingBoard(
            @RequestBody BoardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        BoardResponseDto responseDto = boardService.postingBoard(requestDto, userDetails.getUser());
        CommonResponse<BoardResponseDto> response = new CommonResponse<>(201, "게시글 등록 성공", responseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 특정 게시글 조회 기능
     * @param boardId : 특정 게시글의 번호
     * @return : 특정 게시글 조회 데이터
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<CommonResponse<BoardResponseDto>> selectBoard(@PathVariable Long boardId) {
        BoardResponseDto responseDto = boardService.selectBoard(boardId);
        CommonResponse<BoardResponseDto> response = new CommonResponse<>(200, "게시글 조회 성공", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 전체 게시글 조회 기능
     * @return : 전체 게시글 조회 데이터
     */
    @GetMapping
    public ResponseEntity<CommonResponse<List<BoardResponseDto>>> selectAllBoard() {
        List<BoardResponseDto> boards = boardService.selectAllBoard();
        CommonResponse<List<BoardResponseDto>> response = new CommonResponse<>(200, "게시글 전체 조회 성공",boards);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 게시글 수정 기능 ( 인가 필요 )
     * @param boardId : 수정할 게시글의 id
     * @param requestDto : 수정할 게시글의 정보
     * @return : 수정된 게시글의 정보
     */
    @PutMapping("/{boardId}")
    public ResponseEntity<CommonResponse<BoardResponseDto>> updateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        BoardResponseDto responseDto = boardService.updateBoard(boardId, requestDto, userDetails.getUser());
        CommonResponse<BoardResponseDto> response = new CommonResponse<>(200, "게시글 수정 성공", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 게시글 삭제 기능 ( 인가 필요 )
     * @param boardId : 삭제할 게시글의 id
     * @return : 삭제 완료 메시지 상태 코드 반환
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonResponse<Void>> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        boardService.deleteBoard(boardId, userDetails.getUser());
        CommonResponse<Void> response = new CommonResponse<>(204, "게시글 삭제 성공");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * 좋아요 등록 기능 ( 인가 필요 )
     * @param boardId : 좋아요 횟수가 추가 될 게시글 번호
     * @return : 좋아요 등록에 대한 성공 메시지 및 상태 코드 반환
     */
    @PostMapping("/{boardId}/like")
    public ResponseEntity<CommonResponse<Long>> increasedBoardLikes(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        Long likeCount = boardService.increasedBoardLikes(boardId, userDetails.getUser());
        CommonResponse<Long> response = new CommonResponse<>(201, "게시글 좋아요 등록 성공", likeCount);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 특정 게시글 좋아요 조회 기능 ( 좋아요가 없을 경우 0을 반환 ) => Long default 0
     * @param boardId : 특정 게시글의 게시글 번호
     * @return : likeCount => 특정 게시글의 좋아요 갯수 반환
     */
    @GetMapping("/{boardId}/like")
    public ResponseEntity<CommonResponse<Long>> findByBoardLike(@PathVariable Long boardId) {
        Long likeCount = boardService.findByBoardLike(boardId);
        CommonResponse<Long> response = new CommonResponse<>(200, "해당 게시글의 좋아요 조회 성공", likeCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 좋아요 삭제 기능
     * @param boardId : 삭제할 좋아요의 게시글의 번호
     * @param userDetails : 삭제할 좋아요의 유저 번호
     * @return :
     */
    @DeleteMapping("/{boardId}/like")
    public ResponseEntity<CommonResponse<Long>> deleteBoardLike(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        Long likeCount = boardService.deleteBoardLike(boardId, userDetails.getUser());
        CommonResponse<Long> response = new CommonResponse<>(204, "좋아요가 삭제되었습니다.", likeCount);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}