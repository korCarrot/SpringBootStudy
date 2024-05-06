package com.boot.springbootstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  //@EnableJpaAuditing은 Spring Boot 애플리케이션에서 @SpringBootApplication 어노테이션이 있는 클래스나 @Configuration 클래스에서 선언됩니다. 이렇게 하면 JPA Auditing이 해당 애플리케이션의 모든 JPA 엔티티에서 자동으로 활성화
//@EnableJpaAuditing : 엔티티의 생성일과 수정일을 자동으로 설정하기 위해 Spring Data JPA의 AuditingEntityListener를 활성화
public class SpringBootStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStudyApplication.class, args);
    }

}
