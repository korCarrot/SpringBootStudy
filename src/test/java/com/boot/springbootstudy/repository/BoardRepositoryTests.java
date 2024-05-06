package com.boot.springbootstudy.repository;

import com.boot.springbootstudy.domain.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    // BaseEntity를 상속한 Board클래스(테이블)는 BoardRepository인터페이스에서 상속하는 JpaRepository의 제네릭으로 설정되어있음.
    @Autowired
    private BoardRepository boardRepository;

// insert, update 기능 - save()
    @Test   //테스트 코드에서 @ID값이 null이므로 insert만 실행됩니다.
//테스트 환경에서는 일반적으로 데이터베이스에 저장되는 식별자 값을 생성하기 위한 데이터베이스의 자동 증가(auto-increment) 기능을 사용하지 않기 때문
    public void testInsert(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Board board=Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer("user"+ (i % 10))
                    .build();
// save(S entity): 엔티티를 저장하거나 업데이트합니다.
// 영속 컨텍스트 내에 데이터를 찾아보고 해당 객체가 없으면 insert, 존재하면 update를 자동으로 실행
            Board result=boardRepository.save(board);   //save메소드로 insert 또는 update //데이터베이스에 저장된 데이터와 동기화된 Board객체가 반환
            log.info("BNO: " + result.getBno());
        });
    }

//  select 기능(게시물 조회 기능) - findByID()
    @Test
    public void testSelect(){
        Long bno=100L;

//      findById의 리턴 타입은 Optional<T>
//      Optional<T>은 T 타입의 객체를 감싸는 컨테이너로, 값이 존재할 수도 있고 존재하지 않을 수도 있는 상황을 표현합니다.
//      값이 존재하는 경우 해당 값을 감싼 Optional 객체를 반환하고, 값이 존재하지 않는 경우에는 빈 Optional 객체를 반환합니다.
        Optional<Board> result = boardRepository.findById(bno);

//      orElseThrow() 메서드는 Optional 객체가 비어있을 때, 즉 값이 존재하지 않을 때 예외를 발생시키는 메서드입니다.
//      이 메서드는 예외를 직접 정의하여 처리할 수도 있습니다. String result = result.orElseThrow(() -> new IllegalArgumentException("Value is not present"));
        Board board = result.orElseThrow();

        log.info(board);
    }


//    update 기능(제목/내용 수정)   *mod_date 수정 됨
//    select문이 2번 나오는 이유 - 1.findByID()  2. save()-업데이트 전 데이터 검색
    @Test
    public void testUpdate(){
        Long bno=100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        board.change("update..title 100", "update content 100");

        boardRepository.save(board);
    }


//  삭제 기능   (update와 마찬가지로 데이터 확인 후 실행)
    @Test
    public void testDelete(){
        Long bno = 1L;
        boardRepository.deleteById(bno);
    }


//   페이징 처리 (Pageable, Page<E> 타입)
//   PageRequest.of(페이지 번호, 사이즈):       페이지 번호는 0부터
//   PageRequest.of(페이지 번호, 사이즈, sort):         정렬 조건 추가
//   PageRequest.of(페이지 번호, 사이즈, sort.direction, 속성...): 정렬 방향과 여러 속성 지정
    @Test
    public void testPaging(){

        //1page order by bno desc
       Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        //파라미터로 Pageable을 사용 -> 리턴 타입 = Page<T>  /  페이징 처리에 데이터가 많은 경우 count 처리를 자동으로 실행
       Page<Board> result = boardRepository.findAll(pageable);

       log.info("total count: " + result.getTotalElements());
       log.info("total page: " + result.getTotalPages());
       log.info("page number: " + result.getNumber());
       log.info("page size: " + result.getSize());

       List<Board> todoList = result.getContent();

       todoList.forEach(board->{log.info(board);});

    }



//  데이터베이스 모든 정보 확인
    @Test
    public void testSelectAll(){
        List<Board> result = boardRepository.findAll();
        log.info(result);
    }
}
