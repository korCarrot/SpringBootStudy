package com.boot.springbootstudy.repository;

import com.boot.springbootstudy.domain.Member;
import com.boot.springbootstudy.domain.MemberRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //회원 추가
    @Test
    public void insertMembers(){

        IntStream.rangeClosed(1,100).forEach(i -> {

            Member member = Member.builder()
                    .mid("member"+i)
                    .mpw(passwordEncoder.encode("1111"))
                    .email("email"+i+"@aaa.bbb")
                    .build();

            member.addRole(MemberRole.USER);

            if (i >= 90) {
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);

        });

    }


    //회원 조회
    @Test
    public void testRead() {    //member100이 USER와 ADMIN 권한을 모두 가지고 있는지 테스트

        Optional<Member> result = memberRepository.getWithRoles("member100");

        Member member = result.orElseThrow();

        log.info(member);
        log.info(member.getRoleSet());

        member.getRoleSet().forEach(memberRole -> log.info(memberRole.name()));

    }


    @Commit
    @Test
    public void testUpdate(){

        String mid = "cookie_00@naver.com"; //소셜로그인으로 추가된 사용자로 현재 DB에 존재하는 이메일      //내 카카오톡 계정으로 로그인시..
        String mpw = passwordEncoder.encode("54321");

        memberRepository.updatePassword(mid, mpw);
    }

}
