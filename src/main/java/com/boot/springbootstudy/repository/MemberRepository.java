package com.boot.springbootstudy.repository;

import com.boot.springbootstudy.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid=:mid and m.social = false")
    Optional<Member> getWithRoles(String mid);

    //email을 이용해서 회원정보 찾기
    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);

    @Modifying  // 기본적으로 Spring Data JPA는 @Query 애너테이션을 사용한 메소드가 데이터베이스를 읽는 쿼리인지 쓰는 쿼리인지 구분하지 않습니다. 따라서 데이터베이스를 수정하는 쿼리를 실행할 때는 @Modifying을 함께 사용하여 이를 명시해 줘야 합니다.
    @Transactional
    @Query("update Member m set m.mpw = :mpw where m.mid = :mid")
    void updatePassword(@Param("mpw") String password, @Param("mid") String mid);
}
