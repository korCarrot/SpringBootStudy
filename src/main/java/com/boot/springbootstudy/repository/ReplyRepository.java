package com.boot.springbootstudy.repository;

import com.boot.springbootstudy.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r where r.board.bno = :bno")    // :bno는 쿼리 메소드의 매개변수로 전달된 값에 해당.
    Page<Reply> listOfBoard(Long bno, Pageable pageable);       // JPQL에서 쿼리에 파라미터를 사용할 때는 쿼리 문자열 안에 :를 사용하여 파라미터를 표시.

    //"Board의 bno 값이 주어진 bno와 일치하는 모든 Reply 엔티티를 삭제한다"
    //deleteBy로 시작하여 그 뒤에는 엔티티의 속성명, 그 뒤에 _를 사용하여 Board 엔티티의 속성명, 마지막으로 메서드의 매개변수로 전달된 값에 해당하는 엔티티를 삭제.
    void deleteByBoard_Bno(Long bno);   // Spring Data JPA의 Query Creation 기능을 사용하여 정의된 메서드. 메서드 이름에 따라 자동으로 쿼리를 생성.
}
