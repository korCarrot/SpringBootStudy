package com.boot.springbootstudy.service;

import com.boot.springbootstudy.domain.Member;
import com.boot.springbootstudy.domain.MemberRole;
import com.boot.springbootstudy.dto.MemberJoinDTO;
import com.boot.springbootstudy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {

        String mid = memberJoinDTO.getMid();

        boolean exist = memberRepository.existsById(mid);   //특정 ID를 가진 엔티티가 데이터베이스에 존재하는지 확인하는 데 사용

        if (exist) {
            throw new MidExistException();
        }

       Member member = modelMapper.map(memberJoinDTO, Member.class);
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        member.addRole(MemberRole.USER);

        log.info("========================");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);

    }
}
