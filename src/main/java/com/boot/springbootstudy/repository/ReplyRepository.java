package com.boot.springbootstudy.repository;

import com.boot.springbootstudy.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r where r.board.bno = :bno")    // :bno는 쿼리 메소드의 매개변수로 전달된 값에 해당.
    Page<Reply> listOfBoard(Long bno, Pageable pageable);       // JPQL에서 쿼리에 파라미터를 사용할 때는 쿼리 문자열 안에 :를 사용하여 파라미터를 표시.
}
