package com.boot.springbootstudy.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    private String mid;
    private String mpw;
    private String email;

    private boolean del;
    private boolean social;

    private Map<String,Object> props;   //소셜 로그인 정보

    //GrantedAuthority : Spring Security에서 사용자의 권한을 나타내는 인터페이스입니다. 이 인터페이스를 구현한 클래스는 사용자가 가진 권한(예: ROLE_USER, ROLE_ADMIN 등)을 나타냅니다.
    public MemberSecurityDTO(String username, String password, String email, boolean del, boolean social, Collection<? extends GrantedAuthority> authorities) {

        //super(username, password, authorities); : 부모 클래스인 User의 생성자를 호출하는 코드
        //User 클래스는 Spring Security에서 제공하는 UserDetails 인터페이스의 기본 구현체입니다.
        super(username, password, authorities);

        this.mid = username;
        this.mpw = password;
        this.email = email;
        this.del = del;
        this.social = social;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.mid;
    }
}
