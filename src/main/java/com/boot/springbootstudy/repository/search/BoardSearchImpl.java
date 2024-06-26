    package com.boot.springbootstudy.repository.search;

    import com.boot.springbootstudy.domain.Board;
    import com.boot.springbootstudy.domain.QBoard;
    import com.boot.springbootstudy.domain.QReply;
    import com.boot.springbootstudy.dto.BoardImageDTO;
    import com.boot.springbootstudy.dto.BoardListAllDTO;
    import com.boot.springbootstudy.dto.BoardListReplyCountDTO;
    import com.querydsl.core.BooleanBuilder;
    import com.querydsl.core.Tuple;
    import com.querydsl.core.types.Projections;
    import com.querydsl.jpa.JPQLQuery;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageImpl;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

    import java.util.List;
    import java.util.stream.Collectors;

    //QuerydslRepositorySupport는 스프링 데이터에서 제공하는 클래스로,
    // Java에서 타입 안전한 쿼리를 위한 프레임워크인 Querydsl을 사용하는 리포지토리 구현을 위한 기본 클래스 역할을 합니다.
    // QuerydslRepositorySupport를 확장함으로써, 보일러플레이트 코드를 많이 작성할 필요 없이 Querydsl 쿼리를 지원하는 리포지토리 클래스를 쉽게 만들 수 있습니다.
    public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

        public BoardSearchImpl() {
            super(Board.class);
        }

//      조회
        @Override
        public Page<Board> search1(Pageable pageable) {

//          QBoard board = QBoard.board; : Querydsl은 코드 생성 도구를 사용하여 엔티티 클래스에 대응하는 Querydsl 타입을 생성합니다.
//          QBoard는 이런 Querydsl 타입을 나타냅니다. 이 줄은 QBoard의 인스턴스를 생성하고, 이 인스턴스를 이용하여 Querydsl 쿼리를 작성할 때 사용합니다.
            QBoard board = QBoard.board; //Q도메인 객체

//          JPQLQuery<Board> query = from(board); : from 메소드는 Querydsl의 시작점을 정의합니다.
//          이것은 JPQL 쿼리를 생성하는 데 사용됩니다. 여기서는 board 엔티티를 기준으로 쿼리를 작성합니다. 이 쿼리는 Board 엔티티를 조회하는 것을 나타냅니다.
//          *query: JPQLQuery 또는 SQLQuery와 같은 Querydsl의 쿼리 객체
            JPQLQuery<Board> query = from(board); //select.. from board

//          BooleanBuilder는 Querydsl에서 동적으로 조건을 추가할 수 있는 빌더 클래스입니다.
//          여기서는 booleanBuilder 인스턴스를 생성하여 여러 개의 조건을 OR 연산자로 결합
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            booleanBuilder.or(board.title.contains("11")); // title LIKE '%11%

            booleanBuilder.or(board.content.contains("11")); // content LIKE '%11%'

//          query.where(board.title.contains("1")); : where 메소드는 쿼리에 조건을 추가하는 데 사용됩니다.
//          여기서는 title 필드가 "1"을 포함하는 경우에 대한 조건을 추가했습니다. 이것은 "title LIKE '%1%'"에 해당하는 SQL 쿼리를 생성합니다.
//            query.where(board.title.contains("1")); //where title like...

            query.where(booleanBuilder); //query 객체에 booleanBuilder를 적용하여 동적으로 생성된 조건을 추가
            query.where(board.bno.gt(0L)); //board의 bno 필드가 0보다 큰 경우에 대한 조건을 추가합니다. 이것은 "bno > 0"에 해당하는 SQL 쿼리

            //getQuerydsl() : 현재 리포지토리에 대한 Querydsl 지원을 제공하는 Querydsl 객체를 반환. Querydsl 객체를 사용하여 쿼리에 페이징이나 정렬을 적용하거나, 특정한 쿼리 빌더를 생성할 때 활용.
            //applyPagination() : Querydsl을 사용하여 쿼리를 페이징하는 데 사용되는 메소드입니다. 이 메소드는 주어진 페이지 정보(Pageable 객체)를 기반으로 쿼리 결과를 페이징하여 가져옵니다.
            //applyPagination 메소드를 사용하여 쿼리 결과를 페이징할 수 있으며, 페이징된 결과를 Page 객체로 반환하여 처리 가능
            this.getQuerydsl().applyPagination(pageable, query);    //JPQLQuery 객체

//          query.fetch() : 이 메소드는 쿼리를 실행하여 해당하는 결과를 가져옵니다.
//          여기서는 Board 엔티티를 조회하는 쿼리를 실행하고, 결과를 List<Board> 형태로 가져옵니다.
            List<Board> list = query.fetch();

//          query.fetchCount(): 이 메소드는 쿼리를 실행하여 결과의 개수를 가져옵니다. 여기서는 Board 엔티티를 조회하는 쿼리를 실행하고, 결과의 개수를 반환합니다.
            long count =query.fetchCount();

            return null;
        }

//      검색조건 조회
        @Override
        public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

            QBoard board = QBoard.board;
            JPQLQuery<Board> query = from(board);

            //검색 조건과 키워드가 있다면
            if ( (types != null && types.length > 0) && keyword != null ) {

                BooleanBuilder booleanBuilder = new BooleanBuilder();

                //검색 조건별로(제목, 내용, 작성자) SQL 조건문에 '키워드' 설정
                for(String type : types){

                    switch (type){
                        case "t" :
                            booleanBuilder.or(board.title.contains(keyword));
                            break;
                        case "c" :
                            booleanBuilder.or(board.content.contains(keyword));
                            break;
                        case "w" :
                            booleanBuilder.or(board.writer.contains(keyword));
                            break;
                    }   //end switch
                }   //end for   (booleanBuilder에 조건들이 들어간 상태)

                query.where(booleanBuilder);
            }   //if

            //bno > 0
            query.where(board.bno.gt(0L));

            //paging
            this.getQuerydsl().applyPagination(pageable, query);

            List<Board> list = query.fetch(); //실제 목록 데이터

            long count = query.fetchCount(); // 전체 개수

            //페이징 처리의 최종 결과는 Page<T> 타입을 반환.
            //Spring Data JPA에서 PageImpl 클래스 제공. 3개의 파라미터로 Page<T> 생성.
            return new PageImpl<Board>(list, pageable, count);
        }

        //댓글 개수와 함께 게시글 검색
        @Override
        public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);

        //board를 기준으로 reply와 왼쪽 조인을 수행
        //on : 조인 조건을 설정    /   reply 엔티티의 board 속성과 board 엔티티가 같은 값을 가질 때 조인
        query.leftJoin(reply).on(reply.board.eq(board));

        query.groupBy(board);   //결과를 게시물별로 그룹화

            if ( (types != null && types.length > 0) && keyword != null) {

                BooleanBuilder booleanBuilder = new BooleanBuilder();

                for (String type : types) {

                    switch (type) {

                        case "t":
                            booleanBuilder.or(board.title.contains(keyword));
                            break;

                        case "c":
                            booleanBuilder.or(board.content.contains(keyword));
                            break;

                        case "w":
                            booleanBuilder.or(board.writer.contains(keyword));
                            break;
                    }
                } //end for
                query.where(booleanBuilder);
            }

            //bno > 0
            query.where(board.bno.gt(0L));


            //이 코드에서 Projections.bean 메서드는 BoardListReplyCountDTO 클래스의 인스턴스를 생성
            //Projections는 주로 쿼리 DSL(Query DSL) 라이브러리에서 사용되는 기능
            //JPQL(Java Persistence Query Language) 쿼리에서 DTO(Data Transfer Object)로 결과를 매핑하는 데 사용
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class,
                                        board.bno,
                                        board.title,
                                        board.writer,
                                        board.regDate,                       //reply.count()는 QReply 객체의 인스턴스를 통해 count() 메소드를 호출.
                                        reply.count().as("replyCount") //조인된 reply 엔티티의 개수를 세어 replyCount라는 이름으로 반환
                                        ));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
        }

//        @Override
//        public Page<BoardListReplyCountDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
//
//            QBoard board = QBoard.board;
//            QReply reply = QReply.reply;
//
//            JPQLQuery<Board> boardJPQLQuery = from(board);
//            boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));      //left join
//
//            getQuerydsl().applyPagination(pageable, boardJPQLQuery);    //paging
//
//            List<Board> boardList = boardJPQLQuery.fetch();
//
//            boardList.forEach(board1 -> {
//                System.out.println(board1.getBno());        //paging이 적용되어 10개의 bno가 나온다.
//                System.out.println(board1.getImageSet());   //@BatchSize가 적용된다.
//                System.out.println("-------------------");
//            });
//
//            return null;
//        }


        //게시글의 이미지와 댓글의 숫자까지 처리
        @Override
        public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

            QBoard board = QBoard.board;
            QReply reply = QReply.reply;

            JPQLQuery<Board> boardJPQLQuery = from(board);
            boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));      //left join

            if ((types != null && types.length > 0) && keyword != null) {

                BooleanBuilder booleanBuilder = new BooleanBuilder();

                for (String type : types) {

                    switch (type){
                        case "t" :
                                   booleanBuilder.or(board.title.contains(keyword));
                                   break;
                        case "c" :
                                   booleanBuilder.or(board.content.contains(keyword));
                                   break;

                        case "w" :
                                   booleanBuilder.or(board.writer.contains(keyword));
                                   break;
                    }
                }
                boardJPQLQuery.where(booleanBuilder);
            }

            boardJPQLQuery.groupBy(board);  // QueryDSL을 사용하여 board 엔티티를 기준으로 그룹화(group by)하는 부분

            getQuerydsl().applyPagination(pageable, boardJPQLQuery);    //paging

            // QueryDSL을 사용하여 JPQL 쿼리의 결과를 Tuple 형태로 선택 (행 - SQL에서 여러 열을 선택할 때와 유사)
            //reply.countDistinct(): 각 board 엔티티에 대한 고유한 댓글 수를 셉니다. 이는 각 board에 연결된 reply의 고유 개수를 의미
            JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());

            List<Tuple> tupleList = tupleJPQLQuery.fetch();

            //***stream()의 map()은 내부에서 반복 처리가 가능하다!!!!!!!!!!!!!!
            List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {

                Board board1 = tuple.get(board);    //Board board1 = tuple.get(board); : tuple에서 board 엔티티를 가져옵니다. 이는 쿼리에서 선택한 Board 엔티티 전체를 의미합니다.
                long replyCount = tuple.get(1, Long.class); //long replyCount = tuple.get(1, Long.class); : tuple에서 인덱스 1에 해당하는 댓글 수를 Long 타입으로 가져옵니다. 1은 쿼리에서 두 번째로 선택된 항목(reply.countDistinct())을 의미합니다. //get(int index, Class<T> type). index: 추출하려는 값의 인덱스입니다. type: 추출하려는 값의 데이터 타입입니다.
                //이 과정은 스트림의 각 요소(Tuple)에 대해 반복됩니다. 즉, tupleList의 각 Tuple 객체에 대해 위의 추출 작업이 수행되고, 이를 기반으로 BoardListAllDTO 객체가 생성됩니다.


                //BoardListAllDTO.builder()와 같은 변환 과정이 반복되는 것은 스트림 API의 map 메서드를 통해 이루어집니다. 이 과정에서 내부적으로 반복이 수행됩니다.
                BoardListAllDTO dto = BoardListAllDTO.builder()
                        .bno(board1.getBno())
                        .title(board1.getTitle())
                        .writer(board1.getWriter())
                        .regDate(board1.getRegDate())
                        .replyCount(replyCount)
                        .build();

                //BoardImage를 BoardImageDTO 처리할 부분
                List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted().map(boardImage ->        //sorted() 메서드는 스트림의 요소를 정렬하는 데 사용됩니다. 정렬 기준은 요소의 자연 순서(natural order)에 따라 이루어집니다. 요소의 자연 순서에 따라 오름차순으로 정렬합니다.
                    BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build()
                ).collect(Collectors.toList());

                dto.setBoardImages(imageDTOS);  //처리된 BoardImageDTO들을 추가

                return dto;
            }).collect(Collectors.toList());    //스트림에서 처리한 결과를 최종적으로 수집하는 역할

            long totalCount = boardJPQLQuery.fetchCount();

            return new PageImpl<>(dtoList, pageable, totalCount);
            }


        //Inner Join : 교집합
        //left join : 왼쪽 기준 전부 select (오른쪽에 없는건 null로 표시)       /     교집합 없이 왼쪽만 보려면 where B.ID(조인한 공통점이라 할 때) IS NULL
        //right join : (left join과 동일. where조건에서 왼쪽에 있는 테이블로만 바꾸면 됨.)
        //Outer join : 합집합      /       교집합 부분 제외하고 싶으면 where A.ID IS NULL OR B.ID IS NULL
    }
