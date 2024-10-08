package com.tenco.blog_v1.board;

import com.tenco.blog_v1.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController  {


    // 네이티브 쿼리 연습
    private final BoardNativeRepository boardNativeRepository;
    // jPA API ,JPQL
    private  final BoardRepository boardRepository;
    private final HttpSession session;

    // 특정 게시글 요청 화면
    // 주소설계 - http://localhost:8080/board/1
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Integer id, HttpServletRequest request) {
        // JPA API 사용
        // Board board = boardRepository.findById(id);

        // JPQL FETCH join 사용
        Board board= boardRepository.findByJoinUser(id);
        request.setAttribute("board", board);
        return "board/detail";
    }




    @GetMapping("/")
    public String index(Model model) {

//        List<Board> boardList = boardNativeRepository.findAll();
//        코드수정
        List<Board> boardList = boardRepository.findAll();
        model.addAttribute("boardList", boardList);
        log.warn("여기까지 오니");
        return "index";
    }

    // 주소설계 - http://localhost:8080/board/save-form
    // 게시글 작성 화면
    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }



    // 게시글 저장
    // 주소설계 - http://localhost:8080/board/save
    @PostMapping("/board/save")
    public String save(@ModelAttribute  BoardDTO.SaveDTO reqDto) {
        User sessionUser = (User )session.getAttribute("sessionUser");
        if (sessionUser == null) {

            return "redirect:/login-form";
        }

        // 파라미터가 올바르게 전달 되었는지 확인
        // SaveDTO 에서 ToEntity 사용해서 Board 엔티티로 변환하고 인수 값으로 User 정보 를 넣었다.
        log.warn("save 실행: 제목={}, 내용={}", reqDto.getTitle(), reqDto.getContent());
//        boardNativeRepository.save(title, content);
        boardRepository.save(reqDto.toEntity(sessionUser));
        return "redirect:/";
    }





    // 주소설계 - http://localhost:8080/board/10/delete ( form 활용이기 때문에 delete 선언)
    // form 태크에서는 GET, POST 방식만 지원하기 때문이다.
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id) {
        // 유효성, 인증검사
        // 세션에서 로그인 사용자 정보 가져오기 ->인증(로그인 여부), 인가(권한 -내글?)

        User sessionUser = (User )session.getAttribute("sessionUser");

        if(sessionUser==null){
            return  "redirect:/login-form";
        }

        //권한 체크
        Board board= boardRepository.findById(id);
        if (board == null) {

            return"redirect:/error-404";
        }
        if (! board.getUser().getId().equals( sessionUser.getId()  )) {
            return  "redirect:/error-403";

        }
//        boardRepository.deleteById(id); // JPQL 식 DELETE
            boardRepository.jpaDeleteById(id); //JPA EM식 DELETE
        return "redirect:/";

    }


    // 게시글 수정 화면 요청
    // board/id/update
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Integer id, HttpServletRequest request) {
        Board board = boardNativeRepository.findById(id);
        request.setAttribute("board", board);
        return "board/update-form"; // src/main/resources/templates/board/update-form.mustache
    }

    // 게시글 수정 요청 기능
    // board/{id}/update
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable(name = "id") Integer id, @RequestParam(name = "title") String title, @RequestParam(name = "content") String content) {
        boardNativeRepository.updateById(id, title, content);
        return "redirect:/board/" + id;
    }

}
