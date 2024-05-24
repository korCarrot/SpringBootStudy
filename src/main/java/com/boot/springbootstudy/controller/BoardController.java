package com.boot.springbootstudy.controller;

import com.boot.springbootstudy.dto.*;
import com.boot.springbootstudy.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    @Value("${com.boot.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;

    private final BoardService boardService;

    /* Java Beans와 @ModelAttribute : 스프링 MVC의 컨틀롤러는 파라미터로 getter/setter를 이용하는 Java Beans 형식의
                                         사용자 정의 클래스가 파라미터인 경우 자동으로 화면까지 객체를 전달합니다. */
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

//        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }

    //게시글 글쓰기
    @PreAuthorize("hasRole('USER')")
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
    @PreAuthorize("isAuthenticated()")  //로그인한 사람만 글 조회 가능 -> 로그인이 안되어있다면 로그인 페이지로 이동
    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    //#boardDTO는 컨트롤러의 매개변수를 참조 (ChatGPT)
    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/modify")
    public String modify(@Valid BoardDTO boardDTO, PageRequestDTO pageRequestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

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

//    @PostMapping("/remove")
//    public String remove(Long bno, RedirectAttributes redirectAttributes) {
//
//        log.info("remove post.. " + bno);
//
//        boardService.remove(bno);
//
//        redirectAttributes.addFlashAttribute("result", "removed");
//
//        return "redirect:/board/list";
//
//    }

    //#boardDTO는 컨트롤러의 매개변수를 참조 (ChatGPT)
    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {

        Long bno = boardDTO.getBno();
        log.info("remove post.. " + bno);

        boardService.remove(bno);

        //게시물이 데이터베이스상에서 삭제되었다면 첨부파일 삭제
        log.info(boardDTO.getFileNames());
        List<String> fileNames = boardDTO.getFileNames();
        if (fileNames != null && fileNames.size() > 0) {
            removeFiles(fileNames);
        }


        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";
    }

    public void removeFiles(List<String> files) {

        for (String fileName : files){

            Resource resource = new FileSystemResource(uploadPath+ File.separator+fileName);

//            String resourceName = resource.getFilename();

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());

                resource.getFile().delete();    //파일 삭제

                //섬네일이 존재한다면
                if (contentType.startsWith("image")){
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);

                    thumbnailFile.delete(); //섬네일 파일 삭제
                }

            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

    }


}
