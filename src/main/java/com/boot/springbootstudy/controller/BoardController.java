package com.boot.springbootstudy.controller;

import com.boot.springbootstudy.dto.BoardDTO;
import com.boot.springbootstudy.dto.BoardListReplyCountDTO;
import com.boot.springbootstudy.dto.PageRequestDTO;
import com.boot.springbootstudy.dto.PageResponseDTO;
import com.boot.springbootstudy.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /* Java Beans와 @ModelAttribute : 스프링 MVC의 컨틀롤러는 파라미터로 getter/setter를 이용하는 Java Beans 형식의
                                         사용자 정의 클래스가 파라미터인 경우 자동으로 화면까지 객체를 전달합니다. */
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }

    //게시글 글쓰기
    @GetMapping("/register")
    public void registerGET(){

    }

    //게시글 등록
    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        log.info("board POST register.......");

        if(bindingResult.hasErrors()){
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno  = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";

    }

    //게시글 읽기 / 수정하기
    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO, @Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        log.info("board modify post......." + boardDTO);

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );

            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?"+link;
        }

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";

// redirectAttributes.addAttribute("bno", boardDTO.getBno());는 리다이렉트 시에 URL에 쿼리 파라미터를 추가하는 데 사용됩니다. 이 경우, bno라는 이름의 쿼리 파라미터에 boardDTO.getBno()의 값을 추가하려는 것입니다.
//따라서 return "redirect:/board/modify?"+link;가 실행될 때, bno라는 쿼리 파라미터에 해당 게시물의 번호가 추가됩니다. 이를 통해 리다이렉트된 페이지에서 해당 게시물을 식별하고 사용할 수 있습니다.
//예를 들어, /board/modify로 리다이렉트되면서 bno=123과 같은 형태로 쿼리 파라미터가 추가됩니다. 이 경우, 리다이렉트된 페이지에서 URL을 분석하여 bno 값을 읽어 해당 게시물을 식별하고 처리할 수 있습니다.

    }

    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {

        log.info("remove post.. " + bno);

        boardService.remove(bno);

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";

    }

}
