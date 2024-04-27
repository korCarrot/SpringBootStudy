package com.boot.springbootstudy.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
public class SampleController {

    @GetMapping("/hello")
    public void hello(Model model){
        log.info("hello....");
        model.addAttribute("msg", "Hello World");
    }

    @GetMapping(value = {"/ex/ex1", "/ex/ex1a", "ex/ex1b"})
    public void ex1(Model model){

        List<String> list = Arrays.asList("AAA","BBB","CCC","DDD");

        model.addAttribute("list", list);

    }

    @GetMapping(value = "/ex/ex2")
    public void ex2(Model model){

        List<String> list = Arrays.asList("AAA","BBB","CCC","DDD");

        model.addAttribute("list", list);

    }

    class SampleDTO {
        private String p1,p2,p3;

        public String getP1() {
            return p1;
        }
        public String getP2() {
            return p2;
        }
        public String getP3() {
            return p3;
        }
    }

    @GetMapping(value =  "/ex/ex2a")
    public void ex2a(Model model){

        log.info("ex/ex2................");
/*IntStream.range(1, 10)은 1부터 9까지의 정수 스트림을 생성합니다.
그런 다음 mapToObj를 사용하여 각 숫자를 "Data"와 결합하여 새로운 문자열을 만들고,
collect(Collectors.toList())를 사용하여 결과를 문자열 리스트로 수집합니다.
 */
        List<String> strList = IntStream.range(1,10)
                .mapToObj(i -> "Data"+i)
                .collect(Collectors.toList());  // 리스트로 수집

        model.addAttribute("list", strList);

        Map<String, String> map = new HashMap<>();
        map.put("A","AAAA");
        map.put("B","BBBB");

        model.addAttribute("map", map);

        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.p1 ="Value -- p1";
        sampleDTO.p2 ="Value -- p2";
        sampleDTO.p3 ="Value -- p3";

        model.addAttribute("dto", sampleDTO);

    }

    @GetMapping("/ex/ex3")
    public void ex3(Model model) {

       model.addAttribute("arr",new String[]{"AAA", "BBB", "CCC"});

    }


}
