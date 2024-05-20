package com.boot.springbootstudy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//http://localhost:8080/swagger-ui/
//swagger-ui 사용할 때 경로 맨 마지막에 '/' 작성해주어야 함.
@Configuration //해당 클래스가 스프링빈(Bean)에 대한 설정을 하는 클래스임을 명시
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {

    //Swagger UI가 적용되면서 달라진 정적 파일의 경로를 다시 재정의
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
    }
}
