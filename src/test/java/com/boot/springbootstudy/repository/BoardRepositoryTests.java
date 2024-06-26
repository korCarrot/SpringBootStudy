package com.boot.springbootstudy.repository;

import com.boot.springbootstudy.domain.Board;
import com.boot.springbootstudy.domain.BoardImage;
import com.boot.springbootstudy.dto.BoardListAllDTO;
import com.boot.springbootstudy.dto.BoardListReplyCountDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

//Spring이나 Spring Boot에서 테스트 메소드의 리턴 타입은 꼭 void일 필요는 없지만, 테스트의 명확성과 프레임워크의 기대에 맞추기 위해 void를 사용하는 것이 일반적입니다.
@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    // BaseEntity를 상속한 Board클래스(테이블)는 BoardRepository인터페이스에서 상속하는 JpaRepository의 제네릭으로 설정되어있음.
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

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
//   Pageable 객체는 페이지네이션 정보를 포함하고 있습니다. 이 정보는 현재 페이지 번호, 페이지당 항목 수, 정렬 정보 등을 포함
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

    @Test
    public void getTime(){
        String time =boardRepository.getTime();
        log.info("JPQL테스트: " + time);
    }

// BoardSearch인터페이스와 연동한 상태에서 boardRepository 실행 가능.
    @Test
    public void testSearch1(){

        //2page order by bno desc
        Pageable pageable=PageRequest.of(1,10,Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }

// 검색 조건들 설정 후 키워드 검색
    @Test
    public void testSearchAll(){

        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        //result = 데이터 목록 + 페이지 정보 + 전체 개수
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        //total pages
        log.info("total pages: " + result.getTotalPages());

        //page size
        log.info("page size: " + result.getSize());

        //page number
        log.info("page number: " + result.getNumber());

        //prev : next
        log.info("이전 페이지: " + result.hasPrevious() + ": " + "다음 페이지: " + result.hasNext());

        result.getContent().forEach( board -> log.info(board));
    }


    //댓글 개수와 함께 게시글 검색
    @Test
    public void testSearchReplyCount(){

        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        //total pages
        log.info("total pages: " + result.getTotalPages());
        //page size
        log.info("page size: " + result.getSize());
        //pageNumber
        log.info("pageNumber: " + result.getNumber()); //result.getNumber()는 Page 객체의 메소드로, 현재 페이지 번호를 나타냅니다. 이 메소드는 현재 페이지의 인덱스를 반환하며, 반환되는 값은 0부터 시작합니다.
        //prev next
        log.info("result.hasPrevious(): result.hasNext() : " + result.hasPrevious() +": " + result.hasNext());

        result.getContent().forEach(log::info);
    }

    @Test
    public void testInsertWithImages(){

        Board board = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for (int i = 0; i < 3; i++) {

            board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
        } //end for

        boardRepository.save(board);
    }


    @Test
//    @Transactional //여러번 쿼리를 실행하기 위함 (board객체를 갖고와서 DB연결이 끊어지기 때문)
    public void testReadWithImages(){

        //반드시 존재하는 bno로 확인
//        Optional<Board> result = boardRepository.findById(1L);
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        log.info(board);
        log.info("----------");
//        log.info(board.getImageSet());

        for(BoardImage boardImage : board.getImageSet()){
            log.info(boardImage);
        }
    }

    @Test
    public void testModifyImages(){

        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        //기존의 첨부파일들은 삭제
        board.clearImages();

        //새로운 첨부파일들
        for (int i = 0; i < 2; i++) {

            board.addImage(UUID.randomUUID().toString(), "updatefile"+i+".jpg");
        }

        boardRepository.save(board);

    }

    @Test
    @Transactional  //기본적으로 트랜잭션 매니저가 모든 변경 사항을 트랜잭션 범위 내에서 커밋합니다. 그러나 @Transactional 어노테이션을 사용할 때 트랜잭션 경계를 정의할 수 있습니다. 이때 특정 메서드에서 변경을 수행한 후에 트랜잭션을 커밋하고 싶을 때 @Commit 어노테이션을 사용할 수 있습니다.
    @Commit //@Commit 어노테이션은 Spring에서 트랜잭션을 사용할 때 사용됩니다. 이 어노테이션은 트랜잭션이 완료되고 커밋되는 시점에 메서드 실행을 강제합니다.  // 일반적으로 예외 발생 시 롤백되도록 트랜잭션을 구성하는 것이 안전합니다.
    public void testRemoveAll(){

        Long bno = 1L;

        replyRepository.deleteByBoard_Bno(bno); //Reply 엔티티들을 삭제

        boardRepository.deleteById(bno);    //그리고 Board를 삭제

    }

    //더미 데이터 추가
    @Test
    public void testInsertAll(){

        for (int i = 0; i < 100; i++) {

            Board board = Board.builder()
                    .title("Title.."+i)
                    .content("Content.."+i)
                    .writer("Writer.."+i)
                    .build();

            //이미지 넣기
            for (int j = 0; j < 3; j++) {

                if (i % 5 == 0) {
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(), i + "file" + j + ".jpg");

            } //end for (j)

            boardRepository.save(board);

        } //end for (i)
    }


//    @Transactional
//    @Test
//    public void testSearchImageReplyCount(){
//
//        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
//
//        boardRepository.searchWithAll(null, null, pageable);
//    }


    @Transactional
    @Test
    public void testSearchImageReplyCount(){

        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());

//        boardRepository.searchWithAll(null, null, pageable);

        Page< BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);

        log.info("-------------------------------------------");
        log.info("result.getTotalElements(): "+result.getTotalElements());

        result.getContent().forEach(log::info);
    }
}

/*
Spring Data JPA에서 페이지네이션을 처리할 때, Page 객체는 여러 유용한 정보를 제공합니다. 여기에는 현재 페이지, 전체 페이지 수, 현재 페이지의 데이터 리스트, 전체 데이터 수 등이 포함됩니다. Page 객체의 주요 메소드들 중 일부는 다음과 같습니다:

getTotalElements(): 전체 데이터 수를 반환합니다.
getTotalPages(): 전체 페이지 수를 반환합니다.
getSize(): 페이지 당 데이터 수를 반환합니다.
getNumber(): 현재 페이지 번호를 반환합니다.
getContent(): 현재 페이지의 데이터를 List 형태로 반환합니다.
hasPrevious(): 이전 페이지가 있는지 여부를 반환합니다.
hasNext(): 다음 페이지가 있는지 여부를 반환합니다.

 */