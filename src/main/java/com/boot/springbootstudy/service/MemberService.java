package com.boot.springbootstudy.service;

import com.boot.springbootstudy.dto.MemberJoinDTO;

public interface MemberService {

    //MidExistException은 Exception 클래스를 상속받은 사용자 정의 예외 클래스입니다.
    //이 예외 클래스는 MemberService 인터페이스와 관련된 특정 상황(회원 아이디가 이미 존재하는 경우)을 나타내기 위해 사용됩니다.
    //static클래스로 선언해서 필요한 곳에서 사용
    static class MidExistException extends Exception{

    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;

}
