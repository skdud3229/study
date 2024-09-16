package com.example.web_ide.controller;
import com.example.web_ide.domain.BoardResponse;
import com.example.web_ide.domain.BoardSummary;
import com.example.web_ide.domain.PageResponse;
import com.example.web_ide.domain.dao.Board;
import com.example.web_ide.domain.BoardRequest;
import com.example.web_ide.service.BoardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String Hello(){
        return "hello!";
    }

    /* board pagination api, 양수인 page와 size를 요청받음 */
    @GetMapping("/board")
    public ResponseEntity<PageResponse<BoardSummary>> getBoardPage(@Positive(message="게시글 페이지는 0보다 커야 합니다.") @RequestParam(defaultValue = "1") int page, @Positive(message="게시글 개수는 0보다 커야 합니다.") @RequestParam(defaultValue = "20") int size){
        Page<Board> boardPage=boardService.getBoardPage(page,size); //현재 0페이지부터 시작하므로 바꿔줘야 함
        /* dto로의 변환은 controller에서 수행 -> 이후에 view 상에서 요청하는 데이터가 바뀔 때 controller만 변경 */
        PageResponse<BoardSummary> res= PageResponse.from(boardPage,BoardSummary::from);
        return ResponseEntity.ok(res);
    }

    /* board의 상세정보를 불러오는 api */
    @GetMapping("/board/{id}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long id){
        Board board=boardService.getBoard(id); //존재하지 않는 id일 경우 NoSuchElementException 발생
        BoardResponse res= BoardResponse.from(board);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/board")
    public ResponseEntity<BoardResponse> createBoard(@RequestBody @Valid BoardRequest boardRequest){
        Board board=boardService.createBoard(boardRequest);
        BoardResponse res=BoardResponse.from(board);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/board/{id}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long id, @RequestBody @Valid BoardRequest boardRequest){
        Board board=boardService.updateBoard(id,boardRequest);
        BoardResponse res=BoardResponse.from(board);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id){
        boardService.deleteBoard(id);
        return ResponseEntity.ok().build();
    }
}
