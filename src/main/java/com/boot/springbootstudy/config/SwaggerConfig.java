//package com.boot.springbootstudy.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//@Configuration
//public class SwaggerConfig {
//
////  Docket 객체를 통해 Swagger 문서의 구성을 제어하고 API를 문서화
///*
//useDefaultResponseMessages(false) :  기본 응답 메시지를 사용하지 않도록 설정합니다. 즉, 사용자가 직접 응답 메시지를 정의할 수 있습니다
//select() : API 문서에 표시할 요청 핸들러를 선택
//apis(RequestHandlerSelectors.basePackage("com.boot.springbootstudy")) : 지정된 패키지 내의 요청 핸들러만을 선택하도록 설정
//paths(PathSelectors.any()) : 모든 경로를 포함하도록 설정합니다. 이는 API 문서에 프로젝트 내의 모든 엔드포인트를 포함
//apiInfo(apiInfo()) : API 문서의 정보를 설정하는 메서드
// */
//    @Bean
//    public Docket api(){
//        return new Docket(DocumentationType.OAS_30)
//                .useDefaultResponseMessages(false)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.boot.springbootstudy"))
//
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Boot 01 Project Swagger")
//                .build();
//    }
//
//}
//
