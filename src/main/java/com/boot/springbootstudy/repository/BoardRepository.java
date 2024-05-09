package com.boot.springbootstudy.repository;

import com.boot.springbootstudy.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//JpaRepository는 다양한 기본 메서드들을 제공
//save(S entity): 엔티티를 저장하거나 업데이트합니다.           //deleteById(ID id): 주어진 ID에 해당하는 엔티티를 삭제합니다.
//findById(ID id): 주어진 ID에 해당하는 엔티티를 조회합니다.    //findAll(): 모든 엔티티를 조회합니다.
//count(): 엔티티의 총 개수를 반환합니다.    //이 외에도 다양한 쿼리 메서드를 제공하여 사용자가 데이터베이스 쿼리를 직접 작성하지 않고도 엔티티를 검색할 수 있습니다.

//JpaRepository: JPA(Java Persistence API)를 사용하여 데이터베이스와 상호 작용할 때 흔히 수행하는 CRUD(Create, Read, Update, Delete) 작업을 지원하는 인터페이스
//Board: JpaRepository가 조작할 엔티티 클래스입니다. 보통 데이터베이스 테이블과 매핑되는 엔티티 클래스입니다. 이 경우에는 Board 엔티티에 대한 CRUD(Create, Read, Update, Delete) 작업을 수행할 것입니다.
//Long: 엔티티 클래스의 기본 키 타입을 지정합니다. 대부분의 경우, 데이터베이스의 테이블은 기본 키로 사용되는 일련번호(auto-increment) 또는 유니크한 값이 필요하므로, 보통은 기본 키 타입으로 Long을 사용합니다. 여기서 Long은 일련번호(auto-increment) 형태로 생성되는 기본 키를 의미합니다.
public interface BoardRepository extends JpaRepository<Board, Long> {

// * 쿼리메소드 : 제목에 특정한 키워드가 존재하는 게시글들을 bno의 역순으로 정렬해서 가져오고 싶은 경우 예시
//    Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);

//  * @Query를 사용한 JPQL
// :keyword는 JPQL의 이름 매개변수입니다. 이는 메서드 파라미터로부터 바인딩된다고 가정됩니다. 따라서 메서드를 호출할 때 해당 매개변수에 값을 전달하여 쿼리를 실행할 수 있습니다.
    @Query("select b from Board b where b.title like concat('%', :keyword, '%') ")
    Page<Board> findKeyword(String keyword, Pageable pageable);

//  네이티브 쿼리는 직접 SQL을 사용하여 데이터베이스에 쿼리를 전달하는 방식
//  value: 실행할 SQL 쿼리를 지정   nativeQuery: 네이티브 쿼리를 실행할지 여부를 지정
    @Query(value = "select now()", nativeQuery = true)
    String getTime();
}
