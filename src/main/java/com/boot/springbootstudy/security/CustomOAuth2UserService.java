package com.boot.springbootstudy.security;

import com.boot.springbootstudy.domain.Member;
import com.boot.springbootstudy.domain.MemberRole;
import com.boot.springbootstudy.repository.MemberRepository;
import com.boot.springbootstudy.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //로그인한 사용자 정보
    @Override   //OAuth2UserRequest : OAuth 2.0 프로토콜에 따른 사용자 요청 정보를 포함
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("userRequest");
        log.info(userRequest);  //사용자 요청 정보를 확인

        log.info("oauth2 user..........................");

        //사용자 요청에 대한 클라이언트 등록 정보를 가져옵니다. 클라이언트 등록 정보에는 OAuth 2.0 클라이언트에 대한 정보가 포함됩니다.
        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        //클라이언트 등록 정보에서 클라이언트 이름을 가져옵니다.
        String clientName = clientRegistration.getClientName();

        log.info("NAME: " + clientName);

        //***ClientRegistration은 현재 OAuth 2.0 클라이언트의 등록 정보를 나타내는 반면, OAuth2User는 사용자의 인증 및 프로필 정보를 나타냅니다.
        //DefaultOAuth2UserService 클래스의 loadUser 메소드를 호출하여 OAuth 2.0 사용자 정보를 가져옵니다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //OAuth 2.0 사용자 정보에서 속성(attribute) 맵을 가져옵니다. 이 맵에는 사용자의 프로필 정보가 포함됩니다.
        Map<String,Object> paramMap = oAuth2User.getAttributes();

        String email = null;

        switch (clientName){
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
        }

        log.info("==================");

        log.info(email);

        log.info("==================");

        return generateDTO(email, paramMap);
    }


    //회원 정보(기존에 없으면) 추가 또는 반환
    private MemberSecurityDTO generateDTO(String email, Map<String, Object> params) {

        Optional<Member> result = memberRepository.findByEmail(email);

        //데이터베이스에 해당 이메일 사용자가 없다면
        if (result.isEmpty()) {
            //회원추가 -- mid는 이메일 주소 / 패스워드는 1111
            Member member = Member.builder()
                    .mid(email)
                    .mpw(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .build();

            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            //MemberSecurityDTO 구성 및 반환
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(email, "1111", email, false, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(params);

            return memberSecurityDTO;
        }else { //데이터베이스에 해당 이메일 사용자가 있다면
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(member.getMid(),
                            member.getMpw(),
                            member.getEmail(),
                            member.isDel(),
                            member.isSocial(),
                            member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name())).collect(Collectors.toList()));
            return memberSecurityDTO;
        }
    }


    private String getKakaoEmail(Map<String, Object> paramMap){

        log.info("KAKAO-------------------------------------");

        Object value = paramMap.get("kakao_account");

        log.info(value);

        //Map 안에 Map인듯.
        //value에는 카카오 계정 정보가 포함되어 있습니다. 예를 들어, 이메일 주소, 닉네임 등 다양한 사용자 정보가 들어 있을 수 있습니다.
        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String)accountMap.get("email");

        log.info("email..." + email);

        return email;
    }

}
